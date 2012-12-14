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
import org.attoparser.markup.html.warnings.HtmlParsingEventWarnings;


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
     *   Called when the start of a standalone element is found.
     * </p>
     * <p>
     *   A standalone element can be either a minimized tag or not. 
     *   For example: <tt>&lt;img src="..." /&gt;</tt> is minimized (self-closed), as opposed to 
     *   <tt>&lt;img src="..."&gt;</tt> (non-minimized, perfectly valid from the HTML 
     *   but not from the XML or XHTML standpoints).
     * </p>
     * <p>
     *   The reported warnings explain the issues found with this element
     *   during parsing (misplacement, bad nesting, etc.) 
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
     * @param nameOffset the offset (position in buffer) where the element name appears.
     * @param nameLen the length (in chars) of the element name.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @param minimized whether the tag representing this element is minimized (self-closed) or not.
     * @param warnings the list of warnings applicable to the parsing of this element.
     * @throws AttoParseException
     */
    public void handleHtmlStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final boolean minimized,
            final HtmlParsingEventWarnings warnings)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of a standalone element is found.
     * </p>
     * 
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @param minimized whether the tag representing this element is minimized (self-closed) or not.
     * @throws AttoParseException
     */
    public void handleHtmlStandaloneElementEnd(
            final int line, final int col,
            final boolean minimized)
            throws AttoParseException;

    
    

    /**
     * <p>
     *   Called when the start of an open element (an <i>open tag</i>) is found.
     * </p>
     * <p>
     *   The reported warnings explain the issues found with this element
     *   during parsing (misplacement, bad nesting, etc.) 
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
     * @param nameOffset the offset (position in buffer) where the element name appears.
     * @param nameLen the length (in chars) of the element name.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @param warnings the list of warnings applicable to the parsing of this element.
     * @throws AttoParseException
     */
    public void handleHtmlOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final HtmlParsingEventWarnings warnings)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of an open element (an <i>open tag</i>) is found.
     * </p>
     * 
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlOpenElementEnd(
            final int line, final int col)
            throws AttoParseException;

    

    
    
    
    /**
     * <p>
     *   Called when the start of a close element (a <i>close tag</i>) is found.
     * </p>
     * <p>
     *   The reported warnings explain the issues found with this element
     *   during parsing (misplacement, bad nesting, etc.) 
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
     * @param nameOffset the offset (position in buffer) where the element name appears.
     * @param nameLen the length (in chars) of the element name.
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @param warnings the list of warnings applicable to the parsing of this element.
     * @throws AttoParseException
     */
    public void handleHtmlCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final HtmlParsingEventWarnings warnings)
            throws AttoParseException;
    
    /**
     * <p>
     *   Called when the end of a close element (a <i>close tag</i>) is found.
     * </p>
     * 
     * @param line the line in the original document where this artifact starts.
     * @param col the column in the original document where this artifact starts.
     * @throws AttoParseException
     */
    public void handleHtmlCloseElementEnd(
            final int line, final int col)
            throws AttoParseException;
    


    
}
