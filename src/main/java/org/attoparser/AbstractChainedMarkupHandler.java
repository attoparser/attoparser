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
package org.attoparser;


import org.attoparser.config.ParseConfiguration;

/**
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public abstract class AbstractChainedMarkupHandler
            extends AbstractMarkupHandler {


    private final IMarkupHandler next;


    protected AbstractChainedMarkupHandler(final IMarkupHandler next) {
        super();
        if (next == null) {
            throw new IllegalArgumentException("Next handler cannot be null");
        }
        this.next = next;
    }


    protected IMarkupHandler getNext() {
        return this.next;
    }




    public void setParseConfiguration(final ParseConfiguration parseConfiguration) {
        this.next.setParseConfiguration(parseConfiguration);
    }



    public void setParseStatus(final ParseStatus status) {
        this.next.setParseStatus(status);
    }



    public void handleDocumentStart(
            final long startTimeNanos, final int line, final int col)
            throws ParseException {
        this.next.handleDocumentStart(startTimeNanos, line, col);
    }


    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws ParseException {
        this.next.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);
    }



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
        this.next.handleXmlDeclaration(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol,
                encodingOffset, encodingLen, encodingLine, encodingCol,
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen, line, col);
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
            throws ParseException {
        this.next.handleDocType(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                typeOffset, typeLen, typeLine, typeCol,
                publicIdOffset, publicIdLen, publicIdLine, publicIdCol,
                systemIdOffset, systemIdLen, systemIdLine, systemIdCol,
                internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol,
                outerOffset, outerLen, outerLine, outerCol);
    }



    public void handleCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {
        this.next.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
    }



    public void handleComment(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {
        this.next.handleComment(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
    }



    public void handleText(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        this.next.handleText(buffer, offset, len, line, col);
    }


    public void handleStandaloneElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col)
            throws ParseException {
        this.next.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);
    }

    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col)
            throws ParseException {
        this.next.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);
    }

    

    public void handleOpenElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws ParseException {
        this.next.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);
    }

    public void handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {
        this.next.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);
    }


    
    public void handleCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws ParseException {
        this.next.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }

    public void handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {
        this.next.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);
    }


    
    public void handleAutoCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws ParseException {
        this.next.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }

    public void handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {
        this.next.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);
    }
    

    
    public void handleUnmatchedCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws ParseException {
        this.next.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }


    public void handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {
        this.next.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
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
            throws ParseException {
        this.next.handleAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen,
                valueOuterOffset, valueOuterLen, valueLine, valueCol);
    }


    
    public void handleInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        this.next.handleInnerWhiteSpace(buffer, offset, len, line, col);
    }



    public void handleProcessingInstruction(
            final char[] buffer,
            final int targetOffset, final int targetLen,
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {
        this.next.handleProcessingInstruction(
                buffer,
                targetOffset, targetLen, targetLine, targetCol,
                contentOffset, contentLen, contentLine, contentCol,
                outerOffset, outerLen, line, col);
    }


}