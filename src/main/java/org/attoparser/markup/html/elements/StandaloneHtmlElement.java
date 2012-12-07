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
    public void handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        stack.openElement(this);
        
        handler.handleHtmlNonMinimizedStandaloneElementStart(buffer, nameOffset, nameLen, line, col);

    }

    
    @Override
    public void handleOpenElementEnd(
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlNonMinimizedStandaloneElementEnd(line, col);
        
    }


    
    
    @Override
    public void handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlIgnorableCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }

    
    @Override
    public void handleCloseElementEnd(
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {
        
        handler.handleHtmlIgnorableCloseElementEnd(line, col);

        stack.closeElement();
        
    }

    
    
    
}