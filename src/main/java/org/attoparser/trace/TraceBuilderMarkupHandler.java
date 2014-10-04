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
package org.attoparser.trace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.attoparser.AbstractMarkupHandler;
import org.attoparser.ParseException;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.2
 *
 */
public final class TraceBuilderMarkupHandler extends AbstractMarkupHandler {

    
    private final List<MarkupTraceEvent> trace = new ArrayList<MarkupTraceEvent>(20);
    


    public TraceBuilderMarkupHandler() {
        super();
    }



    
    
    public List<MarkupTraceEvent> getTrace() {
        return Collections.unmodifiableList(this.trace);
    }
    


    
    @Override
    public void handleDocumentStart(final long startTimeNanos, final int line, final int col)
            throws ParseException {
        this.trace.add(new MarkupTraceEvent.DocumentStartTraceEvent(startTimeNanos, line, col));
    }

    
    
    @Override
    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws ParseException {
        this.trace.add(new MarkupTraceEvent.DocumentEndTraceEvent(endTimeNanos, totalTimeNanos, line, col));
    }



    @Override
    public void handleStandaloneElementStart(
            final char[] buffer,
            final int offset, final int len,
            final boolean minimized, final int line, final int col)
            throws ParseException {
        final String elementName = new String(buffer, offset, len);
        if (minimized) {
            this.trace.add(new MarkupTraceEvent.StandaloneElementStartTraceEvent(elementName, line, col));
        } else {
            this.trace.add(new MarkupTraceEvent.NonMinimizedStandaloneElementStartTraceEvent(elementName, line, col));
        }
    }



    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final boolean minimized, final int line, final int col)
            throws ParseException {
        final String elementName = new String(buffer, offset, len);
        if (minimized) {
            this.trace.add(new MarkupTraceEvent.StandaloneElementEndTraceEvent(elementName, line, col));
        } else {
            this.trace.add(new MarkupTraceEvent.NonMinimizedStandaloneElementEndTraceEvent(elementName, line, col));
        }
    }
    
    

    
    @Override
    public void handleOpenElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(new MarkupTraceEvent.OpenElementStartTraceEvent(elementName, line, col));
    }
    
    

    
    @Override
    public void handleOpenElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(new MarkupTraceEvent.OpenElementEndTraceEvent(elementName, line, col));
    }
    
    

    
    @Override
    public void handleCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(new MarkupTraceEvent.CloseElementStartTraceEvent(elementName, line, col));
    }
    
    

    
    @Override
    public void handleCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(new MarkupTraceEvent.CloseElementEndTraceEvent(elementName, line, col));
    }
    
    

    
    @Override
    public void handleAutoCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(new MarkupTraceEvent.AutoCloseElementStartTraceEvent(elementName, line, col));
    }
    
    

    
    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(new MarkupTraceEvent.AutoCloseElementEndTraceEvent(elementName, line, col));
    }
    
    

    
    @Override
    public void handleUnmatchedCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(new MarkupTraceEvent.UnmatchedCloseElementStartTraceEvent(elementName, line, col));
    }
    
    

    
    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(new MarkupTraceEvent.UnmatchedCloseElementEndTraceEvent(elementName, line, col));
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

        final String attributeName = new String(buffer, nameOffset, nameLen);
        final String operator = new String(buffer, operatorOffset, operatorLen);
        final String value = new String(buffer, valueOuterOffset, valueOuterLen);

        this.trace.add(new MarkupTraceEvent.AttributeTraceEvent(
                attributeName, nameLine, nameCol, operator, operatorLine, operatorCol, value, valueLine, valueCol));

    }

    
    
    @Override
    public void handleText(final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        final String content = new String(buffer, offset, len);
        this.trace.add(new MarkupTraceEvent.TextTraceEvent(content, line, col));
    }


    
    @Override
    public void handleComment(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws ParseException {
        final String content = new String(buffer, contentOffset, contentLen);
        this.trace.add(new MarkupTraceEvent.CommentTraceEvent(content, line, col));
    }

    
    @Override
    public void handleCDATASection(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {
        final String content = new String(buffer, contentOffset, contentLen);
        this.trace.add(new MarkupTraceEvent.CDATASectionTraceEvent(content, line, col));
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

        final String keyword = new String(buffer, keywordOffset, keywordLen);
        final String version = new String(buffer, versionOffset, versionLen);
        final String encoding =
                (encodingOffset > 0?
                        new String(buffer, encodingOffset, encodingLen) :
                        null);
        final String standalone =
                (standaloneOffset > 0?
                        new String(buffer, standaloneOffset, standaloneLen) :
                        null);

        this.trace.add(
                new MarkupTraceEvent.XmlDeclarationTraceEvent(
                        keyword, keywordLine, keywordCol,
                        version, versionLine, versionCol,
                        encoding, encodingLine, encodingCol,
                        standalone, standaloneLine, standaloneCol));

    }

    
    
    




    @Override
    public void handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws ParseException {
        final String content = new String(buffer, offset, len);
        this.trace.add(new MarkupTraceEvent.InnerWhiteSpaceTraceEvent(content, line, col));
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

        final String keyword = new String(buffer, keywordOffset, keywordLen);
        final String elementName = new String(buffer, elementNameOffset, elementNameLen);
        final String type = new String(buffer, typeOffset, typeLen);
        final String publicId = (publicIdOffset <= 0 ? null : new String(buffer, publicIdOffset, publicIdLen));
        final String systemId = (systemIdOffset <= 0 ? null : new String(buffer, systemIdOffset, systemIdLen));
        final String internalSubset = (internalSubsetOffset <= 0 ? null : new String(buffer, internalSubsetOffset, internalSubsetLen));

        this.trace.add(new MarkupTraceEvent.DocTypeTraceEvent(
                keyword, keywordLine, keywordCol,
                elementName, elementNameLine, elementNameCol,
                type, typeLine, typeCol,
                publicId, publicIdLine, publicIdCol,
                systemId, systemIdLine, systemIdCol,
                internalSubset, internalSubsetLine, internalSubsetCol));

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

        this.trace.add(
                new MarkupTraceEvent.ProcessingInstructionTraceEvent(
                        target, targetLine, targetCol,
                        content, contentLine, contentCol));

    }

    
    
}