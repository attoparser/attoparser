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
package org.attoparser.simple;

import java.util.LinkedHashMap;
import java.util.Map;

import org.attoparser.AbstractMarkupHandler;
import org.attoparser.ParseException;


/**
 * <p>
 *   Implementation of the {@link org.attoparser.IMarkupHandler} interface aimed at converting the events launched
 *   through this interface to the simpler ones at the {@link org.attoparser.simple.ISimpleMarkupHandler} interface.
 * </p>
 * <p>
 *   Note that this handler, when not used as a part of a larger chain of handlers, can be more easily applied
 *   by making use of the {@link org.attoparser.simple.SimpleMarkupParser} class.
 * </p>
 * <p>
 *   Sample usage:
 * </p>
 * <pre><code>
 *   // Obtain a java.io.Reader on the document to be parsed
 *   final Reader documentReader = ...;
 *
 *   // Create the handler instance. Extending the no-op AbstractSimpleMarkupHandler is a good start
 *   final ISimpleMarkupHandler simpleHandler = new AbstractSimpleMarkupHandler() {
 *       ... // some events implemented
 *   };
 *
 *   // Create a handler chain with the 'simplifier' handler, which will convert events from 'normal' to 'simple'.
 *   final IMarkupHandler handler = new SimplifierMarkupHandler(simpleHandler);
 *
 *   // Create or obtain the parser instance (note this is not the 'simple' one!)
 *   final IMarkupParser parser = new MarkupParser(ParseConfiguration.htmlConfiguration());
 *
 *   // Parse it!
 *   parser.parse(documentReader, handler);
 * </code></pre>
 * <p>
 *   Note that, as with most handlers, this class is <strong>not thread-safe</strong>. Also, instances of this class
 *   should not be reused across parsing operations.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public final class SimplifierMarkupHandler extends AbstractMarkupHandler {


    private final ISimpleMarkupHandler handler;
    
    private String currentElementName;
    private Map<String,String> currentElementAttributes;
    private int currentElementLine;
    private int currentElementCol;





    /**
     * <p>
     *   Creates a new instance of this parser.
     * </p>
     *
     * @param handler the <strong>simple</strong> handler to which events will be delegated.
     */
    public SimplifierMarkupHandler(final ISimpleMarkupHandler handler) {
        super();
        if (handler == null) {
            throw new IllegalArgumentException("Delegate handler cannot be null");
        }
        this.handler = handler;
    }




    @Override
    public void handleDocumentStart(
            final long startTimeNanos,
            final int line, final int col)
            throws ParseException {

        this.handler.handleDocumentStart(startTimeNanos, line, col);

    }



    @Override
    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos,
            final int line, final int col)
            throws ParseException {

        this.handler.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);

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

        this.handler.handleXmlDeclaration(version, encoding, standalone, line, col);

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

        this.handler.handleDocType(
                new String(buffer, elementNameOffset, elementNameLen),
                (publicIdOffset <= 0 ? null : new String(buffer, publicIdOffset, publicIdLen)),
                (systemIdOffset <= 0 ? null : new String(buffer, systemIdOffset, systemIdLen)),
                (internalSubsetOffset <= 0 ? null : new String(buffer, internalSubsetOffset, internalSubsetLen)),
                outerLine, outerCol);

    }



    @Override
    public void handleCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        this.handler.handleCDATASection(buffer, contentOffset, contentLen, line, col);

    }



    @Override
    public void handleComment(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        this.handler.handleComment(buffer, contentOffset, contentLen, line, col);

    }



    @Override
    public void handleText(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        this.handler.handleText(buffer, offset, len, line, col);

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
        
        this.handler.handleStandaloneElement(
                this.currentElementName, this.currentElementAttributes, minimized, this.currentElementLine, this.currentElementCol);

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

        this.handler.handleOpenElement(this.currentElementName, this.currentElementAttributes, this.currentElementLine, this.currentElementCol);

    }



    @Override
    public void handleAutoOpenElementStart(
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
    public void handleAutoOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.handler.handleAutoOpenElement(this.currentElementName, this.currentElementAttributes, this.currentElementLine, this.currentElementCol);

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

        this.handler.handleCloseElement(this.currentElementName, this.currentElementLine, this.currentElementCol);

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

        this.handler.handleAutoCloseElement(this.currentElementName, this.currentElementLine, this.currentElementCol);

    }


    
    
    @Override
    public void handleUnmatchedCloseElementStart(
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
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.handler.handleUnmatchedCloseElement(this.currentElementName, this.currentElementLine, this.currentElementCol);

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
            this.currentElementAttributes = new LinkedHashMap<String, String>(3, 1.0f);
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

        this.handler.handleProcessingInstruction(
                new String(buffer, targetOffset, targetLen),
                (contentOffset <= 0 ? null : new String(buffer, contentOffset, contentLen)),
                line, col);

    }



}