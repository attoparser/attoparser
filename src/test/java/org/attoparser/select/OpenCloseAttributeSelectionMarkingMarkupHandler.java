/*
 * =============================================================================
 *
 *   Copyright (c) 2012-2022, The ATTOPARSER team (https://www.attoparser.org)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
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

import org.attoparser.AbstractChainedMarkupHandler;
import org.attoparser.IMarkupHandler;
import org.attoparser.ParseException;

/*
 *
 * @author Daniel Fernandez
 * @since 2.0.0
 */
public class OpenCloseAttributeSelectionMarkingMarkupHandler
            extends AbstractChainedMarkupHandler {

    private static final char[] SELECTOR_ATTRIBUTE_NAME = "selectors".toCharArray();
    private static final char[] INNER_WHITESPACE_BUFFER = " ".toCharArray();
    private static final int SELECTOR_ATTRIBUTE_BUFFER_LEN = 40;

    private ParseSelection selection;

    private boolean lastWasInnerWhiteSpace = false;
    private char[] selectorAttributeBuffer;


    public OpenCloseAttributeSelectionMarkingMarkupHandler(final IMarkupHandler handler) {

        super(handler);

        this.selectorAttributeBuffer = new char[SELECTOR_ATTRIBUTE_BUFFER_LEN];
        System.arraycopy(SELECTOR_ATTRIBUTE_NAME, 0, this.selectorAttributeBuffer, 0, SELECTOR_ATTRIBUTE_NAME.length);
        this.selectorAttributeBuffer[SELECTOR_ATTRIBUTE_NAME.length] = '=';
        this.selectorAttributeBuffer[SELECTOR_ATTRIBUTE_NAME.length + 1] = '\"';

    }


    @Override
    public void setParseSelection(final ParseSelection selection) {
        this.selection = selection;
        super.setParseSelection(selection);
    }




    @Override
    public void handleAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws ParseException {
        this.lastWasInnerWhiteSpace = false;
        getNext().handleAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);
    }


    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col)
            throws ParseException {

        if (this.selection.isMatchingAny()) {

            if (!this.lastWasInnerWhiteSpace) {
                getNext().handleInnerWhiteSpace(INNER_WHITESPACE_BUFFER, 0, INNER_WHITESPACE_BUFFER.length, line, col);
                this.lastWasInnerWhiteSpace = true;
            }

            final String selectorValues = this.selection.toString();

            final int selectorValuesLen = selectorValues.length();
            checkSelectorAttributeLen(selectorValuesLen);

            selectorValues.getChars(0, selectorValuesLen, this.selectorAttributeBuffer, SELECTOR_ATTRIBUTE_NAME.length + 2);
            this.selectorAttributeBuffer[SELECTOR_ATTRIBUTE_NAME.length + 2 + selectorValuesLen] = '\"';

            getNext().handleAttribute(
                    this.selectorAttributeBuffer,
                    0, SELECTOR_ATTRIBUTE_NAME.length, line, col,
                    SELECTOR_ATTRIBUTE_NAME.length, 1, line, col,
                    SELECTOR_ATTRIBUTE_NAME.length + 2, selectorValuesLen,
                    SELECTOR_ATTRIBUTE_NAME.length + 1, selectorValuesLen + 2, line, col);

        }

        this.lastWasInnerWhiteSpace = false;
        getNext().handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);

    }

    @Override
    public void handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (this.selection.isMatchingAny()) {

            if (!this.lastWasInnerWhiteSpace) {
                getNext().handleInnerWhiteSpace(INNER_WHITESPACE_BUFFER, 0, INNER_WHITESPACE_BUFFER.length, line, col);
                this.lastWasInnerWhiteSpace = true;
            }

            final String selectorValues = this.selection.toString();

            final int selectorValuesLen = selectorValues.length();
            checkSelectorAttributeLen(selectorValuesLen);

            selectorValues.getChars(0, selectorValuesLen, this.selectorAttributeBuffer, SELECTOR_ATTRIBUTE_NAME.length + 2);
            this.selectorAttributeBuffer[SELECTOR_ATTRIBUTE_NAME.length + 2 + selectorValuesLen] = '\"';

            getNext().handleAttribute(
                    this.selectorAttributeBuffer,
                    0, SELECTOR_ATTRIBUTE_NAME.length, line, col,
                    SELECTOR_ATTRIBUTE_NAME.length, 1, line, col,
                    SELECTOR_ATTRIBUTE_NAME.length + 2, selectorValuesLen,
                    SELECTOR_ATTRIBUTE_NAME.length + 1, selectorValuesLen + 2, line, col);

        }

        this.lastWasInnerWhiteSpace = false;
        getNext().handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);

    }

    @Override
    public void handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (this.selection.isMatchingAny()) {

            if (!this.lastWasInnerWhiteSpace) {
                getNext().handleInnerWhiteSpace(INNER_WHITESPACE_BUFFER, 0, INNER_WHITESPACE_BUFFER.length, line, col);
                this.lastWasInnerWhiteSpace = true;
            }

            final String selectorValues = this.selection.toString();

            final int selectorValuesLen = selectorValues.length();
            checkSelectorAttributeLen(selectorValuesLen);

            selectorValues.getChars(0, selectorValuesLen, this.selectorAttributeBuffer, SELECTOR_ATTRIBUTE_NAME.length + 2);
            this.selectorAttributeBuffer[SELECTOR_ATTRIBUTE_NAME.length + 2 + selectorValuesLen] = '\"';

            getNext().handleAttribute(
                    this.selectorAttributeBuffer,
                    0, SELECTOR_ATTRIBUTE_NAME.length, line, col,
                    SELECTOR_ATTRIBUTE_NAME.length, 1, line, col,
                    SELECTOR_ATTRIBUTE_NAME.length + 2, selectorValuesLen,
                    SELECTOR_ATTRIBUTE_NAME.length + 1, selectorValuesLen + 2, line, col);

        }

        this.lastWasInnerWhiteSpace = false;
        getNext().handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);

    }

    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (this.selection.isMatchingAny()) {

            if (!this.lastWasInnerWhiteSpace) {
                getNext().handleInnerWhiteSpace(INNER_WHITESPACE_BUFFER, 0, INNER_WHITESPACE_BUFFER.length, line, col);
                this.lastWasInnerWhiteSpace = true;
            }

            final String selectorValues = this.selection.toString();

            final int selectorValuesLen = selectorValues.length();
            checkSelectorAttributeLen(selectorValuesLen);

            selectorValues.getChars(0, selectorValuesLen, this.selectorAttributeBuffer, SELECTOR_ATTRIBUTE_NAME.length + 2);
            this.selectorAttributeBuffer[SELECTOR_ATTRIBUTE_NAME.length + 2 + selectorValuesLen] = '\"';

            getNext().handleAttribute(
                    this.selectorAttributeBuffer,
                    0, SELECTOR_ATTRIBUTE_NAME.length, line, col,
                    SELECTOR_ATTRIBUTE_NAME.length, 1, line, col,
                    SELECTOR_ATTRIBUTE_NAME.length + 2, selectorValuesLen,
                    SELECTOR_ATTRIBUTE_NAME.length + 1, selectorValuesLen + 2, line, col);

        }

        this.lastWasInnerWhiteSpace = false;
        getNext().handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);

    }

    @Override
    public void handleInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        this.lastWasInnerWhiteSpace = true;
        getNext().handleInnerWhiteSpace(buffer, offset, len, line, col);
    }




    private void checkSelectorAttributeLen(final int valueLen) {
        final int totalLenRequired = SELECTOR_ATTRIBUTE_NAME.length + 3 + valueLen;
        if (this.selectorAttributeBuffer.length < totalLenRequired) {
            final char[] newSelectorAttributeBuffer = new char[totalLenRequired];
            System.arraycopy(this.selectorAttributeBuffer, 0, newSelectorAttributeBuffer, 0, this.selectorAttributeBuffer.length);
            this.selectorAttributeBuffer = newSelectorAttributeBuffer;
        }
    }

}
