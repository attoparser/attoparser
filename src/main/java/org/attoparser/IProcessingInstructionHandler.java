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
 *   Interface to be implemented by all handlers capable of receiving events about Processing Instructions.
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
public interface IProcessingInstructionHandler {


    /**
     * <p>
     *   Called when a Processing Instruction is found.
     * </p>
     * <p>
     *   Three [offset, len] pairs are provided for three partitions (<i>outer</i>,
     *   <i>target</i> and <i>content</i>):
     * </p>
     * <p>
     *   <kbd>&lt;?xls-stylesheet somePar1="a" somePar2="b"?&gt;</kbd><br>
     *   <kbd><b>|&nbsp;[TARGET------]&nbsp;[CONTENT----------------]&nbsp;|</b></kbd><br>
     *   <kbd><b>[OUTER-------------------------------------]</b></kbd>
     * </p>
     * <p>
     *   Note that, although XML Declarations have the same format as processing instructions,
     *   they are not considered as such and therefore are handled through a different handling
     *   method.
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
     * @param targetOffset offset for the <i>target</i> partition.
     * @param targetLen length of the <i>target</i> partition.
     * @param targetLine the line in the original document where the <i>target</i> partition starts.
     * @param targetCol the column in the original document where the <i>target</i> partition starts.
     * @param contentOffset offset for the <i>content</i> partition.
     * @param contentLen length of the <i>content</i> partition.
     * @param contentLine the line in the original document where the <i>content</i> partition starts.
     * @param contentCol the column in the original document where the <i>content</i> partition starts.
     * @param outerOffset offset for the <i>outer</i> partition.
     * @param outerLen length of the <i>outer</i> partition.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleProcessingInstruction(
            final char[] buffer,
            final int targetOffset, final int targetLen,
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException;

}