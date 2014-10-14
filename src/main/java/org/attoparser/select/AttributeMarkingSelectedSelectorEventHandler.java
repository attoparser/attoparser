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

import org.attoparser.IMarkupHandler;
import org.attoparser.ParseException;

/**
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public class AttributeMarkingSelectedSelectorEventHandler extends AllowingSelectedSelectorEventHandler {

    private static final char[] INNER_WHITESPACE_BUFFER = " ".toCharArray();

    private final char[] selectorAttributeName;
    private final int selectorAttributeNameLen;

    private boolean lastWasInnerWhiteSpace = false;
    private char[] selectorAttributeBuffer;


    public AttributeMarkingSelectedSelectorEventHandler(final String selectorAttributeName) {

        super();

        this.selectorAttributeName = selectorAttributeName.toCharArray();
        this.selectorAttributeNameLen = this.selectorAttributeName.length;
        this.selectorAttributeBuffer = new char[this.selectorAttributeNameLen + 20];
        System.arraycopy(this.selectorAttributeName, 0, this.selectorAttributeBuffer, 0, this.selectorAttributeNameLen);
        this.selectorAttributeBuffer[this.selectorAttributeNameLen] = '=';
        this.selectorAttributeBuffer[this.selectorAttributeNameLen + 1] = '\"';

    }


    @Override
    public void handleSelectedAttribute(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol,
            final IMarkupHandler handler)
            throws ParseException {
        this.lastWasInnerWhiteSpace = false;
        handler.handleAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);
    }

    @Override
    public void handleSelectedStandaloneElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {

        if (!this.lastWasInnerWhiteSpace) {
            handler.handleInnerWhiteSpace(INNER_WHITESPACE_BUFFER, 0, INNER_WHITESPACE_BUFFER.length, line, col);
            this.lastWasInnerWhiteSpace = true;
        }

        StringBuilder selectorValues = null;
        for (int i = 0; i < selectors.length; i++) {
            if (selectorMatches[i]) {
                if (selectorValues != null) {
                    selectorValues.append(' ');
                } else {
                    selectorValues = new StringBuilder(30);
                }
                selectorValues.append(selectors[i]);
            }
        }

        if (selectorValues != null) {

            final int selectorValuesLen = selectorValues.length();
            checkSelectorAttributeLen(selectorValuesLen);

            selectorValues.getChars(0, selectorValuesLen, this.selectorAttributeBuffer, this.selectorAttributeNameLen + 2);
            this.selectorAttributeBuffer[this.selectorAttributeNameLen + 2 + selectorValuesLen] = '\"';

            handler.handleAttribute(
                    this.selectorAttributeBuffer,
                    0, this.selectorAttributeNameLen, line, col,
                    this.selectorAttributeNameLen, 1, line, col,
                    this.selectorAttributeNameLen + 2, selectorValuesLen,
                    this.selectorAttributeNameLen + 1, selectorValuesLen + 2, line, col);

        }

        this.lastWasInnerWhiteSpace = false;
        handler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);

    }

    @Override
    public void handleSelectedOpenElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {

        if (!this.lastWasInnerWhiteSpace) {
            handler.handleInnerWhiteSpace(INNER_WHITESPACE_BUFFER, 0, INNER_WHITESPACE_BUFFER.length, line, col);
            this.lastWasInnerWhiteSpace = true;
        }

        StringBuilder selectorValues = null;
        for (int i = 0; i < selectors.length; i++) {
            if (selectorMatches[i]) {
                if (selectorValues != null) {
                    selectorValues.append(' ');
                } else {
                    selectorValues = new StringBuilder(30);
                }
                selectorValues.append(selectors[i]);
            }
        }

        if (selectorValues != null) {

            final int selectorValuesLen = selectorValues.length();
            checkSelectorAttributeLen(selectorValuesLen);

            selectorValues.getChars(0, selectorValuesLen, this.selectorAttributeBuffer, this.selectorAttributeNameLen + 2);
            this.selectorAttributeBuffer[this.selectorAttributeNameLen + 2 + selectorValuesLen] = '\"';

            handler.handleAttribute(
                    this.selectorAttributeBuffer,
                    0, this.selectorAttributeNameLen, line, col,
                    this.selectorAttributeNameLen, 1, line, col,
                    this.selectorAttributeNameLen + 2, selectorValuesLen,
                    this.selectorAttributeNameLen + 1, selectorValuesLen + 2, line, col);

        }

        this.lastWasInnerWhiteSpace = false;
        handler.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);

    }

    @Override
    public void handleSelectedElementInnerWhiteSpace(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        this.lastWasInnerWhiteSpace = true;
        handler.handleInnerWhiteSpace(buffer, offset, len, line, col);
    }




    private void checkSelectorAttributeLen(final int valueLen) {
        final int totalLenRequired = this.selectorAttributeNameLen + 3 + valueLen;
        if (this.selectorAttributeBuffer.length < totalLenRequired) {
            final char[] newSelectorAttributeBuffer = new char[totalLenRequired];
            System.arraycopy(this.selectorAttributeBuffer, 0, newSelectorAttributeBuffer, 0, this.selectorAttributeBuffer.length);
            this.selectorAttributeBuffer = newSelectorAttributeBuffer;
        }
    }

}
