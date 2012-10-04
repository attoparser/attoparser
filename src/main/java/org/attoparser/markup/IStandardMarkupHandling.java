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

    
    public void handleDocumentStart()
            throws AttoParseException;

    
    public void handleDocumentEnd()
            throws AttoParseException;
    
    
    public void handleStandaloneElement(
            final String elementName, final Map<String,String> attributes,
            final int line, final int col)
            throws AttoParseException;

    
    public void handleOpenElement(
            final String elementName, final Map<String,String> attributes,
            final int line, final int col)
            throws AttoParseException;
    
    
    public void handleCloseElement(
            final String elementName, final int line, final int col)
            throws AttoParseException;
    
    
    public void handleXmlDeclaration(
            final String version,
            final String encoding,
            final String standalone,
            final int line, final int col)
            throws AttoParseException;

    
    public void handleDocType(
            final String elementName, final String publicId, final String systemId,
            final String internalSubset, final int line, final int col)
            throws AttoParseException;
    

    public void handleProcessingInstruction(
            final String target, final String content, 
            int line, int col) 
            throws AttoParseException;

    
    public void handleText(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException;
    
    
    public void handleComment(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException;
    
    
    public void handleCDATASection(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException;
    
}
