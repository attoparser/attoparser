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
import org.attoparser.IAttoHandleResult;
import org.attoparser.markup.AbstractDetailedMarkupAttoHandler;
import org.attoparser.markup.MarkupParsingConfiguration;
import org.attoparser.markup.html.elements.BasicHtmlElement;
import org.attoparser.markup.html.elements.HtmlElementStack;
import org.attoparser.markup.html.elements.HtmlElements;
import org.attoparser.markup.html.elements.IHtmlElement;





/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public abstract class AbstractDetailedNonValidatingHtmlAttoHandler 
        extends AbstractDetailedMarkupAttoHandler
        implements IDetailedHtmlElementHandling {



    private final HtmlElementStack stack;
    private IHtmlElement currentElement = null;

    
    
    protected AbstractDetailedNonValidatingHtmlAttoHandler(final MarkupParsingConfiguration configuration) {
        super(configuration);
        this.stack = new HtmlElementStack();
    }

    
    
    
    private static IHtmlElement getElementByName(final char[] buffer, final int offset, final int len) {
        final IHtmlElement element = HtmlElements.forName(buffer, offset, len);
        if (element == null) {
            return new BasicHtmlElement(new String(buffer, offset, len));
        }
        return element;
    }
    
    
    
    

    
    

    /*
     * ----------------------------------------------------------------------
     * NOTE no implementation is needed at this level of:
     *   handleDocumentStart(long, int, int, MarkupParsingConfiguration)
     *   handleDocumentEnd(long, long, int, int, MarkupParsingConfiguration)
     * ----------------------------------------------------------------------
     */

    
    
    
    
    
    @Override
    public final IAttoHandleResult handleStandaloneElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        
        this.currentElement = getElementByName(buffer, offset, len);
        return this.currentElement.handleStandaloneElementStart(buffer, offset, len, line, col, this.stack, this);

    }

    @Override
    public final IAttoHandleResult handleStandaloneElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        
        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IAttoHandleResult result =
            this.currentElement.handleStandaloneElementEnd(buffer, offset, len, line, col, this.stack, this);
        this.currentElement = null;

        return result;

    }


    
    
    @Override
    public final IAttoHandleResult handleOpenElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        
        this.currentElement = getElementByName(buffer, offset, len);
        return this.currentElement.handleOpenElementStart(buffer, offset, len, line, col, this.stack, this);

    }

    @Override
    public final IAttoHandleResult handleOpenElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IAttoHandleResult result =
            this.currentElement.handleOpenElementEnd(buffer, offset, len, line, col, this.stack, this);
        this.currentElement = null;

        return result;

    }

    
    
    
    @Override
    public final IAttoHandleResult handleCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        
        this.currentElement = getElementByName(buffer, offset, len);
        return this.currentElement.handleCloseElementStart(buffer, offset, len, line, col, this.stack, this);
        
    }

    @Override
    public final IAttoHandleResult handleCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IAttoHandleResult result =
            this.currentElement.handleCloseElementEnd(buffer, offset, len, line, col, this.stack, this);
        this.currentElement = null;

        return result;

    }

    
    
    
    @Override
    public final IAttoHandleResult handleAutoCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        
        this.currentElement = getElementByName(buffer, offset, len);
        return this.currentElement.handleAutoCloseElementStart(buffer, offset, len, line, col, this.stack, this);
        
    }

    @Override
    public final IAttoHandleResult handleAutoCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IAttoHandleResult result =
            this.currentElement.handleAutoCloseElementEnd(buffer, offset, len, line, col, this.stack, this);
        this.currentElement = null;

        return result;

    }

    
    
    
    @Override
    public final IAttoHandleResult handleUnmatchedCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        
        this.currentElement = getElementByName(buffer, offset, len);
        return this.currentElement.handleUnmatchedCloseElementStart(buffer, offset, len, line, col, this.stack, this);
        
    }

    @Override
    public final IAttoHandleResult handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IAttoHandleResult result =
            this.currentElement.handleUnmatchedCloseElementEnd(buffer, offset, len, line, col, this.stack, this);
        this.currentElement = null;

        return result;

    }
    
    
    
    
    @Override
    public final IAttoHandleResult handleAttribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, 
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot handle attribute: no current element");
        }
        
        return this.currentElement.handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                valueLine, valueCol, this.stack, this);
        
    }




    @Override
    public final IAttoHandleResult handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot handle attribute: no current element");
        }
        
        return this.currentElement.handleInnerWhiteSpace(buffer, offset, len, line, col, this.stack, this);
        
    }

    
    
    



    
    /*
     * ************************************************************
     * *                                                          *
     * * IMPLEMENTATION OF IDetailedHtmlElementHandling INTERFACE *
     * *                                                          *
     * ************************************************************
     */
    

    /*
     * -------------------------------------------------
     * STANDALONE ELEMENTS: <img ... />, <img ... >, etc 
     * -------------------------------------------------
     */

    public IAttoHandleResult handleHtmlStandaloneElementStart(
            final IHtmlElement element,
            final boolean minimized,
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    public IAttoHandleResult handleHtmlStandaloneElementEnd(
            final IHtmlElement element,
            final boolean minimized,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    
    /*
     * -------------------------
     * OPEN ELEMENTS: <div ... >
     * -------------------------
     */
    
    public IAttoHandleResult handleHtmlOpenElementStart(
            final IHtmlElement element,
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    public IAttoHandleResult handleHtmlOpenElementEnd(
            final IHtmlElement element,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    
    /*
     * ----------------------
     * CLOSE ELEMENTS: </div>
     * ----------------------
     */
    
    public IAttoHandleResult handleHtmlCloseElementStart(
            final IHtmlElement element,
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    public IAttoHandleResult handleHtmlCloseElementEnd(
            final IHtmlElement element,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    /*
     * ------------------------------------------
     * AUTO CLOSE ELEMENTS (because of balancing)
     * ------------------------------------------
     */

    public IAttoHandleResult handleHtmlAutoCloseElementStart(
            final IHtmlElement element,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    public IAttoHandleResult handleHtmlAutoCloseElementEnd(
            final IHtmlElement element,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    /*
     * ------------------------------------------------
     * UNMATCHED CLOSE ELEMENTS: </div> without a <div>
     * ------------------------------------------------
     */

    public IAttoHandleResult handleHtmlUnmatchedCloseElementStart(
            final IHtmlElement element,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    public IAttoHandleResult handleHtmlUnmatchedCloseElementEnd(
            final IHtmlElement element,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    
    /*
     * -----------------------------------------------------------------------
     * ATTRIBUTES
     * -----------------------------------------------------------------------
     */
    
    public IAttoHandleResult handleHtmlAttribute(
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
        return null;
    }




    public IAttoHandleResult handleHtmlInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }



}