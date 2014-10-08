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
package org.attoparser.dom;

import java.io.Reader;

import org.attoparser.MarkupParser;
import org.attoparser.ParseException;
import org.attoparser.config.ParseConfiguration;


/**
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class DOMMarkupParser implements IDOMMarkupParser {


    private final MarkupParser markupParser;




    public DOMMarkupParser(final ParseConfiguration configuration) {
        super();
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        this.markupParser = new MarkupParser(configuration);
    }





    public Document parse(final String document)
            throws ParseException {
        return parse(null, document);
    }


    public Document parse(final char[] document)
            throws ParseException {
        return parse(null, document);
    }


    public Document parse(final char[] document, final int offset, final int len)
            throws ParseException {
        return parse(null, document, offset, len);
    }


    public Document parse(final Reader reader)
            throws ParseException {
        return parse(null, reader);
    }




    public Document parse(final String documentName, final String document)
            throws ParseException {

        final DOMBuilderMarkupHandler domHandler = new DOMBuilderMarkupHandler(documentName);
        this.markupParser.parse(document, domHandler);
        return domHandler.getDocument();

    }


    public Document parse(final String documentName, final char[] document)
            throws ParseException {

        final DOMBuilderMarkupHandler domHandler = new DOMBuilderMarkupHandler(documentName);
        this.markupParser.parse(document, domHandler);
        return domHandler.getDocument();

    }


    public Document parse(final String documentName, final char[] document, final int offset, final int len)
            throws ParseException {

        final DOMBuilderMarkupHandler domHandler = new DOMBuilderMarkupHandler(documentName);
        this.markupParser.parse(document, offset, len, domHandler);
        return domHandler.getDocument();

    }


    public Document parse(final String documentName, final Reader reader)
            throws ParseException {

        final DOMBuilderMarkupHandler domHandler = new DOMBuilderMarkupHandler(documentName);
        this.markupParser.parse(reader, domHandler);
        return domHandler.getDocument();

    }

}
