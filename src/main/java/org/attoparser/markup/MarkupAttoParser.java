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

import org.attoparser.AbstractBufferedAttoParser;
import org.attoparser.IAttoHandler;
import org.attoparser.exception.AttoParseException;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class MarkupAttoParser extends AbstractBufferedAttoParser {


    
    public MarkupAttoParser() {
        super();
    }
    

    
    
    
    
    protected final BufferParseResult parseBuffer(
            final char[] buffer, final int offset, final int len, 
            final IAttoHandler handler, final int line, final int col) 
            throws AttoParseException {


        MarkupParsingLocator locator = new MarkupParsingLocator(line, col);
        
        int currentLine = locator.getLine();
        int currentCol = locator.getCol();
        
        final int maxi = offset + len;
        int i = offset;
        int current = i;
        
        boolean inTag = false;
        boolean inClosingTag = false;
        boolean inComment = false;
        boolean inCdata = false;
        
        int tagStart;
        int tagEnd;
        
        while (i < maxi) {
            
            currentLine = locator.getLine();
            currentCol = locator.getCol();
            
            if (!inTag) {
                
                tagStart = MarkupAttoParserUtil.findNext(buffer, i, maxi, '<', inTag, locator);
                
                if (tagStart == -1) {
                    return new BufferParseResult(current, currentLine, currentCol);
                }
            
                if (tagStart > current) {
                    // We avoid empty-string text events
                    handler.text(
                            buffer, current, (tagStart - current), 
                            currentLine, currentCol);
                }
                
                current = tagStart;
                i = current;
                inTag = true;
                
            } else {

                inClosingTag = false;
                inComment = false;
                inCdata = false;
                
                if (maxi - current > 1 &&
                        buffer[current + 1] == '/') {

                    inClosingTag = true;
                    inTag = false;
                    
                } else if (maxi - current > 3 &&
                        buffer[current + 1] == '!' && 
                        buffer[current + 2] == '-' && 
                        buffer[current + 3] == '-') {
                    
                    inComment = true;
                    inTag = false;
                    
                } else if (maxi - current > 8 &&
                        buffer[current + 1] == '!' && 
                        buffer[current + 2] == '[' && 
                        buffer[current + 3] == 'C' &&
                        buffer[current + 4] == 'D' &&
                        buffer[current + 5] == 'A' &&
                        buffer[current + 6] == 'T' &&
                        buffer[current + 7] == 'A' &&
                        buffer[current + 8] == '[') {
                    
                    inCdata = true;
                    inTag = false;
                    
                }
                        
                
                tagEnd = MarkupAttoParserUtil.findNext(buffer, i, maxi, '>', inTag, locator);
                
                if (tagEnd == -1) {
                    // This is an unfinished structure
                    return new BufferParseResult(current, currentLine, currentCol);
                }
                
                if (inClosingTag) {
                    // This is a closing tag
                    
                    handler.structure(buffer, (current + 1), (tagEnd - (current + 1)), currentLine, currentCol);
                    
                } else if (inComment) {
                    // This is a comment! (obviously ;-))
                    
                    while (tagEnd - current < 7 || buffer[tagEnd - 1] != '-' || buffer[tagEnd - 2] != '-') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        MarkupAttoParserUtil.countChar(buffer[tagEnd], locator);
                        tagEnd = MarkupAttoParserUtil.findNext(buffer, tagEnd + 1, maxi, '>', false, locator);
                        
                        if (tagEnd == -1) {
                            return new BufferParseResult(current, currentLine, currentCol);
                        }
                        
                    }
                    
                    handler.structure(buffer, (current + 1), (tagEnd - (current + 1)), currentLine, currentCol);
                    
                } else if (inCdata) {
                    // This is a CDATA section
                    
                    while (tagEnd - current < 12 || buffer[tagEnd - 1] != ']' || buffer[tagEnd - 2] != ']') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        MarkupAttoParserUtil.countChar(buffer[tagEnd], locator);
                        tagEnd = MarkupAttoParserUtil.findNext(buffer, tagEnd + 1, maxi, '>', false, locator);
                        
                        if (tagEnd == -1) {
                            return new BufferParseResult(current, currentLine, currentCol);
                        }
                        
                    }
                    
                    handler.structure(buffer, (current + 1), (tagEnd - (current + 1)), currentLine, currentCol);
                    
                } else {

                    handler.structure(buffer, (current + 1), (tagEnd - (current + 1)), currentLine, currentCol);
                    
                }
                
                // The '>' char will be considered as processed too
                MarkupAttoParserUtil.countChar(buffer[tagEnd], locator);
                
                current = tagEnd + 1;
                i = current;
                inTag = false;
                
            }
            
        }
        
        return new BufferParseResult(current, locator.getLine(), locator.getCol());
        
    }
    
    
}
