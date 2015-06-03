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
package org.attoparser.duplicate;

import org.attoparser.AbstractMarkupHandler;
import org.attoparser.IMarkupHandler;
import org.attoparser.ParseException;
import org.attoparser.ParseStatus;
import org.attoparser.config.ParseConfiguration;
import org.attoparser.select.ParseSelection;


/**
 * <p>
 *   Implementation of {@link org.attoparser.IMarkupHandler} used for duplicating events, sending them to two
 *   different handlers.
 * </p>
 * <p>
 *   Note that, as with most handlers, this class is <strong>not thread-safe</strong>. Also, instances of this class
 *   should not be reused across parsing operations.
 * </p>
 * <p>
 *   Sample usage:
 * </p>
 * <pre><code>
 *
 *   final Writer writer1 = new StringWriter();
 *   final IMarkupHandler handler1 = new OutputMarkupHandler(writer1);
 *
 *   final Writer writer2 = new StringWriter();
 *   final IMarkupHandler handler2 = new PrettyHtmlMarkupHandler(writer2);
 *
 *   final IMarkupHandler handler = new DuplicateMarkupHandler(handler1, handler2);
 *
 *   parser.parse(document, handler);
 *
 *   return writer.toString();
 *
 * </code></pre>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public final class DuplicateMarkupHandler extends AbstractMarkupHandler {


    private final IMarkupHandler handler1;
    private final IMarkupHandler handler2;


    /**
     * <p>
     *   Creates a new instance of this handler, specifying the two delegate handlers to which events
     *   will be redirected.
     * </p>
     * @param handler1 the first delegate handler.
     * @param handler2 the second delegate handler.
     */
    public DuplicateMarkupHandler(final IMarkupHandler handler1, final IMarkupHandler handler2) {
        super();
        if (handler1 == null) {
            throw new IllegalArgumentException("Handler 1 cannot be null");
        }
        if (handler2 == null) {
            throw new IllegalArgumentException("Handler 2 cannot be null");
        }
        this.handler1 = handler1;
        this.handler2 = handler2;
    }




    @Override
    public void setParseConfiguration(final ParseConfiguration parseConfiguration) {
        this.handler1.setParseConfiguration(parseConfiguration);
        this.handler2.setParseConfiguration(parseConfiguration);
    }


    @Override
    public void setParseStatus(final ParseStatus status) {
        this.handler1.setParseStatus(status);
        this.handler2.setParseStatus(status);
    }


    @Override
    public void setParseSelection(final ParseSelection selection) {
        this.handler1.setParseSelection(selection);
        this.handler2.setParseSelection(selection);
    }





    @Override
    public void handleDocumentStart(
            final long startTimeNanos, final int line, final int col)
            throws ParseException {

        this.handler1.handleDocumentStart(startTimeNanos, line, col);
        this.handler2.handleDocumentStart(startTimeNanos, line, col);

    }




    @Override
    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws ParseException {

        this.handler1.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);
        this.handler2.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);

    }





    @Override
    public void handleText(final char[] buffer, final int offset, final int len, final int line, final int col)
            throws ParseException {
        
        this.handler1.handleText(buffer, offset, len, line, col);
        this.handler2.handleText(buffer, offset, len, line, col);

    }




    @Override
    public void handleComment(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws ParseException {

        this.handler1.handleComment(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        this.handler2.handleComment(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);

    }



    
    @Override
    public void handleCDATASection(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        this.handler1.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        this.handler2.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);

    }




    @Override
    public void handleStandaloneElementStart(
            final char[] buffer, final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col) throws ParseException {

        this.handler1.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);
        this.handler2.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);

    }




    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer, final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col) throws ParseException {

        this.handler1.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);
        this.handler2.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);

    }




    @Override
    public void handleOpenElementStart(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col) throws ParseException {

        this.handler1.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);
        this.handler2.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleOpenElementEnd(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col) throws ParseException {

        this.handler1.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);
        this.handler2.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleAutoOpenElementStart(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col) throws ParseException {

        this.handler1.handleAutoOpenElementStart(buffer, nameOffset, nameLen, line, col);
        this.handler2.handleAutoOpenElementStart(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleAutoOpenElementEnd(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col) throws ParseException {

        this.handler1.handleAutoOpenElementEnd(buffer, nameOffset, nameLen, line, col);
        this.handler2.handleAutoOpenElementEnd(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleCloseElementStart(final char[] buffer, final int nameOffset, final int nameLen, final int line,
            final int col) throws ParseException {

        this.handler1.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);
        this.handler2.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleCloseElementEnd(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col) throws ParseException {

        this.handler1.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        this.handler2.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleAutoCloseElementStart(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.handler1.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);
        this.handler2.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }





    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.handler1.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        this.handler2.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleUnmatchedCloseElementStart(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.handler1.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);
        this.handler2.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer, final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.handler1.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        this.handler2.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);

    }




    @Override
    public void handleAttribute(final char[] buffer, final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, final int valueContentOffset,
            final int valueContentLen, final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol) throws ParseException {

        this.handler1.handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol, operatorOffset,
                operatorLen, operatorLine, operatorCol, valueContentOffset, valueContentLen,
                valueOuterOffset, valueOuterLen, valueLine, valueCol);
        this.handler2.handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol, operatorOffset,
                operatorLen, operatorLine, operatorCol, valueContentOffset, valueContentLen,
                valueOuterOffset, valueOuterLen, valueLine, valueCol);

    }




    @Override
    public void handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws ParseException {

        this.handler1.handleInnerWhiteSpace(buffer, offset, len, line, col);
        this.handler2.handleInnerWhiteSpace(buffer, offset, len, line, col);

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
            final int outerLine, final int outerCol) throws ParseException {

        this.handler1.handleDocType(buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol, typeOffset, typeLen,
                typeLine, typeCol, publicIdOffset, publicIdLen, publicIdLine, publicIdCol, systemIdOffset,
                systemIdLen, systemIdLine, systemIdCol, internalSubsetOffset, internalSubsetLen,
                internalSubsetLine, internalSubsetCol, outerOffset, outerLen, outerLine, outerCol);
        this.handler2.handleDocType(buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol, typeOffset, typeLen,
                typeLine, typeCol, publicIdOffset, publicIdLen, publicIdLine, publicIdCol, systemIdOffset,
                systemIdLen, systemIdLine, systemIdCol, internalSubsetOffset, internalSubsetLen,
                internalSubsetLine, internalSubsetCol, outerOffset, outerLen, outerLine, outerCol);

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
            final int line,final int col) 
            throws ParseException {

        this.handler1.handleXmlDeclaration(buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol, encodingOffset, encodingLen,
                encodingLine, encodingCol, standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen, line, col);
        this.handler2.handleXmlDeclaration(buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol, encodingOffset, encodingLen,
                encodingLine, encodingCol, standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen, line, col);

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

        this.handler1.handleProcessingInstruction(buffer, targetOffset, targetLen, targetLine, targetCol,
                contentOffset, contentLen, contentLine, contentCol, outerOffset, outerLen, line, col);
        this.handler2.handleProcessingInstruction(buffer, targetOffset, targetLen, targetLine, targetCol,
                contentOffset, contentLen, contentLine, contentCol, outerOffset, outerLen, line, col);

    }


    
    
}