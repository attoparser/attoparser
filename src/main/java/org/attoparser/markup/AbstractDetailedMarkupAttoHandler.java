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

import org.attoparser.AttoParseException;






/**
 * <p>
 *   Base abstract implementations for markup-specialized attohandlers that not only differentiate
 *   among the different types of markup structures (as its superclass {@link AbstractBasicMarkupAttoHandler}
 *   does), but also divide both elements (<i>tags</i>) and DOCTYPE clauses into its components, lauching
 *   different events for them.
 * </p>
 * <p>
 *   Handlers extending from this class can make use of a {@link DocumentRestrictions} instance
 *   specifying a set of restrictions to be applied during document parsing (for example, 
 *   for ensuring that a document is well-formed from an XML/XHTML standpoint).
 * </p>
 * <p>
 *   As for structures, this implementation differentiates among:
 * </p>
 * <ul>
 *   <li><b>Tags (a.k.a. <i>elements</i>)</b>: <tt>&lt;body&gt;</tt>, <tt>&lt;img/&gt;</tt>, 
 *       <tt>&lt;div class="content"&gt;</tt>, etc. Divided into:
 *       <ul>
 *         <li>Standalone elements: <b>start</b>, <b>name</b>, <b>attribute</b>,
 *             <b>attribute separator</b> (whitespace) and <b>end</b>.</li>
 *         <li>Open elements: <b>start</b>, <b>name</b>, <b>attribute</b>,
 *             <b>attribute separator</b> (whitespace) and <b>end</b>.</li>
 *         <li>Close elements: <b>start</b>, <b>name</b> and <b>end</b>.</li>
 *       </ul>
 *   </li>
 *   <li><b>Comments</b>: <tt>&lt;!-- this is a comment --&gt;</tt></li>
 *   <li><b>CDATA sections</b>: <tt>&lt;![CDATA[ ... ]]&gt;</tt></li>
 *   <li><b>DOCTYPE clauses</b>: <tt>&lt;!DOCTYPE html&gt;</tt></li>
 *   <li><b>XML Declarations</b>: <tt>&lt;?xml version="1.0"?&gt;</tt></li>
 *   <li><b>Processing Instructions</b>: <tt>&lt;?xsl-stylesheet ...?&gt;</tt></li>
 * </ul>
 * <p>
 *   Unclosed elements are balanced by means of the <tt>handleBalanced*</tt> handler methods.
 * </p>
 * <p>
 *   This class provides empty implementations for all event handlers, so that
 *   subclasses can override only the methods they need.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractDetailedMarkupAttoHandler 
        extends AbstractBasicMarkupAttoHandler
        implements IDetailedBalancedElementHandling, IDetailedDocTypeHandling {

    private static final DocumentRestrictions RESTRICTIONS_NONE = DocumentRestrictions.none(); 
    
    private final RestrictedWrapper wrapper;
    
    
    
    
    protected AbstractDetailedMarkupAttoHandler() {
        this(RESTRICTIONS_NONE);
    }

    
    protected AbstractDetailedMarkupAttoHandler(final DocumentRestrictions documentRestrictions) {
        super();
        this.wrapper = new RestrictedWrapper(this, documentRestrictions);
    }

    


    
    @Override
    public final void handleDocumentStart(final long startTimeNanos, final int line, final int col)
            throws AttoParseException {
        super.handleDocumentStart(startTimeNanos, line, col);
        this.wrapper.handleDocumentStart(startTimeNanos, line, col);
    }


    @Override
    public final void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws AttoParseException {
        super.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);
        this.wrapper.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);
    }
    

    
    @Override
    public final void handleXmlDeclaration(
            final char[] buffer, 
            final int keywordOffset, final int keywordLen, 
            final int keywordLine, final int keywordCol, 
            final int versionOffset, final int versionLen, 
            final int versionLine, final int versionCol,
            final int encodingOffset, final int encodingLen, 
            final int encodingLine, final int encodingCol, 
            final int standaloneOffset, final int standaloneLen,
            final int standaloneLine, final int standaloneCol, 
            final int outerOffset, final int outerLen, 
            final int line, final int col) 
            throws AttoParseException {
        
        super.handleXmlDeclaration(buffer, keywordOffset, keywordLen, keywordLine,
                keywordCol, versionOffset, versionLen, versionLine, versionCol,
                encodingOffset, encodingLen, encodingLine, encodingCol,
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen, line, col);
        
        this.wrapper.handleXmlDeclaration(
                buffer, 
                keywordOffset, keywordLen, keywordLine, keywordCol, 
                versionOffset, versionLen, versionLine, versionCol, 
                encodingOffset, encodingLen, encodingLine, encodingCol, 
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol, 
                outerOffset, outerLen, line, col);
        
    }



    @Override
    public final void handleDocType(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        super.handleDocType(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        DocTypeMarkupParsingUtil.parseDetailedDocType(
                buffer, outerOffset, outerLen, line, col, this.wrapper);
        
    }

    
    
    @Override
    public final void handleStandaloneElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleStandaloneElement(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        ElementMarkupParsingUtil.parseDetailedStandaloneElement(buffer, outerOffset, outerLen, line, col, this.wrapper);
        
    }

    
    @Override
    public final void handleOpenElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleOpenElement(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        ElementMarkupParsingUtil.parseDetailedOpenElement(buffer, outerOffset, outerLen, line, col, this.wrapper);
        
    }

    
    @Override
    public final void handleCloseElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        super.handleCloseElement(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        ElementMarkupParsingUtil.parseDetailedCloseElement(buffer, outerOffset, outerLen, line, col, this.wrapper);

    }

    
    
    
    
    

    /**
     * <p>
     *   Called when document parsing starts.
     * </p>
     * 
     * @param startTimeNanos the starting time, in nanoseconds.
     * @param documentRestrictions the document restrictions being applied.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleDocumentStart(final long startTimeNanos, 
            final int line, final int col, final DocumentRestrictions documentRestrictions)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    /**
     * <p>
     *   Called when document parsing ends.
     * </p>
     * 
     * @param endTimeNanos the parsing end time, in nanoseconds.
     * @param totalTimeNanos the difference between parsing start and end times.
     * @param documentRestrictions the document restrictions being applied.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos, 
            final int line, final int col, final DocumentRestrictions documentRestrictions)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    

    
    /**
     * <p>
     *   Called when a XML Declaration is found when using a handler extending from
     *   {@link AbstractDetailedMarkupAttoHandler}.
     * </p>
     * <p>
     *   Five [offset, len] pairs are provided for five partitions (<i>outer</i>,
     *   <i>keyword</i>, <i>version</i>, <i>encoding</i> and <i>standalone</i>):
     * </p>
     * <p>
     *   <tt>&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;</tt><br />
     *   <tt><b>|&nbsp;[K]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[V]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[ENC]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[S]&nbsp;&nbsp;|</b></tt><br />
     *   <tt><b>[OUTER------------------------------------------------]</b></tt>
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param keywordOffset offset for the <i>keyword</i> partition.
     * @param keywordLen length of the <i>keyword</i> partition.
     * @param keywordLine the line in the original document where the <i>keyword</i> partition starts.
     * @param keywordCol the column in the original document where the <i>keyword</i> partition starts.
     * @param versionOffset offset for the <i>version</i> partition.
     * @param versionLen length of the <i>version</i> partition.
     * @param versionLine the line in the original document where the <i>version</i> partition starts.
     * @param versionCol the column in the original document where the <i>version</i> partition starts.
     * @param encodingOffset offset for the <i>encoding</i> partition.
     * @param encodingLen length of the <i>encoding</i> partition.
     * @param encodingLine the line in the original document where the <i>encoding</i> partition starts.
     * @param encodingCol the column in the original document where the <i>encoding</i> partition starts.
     * @param standaloneOffset offset for the <i>standalone</i> partition.
     * @param standaloneLen length of the <i>standalone</i> partition.
     * @param standaloneLine the line in the original document where the <i>standalone</i> partition starts.
     * @param standaloneCol the column in the original document where the <i>standalone</i> partition starts.
     * @param outerOffset offset for the <i>outer</i> partition.
     * @param outerLen length of the <i>outer</i> partition.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleXmlDeclarationDetail(
            final char[] buffer, 
            final int keywordOffset, final int keywordLen, 
            final int keywordLine, final int keywordCol, 
            final int versionOffset, final int versionLen, 
            final int versionLine, final int versionCol,
            final int encodingOffset, final int encodingLen, 
            final int encodingLine, final int encodingCol, 
            final int standaloneOffset, final int standaloneLen,
            final int standaloneLine, final int standaloneCol, 
            final int outerOffset, final int outerLen, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    

    
    
    public void handleStandaloneElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    

    public void handleOpenElementStart(
            final char[] buffer,
            final int offset,
            final int len, final int line,
            final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleOpenElementName(
            final char[] buffer,
            final int offset,
            final int len, final int line,
            final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleOpenElementEnd(
            final char[] buffer,
            final int offset,
            final int len, final int line,
            final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
    public void handleCloseElementStart(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleCloseElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
    public void handleBalancedCloseElementStart(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleBalancedCloseElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleBalancedCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
    public void handleAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
    public void handleAttributeSeparator(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleDocType(
            final char[] buffer, 
            final int keywordOffset, final int keywordLen,
            final int keywordLine, final int keywordCol,
            final int elementNameOffset, final int elementNameLen,
            final int elementNameLine, final int elementNameCol,
            final int typeOffset, final int typeLen,
            final int typeLine, final int typeCol,
            final int publicIdOffset, final int publicIdLen,
            final int publicIdLine, final int publicIdCol,
            final int systemIdOffset, final int systemIdLen,
            final int systemIdLine, final int systemIdCol,
            final int internalSubsetOffset, final int internalSubsetLen,
            final int internalSubsetLine, final int internalSubsetCol,
            final int outerOffset, final int outerLen,
            final int outerLine, final int outerCol) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    


    
    
    
    
    
    
    static final class RestrictedWrapper
            implements IDetailedElementHandling, IDetailedDocTypeHandling {
        
        private static final int DEFAULT_STACK_SIZE = 15;
        private static final int DEFAULT_ATTRIBUTE_NAMES_SIZE = 5;

        private static final char[] CLOSE_START = "</".toCharArray();
        private static final char[] CLOSE_END = ">".toCharArray();
        
        private final AbstractDetailedMarkupAttoHandler handler;

        private final DocumentRestrictions documentRestrictions; 
        
        private final boolean requireBalancedElements;
        private final boolean requireNoUnbalancedCloseElements;
        private final boolean requireWellFormedProlog;
        private final boolean requireUniqueRootElement;
        private final boolean requireWellFormedAttributeValues;
        private final boolean requireUniqueAttributesInElement;
        
        private final boolean requireNoProlog;
        
        private char[][] elementStack;
        private int elementStackSize;
        
        private boolean xmlDeclarationRead = false;
        private boolean docTypeRead = false;
        private boolean elementRead = false;
        private char[] rootElementName = null;
        private char[][] currentElementAttributeNames = null;
        private int currentElementAttributeNamesSize = 0;
        
        
        
        RestrictedWrapper(final AbstractDetailedMarkupAttoHandler handler, final DocumentRestrictions documentRestrictions) {
            
            super();
            
            this.handler = handler;
            this.documentRestrictions = documentRestrictions;

            this.requireBalancedElements = documentRestrictions.getRequireBalancedElements();
            this.requireNoUnbalancedCloseElements = 
                    (this.requireBalancedElements || documentRestrictions.getRequireNoUnbalancedCloseElements());
            this.requireWellFormedProlog = documentRestrictions.getRequireWellFormedProlog();
            this.requireUniqueRootElement = documentRestrictions.getRequireUniqueRootElement();
            this.requireWellFormedAttributeValues = documentRestrictions.getRequireWellFormedAttributeValues();
            this.requireUniqueAttributesInElement = documentRestrictions.getRequireUniqueAttributesInElement();
            
            this.elementStack = new char[DEFAULT_STACK_SIZE][];
            this.elementStackSize = 0;
            
            this.requireNoProlog = documentRestrictions.getRequireNoProlog();
            
        }

        
        public void handleDocumentStart(final long startTimeNanos, final int line, final int col)
                throws AttoParseException {
            this.handler.handleDocumentStart(startTimeNanos, line, col, this.documentRestrictions);
        }


        public void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
                throws AttoParseException {
            
            if (this.requireBalancedElements && this.elementStackSize > 0) {
                final char[] popped = popFromStack();
                throw new AttoParseException(
                    "Malformed markup: element " +
                    "\"" + new String(popped, 0, popped.length) + "\"" +
                    " is never closed (no closing tag at the end of document)");
            }
            
            if (this.requireUniqueRootElement && !this.elementRead) {
                throw new AttoParseException(
                        "Malformed markup: no root element present");
            }
            
            cleanStack(line, col);
            
            this.handler.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col, this.documentRestrictions);
            
        }

        
        public final void handleXmlDeclaration(
                final char[] buffer, 
                final int keywordOffset, final int keywordLen, 
                final int keywordLine, final int keywordCol, 
                final int versionOffset, final int versionLen, 
                final int versionLine, final int versionCol,
                final int encodingOffset, final int encodingLen, 
                final int encodingLine, final int encodingCol, 
                final int standaloneOffset, final int standaloneLen,
                final int standaloneLine, final int standaloneCol, 
                final int outerOffset, final int outerLen, 
                final int line, final int col) 
                throws AttoParseException {

            if (this.requireNoProlog) {
                throw new AttoParseException(
                        "No prolog is allowed by document restrictions, but an XML Declaration has been found",
                        line, col);
            }
            
            if (this.requireWellFormedProlog) {
                
                if (this.xmlDeclarationRead) {
                    throw new AttoParseException(
                            "Malformed markup: Only one XML Declaration can appear in document",
                            line, col);
                }
                if (this.docTypeRead) {
                    throw new AttoParseException(
                            "Malformed markup: XML Declaration must appear before DOCTYPE",
                            line, col);
                }
                if (this.elementRead) {
                    throw new AttoParseException(
                            "Malformed markup: XML Declaration must appear before any " +
                            "elements in document",
                            line, col);
                }
                
            }
            
            this.handler.handleXmlDeclarationDetail(buffer, keywordOffset, keywordLen, keywordLine,
                    keywordCol, versionOffset, versionLen, versionLine, versionCol,
                    encodingOffset, encodingLen, encodingLine, encodingCol,
                    standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                    outerOffset, outerLen, line, col);
            
            this.xmlDeclarationRead = true;
            
        }
        

        public void handleStandaloneElementStart(
                final char[] buffer, 
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            
            this.handler.handleStandaloneElementStart(buffer, offset, len, line, col);
            
        }

        public void handleStandaloneElementName(
                final char[] buffer, 
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            
            if (this.elementStackSize == 0) {
                
                if (this.requireUniqueRootElement && this.elementRead) {
                    throw new AttoParseException(
                            "Malformed markup: Only one root element is allowed",
                            line, col);
                }

                if (this.requireWellFormedProlog && this.docTypeRead) {
                    boolean matches = (this.rootElementName.length == len);
                    for (int i = 0; matches && i < len; i++) {
                        if (buffer[offset + i] != this.rootElementName[i]) {
                            matches = false;
                        }
                    }
                    if (!matches) {
                        throw new AttoParseException(
                            "Malformed markup: Root element should be \"" + new String(this.rootElementName) + "\", " +
                            "but \"" + new String(buffer, offset, len) + "\" has been found",
                            line, col);
                    }
                }
                
            }

            if (this.requireUniqueAttributesInElement) {
                this.currentElementAttributeNames = null;
                this.currentElementAttributeNamesSize = 0;
            }
            
            this.handler.handleStandaloneElementName(buffer, offset, len, line, col);
            
        }

        public void handleStandaloneElementEnd(
                final char[] buffer,
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            
            this.handler.handleStandaloneElementEnd(buffer, offset, len, line, col);
            
            this.elementRead = true;
            
        }

        
        public void handleOpenElementStart(
                final char[] buffer,
                final int offset,
                final int len, final int line,
                final int col)
                throws AttoParseException {
            
            this.handler.handleOpenElementStart(buffer, offset, len, line, col);
            
        }

        public void handleOpenElementName(
                final char[] buffer,
                final int offset,
                final int len, final int line,
                final int col)
                throws AttoParseException {
            
            if (this.elementStackSize == 0) {
                
                if (this.requireUniqueRootElement && this.elementRead) {
                    throw new AttoParseException(
                            "Malformed markup: Only one root element is allowed",
                            line, col);
                }

                if (this.requireWellFormedProlog && this.docTypeRead) {
                    boolean matches = (this.rootElementName.length == len);
                    for (int i = 0; matches && i < len; i++) {
                        if (buffer[offset + i] != this.rootElementName[i]) {
                            matches = false;
                        }
                    }
                    if (!matches) {
                        throw new AttoParseException(
                            "Malformed markup: Root element should be \"" + new String(this.rootElementName) + "\", " +
                            "but \"" + new String(buffer, offset, len) + "\" has been found",
                            line, col);
                    }
                }
                
            }

            if (this.requireUniqueAttributesInElement) {
                this.currentElementAttributeNames = null;
                this.currentElementAttributeNamesSize = 0;
            }
            
            this.handler.handleOpenElementName(buffer, offset, len, line, col);
            
            addToStack(buffer, offset, len);
            
        }

        public void handleOpenElementEnd(
                final char[] buffer,
                final int offset,
                final int len, final int line,
                final int col)
                throws AttoParseException {
            
            this.handler.handleOpenElementEnd(buffer, offset, len, line, col);
            
            this.elementRead = true;
            
        }

        
        public void handleCloseElementStart(
                final char[] buffer,
                final int offset, final int len, 
                final int line, final int col)
                throws AttoParseException {
            
            // We will not do anything here because we have to
            // wait for the corresponding "elementName" event
            // to execute and check the balancing (we might need
            // some balancing events to be executed before this one).
            
        }

        public void handleCloseElementName(
                final char[] buffer, 
                final int offset, final int len, 
                final int line, final int col)
                throws AttoParseException {
            
            checkStackForElement(buffer, offset, len, line, col);
            
            if (this.requireUniqueAttributesInElement) {
                this.currentElementAttributeNames = null;
                this.currentElementAttributeNamesSize = 0;
            }
            
            this.handler.handleCloseElementStart(CLOSE_START, 0, CLOSE_START.length, line, col - 2);
            this.handler.handleCloseElementName(buffer, offset, len, line, col);
            
        }

        public void handleCloseElementEnd(
                final char[] buffer,
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            
            this.handler.handleCloseElementEnd(buffer, offset, len, line, col);
            
            this.elementRead = true;
            
        }

        
        public void handleAttribute(
                final char[] buffer,
                final int nameOffset, final int nameLen,
                final int nameLine, final int nameCol,
                final int operatorOffset, final int operatorLen,
                final int operatorLine, final int operatorCol,
                final int valueContentOffset, final int valueContentLen,
                final int valueOuterOffset, final int valueOuterLen,
                final int valueLine, final int valueCol)
                throws AttoParseException {
            
            if (this.requireUniqueAttributesInElement) {
                
                // Check attribute name is unique in this element
                if (this.currentElementAttributeNames == null) {
                    // we only create this structure if there is at least one attribute
                    this.currentElementAttributeNames = new char[DEFAULT_ATTRIBUTE_NAMES_SIZE][];
                }
                for (int i = 0; i < this.currentElementAttributeNamesSize; i++) {
                    if (this.currentElementAttributeNames[i].length != nameLen) {
                        continue;
                    }
                    int j;
                    for (j = 0; j < nameLen; j++) {
                        if (this.currentElementAttributeNames[i][j] != buffer[nameOffset + j]) {
                            break;
                        }
                    }
                    if (j == nameLen) {
                        throw new AttoParseException(
                            "Malformed markup: Attribute \"" + new String(buffer, nameOffset, nameLen) + "\" " +
                            "appears more than once in element", 
                            nameLine, nameCol);
                    }
                }
                if (this.currentElementAttributeNamesSize == this.currentElementAttributeNames.length) {
                    // we need to grow the array!
                    final char[][] newCurrentElementAttributeNames = new char[this.currentElementAttributeNames.length * 2][];
                    System.arraycopy(this.currentElementAttributeNames, 0, newCurrentElementAttributeNames, 0, this.currentElementAttributeNames.length);
                    this.currentElementAttributeNames = newCurrentElementAttributeNames;
                }
                this.currentElementAttributeNames[this.currentElementAttributeNamesSize] = new char[nameLen];
                System.arraycopy(buffer, nameOffset, this.currentElementAttributeNames[this.currentElementAttributeNamesSize], 0, nameLen);
                this.currentElementAttributeNamesSize++;
                
            }

                
            if (this.requireWellFormedAttributeValues) {
                
                // Check there is an operator
                if (operatorLen == 0)  {
                    throw new AttoParseException(
                            "Malformed markup: Value for attribute \"" + new String(buffer, nameOffset, nameLen) + "\" " +
                            "must include an equals (=) sign and a value surrounded by commas", 
                            operatorLine, operatorCol);
                }
                
                
                // Check attribute is surrounded by commas (double or single)
                if (valueOuterLen == 0 || valueOuterLen == valueContentLen)  {
                    throw new AttoParseException(
                            "Malformed markup: Value for attribute \"" + new String(buffer, nameOffset, nameLen) + "\" " +
                            "must be surrounded by commas", 
                            valueLine, valueCol);
                }
            
            }
            
            this.handler.handleAttribute(
                    buffer, 
                    nameOffset, nameLen, nameLine, nameCol, 
                    operatorOffset, operatorLen, operatorLine, operatorCol, 
                    valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);
            
        }


        
        public void handleAttributeSeparator(
                final char[] buffer,
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            
            this.handler.handleAttributeSeparator(buffer, offset, len, line, col);
            
        }



        public void handleDocType(
                final char[] buffer, 
                final int keywordOffset, final int keywordLen,
                final int keywordLine, final int keywordCol,
                final int elementNameOffset, final int elementNameLen,
                final int elementNameLine, final int elementNameCol,
                final int typeOffset, final int typeLen,
                final int typeLine, final int typeCol,
                final int publicIdOffset, final int publicIdLen,
                final int publicIdLine, final int publicIdCol,
                final int systemIdOffset, final int systemIdLen,
                final int systemIdLine, final int systemIdCol,
                final int internalSubsetOffset, final int internalSubsetLen,
                final int internalSubsetLine, final int internalSubsetCol,
                final int outerOffset, final int outerLen,
                final int outerLine, final int outerCol) 
                throws AttoParseException {
            
            if (this.requireNoProlog) {
                throw new AttoParseException(
                        "No prolog is allowed by document restrictions, but a DOCTYPE clause has been found",
                        outerLine, outerCol);
            }
            
            if (this.requireWellFormedProlog && this.docTypeRead) {
                throw new AttoParseException(
                        "Malformed markup: Only one DOCTYPE clause can appear in document",
                        outerLine, outerCol);
            }
            
            if (this.requireWellFormedProlog && this.elementRead) {
                throw new AttoParseException(
                        "Malformed markup: DOCTYPE must appear before any " +
                        "elements in document",
                        outerLine, outerCol);
            }
            
            this.rootElementName = new char[elementNameLen];
            System.arraycopy(buffer, elementNameOffset, this.rootElementName, 0, elementNameLen);
            
            this.handler.handleDocType(
                    buffer, 
                    keywordOffset, keywordLen, keywordLine, keywordCol, 
                    elementNameOffset, elementNameLen, elementNameLine, elementNameCol, 
                    typeOffset, typeLen, typeLine, typeCol, 
                    publicIdOffset, publicIdLen, publicIdLine, publicIdCol, 
                    systemIdOffset, systemIdLen, systemIdLine, systemIdCol, 
                    internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol, 
                    outerOffset, outerLen, outerLine, outerCol);
            
            this.docTypeRead = true;
            
        }

        
        
        
        
        private void checkStackForElement(
                final char[] buffer, final int offset, final int len, final int line, final int col) 
                throws AttoParseException {
            
            char[] popped = popFromStack();

            while (popped != null) {

                boolean matches = (len == popped.length);
            
                final int maxi = offset + len;
                for (int i = offset; matches && i < maxi; i++) {
                    if (buffer[i] != popped[i - offset]) {
                        matches = false;
                    }
                }
    
                if (matches) {
                    // We found the corresponding opening element!
                    return;
                }
                
                // does not match...
                
                if (this.requireBalancedElements) {
                    throw new AttoParseException(
                            "Malformed markup: element " +
                            "\"" + new String(popped, 0, popped.length) + "\"" +
                            " is never closed", line, col - 2);
                }
                
                this.handler.handleBalancedCloseElementStart(CLOSE_START, 0, CLOSE_START.length, line, col - 2);
                this.handler.handleBalancedCloseElementName(popped, 0, popped.length, line, col - 2);
                this.handler.handleBalancedCloseElementEnd(CLOSE_END, 0, CLOSE_END.length, line, col - 2);
                
                popped = popFromStack();
                
            }

            // closing element at the root level
            if (this.requireNoUnbalancedCloseElements) {
                throw new AttoParseException(
                        "Malformed markup: closing element " +
                        "\"" + new String(buffer, offset, len) + "\"" +
                        " is never open", line, col - 2);
            }
            
            // Nothing to check! just return.
            
        }
        

        
        
        private void cleanStack(final int line, final int col) 
                throws AttoParseException {
            
            if (this.elementStackSize > 0) {

                // When we arrive here we know that "requireBalancedElements" is
                // false. If it were true, an exception would have been raised before.
                
                char[] popped = popFromStack();

                while (popped != null) {
                    
                    this.handler.handleBalancedCloseElementStart(CLOSE_START, 0, CLOSE_START.length, line, col);
                    this.handler.handleBalancedCloseElementName(popped, 0, popped.length, line, col);
                    this.handler.handleBalancedCloseElementEnd(CLOSE_END, 0, CLOSE_END.length, line, col);
                    
                    popped = popFromStack();
                    
                }
                
            }
            
        }
        
        
        
        private void addToStack(
                final char[] buffer, final int offset, final int len) {
            
            if (this.elementStackSize == this.elementStack.length) {
                growStack();
            }
            
            this.elementStack[this.elementStackSize] = new char[len];
            System.arraycopy(buffer, offset, this.elementStack[this.elementStackSize], 0, len);
            
            this.elementStackSize++;
            
        }

        
        private char[] popFromStack() {
            
            if (this.elementStackSize == 0) {
                return null;
            }
            
            this.elementStackSize--;
            
            final char[] popped = this.elementStack[this.elementStackSize];
            this.elementStack[this.elementStackSize] = null;

            return popped;
            
        }
        
        
        private void growStack() {
            
            final int newStackSize = this.elementStack.length * 2;
            final char[][] newStack = new char[newStackSize][];
            System.arraycopy(this.elementStack, 0, newStack, 0, this.elementStack.length);
            this.elementStack = newStack;
            
        }
        
        
    }
    
    
    
    
}