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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.attoparser.util.TextUtil;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class HtmlElements {

    private static final ElementRepository ELEMENTS = new ElementRepository();


    // Set containing all the standard elements, for posible external reference
    public static final Set<IHtmlElement> ALL_STANDARD_ELEMENTS;
    // Set containing all the standard element names, for posible external reference
    public static final Set<String> ALL_STANDARD_ELEMENT_NAMES;
    // Set containing all the standard attribute names, for posible external reference
    public static final Set<String> ALL_STANDARD_ATTRIBUTE_NAMES;


    // Root
    public static final IHtmlElement HTML = new BasicHtmlElement("html");
    
    // Document metadata
    public static final IHtmlElement HEAD = new BasicHtmlElement("head");
    public static final IHtmlElement TITLE = new BasicHtmlElement("title");
    public static final IHtmlElement BASE = new VoidHtmlElement("base");
    public static final IHtmlElement LINK = new VoidHtmlElement("link");
    public static final IHtmlElement META = new VoidHtmlElement("meta");
    public static final IHtmlElement STYLE = new CdataContentHtmlElement("style");
    
    // Scripting
    public static final IHtmlElement SCRIPT = new CdataContentHtmlElement("script");
    public static final IHtmlElement NOSCRIPT = new BasicHtmlElement("noscript");
    
    // Sections
    public static final IHtmlElement BODY = new BasicHtmlElement("body");
    public static final IHtmlElement ARTICLE = new AutoCloserHtmlElement("article", new String[] { "p" }, null);
    public static final IHtmlElement SECTION = new AutoCloserHtmlElement("section", new String[] { "p" }, null);
    public static final IHtmlElement NAV = new AutoCloserHtmlElement("nav", new String[] { "p" }, null);
    public static final IHtmlElement ASIDE = new AutoCloserHtmlElement("aside", new String[] { "p" }, null);
    public static final IHtmlElement H1 = new AutoCloserHtmlElement("h1", new String[] { "p" }, null);
    public static final IHtmlElement H2 = new AutoCloserHtmlElement("h2", new String[] { "p" }, null);
    public static final IHtmlElement H3 = new AutoCloserHtmlElement("h3", new String[] { "p" }, null);
    public static final IHtmlElement H4 = new AutoCloserHtmlElement("h4", new String[] { "p" }, null);
    public static final IHtmlElement H5 = new AutoCloserHtmlElement("h5", new String[] { "p" }, null);
    public static final IHtmlElement H6 = new AutoCloserHtmlElement("h6", new String[] { "p" }, null);
    public static final IHtmlElement HGROUP = new AutoCloserHtmlElement("hgroup", new String[] { "p" }, null);
    public static final IHtmlElement HEADER = new AutoCloserHtmlElement("header", new String[] { "p" }, null);
    public static final IHtmlElement FOOTER = new AutoCloserHtmlElement("footer", new String[] { "p" }, null);
    public static final IHtmlElement ADDRESS = new AutoCloserHtmlElement("address", new String[] { "p" }, null);
    public static final IHtmlElement MAIN = new AutoCloserHtmlElement("main", new String[] { "p" }, null);

    // Grouping content
    public static final IHtmlElement P = new AutoCloserHtmlElement("p", new String[] { "p" }, null);
    public static final IHtmlElement HR = new VoidAutoCloserHtmlElement("hr", new String[] { "p" }, null);
    public static final IHtmlElement PRE = new AutoCloserHtmlElement("pre", new String[] { "p" }, null);
    public static final IHtmlElement BLOCKQUOTE = new AutoCloserHtmlElement("blockquote", new String[] { "p" }, null);
    public static final IHtmlElement OL = new AutoCloserHtmlElement("ol", new String[] { "p" }, null);
    public static final IHtmlElement UL = new AutoCloserHtmlElement("ul", new String[] { "p" }, null);
    public static final IHtmlElement LI = new AutoCloserHtmlElement("li", new String[] { "li" }, new String[] { "ul", "ol" });
    public static final IHtmlElement DL = new AutoCloserHtmlElement("dl", new String[] { "p" }, null);
    public static final IHtmlElement DT = new AutoCloserHtmlElement("dt", new String[] { "dt", "dd" }, new String[] { "dl" });
    public static final IHtmlElement DD = new AutoCloserHtmlElement("dd", new String[] { "dt", "dd" }, new String[] { "dl" });
    public static final IHtmlElement FIGURE = new BasicHtmlElement("figure");
    public static final IHtmlElement FIGCAPTION = new BasicHtmlElement("figcaption");
    public static final IHtmlElement DIV = new AutoCloserHtmlElement("div", new String[] { "p" }, null);
    
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
    public static final IHtmlElement RB = new AutoCloserHtmlElement("rb", new String[] { "rb", "rt", "rtc", "rp" }, new String[] { "ruby" });
    public static final IHtmlElement RT = new AutoCloserHtmlElement("rt", new String[] { "rb", "rt", "rp" }, new String[] { "ruby", "rtc" });
    public static final IHtmlElement RTC = new AutoCloserHtmlElement("rtc", new String[] { "rb", "rt", "rtc", "rp" }, new String[] { "ruby" });
    public static final IHtmlElement RP = new AutoCloserHtmlElement("rp", new String[] { "rb", "rt", "rp" }, new String[] { "ruby", "rtc" });
    public static final IHtmlElement BDI = new BasicHtmlElement("bdi");
    public static final IHtmlElement BDO = new BasicHtmlElement("bdo");
    public static final IHtmlElement SPAN = new BasicHtmlElement("span");
    public static final IHtmlElement BR = new VoidHtmlElement("br");
    public static final IHtmlElement WBR = new VoidHtmlElement("wbr");

    // Edits
    public static final IHtmlElement INS = new BasicHtmlElement("ins");
    public static final IHtmlElement DEL = new BasicHtmlElement("del");
    
    // Embedded content
    public static final IHtmlElement IMG = new VoidHtmlElement("img");
    public static final IHtmlElement IFRAME = new BasicHtmlElement("iframe");
    public static final IHtmlElement EMBED = new VoidHtmlElement("embed");
    public static final IHtmlElement OBJECT = new BasicHtmlElement("object");
    public static final IHtmlElement PARAM = new VoidHtmlElement("param");
    public static final IHtmlElement VIDEO = new BasicHtmlElement("video");
    public static final IHtmlElement AUDIO = new BasicHtmlElement("audio");
    public static final IHtmlElement SOURCE = new VoidHtmlElement("source");
    public static final IHtmlElement TRACK = new VoidHtmlElement("track");
    public static final IHtmlElement CANVAS = new BasicHtmlElement("canvas");
    public static final IHtmlElement MAP = new BasicHtmlElement("map");
    public static final IHtmlElement AREA = new VoidHtmlElement("area");
    
    // Tabular data
    public static final IHtmlElement TABLE = new AutoCloserHtmlElement("table", new String[] { "p" }, null);
    public static final IHtmlElement CAPTION = new AutoCloserHtmlElement("caption", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    public static final IHtmlElement COLGROUP = new AutoCloserHtmlElement("colgroup", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    public static final IHtmlElement COL = new VoidHtmlElement("col");
    public static final IHtmlElement TBODY = new AutoCloserHtmlElement("tbody", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    public static final IHtmlElement THEAD = new AutoCloserHtmlElement("thead", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    public static final IHtmlElement TFOOT = new AutoCloserHtmlElement("tfoot", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    public static final IHtmlElement TR = new AutoCloserHtmlElement("tr", new String[] { "tr", "caption", "colgroup" }, new String[] { "table", "thead", "tbody", "tfoot" });
    public static final IHtmlElement TD = new AutoCloserHtmlElement("td", new String[] { "td", "th" }, new String[] { "tr" });
    public static final IHtmlElement TH = new AutoCloserHtmlElement("th", new String[] { "td", "th" }, new String[] { "tr" });
    
    // Forms
    public static final IHtmlElement FORM = new AutoCloserHtmlElement("form", new String[] { "p" }, null);
    public static final IHtmlElement FIELDSET = new AutoCloserHtmlElement("fieldset", new String[] { "p" }, null);
    public static final IHtmlElement LEGEND = new BasicHtmlElement("legend");
    public static final IHtmlElement LABEL = new BasicHtmlElement("label");
    public static final IHtmlElement INPUT = new VoidHtmlElement("input");
    public static final IHtmlElement BUTTON = new BasicHtmlElement("button");
    public static final IHtmlElement SELECT = new BasicHtmlElement("select");
    public static final IHtmlElement DATALIST = new BasicHtmlElement("datalist");
    public static final IHtmlElement OPTGROUP = new AutoCloserHtmlElement("optgroup", new String[] { "optgroup", "option" }, new String[] { "select" });
    public static final IHtmlElement OPTION = new AutoCloserHtmlElement("option", new String[] { "option" }, new String[] { "select", "optgroup", "datalist" });
    public static final IHtmlElement TEXTAREA = new BasicHtmlElement("textarea");
    public static final IHtmlElement KEYGEN = new VoidHtmlElement("keygen");
    public static final IHtmlElement OUTPUT = new BasicHtmlElement("output");
    public static final IHtmlElement PROGRESS = new BasicHtmlElement("progress");
    public static final IHtmlElement METER = new BasicHtmlElement("meter");
    
    // Interactive elements
    public static final IHtmlElement DETAILS = new BasicHtmlElement("details");
    public static final IHtmlElement SUMMARY = new BasicHtmlElement("summary");
    public static final IHtmlElement COMMAND = new BasicHtmlElement("command");
    public static final IHtmlElement MENU = new AutoCloserHtmlElement("menu", new String[] { "p" }, null);
    public static final IHtmlElement MENUITEM = new VoidHtmlElement("menuitem");
    public static final IHtmlElement DIALOG = new BasicHtmlElement("dialog");
    
    

    static {

        ALL_STANDARD_ELEMENTS =
                Collections.unmodifiableSet(new LinkedHashSet<IHtmlElement>(Arrays.asList(
                        new IHtmlElement[] {
                                HTML, HEAD, TITLE, BASE, LINK, META, STYLE, SCRIPT, NOSCRIPT, BODY, ARTICLE,
                                SECTION, NAV, ASIDE, H1, H2, H3, H4, H5, H6, HGROUP, HEADER, FOOTER,
                                ADDRESS, P, HR, PRE, BLOCKQUOTE, OL, UL, LI, DL, DT, DD, FIGURE,
                                FIGCAPTION, DIV, A, EM, STRONG, SMALL, S, CITE, G, DFN, ABBR, TIME,
                                CODE, VAR, SAMP, KBD, SUB, SUP, I, B, U, MARK, RUBY, RB, RT, RTC,
                                RP, BDI, BDO, SPAN, BR, WBR, INS, DEL, IMG, IFRAME, EMBED, OBJECT,
                                PARAM, VIDEO, AUDIO, SOURCE, TRACK, CANVAS, MAP, AREA, TABLE, CAPTION,
                                COLGROUP, COL, TBODY, THEAD, TFOOT, TR, TD, TH, FORM, FIELDSET, LEGEND, LABEL,
                                INPUT, BUTTON, SELECT, DATALIST, OPTGROUP, OPTION, TEXTAREA, KEYGEN, OUTPUT, PROGRESS,
                                METER, DETAILS, SUMMARY, COMMAND, MENU, MENUITEM, DIALOG, MAIN
                        })));

        /*
         * Register the standard elements at the element repository, in order to initialize it
         */
        for (final IHtmlElement element : ALL_STANDARD_ELEMENTS) {
            ELEMENTS.storeElement(element);
        }


        final Set<String> allStandardElementNamesAux = new LinkedHashSet<String>(ALL_STANDARD_ELEMENTS.size() + 3);
        for (final IHtmlElement element : ALL_STANDARD_ELEMENTS) {
            allStandardElementNamesAux.add(element.getName());
        }
        ALL_STANDARD_ELEMENT_NAMES = Collections.unmodifiableSet(allStandardElementNamesAux);


        ALL_STANDARD_ATTRIBUTE_NAMES =
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



    public static IHtmlElement forName(final char[] elementNameBuffer, final int offset, final int len) {
        if (elementNameBuffer == null) {
            throw new IllegalArgumentException("Buffer cannot be null");
        }
        return ELEMENTS.getElement(elementNameBuffer, offset, len);
    }


    public static IHtmlElement forName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        return ELEMENTS.getElement(name);
    }

    
    

    
    
    private HtmlElements() {
        super();
    }
    
    

    /*
     * <p>
     *     This class is <strong>thread-safe</strong>. The reason for this is that it not only contains the
     *     standard elements, but will also contain new instances of IHtmlElement created during parsing (created
     *     when asking the repository for them when they do not exist yet. As any thread can create a new element,
     *     this has to be lock-protected.
     * </p>
     */
    static final class ElementRepository {

        private final List<IHtmlElement> repository;

        private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
        private final Lock readLock = this.lock.readLock();
        private final Lock writeLock = this.lock.writeLock();


        ElementRepository() {
            this.repository = new ArrayList<IHtmlElement>(40);
        }



        IHtmlElement getElement(final char[] text, final int offset, final int len) {

            this.readLock.lock();
            try {

                final int index = binarySearch(this.repository, text, offset, len);

                if (index != -1) {
                    return this.repository.get(index);
                }

            } finally {
                this.readLock.unlock();
            }


            /*
             * NOT FOUND. We need to obtain a write lock and store the text
             */
            this.writeLock.lock();
            try {
                return storeElement(text, offset, len);
            } finally {
                this.writeLock.unlock();
            }

        }


        IHtmlElement getElement(final String text) {

            this.readLock.lock();
            try {

                final int index = binarySearch(this.repository, text);

                if (index != -1) {
                    return this.repository.get(index);
                }

            } finally {
                this.readLock.unlock();
            }


            /*
             * NOT FOUND. We need to obtain a write lock and store the text
             */
            this.writeLock.lock();
            try {
                return storeElement(text);
            } finally {
                this.writeLock.unlock();
            }

        }


        private IHtmlElement storeElement(final char[] text, final int offset, final int len) {

            final int index = binarySearch(this.repository, text, offset, len);
            if (index != -1) {
                // It was already added while we were waiting for the lock!
                return this.repository.get(index);
            }

            final IHtmlElement element = new BasicHtmlElement(new String(text, offset, len).toLowerCase());

            this.repository.add(element);
            Collections.sort(this.repository, ElementComparator.INSTANCE);

            return element;

        }


        private IHtmlElement storeElement(final String text) {

            final int index = binarySearch(this.repository, text);
            if (index != -1) {
                // It was already added while we were waiting for the lock!
                return this.repository.get(index);
            }

            final IHtmlElement element = new BasicHtmlElement(text.toLowerCase());

            this.repository.add(element);
            Collections.sort(this.repository,ElementComparator.INSTANCE);

            return element;

        }


        private IHtmlElement storeElement(final IHtmlElement element) {

            // This method will only be called from within the HtmlElements class itself, during initialization of
            // standard elements.

            this.repository.add(element);
            Collections.sort(this.repository,ElementComparator.INSTANCE);

            return element;

        }



        private static int binarySearch(final List<IHtmlElement> values,
                                        final String text) {

            int low = 0;
            int high = values.size() - 1;

            while (low <= high) {

                final int mid = (low + high) >>> 1;
                final String midVal = values.get(mid).getName();

                final int cmp = TextUtil.compareTo(false, midVal, text);

                if (cmp < 0) {
                    low = mid + 1;
                } else if (cmp > 0) {
                    high = mid - 1;
                } else {
                    // Found!!
                    return mid;
                }

            }

            return -1;  // Not Found!!

        }


        private static int binarySearch(final List<IHtmlElement> values,
                                        final char[] text, final int offset, final int len) {

            int low = 0;
            int high = values.size() - 1;

            while (low <= high) {

                final int mid = (low + high) >>> 1;
                final String midVal = values.get(mid).getName();

                final int cmp = TextUtil.compareTo(false, midVal, 0, midVal.length(), text, offset, len);

                if (cmp < 0) {
                    low = mid + 1;
                } else if (cmp > 0) {
                    high = mid - 1;
                } else {
                    // Found!!
                    return mid;
                }

            }

            return -1;  // Not Found!!

        }


        private static class ElementComparator implements Comparator<IHtmlElement> {

            private static ElementComparator INSTANCE = new ElementComparator();

            public int compare(final IHtmlElement o1, final IHtmlElement o2) {
                return TextUtil.compareTo(false, o1.getName(), o2.getName());
            }
        }

    }




}