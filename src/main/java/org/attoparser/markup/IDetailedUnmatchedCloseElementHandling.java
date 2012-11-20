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
package org.attoparser.markup;

import org.attoparser.AttoParseException;


/**
 * <p>
 *   Handler feature interface to be implemented by {@link org.attoparser.IAttoHandler} implementations
 *   that offer detailed reporting of unmatched <i>close element</i> artifacts (those that have
 *   no corresponding previous <i>open element</i> artifacts).
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public interface IDetailedUnmatchedCloseElementHandling extends IDetailedElementHandling {
    
    
    
    /**
     * <p>
     *   Called when the start of an unmatched close element (<i>close tag</i>) is found. This
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
    public void handleUnmatchedCloseElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;

    /**
     * <p>
     *   Called when the name of an unmatched close element (<i>close tag</i>) is found. E.g., the
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
    public void handleUnmatchedCloseElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of an unmatched close element (<i>close tag</i>) is found. This
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
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;


    
}
