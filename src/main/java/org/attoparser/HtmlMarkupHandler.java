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
package org.attoparser;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
final class HtmlMarkupHandler extends AbstractMarkupHandler {


    private final IMarkupHandler handler;
    private ParseStatus status = null; // Will be always set, but anyway we should initialize.

    private IHtmlElement currentElement = null;




    public HtmlMarkupHandler(final IMarkupHandler handler) {
        super();
        if (handler == null) {
            throw new IllegalArgumentException("Delegate handler cannot be null");
        }
        this.handler = handler;
    }



    @Override
    public void setParserStatus(final ParseStatus status) {
        // This will be ALWAYS called, so there is no need to actually check whether this property is null when using it
        this.status = status;
        this.handler.setParserStatus(status);
    }




    @Override
    public void handleDocumentStart(
            final long startTimeNanos, final int line, final int col)
            throws ParseException {

        this.handler.handleDocumentStart(startTimeNanos, line, col);

    }



    @Override
    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws ParseException {

        this.handler.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);

    }



    @Override
    public void handleXmlDeclaration(
            final char[] buffer,
            final int keywordOffset, final int keywordLen, final int keywordLine, final int keywordCol,
            final int versionOffset, final int versionLen, final int versionLine, final int versionCol,
            final int encodingOffset, final int encodingLen, final int encodingLine, final int encodingCol,
            final int standaloneOffset, final int standaloneLen, final int standaloneLine, final int standaloneCol,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        this.handler.handleXmlDeclaration(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol,
                encodingOffset, encodingLen, encodingLine, encodingCol,
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen,
                line, col);

    }



    @Override
    public void handleDocType(
            final char[] buffer,
            final int keywordOffset, final int keywordLen, final int keywordLine, final int keywordCol,
            final int elementNameOffset, final int elementNameLen, final int elementNameLine, final int elementNameCol,
            final int typeOffset, final int typeLen, final int typeLine, final int typeCol,
            final int publicIdOffset, final int publicIdLen, final int publicIdLine, final int publicIdCol,
            final int systemIdOffset, final int systemIdLen, final int systemIdLine, final int systemIdCol,
            final int internalSubsetOffset, final int internalSubsetLen, final int internalSubsetLine, final int internalSubsetCol,
            final int outerOffset, final int outerLen,
            final int outerLine, final int outerCol)
            throws ParseException {

        this.handler.handleDocType(
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
    public void handleCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        this.handler.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);

    }



    @Override
    public void handleComment(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        this.handler.handleComment(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);

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
            final int offset, final int len,
            final boolean minimized, final int line, final int col)
            throws ParseException {
        
        this.currentElement = HtmlElements.forName(buffer, offset, len);
        this.currentElement.handleStandaloneElementStart(buffer, offset, len, minimized, line, col, this.handler, this.status);

    }

    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final boolean minimized, final int line, final int col)
            throws ParseException {
        
        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IHtmlElement element = this.currentElement;
        this.currentElement = null;

        // Hoping for better days in which tail calls might be optimized ;)
        element.handleStandaloneElementEnd(buffer, offset, len, minimized, line, col, this.handler, this.status);

    }


    
    
    @Override
    public void handleOpenElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws ParseException {
        
        this.currentElement = HtmlElements.forName(buffer, offset, len);
        this.currentElement.handleOpenElementStart(buffer, offset, len, line, col, this.handler, this.status);

    }

    @Override
    public void handleOpenElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IHtmlElement element = this.currentElement;
        this.currentElement = null;

        // Hoping for better days in which tail calls might be optimized ;)
        element.handleOpenElementEnd(buffer, offset, len, line, col, this.handler, this.status);

    }

    
    
    
    @Override
    public void handleCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws ParseException {
        
        this.currentElement = HtmlElements.forName(buffer, offset, len);
        this.currentElement.handleCloseElementStart(buffer, offset, len, line, col, this.handler, this.status);
        
    }

    @Override
    public void handleCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IHtmlElement element = this.currentElement;
        this.currentElement = null;

        // Hoping for better days in which tail calls might be optimized ;)
        element.handleCloseElementEnd(buffer, offset, len, line, col, this.handler, this.status);

    }

    
    
    
    @Override
    public void handleAutoCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws ParseException {
        
        this.currentElement = HtmlElements.forName(buffer, offset, len);
        this.currentElement.handleAutoCloseElementStart(buffer, offset, len, line, col, this.handler, this.status);
        
    }

    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IHtmlElement element = this.currentElement;
        this.currentElement = null;

        // Hoping for better days in which tail calls might be optimized ;)
        element.handleAutoCloseElementEnd(buffer, offset, len, line, col, this.handler, this.status);

    }

    
    
    
    @Override
    public void handleUnmatchedCloseElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws ParseException {
        
        this.currentElement = HtmlElements.forName(buffer, offset, len);
        this.currentElement.handleUnmatchedCloseElementStart(buffer, offset, len, line, col, this.handler, this.status);
        
    }

    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final IHtmlElement element = this.currentElement;
        this.currentElement = null;

        // Hoping for better days in which tail calls might be optimized ;)
        element.handleUnmatchedCloseElementEnd(buffer, offset, len, line, col, this.handler, this.status);

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

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot handle attribute: no current element");
        }
        
        this.currentElement.handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                valueLine, valueCol, this.handler, this.status);
        
    }




    @Override
    public void handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot handle attribute: no current element");
        }
        
        this.currentElement.handleInnerWhiteSpace(buffer, offset, len, line, col, this.handler, this.status);
        
    }




    @Override
    public void handleProcessingInstruction(
            final char[] buffer,
            final int targetOffset, final int targetLen, final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen, final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        this.handler.handleProcessingInstruction(
                buffer,
                targetOffset, targetLen, targetLine, targetCol,
                contentOffset, contentLen, contentLine, contentCol,
                outerOffset, outerLen,
                line, col);

    }


}