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

import java.io.Reader;

import org.attoparser.content.IAttoContentHandler;
import org.attoparser.exception.AttoParseException;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public interface IAttoParser {
    
    
    public void parse(final String document, final IAttoContentHandler handler) 
            throws AttoParseException;
    
    public void parse(final char[] document, final IAttoContentHandler handler) 
            throws AttoParseException;
    
    public void parse(final char[] document, final int offset, final int len, final IAttoContentHandler handler) 
            throws AttoParseException;
    
    public void parse(final Reader reader, final IAttoContentHandler handler) 
            throws AttoParseException;


    
}
