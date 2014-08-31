/*
 * =============================================================================
 * 
 *   Copyright (c) 2012, The ATTOPARSER team (http://www.attoparser.org)
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
package org.attoparser.markup.html.elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.attoparser.util.SegmentedArray;
import org.attoparser.util.SegmentedArray.IValueHandler;







/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class HtmlElements {

    static final int CASE_DIFF = ('a' - 'A');

    private static final int ELEMENTS_SEGMENT_SIZE = (('z' - 'a') + 1);
    private static final SegmentedArray<IHtmlElement, String> ELEMENTS =
            new SegmentedArray<IHtmlElement, String>(IHtmlElement.class, new ElementValueHandler(), ELEMENTS_SEGMENT_SIZE);


    // Set containing all the standard element names, for posible external reference
    public static final Set<String> ALL_ELEMENT_NAMES;
    // Set containing all the standard attribute names, for posible external reference
    public static final Set<String> ALL_ATTRIBUTE_NAMES;


    // Root
    public static final IHtmlElement HTML = new BasicHtmlElement("html");
    
    // Document metadata
    public static final IHtmlElement HEAD = new BasicHtmlElement("head");
    public static final IHtmlElement TITLE = new BasicHtmlElement("title");
    public static final IHtmlElement BASE = new StandaloneHtmlElement("base");
    public static final IHtmlElement LINK = new StandaloneHtmlElement("link");
    public static final IHtmlElement META = new StandaloneHtmlElement("meta");
    public static final IHtmlElement STYLE = new CdataContentHtmlElement("style");
    
    // Scripting
    public static final IHtmlElement SCRIPT = new CdataContentHtmlElement("script");
    public static final IHtmlElement NOSCRIPT = new BasicHtmlElement("noscript");
    
    // Sections
    public static final IHtmlElement BODY = new BasicHtmlElement("body");
    public static final IHtmlElement ARTICLE = new BasicHtmlElement("article");
    public static final IHtmlElement SECTION = new BasicHtmlElement("section");
    public static final IHtmlElement NAV = new BasicHtmlElement("nav");
    public static final IHtmlElement ASIDE = new BasicHtmlElement("aside");
    public static final IHtmlElement H1 = new BasicHtmlElement("h1");
    public static final IHtmlElement H2 = new BasicHtmlElement("h2");
    public static final IHtmlElement H3 = new BasicHtmlElement("h3");
    public static final IHtmlElement H4 = new BasicHtmlElement("h4");
    public static final IHtmlElement H5 = new BasicHtmlElement("h5");
    public static final IHtmlElement H6 = new BasicHtmlElement("h6");
    public static final IHtmlElement HGROUP = new BasicHtmlElement("hgroup");
    public static final IHtmlElement HEADER = new BasicHtmlElement("header");
    public static final IHtmlElement FOOTER = new BasicHtmlElement("footer");
    public static final IHtmlElement ADDRESS = new BasicHtmlElement("address");
    public static final IHtmlElement MAIN = new BasicHtmlElement("main");

    // Grouping content
    public static final IHtmlElement P = new BasicHtmlElement("p");
    public static final IHtmlElement HR = new StandaloneHtmlElement("hr");
    public static final IHtmlElement PRE = new BasicHtmlElement("pre");
    public static final IHtmlElement BLOCKQUOTE = new BasicHtmlElement("blockquote");
    public static final IHtmlElement OL = new BasicHtmlElement("ol");
    public static final IHtmlElement UL = new BasicHtmlElement("ul");
    public static final IHtmlElement LI = new BasicHtmlElement("li");
    public static final IHtmlElement DL = new BasicHtmlElement("dl");
    public static final IHtmlElement DT = new BasicHtmlElement("dt");
    public static final IHtmlElement DD = new BasicHtmlElement("dd");
    public static final IHtmlElement FIGURE = new BasicHtmlElement("figure");
    public static final IHtmlElement FIGCAPTION = new BasicHtmlElement("figcaption");
    public static final IHtmlElement DIV = new BasicHtmlElement("div");
    
    // Text-level semantics
    public static final IHtmlElement A = new BasicHtmlElement("a");
    public static final IHtmlElement EM = new BasicHtmlElement("em");
    public static final IHtmlElement STRONG = new BasicHtmlElement("strong");
    public static final IHtmlElement SMALL = new BasicHtmlElement("small");
    public static final IHtmlElement S = new BasicHtmlElement("s");
    public static final IHtmlElement CITE = new BasicHtmlElement("cite");
    public static final IHtmlElement G = new BasicHtmlElement("g");
    public static final IHtmlElement DFN = new BasicHtmlElement("dfn");
    public static final IHtmlElement ABBR = new BasicHtmlElement("abbr");
    public static final IHtmlElement TIME = new BasicHtmlElement("time");
    public static final IHtmlElement CODE = new BasicHtmlElement("code");
    public static final IHtmlElement VAR = new BasicHtmlElement("var");
    public static final IHtmlElement SAMP = new BasicHtmlElement("samp");
    public static final IHtmlElement KBD = new BasicHtmlElement("kbd");
    public static final IHtmlElement SUB = new BasicHtmlElement("sub");
    public static final IHtmlElement SUP = new BasicHtmlElement("sup");
    public static final IHtmlElement I = new BasicHtmlElement("i");
    public static final IHtmlElement B = new BasicHtmlElement("b");
    public static final IHtmlElement U = new BasicHtmlElement("u");
    public static final IHtmlElement MARK = new BasicHtmlElement("mark");
    public static final IHtmlElement RUBY = new BasicHtmlElement("ruby");
    public static final IHtmlElement RT = new BasicHtmlElement("rt");
    public static final IHtmlElement RP = new BasicHtmlElement("rp");
    public static final IHtmlElement BDI = new BasicHtmlElement("bdi");
    public static final IHtmlElement BDO = new BasicHtmlElement("bdo");
    public static final IHtmlElement SPAN = new BasicHtmlElement("span");
    public static final IHtmlElement BR = new StandaloneHtmlElement("br");
    public static final IHtmlElement WBR = new BasicHtmlElement("wbr");
    
    // Edits
    public static final IHtmlElement INS = new BasicHtmlElement("ins");
    public static final IHtmlElement DEL = new BasicHtmlElement("del");
    
    // Embedded content
    public static final IHtmlElement IMG = new StandaloneHtmlElement("img");
    public static final IHtmlElement IFRAME = new BasicHtmlElement("iframe");
    public static final IHtmlElement EMBED = new BasicHtmlElement("embed");
    public static final IHtmlElement OBJECT = new BasicHtmlElement("object");
    public static final IHtmlElement PARAM = new StandaloneHtmlElement("param");
    public static final IHtmlElement VIDEO = new BasicHtmlElement("video");
    public static final IHtmlElement AUDIO = new BasicHtmlElement("audio");
    public static final IHtmlElement SOURCE = new BasicHtmlElement("source");
    public static final IHtmlElement TRACK = new BasicHtmlElement("track");
    public static final IHtmlElement CANVAS = new BasicHtmlElement("canvas");
    public static final IHtmlElement MAP = new BasicHtmlElement("map");
    public static final IHtmlElement AREA = new StandaloneHtmlElement("area");
    
    // Tabular data
    public static final IHtmlElement TABLE = new BasicHtmlElement("table");
    public static final IHtmlElement CAPTION = new BasicHtmlElement("caption");
    public static final IHtmlElement COLGROUP = new BasicHtmlElement("colgroup");
    public static final IHtmlElement COL = new StandaloneHtmlElement("col");
    public static final IHtmlElement TBODY = new BasicHtmlElement("tbody");
    public static final IHtmlElement THEAD = new BasicHtmlElement("thead");
    public static final IHtmlElement TFOOT = new BasicHtmlElement("tfoot");
    public static final IHtmlElement TR = new BasicHtmlElement("tr");
    public static final IHtmlElement TD = new BasicHtmlElement("td");
    public static final IHtmlElement TH = new BasicHtmlElement("th");
    
    // Forms
    public static final IHtmlElement FORM = new BasicHtmlElement("form");
    public static final IHtmlElement FIELDSET = new BasicHtmlElement("fieldset");
    public static final IHtmlElement LEGEND = new BasicHtmlElement("legend");
    public static final IHtmlElement LABEL = new BasicHtmlElement("label");
    public static final IHtmlElement INPUT = new StandaloneHtmlElement("input");
    public static final IHtmlElement BUTTON = new BasicHtmlElement("button");
    public static final IHtmlElement SELECT = new BasicHtmlElement("select");
    public static final IHtmlElement DATALIST = new BasicHtmlElement("datalist");
    public static final IHtmlElement OPTGROUP = new BasicHtmlElement("optgroup");
    public static final IHtmlElement OPTION = new BasicHtmlElement("option");
    public static final IHtmlElement TEXTAREA = new BasicHtmlElement("textarea");
    public static final IHtmlElement KEYGEN = new BasicHtmlElement("keygen");
    public static final IHtmlElement OUTPUT = new BasicHtmlElement("output");
    public static final IHtmlElement PROGRESS = new BasicHtmlElement("progress");
    public static final IHtmlElement METER = new BasicHtmlElement("meter");
    
    // Interactive elements
    public static final IHtmlElement DETAILS = new BasicHtmlElement("details");
    public static final IHtmlElement SUMMARY = new BasicHtmlElement("summary");
    public static final IHtmlElement COMMAND = new BasicHtmlElement("command");
    public static final IHtmlElement MENU = new BasicHtmlElement("menu");
    public static final IHtmlElement MENUITEM = new BasicHtmlElement("menuitem");
    public static final IHtmlElement DIALOG = new BasicHtmlElement("dialog");
    
    

    static {

        ALL_ELEMENT_NAMES =
                Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
                new String[] {
                    HTML.getName(), HEAD.getName(), TITLE.getName(), BASE.getName(), LINK.getName(), META.getName(),
                    STYLE.getName(), SCRIPT.getName(), NOSCRIPT.getName(), BODY.getName(), ARTICLE.getName(),
                    SECTION.getName(), NAV.getName(), ASIDE.getName(), H1.getName(), H2.getName(), H3.getName(),
                    H4.getName(), H5.getName(), H6.getName(), HGROUP.getName(), HEADER.getName(), FOOTER.getName(),
                    ADDRESS.getName(), P.getName(), HR.getName(), PRE.getName(), BLOCKQUOTE.getName(), OL.getName(),
                    UL.getName(), LI.getName(), DL.getName(), DT.getName(), DD.getName(), FIGURE.getName(),
                    FIGCAPTION.getName(), DIV.getName(), A.getName(), EM.getName(), STRONG.getName(), SMALL.getName(),
                    S.getName(), CITE.getName(), G.getName(), DFN.getName(), ABBR.getName(), TIME.getName(),
                    CODE.getName(), VAR.getName(), SAMP.getName(), KBD.getName(), SUB.getName(), SUP.getName(),
                    I.getName(), B.getName(), U.getName(), MARK.getName(), RUBY.getName(), RT.getName(),
                    RP.getName(), BDI.getName(), BDO.getName(), SPAN.getName(), BR.getName(), WBR.getName(),
                    INS.getName(), DEL.getName(), IMG.getName(), IFRAME.getName(), EMBED.getName(), OBJECT.getName(),
                    PARAM.getName(), VIDEO.getName(), AUDIO.getName(), SOURCE.getName(), TRACK.getName(),
                    CANVAS.getName(), MAP.getName(), AREA.getName(), TABLE.getName(), CAPTION.getName(),
                    COLGROUP.getName(), COL.getName(), TBODY.getName(), THEAD.getName(), TFOOT.getName(), TR.getName(),
                    TD.getName(), TH.getName(), FORM.getName(), FIELDSET.getName(), LEGEND.getName(), LABEL.getName(),
                    INPUT.getName(), BUTTON.getName(), SELECT.getName(), DATALIST.getName(), OPTGROUP.getName(),
                    OPTION.getName(), TEXTAREA.getName(), KEYGEN.getName(), OUTPUT.getName(), PROGRESS.getName(),
                    METER.getName(), DETAILS.getName(), SUMMARY.getName(), COMMAND.getName(), MENU.getName(),
                    MENUITEM.getName(), DIALOG.getName(), MAIN.getName()
                })));


        ALL_ATTRIBUTE_NAMES =
                Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
                new String[] {
                        "abbr", "accept", "accept-charset", "accesskey", "action", "align", "alt", "archive",
                        "autocomplete", "autofocus", "autoplay", "axis", "border", "cellpadding", "cellspacing",
                        "challenge", "char", "charoff", "charset", "checked", "cite", "class", "classid",
                        "codebase", "codetype", "cols", "colspan", "command", "content", "contenteditable",
                        "contextmenu", "controls", "coords", "data", "datetime", "declare", "default",
                        "defer", "dir", "disabled", "draggable", "dropzone", "enctype", "for", "form",
                        "formaction", "formenctype", "formmethod", "formnovalidate", "formtarget",
                        "frame", "headers", "height", "hidden", "high", "href", "hreflang", "http-equiv",
                        "icon", "id", "ismap", "keytype", "kind", "label", "lang", "list", "longdesc",
                        "loop", "low", "max", "maxlength", "media", "method", "min", "multiple", "muted",
                        "name", "nohref", "novalidate", "onabort", "onafterprint", "onbeforeprint",
                        "onbeforeunload", "onblur", "oncanplay", "oncanplaythrough", "onchange",
                        "onclick", "oncontextmenu", "oncuechange", "ondblclick", "ondrag", "ondragend",
                        "ondragenter", "ondragleave", "ondragover", "ondragstart", "ondrop",
                        "ondurationchange", "onemptied", "onended", "onerror", "onfocus",
                        "onformchange", "onforminput", "onhaschange", "oninput", "oninvalid", "onkeydown",
                        "onkeypress", "onkeyup", "onload", "onloadeddata", "onloadedmetadata",
                        "onloadstart", "onmessage", "onmousedown", "onmousemove", "onmouseout", "onmouseover",
                        "onmouseup", "onmousewheel", "onoffline", "ononline", "onpagehide", "onpageshow",
                        "onpause", "onplay", "onplaying", "onpopstate", "onprogress", "onratechange",
                        "onredo", "onreset", "onresize", "onscroll", "onseeked", "onseeking",
                        "onselect", "onstalled", "onstorage", "onsubmit", "onsuspend", "ontimeupdate",
                        "onundo", "onunload", "onvolumechange", "onwaiting", "open", "optimum", "pattern",
                        "placeholder", "poster", "preload", "profile", "radiogroup", "readonly", "rel",
                        "required", "rev", "rows", "rowspan", "rules", "scheme", "scope", "selected",
                        "shape", "size", "span", "spellcheck", "src", "srclang", "standby", "style", "summary",
                        "tabindex", "title", "translate", "type", "usemap", "valign", "value", "valuetype",
                        "width", "xml:lang", "xml:space", "xmlns"
                })));


    }
    
    
    public static IHtmlElement lookFor(final String elementName) {
        if (elementName == null) {
            throw new IllegalArgumentException("Element name cannot be null");
        }
        return ELEMENTS.searchByText(elementName);
    }
    
    
    public static IHtmlElement lookFor(final char[] elementName) {
        if (elementName == null) {
            throw new IllegalArgumentException("Element name cannot be null");
        }
        return lookFor(elementName, 0, elementName.length);
    }
    
    
    public static IHtmlElement lookFor(final char[] elementNameBuffer, final int offset, final int len) {
        if (elementNameBuffer == null) {
            throw new IllegalArgumentException("Buffer cannot be null");
        }
        return ELEMENTS.searchByText(elementNameBuffer, offset, len);
    }
    
    
    
    static void registerElement(final IHtmlElement element) {
        ELEMENTS.registerValue(element);
    }

    
    

    
    
    private HtmlElements() {
        super();
    }
    
    

    
    private static final class ElementValueHandler implements IValueHandler<IHtmlElement, String> {
        
        /*
         * Class is private, only used here. No need to validate arguments.
         */
        
        ElementValueHandler() {
            super();
        }
        
        
        public String getKey(final IHtmlElement value) {
            return value.getName();
        }


        public int getSegment(final IHtmlElement value) {
            return getSegmentByKey(getKey(value));
        }

        public int getSegmentByKey(final String key) {
            return getSegmentByText(key);
        }

        public int getSegmentByText(final String text) {
            return getSegmentByChar(text.charAt(0));
        }

        public int getSegmentByText(final char[] textBuffer, final int textOffset, final int textLen) {
            return getSegmentByChar(textBuffer[textOffset]);
        }


        private int getSegmentByChar(final char c) {
            if (c >= 'A' && c <= 'Z') {
                // We want lower- and upper-case to be together in the same segment in order
                // to implement case-insensitivity
                return c + CASE_DIFF;
            }
            return c;
        }
        
        public boolean matchesByKey(final IHtmlElement value, final String key) {
            return matchesByText(value, key);
        }

        public boolean matchesByText(final IHtmlElement value, final String text) {
            return value.matches(text);
        }

        public boolean matchesByText(final IHtmlElement value, final char[] textBuffer,
                final int textOffset, final int textLen) {
            return value.matches(textBuffer, textOffset, textLen);
        }
        
    }
    
    
    
    
}