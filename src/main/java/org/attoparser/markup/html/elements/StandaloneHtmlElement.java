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
public class StandaloneHtmlElement extends BasicHtmlElement {

    
    
    
    public StandaloneHtmlElement(final String name) {
        super(name);
    }

    
    
    
    
    @Override
    public void handleOpenElementStartAndName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        stack.openElement(this);
        
        handler.handleHtmlUnclosedStandaloneElementStart(OPEN_ELEMENT_START, 0, OPEN_ELEMENT_START.length, line, col - 1);
        handler.handleHtmlUnclosedStandaloneElementName(buffer, offset, len, line, col);

    }

    
    @Override
    public void handleOpenElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlUnclosedStandaloneElementEnd(OPEN_ELEMENT_END, 0, OPEN_ELEMENT_END.length, line, col);
        
    }


    
    
    @Override
    public void handleCloseElementStartAndName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlUnmatchedCloseElementStart(CLOSE_ELEMENT_START, 0, CLOSE_ELEMENT_START.length, line, col - 2);
        handler.handleHtmlUnmatchedCloseElementName(buffer, offset, len, line, col);

    }

    
    @Override
    public void handleCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlUnmatchedCloseElementEnd(CLOSE_ELEMENT_END, 0, CLOSE_ELEMENT_END.length, line, col);

        stack.closeElement();
        
    }

    
    
    
}