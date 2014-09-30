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
package org.attoparser.markup.dom;

import java.util.LinkedHashMap;
import java.util.Map;

import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.markup.AbstractMarkupAttoHandler;
import org.attoparser.markup.IElementPreparationResult;
import org.attoparser.markup.dom.impl.CDATASection;
import org.attoparser.markup.dom.impl.Comment;
import org.attoparser.markup.dom.impl.DocType;
import org.attoparser.markup.dom.impl.Document;
import org.attoparser.markup.dom.impl.Element;
import org.attoparser.markup.dom.impl.ProcessingInstruction;
import org.attoparser.markup.dom.impl.Text;
import org.attoparser.markup.dom.impl.XmlDeclaration;


/**
 * <p>
 *   Implementation of {@link org.attoparser.IAttoHandler} that considers input
 *   as XML code and builds an attoDOM tree with objects from package 
 *   <tt>org.attoparser.markup.dom</tt>. 
 * </p>
 * <p>
 *   Use of this handler requires the document to be well-formed from the XML standpoint.
 * </p>
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class DOMBuilderMarkupAttoHandler extends AbstractMarkupAttoHandler {
    
    private final String documentName;
    
    private Document document = null;
    private boolean parsingFinished = false;
    private long parsingStartTimeNanos = -1L;
    private long parsingEndTimeNanos = -1L;
    private long parsingTotalTimeNanos = -1L;

    private INestableNode currentParent = null;


    private String currentElementName;
    private Map<String,String> currentElementAttributes;
    private int currentElementLine;
    private int currentElementCol;

    

    /**
     * <p>
     *   Creates a new instance of this handler.
     * </p> 
     */
    public DOMBuilderMarkupAttoHandler() {
        this(null);
    }

    /**
     * <p>
     *   Creates a new instance of this handler.
     * </p> 
     */
    public DOMBuilderMarkupAttoHandler(final String documentName) {
        super();
        this.documentName = 
                (documentName == null? 
                        String.valueOf(System.identityHashCode(this)) : documentName);
    }

    
    
    /**
     * <p>
     *   Returns the attoDOM {@link Document} created during parsing.
     * </p>
     * 
     * @return the built DOM document object. 
     */
    public Document getDocument() {
        return this.document;
    }

    

    /**
     * <p>
     *   Returns the time (in nanoseconds) when parsing started.
     * </p>
     * 
     * @return the start time.
     */
    public long getParsingStartTimeNanos() {
        return this.parsingStartTimeNanos;
    }

    /**
     * <p>
     *   Returns the time (in nanoseconds) when parsing ended.
     * </p>
     * 
     * @return the end time.
     */
    public long getParsingEndTimeNanos() {
        return this.parsingEndTimeNanos;
    }

    /**
     * <p>
     *   Returns the difference (in nanoseconds) between parsing start and end.
     * </p>
     * 
     * @return the parsing time in nanos.
     */
    public long getParsingTotalTimeNanos() {
        return this.parsingTotalTimeNanos;
    }

    
    /**
     * <p>
     *   Returns whether parsing has already finished or not.
     * </p>
     * 
     * @return <tt>true</tt> if parsing has finished, <tt>false</tt> if not.
     */
    public boolean isParsingFinished() {
        return this.parsingFinished;
    }


    

    @Override
    public IAttoHandleResult handleDocumentStart(
            final long startTimeNanos, 
            final int line, final int col)
            throws AttoParseException {
        
        this.document = new Document(this.documentName);
        this.parsingStartTimeNanos = startTimeNanos;

        return null;

    }

    
    
    @Override
    public IAttoHandleResult handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, 
            final int line, final int col)
            throws AttoParseException {

        this.parsingEndTimeNanos = endTimeNanos;
        this.parsingTotalTimeNanos = totalTimeNanos;
        
        this.parsingFinished = true;

        return null;

    }






    @Override
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

        final String version = new String(buffer, versionOffset, versionLen);
        final String encoding =
                (encodingOffset > 0?
                        new String(buffer, encodingOffset, encodingLen) :
                        null);
        final String standalone =
                (standaloneOffset > 0?
                        new String(buffer, standaloneOffset, standaloneLen) :
                        null);

        final XmlDeclaration xmlDeclaration = new XmlDeclaration(version, encoding, standalone);
        xmlDeclaration.setLine(Integer.valueOf(line));
        xmlDeclaration.setLine(Integer.valueOf(col));

        if (this.currentParent == null) {
            this.document.addChild(xmlDeclaration);
        } else {
            this.currentParent.addChild(xmlDeclaration);
        }

        return null;

    }



    @Override
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

        final String elementName = new String(buffer, elementNameOffset, elementNameLen);
        final String publicId = (publicIdOffset <= 0 ? null : new String(buffer, publicIdOffset, publicIdLen));
        final String systemId = (systemIdOffset <= 0 ? null : new String(buffer, systemIdOffset, systemIdLen));
        final String internalSubset =
                (internalSubsetOffset <= 0 ? null : new String(buffer, internalSubsetOffset, internalSubsetLen));

        final DocType docType = new DocType(elementName, publicId, systemId, internalSubset);
        docType.setLine(Integer.valueOf(outerLine));
        docType.setLine(Integer.valueOf(outerCol));

        if (this.currentParent == null) {
            this.document.addChild(docType);
        } else {
            this.currentParent.addChild(docType);
        }

        return null;

    }



    @Override
    public IAttoHandleResult handleCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {

        final CDATASection cdataSection = new CDATASection(new String(buffer, contentOffset, contentLen));
        cdataSection.setLine(Integer.valueOf(line));
        cdataSection.setLine(Integer.valueOf(col));

        if (this.currentParent == null) {
            this.document.addChild(cdataSection);
        } else {
            this.currentParent.addChild(cdataSection);
        }

        return null;

    }



    @Override
    public IAttoHandleResult handleComment(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {

        final Comment comment = new Comment(new String(buffer, contentOffset, contentLen));
        comment.setLine(Integer.valueOf(line));
        comment.setLine(Integer.valueOf(col));

        if (this.currentParent == null) {
            this.document.addChild(comment);
        } else {
            this.currentParent.addChild(comment);
        }

        return null;

    }



    @Override
    public IAttoHandleResult handleText(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        final Text text = new Text(new String(buffer, offset, len));
        text.setLine(Integer.valueOf(line));
        text.setLine(Integer.valueOf(col));

        if (this.currentParent == null) {
            this.document.addChild(text);
        } else {
            this.currentParent.addChild(text);
        }

        return null;

    }



    @Override
    public IElementPreparationResult prepareForElement(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {

        return null;

    }



    @Override
    public IAttoHandleResult handleStandaloneElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col)
            throws AttoParseException {

        this.currentElementName = new String(buffer, nameOffset, nameLen);
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;

        return null;

    }



    @Override
    public IAttoHandleResult handleStandaloneElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final boolean minimized, final int line, final int col)
            throws AttoParseException {

        final Element element = new Element(this.currentElementName);
        element.addAttributes(this.currentElementAttributes);
        element.setLine(Integer.valueOf(this.currentElementLine));
        element.setLine(Integer.valueOf(this.currentElementCol));

        if (this.currentParent == null) {
            this.document.addChild(element);
        } else {
            this.currentParent.addChild(element);
        }

        return null;

    }



    @Override
    public IAttoHandleResult handleOpenElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {

        this.currentElementName = new String(buffer, nameOffset, nameLen);
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;

        return null;

    }



    @Override
    public IAttoHandleResult handleOpenElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        final Element element = new Element(this.currentElementName);
        element.addAttributes(this.currentElementAttributes);
        element.setLine(Integer.valueOf(this.currentElementLine));
        element.setLine(Integer.valueOf(this.currentElementCol));

        if (this.currentParent == null) {
            this.document.addChild(element);
        } else {
            this.currentParent.addChild(element);
        }
        this.currentParent = element;

        return null;

    }



    @Override
    public IAttoHandleResult handleCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {

        this.currentElementName = new String(buffer, nameOffset, nameLen);
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;

        return null;

    }



    @Override
    public IAttoHandleResult handleCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        this.currentParent = this.currentParent.getParent();

        return null;

    }




    @Override
    public IAttoHandleResult handleAutoCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {

        this.currentElementName = new String(buffer, nameOffset, nameLen);
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;

        return null;

    }



    @Override
    public IAttoHandleResult handleAutoCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        this.currentParent = this.currentParent.getParent();

        return null;

    }




    @Override
    public IAttoHandleResult handleUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {

        // Unmatched closings should have no effect in DOM
        return null;

    }



    @Override
    public IAttoHandleResult handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        // Unmatched closings have no effect in DOM
        return null;

    }




    @Override
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
    public IAttoHandleResult handleInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        // Nothing to be done here - we will ignore inner whitespace
        return null;

    }



    @Override
    public IAttoHandleResult handleProcessingInstruction(
            final char[] buffer,
            final int targetOffset, final int targetLen,
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {

        final String target = new String(buffer, targetOffset, targetLen);
        final String content = (contentOffset <= 0 ? null : new String(buffer, contentOffset, contentLen));

        final ProcessingInstruction processingInstruction =
                new ProcessingInstruction(target, content);
        processingInstruction.setLine(Integer.valueOf(line));
        processingInstruction.setLine(Integer.valueOf(col));

        if (this.currentParent == null) {
            this.document.addChild(processingInstruction);
        } else {
            this.currentParent.addChild(processingInstruction);
        }

        return null;

    }


}
