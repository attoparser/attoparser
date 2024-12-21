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

    boolean shouldDisableParsing; // This is meant to be modified only inside CDATA elements (disabling can depend on an attribute)
    boolean parsingDisabled;
    char[] parsingDisabledLimitSequence;

    boolean avoidStacking;


    // These attributes instruct the event processor to make sure an element is correctly stacked inside the elements
    // it needs to. For example, a <tr> element will ask for the auto-opening of a <tbody> element as its
    // parent, but it will specify as limits also <thead> and <tfoot> because these two elements are also valid
    // parents for it (just not default).
    // The limits array will be used for specifying: if not null, the IMMEDIATE parents that will be considered
    // valid. If not null, that the parent element sequence should only be considered from the document root, and
    // that it should be completed if something is missing (e.g. there is <html> but no <body>).
    // When in HTML, the auto-open elements will be:
    //
    //   * <tr>
    //     RULE: if (parent != <tbody> &amp;&amp; parent != <tfoot> &amp;&amp; parent != <thead>) -> AUTO-OPEN <tbody>
    //     PARENTS: (tbody) LIMITS: (tbody,tfoot,thead)
    //
    //   * <col>
    //     RULE: if (parent != <colgroup>) -> AUTO-OPEN <colgroup>
    //     PARENTS: (colgroup) LIMITS: (colgroup)
    //
    //   * <meta>, <link>, <script>, <style>, <template>, <base>, <object>
    //     RULE: if (parent == null) -> AUTO-OPEN <html>, <head>
    //           if (parent == html &amp;&amp; html.parent == null) -> AUTO-OPEN <head>
    //     PARENTS: (html,head) LIMITS: (null)
    //
    //   * All other standard HTML tags
    //     RULE: if (parent == null) -> AUTO-OPEN <html>, <body>
    //           if (parent == html &amp;&amp; html.parent == null) -> AUTO-OPEN <body>
    //     PARENTS: (html,body) LIMITS: (null)
    //
    char[][] autoOpenParents;
    char[][] autoOpenLimits;

    // These two attributes instruct the event processor to make sure certain elements are closed before an
    // open/standalone start event is actually fired. This avoids incorrect stacking of elements that cannot appear
    // inside the currently open ones. For example, an <li> element will ask for the auto-closing of every currently
    // open <li> element. This possible parent <li> element will be searched in the stack from the currently open
    // element (because the last open element could be a <b>, an <a>, etc.) and it will be closed (along with any
    // other open elements that have been open after it (i.e. those <b>, <a>, etc.). Note also that some "limits"
    // are established so that, in this same example, we stop searching the stack for that open <li> as soon as we
    // find an <ul> or <ol> in the stack. This allows correctly stacking of <ul> or <ol> inside an <li> of a
    // higher-level <ul> or <ol>.
    char[][] autoCloseRequired;
    char[][] autoCloseLimits;

    // This flag indicates whether the auto-open and auto-close operations have already been done, so that the
    // firing events know that they don't need to stop the execution chain again.
    boolean autoOpenCloseDone;



    /**
     * <p>
     *   Builds a new instance of this class.
     * </p>
     * <p>
     *   This constructor is for internal use. As a general rule of thumb, there is no reason why any user of this
     *   class would need to call this constructor.
     * </p>
     */
    public ParseStatus() {
        super();
    }


    /**
     * <p>
     *   Returns the line in the document the parser is currently located at.
     * </p>
     * <p>
     *   Note this should not be used for event reference, because the parser cursor might be ahead of the events
     *   it is reporting. In order to know the lines and cols an event was found at, use the <kbd>(line,col)</kbd>
     *   pairs reported with every event handler.
     * </p>
     *
     * @return the line number.
     */
    public int getLine() {
        return this.line;
    }


    /**
     * <p>
     *   Returns the column in the current line in the document the parser is currently located at.
     * </p>
     * <p>
     *   Note this should not be used for event reference, because the parser cursor might be ahead of the events
     *   it is reporting. In order to know the lines and cols an event was found at, use the <kbd>(line,col)</kbd>
     *   pairs reported with every event handler.
     * </p>
     *
     * @return the column number.
     */
    public int getCol() {
        return this.col;
    }



    /**
     * <p>
     *   Determines whether parsing is currently disabled or not. This only happens if an event handler calls the
     *   {@link #setParsingDisabled(char[])} method. In such case, every Text event that will be reported until
     *   the specified limit sequence is found will return <kbd>false</kbd> for this method.
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
     *   able to correctly report HTML elements such as <kbd>&lt;script&gt;</kbd> or <kbd>&lt;style&gt;</kbd>, which bodies
     *   should not be parsed (they are <kbd>CDATA</kbd>).
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
     *   flag set to <kbd>true</kbd> means that these operations have already been performed and that the event is
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
     *   Force the parser to (possibly) perform a series of auto-open operations for elements that should be
     *   considered parents of the one being open at a specific moment per the markup spec (made for HTML).
     * </p>
     * <p>
     *   These attributes instruct the event processor to make sure an element is correctly stacked inside the elements
     *   it needs to. For example, a <kbd>&lt;tr&gt;</kbd> element will ask for the auto-opening of a <kbd>&lt;tbody&gt;</kbd> element as its
     *   parent, but it will specify as limits also <kbd>&lt;thead&gt;</kbd> and <kbd>&lt;tfoot&gt;</kbd> because these two elements are also valid
     *   parents for it (just not default).
     * </p>
     * <p>
     *   The limits array will be used for specifying: if not null, the IMMEDIATE parents that will be considered
     *   valid. If not null, that the parent element sequence should only be considered from the document root, and
     *   that it should be completed if something is missing (e.g. there is <kbd>&lt;html&gt;</kbd> but no <kbd>&lt;body&gt;</kbd>).
     * </p>
     * <p>
     *   When in HTML, the auto-open elements will be:
     * </p>
     * <ul>
     *   <li>
     *      <kbd>&lt;tr&gt;</kbd><br>
     *      RULE: if (parent != <kbd>&lt;tbody&gt;</kbd> &amp;&amp; parent != <kbd>&lt;tfoot&gt;</kbd> &amp;&amp; parent != <kbd>&lt;thead&gt;</kbd>) : AUTO-OPEN <kbd>&lt;tbody&gt;</kbd><br>
     *      PARENTS: (tbody) LIMITS: (tbody,tfoot,thead)
     *   </li>
     *
     *   <li>
     *      <kbd>&lt;col&gt;</kbd><br>
     *      RULE: if (parent != <kbd>&lt;colgroup&gt;</kbd>) : AUTO-OPEN <kbd>&lt;colgroup&gt;</kbd><br>
     *      PARENTS: (colgroup) LIMITS: (colgroup)
     *   </li>
     *
     *   <li>
     *      <kbd>&lt;meta&gt;</kbd>, <kbd>&lt;link&gt;</kbd>, <kbd>&lt;script&gt;</kbd>, <kbd>&lt;style&gt;</kbd>, <kbd>&lt;template&gt;</kbd>, <kbd>&lt;base&gt;</kbd>, <kbd>&lt;object&gt;</kbd><br>
     *      RULE: if (parent == null) : AUTO-OPEN <kbd>&lt;html&gt;</kbd>, <kbd>&lt;head&gt;</kbd><br>
     *            if (parent == <kbd>&lt;html&gt;</kbd> &amp;&amp; <kbd>&lt;html&gt;</kbd>.parent == null) : AUTO-OPEN <kbd>&lt;head&gt;</kbd><br>
     *      PARENTS: (html,head) LIMITS: (null)
     *   </li>
     *
     *   <li>
     *      All other standard HTML tags<br>
     *      RULE: if (parent == null) : AUTO-OPEN <kbd>&lt;html&gt;</kbd>, <kbd>&lt;body&gt;</kbd><br>
     *            if (parent == <kbd>&lt;html&gt;</kbd> &amp;&amp; <kbd>&lt;html&gt;</kbd>.parent == null) : AUTO-OPEN <kbd>&lt;body&gt;</kbd><br>
     *      PARENTS: (html,body) LIMITS: (null)
     *   </li>
     * </ul>
     *
     * @param autoOpenParents the parent sequence to be (potentially) auto-open.
     * @param autoOpenLimits the names of the elements that will serve as limits for the auto-open operation. If null,
     *                       the parent sequence will only be applied if at root level, or of the sequence is incomplete.
     */
    public void setAutoOpenRequired(final char[][] autoOpenParents, final char[][] autoOpenLimits) {
        this.autoOpenParents = autoOpenParents;
        this.autoOpenLimits = autoOpenLimits;
    }


    /**
     * <p>
     *   Force the parser to (possibly) perform a series of auto-close operations for elements that might be open
     *   at the moment in the element stack.
     * </p>
     * <p>
     *   The parser will auto-close all elements which names match one from the <kbd>autoCloseRequired</kbd> array,
     *   popping them from the stack until it finds an element with any of the names in the
     *   <kbd>autoCloseLimits</kbd> array.
     * </p>
     * <p>
     *   For example, when parsing HTML an open <kbd>&lt;li&gt;</kbd> will require closing all currently open
     *   <kbd>&lt;li&gt;</kbd>'s until an <kbd>&lt;ul&gt;</kbd> or <kbd>&lt;ol&gt;</kbd> is found.
     * </p>
     * <p>
     *   These flags will only be honored by the parser in <em>start</em> events for standalone or open elements, and
     *   after setting them the handler should never propagate the event to its delegate handler (if it exists),
     *   returning control back to the parser instead and letting the parser re-launch the event. When the event
     *   is re-launched, the parser will have set the <kbd>autoOpenCloseDone</kbd> flag to true, which can be
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
     *   Indicate the parser whether the element being handled (in the <kbd>start</kbd> event of a standalone
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
