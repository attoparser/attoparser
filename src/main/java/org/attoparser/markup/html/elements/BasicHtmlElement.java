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
import org.attoparser.markup.html.HtmlElementStack;
import org.attoparser.markup.html.IDetailedHtmlElementHandling;
import org.attoparser.markup.html.warnings.HtmlParsingEventWarnings;







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

    
    
    
    public void handleStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler)
            throws AttoParseException {

        stack.openElement(this);
        
        handler.handleHtmlStandaloneElementStart(buffer, nameOffset, nameLen, line, col, true, HtmlParsingEventWarnings.WARNINGS_NONE);
        
    }

    
    public void handleStandaloneElementEnd(
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlStandaloneElementEnd(line, col, true);

        stack.closeElement();
        
    }

    
    
    
    public void handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        stack.openElement(this);
        
        handler.handleHtmlOpenElementStart(buffer, nameOffset, nameLen, line, col, HtmlParsingEventWarnings.WARNINGS_NONE);

    }

    
    public void handleOpenElementEnd(
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlOpenElementEnd(line, col);
        
    }


    
    
    public void handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlCloseElementStart(buffer, nameOffset, nameLen, line, col, HtmlParsingEventWarnings.WARNINGS_NONE);

    }

    
    public void handleCloseElementEnd(
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlCloseElementEnd(line, col);

        stack.closeElement();
        
    }

    
    
    
    public final void handleAutoCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {

        throw new IllegalStateException(
                "Parsing error: no Autobalancing should be performed during HTML parsing");
        
    }
    
    
    public final void handleAutoCloseElementEnd(
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {

        throw new IllegalStateException(
                "Parsing error: no Autobalancing should be performed during HTML parsing");
        
    }

    
    
    
    public final void handleUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        // Stack should not be affected by this, simply delegate the event
        handler.handleHtmlCloseElementStart(buffer, nameOffset, nameLen, line, col, HtmlParsingEventWarnings.WARNINGS_IGNORABLE_UNMATCHED_CLOSE_ELEMENT);
        
    }

    
    public final void handleUnmatchedCloseElementEnd(
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler)
            throws AttoParseException {
        
        // Stack should not be affected by this, simply delegate the event
        handler.handleHtmlCloseElementEnd(line, col);
        
    }

    
    
    
    public final void handleAttribute(
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
        handler.handleHtmlAttribute(buffer, nameOffset, nameLen, nameLine, nameCol, operatorOffset, operatorLen, operatorLine, operatorCol, valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);
        
    }

    
    public final void handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler)
            throws AttoParseException {
        
        // Stack should not be affected by this, simply delegate the event
        handler.handleHtmlInnerWhiteSpace(buffer, offset, len, line, col);
        
    }
    
    
}