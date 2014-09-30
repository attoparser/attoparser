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
package org.attoparser.html;

import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.AbstractMarkupAttoHandler;
import org.attoparser.IElementPreparationResult;
import org.attoparser.IMarkupAttoHandler;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class HtmlMarkupAttoHandler extends AbstractMarkupAttoHandler {


    private final IMarkupAttoHandler handler;
    private IHtmlElement currentElement = null;




    public HtmlMarkupAttoHandler(final IMarkupAttoHandler handler) {
        super();
        this.handler = handler;
    }




    @Override
    public IAttoHandleResult handleDocumentStart(
            final long startTimeNanos, final int line, final int col)
            throws AttoParseException {

        return this.handler.handleDocumentStart(startTimeNanos, line, col);

    }



    @Override
    public IAttoHandleResult handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws AttoParseException {

        return this.handler.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);

    }



    @Override
    public IAttoHandleResult handleXmlDeclaration(
            final char[] buffer,
            final int keywordOffset, final int keywordLen, final int keywordLine, final int keywordCol,
            final int versionOffset, final int versionLen, final int versionLine, final int versionCol,
            final int encodingOffset, final int encodingLen, final int encodingLine, final int encodingCol,
            final int standaloneOffset, final int standaloneLen, final int standaloneLine, final int standaloneCol,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {

        return this.handler.handleXmlDeclaration(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol,
                encodingOffset, encodingLen, encodingLine, encodingCol,
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen,
                line, col);

    }



    @Override
    public IAttoHandleResult handleDocType(
            final char[] buffer,
            final int keywordOffset, final int keywordLen, final int keywordLine, final int keywordCol,
            final int elementNameOffset, final int elementNameLen, final int elementNameLine, final int elementNameCol,
            final int typeOffset, final int typeLen, final int typeLine, final int typeCol,
            final int publicIdOffset, final int publicIdLen, final int publicIdLine, final int publicIdCol,
            final int systemIdOffset, final int systemIdLen, final int systemIdLine, final int systemIdCol,
            final int internalSubsetOffset, final int internalSubsetLen, final int internalSubsetLine, final int internalSubsetCol,
            final int outerOffset, final int outerLen,
            final int outerLine, final int outerCol)
            throws AttoParseException {

        return this.handler.handleDocType(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                typeOffset, typeLen, typeLine, typeCol,
                publicIdOffset, publicIdLen, publicIdLine, publicIdCol,
                systemIdOffset, systemIdLen, systemIdLine, systemIdCol,
                internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol,
                outerOffset, outerLen,
                outerLine, outerCol);

    }



    @Override
    public IAttoHandleResult handleCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {

        return this.handler.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);

    }



    @Override
    public IAttoHandleResult handleComment(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {

        return this.handler.handleComment(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);

    }



    @Override
    public IAttoHandleResult handleText(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        return this.handler.handleText(buffer, offset, len, line, col);

    }



    @Override
    public final IElementPreparationResult prepareForElement(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        final IHtmlElement elementToPrepare = HtmlElements.forName(buffer, offset, len);
        return elementToPrepare.prepareForElement(buffer, offset, len, line, col, this.handler);

    }



    @Override
    public final IAttoHandleResult handleStandaloneElementStart(
            final char[] buffer,
            final int offset, final int len,
            final boolean minimized, final int line, final int col)
            throws AttoParseException {
        
        this.currentElement = HtmlElements.forName(buffer, offset, len);
        return this.currentElement.handleStandaloneElementStart(buffer, offset, len, minimized, line, col, this.handler);

    }

    @Override
    public final IAttoHandleResult handleStandaloneElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final boolean minimized, final int line, final int col)
            throws AttoParseException {
        
        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IAttoHandleResult result =
            this.currentElement.handleStandaloneElementEnd(buffer, offset, len, minimized, line, col, this.handler);
        this.currentElement = null;

        return result;

    }


    
    
    @Override
    public final IAttoHandleResult handleOpenElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        
        this.currentElement = HtmlElements.forName(buffer, offset, len);
        return this.currentElement.handleOpenElementStart(buffer, offset, len, line, col, this.handler);

    }

    @Override
    public final IAttoHandleResult handleOpenElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IAttoHandleResult result =
            this.currentElement.handleOpenElementEnd(buffer, offset, len, line, col, this.handler);
        this.currentElement = null;

        return result;

    }

    
    
    
    @Override
    public final IAttoHandleResult handleCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        
        this.currentElement = HtmlElements.forName(buffer, offset, len);
        return this.currentElement.handleCloseElementStart(buffer, offset, len, line, col, this.handler);
        
    }

    @Override
    public final IAttoHandleResult handleCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IAttoHandleResult result =
            this.currentElement.handleCloseElementEnd(buffer, offset, len, line, col, this.handler);
        this.currentElement = null;

        return result;

    }

    
    
    
    @Override
    public final IAttoHandleResult handleAutoCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        
        this.currentElement = HtmlElements.forName(buffer, offset, len);
        return this.currentElement.handleAutoCloseElementStart(buffer, offset, len, line, col, this.handler);
        
    }

    @Override
    public final IAttoHandleResult handleAutoCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IAttoHandleResult result =
            this.currentElement.handleAutoCloseElementEnd(buffer, offset, len, line, col, this.handler);
        this.currentElement = null;

        return result;

    }

    
    
    
    @Override
    public final IAttoHandleResult handleUnmatchedCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        
        this.currentElement = HtmlElements.forName(buffer, offset, len);
        return this.currentElement.handleUnmatchedCloseElementStart(buffer, offset, len, line, col, this.handler);
        
    }

    @Override
    public final IAttoHandleResult handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IAttoHandleResult result =
            this.currentElement.handleUnmatchedCloseElementEnd(buffer, offset, len, line, col, this.handler);
        this.currentElement = null;

        return result;

    }
    
    
    
    
    @Override
    public final IAttoHandleResult handleAttribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, 
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot handle attribute: no current element");
        }
        
        return this.currentElement.handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                valueLine, valueCol, this.handler);
        
    }




    @Override
    public final IAttoHandleResult handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot handle attribute: no current element");
        }
        
        return this.currentElement.handleInnerWhiteSpace(buffer, offset, len, line, col, this.handler);
        
    }




    @Override
    public IAttoHandleResult handleProcessingInstruction(
            final char[] buffer,
            final int targetOffset, final int targetLen, final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen, final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {

        return this.handler.handleProcessingInstruction(
                buffer,
                targetOffset, targetLen, targetLine, targetCol,
                contentOffset, contentLen, contentLine, contentCol,
                outerOffset, outerLen,
                line, col);

    }


}