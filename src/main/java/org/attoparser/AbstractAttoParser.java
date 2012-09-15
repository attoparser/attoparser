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

import org.attoparser.config.DefaultAttoConfig;
import org.attoparser.config.IAttoConfig;
import org.attoparser.content.IAttoContentHandler;
import org.attoparser.exception.AttoParseException;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractAttoParser implements IAttoParser {

    
    private static final DefaultAttoConfig DEFAULT_ATTO_CONFIG = new DefaultAttoConfig();

    
    
    public AbstractAttoParser() {
        super();
    }
    

    
    public final void parse(final String document, final IAttoContentHandler handler) 
            throws AttoParseException {
        parse(document, handler, DEFAULT_ATTO_CONFIG);
    }
    
    public final void parse(final char[] document, final IAttoContentHandler handler) 
            throws AttoParseException {
        parse(document, handler, DEFAULT_ATTO_CONFIG);
    }

    public final void parse(final String document, final int offset, final int len, final IAttoContentHandler handler) 
            throws AttoParseException {
        parse(document, offset, len, handler, DEFAULT_ATTO_CONFIG);
    }
    
    public final void parse(final char[] document, final int offset, final int len, final IAttoContentHandler handler) 
            throws AttoParseException {
        parse(document, offset, len, handler, DEFAULT_ATTO_CONFIG);
    }
    
    public final void parse(final String document, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException  {
        parse(document.toCharArray(), handler, config);
    }
    
    public final void parse(final char[] document, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException {
        parse(document, 0, document.length, handler, config);
    }

    public final void parse(
            final String document, final int offset, final int len, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException {
        // We avoid copying the entire String if we only need some chars
        final char[] doc = new char[len];
        document.getChars(offset, (offset + len), doc, 0);
        parse(doc, 0, len, handler, config);
    }
    
    
}
