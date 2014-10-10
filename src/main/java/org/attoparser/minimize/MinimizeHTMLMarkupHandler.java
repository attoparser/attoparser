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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
public final class MinimizeHTMLMarkupHandler extends AbstractMarkupHandler {

    public enum MinimizeMode {

        ONLY_WHITE_SPACE(false, false), COMPLETE(true, true);

        private boolean removeComments;
        private boolean unquoteAttributes;

        MinimizeMode(final boolean removeComments, final boolean unquoteAttributes) {
            this.removeComments = removeComments;
            this.unquoteAttributes = unquoteAttributes;
        }

    }


    /*
     * Relevant links:
     *    - http://perfectionkills.com/optimizing-html/
     *    - http://perfectionkills.com/experimenting-with-html-minifier/
     */

    // Space will be removed from between sibling block elements, and also from between opening tags of block container and block elements

    private static final String[] BLOCK_ELEMENTS_STR =
            new String[]{
                    "address", "article", "aside", "audio", "blockquote", "canvas",
                    "dd", "div", "dl", "dt", "fieldset", "figcaption", "figure", "footer",
                    "form", "h1", "h2", "h3", "h4", "h5", "h6", "header", "hgroup", "hr",
                    "li", "noscript", "ol", "option", "output", "p", "pre", "section", "table", "tbody",
                    "tfoot", "tr", "td", "th", "ul", "video"
            };
    private static final String[] BLOCK_CONTAINER_ELEMENTS_STR =
            new String[] {
                    "address", "article", "aside", "div", "dl", "fieldset", "footer",
                    "form", "header", "hgroup",  "noscript", "ol", "section", "table",
                    "tbody", "tr", "tfoot", "ul"
            };

    private static final char[][] BLOCK_ELEMENTS;
    private static final char[][] BLOCK_CONTAINER_ELEMENTS;


    private final char[] ELEMENT_INNER_WHITE_SPACE = new char[] { ' ' };
    private final char[] ATTRIBUTE_OPERATOR = new char[] { '=' };

    private final MinimizeMode minimizeMode;
    private final IMarkupHandler handler;

    private char[] internalBuffer = new char[30];
    private int pendingElementInnerWhiteSpaceLine = 1;
    private int pendingElementInnerWhiteSpaceCol = 1;
    private boolean lastCharWasTextWhiteSpace = false;





    static {

        final List<String> blockElementsStrList = new ArrayList<String>(Arrays.asList(BLOCK_ELEMENTS_STR));
        Collections.sort(blockElementsStrList);

        final List<String> blockContainerElementsStrList = new ArrayList<String>(Arrays.asList(BLOCK_ELEMENTS_STR));
        Collections.sort(blockContainerElementsStrList);

        BLOCK_ELEMENTS = new char[blockElementsStrList.size()][];
        BLOCK_CONTAINER_ELEMENTS = new char[blockContainerElementsStrList.size()][];

        for (int i = 0; i < BLOCK_ELEMENTS.length; i++) {
            BLOCK_ELEMENTS[i] = blockElementsStrList.get(i).toCharArray();
        }
        for (int i = 0; i < BLOCK_CONTAINER_ELEMENTS.length; i++) {
            BLOCK_CONTAINER_ELEMENTS[i] = blockContainerElementsStrList.get(i).toCharArray();
        }

    }





