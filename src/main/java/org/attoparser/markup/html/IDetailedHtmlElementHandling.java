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
import org.attoparser.markup.IAttributeSequenceHandling;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public interface IDetailedHtmlElementHandling extends IAttributeSequenceHandling {

    

    /**
     * <p>
     *   Called when the start of a standalone element (a <i>minimized tag</i>) is found. This
     *   "start" is considered to be the "<tt>&lt;</tt>" symbol the element starts with.
     * </p>
     * <p>
     *   An example: <tt>&lt;input type="text" /&gt;</tt>
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
    public void handleHtmlStandaloneElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of a standalone element (a <i>minimized tag</i>) is found. E.g., the
     *   "element name" of an element like <tt>&lt;img src="images/logo.png" /&gt;</tt> would be 
     *   "<tt>img</tt>".
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
    public void handleHtmlStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of a standalone element (a <i>minimized tag</i>) is found. This
     *   "end" is considered to be the "<tt>/&gt;</tt>" sequence the element ends with.
     * </p>
     * <p>
     *   An example: <tt>&lt;input type="text" /&gt;</tt>
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
    public void handleHtmlStandaloneElementEnd(
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
     *   An unclosed standalone element looks like this: <tt>&lt;input type="text"&gt;</tt>. This
     *   element could be written as <tt>&lt;input type="text" /&gt;</tt>, but instead is left
     *   unclosed.
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
     *   "element name" of an element like <tt>&lt;img src="images/logo.png" &gt;</tt> would be 
     *   "<tt>img</tt>".
     * </p>
     * <p>
     *   An unclosed standalone element looks like this: <tt>&lt;input type="text"&gt;</tt>. This
     *   element could be written as <tt>&lt;input type="text" /&gt;</tt>, but instead is left
     *   unclosed.
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
     *   An unclosed standalone element looks like this: <tt>&lt;input type="text"&gt;</tt>. This
     *   element could be written as <tt>&lt;input type="text" /&gt;</tt>, but instead is left
     *   unclosed.
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
     *   Called when the start of a close element (a <i>close tag</i>) is needed for
     *   balancing an unclosed tag. This
     *   "start" is considered to be the "<tt>&lt;/</tt>" sequence the element starts with.
     * </p>
     * <p>
     *   HTML-specific intelligence should be used in order to determine when an element should be closed, instead
     *   of basing this decision on mere hierarchy as done by {@link AbstractDetailedMarkupAttoHandler}.
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
    public void handleHtmlBalancedCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of a close element (a <i>close tag</i>) is needed for
     *   balancing an unclosed tag. E.g., the
     *   "element name" of an element like <tt>&lt;/div&gt;</tt> would be "<tt>div</tt>".
     * </p>
     * <p>
     *   HTML-specific intelligence should be used in order to determine when an element should be closed, instead
     *   of basing this decision on mere hierarchy as done by {@link AbstractDetailedMarkupAttoHandler}.
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
    public void handleHtmlBalancedCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of a close element (a <i>close tag</i>) is needed for
     *   balancing an unclosed tag. This
     *   "end" is considered to be the "<tt>&gt;</tt>" symbol the element ends with.
     * </p>
     * <p>
     *   HTML-specific intelligence should be used in order to determine when an element should be closed, instead
     *   of basing this decision on mere hierarchy as done by {@link AbstractDetailedMarkupAttoHandler}.
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
    public void handleHtmlBalancedCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;



    
}
