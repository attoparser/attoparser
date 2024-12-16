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


/*
 * Specialization of HtmlElement for void HTML elements which also require some open
 * elements to be closed before they appear, as per the HTML specification.
 * For example, an <hr> element will force the auto-close of a previous <p> if
 * they are siblings.
 * 
 * @author Daniel Fernandez
 * @since 2.0.0
 *
 */
class HtmlVoidAutoCloseElement extends HtmlVoidElement {


    protected final char[][] autoCloseRequired;
    protected final char[][] autoCloseLimits;


    HtmlVoidAutoCloseElement(final String name, final String[] autoCloseElements, final String[] autoCloseLimits) {

        super(name);

        if (autoCloseElements == null) {
            throw new IllegalArgumentException("The array of auto-close elements cannot be null");
        }

        final char[][] autoCloseElementsCharArray = new char[autoCloseElements.length][];
        for (int i = 0; i < autoCloseElementsCharArray.length; i++) {
            autoCloseElementsCharArray[i] = autoCloseElements[i].toCharArray();
        }

        final char[][] autoCloseLimitsCharArray;
        if (autoCloseLimits != null) {
            autoCloseLimitsCharArray = new char[autoCloseLimits.length][];
            for (int i = 0; i < autoCloseLimitsCharArray.length; i++) {
                autoCloseLimitsCharArray[i] = autoCloseLimits[i].toCharArray();
            }
        } else {
            autoCloseLimitsCharArray = null;
        }

        this.autoCloseRequired = autoCloseElementsCharArray;
        this.autoCloseLimits = autoCloseLimitsCharArray;

    }




    @Override
    public void handleOpenElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status,
            final boolean autoOpenEnabled, final boolean autoCloseEnabled)
            throws ParseException {

        status.setAvoidStacking(true);

        if (autoCloseEnabled && !status.isAutoOpenCloseDone()) {
            status.setAutoCloseRequired(this.autoCloseRequired, this.autoCloseLimits);
            return;
        }

        handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, false, line, col);

    }



    @Override
    public void handleStandaloneElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status,
            final boolean autoOpenEnabled, final boolean autoCloseEnabled)
            throws ParseException {

        status.setAvoidStacking(true);

        if (autoCloseEnabled && !status.isAutoOpenCloseDone()) {
            status.setAutoCloseRequired(this.autoCloseRequired, this.autoCloseLimits);
            return;
        }

        handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);

    }

    
    
}