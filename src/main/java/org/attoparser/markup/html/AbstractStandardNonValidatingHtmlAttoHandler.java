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
package org.attoparser.markup.html;

import java.util.LinkedHashMap;
import java.util.Map;

import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.markup.MarkupParsingConfiguration;
import org.attoparser.markup.html.elements.IHtmlElement;






/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public abstract class AbstractStandardNonValidatingHtmlAttoHandler 
        extends AbstractDetailedNonValidatingHtmlAttoHandler {

    
    private String currentElementName;
    private Map<String,String> currentElementAttributes;
    private int currentElementLine;
    private int currentElementCol;
    


    protected AbstractStandardNonValidatingHtmlAttoHandler(final MarkupParsingConfiguration configuration) {
        super(configuration);
    }




    private static String getInternedElementName(final IHtmlElement element, final char[] nameBuffer, final int nameOffset, final int nameLen) {
        // Check case-equality. If they match, use the already-existing String from the element object
        final int maxi = nameOffset + nameLen;
        final String elementName = element.getName();
        for (int i = nameOffset; i < maxi; i++) {
            if (nameBuffer[i] != elementName.charAt(i - nameOffset)) {
                return new String(nameBuffer, nameOffset, nameLen);
            }
        }
        return element.getName();
    }
    
    
    
    
    
    @Override
    public final IAttoHandleResult handleHtmlStandaloneElementStart(
            final IHtmlElement element,
            final boolean minimized, 
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col) 
            throws AttoParseException {

        this.currentElementName = getInternedElementName(element, buffer, nameOffset, nameLen);
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;

        return null;
        
    }


    @Override
    public final IAttoHandleResult handleHtmlStandaloneElementEnd(
            final IHtmlElement element,
            final boolean minimized,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {

        return handleHtmlStandaloneElement(
                element, minimized,
                this.currentElementName, this.currentElementAttributes,
                this.currentElementLine, this.currentElementCol);

    }


    
    
    @Override
    public final IAttoHandleResult handleHtmlOpenElementStart(
            final IHtmlElement element, 
            final char[] buffer,
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws AttoParseException {

        this.currentElementName = getInternedElementName(element, buffer, nameOffset, nameLen);
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;

        return null;
        
    }


    @Override
    public final IAttoHandleResult handleHtmlOpenElementEnd(
            final IHtmlElement element,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {

        return handleHtmlOpenElement(
                element,
                this.currentElementName, this.currentElementAttributes,
                this.currentElementLine, this.currentElementCol);
        
    }




    @Override
    public final IAttoHandleResult handleHtmlCloseElementStart(
            final IHtmlElement element,
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws AttoParseException {

        this.currentElementName = getInternedElementName(element, buffer, nameOffset, nameLen);
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;

        return null;
        
    }

    
    @Override
    public final IAttoHandleResult handleHtmlCloseElementEnd(
            final IHtmlElement element,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            int line, int col)
            throws AttoParseException {

        return handleHtmlCloseElement(element, this.currentElementName, this.currentElementLine, this.currentElementCol);
        
    }




    @Override
    public final IAttoHandleResult handleHtmlAttribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, 
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol) 
            throws AttoParseException {

        final String attributeName = new String(buffer, nameOffset, nameLen);
        final String attributeValue = 
                (valueContentLen <= 0?  "" : new String(buffer, valueContentOffset, valueContentLen));
        
        if (this.currentElementAttributes == null) {
            this.currentElementAttributes = new LinkedHashMap<String, String>(5, 1.0f);
        }

        this.currentElementAttributes.put(attributeName, attributeValue);

        return null;

    }
   

    @Override
    public final IAttoHandleResult handleHtmlInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {

        return null;
        
    }


    
    
    @Override
    public final IAttoHandleResult handleDocType(
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

        return handleDocType(
                new String(buffer, elementNameOffset, elementNameLen),
                (publicIdOffset <= 0? null : new String(buffer, publicIdOffset, publicIdLen)),
                (systemIdOffset <= 0? null : new String(buffer, systemIdOffset, systemIdLen)),
                (internalSubsetOffset <= 0? null : new String(buffer, internalSubsetOffset, internalSubsetLen)),
                outerLine, outerCol);
        
    }


    
    @Override
    public final IAttoHandleResult handleComment(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        return handleComment(buffer, contentOffset, contentLen, line, col);
        
    }
    


    
    @Override
    public final IAttoHandleResult handleCDATASection(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        return handleCDATASection(buffer, contentOffset, contentLen, line, col);
        
    }
    
    

    
    @Override
    public final IAttoHandleResult handleXmlDeclarationDetail(
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

        final String version = new String(buffer, versionOffset, versionLen);
        final String encoding =
                (encodingOffset > 0?
                        new String(buffer, encodingOffset, encodingLen) :
                        null);
        final String standalone =
                (standaloneOffset > 0?
                        new String(buffer, standaloneOffset, standaloneLen) :
                        null);
        
        return handleXmlDeclaration(version, encoding, standalone, line, col);
        
    }


    
    
    @Override
    public final IAttoHandleResult handleProcessingInstruction(
            final char[] buffer, 
            final int targetOffset, final int targetLen, 
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        return handleProcessingInstruction(
                new String(buffer, targetOffset, targetLen), 
                (contentOffset <= 0? null : new String(buffer, contentOffset, contentLen)), 
                line, col);
        
    }


    
    
    

    /*
     * -----------------------------------
     * -----------------------------------
     *  METHODS MEANT TO BE OVERRIDDEN
     * -----------------------------------
     * -----------------------------------
     */









    /**
     * <p>
     *   Called when a standalone element (a <i>minimized tag</i>) is found.
     * </p>
     * <p>
     *   Note that <b>the element attributes map can be null if no attributes are present</b>. 
     * </p>
     * 
     * @param element the {@link IHtmlElement} element object representing the corresponding HTML element.
     * @param minimized whether the tag representing this element is minimized (self-closed) or not.
     * @param elementName the element name (e.g. "&lt;img src="logo.png"&gt;" -> "img").
     * @param attributes the element attributes map, or null if no attributes are present.
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public IAttoHandleResult handleHtmlStandaloneElement(
            final IHtmlElement element,
            final boolean minimized,
            final String elementName, 
            final Map<String,String> attributes,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }
    
    
    /**
     * <p>
     *   Called when an open element (an <i>open tag</i>) is found.
     * </p>
     * <p>
     *   Note that <b>the element attributes map can be null if no attributes are present</b>. 
     * </p>
     * 
     * @param element the {@link IHtmlElement} element object representing the corresponding HTML element.
     * @param elementName the element name (e.g. "&lt;div class="content"&gt;" -> "div").
     * @param attributes the element attributes map, or null if no attributes are present.
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public IAttoHandleResult handleHtmlOpenElement(
            final IHtmlElement element,
            final String elementName, 
            final Map<String,String> attributes,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }
    
    
    /**
     * <p>
     *   Called when a close element (a <i>close tag</i>) is found.
     * </p>
     * 
     * @param element the {@link IHtmlElement} element object representing the corresponding HTML element.
     * @param elementName the element name (e.g. "&lt;/div&gt;" -> "div").
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public IAttoHandleResult handleHtmlCloseElement(
            final IHtmlElement element,
            final String elementName, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
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
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public IAttoHandleResult handleDocType(
            final String elementName, final String publicId, final String systemId, 
            final String internalSubset, final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
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
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public IAttoHandleResult handleComment(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
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
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public IAttoHandleResult handleCDATASection(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
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
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public IAttoHandleResult handleXmlDeclaration(
            final String version, 
            final String encoding, 
            final String standalone, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
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
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    @SuppressWarnings("unused")
    public IAttoHandleResult handleProcessingInstruction(
            final String target, final String content, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }
    
    
    
}