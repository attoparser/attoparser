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
package org.attoparser;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
class HtmlBasicElement extends HtmlAbstractElement {



    
    public HtmlBasicElement(final String name) {
        super(name);
    }


    public void handleStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col, 
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {

        handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);
        
    }

    
    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {

        handler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);
        
    }

    
    
    
    public void handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {
        
        handler.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);

    }

    
    public void handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {
        
        handler.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);
        
    }


    
    
    public void handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {
        
        handler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }

    
    public void handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {

        handler.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        
    }

    
    
    
    public void handleAutoCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {

        handler.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }
    
    
    public void handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {

        handler.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);

    }

    
    
    
    public void handleUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {
        
        handler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);
        
    }

    
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {
        
        handler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        
    }

    
    
    
    public void handleAttribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, 
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol, 
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {
        
        handler.handleAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);
        
    }

    
    public void handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col, 
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {
        
        handler.handleInnerWhiteSpace(buffer, offset, len, line, col);
        
    }
    
    
}