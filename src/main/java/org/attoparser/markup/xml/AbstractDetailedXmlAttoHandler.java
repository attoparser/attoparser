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
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        super.handleStandaloneElementStart(buffer, offset, len, line, col);
        handleXmlStandaloneElementStart(buffer, offset, len, line, col);
    }


    @Override
    public final void handleStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        super.handleStandaloneElementName(buffer, offset, len, line, col);
        handleXmlStandaloneElementName(buffer, offset, len, line, col);
    }


    @Override
    public final void handleStandaloneElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        super.handleStandaloneElementEnd(buffer, offset, len, line, col);
        handleXmlStandaloneElementEnd(buffer, offset, len, line, col);
    }


    @Override
    public final void handleOpenElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        super.handleOpenElementStart(buffer, offset, len, line, col);
        handleXmlOpenElementStart(buffer, offset, len, line, col);
    }


    @Override
    public final void handleOpenElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        super.handleOpenElementName(buffer, offset, len, line, col);
        handleXmlOpenElementName(buffer, offset, len, line, col);
    }


    @Override
    public final void handleOpenElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        super.handleOpenElementEnd(buffer, offset, len, line, col);
        handleXmlOpenElementEnd(buffer, offset, len, line, col);
    }


    @Override
    public final void handleCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        super.handleCloseElementStart(buffer, offset, len, line, col);
        handleXmlCloseElementStart(buffer, offset, len, line, col);
    }


    @Override
    public final void handleCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        super.handleCloseElementName(buffer, offset, len, line, col);
        handleXmlCloseElementName(buffer, offset, len, line, col);
    }


    @Override
    public final void handleCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        super.handleCloseElementEnd(buffer, offset, len, line, col);
        handleXmlCloseElementEnd(buffer, offset, len, line, col);
    }


    @Override
    public final void handleAutoCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        super.handleAutoCloseElementStart(buffer, offset, len, line, col);
        // Auto closing is disabled (markup has to be completely balanced),
        // exception will be thrown when the auto-closed element name is provided
    }


    @Override
    public final void handleAutoCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        super.handleAutoCloseElementName(buffer, offset, len, line, col);
        // Auto closing is disabled (markup has to be completely balanced)
        throw new AttoParseException(
                "Malformed markup: Element \"" + new String(buffer, offset, len) + "\" " +
        		"is never closed",
                line, col);
    }

    
    @Override
    public final void handleAutoCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        super.handleAutoCloseElementEnd(buffer, offset, len, line, col);
        // Auto closing is disabled (markup has to be completely balanced),
        // exception will be thrown when the auto-closed element name is provided
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
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlStandaloneElementEnd(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlOpenElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlOpenElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlOpenElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    
    
}