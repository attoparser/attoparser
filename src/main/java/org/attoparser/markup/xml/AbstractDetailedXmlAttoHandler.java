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
package org.attoparser.markup.xml;

import org.attoparser.AttoParseException;
import org.attoparser.markup.AbstractDetailedMarkupAttoHandler;
import org.attoparser.markup.MarkupParsingConfiguration;






/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public abstract class AbstractDetailedXmlAttoHandler 
        extends AbstractDetailedMarkupAttoHandler
        implements IDetailedXmlElementHandling {


    
    
    public AbstractDetailedXmlAttoHandler() {
        super(XmlParsing.xmlParsingConfiguration());
    }
    
    

    
    @Override
    public final void handleDocumentStart(final long startTimeNanos, 
            final int line, final int col, final MarkupParsingConfiguration documentRestrictions)
            throws AttoParseException {
        super.handleDocumentStart(startTimeNanos, line, col, documentRestrictions);
        handleXmlDocumentStart(startTimeNanos, line, col);
    }


    @Override
    public final void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos,
            final int line, final int col, final MarkupParsingConfiguration documentRestrictions)
            throws AttoParseException {
        super.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col, documentRestrictions);
        handleXmlDocumentEnd(endTimeNanos, totalTimeNanos, line, col);
    }




    @Override
    public final void handleStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws AttoParseException {
        super.handleStandaloneElementStart(buffer, nameOffset, nameLen, line, col);
        handleXmlStandaloneElementStart(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final void handleStandaloneElementEnd(
            final int line, final int col)
            throws AttoParseException {
        super.handleStandaloneElementEnd(line, col);
        handleXmlStandaloneElementEnd(line, col);
    }


    @Override
    public final void handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        super.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);
        handleXmlOpenElementStart(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final void handleOpenElementEnd(
            final int line, final int col)
            throws AttoParseException {
        super.handleOpenElementEnd(line, col);
        handleXmlOpenElementEnd(line, col);
    }


    @Override
    public final void handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        super.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);
        handleXmlCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final void handleCloseElementEnd(
            final int line, final int col)
            throws AttoParseException {
        super.handleCloseElementEnd(line, col);
        handleXmlCloseElementEnd(line, col);
    }


    @Override
    public final void handleAutoCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        super.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);
        // Auto closing is disabled (markup has to be completely balanced).
        throw new AttoParseException(
                "Malformed markup: Element \"" + new String(buffer, nameOffset, nameLen) + "\" " +
                "is never closed",
                line, col);
    }

    
    @Override
    public final void handleAutoCloseElementEnd(
            final int line, final int col)
            throws AttoParseException {
        super.handleAutoCloseElementEnd(line, col);
        // Auto closing is disabled (markup has to be completely balanced).
    }








    
    

    
    @SuppressWarnings("unused")
    public void handleXmlDocumentStart(final long startTimeNanos, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    @SuppressWarnings("unused")
    public void handleXmlDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlStandaloneElementEnd(
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlOpenElementEnd(
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlCloseElementEnd(
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    
    
}