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
package org.attoparser.simple;

import java.util.Map;

import org.attoparser.ParseException;


/**
 * <p>
 *   Base abstract implementation of {@link org.attoparser.simple.ISimpleMarkupHandler} that implements all of
 *   its methods as no-ops.
 * </p>
 * <p>
 *   This class allows the easy creation of new handler implementations by extending it and simply overriding
 *   the methods that are of interest for the developer.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public abstract class AbstractSimpleMarkupHandler implements ISimpleMarkupHandler {


    protected AbstractSimpleMarkupHandler() {
        super();
    }




    public void handleDocumentStart(
            final long startTimeNanos, final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleXmlDeclaration(
            final String version, final String encoding, final String standalone,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleDocType(
            final String elementName, final String publicId, final String systemId, final String internalSubset,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleCDATASection(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleComment(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleText(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleStandaloneElement(
            final String elementName, final Map<String, String> attributes,
            final boolean minimized,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleOpenElement(
            final String elementName, final Map<String, String> attributes,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleAutoOpenElement(
            final String elementName, final Map<String, String> attributes,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleCloseElement(
            final String elementName,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleAutoCloseElement(
            final String elementName,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleUnmatchedCloseElement(
            final String elementName,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleProcessingInstruction(
            final String target, final String content,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done here, meant to be overridden if required
    }


}