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
import org.attoparser.markup.IElementPreparationResult;
import org.attoparser.markup.IMarkupAttoHandler;







/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
class BasicHtmlElement extends AbstractHtmlElement {



    
    public BasicHtmlElement(final String name) {
        super(name);
    }


    public IElementPreparationResult prepareForElement(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupAttoHandler handler)
            throws AttoParseException {

        // No preparation to be done by default, no need to delegate this (it's the elements who know
        // if they need preparation or not)
        return null;

    }


    public IAttoHandleResult handleStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col, 
            final IMarkupAttoHandler handler)
            throws AttoParseException {

        return handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);
        
    }

    
    public IAttoHandleResult handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col,
            final IMarkupAttoHandler handler)
            throws AttoParseException {

        return handler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);
        
    }

    
    
    
    public IAttoHandleResult handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupAttoHandler handler)
            throws AttoParseException {
        
        return handler.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);

    }

    
    public IAttoHandleResult handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupAttoHandler handler)
            throws AttoParseException {
        
        return handler.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);
        
    }


    
    
    public IAttoHandleResult handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupAttoHandler handler)
            throws AttoParseException {
        
        return handler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }

    
    public IAttoHandleResult handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupAttoHandler handler)
            throws AttoParseException {

        return handler.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        
    }

    
    
    
    public IAttoHandleResult handleAutoCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupAttoHandler handler)
            throws AttoParseException {

        return handler.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }
    
    
    public IAttoHandleResult handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupAttoHandler handler)
            throws AttoParseException {

        return handler.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);

    }

    
    
    
    public final IAttoHandleResult handleUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupAttoHandler handler)
            throws AttoParseException {
        
        return handler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);
        
    }

    
    public final IAttoHandleResult handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupAttoHandler handler)
            throws AttoParseException {
        
        return handler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        
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
            final IMarkupAttoHandler handler)
            throws AttoParseException {
        
        return handler.handleAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);
        
    }

    
    public final IAttoHandleResult handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col, 
            final IMarkupAttoHandler handler)
            throws AttoParseException {
        
        return handler.handleInnerWhiteSpace(buffer, offset, len, line, col);
        
    }
    
    
}