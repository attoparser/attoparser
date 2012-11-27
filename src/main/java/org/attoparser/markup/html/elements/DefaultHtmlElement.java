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
public final class DefaultHtmlElement extends AbstractHtmlElement {

    
    public DefaultHtmlElement(final String name) {
        super(name);
    }

    public void handleStandaloneElementStartAndName(char[] buffer, int offset,
            int len, int line, int col, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
System.out.print("[" + System.identityHashCode(this) + "] ");
handler.handleHtmlClosedStandaloneElementName(buffer, offset, len, line, col);
    }

    public void handleStandaloneElementEnd(char[] buffer, int offset, int len,
            int line, int col, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
        
    }

    public void handleOpenElementStartAndName(char[] buffer, int offset,
            int len, int line, int col, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
System.out.print("[" + System.identityHashCode(this) + "] ");
handler.handleHtmlOpenElementName(buffer, offset, len, line, col);
    }

    public void handleOpenElementEnd(char[] buffer, int offset, int len,
            int line, int col, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
        
    }

    public void handleCloseElementStartAndName(char[] buffer, int offset,
            int len, int line, int col, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
System.out.print("[" + System.identityHashCode(this) + "] ");
handler.handleHtmlCloseElementName(buffer, offset, len, line, col);
    }

    public void handleCloseElementEnd(char[] buffer, int offset, int len,
            int line, int col, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
        
    }

    public void handleAutoCloseElementStartAndName(char[] buffer, int offset,
            int len, int line, int col, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
        
    }

    public void handleAutoCloseElementEnd(char[] buffer, int offset, int len,
            int line, int col, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
        
    }

    public void handleUnmatchedCloseElementStartAndName(char[] buffer,
            int offset, int len, int line, int col, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
        
    }

    public void handleUnmatchedCloseElementEnd(char[] buffer, int offset,
            int len, int line, int col, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
        
    }

    public void handleAttribute(char[] buffer, int nameOffset, int nameLen,
            int nameLine, int nameCol, int operatorOffset, int operatorLen,
            int operatorLine, int operatorCol, int valueContentOffset,
            int valueContentLen, int valueOuterOffset, int valueOuterLen,
            int valueLine, int valueCol, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
        
    }

    public void handleAttributeSeparator(char[] buffer, int offset, int len,
            int line, int col, HtmlElementStack stack,
            IDetailedHtmlElementHandling handler) throws AttoParseException {
        // TODO Auto-generated method stub
        
    }


    
    
    
    
    
    
}