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
package org.attoparser.markup.html;

import org.attoparser.AttoParseException;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public interface IDetailedHtmlElementHandling extends IHtmlAttributeSequenceHandling {

    

    /**
     * <p>
     *   Called when the start of a minimized standalone element is found. This
     *   "start" is considered to be the "<tt>&lt;</tt>" symbol the element starts with.
     * </p>
     * <p>
     *   A <tt>minimized</tt> standalone element is well-formed from the XML standpoint. 
     *   For example: <tt>&lt;img src="..." /&gt;</tt>, as opposed to 
     *   <tt>&lt;img src="..."&gt;</tt> (non-minimized, perfectly valid from the HTML 
     *   but not from the XML or XHTML standpoints).
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlMinimizedStandaloneElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of a minimized standalone element is found. E.g., the
     *   "element name" of an element like <tt>&lt;img src="images/logo.png" /&gt;</tt> would be 
     *   "<tt>img</tt>".
     * </p>
     * <p>
     *   A <tt>minimized</tt> standalone element is well-formed from the XML standpoint. 
     *   For example: <tt>&lt;img src="..." /&gt;</tt>, as opposed to 
     *   <tt>&lt;img src="..."&gt;</tt> (non-minimized, perfectly valid from the HTML 
     *   but not from the XML or XHTML standpoints).
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlMinimizedStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of a minimized standalone element is found. This
     *   "end" is considered to be the "<tt>/&gt;</tt>" (if minimized) or 
     *   "<tt>&gt;</tt>" (if not minimized) sequence the element ends with.
     * </p>
     * <p>
     *   A <tt>minimized</tt> standalone element is well-formed from the XML standpoint. 
     *   For example: <tt>&lt;img src="..." /&gt;</tt>, as opposed to 
     *   <tt>&lt;img src="..."&gt;</tt> (non-minimized, perfectly valid from the HTML 
     *   but not from the XML or XHTML standpoints).
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlMinimizedStandaloneElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    
    
    
    
    /**
     * <p>
     *   Called when the start of a non-minimized standalone element is found. This
     *   "start" is considered to be the "<tt>&lt;</tt>" symbol the element starts with.
     * </p>
     * <p>
     *   A <tt>minimized</tt> standalone element is well-formed from the XML standpoint. 
     *   For example: <tt>&lt;img src="..." /&gt;</tt>, as opposed to 
     *   <tt>&lt;img src="..."&gt;</tt> (non-minimized, perfectly valid from the HTML 
     *   but not from the XML or XHTML standpoints).
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlNonMinimizedStandaloneElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of a non-minimized standalone element is found. E.g., the
     *   "element name" of an element like <tt>&lt;img src="images/logo.png" /&gt;</tt> would be 
     *   "<tt>img</tt>".
     * </p>
     * <p>
     *   A <tt>minimized</tt> standalone element is well-formed from the XML standpoint. 
     *   For example: <tt>&lt;img src="..." /&gt;</tt>, as opposed to 
     *   <tt>&lt;img src="..."&gt;</tt> (non-minimized, perfectly valid from the HTML 
     *   but not from the XML or XHTML standpoints).
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlNonMinimizedStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of a non-minimized standalone element is found. This
     *   "end" is considered to be the "<tt>/&gt;</tt>" (if minimized) or 
     *   "<tt>&gt;</tt>" (if not minimized) sequence the element ends with.
     * </p>
     * <p>
     *   A <tt>minimized</tt> standalone element is well-formed from the XML standpoint. 
     *   For example: <tt>&lt;img src="..." /&gt;</tt>, as opposed to 
     *   <tt>&lt;img src="..."&gt;</tt> (non-minimized, perfectly valid from the HTML 
     *   but not from the XML or XHTML standpoints).
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlNonMinimizedStandaloneElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    

    /**
     * <p>
     *   Called when the start of an open element (an <i>open tag</i>) is found. This
     *   "start" is considered to be the "<tt>&lt;</tt>" symbol the element starts with.
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlOpenElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the name of an open element (an <i>open tag</i>) is found. E.g., the
     *   "element name" of an element like <tt>&lt;div class="basic"&gt;</tt> would be "<tt>div</tt>".
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlOpenElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of an open element (an <i>open tag</i>) is found. This
     *   "end" is considered to be the "<tt>&gt;</tt>" symbol the element ends with.
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlOpenElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    

    
    
    
    /**
     * <p>
     *   Called when the start of a close element (a <i>close tag</i>) is found. This
     *   "start" is considered to be the "<tt>&lt;/</tt>" sequence the element starts with.
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of a close element (a <i>close tag</i>) is found. E.g., the
     *   "element name" of an element like <tt>&lt;/div&gt;</tt> would be "<tt>div</tt>".
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of a close element (a <i>close tag</i>) is found. This
     *   "end" is considered to be the "<tt>&gt;</tt>" symbol the element ends with.
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    

    
    
    
    /**
     * <p>
     *   Called when the start of an ignorable close element (or <i>close tag</i>) is found. This
     *   "start" is considered to be the "<tt>&lt;/</tt>" sequence the element starts with.
     * </p>
     * <p>
     *   An <i>ignorable close element</i> is an HTML-invalid <i>close element</i> that can appear 
     *   under two circumstances:
     * </p>
     * <ul>
     *   <li>Markup contains a <i>close element</i> that simply does not correspond to any previously
     *       open elements.</li>
     *   <li>Markup contains a <i>close element</i> that corresponds to a previously parsed
     *       <i>standalone element</i> (minimized or not).</li>
     * </ul>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlIgnorableCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of an ignorable close element (a <i>close tag</i>) is found. E.g., the
     *   "element name" of an element like <tt>&lt;/div&gt;</tt> would be "<tt>div</tt>".
     * </p>
     * <p>
     *   An <i>ignorable close element</i> is an HTML-invalid <i>close element</i> that can appear 
     *   under two circumstances:
     * </p>
     * <ul>
     *   <li>Markup contains a <i>close element</i> that simply does not correspond to any previously
     *       open elements.</li>
     *   <li>Markup contains a <i>close element</i> that corresponds to a previously parsed
     *       <i>standalone element</i> (minimized or not).</li>
     * </ul>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlIgnorableCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of an ignorable close element (a <i>close tag</i>) is found. This
     *   "end" is considered to be the "<tt>&gt;</tt>" symbol the element ends with.
     * </p>
     * <p>
     *   An <i>ignorable close element</i> is an HTML-invalid <i>close element</i> that can appear 
     *   under two circumstances:
     * </p>
     * <ul>
     *   <li>Markup contains a <i>close element</i> that simply does not correspond to any previously
     *       open elements.</li>
     *   <li>Markup contains a <i>close element</i> that corresponds to a previously parsed
     *       <i>standalone element</i> (minimized or not).</li>
     * </ul>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlIgnorableCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    

    
    
    
    /**
     * <p>
     *   Called when the start of a synthetic open element (an <i>open tag</i> that does not
     *   appear in markup but is needed in order to guarantee correct HTML structure) is generated. This
     *   "start" is considered to be the "<tt>&lt;</tt>" symbol the element starts with.
     * </p>
     * <p>
     *   A <i>synthetic open element</i> is an HTML <i>open element</i> that does not exist in
     *   the parsed markup, but is automatically generated in order to open an element when
     *   the HTML specifications determine it has to be open.
     * </p>
     * <p>
     *   For example, <tt>&lt;body&gt;</tt> elements inside <tt>&lt;html&gt;</tt> always
     *   require the presence of a <tt>&lt;head&gt;</tt> element before them, so one will be
     *   synthetically inserted if that does not happen.
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlSyntheticOpenElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of a synthetic open element (an <i>open tag</i> that does not
     *   appear in markup but is needed in order to guarantee correct HTML structure) is generated. E.g., the
     *   "element name" of an element like <tt>&lt;/div&gt;</tt> would be "<tt>div</tt>".
     * </p>
     * <p>
     *   A <i>synthetic open element</i> is an HTML <i>open element</i> that does not exist in
     *   the parsed markup, but is automatically generated in order to open an element when
     *   the HTML specifications determine it has to be open.
     * </p>
     * <p>
     *   For example, <tt>&lt;body&gt;</tt> elements inside <tt>&lt;html&gt;</tt> always
     *   require the presence of a <tt>&lt;head&gt;</tt> element before them, so one will be
     *   synthetically inserted if that does not happen.
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlSyntheticOpenElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of a synthetic open element (an <i>open tag</i> that does not
     *   appear in markup but is needed in order to guarantee correct HTML structure) is generated. This
     *   "end" is considered to be the "<tt>&gt;</tt>" symbol the element ends with.
     * </p>
     * <p>
     *   A <i>synthetic open element</i> is an HTML <i>open element</i> that does not exist in
     *   the parsed markup, but is automatically generated in order to open an element when
     *   the HTML specifications determine it has to be open.
     * </p>
     * <p>
     *   For example, <tt>&lt;body&gt;</tt> elements inside <tt>&lt;html&gt;</tt> always
     *   require the presence of a <tt>&lt;head&gt;</tt> element before them, so one will be
     *   synthetically inserted if that does not happen.
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlSyntheticOpenElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    

    
    
    
    /**
     * <p>
     *   Called when the start of a synthetic close element (a <i>close tag</i> that does not
     *   appear in markup but is needed in order to guarantee correct HTML structure) is generated. This
     *   "start" is considered to be the "<tt>&lt;</tt>" symbol the element starts with.
     * </p>
     * <p>
     *   A <i>synthetic close element</i> is an HTML <i>close element</i> that does not exist in
     *   the parsed markup, but is automatically generated in order to close an element when
     *   the HTML specifications determine it has to be closed.
     * </p>
     * <p>
     *   For example, <tt>&lt;p&gt;</tt> elements cannot be nested, and therefore if there
     *   are two <tt>&lt;p&gt;</tt> open elements at the same hierarchy level in sequence,
     *   a <tt>&lt;/p&gt;</tt> <i>forced close element</i> will be emitted before the second 
     *   <tt>&lt;p&gt;</tt> open element in order to close the first one. 
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlSyntheticCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of a synthetic close element (a <i>close tag</i> that does not
     *   appear in markup but is needed in order to guarantee correct HTML structure) is generated. E.g., the
     *   "element name" of an element like <tt>&lt;/div&gt;</tt> would be "<tt>div</tt>".
     * </p>
     * <p>
     *   A <i>synthetic close element</i> is an HTML <i>close element</i> that does not exist in
     *   the parsed markup, but is automatically generated in order to close an element when
     *   the HTML specifications determine it has to be closed.
     * </p>
     * <p>
     *   For example, <tt>&lt;p&gt;</tt> elements cannot be nested, and therefore if there
     *   are two <tt>&lt;p&gt;</tt> open elements at the same hierarchy level in sequence,
     *   a <tt>&lt;/p&gt;</tt> <i>forced close element</i> will be emitted before the second 
     *   <tt>&lt;p&gt;</tt> open element in order to close the first one. 
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlSyntheticCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of a synthetic close element (a <i>close tag</i> that does not
     *   appear in markup but is needed in order to guarantee correct HTML structure) is generated. This
     *   "end" is considered to be the "<tt>&gt;</tt>" symbol the element ends with.
     * </p>
     * <p>
     *   A <i>synthetic close element</i> is an HTML <i>close element</i> that does not exist in
     *   the parsed markup, but is automatically generated in order to close an element when
     *   the HTML specifications determine it has to be closed.
     * </p>
     * <p>
     *   For example, <tt>&lt;p&gt;</tt> elements cannot be nested, and therefore if there
     *   are two <tt>&lt;p&gt;</tt> open elements at the same hierarchy level in sequence,
     *   a <tt>&lt;/p&gt;</tt> <i>forced close element</i> will be emitted before the second 
     *   <tt>&lt;p&gt;</tt> open element in order to close the first one. 
     * </p>
     * <p>
     *   Artifacts are reported using the document <tt>buffer</tt> directly, and this buffer 
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <tt>len</tt> chars from the buffer <tt>char[]</tt> starting
     *   in <tt>offset</tt> or by creating a <tt>String</tt> from it using the same specification). 
     * </p>
     * <p>
     *   <b>Implementations of this handler should never modify the document buffer.</b> 
     * </p>
     * 
     * @param buffer the document buffer (not copied)
     * @param offset the offset (position in buffer) where the artifact starts.
     * @param len the length (in chars) of the artifact, starting in offset.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlSyntheticCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    


    
}
