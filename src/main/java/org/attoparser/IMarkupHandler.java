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

import org.attoparser.config.ParseConfiguration;
import org.attoparser.select.ParseSelection;

/**
 * <p>
 *   Interface to be implemented by all Markup Handlers.
 * </p>
 * <p>
 *   Markup handlers are the objects that receive the events produced during parsing and perform the operations
 *   the users need. <strong>This interface is the main entry point to use AttoParser</strong>.
 * </p>
 * <p>
 *   Markup handlers can be <strong>stateful</strong>, which means that a new instance of the markup handler
 *   class should be created for each parsing operation. In such case, it is not required that these implementations
 *   are <em>thread-safe</em>.
 * </p>
 * <p>
 *   There is an abstract, basic, no-op implementation of this interface called
 *   {@link org.attoparser.AbstractMarkupHandler} which can be used for easily creating new handlers by overriding
 *   only the relevant event handling methods.
 * </p>
 * <p>
 *   Note also there is a <strong>simplified</strong> version of this interface that reduces the number of events
 *   and also simplifies the operations on textual buffers, called {@link org.attoparser.simple.ISimpleMarkupHandler},
 *   which can be easily used with the convenience ad-hoc parser class
 *   {@link org.attoparser.simple.SimpleMarkupParser}.
 * </p>
 * <p>
 *   AttoParser provides several useful implementations of this interface out-of-the-box:
 * </p>
 *
 * <h3>Markup output</h3>
 * <dl>
 *
 *   <dt>{@link org.attoparser.output.OutputMarkupHandler}</dt><dd>
 *     For writing the received events to a
 *     specified {@link java.io.Writer} object, without any loss of information (case, whitespaces, etc.). This
 *     handler is useful for performing filtering/transformation operations on the parsed markup, placing this
 *     handler at the end of the handler chain so that it outputs the final results of such operation.
 *   </dd>
 *
 *   <dt>{@link org.attoparser.output.TextOutputMarkupHandler}</dt>
 *   <dd>
 *     For writing the received events to a
 *     specified {@link java.io.Writer} object as mere text, ignoring all non-text events. This will effectively
 *     strip all markup elements, comments, DOCTYPEs, etc. from the original markup.
 *   </dd>
 * </dl>
 *
 * <h3>Format conversion and transformation operations</h3>
 * <dl>
 *
 *   <dt>{@link org.attoparser.dom.DOMBuilderMarkupHandler}</dt>
 *   <dd>
 *     For building a DOM tree as a result of parsing
 *     a document. This DOM tree will be created using the classes at the <tt>org.attoparser.dom</tt> package.
 *     This handler can be more easily applied by using the convenience ad-hoc parser
 *     class {@link org.attoparser.dom.DOMMarkupParser}.
 *   </dd>
 *
 *   <dt>{@link org.attoparser.simple.SimplifierMarkupHandler}</dt>
 *   <dd>
 *     For transforming the produced markup
 *     parsing events into a much simpler format, removing much of the complexity of these parsing events
 *     and allowing users to create their handlers by means of the
 *     {@link org.attoparser.simple.ISimpleMarkupHandler} interface. Note this handler can be more easily
 *     applied by using the convenience ad-hoc parser class {@link org.attoparser.simple.SimpleMarkupParser}.
 *   </dd>
 *
 *   <dt>{@link org.attoparser.minimize.MinimizeHtmlMarkupHandler}</dt>
 *   <dd>
 *       For minimizing (compacting) HTML markup: remove excess white space, unquote attributes, etc.
 *   </dd>
 *
 * </dl>
 *
 * <h3>Fragment selection and event management</h3>
 * <dl>
 *
 *   <dt>{@link org.attoparser.select.BlockSelectorMarkupHandler}</dt>
 *   <dd>
 *     For applying <em>block selection</em> (element + subtree) on the parsed markup, based on a set
 *     of specified <em>markup selectors</em> (see {@link org.attoparser.select}).
 *   </dd>
 *
 *   <dt>{@link org.attoparser.select.NodeSelectorMarkupHandler}</dt>
 *   <dd>
 *     For applying <em>node selection</em> (element, no subtree) on the parsed markup, based on a set
 *     of specified <em>markup selectors</em> (see {@link org.attoparser.select}).
 *   </dd>
 *
 *   <dt>{@link org.attoparser.select.AttributeSelectionMarkingMarkupHandler}</dt>
 *   <dd>
 *     For synthetically adding an attribute (with the specified name) to markup elements displaying which of the
 *     specified selectors (block or node) match those markup elements.
 *   </dd>
 *
 *   <dt>{@link org.attoparser.duplicate.DuplicateMarkupHandler}</dt>
 *   <dd>
 *     For duplicating parsing events, sending each
 *     of them to two different implementations if {@link org.attoparser.IMarkupHandler}.
 *   </dd>
 *
 * </dl>
 *
 * <h3>Testing and Debugging</h3>
 * <dl>
 *
 *   <dt>{@link org.attoparser.prettyhtml.PrettyHtmlMarkupHandler}</dt>
 *   <dd>
 *     For creating an HTML
 *     document visually explaining all the events happened during the parsing of a document:
 *     elements, attributes, auto-closing of elements, unmatched artifacts, etc.
 *   </dd>
 *
 *   <dt>{@link org.attoparser.trace.TraceBuilderMarkupHandler}</dt>
 *   <dd>
 *     For building a trace of parsing events (a
 *     list of {@link org.attoparser.trace.MarkupTraceEvent} objects) detailing all the events launched
 *     during the parsing of a document.
 *   </dd>
 *
 * </dl>
 *
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public interface IMarkupHandler
            extends IDocumentHandler, IXMLDeclarationHandler, IDocTypeHandler, ICDATASectionHandler, ICommentHandler,
                    ITextHandler, IElementHandler, IProcessingInstructionHandler {


    /**
     * <p>
     *   Sets the {@link org.attoparser.config.ParseConfiguration} object that will be used during the parsing
     *   operation. This object will normally have been specified to the parser object during its instantiation
     *   or initialization.
     * </p>
     * <p>
     *   This method is always called by the parser <strong>before</strong> calling any other event handling method.
     * </p>
     * <p>
     *   Note that this method can be <strong>safely ignored by most implementations</strong>, as there are
     *   very few scenarios in which this kind of interaction would be consisdered relevant.
     * </p>
     *
     * @param parseConfiguration the configuration object.
     */
    public void setParseConfiguration(final ParseConfiguration parseConfiguration);


    /**
     * <p>
     *   Sets the {@link org.attoparser.ParseStatus} object that will be used during the parsing operation. This
     *   object can be used for instructing the parser about specific low-level conditions arisen during event
     *   handling.
     * </p>
     * <p>
     *   This method is always called by the parser <strong>before</strong> calling any other event handling method.
     * </p>
     * <p>
     *   Note that this method can be <strong>safely ignored by most implementations</strong>, as there are
     *   very few and very specific scenarios in which this kind of interaction with the parser would be needed.
     *   It is therefore mainly for internal use.
     * </p>
     *
     * @param status the status object.
     */
    public void setParseStatus(final ParseStatus status);


    /**
     * <p>
     *   Sets the {@link org.attoparser.select.ParseSelection} object that represents the different levels of
     *   selectors (if any) that are currently active for the fired events.
     * </p>
     * <p>
     *   This method is always called by the parser <strong>before</strong> calling any other event handling method.
     * </p>
     * <p>
     *   Note that this method can be <strong>safely ignored by most implementations</strong>, as there are
     *   very few scenarios in which this kind of interaction would be consisdered relevant.
     * </p>
     *
     * @param selection the selection object.
     */
    public void setParseSelection(final ParseSelection selection);


}