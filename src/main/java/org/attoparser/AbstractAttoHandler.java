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

import org.attoparser.exception.AttoParseException;




/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractAttoHandler implements IAttoHandler {

    
    protected AbstractAttoHandler() {
        super();
    }
    
    
    public void startDocument() throws AttoParseException {
        // Nothing to be done here
    }

    
    public void endDocument() throws AttoParseException {
        // Nothing to be done here
    }


    public void text(final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here
    }

    public void structure(final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here
    }
    
    
}