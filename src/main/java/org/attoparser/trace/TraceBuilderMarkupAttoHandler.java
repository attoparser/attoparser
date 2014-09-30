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
package org.attoparser.trace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.AbstractMarkupAttoHandler;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.2
 *
 */
public final class TraceBuilderMarkupAttoHandler extends AbstractMarkupAttoHandler {

    
    private final List<MarkupParsingTraceEvent> trace = new ArrayList<MarkupParsingTraceEvent>(20);
    


    public TraceBuilderMarkupAttoHandler() {
        super();
    }



    
    
    public List<MarkupParsingTraceEvent> getTrace() {
        return Collections.unmodifiableList(this.trace);
    }
    


    
    @Override
    public IAttoHandleResult handleDocumentStart(final long startTimeNanos, final int line, final int col)
            throws AttoParseException {
        this.trace.add(MarkupParsingTraceEvent.forDocumentStart(startTimeNanos, line, col));
        return null;
    }

    
    
    @Override
    public IAttoHandleResult handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws AttoParseException {
        this.trace.add(MarkupParsingTraceEvent.forDocumentEnd(endTimeNanos, totalTimeNanos, line, col));
        return null;
    }



    @Override
    public IAttoHandleResult handleStandaloneElementStart(
            final char[] buffer,
            final int offset, final int len,
            final boolean minimized, final int line, final int col)
            throws AttoParseException {
        final String elementName = new String(buffer, offset, len);
        if (minimized) {
            this.trace.add(MarkupParsingTraceEvent.forStandaloneElementStart(elementName, line, col));
        } else {
            this.trace.add(MarkupParsingTraceEvent.forNonMinimizedStandaloneElementStart(elementName, line, col));
        }
        return null;
    }



    @Override
    public IAttoHandleResult handleStandaloneElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final boolean minimized, final int line, final int col)
            throws AttoParseException {
        final String elementName = new String(buffer, offset, len);
        if (minimized) {
            this.trace.add(MarkupParsingTraceEvent.forStandaloneElementEnd(elementName, line, col));
        } else {
            this.trace.add(MarkupParsingTraceEvent.forNonMinimizedStandaloneElementEnd(elementName, line, col));
        }
        return null;
    }
    
    

    
    @Override
    public IAttoHandleResult handleOpenElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(MarkupParsingTraceEvent.forOpenElementStart(elementName, line, col));
        return null;
    }
    
    

    
    @Override
    public IAttoHandleResult handleOpenElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(MarkupParsingTraceEvent.forOpenElementEnd(elementName, line, col));
        return null;
    }
    
    

    
    @Override
    public IAttoHandleResult handleCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(MarkupParsingTraceEvent.forCloseElementStart(elementName, line, col));
        return null;
    }
    
    

    
    @Override
    public IAttoHandleResult handleCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(MarkupParsingTraceEvent.forCloseElementEnd(elementName, line, col));
        return null;
    }
    
    

    
    @Override
    public IAttoHandleResult handleAutoCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(MarkupParsingTraceEvent.forAutoCloseElementStart(elementName, line, col));
        return null;
    }
    
    

    
    @Override
    public IAttoHandleResult handleAutoCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(MarkupParsingTraceEvent.forAutoCloseElementEnd(elementName, line, col));
        return null;
    }
    
    

    
    @Override
    public IAttoHandleResult handleUnmatchedCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(MarkupParsingTraceEvent.forUnmatchedCloseElementStart(elementName, line, col));
        return null;
    }
    
    

    
    @Override
    public IAttoHandleResult handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        final String elementName = new String(buffer, offset, len);
        this.trace.add(MarkupParsingTraceEvent.forUnmatchedCloseElementEnd(elementName, line, col));
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
        final String operator = new String(buffer, operatorOffset, operatorLen);
        final String value = new String(buffer, valueOuterOffset, valueOuterLen);

        this.trace.add(MarkupParsingTraceEvent.forAttribute(
                attributeName, nameLine, nameCol, operator, operatorLine, operatorCol, value, valueLine, valueCol));
        return null;

    }

    
    
    @Override
    public IAttoHandleResult handleText(final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        final String content = new String(buffer, offset, len);
        this.trace.add(MarkupParsingTraceEvent.forText(content, line, col));
        return null;
    }


    
    @Override
    public IAttoHandleResult handleComment(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        final String content = new String(buffer, contentOffset, contentLen);
        this.trace.add(MarkupParsingTraceEvent.forComment(content, line, col));
        return null;
    }

    
    @Override
    public IAttoHandleResult handleCDATASection(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {
        final String content = new String(buffer, contentOffset, contentLen);
        this.trace.add(MarkupParsingTraceEvent.forCDATASection(content, line, col));
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
            final int line,final int col) 
            throws AttoParseException {

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
                MarkupParsingTraceEvent.forXmlDeclaration(
                        keyword, keywordLine, keywordCol,
                        version, versionLine, versionCol,
                        encoding, encodingLine, encodingCol,
                        standalone, standaloneLine, standaloneCol));
        return null;

    }

    
    
    




    @Override
    public IAttoHandleResult handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        final String content = new String(buffer, offset, len);
        this.trace.add(MarkupParsingTraceEvent.forInnerWhiteSpace(content, line, col));
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

        final String keyword = new String(buffer, keywordOffset, keywordLen);
        final String elementName = new String(buffer, elementNameOffset, elementNameLen);
        final String type = new String(buffer, typeOffset, typeLen);
        final String publicId = (publicIdOffset <= 0 ? null : new String(buffer, publicIdOffset, publicIdLen));
        final String systemId = (systemIdOffset <= 0 ? null : new String(buffer, systemIdOffset, systemIdLen));
        final String internalSubset = (internalSubsetOffset <= 0 ? null : new String(buffer, internalSubsetOffset, internalSubsetLen));

        this.trace.add(MarkupParsingTraceEvent.forDocType(
                keyword, keywordLine, keywordCol,
                elementName, elementNameLine, elementNameCol,
                type, typeLine, typeCol,
                publicId, publicIdLine, publicIdCol,
                systemId, systemIdLine, systemIdCol,
                internalSubset, internalSubsetLine, internalSubsetCol));
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

        this.trace.add(
                MarkupParsingTraceEvent.forProcessingInstruction(
                        target, targetLine, targetCol,
                        content, contentLine, contentCol));
        return null;

    }

    
    
}