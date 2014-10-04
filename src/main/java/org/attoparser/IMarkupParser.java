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
package org.attoparser;

import java.io.Reader;


/**
 * <p>
 *   Common interface for all <i>attoparser</i> instances.
 *   Default implementation is {@link MarkupParser}.
 * </p>
 * <p>
 *   <i>Attoparser</i> implementations work as SAX-style parsers that need 
 *   an <i>attohandler</i> instance for handling parsing events. Depending
 *   on the desired level of detail in these events, several handler 
 *   abstract and concrete implementations are offered out-of-the-box:
 * </p>
 * <ul>
 *   <li>{@link AbstractMarkupHandler}: basic implementation only differentiating
 *       between <i>text</i> and <i>structures</i></li>
 *   <li>{@link MarkupEventProcessor}: markup-specialized
 *       (XML and HTML) abstract handler able not only to differentiate among different
 *       types of markup structures, but also of reporting lowel-level detail inside
 *       elements (name, attributes, inner whitespace) and DOCTYPE clauses.</li>
 *   <li>{@link org.attoparser.simple.SimplifierMarkupHandler}: higher-level
 *       markup-specialized (XML and HTML) abstract handler that offers an interface
 *       more similar to the Standard SAX ContentHandlers (use of Strings instead of
 *       char[]'s, attribute maps, etc).</li>
 *   <li>{@link org.attoparser.xml.XmlMarkupAttoHandler}: XML-specialized
 *       abstract handler equivalent to {@link MarkupEventProcessor}
 *       but only allowing XML markup.</li>
 *   <li>{@link org.attoparser.dom.DOMBuilderMarkupHandler}: handler implementation
 *       (non-abstract) for building an attoDOM tree (DOM node tres based on classes
 *       from the <tt>org.attoparser.markup.dom</tt> package) from XML markup.</li> 
 * </ul>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
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
