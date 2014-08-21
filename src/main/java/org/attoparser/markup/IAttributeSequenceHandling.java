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
 *   that offer detailed reporting of markup attributes.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public interface IAttributeSequenceHandling {

    
    
    /**
     * <p>
     *   Called when an attribute is found.
     * </p>
     * <p>
     *   Three [offset, len] pairs are provided for three partitions (<i>name</i>,
     *   <i>operator</i>, <i>valueContent</i> and <i>valueOuter</i>):
     * </p>
     * <p>
     *   <tt>class="basic_column"</tt><br />
     *   <tt><b>[NAM]*&nbsp;[VALUECONTE]|   (*) = [OPERATOR]</b></tt><br />
     *   <tt><b>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[VALUEOUTER--]</b></tt><br />
     *   <tt><b>[OUTER-------------]</b></tt>
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
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    public IAttoHandleResult handleAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException;

    
    /**
     * <p>
     *   Called when an amount of white space is found inside an element.
     * </p>
     * <p>
     *   This attribute separators can contain any amount of whitespace, including
     *   line feeds:
     * </p>
     * <p>
     *   <tt>&lt;div id="main"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;class="basic_column"&gt;</tt><br />
     *   <tt><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[ATTSEP]</b></tt><br />
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
     * @param offset offset for the artifact.
     * @param len length of the artifact.
     * @param line the line in the original document where the artifact starts.
     * @param col the column in the original document where the artifact starts.
     * @return the result of handling the event, or null if no relevant result has to be returned.
     * @throws AttoParseException
     */
    public IAttoHandleResult handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException;
    
}
