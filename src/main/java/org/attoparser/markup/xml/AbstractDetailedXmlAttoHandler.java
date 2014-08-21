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
import org.attoparser.IAttoHandleResult;
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
    public final IAttoHandleResult handleDocumentStart(final long startTimeNanos,
            final int line, final int col, final MarkupParsingConfiguration documentRestrictions)
            throws AttoParseException {
        return handleXmlDocumentStart(startTimeNanos, line, col);
    }


    @Override
    public final IAttoHandleResult handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos,
            final int line, final int col, final MarkupParsingConfiguration documentRestrictions)
            throws AttoParseException {
        return handleXmlDocumentEnd(endTimeNanos, totalTimeNanos, line, col);
    }




    @Override
    public final IAttoHandleResult handleStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws AttoParseException {
        return handleXmlStandaloneElementStart(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final IAttoHandleResult handleStandaloneElementEnd(
            final int line, final int col)
            throws AttoParseException {
        return handleXmlStandaloneElementEnd(line, col);
    }


    @Override
    public final IAttoHandleResult handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        return handleXmlOpenElementStart(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final IAttoHandleResult handleOpenElementEnd(
            final int line, final int col)
            throws AttoParseException {
        return handleXmlOpenElementEnd(line, col);
    }


    @Override
    public final IAttoHandleResult handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        return handleXmlCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final IAttoHandleResult handleCloseElementEnd(
            final int line, final int col)
            throws AttoParseException {
        return handleXmlCloseElementEnd(line, col);
    }


    @Override
    public final IAttoHandleResult handleAutoCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Auto closing is disabled (markup has to be completely balanced).
        throw new AttoParseException(
                "Malformed markup: Element \"" + new String(buffer, nameOffset, nameLen) + "\" " +
                "is never closed",
                line, col);
    }

    
    @Override
    public final IAttoHandleResult handleAutoCloseElementEnd(
            final int line, final int col)
            throws AttoParseException {
        // Auto closing is disabled (markup has to be completely balanced).
        return null;
    }








    
    

    
    @SuppressWarnings("unused")
    public IAttoHandleResult handleXmlDocumentStart(final long startTimeNanos,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    @SuppressWarnings("unused")
    public IAttoHandleResult handleXmlDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    public IAttoHandleResult handleXmlStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    public IAttoHandleResult handleXmlStandaloneElementEnd(
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    public IAttoHandleResult handleXmlOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    public IAttoHandleResult handleXmlOpenElementEnd(
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    public IAttoHandleResult handleXmlCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    public IAttoHandleResult handleXmlCloseElementEnd(
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }
    
    
}