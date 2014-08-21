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
import org.attoparser.AttoHandleResult;
import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.IAttoHandler;



/**
 * <p>
 *   Default implementation of the {@link org.attoparser.IAttoParser} interface, able of
 *   parsing XML and HTML markup.
 * </p>
 * <p>
 *   This parser reports as <i>structures</i>:
 * </p>
 * <ul>
 *   <li><b>Tags (a.k.a. <i>elements</i>)</b>: <tt>&lt;body&gt;</tt>, <tt>&lt;img/&gt;</tt>, 
 *       <tt>&lt;div class="content"&gt;</tt>, etc.</li>
 *   <li><b>Comments</b>: <tt>&lt;!-- this is a comment --&gt;</tt></li>
 *   <li><b>CDATA sections</b>: <tt>&lt;![CDATA[ ... ]]&gt;</tt></li>
 *   <li><b>DOCTYPE clauses</b>: <tt>&lt;!DOCTYPE html&gt;</tt></li>
 *   <li><b>XML Declarations</b>: <tt>&lt;?xml version="1.0"?&gt;</tt></li>
 *   <li><b>Processing Instructions</b>: <tt>&lt;?xsl-stylesheet ...?&gt;</tt></li>
 * </ul>
 * <p>
 *   This parser class is <b>thread-safe</b>. But take into account that, usually, the 
 *   {@link IAttoHandler} implementations passed to parsers for event handling are not.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class MarkupAttoParser extends AbstractBufferedAttoParser {

    private final boolean canSplitText;

    /**
     * <p>
     *   Creates a new instance of this parser.
     * </p>
     */
    public MarkupAttoParser() {
        this(false);
    }

    /**
     * <p>
     *   Creates a new instance of this parser.
     * </p>
     *
     * @param canSplitText if {@code true}, text nodes may be split and sent to the handler as multiple text nodes.  The
     *                  default is {@code false}.
     */
    public MarkupAttoParser(final boolean canSplitText) {
        super();
        this.canSplitText = canSplitText;
    }
    

    
    
    
    @Override
    protected final BufferParseResult parseBuffer(
            final char[] buffer, final int offset, final int len, 
            final IAttoHandler handler, final int line, final int col,
            final char[] skipUntilSequence)
            throws AttoParseException {


        final int[] locator = new int[] {line, col};
        
        int currentLine = locator[0];
        int currentCol = locator[1];
        
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

        char[] skipUntil = skipUntilSequence;

        int tagStart = -1;
        int tagEnd = -1;
        
        while (i < maxi) {

            currentLine = locator[0];
            currentCol = locator[1];

            if (skipUntil != null) {
                // We need to disable parsing until we find a specific character sequence.
                // This allows correct parsing of CDATA (not PCDATA) sections (e.g. <script> tags).
                final int sequenceIndex =
                        MarkupParsingUtil.findCharacterSequence(buffer, i, maxi, locator, skipUntil);
                if (sequenceIndex == -1) {

                    // Not found, should ask for more buffer
                    if (canSplitText) {

                        final IAttoHandleResult result =
                            handler.handleText(buffer, current, len - current, currentLine, currentCol);
                        if (result != null && result != AttoHandleResult.CONTINUE) {
                            skipUntil = result.getParsingDisableLimit();
                        }

                        current = len;

                    }

                    return new BufferParseResult(current, currentLine, currentCol, false, skipUntil);

                }
                i = sequenceIndex;
                skipUntil = null;
            }

            inStructure =
                    (inOpenElement || inCloseElement || inComment || inCdata || inDocType || inXmlDeclaration || inProcessingInstruction);
            
            if (!inStructure) {
                
                tagStart = MarkupParsingUtil.findNextStructureStart(buffer, i, maxi, locator);
                
                if (tagStart == -1) {

                    if (canSplitText) {

                        final IAttoHandleResult result =
                            handler.handleText(buffer, current, len - current, currentLine, currentCol);
                        if (result != null && result != AttoHandleResult.CONTINUE) {
                            skipUntil = result.getParsingDisableLimit();
                        }

                        current = len;

                    }

                    return new BufferParseResult(current, currentLine, currentCol, false, skipUntil);

                }

                inOpenElement = ElementMarkupParsingUtil.isOpenElementStart(buffer, tagStart, maxi);
                if (!inOpenElement) {
                    inCloseElement = ElementMarkupParsingUtil.isCloseElementStart(buffer, tagStart, maxi);
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
                    
                    LocatorUtils.countChar(locator, buffer[tagStart]);
                    tagStart = MarkupParsingUtil.findNextStructureStart(buffer, tagStart + 1, maxi, locator);
                    
                    if (tagStart == -1) {
                        return new BufferParseResult(current, currentLine, currentCol, false, skipUntil);
                    }

                    inOpenElement = ElementMarkupParsingUtil.isOpenElementStart(buffer, tagStart, maxi);
                    if (!inOpenElement) {
                        inCloseElement = ElementMarkupParsingUtil.isCloseElementStart(buffer, tagStart, maxi);
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

                    final IAttoHandleResult result =
                        handler.handleText(
                                buffer, current, (tagStart - current),
                                currentLine, currentCol);

                    if (result != null && result != AttoHandleResult.CONTINUE) {
                        skipUntil = result.getParsingDisableLimit();
                    }

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
                    return new BufferParseResult(current, currentLine, currentCol, true, skipUntil);
                }

                
                if (inOpenElement) {
                    // This is a closing tag

                    final IAttoHandleResult result =
                            handler.handleStructure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    if (result != null && result != AttoHandleResult.CONTINUE) {
                        skipUntil = result.getParsingDisableLimit();
                    }

                    inOpenElement = false;
                    
                } else if (inCloseElement) {
                    // This is a closing tag

                    final IAttoHandleResult result =
                            handler.handleStructure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    if (result != null && result != AttoHandleResult.CONTINUE) {
                        skipUntil = result.getParsingDisableLimit();
                    }

                    inCloseElement = false;
                    
                } else if (inComment) {
                    // This is a comment! (obviously ;-))
                    
                    while (tagEnd - current < 7 || buffer[tagEnd - 1] != '-' || buffer[tagEnd - 2] != '-') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        LocatorUtils.countChar(locator, buffer[tagEnd]);
                        tagEnd = MarkupParsingUtil.findNextStructureEndDontAvoidQuotes(buffer, tagEnd + 1, maxi, locator);
                        
                        if (tagEnd == -1) {
                            return new BufferParseResult(current, currentLine, currentCol, true, skipUntil);
                        }
                        
                    }

                    final IAttoHandleResult result =
                            handler.handleStructure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    if (result != null && result != AttoHandleResult.CONTINUE) {
                        skipUntil = result.getParsingDisableLimit();
                    }

                    inComment = false;
                    
                } else if (inCdata) {
                    // This is a CDATA section
                    
                    while (tagEnd - current < 11 || buffer[tagEnd - 1] != ']' || buffer[tagEnd - 2] != ']') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        LocatorUtils.countChar(locator, buffer[tagEnd]);
                        tagEnd = MarkupParsingUtil.findNextStructureEndDontAvoidQuotes(buffer, tagEnd + 1, maxi, locator);
                        
                        if (tagEnd == -1) {
                            return new BufferParseResult(current, currentLine, currentCol, true, skipUntil);
                        }
                        
                    }

                    final IAttoHandleResult result =
                            handler.handleStructure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    if (result != null && result != AttoHandleResult.CONTINUE) {
                        skipUntil = result.getParsingDisableLimit();
                    }

                    inCdata = false;
                    
                } else if (inDocType) {
                    // This is a DOCTYPE clause

                    final IAttoHandleResult result =
                            handler.handleStructure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    if (result != null && result != AttoHandleResult.CONTINUE) {
                        skipUntil = result.getParsingDisableLimit();
                    }

                    inDocType = false;
                    
                } else if (inXmlDeclaration) {
                    // This is an XML Declaration

                    final IAttoHandleResult result =
                            handler.handleStructure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    if (result != null && result != AttoHandleResult.CONTINUE) {
                        skipUntil = result.getParsingDisableLimit();
                    }

                    inXmlDeclaration = false;
                    
                } else if (inProcessingInstruction) {
                    // This is a processing instruction

                    while (tagEnd - current < 5 || buffer[tagEnd - 1] != '?') {
                        // the '>' we chose is not the PI-closing one. Let's find again

                        LocatorUtils.countChar(locator, buffer[tagEnd]);
                        tagEnd = MarkupParsingUtil.findNextStructureEndDontAvoidQuotes(buffer, tagEnd + 1, maxi, locator);
                        
                        if (tagEnd == -1) {
                            return new BufferParseResult(current, currentLine, currentCol, true, skipUntil);
                        }
                        
                    }


                    final IAttoHandleResult result =
                            handler.handleStructure(buffer, current, (tagEnd - current) + 1, currentLine, currentCol);
                    if (result != null && result != AttoHandleResult.CONTINUE) {
                        skipUntil = result.getParsingDisableLimit();
                    }

                    inProcessingInstruction = false;
                    
                } else {

                    throw new IllegalStateException(
                            "Illegal parsing state: structure is not of a recognized type");
                    
                }
                
                // The '>' char will be considered as processed too
                LocatorUtils.countChar(locator, buffer[tagEnd]);
                
                current = tagEnd + 1;
                i = current;
                
            }
            
        }
        
        return new BufferParseResult(current, locator[0], locator[1], false, skipUntil);
        
    }
    
    
}
