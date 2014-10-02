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
package org.attoparser.xml;

import org.attoparser.AbstractMarkupAttoHandler;
import org.attoparser.AttoParseException;
import org.attoparser.IMarkupAttoHandler;
import org.attoparser.MarkupParsingController;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class XmlMarkupAttoHandler extends AbstractMarkupAttoHandler {



    private final IMarkupAttoHandler handler;



    public XmlMarkupAttoHandler(final IMarkupAttoHandler handler) {
        super();
        if (handler == null) {
            throw new IllegalArgumentException("Delegate handler cannot be null");
        }
        this.handler = handler;
    }



    @Override
    public void setMarkupParsingController(final MarkupParsingController parsingController) {
        this.handler.setMarkupParsingController(parsingController);
    }




    @Override
    public void handleStandaloneElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col)
            throws AttoParseException {
        this.handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);
    }


    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col)
            throws AttoParseException {
        this.handler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);
    }


    @Override
    public void handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        this.handler.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public void handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        this.handler.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public void handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        this.handler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public void handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        this.handler.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public void handleAutoCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Auto closing elements should not be happening in well-formed XML, that that's up to the handler to decide
        // by providing an adequate MarkupParsingConfiguration instance during construction.
        this.handler.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }

    
    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Auto closing elements should not be happening in well-formed XML, that that's up to the handler to decide
        // by providing an adequate MarkupParsingConfiguration instance during construction.
        this.handler.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public void handleUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Parsing of unmatched close elements should not be happening in well-formed XML, that that's up to the handler to decide
        // by providing an adequate MarkupParsingConfiguration instance during construction.
        this.handler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }

    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Parsing of unmatched close elements should not be happening in well-formed XML, that that's up to the handler to decide
        // by providing an adequate MarkupParsingConfiguration instance during construction.
        this.handler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public void handleAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen, final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen, final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {
        this.handler.handleAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                valueLine, valueCol);
    }

    @Override
    public void handleInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        this.handler.handleInnerWhiteSpace(buffer, offset, len, line, col);
    }

    
}