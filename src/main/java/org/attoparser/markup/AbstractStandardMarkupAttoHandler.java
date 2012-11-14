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

import java.util.LinkedHashMap;
import java.util.Map;

import org.attoparser.AttoParseException;







/**
 * <p>
 *   Base abstract implementations for markup-specialized attohandlers that offer an event
 *   handling interface similar to that of the standard SAX {@link org.xml.sax.ContentHandler}.
 * </p>
 * <p>
 *   Handlers extending from this class can make use of a {@link MarkupParsingConfiguration} instance
 *   specifying a markup parsing configuration to be applied during document parsing (for example, 
 *   for ensuring that a document is well-formed from an XML/XHTML standpoint).
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
public abstract class AbstractStandardMarkupAttoHandler 
        extends AbstractDetailedMarkupAttoHandler {

    
    private String currentElementName;
    private Map<String,String> currentElementAttributes;
    private int currentElementLine;
    private int currentElementCol;
    
    
    
    
    
    protected AbstractStandardMarkupAttoHandler() {
        super();
    }
    
    protected AbstractStandardMarkupAttoHandler(final MarkupParsingConfiguration markupParsingConfiguration) {
        super(markupParsingConfiguration);
    }
    
    

    @Override
    public final void handleStandaloneElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        
        super.handleStandaloneElementStart(buffer, offset, len, line, col);
        
        this.currentElementName = null;
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;
        
    }
    
    

    @Override
    public final void handleStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {

        super.handleStandaloneElementName(buffer, offset, len, line, col);
        
        this.currentElementName = new String(buffer, offset, len);
        
    }

    
    
    @Override
    public final void handleStandaloneElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        
        super.handleStandaloneElementEnd(buffer, offset, len, line, col);
        
        handleStandaloneElement(this.currentElementName, this.currentElementAttributes, this.currentElementLine, this.currentElementCol);
        
    }

    
    
    @Override
    public final void handleOpenElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleOpenElementStart(buffer, offset, len, line, col);

        this.currentElementName = null;
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;
        
    }
    
    

    @Override
    public final void handleOpenElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {

        super.handleOpenElementName(buffer, offset, len, line, col);
        
        this.currentElementName = new String(buffer, offset, len);
        
    }

    
    
    @Override
    public final void handleOpenElementEnd(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleOpenElementEnd(buffer, offset, len, line, col);
        
        handleOpenElement(this.currentElementName, this.currentElementAttributes, this.currentElementLine, this.currentElementCol);
        
    }

    
    
    @Override
    public final void handleCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleCloseElementStart(buffer, offset, len, line, col);
        
        this.currentElementName = null;
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;
        
    }
    
    

    @Override
    public final void handleCloseElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {

        super.handleCloseElementName(buffer, offset, len, line, col);
        
        this.currentElementName = new String(buffer, offset, len);
        
    }

    
    
    @Override
    public final void handleCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {

        super.handleCloseElementEnd(buffer, offset, len, line, col);

        handleCloseElement(this.currentElementName, this.currentElementLine, this.currentElementCol);
        
    }

    
    
    
    @Override
    public final void handleAutoCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleAutoCloseElementStart(buffer, offset, len, line, col);
        
        this.currentElementName = null;
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;
        
    }
    
    

    @Override
    public final void handleAutoCloseElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {

        super.handleAutoCloseElementName(buffer, offset, len, line, col);
        
        this.currentElementName = new String(buffer, offset, len);
        
    }

    
    
    @Override
    public final void handleAutoCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {

        super.handleAutoCloseElementEnd(buffer, offset, len, line, col);

        handleBalancedCloseElement(this.currentElementName, this.currentElementLine, this.currentElementCol);
        
    }

    
    
    @Override
    public final void handleAttribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, 
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {

        super.handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                valueLine, valueCol);
        
        final String attributeName = new String(buffer, nameOffset, nameLen);
        final String attributeValue = 
                (valueContentLen <= 0?  "" : new String(buffer, valueContentOffset, valueContentLen));
        
        if (this.currentElementAttributes == null) {
            this.currentElementAttributes = new LinkedHashMap<String, String>(5, 1.0f);
        }

        this.currentElementAttributes.put(attributeName, attributeValue);
        
    }

    
    
    @Override
    public final void handleAttributeSeparator(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleAttributeSeparator(buffer, offset, len, line, col);
        
    }

    
    
    @Override
    public final void handleDocType(
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

        super.handleDocType(buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                typeOffset, typeLen, typeLine, typeCol, publicIdOffset, publicIdLen,
                publicIdLine, publicIdCol, systemIdOffset, systemIdLen, systemIdLine,
                systemIdCol, internalSubsetOffset, internalSubsetLen, internalSubsetLine, 
                internalSubsetCol, outerOffset, outerLen, outerLine, outerCol);
        
        handleDocType(
                new String(buffer, elementNameOffset, elementNameLen),
                (publicIdOffset <= 0? null : new String(buffer, publicIdOffset, publicIdLen)),
                (systemIdOffset <= 0? null : new String(buffer, systemIdOffset, systemIdLen)),
                (internalSubsetOffset <= 0? null : new String(buffer, internalSubsetOffset, internalSubsetLen)),
                outerLine, outerCol);
        
    }


    
    @Override
    public final void handleComment(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        super.handleComment(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        
        handleComment(buffer, contentOffset, contentLen, line, col);
        
    }


    
    @Override
    public final void handleCDATASection(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        super.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        
        handleCDATASection(buffer, contentOffset, contentLen, line, col);
        
    }
    
    

    @Override
    public final void handleXmlDeclarationDetail(
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

        super.handleXmlDeclarationDetail(
                buffer, 
                keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol, 
                encodingOffset, encodingLen, encodingLine, encodingCol, 
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol, 
                outerOffset, outerLen, line, col);
        
        final String version = new String(buffer, versionOffset, versionLen);
        final String encoding =
                (encodingOffset > 0?
                        new String(buffer, encodingOffset, encodingLen) :
                        null);
        final String standalone =
                (standaloneOffset > 0?
                        new String(buffer, standaloneOffset, standaloneLen) :
                        null);
        
        handleXmlDeclaration(version, encoding, standalone, line, col);
        
    }


    
    @Override
    public final void handleProcessingInstruction(
            final char[] buffer, 
            final int targetOffset, final int targetLen, 
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        super.handleProcessingInstruction(
                buffer, 
                targetOffset, targetLen, targetLine, targetCol, 
                contentOffset, contentLen, contentLine, contentCol,
                outerOffset, outerLen, line, col);
        
        handleProcessingInstruction(
                new String(buffer, targetOffset, targetLen), 
                (contentOffset <= 0? null : new String(buffer, contentOffset, contentLen)), 
                line, col);
        
    }


    
    

    /**
     * <p>
     *   Called when a standalone element (a <i>minimized tag</i>) is found.
     * </p>
     * <p>
     *   Note that <b>the element attributes map can be null if no attributes are present</b>. 
     * </p>
     * 
     * @param elementName the element name (e.g. "&lt;img src="logo.png"&gt;" -> "img").
     * @param attributes the element attributes map, or null if no attributes are present.
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleStandaloneElement(
            final String elementName, final Map<String,String> attributes,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    /**
     * <p>
     *   Called when an open element (an <i>open tag</i>) is found.
     * </p>
     * <p>
     *   Note that <b>the element attributes map can be null if no attributes are present</b>. 
     * </p>
     * 
     * @param elementName the element name (e.g. "&lt;div class="content"&gt;" -> "div").
     * @param attributes the element attributes map, or null if no attributes are present.
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleOpenElement(
            final String elementName, final Map<String,String> attributes,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    

    /**
     * <p>
     *   Called when a close element (a <i>close tag</i>) is found.
     * </p>
     * 
     * @param elementName the element name (e.g. "&lt;/div&gt;" -> "div").
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleCloseElement(
            final String elementName, final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    

    /**
     * <p>
     *   Called when a close element (a <i>close tag</i>) is needed in order
     *   to correctly balance the markup.
     * </p>
     * <p>
     *   Implementors might choose to ignore these balancing events.
     * </p>
     * 
     * @param elementName the element name (e.g. "&lt;/div&gt;" -> "div").
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleBalancedCloseElement(
            final String elementName, final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
    /**
     * <p>
     *   Called when a DOCTYPE clause is found.
     * </p>
     * 
     * @param elementName the root element name present in the DOCTYPE clause (e.g. "html").
     * @param publicId the public ID specified, if present (might be null).
     * @param systemId the system ID specified, if present (might be null).
     * @param internalSubset the internal subset specified, if present (might be null).
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleDocType(
            final String elementName, final String publicId, final String systemId, 
            final String internalSubset, final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    

    
    /**
     * <p>
     *   Called when a comment is found.
     * </p>
     * <p>
     *   This artifact is returned as a <tt>char[]</tt> instead of a <tt>String</tt>
     *   because its content can be large. In order to convert it into a <tt>String</tt>,
     *   just do <tt>new String(buffer, offset, len)</tt>.
     * </p>
     * 
     * @param buffer the document buffer.
     * @param offset the offset of the artifact in the document buffer.
     * @param len the length (in chars) of the artifact.
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleComment(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    

    
    /**
     * <p>
     *   Called when a CDATA section is found.
     * </p>
     * <p>
     *   This artifact is returned as a <tt>char[]</tt> instead of a <tt>String</tt>
     *   because its content can be large. In order to convert it into a <tt>String</tt>,
     *   just do <tt>new String(buffer, offset, len)</tt>.
     * </p>
     * 
     * @param buffer the document buffer.
     * @param offset the offset of the artifact in the document buffer.
     * @param len the length (in chars) of the artifact.
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleCDATASection(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
    /**
     * <p>
     *   Called when an XML Declaration is found.
     * </p>
     * 
     * @param version the version value specified (cannot be null).
     * @param encoding the encoding value specified (can be null).
     * @param standalone the standalone value specified (can be null).
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleXmlDeclaration(
            final String version, 
            final String encoding, 
            final String standalone, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
    /**
     * <p>
     *   Called when a Processing Instruction is found.
     * </p>
     * 
     * @param target the target specified in the processing instruction.
     * @param content the content of the processing instruction, if specified (might be null).
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public void handleProcessingInstruction(
            final String target, final String content, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
}