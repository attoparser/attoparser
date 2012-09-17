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
package org.attoparser.markup;

import org.attoparser.exception.AttoParseException;






/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractMarkupAttoHandler 
        extends AbstractBasicMarkupAttoHandler
        implements IElementNameAndAttributeHandling {


    
    protected AbstractMarkupAttoHandler() {
        super();
    }

    
    @Override
    public final void standaloneElement(final char[] buffer, final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        MarkupAttoParserUtil.parseStandaloneElementNameAndAttributes(buffer, offset, len, line, col, this);
        
    }

    
    @Override
    public final void openElement(final char[] buffer, final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        MarkupAttoParserUtil.parseOpenElementNameAndAttributes(buffer, offset, len, line, col, this);
        
    }

    
    @Override
    public final void closeElement(final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {

        MarkupAttoParserUtil.parseCloseElementNameAndAttributes(buffer, offset, len, line, col, this);

    }

    

    
    public void standaloneElementName(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here
    }

    
    public void openElementName(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here
    }

    
    public void closeElementName(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here
    }

    
    public void elementAttribute(
            final char[] nameBuffer, final int nameOffset, final int nameLen, 
            final char[] valueBuffer, final int valueOffset, final int valueLen,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here
    }


    
    
}