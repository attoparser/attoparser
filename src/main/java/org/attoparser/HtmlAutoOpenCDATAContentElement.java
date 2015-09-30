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


/*
 * Specialization of HtmlElement for HTML elements which body should not
 * be considered 'parseable', and therefore should be treated as CDATA
 * (instead of PCDATA). For example, <script> or <style> elements.
 *
 * These elements will disable parsing at the ParseStatus objects, until
 * their closing correspondent is found.
 * 
 * @author Daniel Fernandez
 * @since 2.0.0
 */
class HtmlAutoOpenCDATAContentElement extends HtmlCDATAContentElement {


    private final char[][] autoOpenParents;
    private final char[][] autoOpenLimits;


    public HtmlAutoOpenCDATAContentElement(final String name, final String[] autoOpenParents, final String[] autoOpenLimits) {

        super(name);

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

        if (autoOpenEnabled && !status.isAutoOpenCloseDone()) {
            status.setAutoOpenRequired(this.autoOpenParents, this.autoOpenLimits);
            return;
        }

        super.handleOpenElementStart(
                buffer, nameOffset, nameLen, line, col, handler, status, autoOpenEnabled, autoCloseEnabled);

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

        if (autoOpenEnabled && !status.isAutoOpenCloseDone()) {
            status.setAutoOpenRequired(this.autoOpenParents, this.autoOpenLimits);
            return;
        }

        super.handleStandaloneElementStart(
                buffer, nameOffset, nameLen, minimized, line, col, handler, status, autoOpenEnabled, autoCloseEnabled);

    }

    
}