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
 * Specialization of HtmlElement for HTML elements that will normally live inside a <body> element
 * and therefore will ask for its creation when it is not present when auto-open is enabled.
 * 
 * @author Daniel Fernandez
 * @since 2.0.0
 */
final class HtmlBodyElement extends HtmlAutoOpenCloseElement {

    private static final String[] ARRAY_HTML_BODY = new String[] { "html", "body" };
    private static final String[] ARRAY_HEAD = new String[] { "head" };
    private static final String[] AUTO_CLOSE_LIMITS = new String[] { "script", "template", "element", "decorator", "content", "shadow"};

    HtmlBodyElement(final String name) {
        super(name, ARRAY_HTML_BODY, null, ARRAY_HEAD, AUTO_CLOSE_LIMITS);
    }

}