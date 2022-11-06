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
package org.attoparser.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.attoparser.util.TextUtil;

/*
 * Repository class used for allowing the reuse of String objects by the SimplifierMarkupHandler class, so
 * that turning the char[] objects for element and attribute names into Strings is more efficient.
 *
 * @author Daniel Fernandez
 * @since 2.0.0
 */
public final class StructureTextsRepository {


    private static final String[] STANDARD_ATTRIBUTE_NAMES =
            new String[] {
                    "abbr", "accept", "accept-charset", "accesskey", "action",
                    "align", "alt", "archive", "autocomplete", "autofocus",
                    "autoplay", "axis", "border", "cellpadding", "cellspacing",
                    "challenge", "char", "charoff", "charset", "checked",
                    "cite", "class", "classid", "codebase", "codetype",
                    "cols", "colspan", "command", "content", "contenteditable",
                    "contextmenu", "controls", "coords", "data", "datetime",
                    "declare", "default", "defer", "dir", "disabled",
                    "draggable", "dropzone", "enctype", "for", "form",
                    "formaction", "formenctype", "formmethod", "formnovalidate", "formtarget",
                    "frame", "headers", "height", "hidden", "high",
                    "href", "hreflang", "http-equiv", "icon", "id",
                    "ismap", "keytype", "kind", "label", "lang",
                    "list", "longdesc", "loop", "low", "max",
                    "maxlength", "media", "method", "min", "multiple",
                    "muted", "name", "nohref", "novalidate", "onabort",
                    "onafterprint", "onbeforeprint", "onbeforeunload", "onblur", "oncanplay",
                    "oncanplaythrough", "onchange", "onclick", "oncontextmenu", "oncuechange",
                    "ondblclick", "ondrag", "ondragend", "ondragenter", "ondragleave",
                    "ondragover", "ondragstart", "ondrop", "ondurationchange", "onemptied",
                    "onended", "onerror", "onfocus", "onformchange", "onforminput",
                    "onhaschange", "oninput", "oninvalid", "onkeydown", "onkeypress",
                    "onkeyup", "onload", "onloadeddata", "onloadedmetadata", "onloadstart",
                    "onmessage", "onmousedown", "onmousemove", "onmouseout", "onmouseover",
                    "onmouseup", "onmousewheel", "onoffline", "ononline", "onpagehide",
                    "onpageshow", "onpause", "onplay", "onplaying", "onpopstate",
                    "onprogress", "onratechange", "onredo", "onreset", "onresize",
                    "onscroll", "onseeked", "onseeking", "onselect", "onstalled",
                    "onstorage", "onsubmit", "onsuspend", "ontimeupdate", "onundo",
                    "onunload", "onvolumechange", "onwaiting", "open", "optimum",
                    "pattern", "placeholder", "poster", "preload", "profile",
                    "radiogroup", "readonly", "rel", "required", "rev",
                    "rows", "rowspan", "rules", "scheme", "scope",
                    "selected", "shape", "size", "span", "spellcheck",
                    "src", "srclang", "standby", "style", "summary",
                    "tabindex", "title", "translate", "type", "usemap",
                    "valign", "value", "valuetype", "width", "xml:lang",
                    "xml:space", "xmlns"
            };

    private static final String[] STANDARD_ELEMENT_NAMES =
            new String[] {
                    "a", "abbr", "address", "area", "article",
                    "aside", "audio", "b", "base", "bdi",
                    "bdo", "blockquote", "body", "br", "button",
                    "canvas", "caption", "cite", "code", "col",
                    "colgroup", "command", "datalist", "dd", "del",
                    "details", "dfn", "dialog", "div", "dl",
                    "dt", "em", "embed", "fieldset", "figcaption",
                    "figure", "footer", "form", "g", "h1",
                    "h2", "h3", "h4", "h5", "h6",
                    "head", "header", "hgroup", "hr", "html",
                    "i", "iframe", "img", "input", "ins",
                    "kbd", "keygen", "label", "legend", "li",
                    "link", "main", "map", "mark", "menu",
                    "menuitem", "meta", "meter", "nav", "noscript",
                    "object", "ol", "optgroup", "option", "output",
                    "p", "param", "pre", "progress", "rb",
                    "rp", "rt", "rtc", "ruby", "s",
                    "samp", "script", "section", "select", "small",
                    "source", "span", "strong", "style", "sub",
                    "summary", "sup", "table", "tbody", "td",
                    "textarea", "tfoot", "th", "thead", "time",
                    "title", "tr", "track", "u", "ul",
                    "var", "video", "wbr"
            };


    private static final String[] ALL_STANDARD_NAMES;


    static {

        // First initialize a set to make sure there are no duplicates
        final Set<String> allStandardNamesSet = new HashSet<String>((STANDARD_ELEMENT_NAMES.length + STANDARD_ATTRIBUTE_NAMES.length + 1) * 2, 1.0f);
        allStandardNamesSet.addAll(Arrays.asList(STANDARD_ELEMENT_NAMES));
        allStandardNamesSet.addAll(Arrays.asList(STANDARD_ATTRIBUTE_NAMES));
        for (final String str : STANDARD_ELEMENT_NAMES) {
            allStandardNamesSet.add(str.toUpperCase());
        }
        for (final String str : STANDARD_ATTRIBUTE_NAMES) {
            allStandardNamesSet.add(str.toUpperCase());
        }

        // Now sort them
        final List<String> allStandardNamesList = new ArrayList<String>(allStandardNamesSet);
        Collections.sort(allStandardNamesList);

        // Finally, populate the array
        ALL_STANDARD_NAMES = allStandardNamesList.toArray(new String[allStandardNamesList.size()]);

    }



    // This method will try to avoid creating new strings for each structure name (element/attribute)
    static String getStructureName(final char[] buffer, final int offset, final int len) {

        final int index = TextUtil.binarySearch(true, ALL_STANDARD_NAMES, buffer, offset, len);
        if (index < 0) {
            return new String(buffer, offset, len);
        }

        return ALL_STANDARD_NAMES[index];

    }







    private StructureTextsRepository() {
        super();
    }



}