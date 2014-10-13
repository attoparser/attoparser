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
package org.attoparser.minimize;

import org.attoparser.AbstractMarkupHandler;
import org.attoparser.IMarkupHandler;
import org.attoparser.ParseException;
import org.attoparser.ParseStatus;
import org.attoparser.util.TextUtil;


/**
 * <p>
 *   Implementation of {@link org.attoparser.IMarkupHandler} used for minimizing (compacting) HTML markup.
 * </p>
 * <p>
 *   The minimization operations that can be performed are:
 * </p>
 * <ul>
 *   <li>White Space minimization: excess white space is removed from texts. Also, white space is removed
 *       from between attributes in a tag, and between block and structural tags (which would ignore it).</li>
 *   <li>Unquoting of attributes: quotes are removed from attributes which values contain only alphanumeric
 *       characters.</li>
 *   <li>Collapsing of boolean attributes: boolean attributes (selected, required, disabled, etc.) are detected
 *       and collapsed to their no-value form (e.g. <tt>selected="selected" -&gt; selected</tt>).</li>
 *   <li>Standalone minimized elements are de-minimized to save the slash char
 *       (e.g. <tt>&lt;meta /&gt; -&gt; &lt;meta&gt;</tt>).</li>
 * </ul>
 * <p>
 *   Note that, though theoretically possible per the HTML rules, no tags are created or removed during minimization
 *   in order to ensure the lowest impact (ideally zero, except for text white space) on the DOM of the resulting
 *   markup.
 * </p>
 * <p>
 *   Two minimization modes are available:
 *   {@link MinimizeHtmlMarkupHandler.MinimizeMode#ONLY_WHITE_SPACE} and
 *   {@link MinimizeHtmlMarkupHandler.MinimizeMode#COMPLETE}. The former only minimizes
 *   white space whereas the latter performs all the available minimizations.
 * </p>
 * <p>
 *   Note that, as with most handlers, this class is <strong>not thread-safe</strong>. Also, instances of this class
 *   should not be reused across parsing operations.
 * </p>
 * <p>
 *   Sample usage:
 * </p>
 * <pre><code>
 *
 *   final Writer writer = new StringWriter();
 *
 *   // The output handler will be the last in the handler chain
 *   final IMarkupHandler outputHandler = new OutputMarkupHandler(writer);
 *
 *   // The minimizer handler will do its job before events reach output handler
 *   final IMarkupHandler handler = new MinimizeHtmlMarkupHandler(MinimizeMode.COMPLETE, outputHandler);
 *
 *   parser.parse(document, handler);
 *
 *   return writer.toString();
 *
 * </code></pre>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class MinimizeHtmlMarkupHandler extends AbstractMarkupHandler {

    public enum MinimizeMode {

        ONLY_WHITE_SPACE(false, false, false, false), COMPLETE(true, true, true, true);

        private boolean removeComments;
        private boolean unquoteAttributes;
        private boolean unminimizeStandalones;
        private boolean minimizeBooleanAttributes;

        MinimizeMode(final boolean removeComments, final boolean unquoteAttributes,
                     final boolean unminimizeStandalones, final boolean minimizeBooleanAttributes) {
            this.removeComments = removeComments;
            this.unquoteAttributes = unquoteAttributes;
            this.unminimizeStandalones = unminimizeStandalones;
            this.minimizeBooleanAttributes = minimizeBooleanAttributes;
        }

    }


    /*
     * Relevant links:
     *    - http://perfectionkills.com/optimizing-html/
     *    - http://perfectionkills.com/experimenting-with-html-minifier/
     */


    /*
     * Space will be removed from between sibling block elements, and also from between opening tags of
     * block container and block elements.
     * This array MUST BE IN ALPHABETIC ORDER (needed for binary search).
     */
    private static final String[] BLOCK_ELEMENTS =
            new String[]{
                    "address", "article", "aside", "audio", "base", "blockquote", "body", "canvas", "caption",
                    "col", "colgroup", "dd", "div", "dl", "dt", "fieldset", "figcaption", "figure", "footer",
                    "form", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hgroup", "hr", "html",
                    "li", "link", "meta", "noscript", "ol", "option", "output", "p", "pre", "script", "section",
                    "style", "table", "tbody", "td", "tfoot", "th", "thead", "title", "tr", "ul", "video"
            };

    // No whitespace minimization can be done inside a preformatted element
    private static final String[] PREFORMATTED_ELEMENTS =
            new String[]{
                    "pre", "script", "style", "textarea"
            };

    private static final String[] BOOLEAN_ATTRIBUTE_NAMES =
            new String[]{
                    "allowfullscreen", "async", "autofocus", "autoplay", "checked", "compact",
                    "controls", "declare", "default", "defaultchecked", "defaultmuted",
                    "defaultselected", "defer", "disabled", "draggable", "enabled",
                    "formnovalidate", "hidden", "indeterminate", "inert", "ismap",
                    "itemscope", "loop", "multiple", "muted", "nohref", "noresize",
                    "noshade", "novalidate", "nowrap", "open", "pauseonexit", "readonly",
                    "required", "reversed", "scoped", "seamless", "selected", "sortable",
                    "spellcheck", "translate", "truespeed", "typemustmatch", "visible"
            };

    private static final char[] SIZE_ONE_WHITE_SPACE = new char[] { ' ' };
    private static final char[] ATTRIBUTE_OPERATOR = new char[] { '=' };

    private final MinimizeMode minimizeMode;
    private final IMarkupHandler handler;

    private char[] internalBuffer = new char[30];
    private boolean lastTextEndedInWhiteSpace = false; // last text handled ended in white space (just in case next is also text, and
    private boolean lastOpenElementWasBlock = false; // last element that was open was a block element
    private boolean lastClosedElementWasBlock = false; // last element that was closed was a block element
    private boolean lastVisibleEventWasElement = false; // last event or event group was an element
    private boolean pendingInterBlockElementWhiteSpace = false; // delayed white space between block element tags waiting to determine whether it has to be output or not
    private boolean inPreformattedElement = false; // avoid pre and textarea to have their white space minimized

    private int pendingEventLine = 1;
    private int pendingEventCol = 1;








    public MinimizeHtmlMarkupHandler(final MinimizeMode minimizeMode, final IMarkupHandler handler) {

        super();

        if (minimizeMode == null) {
            throw new IllegalArgumentException("Minimize mode cannot be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        this.minimizeMode = minimizeMode;
        this.handler = handler;

    }




    @Override
    public void setParseStatus(final ParseStatus status) {
        this.handler.setParseStatus(status);
    }




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




    @Override
    public void handleText(final char[] buffer, final int offset, final int len, final int line, final int col)
            throws ParseException {


        // It is a zero-len text, simply ignore the event
        if (len == 0) {
            return;
        }


        // If there is a delayed whitespace text event, just output it
        flushPendingInterBlockElementWhiteSpace(false);

        // If we are inside a preformatted element, there's nothing to minimize
        if (this.inPreformattedElement) {
            this.lastTextEndedInWhiteSpace = false;
            this.lastVisibleEventWasElement = false;
            this.handler.handleText(buffer, offset, len, line, col);
            return;
        }


        /*
         * Check whether we will actually need to compress any whitespaces here or not.
         */

        boolean wasWhiteSpace = this.lastTextEndedInWhiteSpace;
        boolean shouldCompress = false;
        boolean isAllWhiteSpace = true;
        int i = offset;
        int n = len;
        while ((!shouldCompress || isAllWhiteSpace) && n-- != 0) {
            if (isWhitespace(buffer[i])) {
                if (wasWhiteSpace || buffer[i] != ' ') { // if it's a different kind of whitespace, we'll compress anyway
                    shouldCompress = true;
                }
                wasWhiteSpace = true;
            } else {
                wasWhiteSpace = false;
                isAllWhiteSpace = false;
            }
            i++;
        }

        if (!shouldCompress) {

            // Check whether the last char was a whitespace
            this.lastTextEndedInWhiteSpace = (isWhitespace(buffer[offset + len - 1]));

            // If this is all-white-space and last event was an element, better delay this event because
            // it might be that we don't have to launch it after all...
            if (this.lastVisibleEventWasElement && isAllWhiteSpace) {
                this.pendingInterBlockElementWhiteSpace = true;
                this.pendingEventLine = line;
                this.pendingEventCol = col;
                this.lastVisibleEventWasElement = false;
                return;
            }

            // Modify the flag: we are outputting a non-element
            this.lastVisibleEventWasElement = false;

            // Just forward the event's content without modifications
            this.handler.handleText(buffer, offset, len, line, col);

            return;

        }

        /*
         * We know we have to do some compression. So we will use the internal buffer.
         */

        if (this.internalBuffer.length < len) {
            // The buffer might not be long enough, grow it!
            this.internalBuffer = new char[len];
        }

        wasWhiteSpace = this.lastTextEndedInWhiteSpace;
        int internalBufferSize = 0;

        char c;

        i = offset;
        n = len;
        while (n-- != 0) {
            c = buffer[i++];
            if (isWhitespace(c)) {
                if (wasWhiteSpace) {
                    // We already recognized a white space, so we will skip this one
                    continue;
                }
                wasWhiteSpace = true;
                this.internalBuffer[internalBufferSize++] = ' '; // The only kind of whitespace we'll output is 0x20
                continue;
            }
            wasWhiteSpace = false;
            // Everything OK to copy the char to the internal buffer
            this.internalBuffer[internalBufferSize++] = c;
        }

        // If as a result of compressing we don't have to output anything... don't
        if (internalBufferSize > 0) {

            // Check whether the last char was a whitespace
            this.lastTextEndedInWhiteSpace = wasWhiteSpace;

            // If this is all-white-space and last event was an element, better delay this event because
            // it might be that we don't have to launch it after all...
            if (this.lastVisibleEventWasElement && isAllWhiteSpace) {
                this.pendingInterBlockElementWhiteSpace = true;
                this.pendingEventLine = line;
                this.pendingEventCol = col;
                this.lastVisibleEventWasElement = false;
                return;
            }

            // Modify the flag: we are outputting a non-element
            this.lastVisibleEventWasElement = false;

            // We've already constructed a text buffer with adequate white space compression, so we can forward the event
            this.handler.handleText(this.internalBuffer, 0, internalBufferSize, line, col);

        }

    }


    private void flushPendingInterBlockElementWhiteSpace(final boolean ignore) throws ParseException {
        if (this.pendingInterBlockElementWhiteSpace) {
            this.pendingInterBlockElementWhiteSpace = false;
            if (!ignore) {
                this.handler.handleText(SIZE_ONE_WHITE_SPACE, 0, 1, this.pendingEventLine, this.pendingEventCol);
            }
        }
    }




    @Override
    public void handleComment(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws ParseException {

        // Not all minimize modes require stripping comments
        if (!this.minimizeMode.removeComments) {

            // If there is a delayed whitespace text event, just output it
            flushPendingInterBlockElementWhiteSpace(false);

            this.lastVisibleEventWasElement = false;
            this.lastTextEndedInWhiteSpace = false;

            this.handler.handleComment(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);

        }

    }




    @Override
    public void handleCDATASection(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        // If there is a delayed whitespace text event, just output it
        flushPendingInterBlockElementWhiteSpace(false);

        this.lastVisibleEventWasElement = false;
        this.lastTextEndedInWhiteSpace = false;

        this.handler.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);

    }




    @Override
    public void handleStandaloneElementStart(
            final char[] buffer, final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col) throws ParseException {

        this.lastTextEndedInWhiteSpace = false;

        // Check whether the inter-block element whitespace should be written or simply ignored
        final boolean ignorePendingWhiteSpace =
                ((this.lastClosedElementWasBlock || this.lastOpenElementWasBlock) && isBlockElement(buffer, nameOffset, nameLen));
        flushPendingInterBlockElementWhiteSpace(ignorePendingWhiteSpace);

        if (this.minimizeMode.unminimizeStandalones) {
            this.handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, false, line, col);
        } else {
            this.handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);
        }

    }




    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer, final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col) throws ParseException {

        this.lastTextEndedInWhiteSpace = false;
        this.lastClosedElementWasBlock = isBlockElement(buffer, nameOffset, nameLen);
        this.lastOpenElementWasBlock = false;
        this.lastVisibleEventWasElement = true;

        if (this.minimizeMode.unminimizeStandalones) {
            this.handler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, false, line, col);
        } else {
            this.handler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);
        }

    }




    @Override
    public void handleOpenElementStart(final char[] buffer, final int nameOffset, final int nameLen, final int line,
            final int col) throws ParseException {

        this.lastTextEndedInWhiteSpace = false;

        // Check whether the inter-block element whitespace should be written or simply ignored
        final boolean ignorePendingWhiteSpace =
                ((this.lastClosedElementWasBlock || this.lastOpenElementWasBlock) && isBlockElement(buffer, nameOffset, nameLen));
        flushPendingInterBlockElementWhiteSpace(ignorePendingWhiteSpace);

        if (isPreformattedElement(buffer, nameOffset, nameLen)) {
            this.inPreformattedElement = true;
        }

        this.handler.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleOpenElementEnd(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col) throws ParseException {

        this.lastTextEndedInWhiteSpace = false;
        this.lastOpenElementWasBlock = isBlockElement(buffer, nameOffset, nameLen);
        this.lastClosedElementWasBlock = false;
        this.lastVisibleEventWasElement = true;

        this.handler.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleCloseElementStart(final char[] buffer, final int nameOffset, final int nameLen, final int line,
            final int col) throws ParseException {

        this.lastTextEndedInWhiteSpace = false;

        // Check whether the inter-block element whitespace should be written or simply ignored
        final boolean ignorePendingWhiteSpace =
                (this.lastClosedElementWasBlock && isBlockElement(buffer, nameOffset, nameLen));
        flushPendingInterBlockElementWhiteSpace(ignorePendingWhiteSpace);

        if (isPreformattedElement(buffer, nameOffset, nameLen)) {
            this.inPreformattedElement = false;
        }

        this.handler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleCloseElementEnd(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col) throws ParseException {

        this.lastTextEndedInWhiteSpace = false;
        this.lastClosedElementWasBlock = isBlockElement(buffer, nameOffset, nameLen);
        this.lastOpenElementWasBlock = false;
        this.lastVisibleEventWasElement = true;

        this.handler.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleAutoCloseElementStart(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.lastTextEndedInWhiteSpace = false;

        // Check whether the inter-block element whitespace should be written or simply ignored
        final boolean ignorePendingWhiteSpace =
                (this.lastClosedElementWasBlock && isBlockElement(buffer, nameOffset, nameLen));
        flushPendingInterBlockElementWhiteSpace(ignorePendingWhiteSpace);

        this.handler.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }





    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.lastTextEndedInWhiteSpace = false;
        this.lastClosedElementWasBlock = isBlockElement(buffer, nameOffset, nameLen);
        this.lastOpenElementWasBlock = false;
        this.lastVisibleEventWasElement = true;

        this.handler.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleUnmatchedCloseElementStart(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.lastTextEndedInWhiteSpace = false;

        // Check whether the inter-block element whitespace should be written or simply ignored
        final boolean ignorePendingWhiteSpace =
                (this.lastClosedElementWasBlock && isBlockElement(buffer, nameOffset, nameLen));
        flushPendingInterBlockElementWhiteSpace(ignorePendingWhiteSpace);

        this.handler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.lastTextEndedInWhiteSpace = false;
        this.lastClosedElementWasBlock = isBlockElement(buffer, nameOffset, nameLen);
        this.lastOpenElementWasBlock = false;
        this.lastVisibleEventWasElement = true;

        this.handler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleAttribute(final char[] buffer, final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, final int valueContentOffset,
            final int valueContentLen, final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol) throws ParseException {

        this.handler.handleInnerWhiteSpace(
                SIZE_ONE_WHITE_SPACE, 0, SIZE_ONE_WHITE_SPACE.length,
                this.pendingEventLine, this.pendingEventCol);


        final boolean isMinimizableBooleanAttribute =
                this.minimizeMode.minimizeBooleanAttributes &&
                isBooleanAttribute(buffer, nameOffset, nameLen) &&
                TextUtil.equals(false, buffer, nameOffset, nameLen, buffer, valueContentOffset, valueContentLen);

        if (isMinimizableBooleanAttribute) {
            // If it is a minimizable boolean, no need to go any further
            this.handler.handleAttribute(
                    buffer,
                    nameOffset, nameLen, nameLine, nameCol,
                    0, 0, operatorLine, operatorCol,
                    0, 0, 0, 0, operatorLen, operatorCol
                    );
            return;
        }


        final boolean canRemoveAttributeQuotes =
                this.minimizeMode.unquoteAttributes &&
                canAttributeValueBeUnquoted(buffer, valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen);


        if (operatorLen <= 1 && !canRemoveAttributeQuotes) {
            // Operator is already minimal enough, so we don't need to use our attribute-minimizing buffer

            this.handler.handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol, operatorOffset,
                    operatorLen, operatorLine, operatorCol, valueContentOffset, valueContentLen,
                    valueOuterOffset, valueOuterLen, valueLine, valueCol);

        } else {

            final int requiredLen = nameLen + 1 /* new operator len */ + valueOuterLen;
            if (this.internalBuffer.length < requiredLen) {
                this.internalBuffer = new char[requiredLen];
            }

            System.arraycopy(buffer, nameOffset, this.internalBuffer, 0, nameLen);
            System.arraycopy(ATTRIBUTE_OPERATOR, 0, this.internalBuffer, nameLen, ATTRIBUTE_OPERATOR.length);

            if (canRemoveAttributeQuotes) {

                System.arraycopy(buffer, valueContentOffset, this.internalBuffer, nameLen + ATTRIBUTE_OPERATOR.length, valueContentLen);

                this.handler.handleAttribute(
                        this.internalBuffer,
                        0, nameLen, nameLine, nameCol,
                        nameLen, ATTRIBUTE_OPERATOR.length, operatorLine, operatorCol,
                        nameLen + ATTRIBUTE_OPERATOR.length, valueContentLen,
                        nameLen + ATTRIBUTE_OPERATOR.length, valueContentLen,
                        valueLine, valueCol);

            } else {

                System.arraycopy(buffer, valueOuterOffset, this.internalBuffer, nameLen + ATTRIBUTE_OPERATOR.length, valueOuterLen);

                this.handler.handleAttribute(
                        this.internalBuffer,
                        0, nameLen, nameLine, nameCol,
                        nameLen, ATTRIBUTE_OPERATOR.length, operatorLine, operatorCol,
                        nameLen + ATTRIBUTE_OPERATOR.length + (valueOuterOffset - valueContentOffset), valueContentLen,
                        nameLen + ATTRIBUTE_OPERATOR.length, valueOuterLen,
                        valueLine, valueCol);

            }

        }

    }




    @Override
    public void handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws ParseException {

        this.pendingEventLine = line;
        this.pendingEventCol = col;

    }




    @Override
    public void handleDocType(
            final char[] buffer, 
            final int keywordOffset, final int keywordLen,
            final int keywordLine, final int keywordCol, 
            final int elementNameOffset, final int elementNameLen, 
            final int elementNameLine, final int elementNameCol,
            final int typeOffset, final int typeLen, 
            final int typeLine, final int typeCol,
            final int publicIdOffset, final int publicIdLen, 
            final int publicIdLine, final int publicIdCol, 
            final int systemIdOffset, final int systemIdLen,
            final int systemIdLine, final int systemIdCol, 
            final int internalSubsetOffset, final int internalSubsetLen,
            final int internalSubsetLine, final int internalSubsetCol,
            final int outerOffset, final int outerLen,
            final int outerLine, final int outerCol) throws ParseException {

        // If there is a delayed whitespace text event, just output it
        flushPendingInterBlockElementWhiteSpace(false);

        this.lastVisibleEventWasElement = false;
        this.lastTextEndedInWhiteSpace = false;

        this.handler.handleDocType(buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol, typeOffset, typeLen,
                typeLine, typeCol, publicIdOffset, publicIdLen, publicIdLine, publicIdCol, systemIdOffset,
                systemIdLen, systemIdLine, systemIdCol, internalSubsetOffset, internalSubsetLen,
                internalSubsetLine, internalSubsetCol, outerOffset, outerLen, outerLine, outerCol);

    }

    
    
    
    @Override
    public void handleXmlDeclaration(
            final char[] buffer, 
            final int keywordOffset, final int keywordLen,
            final int keywordLine, final int keywordCol,
            final int versionOffset, final int versionLen,
            final int versionLine, final int versionCol,
            final int encodingOffset, final int encodingLen,
            final int encodingLine, final int encodingCol,
            final int standaloneOffset, final int standaloneLen,
            final int standaloneLine, final int standaloneCol,
            final int outerOffset, final int outerLen,
            final int line,final int col) 
            throws ParseException {

        // If there is a delayed whitespace text event, just output it
        flushPendingInterBlockElementWhiteSpace(false);

        this.lastVisibleEventWasElement = false;
        this.lastTextEndedInWhiteSpace = false;

        this.handler.handleXmlDeclaration(buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol, encodingOffset, encodingLen,
                encodingLine, encodingCol, standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen, line, col);

    }






    @Override
    public void handleProcessingInstruction(
            final char[] buffer, 
            final int targetOffset, final int targetLen, 
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws ParseException {

        // If there is a delayed whitespace text event, just output it
        flushPendingInterBlockElementWhiteSpace(false);

        this.lastVisibleEventWasElement = false;
        this.lastTextEndedInWhiteSpace = false;

        this.handler.handleProcessingInstruction(buffer, targetOffset, targetLen, targetLine, targetCol,
                contentOffset, contentLen, contentLine, contentCol, outerOffset, outerLen, line, col);

    }


    




    private static boolean canAttributeValueBeUnquoted(
            final char[] buffer,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen) {

        if (valueContentLen == 0 || valueOuterLen == valueContentLen) {
            // Cannot be unquoted -- value is empty or already has no quotes!
            return false;
        }

        char c;

        int i = valueContentOffset;
        int n = valueContentLen;

        while (n-- != 0) {

            c = buffer[i];

            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))) {
                return false;
            }

            i++;

        }

        return true;

    }


    private static boolean isWhitespace(final char c) {
        return (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f'
            || c == '\u000B' || c == '\u001C' || c == '\u001D' || c == '\u001E' || c == '\u001F'
            || (c > '\u007F' && Character.isWhitespace(c)));

    }


    private static boolean isBlockElement(final char[] buffer, final int nameOffset, final int nameLen) {
        return TextUtil.binarySearch(false, BLOCK_ELEMENTS, buffer, nameOffset, nameLen) >= 0;
    }


    private static boolean isPreformattedElement(final char[] buffer, final int nameOffset, final int nameLen) {
        int i = 0;
        int n = PREFORMATTED_ELEMENTS.length;
        while (n-- != 0) {
            if (TextUtil.compareTo(false, PREFORMATTED_ELEMENTS[i], 0, PREFORMATTED_ELEMENTS[i].length(), buffer, nameOffset, nameLen) == 0) {
                return true;
            }
            i++;
        }
        return false;
    }


    private static boolean isBooleanAttribute(final char[] buffer, final int nameOffset, final int nameLen) {
        return TextUtil.binarySearch(false, BOOLEAN_ATTRIBUTE_NAMES, buffer, nameOffset, nameLen) >= 0;
    }







}