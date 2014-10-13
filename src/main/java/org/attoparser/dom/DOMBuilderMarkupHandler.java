/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2014, The ATTOPARSER team (http://www.attoparser.org)
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
package org.attoparser.dom;

import java.util.LinkedHashMap;
import java.util.Map;

import org.attoparser.AbstractMarkupHandler;
import org.attoparser.ParseException;


/**
 * <p>
 *   Implementation of {@link org.attoparser.IMarkupHandler} that builds a DOM tree using objects of
 *   classes from package <tt>org.attoparser.dom</tt>.
 * </p>
 * <p>
 *   Note that this handler, when not used as a part of a larger chain of handlers, can be more easily applied
 *   by making use of the {@link org.attoparser.dom.DOMMarkupParser} class.
 * </p>
 * <p>
 *   Sample usage:
 * </p>
 * <pre><code>
 *   // Obtain a java.io.Reader on the document to be parsed
 *   final Reader documentReader = ...;
 *
 *   // Instance the DOM-builder handler
 *   final DOMBuilderMarkupHandler handler = new DOMBuilderMarkupHandler("Some document");
 *
 *   // Create or obtain the parser instance
 *   final IMarkupParser parser = new MarkupParser(ParseConfiguration.htmlConfiguration());
 *
 *   // Parse the document
 *   parser.parse(documentReader, handler);
 *
 *   // Obtain the parsed Document Object Model
 *   final Document document = handler.getDocument();
 * </code></pre>
 * <p>
 *   Note that, as with most handlers, this class is <strong>not thread-safe</strong>. Also, instances of this class
 *   should not be reused across parsing operations.
 * </p>
 *
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public final class DOMBuilderMarkupHandler extends AbstractMarkupHandler {
    
    private final String documentName;
    
    private Document document = null;
    private boolean parsingFinished = false;
    private long parsingStartTimeNanos = -1L;
    private long parsingEndTimeNanos = -1L;
    private long parsingTotalTimeNanos = -1L;

    private INestableNode currentParent = null;


    private String currentElementName = null;
    private Map<String,String> currentElementAttributes = null;
    private int currentElementLine = -1;
    private int currentElementCol = -1;

    

    /**
     * <p>
     *   Creates a new instance of this handler.
     * </p> 
     */
    public DOMBuilderMarkupHandler() {
        this(null);
    }

    /**
     * <p>
     *   Creates a new instance of this handler.
     * </p>
     *
     * @param documentName the name of the document to be parsed.
     */
    public DOMBuilderMarkupHandler(final String documentName) {
        super();
        this.documentName = 
                (documentName == null? 
                        String.valueOf(System.identityHashCode(this)) : documentName);
    }

    
    
    /**
     * <p>
     *   Returns the {@link Document} DOM created during parsing.
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
    public void handleDocumentStart(
            final long startTimeNanos, 
            final int line, final int col)
            throws ParseException {
        
        this.document = new Document(this.documentName);
        this.parsingStartTimeNanos = startTimeNanos;

    }

    
    
    @Override
    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, 
            final int line, final int col)
            throws ParseException {

        this.parsingEndTimeNanos = endTimeNanos;
        this.parsingTotalTimeNanos = totalTimeNanos;
        
        this.parsingFinished = true;

    }






    @Override
    public void handleXmlDeclaration(
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
            throws ParseException {

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

    }



    @Override
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
            throws ParseException {

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

    }



    @Override
    public void handleCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        final CDATASection cdataSection = new CDATASection(new String(buffer, contentOffset, contentLen));
        cdataSection.setLine(Integer.valueOf(line));
        cdataSection.setLine(Integer.valueOf(col));

        if (this.currentParent == null) {
            this.document.addChild(cdataSection);
        } else {
            this.currentParent.addChild(cdataSection);
        }

    }



    @Override
    public void handleComment(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        final Comment comment = new Comment(new String(buffer, contentOffset, contentLen));
        comment.setLine(Integer.valueOf(line));
        comment.setLine(Integer.valueOf(col));

        if (this.currentParent == null) {
            this.document.addChild(comment);
        } else {
            this.currentParent.addChild(comment);
        }

    }



    @Override
    public void handleText(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        final Text text = new Text(new String(buffer, offset, len));
        text.setLine(Integer.valueOf(line));
        text.setLine(Integer.valueOf(col));

        if (this.currentParent == null) {
            this.document.addChild(text);
        } else {
            this.currentParent.addChild(text);
        }

    }



    @Override
    public void handleStandaloneElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col)
            throws ParseException {

        this.currentElementName = StructureTextsRepository.getStructureName(buffer, nameOffset, nameLen);
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;

    }



    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col)
            throws ParseException {

        final Element element = new Element(this.currentElementName);
        element.addAttributes(this.currentElementAttributes);
        element.setLine(Integer.valueOf(this.currentElementLine));
        element.setLine(Integer.valueOf(this.currentElementCol));

        if (this.currentParent == null) {
            this.document.addChild(element);
        } else {
            this.currentParent.addChild(element);
        }

    }



    @Override
    public void handleOpenElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.currentElementName = StructureTextsRepository.getStructureName(buffer, nameOffset, nameLen);
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;

    }



    @Override
    public void handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

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

    }



    @Override
    public void handleCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.currentElementName = StructureTextsRepository.getStructureName(buffer, nameOffset, nameLen);
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;

    }



    @Override
    public void handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.currentParent = this.currentParent.getParent();

    }




    @Override
    public void handleAutoCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.currentElementName = StructureTextsRepository.getStructureName(buffer, nameOffset, nameLen);
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;

    }



    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.currentParent = this.currentParent.getParent();

    }




    @Override
    public void handleUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        // Unmatched closings should have no effect in DOM

    }



    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        // Unmatched closings have no effect in DOM

    }




    @Override
    public void handleAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws ParseException {

        final String attributeName = StructureTextsRepository.getStructureName(buffer, nameOffset, nameLen);
        final String attributeValue =
                (valueContentLen <= 0?  "" : new String(buffer, valueContentOffset, valueContentLen));

        if (this.currentElementAttributes == null) {
            this.currentElementAttributes = new LinkedHashMap<String, String>(5, 1.0f);
        }

        this.currentElementAttributes.put(attributeName, attributeValue);

    }



    @Override
    public void handleInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        // Nothing to be done here - we will ignore inner whitespace

    }



    @Override
    public void handleProcessingInstruction(
            final char[] buffer,
            final int targetOffset, final int targetLen,
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

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

    }


}
