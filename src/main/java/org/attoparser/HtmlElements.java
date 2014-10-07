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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/*
 * Repository class for all the standard HTML elements, modeled as objects implementing
 * the IHtmlElement by means of one of its concrete implementations.
 *
 * By using one specific implementation for each element, that HTML element is given
 * some behaviour. E.g., <script> tags should consider their body as CDATA and
 * not PCDATA (i.e. their body should not be parsed), and that's why the SCRIPT
 * constant object is an implementation of HtmlCDATAContentElement.
 * 
 * @author Daniel Fernandez
 * @since 2.0.0
 */
final class HtmlElements {

    private static final HtmlElementRepository ELEMENTS = new HtmlElementRepository();


    // Set containing all the standard elements, for posible external reference
    static final Set<IHtmlElement> ALL_STANDARD_ELEMENTS;


    // Root
    static final IHtmlElement HTML = new HtmlBasicElement("html");
    
    // Document metadata
    static final IHtmlElement HEAD = new HtmlBasicElement("head");
    static final IHtmlElement TITLE = new HtmlBasicElement("title");
    static final IHtmlElement BASE = new HtmlVoidElement("base");
    static final IHtmlElement LINK = new HtmlVoidElement("link");
    static final IHtmlElement META = new HtmlVoidElement("meta");
    static final IHtmlElement STYLE = new HtmlCDATAContentElement("style");
    
    // Scripting
    static final IHtmlElement SCRIPT = new HtmlCDATAContentElement("script");
    static final IHtmlElement NOSCRIPT = new HtmlBasicElement("noscript");
    
    // Sections
    static final IHtmlElement BODY = new HtmlBasicElement("body");
    static final IHtmlElement ARTICLE = new HtmlAutoCloserElement("article", new String[] { "p" }, null);
    static final IHtmlElement SECTION = new HtmlAutoCloserElement("section", new String[] { "p" }, null);
    static final IHtmlElement NAV = new HtmlAutoCloserElement("nav", new String[] { "p" }, null);
    static final IHtmlElement ASIDE = new HtmlAutoCloserElement("aside", new String[] { "p" }, null);
    static final IHtmlElement H1 = new HtmlAutoCloserElement("h1", new String[] { "p" }, null);
    static final IHtmlElement H2 = new HtmlAutoCloserElement("h2", new String[] { "p" }, null);
    static final IHtmlElement H3 = new HtmlAutoCloserElement("h3", new String[] { "p" }, null);
    static final IHtmlElement H4 = new HtmlAutoCloserElement("h4", new String[] { "p" }, null);
    static final IHtmlElement H5 = new HtmlAutoCloserElement("h5", new String[] { "p" }, null);
    static final IHtmlElement H6 = new HtmlAutoCloserElement("h6", new String[] { "p" }, null);
    static final IHtmlElement HGROUP = new HtmlAutoCloserElement("hgroup", new String[] { "p" }, null);
    static final IHtmlElement HEADER = new HtmlAutoCloserElement("header", new String[] { "p" }, null);
    static final IHtmlElement FOOTER = new HtmlAutoCloserElement("footer", new String[] { "p" }, null);
    static final IHtmlElement ADDRESS = new HtmlAutoCloserElement("address", new String[] { "p" }, null);
    static final IHtmlElement MAIN = new HtmlAutoCloserElement("main", new String[] { "p" }, null);

    // Grouping content
    static final IHtmlElement P = new HtmlAutoCloserElement("p", new String[] { "p" }, null);
    static final IHtmlElement HR = new HtmlVoidAutoCloserElement("hr", new String[] { "p" }, null);
    static final IHtmlElement PRE = new HtmlAutoCloserElement("pre", new String[] { "p" }, null);
    static final IHtmlElement BLOCKQUOTE = new HtmlAutoCloserElement("blockquote", new String[] { "p" }, null);
    static final IHtmlElement OL = new HtmlAutoCloserElement("ol", new String[] { "p" }, null);
    static final IHtmlElement UL = new HtmlAutoCloserElement("ul", new String[] { "p" }, null);
    static final IHtmlElement LI = new HtmlAutoCloserElement("li", new String[] { "li" }, new String[] { "ul", "ol" });
    static final IHtmlElement DL = new HtmlAutoCloserElement("dl", new String[] { "p" }, null);
    static final IHtmlElement DT = new HtmlAutoCloserElement("dt", new String[] { "dt", "dd" }, new String[] { "dl" });
    static final IHtmlElement DD = new HtmlAutoCloserElement("dd", new String[] { "dt", "dd" }, new String[] { "dl" });
    static final IHtmlElement FIGURE = new HtmlBasicElement("figure");
    static final IHtmlElement FIGCAPTION = new HtmlBasicElement("figcaption");
    static final IHtmlElement DIV = new HtmlAutoCloserElement("div", new String[] { "p" }, null);
    
