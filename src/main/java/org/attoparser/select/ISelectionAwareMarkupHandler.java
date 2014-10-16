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
package org.attoparser.select;

import org.attoparser.IMarkupHandler;

/**
 * <p>
 *   Specialization of the {@link org.attoparser.IMarkupHandler} interface that allows a handler to receive information
 *   from the {@link org.attoparser.select.BlockSelectorMarkupHandler} and
 *   {@link org.attoparser.select.NodeSelectorMarkupHandler} handler implementations about what selectors are
 *   active for a specific event.
 * </p>
 * <p>
 *   This enables the handler to take specific actions depending on the selectors that are currently matching the
 *   received events.
 * </p>
 * <p>
 *   An example implementation of this interface is {@link org.attoparser.select.AttributeMarkingSelectedMarkupHandler},
 *   which uses the information received through this interface's methods to add a certain attribute to markup
 *   elements that explain why (by means of what selector) they were selected.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public interface ISelectionAwareMarkupHandler extends IMarkupHandler {


    /**
     * <p>
     *   Specifies the selectors that are going to be checked by the previous handler (normally a
     *   {@link org.attoparser.select.BlockSelectorMarkupHandler} or a
     *   {@link org.attoparser.select.NodeSelectorMarkupHandler} implementation).
     * </p>
     * <p>
     *   This method passes <em>all</em> the available selectors, and is only called once during parsing,
     *   before any other event handler is called.
     * </p>
     * <p>
     *   Note the specified array should not be modified in any form, as this could have undesirable effects.
     * </p>
     *
     * @param selectors the selectors that are going to be used for matching.
     */
    public void setSelectors(final String[] selectors);


    /**
     * <p>
     *   Specifies the selectors that will match the following events, according to the previous handler
     *   (normally a {@link org.attoparser.select.BlockSelectorMarkupHandler} or a
     *   {@link org.attoparser.select.NodeSelectorMarkupHandler} implementation).
     * </p>
     * <p>
     *   This method will be called before the event handling methods that correspond with matches in one or
     *   several of the established selectors, so that the event handlers of these selectors can use that
     *   information.
     * </p>
     * <p>
     *   For example, if an &lt;img&gt; tag matches a <tt>"//li"</tt> selector, this
     *   {@link #setCurrentSelection(boolean[])} method will be called before the corresponding
     *   {@link #handleStandaloneElementStart(char[], int, int, boolean, int, int)} specifying that
     *   <tt>"//li"</tt> is now matched. Also, after
     *   {@link #handleStandaloneElementEnd(char[], int, int, boolean, int, int)} has been called,
     *   {@link #setCurrentSelection(boolean[])} will be called again with a <tt>null</tt>
     *   argument in order to reset the current selection flags.
     * </p>
     *
     * @param currentSelection the current selection, i.e. the selectors that now match. This is specified
     *                         as a <tt>boolean[]</tt> referencing the <tt>String[]</tt> previously specified
     *                         in {@link #setSelectors(String[])}, with each position being <tt>true</tt> if
     *                         that selector matches, or <tt>false</tt> if not.
     */
    public void setCurrentSelection(final boolean[] currentSelection);


}