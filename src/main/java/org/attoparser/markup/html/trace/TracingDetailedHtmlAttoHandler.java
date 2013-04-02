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
package org.attoparser.markup.html.trace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.attoparser.AttoParseException;
import org.attoparser.markup.html.AbstractDetailedNonValidatingHtmlAttoHandler;
import org.attoparser.markup.html.HtmlParsingConfiguration;
import org.attoparser.markup.html.elements.IHtmlElement;
import org.attoparser.markup.trace.TraceEvent;







/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.2
 *
 */
public class TracingDetailedHtmlAttoHandler extends AbstractDetailedNonValidatingHtmlAttoHandler {

    
    public static final String TRACE_TYPE_DOCUMENT_START = "DS";
    public static final String TRACE_TYPE_DOCUMENT_END = "DE";

    public static final String TRACE_TYPE_STANDALONE_ELEMENT_START = "SES";
    public static final String TRACE_TYPE_STANDALONE_ELEMENT_END = "SEE";

    public static final String TRACE_TYPE_OPEN_ELEMENT_START = "OES";
    public static final String TRACE_TYPE_OPEN_ELEMENT_END = "OEE";

    public static final String TRACE_TYPE_CLOSE_ELEMENT_START = "CES";
    public static final String TRACE_TYPE_CLOSE_ELEMENT_END = "CEE";

    public static final String TRACE_TYPE_ATTRIBUTE = "A";
    public static final String TRACE_TYPE_TEXT = "T";
    public static final String TRACE_TYPE_COMMENT = "C";
    public static final String TRACE_TYPE_CDATA = "D";
    public static final String TRACE_TYPE_XMLDECL = "X";
    public static final String TRACE_TYPE_INNERWHITESPACE = "IW";
    public static final String TRACE_TYPE_DOCTYPE = "DT";
    public static final String TRACE_TYPE_PROCESSINGINSTRUCTION = "P";
    

    
    private final List<TraceEvent> trace = new ArrayList<TraceEvent>();
    

    
    
    public TracingDetailedHtmlAttoHandler() {
        super(new HtmlParsingConfiguration());
    }

    
    public TracingDetailedHtmlAttoHandler(final HtmlParsingConfiguration configuration) {
        super(configuration);
    }
    
    
    
    
    
    
    public List<TraceEvent> getTrace() {
        return Collections.unmodifiableList(this.trace);
    }
    
    
    
    
    
    @Override
    public void handleDocumentStart(final long startTimeNanos, 
            final int line, final int col,
            final HtmlParsingConfiguration configuration)
            throws AttoParseException {
        this.trace.add(new TraceEvent(line, col, TRACE_TYPE_DOCUMENT_START));
    }

    
    
    @Override
    public void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos, 
            final int line, final int col, 
            final HtmlParsingConfiguration configuration)
            throws AttoParseException {
        this.trace.add(new TraceEvent(line, col, TRACE_TYPE_DOCUMENT_END));
    }
    
    

    
    @Override
    public void handleHtmlStandaloneElementStart(
            final IHtmlElement element,
            final boolean minimized,
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        this.trace.add(
                new TraceEvent(
                        line, col, TRACE_TYPE_STANDALONE_ELEMENT_START, 
                        element.getName(), Boolean.toString(minimized), new String(buffer, offset, len)));
    }

    
    @Override
    public void handleHtmlStandaloneElementEnd(
            final IHtmlElement element,
            final boolean minimized,
            final int line, final int col)
            throws AttoParseException {
        this.trace.add(
                new TraceEvent(
                        line, col, TRACE_TYPE_STANDALONE_ELEMENT_END, 
                        element.getName(), Boolean.toString(minimized)));
    }

    
    
    
    @Override
    public void handleHtmlOpenElementStart(
            final IHtmlElement element,
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        this.trace.add(
                new TraceEvent(
                        line, col, TRACE_TYPE_OPEN_ELEMENT_START, 
                        element.getName(), new String(buffer, offset, len)));
    }

    
    @Override
    public void handleHtmlOpenElementEnd(
            final IHtmlElement element,
            final int line, final int col) 
            throws AttoParseException {
        this.trace.add(new TraceEvent(line, col, TRACE_TYPE_OPEN_ELEMENT_END, element.getName()));
    }

    
    
    
    @Override
    public void handleHtmlCloseElementStart(
            final IHtmlElement element,
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        this.trace.add(
                new TraceEvent(
                        line, col, TRACE_TYPE_CLOSE_ELEMENT_START,
                        element.getName(), new String(buffer, offset, len)));
    }

    
    @Override
    public void handleHtmlCloseElementEnd(
            final IHtmlElement element,
            final int line, final int col) 
            throws AttoParseException {
        this.trace.add(new TraceEvent(line, col, TRACE_TYPE_CLOSE_ELEMENT_END, element.getName()));
    }




    @Override
    public void handleHtmlAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {
        this.trace.add(
                new TraceEvent(
                        nameLine, nameCol, 
                        TRACE_TYPE_ATTRIBUTE, 
                        new String(buffer, nameOffset, nameLen),
                        new String(buffer, operatorOffset, operatorLen),
                        new String(buffer, valueOuterOffset, valueOuterLen)));
    }


    @Override
    public void handleHtmlInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        this.trace.add(new TraceEvent(line, col, TRACE_TYPE_INNERWHITESPACE, new String(buffer, offset, len)));
    }


    

    
    
    @Override
    public void handleText(final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        this.trace.add(new TraceEvent(line, col, TRACE_TYPE_TEXT, new String(buffer, offset, len)));
    }


    
    @Override
    public void handleComment(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        this.trace.add(
                new TraceEvent(line, col, TRACE_TYPE_COMMENT, new String(buffer, contentOffset, contentLen)));
    }

    
    @Override
    public void handleCDATASection(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {
        this.trace.add(
                new TraceEvent(line, col, TRACE_TYPE_CDATA, new String(buffer, contentOffset, contentLen)));
    }

    
    
    
    @Override
    public void handleXmlDeclarationDetail(
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
        this.trace.add(
                new TraceEvent(
                        line, col, 
                        TRACE_TYPE_XMLDECL, 
                        new String(buffer, keywordOffset, keywordLen),
                        new String(buffer, versionOffset, versionLen),
                        new String(buffer, encodingOffset, encodingLen),
                        new String(buffer, standaloneOffset, standaloneLen)));
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
            throws AttoParseException {
        this.trace.add(
                new TraceEvent(
                        outerLine, outerCol, 
                        TRACE_TYPE_DOCTYPE, 
                        new String(buffer, keywordOffset, keywordLen),
                        new String(buffer, elementNameOffset, elementNameLen),
                        new String(buffer, typeOffset, typeLen),
                        new String(buffer, publicIdOffset, publicIdLen),
                        new String(buffer, systemIdOffset, systemIdLen),
                        new String(buffer, internalSubsetOffset, internalSubsetLen)));
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
            throws AttoParseException {
        this.trace.add(
                new TraceEvent(
                        line, col, 
                        TRACE_TYPE_PROCESSINGINSTRUCTION, 
                        new String(buffer, targetOffset, targetLen),
                        new String(buffer, contentOffset, contentLen)));
    }

    
    
    

    
    
}