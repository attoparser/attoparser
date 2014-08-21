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
import org.attoparser.IAttoHandleResult;


/**
 * <p>
 *   Handler feature interface to be implemented by {@link org.attoparser.IAttoHandler} implementations
 *   that offer basic (non-detailed) reporting of DOCTYPE clauses.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public interface IBasicDocTypeHandling {

    /**
     * <p>
     *   Called when a DOCTYPE clause is found.
     * </p>
     * <p>
     *   This method reports the DOCTYPE clause as a whole, not splitting it into its different
     *   parts (root element name, publicId, etc.). This splitting should normally be done by implementations
     *   of the {@link IDetailedDocTypeHandling} interface (like {@link AbstractDetailedMarkupAttoHandler}).
     * </p>
     * <p>
     *   Two [offset, len] pairs are provided for two partitions (<i>outer</i> and <i>content</i>) 
     *   of the DOCTYPE clause:
     * </p>
     * <p>
     *   <tt>&lt;!DOCTYPE html PUBLIC "..." "..."&gt;</tt><br />
     *   <tt><b>|&nbsp;[CONTENT----------------------]|</b></tt><br />
     *   <tt><b>[OUTER---------------------------]</b></tt>
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
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    public IAttoHandleResult handleDocType(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException;
    
}
