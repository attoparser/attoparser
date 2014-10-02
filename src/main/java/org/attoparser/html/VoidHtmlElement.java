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
package org.attoparser.html;

import org.attoparser.AttoParseException;
import org.attoparser.ElementPreparationResult;
import org.attoparser.IElementPreparationResult;
import org.attoparser.IMarkupAttoHandler;
import org.attoparser.MarkupParsingController;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
class VoidHtmlElement extends BasicHtmlElement {

    
    
    
    public VoidHtmlElement(final String name) {
        super(name);
    }


    @Override
    public IElementPreparationResult prepareForElement(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupAttoHandler handler,
            final MarkupParsingController parsingController)
            throws AttoParseException {

        return ElementPreparationResult.DONT_STACK;

    }


    @Override
    public void handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupAttoHandler handler,
            final MarkupParsingController parsingController)
            throws AttoParseException {

        handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, false, line, col);

    }

    
    @Override
    public void handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupAttoHandler handler,
            final MarkupParsingController parsingController)
            throws AttoParseException {

        handler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, false, line, col);

    }


    
    
    @Override
    public void handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupAttoHandler handler,
            final MarkupParsingController parsingController)
            throws AttoParseException {

        // Void elements have no closing tag, so these are always unmatched
        handler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }

    
    @Override
    public void handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupAttoHandler handler,
            final MarkupParsingController parsingController)
            throws AttoParseException {

        // Void elements have no closing tag, so these are always unmatched
        handler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        
    }


    @Override
    public void handleAutoCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupAttoHandler handler,
            final MarkupParsingController parsingController)
            throws AttoParseException {

        // Void elements have no closing tag, so these are always unmatched (note anyway that auto-closing for these
        // elements should never happen)
        handler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }


    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupAttoHandler handler,
            final MarkupParsingController parsingController)
            throws AttoParseException {

        // Void elements have no closing tag, so these are always unmatched (note anyway that auto-closing for these
        // elements should never happen)
        handler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);

    }


}