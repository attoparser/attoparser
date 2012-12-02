/*
 * =============================================================================
 * 
 *   Copyright (c) 2012, The ATTOPARSER team (http://www.attoparser.org)
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
package org.attoparser.markup.html;

import org.attoparser.AttoParseException;
import org.attoparser.markup.AbstractDetailedMarkupAttoHandler;
import org.attoparser.markup.MarkupParsingConfiguration;
import org.attoparser.markup.html.elements.BasicHtmlElement;
import org.attoparser.markup.html.elements.HtmlElements;
import org.attoparser.markup.html.elements.IHtmlElement;





/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public abstract class AbstractDetailedHtmlAttoHandler 
        extends AbstractDetailedMarkupAttoHandler
        implements IDetailedHtmlElementHandling {



    private final HtmlParsingConfiguration configuration;
    private final HtmlElementStack stack;
    private IHtmlElement currentElement = null;

    
    
    protected AbstractDetailedHtmlAttoHandler(final HtmlParsingConfiguration configuration) {
        super(HtmlParsing.markupParsingConfiguration(configuration));
        this.stack = new HtmlElementStack();
        this.configuration = configuration;
    }

    
    
    
    private static IHtmlElement getElementByName(final char[] buffer, final int offset, final int len) {
        final IHtmlElement element = HtmlElements.lookFor(buffer, offset, len);
        if (element == null) {
            return new BasicHtmlElement(new String(buffer, offset, len));
        }
        return element;
    }
    
    
    
    

    
    
    
    @Override
    public final void handleDocumentStart(final long startTimeNanos, 
            final int line, final int col,
            final MarkupParsingConfiguration markupParsingConfiguration)
            throws AttoParseException {
        handleDocumentStart(startTimeNanos, line, col, this.configuration);
    }

    
    
    @Override
    public final void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos, 
            final int line, final int col, 
            final MarkupParsingConfiguration markupParsingConfiguration)
            throws AttoParseException {
        handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col, this.configuration);
    }
    
    
    
    
    
    
    @Override
    public final void handleStandaloneElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        super.handleStandaloneElementStart(buffer, offset, len, line, col);
        //
        // Nothing to be done until we know the name of the element
        //
    }

    @Override
    public final void handleStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        
        super.handleStandaloneElementName(buffer, offset, len, line, col);
        
        this.currentElement = getElementByName(buffer, offset, len);
        this.currentElement.handleStandaloneElementStartAndName(buffer, offset, len, line, col, this.stack, this);

    }

    @Override
    public final void handleStandaloneElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        
        super.handleStandaloneElementEnd(buffer, offset, len, line, col);

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        this.currentElement.handleStandaloneElementEnd(buffer, offset, len, line, col, this.stack, this);
        this.currentElement = null;

    }


    
    
    @Override
    public final void handleOpenElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        super.handleOpenElementStart(buffer, offset, len, line, col);
        //
        // Nothing to be done until we know the name of the element
        //
    }

    @Override
    public final void handleOpenElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {

        super.handleOpenElementName(buffer, offset, len, line, col);
        
        this.currentElement = getElementByName(buffer, offset, len);
        this.currentElement.handleOpenElementStartAndName(buffer, offset, len, line, col, this.stack, this);

    }

    @Override
    public final void handleOpenElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {

        super.handleOpenElementEnd(buffer, offset, len, line, col);

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        this.currentElement.handleOpenElementEnd(buffer, offset, len, line, col, this.stack, this);
        this.currentElement = null;
        
    }

    
    
    
    @Override
    public final void handleCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        super.handleCloseElementStart(buffer, offset, len, line, col);
        //
        // Nothing to be done until we know the name of the element
        //
    }

    @Override
    public final void handleCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {

        super.handleCloseElementName(buffer, offset, len, line, col);
        
        this.currentElement = getElementByName(buffer, offset, len);
        this.currentElement.handleCloseElementStartAndName(buffer, offset, len, line, col, this.stack, this);
        
    }

    @Override
    public final void handleCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        super.handleCloseElementEnd(buffer, offset, len, line, col);

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        this.currentElement.handleCloseElementEnd(buffer, offset, len, line, col, this.stack, this);
        this.currentElement = null;

    }

    
    
    
    @Override
    public final void handleAutoCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        super.handleAutoCloseElementStart(buffer, offset, len, line, col);
        //
        // Nothing to be done until we know the name of the element
        //
    }

    @Override
    public final void handleAutoCloseElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleAutoCloseElementName(buffer, offset, len, line, col);
        
        this.currentElement = getElementByName(buffer, offset, len);
        this.currentElement.handleAutoCloseElementStartAndName(buffer, offset, len, line, col, this.stack, this);
        
    }

    @Override
    public final void handleAutoCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleAutoCloseElementEnd(buffer, offset, len, line, col);

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        this.currentElement.handleAutoCloseElementEnd(buffer, offset, len, line, col, this.stack, this);
        this.currentElement = null;
        
    }

    
    
    
    @Override
    public final void handleUnmatchedCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        super.handleUnmatchedCloseElementStart(buffer, offset, len, line, col);
        //
        // Nothing to be done until we know the name of the element
        //
    }

    @Override
    public final void handleUnmatchedCloseElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleUnmatchedCloseElementName(buffer, offset, len, line, col);
        
        this.currentElement = getElementByName(buffer, offset, len);
        this.currentElement.handleUnmatchedCloseElementStartAndName(buffer, offset, len, line, col, this.stack, this);
        
    }

    @Override
    public final void handleUnmatchedCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleUnmatchedCloseElementEnd(buffer, offset, len, line, col);

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        this.currentElement.handleUnmatchedCloseElementEnd(buffer, offset, len, line, col, this.stack, this);
        this.currentElement = null;
        
    }
    
    
    
    
    @Override
    public final void handleAttribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, 
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {

        super.handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                valueLine, valueCol);

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot handle attribute: no current element");
        }
        
        this.currentElement.handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                valueLine, valueCol, this.stack, this);
        
    }




    @Override
    public final void handleAttributeSeparator(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        super.handleAttributeSeparator(buffer, offset, len, line, col);

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot handle attribute: no current element");
        }
        
        this.currentElement.handleAttributeSeparator(buffer, offset, len, line, col, this.stack, this);
        
    }

    
    
    
    
    
    /*
     * *********************
     * *                   *
     * * DOCUMENT HANDLING *
     * *                   *
     * *********************
     */


    @SuppressWarnings("unused")
    public void handleDocumentStart(
            final long startTimeNanos, 
            final int line, final int col,
            final HtmlParsingConfiguration parsingConfiguration)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    
    @SuppressWarnings("unused")
    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, 
            final int line, final int col, 
            final HtmlParsingConfiguration parsingConfiguration)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    

    
    
    /*
     * ************************************************************
     * *                                                          *
     * * IMPLEMENTATION OF IDetailedHtmlElementHandling INTERFACE *
     * *                                                          *
     * ************************************************************
     */
    

    /*
     * -------------------------------------------
     * STANDALONE ELEMENTS: <img ... />, <img ...> 
     * -------------------------------------------
     */

    public void handleHtmlStandaloneElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col,
            final boolean minimized) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleHtmlStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col,
            final boolean minimized)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleHtmlStandaloneElementEnd(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col,
            final boolean minimized)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    
    /*
     * -------------------------
     * OPEN ELEMENTS: <div ... >
     * -------------------------
     */
    
    public void handleHtmlOpenElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleHtmlOpenElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleHtmlOpenElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    
    /*
     * ----------------------
     * CLOSE ELEMENTS: </div>
     * ----------------------
     */
    
    public void handleHtmlCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleHtmlCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleHtmlCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    
    /*
     * ------------------------------------------------------------------------------
     * FORCED-CLOSE ELEMENTS: </div> auto-generated when the next read element cannot
     * be nested inside the current element (the one that's forcefully closed).
     * ------------------------------------------------------------------------------
     */
    
    public void handleHtmlSyntheticCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleHtmlSyntheticCloseElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleHtmlSyntheticCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    
    /*
     * -----------------------------------------------------------------------
     * UNMATCHED CLOSE ELEMENTS: </div> which have no corresponding open <div>
     * element.
     * -----------------------------------------------------------------------
     */
    
    public void handleHtmlIgnorableCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleHtmlIgnorableCloseElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleHtmlIgnorableCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }




    public void handleHtmlAttribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, 
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }




    public void handleHtmlAttributeSeparator(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    
    
}