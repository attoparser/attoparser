/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2022, The ATTOPARSER team (https://www.attoparser.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       https://www.apache.org/licenses/LICENSE-2.0
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
 * <p>
 *   Default implementation of the {@link org.attoparser.dom.IDOMMarkupParser} interface.
 * </p>
 * <p>
 *   DOM trees created by this class are made with objects of the classes from the <tt>org.attoparser.dom</tt>
 *   package.
 * </p>
 * <p>
 *   Note that this parser interface is actually a convenience artifact aimed at using
 *   the {@link org.attoparser.dom.DOMBuilderMarkupHandler} <strong>DOM-conversion</strong> handler
 *   more easily.
 * </p>
 * <p>
 *   Sample usage:
 * </p>
 * <pre><code>
 *   // Obtain a java.io.Reader on the document to be parsed
 *   final Reader documentReader = ...;
 *
 *   // Create or obtain the parser instance (note this is not the 'simple' one!)
 *   final IDOMMarkupParser parser = new DOMMarkupParser(ParseConfiguration.htmlConfiguration());
 *
 *   // Parse it and return the Document Object Model
 *   final Document document = parser.parse("Some document", documentReader);
 * </code></pre>
 * <p>
 *   This parser class uses an instance of the {@link org.attoparser.MarkupParser} class underneath (configured
 *   with the default values for its buffer pool), and applies to it an instance of the
 *   {@link org.attoparser.dom.DOMBuilderMarkupHandler} handler class in order to make it produce a DOM
 *   (Document Object model) tree as a result of parsing.
 * </p>
 * <p>
 *   In fact, using the {@link org.attoparser.dom.DOMMarkupParser} class as shown above is completely
 *   equivalent to:
 * </p>
 * <pre><code>
 *   // Obtain a java.io.Reader on the document to be parsed
 *   final Reader documentReader = ...;
 *
 *   // Instance the DOM-builder handler
 *   final DOMBuilderMarkupHandler handler = new DOMBuilderMarkupHandler("Some document");
 *
 *   // Create or obtain the parser instance
 *   final IMarkupParser parser = new MarkupParser(ParseConfiguration.htmlConfiguration());
 *
 *   // Parse the document
 *   parser.parse(documentReader, handler);
 *
 *   // Obtain the parsed Document Object Model
 *   final Document document = handler.getDocument();
 * </code></pre>
 * <p>
 *   This parser class is <b>thread-safe</b>.
 * </p>
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
