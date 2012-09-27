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
import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandler;



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
    

    
    
    
    
    @Override
    protected final BufferParseResult parseBuffer(
            final char[] buffer, final int offset, final int len, 
            final IAttoHandler handler, final int line, final int col) 
            throws AttoParseException {


        final MarkupParsingLocator locator = new MarkupParsingLocator(line, col);
        
        int currentLine = locator.line;
        int currentCol = locator.col;
        
        final int maxi = offset + len;
        int i = offset;
        int current = i;
        
        boolean inStructure = false;
        
        boolean inOpenElement = false;
        boolean inCloseElement = false;
        boolean inComment = false;
        boolean inCdata = false;
        boolean inDocType = false;
        boolean inXmlDeclaration = false;
        boolean inProcessingInstruction = false;
        
        int tagStart = -1;
        int tagEnd = -1;
        
        while (i < maxi) {
            
            currentLine = locator.line;
            currentCol = locator.col;
            
            inStructure =
                    (inOpenElement || inCloseElement || inComment || inCdata || inDocType || inXmlDeclaration || inProcessingInstruction);
            
            if (!inStructure) {
                
                tagStart = MarkupParsingUtil.findNextStructureStart(buffer, i, maxi, locator);
                
                if (tagStart == -1) {
                    return new BufferParseResult(current, currentLine, currentCol, false);
                }

                inOpenElement = ElementMarkupParsingUtil.isOpenElementStart(buffer, tagStart, maxi, false);
                if (!inOpenElement) {
                    inCloseElement = ElementMarkupParsingUtil.isCloseElementStart(buffer, tagStart, maxi, false);
                    if (!inCloseElement) {
                        inComment = CommentMarkupParsingUtil.isCommentStart(buffer, tagStart, maxi);
                        if (!inComment) {
                            inCdata = CdataMarkupParsingUtil.isCdataStart(buffer, tagStart, maxi);
                            if (!inCdata) {
                                inDocType = DocTypeMarkupParsingUtil.isDocTypeStart(buffer, tagStart, maxi);
                                if (!inDocType) {
                                    inXmlDeclaration = XmlDeclarationMarkupParsingUtil.isXmlDeclarationStart(buffer, tagStart, maxi);
                                    if (!inXmlDeclaration) {
                                        inProcessingInstruction = ProcessingInstructionMarkupParsingUtil.isProcessingInstructionStart(buffer, tagStart, maxi);
                                        if (!inProcessingInstruction) {
                                            // We test open/close elements again so that we can handle elements starting with "!" and avoid 
                                            // collisions with DOCTYPE
                                            inOpenElement = ElementMarkupParsingUtil.isOpenElementStart(buffer, tagStart, maxi, true);
                                            if (!inOpenElement) {
                                                inCloseElement = ElementMarkupParsingUtil.isCloseElementStart(buffer, tagStart, maxi, true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                inStructure =
                        (inOpenElement || inCloseElement || inComment || inCdata || inDocType || inXmlDeclaration || inProcessingInstruction);
                
                
                while (!inStructure) {
                    // We found a '<', but it cannot be considered a tag because it is not
                    // the beginning of any known structure
                    
                    MarkupParsingLocator.countChar(locator, buffer[tagStart]);
                    tagStart = MarkupParsingUtil.findNextStructureStart(buffer, tagStart + 1, maxi, locator);
                    
                    if (tagStart == -1) {
                        return new BufferParseResult(current, currentLine, currentCol, false);
                    }

                    inOpenElement = ElementMarkupParsingUtil.isOpenElementStart(buffer, tagStart, maxi, false);
                    if (!inOpenElement) {
                        inCloseElement = ElementMarkupParsingUtil.isCloseElementStart(buffer, tagStart, maxi, false);
                        if (!inCloseElement) {
                            inComment = CommentMarkupParsingUtil.isCommentStart(buffer, tagStart, maxi);
                            if (!inComment) {
                                inCdata = CdataMarkupParsingUtil.isCdataStart(buffer, tagStart, maxi);
                                if (!inCdata) {
                                    inDocType = DocTypeMarkupParsingUtil.isDocTypeStart(buffer, tagStart, maxi);
                                    if (!inDocType) {
                                        inXmlDeclaration = XmlDeclarationMarkupParsingUtil.isXmlDeclarationStart(buffer, tagStart, maxi);
                                        if (!inXmlDeclaration) {
                                            inProcessingInstruction = ProcessingInstructionMarkupParsingUtil.isProcessingInstructionStart(buffer, tagStart, maxi);
                                            if (!inProcessingInstruction) {
                                                // We test open/close elements again so that we can handle elements starting with "!" and avoid 
                                                // collisions with DOCTYPE
                                                inOpenElement = ElementMarkupParsingUtil.isOpenElementStart(buffer, tagStart, maxi, true);
                                                if (!inOpenElement) {
                                                    inCloseElement = ElementMarkupParsingUtil.isCloseElementStart(buffer, tagStart, maxi, true);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    inStructure =
                            (inOpenElement || inCloseElement || inComment || inCdata || inDocType || inXmlDeclaration || inProcessingInstruction);
                
                }
            
                
                if (tagStart > current) {
                    // We avoid empty-string text events
                    handler.text(
                            buffer, current, (tagStart - current), 
                            currentLine, currentCol);
                }
                
                current = tagStart;
                i = current;
                
            } else {

                // We do not include processing instructions here because their format
                // is undefined, and everything should be allowed except the "?>" sequence,
                // which will terminate the instruction.
                final boolean avoidQuotes =
                        (inOpenElement || inCloseElement || inDocType || inXmlDeclaration);

                
                tagEnd =
                        (inDocType?
                                DocTypeMarkupParsingUtil.findNextDocTypeStructureEnd(buffer, i, maxi, locator) :
                                (avoidQuotes?
                                        MarkupParsingUtil.findNextStructureEndAvoidQuotes(buffer, i, maxi, locator) :
                                        MarkupParsingUtil.findNextStructureEndDontAvoidQuotes(buffer, i, maxi, locator)));
                
                if (tagEnd < 0) {
                    // This is an unfinished structure
                    return new BufferParseResult(current, currentLine, currentCol, true);
                }

                
                if (inOpenElement) {
                    // This is a closing tag
                    
                    handler.structure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    inOpenElement = false;
                    
                } else if (inCloseElement) {
                    // This is a closing tag
                    
                    handler.structure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    inCloseElement = false;
                    
                } else if (inComment) {
                    // This is a comment! (obviously ;-))
                    
                    while (tagEnd - current < 7 || buffer[tagEnd - 1] != '-' || buffer[tagEnd - 2] != '-') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        MarkupParsingLocator.countChar(locator, buffer[tagEnd]);
                        tagEnd = MarkupParsingUtil.findNextStructureEndDontAvoidQuotes(buffer, tagEnd + 1, maxi, locator);
                        
                        if (tagEnd == -1) {
                            return new BufferParseResult(current, currentLine, currentCol, true);
                        }
                        
                    }
                    
                    handler.structure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    inComment = false;
                    
                } else if (inCdata) {
                    // This is a CDATA section
                    
                    while (tagEnd - current < 12 || buffer[tagEnd - 1] != ']' || buffer[tagEnd - 2] != ']') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        MarkupParsingLocator.countChar(locator, buffer[tagEnd]);
                        tagEnd = MarkupParsingUtil.findNextStructureEndDontAvoidQuotes(buffer, tagEnd + 1, maxi, locator);
                        
                        if (tagEnd == -1) {
                            return new BufferParseResult(current, currentLine, currentCol, true);
                        }
                        
                    }
                    
                    handler.structure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    inCdata = false;
                    
                } else if (inDocType) {
                    // This is a DOCTYPE clause
                    
                    handler.structure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    inDocType = false;
                    
                } else if (inXmlDeclaration) {
                    // This is an XML Declaration

                    handler.structure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    inXmlDeclaration = false;
                    
                } else if (inProcessingInstruction) {
                    // This is a processing instruction

                    while (tagEnd - current < 5 || buffer[tagEnd - 1] != '?') {
                        // the '>' we chose is not the PI-closing one. Let's find again

                        MarkupParsingLocator.countChar(locator, buffer[tagEnd]);
                        tagEnd = MarkupParsingUtil.findNextStructureEndDontAvoidQuotes(buffer, tagEnd + 1, maxi, locator);
                        
                        if (tagEnd == -1) {
                            return new BufferParseResult(current, currentLine, currentCol, true);
                        }
                        
                    }
                    

                    handler.structure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    inProcessingInstruction = false;
                    
                } else {

                    throw new IllegalStateException(
                            "Illegal parsing state: structure is not of a recognized type");
                    
                }
                
                // The '>' char will be considered as processed too
                MarkupParsingLocator.countChar(locator, buffer[tagEnd]);
                
                current = tagEnd + 1;
                i = current;
                
            }
            
        }
        
        return new BufferParseResult(current, locator.line, locator.col, false);
        
    }
    
    
}
