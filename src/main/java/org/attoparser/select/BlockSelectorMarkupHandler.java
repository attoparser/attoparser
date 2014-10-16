/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2014, The ATTOPARSER team (http://www.attoparser.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.attoparser.select;

import java.util.Arrays;
import java.util.List;

import org.attoparser.AbstractMarkupHandler;
import org.attoparser.IMarkupHandler;
import org.attoparser.ParseException;
import org.attoparser.ParseStatus;
import org.attoparser.config.ParseConfiguration;

/**
 * <p>
 *   Implementation of {@link org.attoparser.IMarkupHandler} able to apply <em>block selection</em> based on a set
 *   of specified <em>markup selectors</em> (see {@link org.attoparser.select}).
 * </p>
 * <p>
 *   <em>Block selection</em> means that once an element is selected by any of the specified selectors, the entire
 *   subtree of that element is also considered <em>selected</em>.
 * </p>
 * <p>
 *   This markup handler will apply the specified <strong>markup selectors</strong> and divide parsing events
 *   between two (possibly different) chained markup handlers implementing {@link org.attoparser.IMarkupHandler},
 *   the <tt>selectedHandler</tt> and the <tt>nonSelectedHandler</tt>. Also, document start/end events will be
 *   sent to the <tt>selectedHandler</tt> unless specified otherwise by means of the
 *   {@link #setDocumentStartEndHandler(org.attoparser.IMarkupHandler)} method.
 * </p>
 * <p>
 *   For example, given the following markup:
 * </p>
 * <pre><code>
 * &lt;!DOCTYPE html&gt;
 * &lt;html&gt;
 *   &lt;body&gt;
 *     &lt;h1&gt;AttoParser&lt;/h1&gt;
 *     &lt;div class="content"&gt;
 *       AttoParser is able to select &lt;strong&gt;just a fragment of markup&lt;/strong&gt;.
 *     &lt;/div&gt;
 *   &lt;/body&gt;
 * &lt;/html&gt;
 * </code></pre>
 * <p>
 *   ...and a {@link org.attoparser.select.BlockSelectorMarkupHandler} initialized with:
 * </p>
 * <ul>
 *   <li>An {@link org.attoparser.output.OutputMarkupHandler} object as <tt>selectedHandler</tt>.</li>
 *   <li>A {@link org.attoparser.discard.DiscardMarkupHandler} object as <tt>nonSelectedHandler</tt>.</li>
 * </ul>
 * <p>
 *   Using selector <tt>"div.content"</tt>, we would get:
 * </p>
 * <pre><code>
 * &lt;div class="content"&gt;
 *       AttoParser is able to select &lt;strong&gt;just a fragment of markup&lt;/strong&gt;.
 *     &lt;/div&gt;
 * </code></pre>
 * <p>
 *   If we had two selectors, <tt>"h1"</tt> and <tt>"div//text()"</tt>, we would get:
 * </p>
 * <pre><code>
 * &lt;h1&gt;AttoParser&lt;/h1&gt;
 *       AttoParser is able to select just a fragment of markup.
 *
 * </code></pre>
 * <p>
 *   Have a look at the package info for {@link org.attoparser.select} for a complete explanation on
 *   <em>markup selector</em> syntax.
 * </p>
 * <p>
 *   Note that, as with most handlers, this class is <strong>not thread-safe</strong>. Also, instances of this class
 *   should not be reused across parsing operations.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 * 
 */
public final class BlockSelectorMarkupHandler extends AbstractMarkupHandler {


    private final IMarkupHandler selectedHandler;
    private final IMarkupHandler nonSelectedHandler;
    private final ISelectionAwareMarkupHandler selectionAwareSelectedMarkupHandler; // just an (optional) cast of 'selectedHandler'

    private final IMarkupSelectorReferenceResolver referenceResolver;

    private final SelectorElementBuffer elementBuffer;

    // By default, "documentStart" and "documentEnd" events will be sent to selectedHandler.
    private IMarkupHandler documentStartEndHandler;

    private final int selectorsLen;
    private final String[] selectors;
    private final boolean[] selectorMatches;
    private final MarkupSelectorFilter[] selectorFilters;

    private boolean insideAllSelectorMatchingBlock;
    private boolean someSelectorsMatch;

    private int markupLevel;
    private int[] matchingMarkupLevelsPerSelector;

    private static final int MARKUP_BLOCKS_LEN = 10;
    private int[] markupBlocks;
    private int markupBlockIndex;






    public BlockSelectorMarkupHandler(final IMarkupHandler selectedHandler,
                                      final IMarkupHandler nonSelectedHandler,
                                      final String[] selectors) {
        this(selectedHandler, nonSelectedHandler, selectors, null);
    }



    public BlockSelectorMarkupHandler(final IMarkupHandler selectedHandler,
                                      final IMarkupHandler nonSelectedHandler,
                                      final String[] selectors,
                                      final IMarkupSelectorReferenceResolver referenceResolver) {

        super();

        // Both markup handlers (selected and non-selected CAN be null

        if (selectors == null || selectors.length == 0) {
            throw new IllegalArgumentException("Selector array cannot be null or empty");
        }
        for (final String selector : selectors) {
            if (selector == null || selector.trim().length() == 0) {
                throw new IllegalArgumentException(
                        "Selector array contains at least one null or empty item, which is forbidden");
            }
        }

        this.selectedHandler = selectedHandler;
        this.nonSelectedHandler = nonSelectedHandler;

        // By default, send the "document start" and "document end" events to the selected handler
        this.documentStartEndHandler = this.selectedHandler;

        this.referenceResolver = referenceResolver;

        // We'll just use this to avoid a lot of casts
        this.selectionAwareSelectedMarkupHandler =
                (this.selectedHandler instanceof ISelectionAwareMarkupHandler ?
                    (ISelectionAwareMarkupHandler) this.selectedHandler : null);

        this.selectors = selectors;
        this.selectorsLen = selectors.length;

        // Note this variable is defined basically in order to be reused in different events, but will not be dealt with as "state"
        this.selectorMatches = new boolean[this.selectors.length];
        Arrays.fill(this.selectorMatches, false);

        // Note this variable is defined basically in order to be reused in different events, but will not be dealt with as "state"
        this.someSelectorsMatch = false;

        this.insideAllSelectorMatchingBlock = false;

        this.selectorFilters = new MarkupSelectorFilter[this.selectorsLen];
        // We will not initialize selectorFilters here, but when we receive the configuration (setParseConfiguration)

        this.elementBuffer = new SelectorElementBuffer();

        this.markupLevel = 0;
        this.matchingMarkupLevelsPerSelector = new int[this.selectorsLen];
        Arrays.fill(this.matchingMarkupLevelsPerSelector, Integer.MAX_VALUE);

        this.markupBlockIndex = 0;
        this.markupBlocks = new int[MARKUP_BLOCKS_LEN];
        this.markupBlocks[this.markupLevel] = this.markupBlockIndex;

    }




    /**
     * <p>
     *   Sets the {@link org.attoparser.IMarkupHandler} instance to which the <em>document start</em> and
     *   <em>document end</em> events should be delegated.
     * </p>
     * <p>
     *   This is specified separately in order to avoid undesired event duplicities. By default, the
     *   <em>selected handler</em> specified during construction will be the one receiving these events.
     * </p>
     * <p>
     *   If sending these events to both selected and non-selected handlers is required, developers can make use of
     *   {@link org.attoparser.duplicate.DuplicateMarkupHandler}.
     * </p>
     *
     * @param documentStartEndHandler the handler these events will be delegated to.
     */
    public void setDocumentStartEndHandler(final IMarkupHandler documentStartEndHandler) {
        if (documentStartEndHandler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        this.documentStartEndHandler = documentStartEndHandler;
    }







    @Override
    public void setParseConfiguration(final ParseConfiguration parseConfiguration) {

        /*
         * We will use the parsing mode from the configuration to initialize the parsing filters
         */

        final boolean html =
                ParseConfiguration.ParsingMode.HTML.equals(parseConfiguration.getMode());

        for (int i = 0; i < this.selectorsLen; i++) {

            final List<IMarkupSelectorItem> selectorItems =
                    MarkupSelectorItems.forSelector(html, this.selectors[i], this.referenceResolver);

            this.selectorFilters[i] = new MarkupSelectorFilter(null, selectorItems.get(0));
            MarkupSelectorFilter last = this.selectorFilters[i];
            for (int j = 1; j < selectorItems.size(); j++) {
                last = new MarkupSelectorFilter(last, selectorItems.get(j));
            }

        }


        /*
         * Now delegate to the selected/non-selected handlers
         */

        this.selectedHandler.setParseConfiguration(parseConfiguration);
        this.nonSelectedHandler.setParseConfiguration(parseConfiguration);

    }




    @Override
    public void setParseStatus(final ParseStatus status) {
        this.selectedHandler.setParseStatus(status);
        this.nonSelectedHandler.setParseStatus(status);
    }




    /*
     * ---------------
     * Document events
     * ---------------
     */

    @Override
    public void handleDocumentStart(
            final long startTimeNanos, final int line, final int col)
            throws ParseException {

        if (this.selectionAwareSelectedMarkupHandler != null) {
            this.selectionAwareSelectedMarkupHandler.setSelectors(this.selectors);
        }

        this.documentStartEndHandler.handleDocumentStart(startTimeNanos, line, col);

    }


    @Override
    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws ParseException {

        this.documentStartEndHandler.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);

    }






/*
     * ------------------------
     * XML Declaration events
     * ------------------------
     */

    @Override
    public void handleXmlDeclaration(
            final char[] buffer,
            final int keywordOffset, final int keywordLen, final int keywordLine, final int keywordCol,
            final int versionOffset, final int versionLen, final int versionLine, final int versionCol,
            final int encodingOffset, final int encodingLen, final int encodingLine, final int encodingCol,
            final int standaloneOffset, final int standaloneLen, final int standaloneLine, final int standaloneCol,
            final int outerOffset, final int outerLen, final int line, final int col) throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {

                if (this.matchingMarkupLevelsPerSelector[i] > this.markupLevel) {
                    this.selectorMatches[i] =
                            this.selectorFilters[i].matchXmlDeclaration(true, this.markupLevel, this.markupBlocks[this.markupLevel]);
                    if (this.selectorMatches[i]) {
                        this.someSelectorsMatch = true;
                    }
                } else {
                    this.selectorMatches[i] = true;
                    this.someSelectorsMatch = true;
                }

            }

            if (this.someSelectorsMatch) {
                markCurrentSelection();
                this.selectedHandler.handleXmlDeclaration(
                        buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                        versionOffset, versionLen, versionLine, versionCol,
                        encodingOffset, encodingLen, encodingLine, encodingCol,
                        standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                        outerOffset, outerLen, line, col);
                unmarkCurrentSelection();
                return;
            }

            this.nonSelectedHandler.handleXmlDeclaration(
                    buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                    versionOffset, versionLen, versionLine, versionCol,
                    encodingOffset, encodingLen, encodingLine, encodingCol,
                    standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                    outerOffset, outerLen, line, col);
            return;

        }

        markCurrentSelection();
        this.selectedHandler.handleXmlDeclaration(
                buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol,
                encodingOffset, encodingLen, encodingLine, encodingCol,
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen, line, col);
        unmarkCurrentSelection();

    }





    /*
     * ---------------------
     * DOCTYPE Clause events
     * ---------------------
     */

    @Override
    public void handleDocType(
            final char[] buffer,
            final int keywordOffset, final int keywordLen, final int keywordLine, final int keywordCol,
            final int elementNameOffset, final int elementNameLen, final int elementNameLine, final int elementNameCol,
            final int typeOffset, final int typeLen, final int typeLine, final int typeCol,
            final int publicIdOffset, final int publicIdLen, final int publicIdLine, final int publicIdCol,
            final int systemIdOffset, final int systemIdLen, final int systemIdLine, final int systemIdCol,
            final int internalSubsetOffset, final int internalSubsetLen, final int internalSubsetLine, final int internalSubsetCol,
            final int outerOffset, final int outerLen, final int outerLine, final int outerCol)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {

                if (this.matchingMarkupLevelsPerSelector[i] > this.markupLevel) {
                    this.selectorMatches[i] =
                            this.selectorFilters[i].matchDocTypeClause(true, this.markupLevel, this.markupBlocks[this.markupLevel]);
                    if (this.selectorMatches[i]) {
                        this.someSelectorsMatch = true;
                    }
                } else {
                    this.selectorMatches[i] = true;
                    this.someSelectorsMatch = true;
                }

            }

            if (this.someSelectorsMatch) {
                markCurrentSelection();
                this.selectedHandler.handleDocType(
                        buffer,
                        keywordOffset, keywordLen, keywordLine, keywordCol,
                        elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                        typeOffset, typeLen, typeLine, typeCol,
                        publicIdOffset, publicIdLen, publicIdLine, publicIdCol,
                        systemIdOffset, systemIdLen, systemIdLine, systemIdCol,
                        internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol,
                        outerOffset, outerLen, outerLine, outerCol);
                unmarkCurrentSelection();
                return;
            }

            this.nonSelectedHandler.handleDocType(
                    buffer,
                    keywordOffset, keywordLen, keywordLine, keywordCol,
                    elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                    typeOffset, typeLen, typeLine, typeCol,
                    publicIdOffset, publicIdLen, publicIdLine, publicIdCol,
                    systemIdOffset, systemIdLen, systemIdLine, systemIdCol,
                    internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol,
                    outerOffset, outerLen, outerLine, outerCol);
            return;

        }

        markCurrentSelection();
        this.selectedHandler.handleDocType(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                typeOffset, typeLen, typeLine, typeCol,
                publicIdOffset, publicIdLen, publicIdLine, publicIdCol,
                systemIdOffset, systemIdLen, systemIdLine, systemIdCol,
                internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol,
                outerOffset, outerLen, outerLine, outerCol);
        unmarkCurrentSelection();

    }





    /*
     * --------------------
     * CDATA Section events
     * --------------------
     */

    @Override
    public void handleCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen, final int outerOffset,
            final int outerLen, final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {

                if (this.matchingMarkupLevelsPerSelector[i] > this.markupLevel) {
                    this.selectorMatches[i] =
                            this.selectorFilters[i].matchCDATASection(true, this.markupLevel, this.markupBlocks[this.markupLevel]);
                    if (this.selectorMatches[i]) {
                        this.someSelectorsMatch = true;
                    }
                } else {
                    this.selectorMatches[i] = true;
                    this.someSelectorsMatch = true;
                }

            }

            if (this.someSelectorsMatch) {
                markCurrentSelection();
                this.selectedHandler.handleCDATASection(
                        buffer, contentOffset, contentLen,
                        outerOffset, outerLen, line, col);
                unmarkCurrentSelection();
                return;
            }

            this.nonSelectedHandler.handleCDATASection(
                    buffer, contentOffset, contentLen,
                    outerOffset, outerLen, line, col);
            return;

        }

        markCurrentSelection();
        this.selectedHandler.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        unmarkCurrentSelection();

    }





    /*
     * -----------
     * Text events
     * -----------
     */

    @Override
    public void handleText(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {

                if (this.matchingMarkupLevelsPerSelector[i] > this.markupLevel) {
                    this.selectorMatches[i] =
                            this.selectorFilters[i].matchText(true, this.markupLevel, this.markupBlocks[this.markupLevel]);
                    if (this.selectorMatches[i]) {
                        this.someSelectorsMatch = true;
                    }
                } else {
                    this.selectorMatches[i] = true;
                    this.someSelectorsMatch = true;
                }

            }

            if (this.someSelectorsMatch) {
                markCurrentSelection();
                this.selectedHandler.handleText(buffer, offset, len, line, col);
                unmarkCurrentSelection();
                return;
            }

            this.nonSelectedHandler.handleText(buffer, offset, len, line, col);
            return;

        }

        markCurrentSelection();
        this.selectedHandler.handleText(buffer, offset, len, line, col);
        unmarkCurrentSelection();

    }





    /*
     * --------------
     * Comment events
     * --------------
     */

    @Override
    public void handleComment(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {

                if (this.matchingMarkupLevelsPerSelector[i] > this.markupLevel) {
                    this.selectorMatches[i] =
                            this.selectorFilters[i].matchComment(true, this.markupLevel, this.markupBlocks[this.markupLevel]);
                    if (this.selectorMatches[i]) {
                        this.someSelectorsMatch = true;
                    }
                } else {
                    this.selectorMatches[i] = true;
                    this.someSelectorsMatch = true;
                }

            }

            if (this.someSelectorsMatch) {
                markCurrentSelection();
                this.selectedHandler.handleComment(
                        buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
                unmarkCurrentSelection();
                return;
            }

            this.nonSelectedHandler.handleComment(
                    buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
            return;

        }

        markCurrentSelection();
        this.selectedHandler.handleComment(
                buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        unmarkCurrentSelection();

    }




    /*
     * ----------------
     * Element handling
     * ----------------
     */

    @Override
    public void handleAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen, final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen, final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws ParseException {


        if (!this.insideAllSelectorMatchingBlock) {
            // We are not in a matching block, so let's put this attribute into the buffer just in case it matches
            this.elementBuffer.bufferAttribute(
                    buffer,
                    nameOffset, nameLen, nameLine, nameCol,
                    operatorOffset, operatorLen, operatorLine, operatorCol,
                    valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);
            return;
        }


        this.selectedHandler.handleAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);

    }



    @Override
    public void handleStandaloneElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col)
            throws ParseException {


        if (!this.insideAllSelectorMatchingBlock) {
            // We are not in a matching block, so let's put this element into the buffer just in case it matches
            this.elementBuffer.bufferElementStart(buffer, nameOffset, nameLen, line, col, true, minimized);
            return;
        }

        markCurrentSelection();
        this.selectedHandler.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);

    }



    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.elementBuffer.bufferElementEnd(buffer, nameOffset, nameLen, line, col);

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {

                if (this.matchingMarkupLevelsPerSelector[i] > this.markupLevel) {
                    this.selectorMatches[i] =
                            this.selectorFilters[i].matchStandaloneElement(true, this.markupLevel, this.markupBlocks[this.markupLevel], this.elementBuffer);
                    if (this.selectorMatches[i]) {
                        this.someSelectorsMatch = true;
                    }
                } else {
                    this.selectorMatches[i] = true;
                    this.someSelectorsMatch = true;
                }

            }

            if (this.someSelectorsMatch) {
                markCurrentSelection();
                this.elementBuffer.flushBuffer(this.selectedHandler);
                unmarkCurrentSelection();
                return;
            }

            this.elementBuffer.flushBuffer(this.nonSelectedHandler);
            return;

        }

        this.selectedHandler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);
        unmarkCurrentSelection();

    }



    @Override
    public void handleOpenElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {
            // We are not in a matching block, so let's put this element into the buffer just in case it matches
            this.elementBuffer.bufferElementStart(buffer, nameOffset, nameLen, line, col, false, false);
            return;
        }

        markCurrentSelection();
        this.selectedHandler.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);

    }



    @Override
    public void handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.elementBuffer.bufferElementEnd(buffer, nameOffset, nameLen, line, col);

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {
                if (this.matchingMarkupLevelsPerSelector[i] > this.markupLevel) {
                    this.selectorMatches[i] =
                            this.selectorFilters[i].matchOpenElement(true, this.markupLevel, this.markupBlocks[this.markupLevel], this.elementBuffer);
                    if (this.selectorMatches[i]) {
                        this.someSelectorsMatch = true;
                        this.matchingMarkupLevelsPerSelector[i] = this.markupLevel;
                    }
                } else {
                    this.selectorMatches[i] = true;
                    this.someSelectorsMatch = true;
                }
            }

            if (this.someSelectorsMatch) {

                // Given we are opening a new markup level, we must update this flag (if required)
                updateInsideAllSelectorMatchingBlockFlag();

                this.markupLevel++;

                checkSizeOfMarkupBlocksStructure(this.markupLevel);
                this.markupBlocks[this.markupLevel] = ++this.markupBlockIndex;

                markCurrentSelection();
                this.elementBuffer.flushBuffer(this.selectedHandler);
                unmarkCurrentSelection();

                return;

            }

            this.markupLevel++;

            checkSizeOfMarkupBlocksStructure(this.markupLevel);
            this.markupBlocks[this.markupLevel] = ++this.markupBlockIndex;

            this.elementBuffer.flushBuffer(this.nonSelectedHandler);

            return;

        }

        this.markupLevel++;

        checkSizeOfMarkupBlocksStructure(this.markupLevel);
        this.markupBlocks[this.markupLevel] = ++this.markupBlockIndex;

        this.selectedHandler.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);
        unmarkCurrentSelection();

    }



    @Override
    public void handleCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.markupLevel--;
        for (int i = 0; i < this.selectorsLen; i++) {
            this.selectorFilters[i].removeMatchesForLevel(this.markupLevel);
        }

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {
                // We use the flags indicating past matches to recompute new ones
                this.selectorMatches[i] = this.matchingMarkupLevelsPerSelector[i] <= this.markupLevel;
                if (this.selectorMatches[i]) {
                    this.someSelectorsMatch = true;
                }
            }

            if (this.someSelectorsMatch) {
                markCurrentSelection();
                this.selectedHandler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);
                return;
            }

            this.nonSelectedHandler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);
            return;

        }

        markCurrentSelection();
        this.selectedHandler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }



    @Override
    public void handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {
                // We use the flags indicating past matches to recompute new ones
                this.selectorMatches[i] = this.matchingMarkupLevelsPerSelector[i] <= this.markupLevel;
                if (this.selectorMatches[i]) {
                    this.someSelectorsMatch = true;
                }
            }

            for (int i = 0; i < this.selectorsLen; i++) {
                if (this.matchingMarkupLevelsPerSelector[i] == this.markupLevel) {
                    this.insideAllSelectorMatchingBlock = false;
                    this.matchingMarkupLevelsPerSelector[i] = Integer.MAX_VALUE;
                }
            }

            if (this.someSelectorsMatch) {
                this.selectedHandler.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);
                unmarkCurrentSelection();
                return;
            }

            this.nonSelectedHandler.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);
            return;

        }

        for (int i = 0; i < this.selectorsLen; i++) {
            if (this.matchingMarkupLevelsPerSelector[i] == this.markupLevel) {
                this.insideAllSelectorMatchingBlock = false;
                this.matchingMarkupLevelsPerSelector[i] = Integer.MAX_VALUE;
            }
        }


        this.selectedHandler.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        unmarkCurrentSelection();

    }



    @Override
    public void handleAutoCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.markupLevel--;
        for (int i = 0; i < this.selectorsLen; i++) {
            this.selectorFilters[i].removeMatchesForLevel(this.markupLevel);
        }

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {
                // We use the flags indicating past matches to recompute new ones
                this.selectorMatches[i] = this.matchingMarkupLevelsPerSelector[i] <= this.markupLevel;
                if (this.selectorMatches[i]) {
                    this.someSelectorsMatch = true;
                }
            }

            if (this.someSelectorsMatch) {
                markCurrentSelection();
                this.selectedHandler.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);
                return;
            }

            this.nonSelectedHandler.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);
            return;

        }

        markCurrentSelection();
        this.selectedHandler.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }



    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {
                // We use the flags indicating past matches to recompute new ones
                this.selectorMatches[i] = this.matchingMarkupLevelsPerSelector[i] <= this.markupLevel;
                if (this.selectorMatches[i]) {
                    this.someSelectorsMatch = true;
                }
            }

            for (int i = 0; i < this.selectorsLen; i++) {
                if (this.matchingMarkupLevelsPerSelector[i] == this.markupLevel) {
                    this.insideAllSelectorMatchingBlock = false;
                    this.matchingMarkupLevelsPerSelector[i] = Integer.MAX_VALUE;
                }
            }

            if (this.someSelectorsMatch) {
                this.selectedHandler.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);
                unmarkCurrentSelection();
                return;
            }

            this.nonSelectedHandler.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);
            return;

        }

        for (int i = 0; i < this.selectorsLen; i++) {
            if (this.matchingMarkupLevelsPerSelector[i] == this.markupLevel) {
                this.insideAllSelectorMatchingBlock = false;
                this.matchingMarkupLevelsPerSelector[i] = Integer.MAX_VALUE;
            }
        }


        this.selectedHandler.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        unmarkCurrentSelection();

    }



    @Override
    public void handleUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {
                // We use the flags indicating past matches to recompute new ones
                this.selectorMatches[i] = this.matchingMarkupLevelsPerSelector[i] <= this.markupLevel;
                if (this.selectorMatches[i]) {
                    this.someSelectorsMatch = true;
                }
            }

            if (this.someSelectorsMatch) {
                markCurrentSelection();
                this.selectedHandler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);
                return;
            }

            this.nonSelectedHandler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);
            return;

        }

        markCurrentSelection();
        this.selectedHandler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }



    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {
                // We use the flags indicating past matches to recompute new ones
                this.selectorMatches[i] = this.matchingMarkupLevelsPerSelector[i] <= this.markupLevel;
                if (this.selectorMatches[i]) {
                    this.someSelectorsMatch = true;
                }
            }

            if (this.someSelectorsMatch) {
                this.selectedHandler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
                unmarkCurrentSelection();
                return;
            }

            this.nonSelectedHandler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
            return;

        }

        this.selectedHandler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        unmarkCurrentSelection();

    }




    @Override
    public void handleInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {
            // We are not in a matching block, so let's put this whitespace into the buffer just in case it matches
            this.elementBuffer.bufferElementInnerWhiteSpace(buffer, offset, len, line, col);
            return;
        }

        this.selectedHandler.handleInnerWhiteSpace(buffer, offset, len, line, col);

    }





    /*
     * -------------------------------
     * Processing Instruction handling
     * -------------------------------
     */

    @Override
    public void handleProcessingInstruction(
            final char[] buffer,
            final int targetOffset, final int targetLen, final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen, final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen, final int line, final int col)
            throws ParseException {

        if (!this.insideAllSelectorMatchingBlock) {

            this.someSelectorsMatch = false;
            for (int i = 0; i < this.selectorsLen; i++) {

                if (this.matchingMarkupLevelsPerSelector[i] > this.markupLevel) {
                    this.selectorMatches[i] =
                            this.selectorFilters[i].matchProcessingInstruction(true, this.markupLevel, this.markupBlocks[this.markupLevel]);
                    if (this.selectorMatches[i]) {
                        this.someSelectorsMatch = true;
                    }
                } else {
                    this.selectorMatches[i] = true;
                    this.someSelectorsMatch = true;
                }

            }

            if (this.someSelectorsMatch) {
                markCurrentSelection();
                this.selectedHandler.handleProcessingInstruction(
                        buffer,
                        targetOffset, targetLen, targetLine, targetCol,
                        contentOffset, contentLen, contentLine, contentCol,
                        outerOffset, outerLen, line, col);
                unmarkCurrentSelection();
                return;
            }

            this.nonSelectedHandler.handleProcessingInstruction(
                    buffer,
                    targetOffset, targetLen, targetLine, targetCol,
                    contentOffset, contentLen, contentLine, contentCol,
                    outerOffset, outerLen, line, col);
            return;

        }

        markCurrentSelection();
        this.selectedHandler.handleProcessingInstruction(
                buffer,
                targetOffset, targetLen, targetLine, targetCol,
                contentOffset, contentLen, contentLine, contentCol,
                outerOffset, outerLen, line, col);
        unmarkCurrentSelection();

    }



    /*
     * -------------------------------
     * Selection handling
     * -------------------------------
     */

    private void markCurrentSelection() {
        if (this.selectionAwareSelectedMarkupHandler != null) {
            this.selectionAwareSelectedMarkupHandler.setCurrentSelection(this.selectorMatches);
        }
    }

    private void unmarkCurrentSelection() {
        if (this.selectionAwareSelectedMarkupHandler != null) {
            this.selectionAwareSelectedMarkupHandler.setCurrentSelection(null);
        }
    }


    /*
     * -------------------------------
     * Markup block and level handling
     * -------------------------------
     */

    private void checkSizeOfMarkupBlocksStructure(final int markupLevel) {
        if (markupLevel >= this.markupBlocks.length) {
            final int newLen = Math.max(markupLevel + 1, this.markupBlocks.length + MARKUP_BLOCKS_LEN);
            final int[] newMarkupBlocks = new int[newLen];
            Arrays.fill(newMarkupBlocks, 0);
            System.arraycopy(this.markupBlocks, 0, newMarkupBlocks, 0, this.markupBlocks.length);
            this.markupBlocks = newMarkupBlocks;
        }
    }


    private void updateInsideAllSelectorMatchingBlockFlag() {
        for (int i = 0; i < this.selectorsLen; i++) {
            if (!this.selectorMatches[i]) {
                this.insideAllSelectorMatchingBlock = false;
                return;
            }
        }
        this.insideAllSelectorMatchingBlock = true;
    }


}