    // Text-level semantics
    static final IHtmlElement A = new HtmlBasicElement("a");
    static final IHtmlElement EM = new HtmlBasicElement("em");
    static final IHtmlElement STRONG = new HtmlBasicElement("strong");
    static final IHtmlElement SMALL = new HtmlBasicElement("small");
    static final IHtmlElement S = new HtmlBasicElement("s");
    static final IHtmlElement CITE = new HtmlBasicElement("cite");
    static final IHtmlElement G = new HtmlBasicElement("g");
    static final IHtmlElement DFN = new HtmlBasicElement("dfn");
    static final IHtmlElement ABBR = new HtmlBasicElement("abbr");
    static final IHtmlElement TIME = new HtmlBasicElement("time");
    static final IHtmlElement CODE = new HtmlBasicElement("code");
    static final IHtmlElement VAR = new HtmlBasicElement("var");
    static final IHtmlElement SAMP = new HtmlBasicElement("samp");
    static final IHtmlElement KBD = new HtmlBasicElement("kbd");
    static final IHtmlElement SUB = new HtmlBasicElement("sub");
    static final IHtmlElement SUP = new HtmlBasicElement("sup");
    static final IHtmlElement I = new HtmlBasicElement("i");
    static final IHtmlElement B = new HtmlBasicElement("b");
    static final IHtmlElement U = new HtmlBasicElement("u");
    static final IHtmlElement MARK = new HtmlBasicElement("mark");
    static final IHtmlElement RUBY = new HtmlBasicElement("ruby");
    static final IHtmlElement RB = new HtmlAutoCloserElement("rb", new String[] { "rb", "rt", "rtc", "rp" }, new String[] { "ruby" });
    static final IHtmlElement RT = new HtmlAutoCloserElement("rt", new String[] { "rb", "rt", "rp" }, new String[] { "ruby", "rtc" });
    static final IHtmlElement RTC = new HtmlAutoCloserElement("rtc", new String[] { "rb", "rt", "rtc", "rp" }, new String[] { "ruby" });
    static final IHtmlElement RP = new HtmlAutoCloserElement("rp", new String[] { "rb", "rt", "rp" }, new String[] { "ruby", "rtc" });
    static final IHtmlElement BDI = new HtmlBasicElement("bdi");
    static final IHtmlElement BDO = new HtmlBasicElement("bdo");
    static final IHtmlElement SPAN = new HtmlBasicElement("span");
    static final IHtmlElement BR = new HtmlVoidElement("br");
    static final IHtmlElement WBR = new HtmlVoidElement("wbr");

    // Edits
    static final IHtmlElement INS = new HtmlBasicElement("ins");
    static final IHtmlElement DEL = new HtmlBasicElement("del");
    
    // Embedded content
    static final IHtmlElement IMG = new HtmlVoidElement("img");
    static final IHtmlElement IFRAME = new HtmlBasicElement("iframe");
    static final IHtmlElement EMBED = new HtmlVoidElement("embed");
    static final IHtmlElement OBJECT = new HtmlBasicElement("object");
    static final IHtmlElement PARAM = new HtmlVoidElement("param");
    static final IHtmlElement VIDEO = new HtmlBasicElement("video");
    static final IHtmlElement AUDIO = new HtmlBasicElement("audio");
    static final IHtmlElement SOURCE = new HtmlVoidElement("source");
    static final IHtmlElement TRACK = new HtmlVoidElement("track");
    static final IHtmlElement CANVAS = new HtmlBasicElement("canvas");
    static final IHtmlElement MAP = new HtmlBasicElement("map");
    static final IHtmlElement AREA = new HtmlVoidElement("area");
    
