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
import org.attoparser.markup.ElementPreparationResult;
import org.attoparser.markup.IElementPreparationResult;
import org.attoparser.markup.html.IDetailedHtmlElementHandling;







/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class VoidHtmlElement extends BasicHtmlElement {

    
    
    
    public VoidHtmlElement(final String name) {
        super(name);
    }


    @Override
    public IElementPreparationResult prepareForElement(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IDetailedHtmlElementHandling handler)
            throws AttoParseException {

        return ElementPreparationResult.DONT_STACK;

    }


    @Override
    public IAttoHandleResult handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IDetailedHtmlElementHandling handler)
            throws AttoParseException {

        return handler.handleHtmlStandaloneElementStart(this, false, buffer, nameOffset, nameLen, line, col);

    }

    
    @Override
    public IAttoHandleResult handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IDetailedHtmlElementHandling handler)
            throws AttoParseException {

        return handler.handleHtmlStandaloneElementEnd(this, false, buffer, nameOffset, nameLen, line, col);

    }


    
    
    @Override
    public IAttoHandleResult handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IDetailedHtmlElementHandling handler)
            throws AttoParseException {

        return handler.handleHtmlUnmatchedCloseElementStart(this, buffer, nameOffset, nameLen, line, col);

    }

    
    @Override
    public IAttoHandleResult handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IDetailedHtmlElementHandling handler)
            throws AttoParseException {

        return handler.handleHtmlUnmatchedCloseElementEnd(this, buffer, nameOffset, nameLen, line, col);
        
    }

    
    
    
}