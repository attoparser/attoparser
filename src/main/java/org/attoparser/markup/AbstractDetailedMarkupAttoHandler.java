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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.StackableElementAttoHandleResult;
import org.attoparser.markup.MarkupParsingConfiguration.ElementBalancing;
import org.attoparser.markup.MarkupParsingConfiguration.PrologParsingConfiguration;
import org.attoparser.markup.MarkupParsingConfiguration.UniqueRootElementPresence;


/**
 * <p>
 *   Base abstract implementations for markup-specialized attohandlers that not only differentiate
 *   among the different types of markup structures (as its superclass {@link AbstractBasicMarkupAttoHandler}
 *   does), but also divide both elements (<i>tags</i>) and DOCTYPE clauses into its components, lauching
 *   different events for them.
 * </p>
 * <p>
 *   Handlers extending from this class can make use of a {@link MarkupParsingConfiguration} instance
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
        implements IDetailedElementHandling, IDetailedDocTypeHandling {

    private static final MarkupParsingConfiguration NO_RESTRICTIONS = MarkupParsingConfiguration.noRestrictions(); 
    
    private final StackAwareWrapper wrapper;

    
    
    
    protected AbstractDetailedMarkupAttoHandler() {
        this(NO_RESTRICTIONS);
    }

    
    protected AbstractDetailedMarkupAttoHandler(final MarkupParsingConfiguration markupParsingConfiguration) {
        super();
        this.wrapper = new StackAwareWrapper(this, markupParsingConfiguration);
    }



    @Override
    public final IAttoHandleResult handleDocumentStart(final long startTimeNanos, final int line, final int col)
            throws AttoParseException {
        return this.wrapper.handleDocumentStart(startTimeNanos, line, col);
    }


    @Override
    public final IAttoHandleResult handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws AttoParseException {
        return this.wrapper.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);
    }
    

    
    @Override
    public final IAttoHandleResult handleXmlDeclaration(
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

        return this.wrapper.handleXmlDeclaration(
                buffer, 
                keywordOffset, keywordLen, keywordLine, keywordCol, 
                versionOffset, versionLen, versionLine, versionCol, 
                encodingOffset, encodingLen, encodingLine, encodingCol, 
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol, 
                outerOffset, outerLen, line, col);
        
    }



    @Override
    public final IAttoHandleResult handleDocType(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        return DocTypeMarkupParsingUtil.parseDetailedDocType(
                buffer, outerOffset, outerLen, line, col, this.wrapper);
        
    }

    
    
    @Override
    public final IAttoHandleResult handleStandaloneElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col) 
            throws AttoParseException {

        return ElementMarkupParsingUtil.
                    doTryParseDetailedOpenOrStandaloneElement(
                            buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, this.wrapper, true);

    }

    
    @Override
    public final IAttoHandleResult handleOpenElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col) 
            throws AttoParseException {

        return ElementMarkupParsingUtil.
                    doTryParseDetailedOpenOrStandaloneElement(
                            buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, this.wrapper, false);

    }

    
    @Override
    public final IAttoHandleResult handleCloseElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        return ElementMarkupParsingUtil.
                    doTryParseDetailedCloseElement(
                            buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, this.wrapper);

    }
    
    
    


    /**
     * <p>
     *   Called when document parsing starts.
     * </p>
     * 
     * @param startTimeNanos the starting time, in nanoseconds.
     * @param configuration the document restrictions being applied.
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public IAttoHandleResult handleDocumentStart(final long startTimeNanos,
            final int line, final int col, final MarkupParsingConfiguration configuration)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    /**
     * <p>
     *   Called when document parsing ends.
     * </p>
     * 
     * @param endTimeNanos the parsing end time, in nanoseconds.
     * @param totalTimeNanos the difference between parsing start and end times.
     * @param configuration the document restrictions being applied.
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public IAttoHandleResult handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos,
            final int line, final int col, final MarkupParsingConfiguration configuration)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
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
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public IAttoHandleResult handleXmlDeclarationDetail(
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
        return null;
    }
    

    
    
    public IAttoHandleResult handleStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    public IAttoHandleResult handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    

    public IAttoHandleResult handleOpenElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    public IAttoHandleResult handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    
    public IAttoHandleResult handleCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    public IAttoHandleResult handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    
    public IAttoHandleResult handleAutoCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    public IAttoHandleResult handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }
    

    
    public IAttoHandleResult handleUnmatchedCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    public IAttoHandleResult handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    
    public IAttoHandleResult handleAttribute(
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
        return null;
    }


    
    public IAttoHandleResult handleInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }



    public IAttoHandleResult handleDocType(
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
        return null;
    }

    


    
    
    
    
    
    
    static final class StackAwareWrapper
            implements IDetailedElementHandling, IDetailedDocTypeHandling {

        private static final int DEFAULT_STACK_LEN = 10;
        private static final int DEFAULT_ATTRIBUTE_NAMES_LEN = 3;
        
        private final AbstractDetailedMarkupAttoHandler handler;

        private final MarkupParsingConfiguration markupParsingConfiguration;

        private final boolean useStack;

        private final boolean autoClose;
        private final boolean requireBalancedElements;
        private final boolean requireNoUnmatchedCloseElements;
        
        private final PrologParsingConfiguration prologParsingConfiguration;
        private final UniqueRootElementPresence uniqueRootElementPresence;

        private final boolean caseSensitive;
        
        private final boolean requireWellFormedAttributeValues;
        private final boolean requireUniqueAttributesInElement;
        
        private final boolean validateProlog;
        private final boolean prologPresenceForbidden;
        private final boolean xmlDeclarationPresenceForbidden;
        private final boolean doctypePresenceForbidden;

        // Will be used as an element name cache in order to avoid creating a new
        // char[] object each time an element is pushed into the stack or an attribute
        // is processed to check its uniqueness.
        private final StructureNamesRepository structureNamesRepository;

        private char[][] elementStack;
        private int elementStackSize;
        
        private boolean validPrologXmlDeclarationRead = false;
        private boolean validPrologDocTypeRead = false;
        private boolean elementRead = false;
        private char[] rootElementName = null;
        private char[][] currentElementAttributeNames = null;
        private int currentElementAttributeNamesSize = 0;
        
        
        private boolean closeElementIsMatched = true;
        
        
        StackAwareWrapper(final AbstractDetailedMarkupAttoHandler handler, final MarkupParsingConfiguration markupParsingConfiguration) {

            super();

            this.handler = handler;

            this.caseSensitive = markupParsingConfiguration.isCaseSensitive();
            this.markupParsingConfiguration = markupParsingConfiguration;

            this.useStack = (!ElementBalancing.NO_BALANCING.equals(markupParsingConfiguration.getElementBalancing()) ||
                             markupParsingConfiguration.getRequireUniqueAttributesInElement() ||
                             !UniqueRootElementPresence.NOT_VALIDATED.equals(markupParsingConfiguration.getUniqueRootElementPresence()));

            this.autoClose =
                    (ElementBalancing.AUTO_CLOSE.equals(markupParsingConfiguration.getElementBalancing()) ||
                            ElementBalancing.AUTO_CLOSE_REQUIRE_NO_UNMATCHED_CLOSE.equals(markupParsingConfiguration.getElementBalancing()));
            this.requireBalancedElements =
                    ElementBalancing.REQUIRE_BALANCED.equals(markupParsingConfiguration.getElementBalancing());
            this.requireNoUnmatchedCloseElements =
                    (this.requireBalancedElements ||
                            ElementBalancing.AUTO_CLOSE_REQUIRE_NO_UNMATCHED_CLOSE.equals(markupParsingConfiguration.getElementBalancing()) ||
                            ElementBalancing.REQUIRE_NO_UNMATCHED_CLOSE.equals(markupParsingConfiguration.getElementBalancing()));

            this.prologParsingConfiguration = markupParsingConfiguration.getPrologParsingConfiguration();

            this.prologParsingConfiguration.validateConfiguration();

            this.uniqueRootElementPresence = markupParsingConfiguration.getUniqueRootElementPresence();
            this.requireWellFormedAttributeValues = markupParsingConfiguration.getRequireXmlWellFormedAttributeValues();
            this.requireUniqueAttributesInElement = markupParsingConfiguration.getRequireUniqueAttributesInElement();

            this.validateProlog = this.prologParsingConfiguration.isValidateProlog();
            this.prologPresenceForbidden = this.prologParsingConfiguration.getPrologPresence().isForbidden();
            this.xmlDeclarationPresenceForbidden = this.prologParsingConfiguration.getXmlDeclarationPresence().isRequired();
            this.doctypePresenceForbidden = this.prologParsingConfiguration.getDoctypePresence().isRequired();

            if (this.useStack) {

                this.elementStack = new char[DEFAULT_STACK_LEN][];
                this.elementStackSize = 0;

                this.structureNamesRepository = new StructureNamesRepository();

            } else {

                this.elementStack = null;
                this.elementStackSize = 0;
                this.structureNamesRepository = null;

            }
            
        }



        public IAttoHandleResult handleDocumentStart(final long startTimeNanos, final int line, final int col)
                throws AttoParseException {
            return this.handler.handleDocumentStart(startTimeNanos, line, col, this.markupParsingConfiguration);
        }


        public IAttoHandleResult handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
                throws AttoParseException {
            
            if (this.requireBalancedElements && this.elementStackSize > 0) {
                final char[] popped = popFromStack();
                throw new AttoParseException(
                    "Malformed markup: element " +
                    "\"" + new String(popped, 0, popped.length) + "\"" +
                    " is never closed (no closing tag at the end of document)");
            }
            
            if (!this.elementRead && (
                    (this.validPrologDocTypeRead && this.uniqueRootElementPresence.isDependsOnPrologDoctype()) ||
                    this.uniqueRootElementPresence.isRequiredAlways())) {
                throw new AttoParseException(
                        "Malformed markup: no root element present");
            }

            if (this.useStack) {
                cleanStack(line, col);
            }
            
            return this.handler.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col, this.markupParsingConfiguration);

        }

        
        public IAttoHandleResult handleXmlDeclaration(
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

            if (this.validateProlog && (this.prologPresenceForbidden || this.xmlDeclarationPresenceForbidden)) {
                throw new AttoParseException(
                        "An XML Declaration has been found, but it wasn't allowed",
                        line, col);
            }

            if (this.validateProlog) {
                
                if (this.validPrologXmlDeclarationRead) {
                    throw new AttoParseException(
                            "Malformed markup: Only one XML Declaration can appear in document",
                            line, col);
                }
                if (this.validPrologDocTypeRead) {
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

            final IAttoHandleResult result =
                this.handler.handleXmlDeclarationDetail(buffer, keywordOffset, keywordLen, keywordLine,
                        keywordCol, versionOffset, versionLen, versionLine, versionCol,
                        encodingOffset, encodingLen, encodingLine, encodingCol,
                        standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                        outerOffset, outerLen, line, col);

            if (this.validateProlog) {
                this.validPrologXmlDeclarationRead = true;
            }

            return result;
            
        }
        

        public IAttoHandleResult handleStandaloneElementStart(
                final char[] buffer, 
                final int nameOffset, final int nameLen,
                final int line, final int col)
                throws AttoParseException {

            if (this.useStack) {

                if (this.elementStackSize == 0) {
                    checkValidRootElement(buffer, nameOffset, nameLen, line, col);
                }

                if (this.requireUniqueAttributesInElement) {
                    this.currentElementAttributeNames = null;
                    this.currentElementAttributeNamesSize = 0;
                }

                // This is a standalone element, no need to put into stack

            }

            return this.handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, line, col);
            
        }

        public IAttoHandleResult handleStandaloneElementEnd(
                final char[] buffer,
                final int nameOffset, final int nameLen,
                final int line, final int col)
                throws AttoParseException {

            final IAttoHandleResult result =
                this.handler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, line, col);
            this.elementRead = true;
            return result;

        }

        
        public IAttoHandleResult handleOpenElementStart(
                final char[] buffer,
                final int nameOffset, final int nameLen, 
                final int line, final int col)
                throws AttoParseException {

            if (this.useStack) {

                if (this.elementStackSize == 0) {
                    checkValidRootElement(buffer, nameOffset, nameLen, line, col);
                }

                if (this.requireUniqueAttributesInElement) {
                    this.currentElementAttributeNames = null;
                    this.currentElementAttributeNamesSize = 0;
                }

            }

            final IAttoHandleResult result =
                    this.handler.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);

            if (result instanceof StackableElementAttoHandleResult) {

                final StackableElementAttoHandleResult stackableResult = (StackableElementAttoHandleResult) result;

                if (stackableResult.getShouldStack()) {
                    addToStack(buffer, nameOffset, nameLen);
                }

            } else {

                addToStack(buffer, nameOffset, nameLen);

            }

            return result;

        }

        public IAttoHandleResult handleOpenElementEnd(
                final char[] buffer,
                final int nameOffset, final int nameLen,
                final int line, final int col)
                throws AttoParseException {

            final IAttoHandleResult result =
                this.handler.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);

            this.elementRead = true;

            return result;
            
        }

        
        public IAttoHandleResult handleCloseElementStart(
                final char[] buffer, 
                final int nameOffset, final int nameLen, 
                final int line, final int col)
                throws AttoParseException {

            if (this.useStack) {

                this.closeElementIsMatched =
                        checkStackForElement(buffer, nameOffset, nameLen, line, col);

                if (this.requireUniqueAttributesInElement) {
                    this.currentElementAttributeNames = null;
                    this.currentElementAttributeNamesSize = 0;
                }

                if (this.closeElementIsMatched) {
                    return this.handler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);
                } else {
                    return this.handler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);
                }

            } else {

                return this.handler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);

            }


        }

        public IAttoHandleResult handleCloseElementEnd(
                final char[] buffer,
                final int nameOffset, final int nameLen,
                final int line, final int col)
                throws AttoParseException {

            final IAttoHandleResult result;
            if (!this.useStack || this.closeElementIsMatched) {
                result = this.handler.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);
            } else {
                result = this.handler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
            }
            
            this.elementRead = true;

            return result;

        }

        
        public IAttoHandleResult handleAttribute(
                final char[] buffer,
                final int nameOffset, final int nameLen,
                final int nameLine, final int nameCol,
                final int operatorOffset, final int operatorLen,
                final int operatorLine, final int operatorCol,
                final int valueContentOffset, final int valueContentLen,
                final int valueOuterOffset, final int valueOuterLen,
                final int valueLine, final int valueCol)
                throws AttoParseException {
            
            if (this.useStack && this.requireUniqueAttributesInElement) {
                
                // Check attribute name is unique in this element
                if (this.currentElementAttributeNames == null) {
                    // we only create this structure if there is at least one attribute
                    this.currentElementAttributeNames = new char[DEFAULT_ATTRIBUTE_NAMES_LEN][];
                }
                for (int i = 0; i < this.currentElementAttributeNamesSize; i++) {
                    if (this.currentElementAttributeNames[i].length != nameLen) {
                        continue;
                    }
                    int j;
                    if (this.caseSensitive) {
                        for (j = 0; j < nameLen; j++) {
                            if (this.currentElementAttributeNames[i][j] != buffer[nameOffset + j]) {
                                break;
                            }
                        }
                    } else {
                        for (j = 0; j < nameLen; j++) {
                            if (Character.toLowerCase(this.currentElementAttributeNames[i][j]) != Character.toLowerCase(buffer[nameOffset + j])) {
                                break;
                            }
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
                    final char[][] newCurrentElementAttributeNames = new char[this.currentElementAttributeNames.length + DEFAULT_ATTRIBUTE_NAMES_LEN][];
                    System.arraycopy(this.currentElementAttributeNames, 0, newCurrentElementAttributeNames, 0, this.currentElementAttributeNames.length);
                    this.currentElementAttributeNames = newCurrentElementAttributeNames;
                }

                this.currentElementAttributeNames[this.currentElementAttributeNamesSize] =
                        this.structureNamesRepository.getStructureName(buffer, nameOffset, nameLen);

                this.currentElementAttributeNamesSize++;
                
            }

                
            if (this.requireWellFormedAttributeValues) {
                
                // Check there is an operator
                if (operatorLen == 0)  {
                    throw new AttoParseException(
                            "Malformed markup: Attribute \"" + new String(buffer, nameOffset, nameLen) + "\" " +
                            "must include an equals (=) sign and a value surrounded by quotes",
                            operatorLine, operatorCol);
                }
                
                
                // Check attribute is surrounded by commas (double or single)
                if (valueOuterLen == 0 || valueOuterLen == valueContentLen)  {
                    throw new AttoParseException(
                            "Malformed markup: Value for attribute \"" + new String(buffer, nameOffset, nameLen) + "\" " +
                            "must be surrounded by quotes",
                            valueLine, valueCol);
                }
            
            }
            
            return this.handler.handleAttribute(
                    buffer, 
                    nameOffset, nameLen, nameLine, nameCol, 
                    operatorOffset, operatorLen, operatorLine, operatorCol, 
                    valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);
            
        }


        
        public IAttoHandleResult handleInnerWhiteSpace(
                final char[] buffer,
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            
            return this.handler.handleInnerWhiteSpace(buffer, offset, len, line, col);
            
        }



        public IAttoHandleResult handleDocType(
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
            
            if (this.validateProlog) {
                
                if (this.prologPresenceForbidden || this.doctypePresenceForbidden) {
                    throw new AttoParseException(
                            "A DOCTYPE clause has been found, but it wasn't allowed",
                            outerLine, outerCol);
                }
                
                if (this.validPrologDocTypeRead) {
                    throw new AttoParseException(
                            "Malformed markup: Only one DOCTYPE clause can appear in document",
                            outerLine, outerCol);
                }
                
                if (this.elementRead) {
                    throw new AttoParseException(
                            "Malformed markup: DOCTYPE must appear before any " +
                            "elements in document",
                            outerLine, outerCol);
                }

                if (this.prologParsingConfiguration.isRequireDoctypeKeywordsUpperCase()) {
                    
                    if (keywordLen > 0) {
                        final int maxi = keywordOffset + keywordLen;
                        for (int i = keywordOffset; i < maxi; i++) {
                            if (Character.isLowerCase(buffer[i])) {
                                throw new AttoParseException(
                                        "Malformed markup: DOCTYPE requires upper-case " +
                                        "keywords (\"" + new String(buffer, keywordOffset, keywordLen) + "\" was found)",
                                        outerLine, outerCol);
                            }
                        }
                    }
                    
                    if (typeLen > 0) {
                        final int maxi = typeOffset + typeLen;
                        for (int i = typeOffset; i < maxi; i++) {
                            if (Character.isLowerCase(buffer[i])) {
                                throw new AttoParseException(
                                        "Malformed markup: DOCTYPE requires upper-case " +
                                        "keywords (\"" + new String(buffer, typeOffset, typeLen) + "\" was found)",
                                        outerLine, outerCol);
                            }
                        }
                    }
                    
                }
                
            }

            if (this.useStack) {

                this.rootElementName =
                        this.structureNamesRepository.getStructureName(buffer, elementNameOffset, elementNameLen);

            }

            final IAttoHandleResult result =
                this.handler.handleDocType(
                        buffer,
                        keywordOffset, keywordLen, keywordLine, keywordCol,
                        elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                        typeOffset, typeLen, typeLine, typeCol,
                        publicIdOffset, publicIdLen, publicIdLine, publicIdCol,
                        systemIdOffset, systemIdLen, systemIdLine, systemIdCol,
                        internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol,
                        outerOffset, outerLen, outerLine, outerCol);
            
            if (this.validateProlog) {
                this.validPrologDocTypeRead = true;
            }

            return result;
            
        }


        public IAttoHandleResult handleUnmatchedCloseElementStart(
                final char[] buffer, 
                final int nameOffset, final int nameLen, 
                final int line, final int col) 
                throws AttoParseException {
            throw new UnsupportedOperationException(
                    "This method at the wrapper handler should never be called, it is just " +
                    "here to complete the interface implementation");
        }


        public IAttoHandleResult handleUnmatchedCloseElementEnd(
                final char[] buffer,
                final int nameOffset, final int nameLen,
                final int line, final int col)
                throws AttoParseException {
            throw new UnsupportedOperationException(
                    "This method at the wrapper handler should never be called, it is just " +
                    "here to complete the interface implementation");
        }


        public IAttoHandleResult handleAutoCloseElementStart(
                final char[] buffer, 
                final int nameOffset, final int nameLen, 
                final int line, final int col) 
                throws AttoParseException {
            throw new UnsupportedOperationException(
                    "This method at the wrapper handler should never be called, it is just " +
                    "here to complete the interface implementation");
        }


        public IAttoHandleResult handleAutoCloseElementEnd(
                final char[] buffer,
                final int nameOffset, final int nameLen,
                final int line, final int col)
                throws AttoParseException {
            throw new UnsupportedOperationException(
                    "This method at the wrapper handler should never be called, it is just " +
                    "here to complete the interface implementation");
        }

        

        
        
        private void checkValidRootElement(
                final char[] buffer, final int offset, final int len, final int line, final int col) 
                throws AttoParseException {
            
            if (!this.validateProlog) {
                
                if (this.elementRead && this.uniqueRootElementPresence.isRequiredAlways()) {
                    // We are not validating the prolog, but anyway we required only one element root
                    // and it seems there are several.
                    throw new AttoParseException(
                            "Malformed markup: Only one root element is allowed",
                            line, col);
                }

                // Nothing else to check.
                return;
                
            }
            
            // We don't need to check the possibility of having parsed forbidden XML Decl or DOCTYPE
            // because this has already been checked when the corresponding events were triggered. 
            
            if (this.validPrologDocTypeRead) {
                
                if (this.elementRead) {
                    // If we have a DOCTYPE, we will have a root element name and therefore we will
                    // only allow one root element. But it seems there are several.
                    throw new AttoParseException(
                            "Malformed markup: Only one root element (with name \"" + new String(this.rootElementName) + "\" is allowed",
                            line, col);
                }
                
                boolean matches = (this.rootElementName.length == len);
                if (this.caseSensitive) {
                    for (int i = 0; matches && i < len; i++) {
                        if (buffer[offset + i] != this.rootElementName[i]) {
                            matches = false;
                        }
                    }
                } else {
                    for (int i = 0; matches && i < len; i++) {
                        if (Character.toLowerCase(buffer[offset + i]) != Character.toLowerCase(this.rootElementName[i])) {
                            matches = false;
                        }
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
        
        
        
        private boolean checkStackForElement(
                final char[] buffer, final int offset, final int len, final int line, final int col) 
                throws AttoParseException {

            final int initialStackSize = this.elementStackSize;
            char[] popped = popFromStack();
            int poppedCount = 1;

            while (popped != null) {

                boolean matches = (len == popped.length);
            
                final int maxi = offset + len;
                if (this.caseSensitive) {
                    for (int i = offset; matches && i < maxi; i++) {
                        if (buffer[i] != popped[i - offset]) {
                            matches = false;
                        }
                    }
                } else {
                    for (int i = offset; matches && i < maxi; i++) {
                        if (Character.toLowerCase(buffer[i]) != Character.toLowerCase(popped[i - offset])) {
                            matches = false;
                        }
                    }
                }
    
                if (matches) {

                    // We found the corresponding opening element, so we execute all pending auto-close events
                    // (if needed) and return true (meaning the close element has a matching open element).

                    if (this.autoClose) {
                        if (poppedCount > 1) {
                            rollbackPopFromStack(initialStackSize); // we reinitialize the stack to its original state
                            for (int i = 0; i < poppedCount - 1; i++) { // We don't report the last one as auto-close, because it's the matched one
                                popped = popFromStack();
                                this.handler.handleAutoCloseElementStart(popped, 0, popped.length, line, col);
                                this.handler.handleAutoCloseElementEnd(popped, 0, popped.length, line, col);
                            }
                            popFromStack(); // this is the matched one, just pop and don't report as auto-close
                        }
                    }

                    commitPopFromStack(initialStackSize);

                    return true;

                }
                
                // does not match...
                
                if (this.requireBalancedElements) {
                    throw new AttoParseException(
                            "Malformed markup: element " +
                            "\"" + new String(popped, 0, popped.length) + "\"" +
                            " is never closed", line, col);
                }

                popped = popFromStack();
                poppedCount++;
                
            }

            // closing element at the root level
            if (this.requireNoUnmatchedCloseElements) {
                throw new AttoParseException(
                        "Malformed markup: closing element " +
                        "\"" + new String(buffer, offset, len) + "\"" +
                        " is never open", line, col);
            }

            // Return false because the close element has no matching open element
            rollbackPopFromStack(initialStackSize);
            return false;
            
        }
        

        
        
        private void cleanStack(final int line, final int col) 
                throws AttoParseException {
            
            if (this.elementStackSize > 0) {

                // When we arrive here we know that "requireBalancedElements" is
                // false. If it were true, an exception would have been raised before.
                
                char[] popped = popFromStack();

                while (popped != null) {

                    if (this.autoClose) {
                        this.handler.handleAutoCloseElementStart(popped, 0, popped.length, line, col);
                        this.handler.handleAutoCloseElementEnd(popped, 0, popped.length, line, col);
                    }
                    
                    popped = popFromStack();
                    
                }
                
            }
            
        }
        
        
        
        private void addToStack(
                final char[] buffer, final int offset, final int len) {
            
            if (this.elementStackSize == this.elementStack.length) {
                growStack();
            }


            this.elementStack[this.elementStackSize] =
                    this.structureNamesRepository.getStructureName(buffer, offset, len);
            
            this.elementStackSize++;
            
        }

        
        private char[] popFromStack() {
            if (this.elementStackSize == 0) {
                return null;
            }
            this.elementStackSize--;
            return this.elementStack[this.elementStackSize];
        }
        
        private void commitPopFromStack(final int initialSize) {
            for (int i = this.elementStackSize; i < initialSize; i++) {
                this.elementStack[i] = null;
            }
        }
        
        private void rollbackPopFromStack(final int initialSize) {
            this.elementStackSize = initialSize;
        }
        
        
        private void growStack() {
            
            final int newStackLen = this.elementStack.length + DEFAULT_STACK_LEN;
            final char[][] newStack = new char[newStackLen][];
            System.arraycopy(this.elementStack, 0, newStack, 0, this.elementStack.length);
            this.elementStack = newStack;
            
        }

    }


    /*
     * <p>
     *     This class is <strong>NOT thread-safe</strong>. Should only be used inside a specific handler
     *     instance/thread and only during a single execution.
     * </p>
     */
    static final class StructureNamesRepository {

        private final List<char[]> repository;


        StructureNamesRepository() {
            this.repository = new ArrayList<char[]>(5);
        }



        char[] getStructureName(final char[] text, final int offset, final int len) {

            final int index = binarySearch(this.repository, text, offset, len);

            if (index != -1) {
                return this.repository.get(index);
            }


            /*
             * NOT FOUND. We need to store the text
             */
            return storeStructureName(text, offset, len);

        }


        private char[] storeStructureName(final char[] text, final int offset, final int len) {

            final int index = binarySearch(this.repository, text, offset, len);
            if (index != -1) {
                // It was already added while we were waiting for the lock!
                return this.repository.get(index);
            }

            // We rely on the static structure name cache, just in case it is a standard HTML structure name
            final char[] structureName = MarkupStructureNameRepository.getStructureName(text, offset, len);

            this.repository.add(structureName);
            Collections.sort(this.repository, CharArrayComparator.INSTANCE);

            return structureName;

        }




        private static int binarySearch(final List<char[]> values,
                                        final char[] text, final int offset, final int len) {

            int low = 0;
            int high = values.size() - 1;

            while (low <= high) {

                final int mid = (low + high) >>> 1;
                final char[] midVal = values.get(mid);

                final int cmp = compare(midVal, text, offset, len);

                if (cmp < 0) {
                    low = mid + 1;
                } else if (cmp > 0) {
                    high = mid - 1;
                } else {
                    // Found!!
                    return mid;
                }

            }

            return -1;  // Not Found!!

        }


        private static int compare(final char[] ncr, final char[] text, final int offset, final int len) {
            final int maxCommon = Math.min(ncr.length, len);
            int i;
            for (i = 0; i < maxCommon; i++) {
                final char tc = text[offset + i];
                if (ncr[i] < tc) {
                    return -1;
                } else if (ncr[i] > tc) {
                    return 1;
                }
            }
            if (ncr.length > i) {
                return 1;
            }
            if (len > i) {
                return -1;
            }
            return 0;
        }


        private static class CharArrayComparator implements Comparator<char[]> {

            private static CharArrayComparator INSTANCE = new CharArrayComparator();

            public int compare(final char[] o1, final char[] o2) {
                return StructureNamesRepository.compare(o1, o2, 0, o2.length);
            }
        }

    }



}