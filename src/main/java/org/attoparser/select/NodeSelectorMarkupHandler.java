/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2014, The THYMELEAF team (http://www.thymeleaf.org)
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

/**
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 * 
 */
public final class NodeSelectorMarkupHandler extends AbstractMarkupHandler {


    private final IMarkupHandler handler;
    private final ISelectedSelectorEventHandler selectedHandler;
    private final INonSelectedSelectorEventHandler nonSelectedHandler;

    private final SelectorElementBuffer elementBuffer;

    private final int selectorsLen;
    private final String[] selectors;
    private final boolean[] selectorMatches;
    private final MarkupSelectorFilter[] selectorFilters;

    private final int[][] matchingMarkupLevelsPerSelector;

    private boolean someSelectorsMatch;


    private int markupLevel;

    private static final int MARKUP_BLOCKS_LEN = 10;
    private int[] markupBlocks;
    private int markupBlockIndex;






    public NodeSelectorMarkupHandler(final IMarkupHandler handler,
                                     final ISelectedSelectorEventHandler selectedEventHandler,
                                     final INonSelectedSelectorEventHandler nonSelectedEventHandler,
                                     final String selector, final MarkupSelectorMode mode) {
        this(handler, selectedEventHandler, nonSelectedEventHandler, new String[] {selector}, mode, null);
    }



    public NodeSelectorMarkupHandler(final IMarkupHandler handler,
                                     final ISelectedSelectorEventHandler selectedEventHandler,
                                     final INonSelectedSelectorEventHandler nonSelectedEventHandler,
                                     final String[] selectors, final MarkupSelectorMode mode) {
        this(handler, selectedEventHandler, nonSelectedEventHandler, selectors, mode, null);
    }



    public NodeSelectorMarkupHandler(final IMarkupHandler handler,
                                     final ISelectedSelectorEventHandler selectedEventHandler,
                                     final INonSelectedSelectorEventHandler nonSelectedEventHandler,
                                     final String selector, final MarkupSelectorMode mode,
                                     final IMarkupSelectorReferenceResolver referenceResolver) {
        this(handler, selectedEventHandler, nonSelectedEventHandler, new String[] {selector}, mode, referenceResolver);
    }



    public NodeSelectorMarkupHandler(final IMarkupHandler handler,
                                     final ISelectedSelectorEventHandler selectedEventHandler,
                                     final INonSelectedSelectorEventHandler nonSelectedEventHandler,
                                     final String[] selectors, final MarkupSelectorMode mode,
                                     final IMarkupSelectorReferenceResolver referenceResolver) {

        super();


        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        if (selectors == null || selectors.length == 0) {
            throw new IllegalArgumentException("Selector array cannot be null or empty");
        }
        for (final String selector : selectors) {
            if (selector == null || selector.trim().length() == 0) {
                throw new IllegalArgumentException(
                        "Selector array contains at least one null or empty item, which is forbidden");
            }
        }

        this.handler = handler;
        this.selectedHandler = selectedEventHandler;
        this.nonSelectedHandler = nonSelectedEventHandler;

        this.selectors = selectors;
        this.selectorsLen = selectors.length;

        // Note this variable is defined basically in order to be reused in different events, but will not be dealt with as "state"
        this.selectorMatches = new boolean[this.selectors.length];
        Arrays.fill(this.selectorMatches, false);

        // Note this variable is defined basically in order to be reused in different events, but will not be dealt with as "state"
        this.someSelectorsMatch = false;

        this.selectorFilters = new MarkupSelectorFilter[this.selectorsLen];
        for (int i = 0; i < this.selectorsLen; i++) {

            final List<IMarkupSelectorItem> selectorItems =
                    MarkupSelectorItems.forSelector(mode, selectors[i], referenceResolver);

            this.selectorFilters[i] = new MarkupSelectorFilter(null, selectorItems.get(0));
            MarkupSelectorFilter last = this.selectorFilters[i];
            for (int j = 1; j < selectorItems.size(); j++) {
                last = new MarkupSelectorFilter(last, selectorItems.get(j));
            }

        }

        this.elementBuffer = new SelectorElementBuffer();

        this.matchingMarkupLevelsPerSelector = new int[this.selectorsLen][];
        Arrays.fill(this.matchingMarkupLevelsPerSelector, null);

        this.markupLevel = 0;

        this.markupBlockIndex = 0;
        this.markupBlocks = new int[MARKUP_BLOCKS_LEN];
        this.markupBlocks[this.markupLevel] = this.markupBlockIndex;

    }






    @Override
    public void setParseStatus(final ParseStatus status) {
        this.handler.setParseStatus(status);
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
        this.handler.handleDocumentStart(startTimeNanos, line, col);
    }


    @Override
    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws ParseException {
        this.handler.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);
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

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {

            this.selectorMatches[i] =
                    this.selectorFilters[i].matchXmlDeclaration(false, this.markupLevel, this.markupBlocks[this.markupLevel]);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
            }

        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedXmlDeclaration(
                    this.selectors, this.selectorMatches,
                    buffer,
                    keywordOffset, keywordLen, keywordLine, keywordCol,
                    versionOffset, versionLen, versionLine, versionCol,
                    encodingOffset, encodingLen, encodingLine, encodingCol,
                    standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                    outerOffset, outerLen, line, col, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedXmlDeclaration(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol,
                encodingOffset, encodingLen, encodingLine, encodingCol,
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen, line, col, this.handler);

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

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {

            this.selectorMatches[i] =
                    this.selectorFilters[i].matchDocTypeClause(false, this.markupLevel, this.markupBlocks[this.markupLevel]);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
            }

        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedDocTypeClause(
                    this.selectors, this.selectorMatches,
                    buffer,
                    keywordOffset, keywordLen, keywordLine, keywordCol,
                    elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                    typeOffset, typeLen, typeLine, typeCol,
                    publicIdOffset, publicIdLen, publicIdLine, publicIdCol,
                    systemIdOffset, systemIdLen, systemIdLine, systemIdCol,
                    internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol,
                    outerOffset, outerLen, outerLine, outerCol, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedDocTypeClause(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                typeOffset, typeLen, typeLine, typeCol,
                publicIdOffset, publicIdLen, publicIdLine, publicIdCol,
                systemIdOffset, systemIdLen, systemIdLine, systemIdCol,
                internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol,
                outerOffset, outerLen, outerLine, outerCol, this.handler);

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

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {

            this.selectorMatches[i] =
                    this.selectorFilters[i].matchCDATASection(false, this.markupLevel, this.markupBlocks[this.markupLevel]);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
            }

        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedCDATASection(
                    this.selectors, this.selectorMatches,
                    buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedCDATASection(
                buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, this.handler);

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

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {

            this.selectorMatches[i] =
                    this.selectorFilters[i].matchText(false, this.markupLevel, this.markupBlocks[this.markupLevel]);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
            }

        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedText(
                    this.selectors, this.selectorMatches,
                    buffer, offset, len, line, col, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedText(
                buffer, offset, len, line, col, this.handler);

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

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {

            this.selectorMatches[i] =
                    this.selectorFilters[i].matchComment(false, this.markupLevel, this.markupBlocks[this.markupLevel]);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
            }

        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedComment(
                    this.selectors, this.selectorMatches,
                    buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedComment(
                buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, this.handler);

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


        this.elementBuffer.bufferAttribute(
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

        this.elementBuffer.bufferElementStart(buffer, nameOffset, nameLen, line, col, true, minimized);

    }



    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col)
            throws ParseException {

        this.elementBuffer.bufferElementEnd(buffer, nameOffset, nameLen, line, col);

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {

            this.selectorMatches[i] =
                    this.selectorFilters[i].matchStandaloneElement(false, this.markupLevel, this.markupBlocks[this.markupLevel], this.elementBuffer);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
            }

        }

        if (this.someSelectorsMatch) {
            this.elementBuffer.flushSelectedBuffer(this.selectedHandler, this.handler, this.selectors, this.selectorMatches);
            return;
        }

        this.elementBuffer.flushNonSelectedBuffer(this.nonSelectedHandler, this.handler);

    }



    @Override
    public void handleOpenElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.elementBuffer.bufferElementStart(buffer, nameOffset, nameLen, line, col, false, false);

    }



    @Override
    public void handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.elementBuffer.bufferElementEnd(buffer, nameOffset, nameLen, line, col);

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {
            this.selectorMatches[i] =
                    this.selectorFilters[i].matchOpenElement(false, this.markupLevel, this.markupBlocks[this.markupLevel], this.elementBuffer);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
                addMatchingMarkupLevel(i, this.markupLevel);
            }
        }

        this.markupLevel++;

        checkSizeOfMarkupBlocksStructure(this.markupLevel);
        this.markupBlocks[this.markupLevel] = ++this.markupBlockIndex;

        if (this.someSelectorsMatch) {
            this.elementBuffer.flushSelectedBuffer(this.selectedHandler, this.handler, this.selectors, this.selectorMatches);
            return;
        }

        this.elementBuffer.flushNonSelectedBuffer(this.nonSelectedHandler, this.handler);

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

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {
            // We use the flags indicating past matches to recompute new ones
            this.selectorMatches[i] = isMatchingMarkupLevel(i, this.markupLevel);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
            }
        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedCloseElementStart(
                    this.selectors, this.selectorMatches, buffer, nameOffset, nameLen, line, col, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedCloseElementStart(buffer, nameOffset, nameLen, line, col, this.handler);

    }



    @Override
    public void handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {
            // We use the flags indicating past matches to recompute new ones
            this.selectorMatches[i] = isMatchingMarkupLevel(i, this.markupLevel);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
                removeMatchingMarkupLevel(i, this.markupLevel);
            }
        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedCloseElementEnd(this.selectors, this.selectorMatches, buffer, nameOffset, nameLen, line, col, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedCloseElementEnd(buffer, nameOffset, nameLen, line, col, this.handler);

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

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {
            // We use the flags indicating past matches to recompute new ones
            this.selectorMatches[i] = isMatchingMarkupLevel(i, this.markupLevel);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
            }
        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedAutoCloseElementStart(this.selectors, this.selectorMatches, buffer, nameOffset, nameLen, line, col, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedAutoCloseElementStart(
                buffer, nameOffset, nameLen, line, col, this.handler);

    }



    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {
            // We use the flags indicating past matches to recompute new ones
            this.selectorMatches[i] = isMatchingMarkupLevel(i, this.markupLevel);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
                removeMatchingMarkupLevel(i, this.markupLevel);
            }
        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedAutoCloseElementEnd(this.selectors, this.selectorMatches, buffer, nameOffset, nameLen, line, col, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col, this.handler);

    }



    @Override
    public void handleUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {
            // We use the flags indicating past matches to recompute new ones
            this.selectorMatches[i] = isMatchingMarkupLevel(i, this.markupLevel);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
            }
        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedUnmatchedCloseElementStart(this.selectors, this.selectorMatches, buffer, nameOffset, nameLen, line, col, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col, this.handler);

    }



    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {
            // We use the flags indicating past matches to recompute new ones
            this.selectorMatches[i] = isMatchingMarkupLevel(i, this.markupLevel);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
            }
        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedUnmatchedCloseElementEnd(this.selectors, this.selectorMatches, buffer, nameOffset, nameLen, line, col, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col, this.handler);

    }



    @Override
    public void handleInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        this.elementBuffer.bufferElementInnerWhiteSpace(buffer, offset, len, line, col);

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

        this.someSelectorsMatch = false;
        for (int i = 0; i < this.selectorsLen; i++) {

            this.selectorMatches[i] =
                    this.selectorFilters[i].matchProcessingInstruction(false, this.markupLevel, this.markupBlocks[this.markupLevel]);
            if (this.selectorMatches[i]) {
                this.someSelectorsMatch = true;
            }

        }

        if (this.someSelectorsMatch) {
            this.selectedHandler.handleSelectedProcessingInstruction(
                    this.selectors, this.selectorMatches,
                    buffer,
                    targetOffset, targetLen, targetLine, targetCol,
                    contentOffset, contentLen, contentLine, contentCol,
                    outerOffset, outerLen, line, col, this.handler);
            return;
        }

        this.nonSelectedHandler.handleNonSelectedProcessingInstruction(
                buffer,
                targetOffset, targetLen, targetLine, targetCol,
                contentOffset, contentLen, contentLine, contentCol,
                outerOffset, outerLen, line, col, this.handler);

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


    private void addMatchingMarkupLevel(final int selector, final int markupLevel) {

        if (this.matchingMarkupLevelsPerSelector[selector] == null) {
            // Structure for this selector didn't exist before: create it
            this.matchingMarkupLevelsPerSelector[selector] = new int[2];
            Arrays.fill(this.matchingMarkupLevelsPerSelector[selector], Integer.MAX_VALUE);
        }

        for (int i = 0; i < this.matchingMarkupLevelsPerSelector[selector].length; i++) {
            if (this.matchingMarkupLevelsPerSelector[selector][i] == Integer.MAX_VALUE) {
                this.matchingMarkupLevelsPerSelector[selector][i] = markupLevel;
                return;
            }
        }

        // Not found an available place: grow the structure
        int[] newMatchingMarkupLevelsPerSelector = new int[this.matchingMarkupLevelsPerSelector[selector].length + 2];
        Arrays.fill(newMatchingMarkupLevelsPerSelector, Integer.MAX_VALUE);
        System.arraycopy(this.matchingMarkupLevelsPerSelector[selector],0,newMatchingMarkupLevelsPerSelector,0,this.matchingMarkupLevelsPerSelector[selector].length);

        newMatchingMarkupLevelsPerSelector[this.matchingMarkupLevelsPerSelector[selector].length] = markupLevel;
        this.matchingMarkupLevelsPerSelector[selector] = newMatchingMarkupLevelsPerSelector;

    }


    private boolean isMatchingMarkupLevel(final int selector, final int markupLevel) {
        if (this.matchingMarkupLevelsPerSelector[selector] == null) {
            return false;
        }
        for (int i = 0; i < this.matchingMarkupLevelsPerSelector[selector].length; i++) {
            if (this.matchingMarkupLevelsPerSelector[selector][i] == markupLevel) {
                return true;
            }
        }
        return false;
    }


    private void removeMatchingMarkupLevel(final int selector, final int markupLevel) {
        for (int i = 0; i < this.matchingMarkupLevelsPerSelector[selector].length; i++) {
            if (this.matchingMarkupLevelsPerSelector[selector][i] == markupLevel) {
                this.matchingMarkupLevelsPerSelector[selector][i] = Integer.MAX_VALUE;
                return;
            }
        }
    }



}
