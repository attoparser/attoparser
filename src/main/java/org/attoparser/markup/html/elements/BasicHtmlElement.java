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
package org.attoparser.markup.html.elements;

import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.markup.html.IDetailedHtmlElementHandling;







/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class BasicHtmlElement extends AbstractHtmlElement {



    
    public BasicHtmlElement(final String name) {
        super(name);
    }

    
    
    
    public IAttoHandleResult handleStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler)
            throws AttoParseException {

        stack.openElement(this);
        
        return handler.handleHtmlStandaloneElementStart(this, true, buffer, nameOffset, nameLen, line, col);
        
    }

    
    public IAttoHandleResult handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {

        final IAttoHandleResult result =
            handler.handleHtmlStandaloneElementEnd(this, true, buffer, nameOffset, nameLen, line, col);

        stack.closeElement();

        return result;
        
    }

    
    
    
    public IAttoHandleResult handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        stack.openElement(this);
        
        return handler.handleHtmlOpenElementStart(this, buffer, nameOffset, nameLen, line, col);

    }

    
    public IAttoHandleResult handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        return handler.handleHtmlOpenElementEnd(this, buffer, nameOffset, nameLen, line, col);
        
    }


    
    
    public IAttoHandleResult handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        return handler.handleHtmlCloseElementStart(this, buffer, nameOffset, nameLen, line, col);

    }

    
    public IAttoHandleResult handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {

        final IAttoHandleResult result =
            handler.handleHtmlCloseElementEnd(this, buffer, nameOffset, nameLen, line, col);

        stack.closeElement();

        return result;
        
    }

    
    
    
    public final IAttoHandleResult handleAutoCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {

        throw new IllegalStateException(
                "Parsing error: no Autobalancing should be performed during HTML parsing");
        
    }
    
    
    public final IAttoHandleResult handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {

        throw new IllegalStateException(
                "Parsing error: no Autobalancing should be performed during HTML parsing");
        
    }

    
    
    
    public final IAttoHandleResult handleUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        // Stack should not be affected by this, simply delegate the event
        return handler.handleHtmlCloseElementStart(this, buffer, nameOffset, nameLen, line, col);
        
    }

    
    public final IAttoHandleResult handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler)
            throws AttoParseException {
        
        // Stack should not be affected by this, simply delegate the event
        return handler.handleHtmlCloseElementEnd(this, buffer, nameOffset, nameLen, line, col);
        
    }

    
    
    
    public final IAttoHandleResult handleAttribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, 
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        // Stack should not be affected by this, simply delegate the event
        return handler.handleHtmlAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);
        
    }

    
    public final IAttoHandleResult handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler)
            throws AttoParseException {
        
        // Stack should not be affected by this, simply delegate the event
        return handler.handleHtmlInnerWhiteSpace(buffer, offset, len, line, col);
        
    }
    
    
}