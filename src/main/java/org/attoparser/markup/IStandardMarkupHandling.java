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

import java.util.Map;

import org.attoparser.AttoParseException;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public interface IStandardMarkupHandling {

    
    public void startDocument()
            throws AttoParseException;

    
    public void endDocument()
            throws AttoParseException;
    
    
    public void standaloneElement(
            final String elementName, final Map<String,String> attributes,
            final int line, final int col)
            throws AttoParseException;

    
    public void openElement(
            final String elementName, final Map<String,String> attributes,
            final int line, final int col)
            throws AttoParseException;
    
    
    public void closeElement(
            final String elementName, final int line, final int col)
            throws AttoParseException;
    
    
    public void xmlDeclaration(
            final String version,
            final String encoding,
            final String standalone,
            final int line, final int col)
            throws AttoParseException;

    
    public void docType(
            final String elementName, final String publicId, final String systemId,
            final String internalSubset, final int line, final int col)
            throws AttoParseException;
    

    public void processingInstruction(
            final String target, final String content, 
            int line, int col) 
            throws AttoParseException;

    
    public void text(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException;
    
    
    public void comment(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException;
    
    
    public void cdata(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException;
    
}
