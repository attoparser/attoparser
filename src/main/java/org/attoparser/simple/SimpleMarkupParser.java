/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2025 Attoparser (https://www.attoparser.org)
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
package org.attoparser.simple;

import java.io.Reader;

import org.attoparser.MarkupParser;
import org.attoparser.ParseException;
import org.attoparser.config.ParseConfiguration;


/**
 * <p>
 *   Default implementation of {@link org.attoparser.simple.ISimpleMarkupParser}.
 * </p>
 * <p>
 *   <i>AttoParser</i> <em>simple</em> markup parsers work as SAX-style parsers that need
 *   a <i>markup handler</i> object for handling parsing events. These handlers implement
 *   the {@link org.attoparser.simple.ISimpleMarkupHandler} interface, and are normally developed by
 *   users in order to perform the operations they require for their applications.
 * </p>
 * <p>
 *   See the documentation of the {@link org.attoparser.simple.ISimpleMarkupHandler} interface for more
 *   information on the event handler methods.
 * </p>
 * <p>
 *   Note that this parser class and its corresponding handlers are actually a <strong>simplified
 *   version</strong> of the full-blown {@link org.attoparser.IMarkupParser} infrastructure.
 * </p>
 * <p>
 *   Sample usage:
 * </p>
 * <pre><code>
 *   // Obtain a java.io.Reader on the document to be parsed
 *   final Reader documentReader = ...;
 *
 *   // Create the handler instance. Extending the no-op AbstractSimpleMarkupHandler is a good start
 *   final ISimpleMarkupHandler handler = new AbstractSimpleMarkupHandler() {
 *       ... // some events implemented
 *   };
 *
 *   // Create or obtain the parser instance (can be reused). Example uses the default configuration for HTML
 *   final ISimpleMarkupParser parser = new SimpleMarkupParser(ParseConfiguration.htmlConfiguration());
 *
 *   // Parse it!
 *   parser.parse(documentReader, handler);
 * </code></pre>
 * <p>
 *   This parser class uses an instance of the {@link org.attoparser.MarkupParser} class underneath (configured
 *   with the default values for its buffer pool), and applies to it an instance of the
 *   {@link org.attoparser.simple.SimplifierMarkupHandler} handler class in order to make it able to report
 *   handling events by means of an {@link org.attoparser.simple.ISimpleMarkupHandler} implementation instead of using
 *   the default {@link org.attoparser.IMarkupHandler} interface.
 * </p>
 * <p>
 *   In fact, using the {@link org.attoparser.simple.SimpleMarkupParser} class as shown above is completely
 *   equivalent to:
 * </p>
 * <pre><code>
 *   // Obtain a java.io.Reader on the document to be parsed
 *   final Reader documentReader = ...;
 *
 *   // Create the handler instance. Extending the no-op AbstractSimpleMarkupHandler is a good start
 *   final ISimpleMarkupHandler simpleHandler = new AbstractSimpleMarkupHandler() {
 *       ... // some events implemented
 *   };
 *
 *   // Create a handler chain with the 'simplifier' handler, which will convert events from 'normal' to 'simple'.
 *   final IMarkupHandler handler = new SimplifierMarkupHandler(simpleHandler);
 *
 *   // Create or obtain the parser instance (note this is not the 'simple' one!)
 *   final IMarkupParser parser = new MarkupParser(ParseConfiguration.htmlConfiguration());
 *
 *   // Parse it!
 *   parser.parse(documentReader, handler);
 * </code></pre>
 * <p>
 *   This parser class is <b>thread-safe</b>. However, take into account that, normally,
 *   {@link org.attoparser.simple.ISimpleMarkupHandler} implementations are not. So, even if parsers can be reused,
 *   handler objects usually cannot.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class SimpleMarkupParser implements ISimpleMarkupParser {


    private final MarkupParser markupParser;




    public SimpleMarkupParser(final ParseConfiguration configuration) {
        super();
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        this.markupParser = new MarkupParser(configuration);
    }





    public void parse(final String document, final ISimpleMarkupHandler handler)
            throws ParseException {

        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        this.markupParser.parse(document, new SimplifierMarkupHandler(handler));

    }


    public void parse(final char[] document, final ISimpleMarkupHandler handler)
            throws ParseException {

        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        this.markupParser.parse(document, new SimplifierMarkupHandler(handler));

    }


    public void parse(
            final char[] document, final int offset, final int len, final ISimpleMarkupHandler handler)
            throws ParseException {

        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        this.markupParser.parse(document, offset, len, new SimplifierMarkupHandler(handler));

    }


    public void parse(
            final Reader reader, final ISimpleMarkupHandler handler)
            throws ParseException {

        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        this.markupParser.parse(reader, new SimplifierMarkupHandler(handler));

    }


}
