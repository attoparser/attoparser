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

/**
 * <p>
 *   Interface to be implemented by all handlers capable of receiving events about CDATA Sections.
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
public interface ICDATASectionHandler {


    /**
     * <p>
     *   Called when a CDATA section is found.
     * </p>
     * <p>
     *   Two [offset, len] pairs are provided for two partitions (<i>outer</i> and <i>content</i>):
     * </p>
     * <p>
     *   <kbd>&lt;![CDATA[ this is a CDATA section ]]&gt;</kbd><br>
     *   <kbd><b>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[CONTENT----------------]&nbsp;&nbsp;|</b></kbd><br>
     *   <kbd><b>[OUTER------------------------------]</b></kbd>
     * </p>
     * <p>
     *   Artifacts are reported using the document <kbd>buffer</kbd> directly, and this buffer
     *   should not be considered to be immutable, so reported structures should be copied if they need
     *   to be stored (either by copying <kbd>len</kbd> chars from the buffer <kbd>char[]</kbd> starting
     *   in <kbd>offset</kbd> or by creating a <kbd>String</kbd> from it using the same specification).
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
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException;

}