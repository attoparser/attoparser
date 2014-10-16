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

import org.attoparser.AbstractChainedMarkupHandler;
import org.attoparser.IMarkupHandler;
import org.attoparser.ParseException;

/**
 * <p>
 *   Implementation of the {@link org.attoparser.select.ISelectionAwareMarkupHandler} that
 *   adds an attribute (with a user-specified name) to all elements that match one or more selectors,
 *   as determined by a {@link org.attoparser.select.BlockSelectorMarkupHandler} or
 *   {@link org.attoparser.select.NodeSelectorMarkupHandler} handler.
 * </p>
 * <p>
 *   So for example, given an instance of this handler configured to use <tt>"selectors"</tt> as its attribute name
 *   and an <tt>&lt;img href="logo.png"&gt;</tt> tag that matches both selectors <tt>"//img"</tt> and
 *   <tt>"div/img"</tt>, this handler would transform such tag in:
 *   <tt>&lt;img href="logo.png" selectors="//img div/img"&gt;</tt>
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class AttributeMarkingSelectedMarkupHandler
            extends AbstractChainedMarkupHandler
            implements ISelectionAwareMarkupHandler {
    

    private static final char[] INNER_WHITESPACE_BUFFER = " ".toCharArray();

    private final ISelectionAwareMarkupHandler selectionAwareMarkupHandler;

    private String[] selectors = null;
    private boolean[] currentSelection = null;

    private final char[] selectorAttributeName;
    private final int selectorAttributeNameLen;

    private boolean lastWasInnerWhiteSpace = false;
    private char[] selectorAttributeBuffer;


    /**
     * <p>
     *   Build a new instance of this class, specifying the name of the attribute to be added to the matching elements
     *   and also the handler all events should be delegated to.
     * </p>
     *
     * @param selectorAttributeName the name of the marking attribute.
     * @param handler the handler to delegate events to.
     */
    public AttributeMarkingSelectedMarkupHandler(final String selectorAttributeName, final IMarkupHandler handler) {

        super(handler);

        if (selectorAttributeName == null || selectorAttributeName.trim().length() == 0) {
            throw new IllegalArgumentException("Selector attribute name cannot be null or empty");
        }

        // We capture the handler as selection-aware just in case it is, in order to also forward
        // calls to setSelectors and setCurrentSelection to it.
        this.selectionAwareMarkupHandler =
            (handler instanceof ISelectionAwareMarkupHandler ? (ISelectionAwareMarkupHandler) handler : null);

        this.selectorAttributeName = selectorAttributeName.toCharArray();
        this.selectorAttributeNameLen = this.selectorAttributeName.length;
        this.selectorAttributeBuffer = new char[this.selectorAttributeNameLen + 20];
        System.arraycopy(this.selectorAttributeName, 0, this.selectorAttributeBuffer, 0, this.selectorAttributeNameLen);
        this.selectorAttributeBuffer[this.selectorAttributeNameLen] = '=';
        this.selectorAttributeBuffer[this.selectorAttributeNameLen + 1] = '\"';

    }




    public void setSelectors(final String[] selectors) {
        this.selectors = selectors;
        if (this.selectionAwareMarkupHandler != null) {
            this.selectionAwareMarkupHandler.setSelectors(selectors);
        }
    }


    public void setCurrentSelection(final boolean[] currentSelection) {
        this.currentSelection = currentSelection;
        if (this.selectionAwareMarkupHandler != null) {
            this.selectionAwareMarkupHandler.setCurrentSelection(currentSelection);
        }
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

        if (!this.lastWasInnerWhiteSpace) {
            getNext().handleInnerWhiteSpace(INNER_WHITESPACE_BUFFER, 0, INNER_WHITESPACE_BUFFER.length, line, col);
            this.lastWasInnerWhiteSpace = true;
        }

        StringBuilder selectorValues = null;

        if (this.selectors != null && this.currentSelection != null) {

            for (int i = 0; i < selectors.length; i++) {
                if (this.currentSelection[i]) {
                    if (selectorValues != null) {
                        selectorValues.append(' ');
                    } else {
                        selectorValues = new StringBuilder(30);
                    }
                    selectorValues.append(selectors[i]);
                }
            }

        }

        if (selectorValues != null) {

            final int selectorValuesLen = selectorValues.length();
            checkSelectorAttributeLen(selectorValuesLen);

            selectorValues.getChars(0, selectorValuesLen, this.selectorAttributeBuffer, this.selectorAttributeNameLen + 2);
            this.selectorAttributeBuffer[this.selectorAttributeNameLen + 2 + selectorValuesLen] = '\"';

            getNext().handleAttribute(
                    this.selectorAttributeBuffer,
                    0, this.selectorAttributeNameLen, line, col,
                    this.selectorAttributeNameLen, 1, line, col,
                    this.selectorAttributeNameLen + 2, selectorValuesLen,
                    this.selectorAttributeNameLen + 1, selectorValuesLen + 2, line, col);

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

        if (!this.lastWasInnerWhiteSpace) {
            getNext().handleInnerWhiteSpace(INNER_WHITESPACE_BUFFER, 0, INNER_WHITESPACE_BUFFER.length, line, col);
            this.lastWasInnerWhiteSpace = true;
        }

        StringBuilder selectorValues = null;

        if (this.selectors != null && this.currentSelection != null) {

            for (int i = 0; i < selectors.length; i++) {
                if (this.currentSelection[i]) {
                    if (selectorValues != null) {
                        selectorValues.append(' ');
                    } else {
                        selectorValues = new StringBuilder(30);
                    }
                    selectorValues.append(selectors[i]);
                }
            }

        }

        if (selectorValues != null) {

            final int selectorValuesLen = selectorValues.length();
            checkSelectorAttributeLen(selectorValuesLen);

            selectorValues.getChars(0, selectorValuesLen, this.selectorAttributeBuffer, this.selectorAttributeNameLen + 2);
            this.selectorAttributeBuffer[this.selectorAttributeNameLen + 2 + selectorValuesLen] = '\"';

            getNext().handleAttribute(
                    this.selectorAttributeBuffer,
                    0, this.selectorAttributeNameLen, line, col,
                    this.selectorAttributeNameLen, 1, line, col,
                    this.selectorAttributeNameLen + 2, selectorValuesLen,
                    this.selectorAttributeNameLen + 1, selectorValuesLen + 2, line, col);

        }

        this.lastWasInnerWhiteSpace = false;
        getNext().handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);

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
        final int totalLenRequired = this.selectorAttributeNameLen + 3 + valueLen;
        if (this.selectorAttributeBuffer.length < totalLenRequired) {
            final char[] newSelectorAttributeBuffer = new char[totalLenRequired];
            System.arraycopy(this.selectorAttributeBuffer, 0, newSelectorAttributeBuffer, 0, this.selectorAttributeBuffer.length);
            this.selectorAttributeBuffer = newSelectorAttributeBuffer;
        }
    }

}
