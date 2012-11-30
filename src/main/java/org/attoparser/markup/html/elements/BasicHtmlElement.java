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







/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class BasicHtmlElement extends AbstractHtmlElement {


    protected static final char[] OPEN_ELEMENT_START = "<".toCharArray(); 
    protected static final char[] OPEN_ELEMENT_END = ">".toCharArray(); 
    protected static final char[] CLOSE_ELEMENT_START = "</".toCharArray(); 
    protected static final char[] CLOSE_ELEMENT_END = ">".toCharArray(); 
    protected static final char[] MINIMIZED_ELEMENT_END = "/>".toCharArray(); 
    
    
    
    public BasicHtmlElement(final String name) {
        super(name);
    }

    
    
    
    public void handleStandaloneElementStartAndName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler)
            throws AttoParseException {

        stack.openElement(this);
        
        handler.handleHtmlClosedStandaloneElementStart(OPEN_ELEMENT_START, 0, OPEN_ELEMENT_START.length, line, col - 1);
        handler.handleHtmlClosedStandaloneElementName(buffer, offset, len, line, col);
        
    }

    
    public void handleStandaloneElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlClosedStandaloneElementEnd(MINIMIZED_ELEMENT_END, 0, MINIMIZED_ELEMENT_END.length, line, col);

        stack.closeElement();
        
    }

    
    
    
    public void handleOpenElementStartAndName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        stack.openElement(this);
        
        handler.handleHtmlOpenElementStart(OPEN_ELEMENT_START, 0, OPEN_ELEMENT_START.length, line, col - 1);
        handler.handleHtmlOpenElementName(buffer, offset, len, line, col);

    }

    
    public void handleOpenElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlOpenElementEnd(OPEN_ELEMENT_END, 0, OPEN_ELEMENT_END.length, line, col);
        
    }


    
    
    public void handleCloseElementStartAndName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlCloseElementStart(CLOSE_ELEMENT_START, 0, CLOSE_ELEMENT_START.length, line, col - 2);
        handler.handleHtmlCloseElementName(buffer, offset, len, line, col);

    }

    
    public void handleCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlCloseElementEnd(CLOSE_ELEMENT_END, 0, CLOSE_ELEMENT_END.length, line, col);

        stack.closeElement();
        
    }

    
    
    
    public final void handleAutoCloseElementStartAndName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {

        throw new IllegalStateException(
                "Parsing error: no Autobalancing should be performed during HTML parsing");
        
    }
    
    
    public final void handleAutoCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {

        throw new IllegalStateException(
                "Parsing error: no Autobalancing should be performed during HTML parsing");
        
    }

    
    
    
    public final void handleUnmatchedCloseElementStartAndName(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        // Stack should not be affected by this, simply delegate the event
        handler.handleHtmlUnmatchedCloseElementStart(CLOSE_ELEMENT_START, 0, CLOSE_ELEMENT_START.length, line, col - 2);
        handler.handleHtmlUnmatchedCloseElementName(buffer, offset, len, line, col);
        
    }

    
    public final void handleUnmatchedCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler)
            throws AttoParseException {
        
        // Stack should not be affected by this, simply delegate the event
        handler.handleHtmlUnmatchedCloseElementEnd(CLOSE_ELEMENT_END, 0, CLOSE_ELEMENT_END.length, line, col);
        
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

    
    public final void handleAttributeSeparator(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler)
            throws AttoParseException {
        
        // Stack should not be affected by this, simply delegate the event
        handler.handleHtmlAttributeSeparator(buffer, offset, len, line, col);
        
    }
    
    
}