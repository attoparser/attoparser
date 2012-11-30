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
     *   Called when the start of a closed standalone element is found. This
     *   "start" is considered to be the "<tt>&lt;</tt>" symbol the element starts with.
     * </p>
     * <p>
     *   A <i>closed standalone element</i> is a standalone (i.e. <i>no body, no end tag</i>) element
     *   that is well-formed from the XML standpoint. For example: <tt>&lt;img src="..." /&gt;</tt> 
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
    public void handleHtmlClosedStandaloneElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of a closed standalone element is found. E.g., the
     *   "element name" of an element like <tt>&lt;img src="images/logo.png" /&gt;</tt> would be 
     *   "<tt>img</tt>".
     * </p>
     * <p>
     *   A <i>closed standalone element</i> is a standalone (i.e. <i>no body, no end tag</i>) element
     *   that is well-formed from the XML standpoint. For example: <tt>&lt;img src="..." /&gt;</tt> 
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
    public void handleHtmlClosedStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of a standalone element is found. This
     *   "end" is considered to be the "<tt>/&gt;</tt>" sequence the element ends with.
     * </p>
     * <p>
     *   A <i>closed standalone element</i> is a standalone (i.e. <i>no body, no end tag</i>) element
     *   that is well-formed from the XML standpoint. For example: <tt>&lt;img src="..." /&gt;</tt> 
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
    public void handleHtmlClosedStandaloneElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;


    
    

    /**
     * <p>
     *   Called when the start of an unclosed standalone element (a tag that should have no
     *   body but is not minimized) is found. This
     *   "start" is considered to be the "<tt>&lt;</tt>" symbol the element starts with.
     * </p>
     * <p>
     *   A <i>unclosed standalone element</i> is a standalone (i.e. <i>no body, no end tag</i>) element
     *   that is not well-formed from the XML standpoint because it isn't closed. 
     *   For example: <tt>&lt;img src="..."&gt;</tt>. In HTML, this is valid and completely equivalent to
     *   <tt>&lt;img src="..." /&gt;</tt> because the HTML specifications say the <tt>&lt;img&gt;</tt>
     *   tag cannot have a body.
     * </p>
     * <p>
     *   Whenever parsing finds a <i>close element</i> that corresponds to an <i>unclosed standalone 
     *   element</i> (like for example, <tt>&lt;/img&gt;</tt>, it is reported as an <i>unmatched close
     *   element</i>, because it is considered ill-formed HTML code and not a real <i>body closer</i>.
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
    public void handleHtmlUnclosedStandaloneElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of an unclosed standalone element (a tag that should have no
     *   body but is not minimized) is found. E.g., the
     *   "element name" of an element like <tt>&lt;img src="images/logo.png"&gt;</tt> would be 
     *   "<tt>img</tt>".
     * </p>
     * <p>
     *   A <i>unclosed standalone element</i> is a standalone (i.e. <i>no body, no end tag</i>) element
     *   that is not well-formed from the XML standpoint because it isn't closed. 
     *   For example: <tt>&lt;img src="..."&gt;</tt>. In HTML, this is valid and completely equivalent to
     *   <tt>&lt;img src="..." /&gt;</tt> because the HTML specifications say the <tt>&lt;img&gt;</tt>
     *   tag cannot have a body.
     * </p>
     * <p>
     *   Whenever parsing finds a <i>close element</i> that corresponds to an <i>unclosed standalone 
     *   element</i> (like for example, <tt>&lt;/img&gt;</tt>, it is reported as an <i>unmatched close
     *   element</i>, because it is considered ill-formed HTML code and not a real <i>body closer</i>.
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
    public void handleHtmlUnclosedStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of an unclosed standalone element (a tag that should have no
     *   body but is not minimized) is found. This
     *   "end" is considered to be the "<tt>&gt;</tt>" symbol the element ends with.
     * </p>
     * <p>
     *   A <i>unclosed standalone element</i> is a standalone (i.e. <i>no body, no end tag</i>) element
     *   that is not well-formed from the XML standpoint because it isn't closed. 
     *   For example: <tt>&lt;img src="..."&gt;</tt>. In HTML, this is valid and completely equivalent to
     *   <tt>&lt;img src="..." /&gt;</tt> because the HTML specifications say the <tt>&lt;img&gt;</tt>
     *   tag cannot have a body.
     * </p>
     * <p>
     *   Whenever parsing finds a <i>close element</i> that corresponds to an <i>unclosed standalone 
     *   element</i> (like for example, <tt>&lt;/img&gt;</tt>, it is reported as an <i>unmatched close
     *   element</i>, because it is considered ill-formed HTML code and not a real <i>body closer</i>.
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
    public void handleHtmlUnclosedStandaloneElementEnd(
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
     *   Called when the start of an unmatched close element (or <i>close tag</i>) is found. This
     *   "start" is considered to be the "<tt>&lt;</tt>" symbol the element starts with.
     * </p>
     * <p>
     *   An <i>unmatched close element</i> is an HTML-invalid <i>close element</i> that can appear 
     *   under two circumstances:
     * </p>
     * <ul>
     *   <li>Markup contains a <i>close element</i> that simply does not correspond to any previously
     *       open elements.</li>
     *   <li>Markup contains a <i>close element</i> that corresponds to a previously parsed
     *       <i>unclosed standalone element</i>.</li>
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
    public void handleHtmlUnmatchedCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of an unmatched close element (a <i>close tag</i>) is found. E.g., the
     *   "element name" of an element like <tt>&lt;/div&gt;</tt> would be "<tt>div</tt>".
     * </p>
     * <p>
     *   An <i>unmatched close element</i> is an HTML-invalid <i>close element</i> that can appear 
     *   under two circumstances:
     * </p>
     * <ul>
     *   <li>Markup contains a <i>close element</i> that simply does not correspond to any previously
     *       open elements.</li>
     *   <li>Markup contains a <i>close element</i> that corresponds to a previously parsed
     *       <i>unclosed standalone element</i>.</li>
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
    public void handleHtmlUnmatchedCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of an unmatched close element (a <i>close tag</i>) is found. This
     *   "end" is considered to be the "<tt>&gt;</tt>" symbol the element ends with.
     * </p>
     * <p>
     *   An <i>unmatched close element</i> is an HTML-invalid <i>close element</i> that can appear 
     *   under two circumstances:
     * </p>
     * <ul>
     *   <li>Markup contains a <i>close element</i> that simply does not correspond to any previously
     *       open elements.</li>
     *   <li>Markup contains a <i>close element</i> that corresponds to a previously parsed
     *       <i>unclosed standalone element</i>.</li>
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
    public void handleHtmlUnmatchedCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    

    
    
    
    /**
     * <p>
     *   Called when the start of a forced close element (a <i>close tag</i> that does not
     *   appear in markup but is needed) is found. This
     *   "start" is considered to be the "<tt>&lt;</tt>" symbol the element starts with.
     * </p>
     * <p>
     *   A <i>forced close element</i> is an HTML <i>close element</i> that does not exist in
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
    public void handleHtmlForcedCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of a forced close element (a <i>close tag</i> that does not
     *   appear in markup but is needed) is found. E.g., the
     *   "element name" of an element like <tt>&lt;/div&gt;</tt> would be "<tt>div</tt>".
     * </p>
     * <p>
     *   A <i>forced close element</i> is an HTML <i>close element</i> that does not exist in
     *   the parsed markup, but is automatically generated in order to close an element when
     *   the HTML specifications determine it has to be closed.
     * </p>
     * <p>
     *   For example, <tt>&lt;p&gt;</tt> elements cannot be nested, and therefore if there
     *   are two <tt>&lt;p&gt;</tt> open elements at the same hierarchy level in sequence,
     *   a <tt>&lt;/p&gt;</tt> <i>forced close element</i> will be emitted before the second 
     *   <tt>&lt;p&gt;</tt> open element in order to close the first one. 
     * </p>
     * <ul>
     *   <li>Markup contains a <i>close element</i> that simply does not correspond to any previously
     *       open elements.</li>
     *   <li>Markup contains a <i>close element</i> that corresponds to a previously parsed
     *       <i>unclosed standalone element</i>.</li>
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
    public void handleHtmlForcedCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of a forced close element (a <i>close tag</i> that does not
     *   appear in markup but is needed) is found. This
     *   "end" is considered to be the "<tt>&gt;</tt>" symbol the element ends with.
     * </p>
     * <p>
     *   A <i>forced close element</i> is an HTML <i>close element</i> that does not exist in
     *   the parsed markup, but is automatically generated in order to close an element when
     *   the HTML specifications determine it has to be closed.
     * </p>
     * <p>
     *   For example, <tt>&lt;p&gt;</tt> elements cannot be nested, and therefore if there
     *   are two <tt>&lt;p&gt;</tt> open elements at the same hierarchy level in sequence,
     *   a <tt>&lt;/p&gt;</tt> <i>forced close element</i> will be emitted before the second 
     *   <tt>&lt;p&gt;</tt> open element in order to close the first one. 
     * </p>
     * <ul>
     *   <li>Markup contains a <i>close element</i> that simply does not correspond to any previously
     *       open elements.</li>
     *   <li>Markup contains a <i>close element</i> that corresponds to a previously parsed
     *       <i>unclosed standalone element</i>.</li>
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
    public void handleHtmlForcedCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    


    
}
