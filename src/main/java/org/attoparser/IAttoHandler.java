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



/**
 * <p>
 *   Common interface for all handler implementations. An object implementing this interface
 *   has to be provided to {@link IAttoParser} objects in order to parse a document.
 * </p>
 * <p><b>Event Handling</b></p>
 * <p>
 *   At its most basic, a handler processes four events:
 * </p>
 * <ul>
 *   <li><b>document start</b> (see {@link #handleDocumentStart(int, int)}): triggered at the beginning of
 *       document parsing.</li>
 *   <li><b>document end</b> (see {@link #handleDocumentEnd(int, int)}): triggered at the end of document
 *       parsing.</li>
 *   <li><b>text</b> (see {@link #handleText(char[], int, int, int, int)}): for texts inside the 
 *       document being parsed, containing no instructions or metainformation of any kind.</li>
 *   <li><b>structure</b> (see {@link #handleStructure(char[], int, int, int, int)}): for all kinds of
 *       directives, instructions, metainformation or formatting data inside the document.</li>
 * </ul>
 * <p>
 *   For example, a markup-specialized parser (HTML and XML) will consider tags (a.k.a. <i>elements</i>),
 *   DOCTYPE clauses, etc. as <i>structures</i>.
 * </p>
 * <p>
 *   Even if document parsing events at their most basic level are only divided between <i>texts</i> and
 *   <i>structures</i>, some implementations of this interface might decide to specialize events even more,
 *   like for example differentiating between <i>opening</i> and <i>closing elements</i>, 
 *   <i>attributes</i>, etc.
 * </p>
 * <p><b>Event features</b></p>
 * <p>
 *   Most attohandler events have two important features:
 * </p>
 * <ul>
 *   <li><b>Event location</b>: event handler signatures include specification of <i>line</i> and <i>col</i>, 
 *       determining the position in the document where such events occur.</li>
 *   <li><b>Use of <tt>char[]</tt> at most levels</b>: instead of always creating <tt>String</tt> objects, 
 *       many artifacts are returned as fragments of the original <tt>char[]</tt> document buffer so that 
 *       memory usage is limited (<tt>String</tt>s require copying the underlying array of <tt>char</tt>s). 
 *       [<tt>offset</tt>, <tt>length</tt>] pairs are always provided so that <tt>String</tt>s can be 
 *       created when required simply with <tt>new String(buffer, offset, len)</tt>.</li>
 * </ul>
 * <p><b>Provided Handlers</b></p>
 * <p>
 *   Several {@link IAttoHandler} implementations with diverse levels of detail are provided
 *   out-of-the-box:
 * </p>
 * <ul>
 *   <li>{@link AbstractAttoHandler}: basic implementation only differentiating
 *       between <i>text</i> and <i>structures</i></li>
 *   <li>{@link org.attoparser.markup.AbstractBasicMarkupAttoHandler}: markup-specialized
 *       (XML and HTML) abstract handler able to differentiate among different
 *       types of markup structures: Elements, comments, CDATA, DOCTYPE, etc.</li>
 *   <li>{@link org.attoparser.markup.MarkupEventProcessor}: markup-specialized
 *       (XML and HTML) abstract handler able not only to differentiate among different
 *       types of markup structures, but also of reporting lowel-level detail inside
 *       elements (name, attributes, inner whitespace) and DOCTYPE clauses.</li>
 *   <li>{@link org.attoparser.markup.simple.SimplifierMarkupAttoHandler}: higher-level
 *       markup-specialized (XML and HTML) abstract handler that offers an interface
 *       more similar to the Standard SAX {@link org.xml.sax.ContentHandler}s (use of 
 *       Strings instead of char[]'s, attribute maps, etc).</li>
 *   <li>{@link org.attoparser.markup.xml.XmlMarkupAttoHandler}: XML-specialized
 *       abstract handler equivalent to {@link org.attoparser.markup.MarkupEventProcessor}
 *       but only allowing XML markup.</li>
 *   <li>{@link org.attoparser.markup.xml.AbstractStandardXmlAttoHandler}: XML-specialized
 *       abstract handler equivalent to {@link org.attoparser.markup.simple.SimplifierMarkupAttoHandler}
 *       but only allowing XML markup.</li>
 *   <li>{@link org.attoparser.markup.dom.DOMBuilderMarkupAttoHandler}: handler implementation
 *       (non-abstract) for building an attoDOM tree (DOM node tres based on classes
 *       from the <tt>org.attoparser.markup.dom</tt> package) from XML markup.</li> 
 * </ul>
 * <p><b>Creating handler implementations</b></p>
 * <p>
 *   The usual way to create an {@link IAttoHandler} implementation for parsing documents is to
 *   extend one of the provided abstract implementations (see above) and provide an implementation
 *   for the methods that are relevant for parsing.
 * </p>
 * <p><b><tt>*Handling</tt> interfaces</b></p>
 * <p>
 *   Specific {@link IAttoHandler} implementations (abstract or concrete) usually aggregate
 *   event features by means of implementing <tt>*Handling</tt> interfaces that define
 *   these features.
 * </p>
 * <p><b>Thread safety</b></p>
 * <p>
 *   Unless contrary specified, <b>implementations of this interface are not thread-safe</b>.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public interface IAttoHandler {


    /**
     * <p>
     *   Called at the beginning of document parsing.
     * </p>
     *
     * @param startTimeNanos the current time (in nanoseconds) obtained when parsing starts.
     * @param line the line of the document where parsing starts (usually number 1)
     * @param col the column of the document where parsing starts (usually number 1)
     * @throws AttoParseException
     */
    public IAttoHandleResult handleDocumentStart(final long startTimeNanos, final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called at the end of document parsing.
     * </p>
     *
     * @param endTimeNanos the current time (in nanoseconds) obtained when parsing ends.
     * @param totalTimeNanos the difference between current times at the start and end of
     *        parsing (in nanoseconds)
     * @param line the line of the document where parsing ends (usually the last one)
     * @param col the column of the document where the parsing ends (usually the last one)
     * @throws AttoParseException
     */
    public IAttoHandleResult handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws AttoParseException;

}
