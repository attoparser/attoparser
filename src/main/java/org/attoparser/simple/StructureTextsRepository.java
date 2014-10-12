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
package org.attoparser.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        final Set<String> allStandardNamesSet = new HashSet<String>(STANDARD_ELEMENT_NAMES.length + STANDARD_ATTRIBUTE_NAMES.length + 1, 1.0f);
        allStandardNamesSet.addAll(Arrays.asList(STANDARD_ELEMENT_NAMES));
        allStandardNamesSet.addAll(Arrays.asList(STANDARD_ATTRIBUTE_NAMES));

        // Now sort them
        final List<String> allStandardNamesList = new ArrayList<String>(allStandardNamesSet);
        Collections.sort(allStandardNamesList);

        // Finally, populate the array
        ALL_STANDARD_NAMES = allStandardNamesList.toArray(new String[allStandardNamesList.size()]);

    }



    // This method will try to avoid creating new strings for each structure name (element/attribute)
    static String getStructureName(final char[] buffer, final int offset, final int len) {

        final int index = binarySearchString(false, ALL_STANDARD_NAMES, buffer, offset, len);
        if (index < 0) {
            return new String(buffer, offset, len);
        }

        return ALL_STANDARD_NAMES[index];

    }







    // This method was copied directly from the org.attoparser.TextUtil class so that we avoid making that class public
    static int compareTo(
            final boolean caseSensitive,
            final String text1, final int text1Offset, final int text1Len,
            final char[] text2, final int text2Offset, final int text2Len) {

        if (text1 == null) {
            throw new IllegalArgumentException("First text being compared cannot be null");
        }
        if (text2 == null) {
            throw new IllegalArgumentException("Second text buffer being compared cannot be null");
        }

        char c1, c2;

        int n = Math.min(text1Len, text2Len);
        int i = 0;

        while (n-- != 0) {

            c1 = text1.charAt(text1Offset + i);
            c2 = text2[text2Offset + i];

            if (c1 != c2) {

                if (caseSensitive) {
                    return c1 - c2;
                }

                c1 = Character.toUpperCase(c1);
                c2 = Character.toUpperCase(c2);

                if (c1 != c2) {
                    // We check both upper and lower case because that is how String#compareToIgnoreCase() is defined.
                    c1 = Character.toLowerCase(c1);
                    c2 = Character.toLowerCase(c2);
                    if (c1 != c2) {
                        return c1 - c2;
                    }

                }

            }

            i++;

        }

        return text1Len - text2Len;

    }



    // This method was copied directly from the org.attoparser.TextUtil class so that we avoid making that class public
    static int binarySearchString(
            final boolean caseSensitive, final String[] values, final char[] text, final int offset, final int len) {

        int low = 0;
        int high = values.length - 1;

        int mid, cmp;
        String midVal;

        while (low <= high) {

            mid = (low + high) >>> 1;
            midVal = values[mid];

            cmp = compareTo(caseSensitive, midVal, 0, midVal.length(), text, offset, len);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                // Found!!
                return mid;
            }

        }

        return -(low + 1);  // Not Found!! We return (-(insertion point) - 1), to guarantee all non-founds are < 0

    }




    private StructureTextsRepository() {
        super();
    }



}