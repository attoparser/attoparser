/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2014, The ATTOPARSER team (http://www.attoparser.org)
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
package org.attoparser;



/**
 * <p>
 *   Class used for reporting the status of current parsing operations to handlers.
 * </p>
 * <p>
 *   Instances of this class operate at a very low level, and are only useful in very specific scenarios,
 *   so most {@link org.attoparser.IMarkupHandler} implementations should just ignore its existence and
 *   consider it only for internal use.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class ParseStatus {

    int offset;
    int line;
    int col;
    boolean inStructure;

    boolean parsingDisabled;
    char[] parsingDisabledLimitSequence;

    boolean avoidStacking;

    char[][] autoCloseRequired;
    char[][] autoCloseLimits;
    boolean autoOpenCloseDone;



    ParseStatus() {
        super();
    }


    /**
     * <p>
     *   Determines whether parsing is currently disabled or not. This only happens if an event handler calls the
     *   {@link #setParsingDisabled(char[])} method. In such case, every Text event that will be reported until
     *   the specified limit sequence is found will return <tt>false</tt> for this method.
     * </p>
     *
     * @return whether parsing is currently disabled or not.
     */
    public boolean isParsingDisabled() {
        return this.parsingDisabled;
    }

    /**
     * <p>
     *   Disable parsing until the specified sequence is found in markup. All markup found between the event handler
     *   that calls this method and the limit sequence will be reported as Text.
     * </p>
     * <p>
     *   This is used by HTML parsers (like {@link org.attoparser.MarkupParser} itself, internally) in order to being
     *   able to correctly report HTML elements such as <tt>&lt;script&gt;</tt> or <tt>&lt;style&gt;</tt>, which bodies
     *   should not be parsed (they are <tt>CDATA</tt>).
     * </p>
     *
     * @param limitSequence the char sequence that, once found in markup, will enable parsing again.
     */
    public void setParsingDisabled(final char[] limitSequence) {
        this.parsingDisabledLimitSequence = limitSequence;
    }


    /**
     * <p>
     *   Indicates whether the parser has already performed a required auto-open or auto-close operation. This
     *   flag set to <tt>true</tt> means that these operations have already been performed and that the event is
     *   being relaunched after that (so the event can be propagated if needed).
     * </p>
     *
     * @return true if the auto-open and auto-close operations have already been performed.
     */
    public boolean isAutoOpenCloseDone() {
        return this.autoOpenCloseDone;
    }


    /**
     * <p>
     *   Force the parser to (possibly) perform a series of auto-close operations for elements that might be open
     *   at the moment in the element stack.
     * </p>
     * <p>
     *   The parser will auto-close all elements which names match one from the <tt>autoCloseRequired</tt> array,
     *   popping them from the stack until it finds an element with any of the names in the
     *   <tt>autoCloseLimits</tt> array.
     * </p>
     * <p>
     *   For example, when parsing HTML an open <tt>&lt;li&gt;</tt> will require closing all currently open
     *   <tt>&lt;li&gt;</tt>'s until an <tt>&lt;ul&gt;</tt> or <tt>&lt;ol&gt;</tt> is found.
     * </p>
     * <p>
     *   These flags will only be honored by the parser in <em>start</em> events for standalone or open elements, and
     *   after setting them the handler should never propagate the event to its delegate handler (if it exists),
     *   returning control back to the parser instead and letting the parser re-launch the event. When the event
     *   is re-launched, the parser will have set the <tt>autoOpenCloseDone</tt> flag to true, which can be
     *   checked with the {@link #isAutoOpenCloseDone()} method.
     * </p>
     *
     * @param autoCloseRequired the names of the elements that should be auto-closed.
     * @param autoCloseLimits the names of the elements that will serve as limits for the auto-closing operation.
     */
    public void setAutoCloseRequired(final char[][] autoCloseRequired, final char[][] autoCloseLimits) {
        this.autoCloseRequired = autoCloseRequired;
        this.autoCloseLimits = autoCloseLimits;
    }

    /**
     * <p>
     *   Indicate the parser whether the element being handled (in the <tt>start</tt> event of a standalone
     *   or open element) should be stacked or not.
     * </p>
     * <p>
     *   Minimized elements (e.g. &lt;hr /&gt;) will never be stacked, and it might happen that some open tags
     *   that represent HTML void elements should not be stacked either, like &lt;hr&gt;.
     * </p>
     * <p>
     *   This flag will only be honored by the parser in <em>start</em> events for standalone or open elements.
     * </p>
     *
     * @param avoidStacking whether the parser should avoid stacking this element or not.
     */
    public void setAvoidStacking(final boolean avoidStacking) {
        this.avoidStacking = avoidStacking;
    }

}
