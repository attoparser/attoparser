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
 *   that offer basic (non-detailed) reporting of elements (markup <i>tags</i>).
 * </p>
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public interface IBasicElementHandling {


    /**
     * <p>
     *   Called when a standalone element (a <i>minimized tag</i>) is found.
     * </p>
     * <p>
     *   This method reports the element as a whole, not splitting it among its different
     *   parts (element name, attributes). This splitting should normally be done by implementations
     *   of the {@link IDetailedElementHandling} interface (like {@link AbstractDetailedMarkupAttoHandler}).
     * </p>
     * <p>
     *   Two [offset, len] pairs are provided for two partitions (<i>outer</i> and <i>content</i>) 
     *   of the element:
     * </p>
     * <p>
     *   <tt>&lt;img src="/images/logo.png"/&gt;</tt><br />
     *   <tt><b>|[CONTENT-----------------]&nbsp;|</b></tt><br />
     *   <tt><b>[OUTER----------------------]</b></tt>
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
     * @param contentOffset offset for the <i>content</i> partition.
     * @param contentLen length of the <i>content</i> partition.
     * @param outerOffset offset for the <i>outer</i> partition.
     * @param outerLen length of the <i>outer</i> partition.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleStandaloneElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException;

    
    
    /**
     * <p>
     *   Called when an open element (an <i>open tag</i>) is found.
     * </p>
     * <p>
     *   This method reports the element as a whole, not splitting it among its different
     *   parts (element name, attributes). This splitting should normally be done by implementations
     *   of the {@link IDetailedElementHandling} interface (like {@link AbstractDetailedMarkupAttoHandler}).
     * </p>
     * <p>
     *   Two [offset, len] pairs are provided for two partitions (<i>outer</i> and <i>content</i>) 
     *   of the element:
     * </p>
     * <p>
     *   <tt>&lt;div class="main_section"&gt;</tt><br />
     *   <tt><b>|[CONTENT---------------]|</b></tt><br />
     *   <tt><b>[OUTER-------------------]</b></tt>
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
     * @param contentOffset offset for the <i>content</i> partition.
     * @param contentLen length of the <i>content</i> partition.
     * @param outerOffset offset for the <i>outer</i> partition.
     * @param outerLen length of the <i>outer</i> partition.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleOpenElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException;


    
    /**
     * <p>
     *   Called when a close element (a <i>close tag</i>) is found.
     * </p>
     * <p>
     *   This method reports the element as a whole, not splitting it among its different
     *   parts (element name). This splitting should normally be done by implementations
     *   of the {@link IDetailedElementHandling} interface (like {@link AbstractDetailedMarkupAttoHandler}).
     * </p>
     * <p>
     *   Two [offset, len] pairs are provided for two partitions (<i>outer</i> and <i>content</i>) 
     *   of the element:
     * </p>
     * <p>
     *   <tt>&lt;/div&gt;</tt><br />
     *   <tt><b>|&nbsp;[C]|</b></tt><br />
     *   <tt><b>[OUTE]</b></tt>
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
     * @param contentOffset offset for the <i>content</i> partition.
     * @param contentLen length of the <i>content</i> partition.
     * @param outerOffset offset for the <i>outer</i> partition.
     * @param outerLen length of the <i>outer</i> partition.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleCloseElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException;
    
}
