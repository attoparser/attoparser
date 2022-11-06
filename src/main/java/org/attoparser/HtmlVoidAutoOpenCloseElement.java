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


/*
 * Specialization of HtmlElement for VOID HTML elements that might require some open
 * elements to be closed before they are opened, and also might require some parent elements
 * to be opened as their own parents, as per the HTML specification.
 * For example, a <col> element will force the auto-close of a previous <thead> or <tbody> if
 * they are siblings, but will also require the opening of a <colgroup> if not inside one.
 * 
 * @author Daniel Fernandez
 * @since 2.0.0
 */
class HtmlVoidAutoOpenCloseElement extends HtmlVoidAutoCloseElement {


    private final char[][] autoOpenParents;
    private final char[][] autoOpenLimits;


    HtmlVoidAutoOpenCloseElement(final String name,
                                 final String[] autoOpenParents, final String[] autoOpenLimits,
                                 final String[] autoCloseElements, final String[] autoCloseLimits) {

        super(name, autoCloseElements, autoCloseLimits);

        if (autoOpenParents == null) {
            throw new IllegalArgumentException("The array of auto-open parents cannot be null");
        }

        final char[][] autoOpenParentsCharArray = new char[autoOpenParents.length][];
        for (int i = 0; i < autoOpenParentsCharArray.length; i++) {
            autoOpenParentsCharArray[i] = autoOpenParents[i].toCharArray();
        }

        final char[][] autoOpenLimitsCharArray;
        if (autoOpenLimits != null) {
            autoOpenLimitsCharArray = new char[autoOpenLimits.length][];
            for (int i = 0; i < autoOpenLimitsCharArray.length; i++) {
                autoOpenLimitsCharArray[i] = autoOpenLimits[i].toCharArray();
            }
        } else {
            autoOpenLimitsCharArray = null;
        }

        this.autoOpenParents = autoOpenParentsCharArray;
        this.autoOpenLimits = autoOpenLimitsCharArray;

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

        if ((autoOpenEnabled || autoCloseEnabled) && !status.isAutoOpenCloseDone()) {
            if (autoCloseEnabled) {
                status.setAutoCloseRequired(this.autoCloseRequired, this.autoCloseLimits);
            }
            if (autoOpenEnabled) {
                status.setAutoOpenRequired(this.autoOpenParents, this.autoOpenLimits);
            }
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

        if ((autoOpenEnabled || autoCloseEnabled) && !status.isAutoOpenCloseDone()) {
            if (autoCloseEnabled) {
                status.setAutoCloseRequired(this.autoCloseRequired, this.autoCloseLimits);
            }
            if (autoOpenEnabled) {
                status.setAutoOpenRequired(this.autoOpenParents, this.autoOpenLimits);
            }
            return;
        }

        handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);

    }


}