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
 *   Interface to be implemented by all <em>simple</em> Markup Handlers. The events declared in this interface
 *   are a simplified version of the ones in {@link org.attoparser.IMarkupHandler}.
 * </p>
 * <p>
 *   Markup handlers are the objects that receive the events produced during parsing and perform the operations
 *   the users need.
 * </p>
 * <p>
 *   Markup handlers can be <strong>stateful</strong>, which means that a new instance of the markup handler
 *   class should be created for each parsing operation. In such case, it is not required that these implementations
 *   are <em>thread-safe</em>.
 * </p>
 * <p>
 *   There is an abstract, basic, no-op implementation of this interface called
 *   {@link org.attoparser.simple.AbstractSimpleMarkupHandler} which can be used for easily creating new handlers by
 *   overriding only the relevant event handling methods.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public interface ISimpleMarkupHandler {



    /**
     * <p>
     *   Called at the beginning of document parsing.
     * </p>
     *
     * @param startTimeNanos the current time (in nanoseconds) obtained when parsing starts.
     * @param line the line of the document where parsing starts (usually number 1).
     * @param col the column of the document where parsing starts (usually number 1).
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleDocumentStart(final long startTimeNanos, final int line, final int col)
            throws ParseException;



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
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws ParseException;



    /**
     * <p>
     *   Called when an XML Declaration is found.
     * </p>
     *
     * @param version the version value specified (cannot be null).
     * @param encoding the encoding value specified (can be null).
     * @param standalone the standalone value specified (can be null).
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleXmlDeclaration(
            final String version,
            final String encoding,
            final String standalone,
            final int line, final int col)
            throws ParseException;



    /**
     * <p>
     *   Called when a DOCTYPE clause is found.
     * </p>
     *
     * @param elementName the root element name present in the DOCTYPE clause (e.g. "html").
     * @param publicId the public ID specified, if present (might be null).
     * @param systemId the system ID specified, if present (might be null).
     * @param internalSubset the internal subset specified, if present (might be null).
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleDocType(
            final String elementName, final String publicId, final String systemId,
            final String internalSubset, final int line, final int col)
            throws ParseException;



    /**
     * <p>
     *   Called when a CDATA section is found.
     * </p>
     * <p>
     *   This artifact is returned as a <tt>char[]</tt> instead of a <tt>String</tt>
     *   because its contents can be large (so creating a <tt>String</tt> is avoided if it is
     *   not considered <em>relevant</em>). In order to convert it to a
     *   <tt>String</tt>, just do <tt>new String(buffer, offset, len)</tt>.
     * </p>
     * <p>
     *   Please note <strong>the returned <tt>char[]</tt> buffer should never be modified</strong>.
     * </p>
     *
     * @param buffer the document buffer.
     * @param offset the offset of the artifact in the document buffer.
     * @param len the length (in chars) of the artifact.
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleCDATASection(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException;



    /**
     * <p>
     *   Called when a comment is found.
     * </p>
     * <p>
     *   This artifact is returned as a <tt>char[]</tt> instead of a <tt>String</tt>
     *   because its contents can be large (so creating a <tt>String</tt> is avoided if it is
     *   not considered <em>relevant</em>). In order to convert it to a
     *   <tt>String</tt>, just do <tt>new String(buffer, offset, len)</tt>.
     * </p>
     * <p>
     *   Please note <strong>the returned <tt>char[]</tt> buffer should never be modified</strong>.
     * </p>
     *
     * @param buffer the document buffer.
     * @param offset the offset of the artifact in the document buffer.
     * @param len the length (in chars) of the artifact.
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleComment(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException;



    /**
     * <p>
     *   Called when a <i>text</i> artifact is found.
     * </p>
     * <p>
     *   A sequence of chars is considered to be <i>text</i> when no structures of any kind are
     *   contained inside it. This means no tags (a.k.a. <i>elements</i>),
     *   DOCTYPE's, processing instructions, etc. are contained in the sequence.
     * </p>
     * <p>
     *   Text sequences might include any number of new line and/or control characters.
     * </p>
     * <p>
     *   This artifact is returned as a <tt>char[]</tt> instead of a <tt>String</tt>
     *   because its contents can be large (so creating a <tt>String</tt> is avoided if it is
     *   not considered <em>relevant</em>). In order to convert it to a
     *   <tt>String</tt>, just do <tt>new String(buffer, offset, len)</tt>.
     * </p>
     * <p>
     *   Please note <strong>the returned <tt>char[]</tt> buffer should never be modified</strong>.
     * </p>
     *
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the text artifact starts.
     * @param len the length (in chars) of the text artifact, starting in offset.
     * @param line the line in the original document where this text artifact starts.
     * @param col the column in the original document where this text artifact starts.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleText(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException;



    /**
     * <p>
     *   Called when a standalone element (an element with no closing tag) is found.
     * </p>
     * <p>
     *   Note that <b>the element attributes map can be null if no attributes are present</b>.
     * </p>
     *
     * @param elementName the element name (e.g. "&lt;img src="logo.png"&gt;" -&gt; "img").
     * @param attributes the element attributes map, or null if no attributes are present.
     * @param minimized whether the element has been found minimized (&lt;element/&gt;)in code or not.
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleStandaloneElement(
            final String elementName, final Map<String,String> attributes,
            final boolean minimized,
            final int line, final int col)
            throws ParseException;


    /**
     * <p>
     *   Called when an open element (an <i>open tag</i>) is found.
     * </p>
     * <p>
     *   Note that <b>the element attributes map can be null if no attributes are present</b>.
     * </p>
     *
     * @param elementName the element name (e.g. "&lt;div class="content"&gt;" -&gt; "div").
     * @param attributes the element attributes map, or null if no attributes are present.
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleOpenElement(
            final String elementName, final Map<String,String> attributes,
            final int line, final int col)
            throws ParseException;


    /**
     * <p>
     *   Called when a close element (a <i>close tag</i>) is found.
     * </p>
     *
     * @param elementName the element name (e.g. "&lt;/div&gt;" -&gt; "div").
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleCloseElement(
            final String elementName, final int line, final int col)
            throws ParseException;


    /**
     * <p>
     *   Called when a close element (a <i>close tag</i>) is needed in order
     *   to correctly balance the markup. This is called <i>auto-closing</i>.
     * </p>
     * <p>
     *   Implementors might choose to ignore these autoclosing events.
     * </p>
     *
     * @param elementName the element name (e.g. "&lt;/div&gt;" -&gt; "div").
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleAutoCloseElement(
            final String elementName, final int line, final int col)
            throws ParseException;


    /**
     * <p>
     *   Called when a close element (a <i>close tag</i>) appears without
     *   a corresponding <i>open element</i>.
     * </p>
     * <p>
     *   Implementors might choose to ignore these events.
     * </p>
     *
     * @param elementName the element name (e.g. "&lt;/div&gt;" -&gt; "div").
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleUnmatchedCloseElement(
            final String elementName, final int line, final int col)
            throws ParseException;



    /**
     * <p>
     *   Called when a Processing Instruction is found.
     * </p>
     *
     * @param target the target specified in the processing instruction.
     * @param content the content of the processing instruction, if specified (might be null).
     * @param line the line in the document where this elements appears.
     * @param col the column in the document where this element appears.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleProcessingInstruction(
            final String target, final String content, 
            final int line, final int col) 
            throws ParseException;


    
}