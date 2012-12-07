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
public interface IHtmlElement {
    
    public String getName();
    
    public boolean matches(final String elementName);
    public boolean matches(final char[] elementName);
    public boolean matches(final char[] elementNameBuffer, final int offset, final int len);
    

    
    public void handleStandaloneElementStart(
            final char[] buffer, final int offset, final int len, final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;
    
    public void handleStandaloneElementEnd(
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;

    
    
    public void handleOpenElementStart(
            final char[] buffer, final int offset, final int len, final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;

    public void handleOpenElementEnd(
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;

    
    
    public void handleCloseElementStart(
            final char[] buffer, final int offset, final int len, final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;

    public void handleCloseElementEnd(
            final int line, final int col,
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;

    
    
    public void handleAutoCloseElementStart(
            final char[] buffer, final int offset, final int len, final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;

    public void handleAutoCloseElementEnd(
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;
    

    
    public void handleUnmatchedCloseElementStart(
            final char[] buffer, final int offset, final int len, final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;

    public void handleUnmatchedCloseElementEnd(
            final int line, final int col,
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;
    
    
    
    public void handleAttribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen, final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen, final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol,
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;

    public void handleInnerWhiteSpace(
            final char[] buffer, final int offset, final int len, final int line, final int col,
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException;
    
}