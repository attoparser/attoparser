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
 *   Interface to be implemented by all handlers capable of receiving events about attribute sequences.
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
public interface IAttributeSequenceHandler {


    /**
     * <p>
     *   Called when an attribute is found.
     * </p>
     * <p>
     *   Three [offset, len] pairs are provided for three partitions (<i>name</i>,
     *   <i>operator</i>, <i>valueContent</i> and <i>valueOuter</i>):
     * </p>
     * <p>
     *   <kbd>class="basic_column"</kbd><br>
     *   <kbd><b>[NAM]*&nbsp;[VALUECONTE]|   (*) = [OPERATOR]</b></kbd><br>
     *   <kbd><b>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[VALUEOUTER--]</b></kbd><br>
     *   <kbd><b>[OUTER-------------]</b></kbd>
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
     * @param nameOffset offset for the <i>name</i> partition.
     * @param nameLen length of the <i>name</i> partition.
     * @param nameLine the line in the original document where the <i>name</i> partition starts.
     * @param nameCol the column in the original document where the <i>name</i> partition starts.
     * @param operatorOffset offset for the <i>operator</i> partition.
     * @param operatorLen length of the <i>operator</i> partition.
     * @param operatorLine the line in the original document where the <i>operator</i> partition starts.
     * @param operatorCol the column in the original document where the <i>operator</i> partition starts.
     * @param valueContentOffset offset for the <i>valueContent</i> partition.
     * @param valueContentLen length of the <i>valueContent</i> partition.
     * @param valueOuterOffset offset for the <i>valueOuter</i> partition.
     * @param valueOuterLen length of the <i>valueOuter</i> partition.
     * @param valueLine the line in the original document where the <i>value</i> (outer) partition starts.
     * @param valueCol the column in the original document where the <i>value</i> (outer) partition starts.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws ParseException;


    /**
     * <p>
     *   Called when an amount of white space is found inside an element.
     * </p>
     * <p>
     *   This attribute separators can contain any amount of whitespace, including
     *   line feeds:
     * </p>
     * <p>
     *   <kbd>&lt;div id="main"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;class="basic_column"&gt;</kbd><br>
     *   <kbd><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[INNWSP]</b></kbd><br>
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
     * @param offset offset for the artifact.
     * @param len length of the artifact.
     * @param line the line in the original document where the artifact starts.
     * @param col the column in the original document where the artifact starts.
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException;

}