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
package org.attoparser;

import java.io.Reader;


/**
 * <p>
 *   Interface to be implemented by all Markup Parsers.
 *   Default implementation is {@link org.attoparser.MarkupParser}.
 * </p>
 * <p>
 *   <i>AttoParser</i> markup parsers work as SAX-style parsers that need
 *   a <i>markup handler</i> object for handling parsing events. These handlers implement
 *   the {@link org.attoparser.IMarkupHandler} interface, and are normally developed by
 *   users in order to perform the operations they require for their applications.
 * </p>
 * <p>
 *   See the documentation of the {@link org.attoparser.IMarkupHandler} interface for more
 *   information on the event handler methods, and also on the handler implementations
 *   AttoParser provides out-of-the-box.
 * </p>
 * <p>
 *   Also, note there are two different specialized parsers that use
 *   {@link org.attoparser.MarkupParser} underneath, but which are oriented towards allowing
 *   an easy use of specific parsing features: {@link org.attoparser.dom.IDOMMarkupParser} for
 *   DOM-oriented parsing and {@link org.attoparser.simple.ISimpleMarkupParser} for using
 *   a simplified version of the handler interface ({@link org.attoparser.simple.ISimpleMarkupHandler}).
 * </p>
 * <p>
 *   Sample usage:
 * </p>
 * <pre><code>
 *   // Obtain a java.io.Reader on the document to be parsed
 *   final Reader documentReader = ...;
 *
 *   // Create the handler instance. Extending the no-op AbstractMarkupHandler is a good start
 *   final IMarkupHandler handler = new AbstractMarkupHandler() {
 *       ... // some events implemented
 *   };
 *
 *   // Create or obtain the parser instance (can be reused). Example uses the default configuration for HTML
 *   final IMarkupParser parser = new MarkupParser(ParseConfiguration.htmlConfiguration());
 *
 *   // Parse it!
 *   parser.parse(documentReader, handler);
 * </code></pre>
 * <p>
 *   Note that implementations of this interface should be <strong>thread-safe</strong>, and therefore parsers
 *   should be reusable through several parsing operations and any number of concurrent threads.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public interface IMarkupParser {
    

    /**
     * <p>
     *   Parse a document using the specified {@link IMarkupHandler}.
     * </p>
     *
     * @param document the document to be parsed, as a String.
     * @param handler the handler to be used, an {@link IMarkupHandler} implementation.
     * @throws ParseException if the document cannot be parsed.
     */
    public void parse(final String document, final IMarkupHandler handler)
            throws ParseException;

    
    /**
     * <p>
     *   Parse a document using the specified {@link IMarkupHandler}.
     * </p>
     * 
     * @param document the document to be parsed, as a char[].
     * @param handler the handler to be used, an {@link IMarkupHandler} implementation.
     * @throws ParseException if the document cannot be parsed.
     */
    public void parse(final char[] document, final IMarkupHandler handler)
            throws ParseException;

    
    /**
     * <p>
     *   Parse a document using the specified {@link IMarkupHandler}.
     * </p>
     * 
     * @param document the document to be parsed, as a char[].
     * @param offset the offset to be applied on the char[] document to determine the
     *        start of the document contents.
     * @param len the length (in chars) of the document stored in the char[].
     * @param handler the handler to be used, an {@link IMarkupHandler} implementation.
     * @throws ParseException if the document cannot be parsed.
     */
    public void parse(final char[] document, final int offset, final int len, final IMarkupHandler handler)
            throws ParseException;
    
    
    /**
     * <p>
     *   Parse a document using the specified {@link IMarkupHandler}.
     * </p>
     * <p>
     *   Implementations of this interface must close the provided {@link Reader}
     *   object after parsing.   
     * </p>
     * 
     * @param reader a Reader on the document.
     * @param handler the handler to be used, an {@link IMarkupHandler} implementation.
     * @throws ParseException if the document cannot be parsed.
     */
    public void parse(final Reader reader, final IMarkupHandler handler)
            throws ParseException;


    
}
