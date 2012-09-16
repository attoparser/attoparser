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
package org.attoparser.content;

import org.attoparser.exception.AttoParseException;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public interface IAttoContentHandler {

    public void startDocument()
            throws AttoParseException;
    
    public void endDocument()
            throws AttoParseException;
    
    public void startElement(char[] buffer, final int offset, final int len, final boolean hasBody,
            final int line, final int pos)
            throws AttoParseException;
    
    public void endElement(char[] buffer, final int offset, final int len,
            final int line, final int pos)
            throws AttoParseException;

    public void attribute(
            char[] nameBuffer, final int nameOffset, final int nameLen,
            char[] valueBuffer, final int valueOffset, final int valueLen,
            final int line, final int pos)
            throws AttoParseException;
    
    public void text(final char[] buffer, final int offset, final int len, 
            final int line, final int pos)
            throws AttoParseException;
    
    public void comment(final char[] buffer, final int offset, final int len, 
            final int line, final int pos)
            throws AttoParseException;
    
    public void cdata(final char[] buffer, final int offset, final int len, 
            final int line, final int pos) 
            throws AttoParseException;
    
}