    public MinimizeHTMLMarkupHandler(final MinimizeMode minimizeMode, final IMarkupHandler handler) {

        super();

        if (minimizeMode == null) {
            throw new IllegalArgumentException("Compact mode cannot be null");
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

        if (len == 0) {
            // It is a zero-len text, simply forward the event
            this.handler.handleText(buffer, offset, len, line, col);
            return;
        }

        /*
         * First step is to check whether we will actually need to compress any whitespaces here or not.
         */

        boolean wasWhiteSpace = this.lastCharWasTextWhiteSpace;
        boolean shouldCompress = wasWhiteSpace;
        int i = offset;
        int n = len;
        while (!shouldCompress && n-- != 0) {
            if (isWhitespace(buffer[i])) {
                if (wasWhiteSpace) {
                    shouldCompress = true;
                    break;
                }
                wasWhiteSpace = true;
            } else {
                wasWhiteSpace = false;
            }
            i++;
        }

        if (!shouldCompress) {
            // Check whether the last char was a whitespace
            this.lastCharWasTextWhiteSpace = (isWhitespace(buffer[offset + len - 1]));
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

        wasWhiteSpace = this.lastCharWasTextWhiteSpace;
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

        // Check whether the last char was a whitespace
        this.lastCharWasTextWhiteSpace = wasWhiteSpace;

        // We've already constructed a text buffer with adequate white space compression, so we can forward the event
        this.handler.handleText(this.internalBuffer, 0, internalBufferSize, line, col);

    }




    @Override
    public void handleComment(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws ParseException {

        // Not all compact modes require stripping comments
        if (!this.minimizeMode.removeComments) {

            this.lastCharWasTextWhiteSpace = false;

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

        this.lastCharWasTextWhiteSpace = false;

        this.handler.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);

    }




    @Override
    public void handleStandaloneElementStart(
            final char[] buffer, final int offset, final int len,
            final boolean minimized, final int line, final int col) throws ParseException {

        this.lastCharWasTextWhiteSpace = false;

        this.handler.handleStandaloneElementStart(buffer, offset, len, minimized, line, col);

    }




    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer, final int offset, final int len,
            final boolean minimized, final int line, final int col) throws ParseException {

        this.lastCharWasTextWhiteSpace = false;

        this.handler.handleStandaloneElementEnd(buffer, offset, len, minimized, line, col);

    }




    @Override
    public void handleOpenElementStart(final char[] buffer, final int offset, final int len, final int line,
            final int col) throws ParseException {

        this.lastCharWasTextWhiteSpace = false;

        this.handler.handleOpenElementStart(buffer, offset, len, line, col);

    }




    @Override
    public void handleOpenElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) throws ParseException {

        this.lastCharWasTextWhiteSpace = false;

        this.handler.handleOpenElementEnd(buffer, offset, len, line, col);

    }




    @Override
    public void handleCloseElementStart(final char[] buffer, final int offset, final int len, final int line,
            final int col) throws ParseException {

        this.lastCharWasTextWhiteSpace = false;

        this.handler.handleCloseElementStart(buffer, offset, len, line, col);

    }




    @Override
    public void handleCloseElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) throws ParseException {

        this.lastCharWasTextWhiteSpace = false;

        this.handler.handleCloseElementEnd(buffer, offset, len, line, col);

    }




    @Override
    public void handleAutoCloseElementStart(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        // We are not sure whether auto-close events will make it to a result markup, but anyway the
        // safest option is to set this to false to avoid event-border-compression.
        this.lastCharWasTextWhiteSpace = false;

        this.handler.handleAutoCloseElementStart(buffer, offset, len, line, col);

    }





    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        // We are not sure whether auto-close events will make it to a result markup, but anyway the
        // safest option is to set this to false to avoid event-border-compression.
        this.lastCharWasTextWhiteSpace = false;

        this.handler.handleAutoCloseElementEnd(buffer, offset, len, line, col);

    }




    @Override
    public void handleUnmatchedCloseElementStart(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        this.lastCharWasTextWhiteSpace = false;

        this.handler.handleUnmatchedCloseElementStart(buffer, offset, len, line, col);

    }




    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        this.lastCharWasTextWhiteSpace = false;

        this.handler.handleUnmatchedCloseElementEnd(buffer, offset, len, line, col);

    }




    @Override
    public void handleAttribute(final char[] buffer, final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, final int valueContentOffset,
            final int valueContentLen, final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol) throws ParseException {

        this.handler.handleInnerWhiteSpace(
                ELEMENT_INNER_WHITE_SPACE, 0, ELEMENT_INNER_WHITE_SPACE.length,
                this.pendingElementInnerWhiteSpaceLine, this.pendingElementInnerWhiteSpaceCol);


        final boolean canRemoveAttributeQuotes =
                this.minimizeMode.unquoteAttributes &&
                canAttributeValueBeUnquoted(buffer, valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen);


        if (operatorLen <= 1 && !canRemoveAttributeQuotes) {
            // Operator is already compact enough, so we don't need to use our attribute-compacting buffer

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

        this.pendingElementInnerWhiteSpaceLine = line;
        this.pendingElementInnerWhiteSpaceCol = col;

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

        this.lastCharWasTextWhiteSpace = false;

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

        this.lastCharWasTextWhiteSpace = false;

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

        this.lastCharWasTextWhiteSpace = false;

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


    private boolean isWhitespace(final char c) {
        return (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f'
            || c == '\u000B' || c == '\u001C' || c == '\u001D' || c == '\u001E' || c == '\u001F'
            || (c > '\u007F' && Character.isWhitespace(c)));

    }


}