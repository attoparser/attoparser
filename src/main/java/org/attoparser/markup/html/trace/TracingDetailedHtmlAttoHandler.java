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

import java.io.Writer;

import org.attoparser.AttoParseException;
import org.attoparser.markup.html.AbstractDetailedHtmlAttoHandler;
import org.attoparser.markup.html.HtmlParsingConfiguration;







/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class TracingDetailedHtmlAttoHandler extends AbstractDetailedHtmlAttoHandler {

    
    private final Writer writer;
    
    
    public TracingDetailedHtmlAttoHandler(final Writer writer) {
        super(new HtmlParsingConfiguration());
        this.writer = writer;
    }

    
    public TracingDetailedHtmlAttoHandler(final Writer writer, final HtmlParsingConfiguration configuration) {
        super(configuration);
        this.writer = writer;
    }
    
    
    
    
    
    
    @Override
    public void handleDocumentStart(final long startTimeNanos, 
            final int line, final int col,
            final HtmlParsingConfiguration configuration)
            throws AttoParseException {
        try {
            this.writer.write('[');
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
    }

    
    
    @Override
    public void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos, 
            final int line, final int col, 
            final HtmlParsingConfiguration configuration)
            throws AttoParseException {
        try {
            this.writer.write(']');
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
    }
    
    

    
    @Override
    public void handleHtmlClosedStandaloneElementStart(char[] buffer,
            int offset, int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlClosedStandaloneElementStart(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlClosedStandaloneElementName(char[] buffer,
            int offset, int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlClosedStandaloneElementName(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlClosedStandaloneElementEnd(char[] buffer, int offset,
            int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlClosedStandaloneElementEnd(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlUnclosedStandaloneElementStart(char[] buffer,
            int offset, int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlUnclosedStandaloneElementStart(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlUnclosedStandaloneElementName(char[] buffer,
            int offset, int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlUnclosedStandaloneElementName(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlUnclosedStandaloneElementEnd(char[] buffer,
            int offset, int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlUnclosedStandaloneElementEnd(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlOpenElementStart(char[] buffer, int offset, int len,
            int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlOpenElementStart(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlOpenElementName(char[] buffer, int offset, int len,
            int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlOpenElementName(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlOpenElementEnd(char[] buffer, int offset, int len,
            int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlOpenElementEnd(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlCloseElementStart(char[] buffer, int offset, int len,
            int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlCloseElementStart(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlCloseElementName(char[] buffer, int offset, int len,
            int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlCloseElementName(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlCloseElementEnd(char[] buffer, int offset, int len,
            int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlCloseElementEnd(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlForcedCloseElementStart(char[] buffer, int offset,
            int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlForcedCloseElementStart(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlForcedCloseElementName(char[] buffer, int offset,
            int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlForcedCloseElementName(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlForcedCloseElementEnd(char[] buffer, int offset,
            int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlForcedCloseElementEnd(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlUnmatchedCloseElementStart(char[] buffer, int offset,
            int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlUnmatchedCloseElementStart(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlUnmatchedCloseElementName(char[] buffer, int offset,
            int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlUnmatchedCloseElementName(buffer, offset, len, line, col);
    }

    @Override
    public void handleHtmlUnmatchedCloseElementEnd(char[] buffer, int offset,
            int len, int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.handleHtmlUnmatchedCloseElementEnd(buffer, offset, len, line, col);
    }


    
    
}