    // Tabular data
    static final IHtmlElement TABLE = new HtmlAutoCloserElement("table", new String[] { "p" }, null);
    static final IHtmlElement CAPTION = new HtmlAutoCloserElement("caption", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final IHtmlElement COLGROUP = new HtmlAutoCloserElement("colgroup", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final IHtmlElement COL = new HtmlVoidElement("col");
    static final IHtmlElement TBODY = new HtmlAutoCloserElement("tbody", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final IHtmlElement THEAD = new HtmlAutoCloserElement("thead", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final IHtmlElement TFOOT = new HtmlAutoCloserElement("tfoot", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final IHtmlElement TR = new HtmlAutoCloserElement("tr", new String[] { "tr", "caption", "colgroup" }, new String[] { "table", "thead", "tbody", "tfoot" });
    static final IHtmlElement TD = new HtmlAutoCloserElement("td", new String[] { "td", "th" }, new String[] { "tr" });
    static final IHtmlElement TH = new HtmlAutoCloserElement("th", new String[] { "td", "th" }, new String[] { "tr" });
    
    // Forms
    static final IHtmlElement FORM = new HtmlAutoCloserElement("form", new String[] { "p" }, null);
    static final IHtmlElement FIELDSET = new HtmlAutoCloserElement("fieldset", new String[] { "p" }, null);
    static final IHtmlElement LEGEND = new HtmlBasicElement("legend");
    static final IHtmlElement LABEL = new HtmlBasicElement("label");
    static final IHtmlElement INPUT = new HtmlVoidElement("input");
    static final IHtmlElement BUTTON = new HtmlBasicElement("button");
    static final IHtmlElement SELECT = new HtmlBasicElement("select");
    static final IHtmlElement DATALIST = new HtmlBasicElement("datalist");
    static final IHtmlElement OPTGROUP = new HtmlAutoCloserElement("optgroup", new String[] { "optgroup", "option" }, new String[] { "select" });
    static final IHtmlElement OPTION = new HtmlAutoCloserElement("option", new String[] { "option" }, new String[] { "select", "optgroup", "datalist" });
    static final IHtmlElement TEXTAREA = new HtmlBasicElement("textarea");
    static final IHtmlElement KEYGEN = new HtmlVoidElement("keygen");
    static final IHtmlElement OUTPUT = new HtmlBasicElement("output");
    static final IHtmlElement PROGRESS = new HtmlBasicElement("progress");
    static final IHtmlElement METER = new HtmlBasicElement("meter");
    
    // Interactive elements
    static final IHtmlElement DETAILS = new HtmlBasicElement("details");
    static final IHtmlElement SUMMARY = new HtmlBasicElement("summary");
    static final IHtmlElement COMMAND = new HtmlBasicElement("command");
    static final IHtmlElement MENU = new HtmlAutoCloserElement("menu", new String[] { "p" }, null);
    static final IHtmlElement MENUITEM = new HtmlVoidElement("menuitem");
    static final IHtmlElement DIALOG = new HtmlBasicElement("dialog");
    
    

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


    }


    /*
     * Note this will always be case-insensitive, because we are dealing with HTML.
     */
    static IHtmlElement forName(final char[] elementNameBuffer, final int offset, final int len) {
        if (elementNameBuffer == null) {
            throw new IllegalArgumentException("Buffer cannot be null");
        }
        return ELEMENTS.getElement(elementNameBuffer, offset, len);
    }


    

    
    
    private HtmlElements() {
        super();
    }
    
    

    /*
     * This repository class is thread-safe. The reason for this is that it not only contains the
     * standard elements, but will also contain new instances of IHtmlElement created during parsing (created
     * when asking the repository for them when they do not exist yet. As any thread can create a new element,
     * this has to be lock-protected.
     */
    static final class HtmlElementRepository {

        private final List<IHtmlElement> repository;

        private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
        private final Lock readLock = this.lock.readLock();
        private final Lock writeLock = this.lock.writeLock();


        HtmlElementRepository() {
            this.repository = new ArrayList<IHtmlElement>(40);
        }



        IHtmlElement getElement(final char[] text, final int offset, final int len) {

            this.readLock.lock();
            try {

                final int index = binarySearch(this.repository, text, offset, len);

                if (index >= 0) {
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


        private IHtmlElement storeElement(final char[] text, final int offset, final int len) {

            final int index = binarySearch(this.repository, text, offset, len);
            if (index >= 0) {
                // It was already added while we were waiting for the lock!
                return this.repository.get(index);
            }

            final IHtmlElement element = new HtmlBasicElement(new String(text, offset, len).toLowerCase());

            // binary Search returned (-(insertion point) - 1)
            this.repository.add(((index + 1) * -1), element);

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
                                        final char[] text, final int offset, final int len) {

            int low = 0;
            int high = values.size() - 1;

            int mid, cmp;
            String midVal;

            while (low <= high) {

                mid = (low + high) >>> 1;
                midVal = values.get(mid).getName();

                cmp = TextUtil.compareTo(false, midVal, 0, midVal.length(), text, offset, len);

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


        private static class ElementComparator implements Comparator<IHtmlElement> {

            private static ElementComparator INSTANCE = new ElementComparator();

            public int compare(final IHtmlElement o1, final IHtmlElement o2) {
                return TextUtil.compareTo(false, o1.getName(), o2.getName());
            }
        }

    }




}