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
package org.attoparser.markup.simple;

import java.util.LinkedHashMap;
import java.util.Map;

import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.markup.AbstractMarkupAttoHandler;
import org.attoparser.markup.IElementPreparationResult;


/**
 * <p>
 *   Base abstract implementations for markup-specialized attohandlers that offer an event
 *   handling interface similar to that of the standard SAX {@link org.xml.sax.ContentHandler}.
 * </p>
 * <p>
 *   Handlers extending from this class can make use of a {@link org.attoparser.markup.MarkupParsingConfiguration} instance
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
public final class SimplifierMarkupAttoHandler extends AbstractMarkupAttoHandler {


    private final ISimpleMarkupAttoHandler handler;
    
    private String currentElementName;
    private Map<String,String> currentElementAttributes;
    private int currentElementLine;
    private int currentElementCol;
    
    
    
    
    
    public SimplifierMarkupAttoHandler(final ISimpleMarkupAttoHandler handler) {
        super();
        this.handler = handler;
    }


    @Override
    public IAttoHandleResult handleDocumentStart(
            final long startTimeNanos,
            final int line, final int col)
            throws AttoParseException {

        this.handler.handleDocumentStart(startTimeNanos, line, col);
        return null;

    }



    @Override
    public IAttoHandleResult handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos,
            final int line, final int col)
            throws AttoParseException {

        this.handler.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);
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

        this.handler.handleXmlDeclaration(version, encoding, standalone, line, col);
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

        this.handler.handleDocType(
                new String(buffer, elementNameOffset, elementNameLen),
                (publicIdOffset <= 0 ? null : new String(buffer, publicIdOffset, publicIdLen)),
                (systemIdOffset <= 0 ? null : new String(buffer, systemIdOffset, systemIdLen)),
                (internalSubsetOffset <= 0 ? null : new String(buffer, internalSubsetOffset, internalSubsetLen)),
                outerLine, outerCol);
        return null;

    }



    @Override
    public IAttoHandleResult handleCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {

        this.handler.handleCDATASection(buffer, contentOffset, contentLen, line, col);
        return null;

    }



    @Override
    public IAttoHandleResult handleComment(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {

        this.handler.handleComment(buffer, contentOffset, contentLen, line, col);
        return null;

    }



    @Override
    public IAttoHandleResult handleText(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        this.handler.handleText(buffer, offset, len, line, col);
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
        
        this.handler.handleStandaloneElement(
                this.currentElementName, this.currentElementAttributes, minimized, this.currentElementLine, this.currentElementCol);
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

        this.handler.handleOpenElement(this.currentElementName, this.currentElementAttributes, this.currentElementLine, this.currentElementCol);
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

        this.handler.handleCloseElement(this.currentElementName, this.currentElementLine, this.currentElementCol);
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

        this.handler.handleAutoCloseElement(this.currentElementName, this.currentElementLine, this.currentElementCol);
        return null;

    }


    
    
    @Override
    public IAttoHandleResult handleUnmatchedCloseElementStart(
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
    public IAttoHandleResult handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        this.handler.handleUnmatchedCloseElement(this.currentElementName, this.currentElementLine, this.currentElementCol);
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
            this.currentElementAttributes = new LinkedHashMap<String, String>(3, 1.0f);
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

        this.handler.handleProcessingInstruction(
                new String(buffer, targetOffset, targetLen),
                (contentOffset <= 0 ? null : new String(buffer, contentOffset, contentLen)),
                line, col);
        return null;
        
    }



}