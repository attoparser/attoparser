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
 * Implementation of IHtmlElement for void HTML elements. Void elements are those
 * that should never have a body, and which can be specified in markup code both
 * as minimized standalone tags (<meta/>) or as open tags (<meta>) which are not really
 * open tags, but non-minimized standalone ones.
 *
 * These elements will redirect their 'open element' events to 'standalone element' events,
 * and also will consider any 'close element' events as UNMATCHED, because these elements
 * should never appear in closing form.
 *
 * @author Daniel Fernandez
 * @since 2.0.0
 *
 */
class HtmlVoidElement extends HtmlElement {

    
    
    
    public HtmlVoidElement(final String name) {
        super(name);
    }




    @Override
    public void handleStandaloneElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {

        status.setAvoidStacking(true);
        handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);

    }


    @Override
    public void handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {

        status.setAvoidStacking(true);
        handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, false, line, col);

    }

    
    @Override
    public void handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {

        handler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, false, line, col);

    }


    
    
    @Override
    public void handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {

        // Void elements have no closing tag, so these are always unmatched
        handler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);

    }

    
    @Override
    public void handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {

        // Void elements have no closing tag, so these are always unmatched
        handler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        
    }


}