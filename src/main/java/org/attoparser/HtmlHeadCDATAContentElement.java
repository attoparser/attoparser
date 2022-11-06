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
final class HtmlHeadCDATAContentElement extends HtmlAutoOpenCDATAContentElement {

    private static final String[] ARRAY_HTML_HEAD = new String[] { "html", "head" };

    public HtmlHeadCDATAContentElement(final String name) {
        super(name, ARRAY_HTML_HEAD, null);
    }

    
}