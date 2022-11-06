/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2022, The ATTOPARSER team (https://www.attoparser.org)
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

/**
 * <p>
 *   Interface to be implemented by all handlers capable of receiving events about DOCTYPE clauses.
 * </p>
 * <p>
 *   Events in this interface are a part of the {@link IMarkupHandler} interface, the main handling interface in
 *   AttoParser.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 * @since 2.0.0
 * @see org.attoparser.IMarkupHandler
 *
 */
public interface IDocTypeHandler {



    /**
     * <p>
     *   Called when a DOCTYPE clause is found.
     * </p>
     * <p>
     *   This method reports the DOCTYPE clause splitting it into its different
     *   parts.
     * </p>
     * <p>
     *   Seven [offset, len] pairs are provided for seven partitions (<i>outer</i>,
     *   <i>keyword</i>, <i>elementName</i>, <i>type</i>, <i>publicId</i>,
     *   <i>systemId</i> and <i>internalSubset</i>) of the DOCTYPE clause:
     * </p>
     * <p>
     *   <tt>&lt;!DOCTYPE html PUBLIC ".........." ".........." [................]&gt;</tt><br>
     *   <tt><b>|&nbsp;[KEYWO]&nbsp;[EN]&nbsp;[TYPE]&nbsp;&nbsp;[PUBLICID]&nbsp;&nbsp;&nbsp;[SYSTEMID]&nbsp;&nbsp;&nbsp;[INTERNALSUBSET]&nbsp;|</b></tt><br>
     *   <tt><b>[OUTER------------------------------------------------------------]</b></tt>
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
     * @param keywordOffset offset for the <i>keyword</i> partition.
     * @param keywordLen length of the <i>keyword</i> partition.
     * @param keywordLine the line in the original document where the <i>keyword</i> partition starts.
     * @param keywordCol the column in the original document where the <i>keyword</i> partition starts.
     * @param elementNameOffset offset for the <i>elementName</i> partition.
     * @param elementNameLen length of the <i>elementName</i> partition.
     * @param elementNameLine the line in the original document where the <i>elementName</i> partition starts.
     * @param elementNameCol the column in the original document where the <i>elementName</i> partition starts.
     * @param typeOffset offset for the <i>type</i> partition.
     * @param typeLen length of the <i>type</i> partition.
     * @param typeLine the line in the original document where the <i>type</i> partition starts.
     * @param typeCol the column in the original document where the <i>type</i> partition starts.
     * @param publicIdOffset offset for the <i>publicId</i> partition.
     * @param publicIdLen length of the <i>publicId</i> partition.
     * @param publicIdLine the line in the original document where the <i>publicId</i> partition starts.
     * @param publicIdCol the column in the original document where the <i>publicId</i> partition starts.
     * @param systemIdOffset offset for the <i>systemId</i> partition.
     * @param systemIdLen length of the <i>systemId</i> partition.
     * @param systemIdLine the line in the original document where the <i>systemId</i> partition starts.
     * @param systemIdCol the column in the original document where the <i>systemId</i> partition starts.
     * @param internalSubsetOffset offset for the <i>internalSubsetId</i> partition.
     * @param internalSubsetLen length of the <i>internalSubsetId</i> partition.
     * @param internalSubsetLine the line in the original document where the <i>internalSubsetId</i> partition starts.
     * @param internalSubsetCol the column in the original document where the <i>internalSubsetId</i> partition starts.
     * @param outerOffset offset for the <i>outer</i> partition.
     * @param outerLen length of the <i>outer</i> partition.
     * @param outerLine the line in the original document where this artifact starts.
     * @param outerCol the column in the original document where this artifact starts.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleDocType(
            final char[] buffer,
            final int keywordOffset, final int keywordLen,
            final int keywordLine, final int keywordCol,
            final int elementNameOffset, final int elementNameLen,
            final int elementNameLine, final int elementNameCol,
            final int typeOffset, final int typeLen,
            final int typeLine, final int typeCol,
            final int publicIdOffset, final int publicIdLen,
            final int publicIdLine, final int publicIdCol,
            final int systemIdOffset, final int systemIdLen,
            final int systemIdLine, final int systemIdCol,
            final int internalSubsetOffset, final int internalSubsetLen,
            final int internalSubsetLine, final int internalSubsetCol,
            final int outerOffset, final int outerLen,
            final int outerLine, final int outerCol)
            throws ParseException;



}