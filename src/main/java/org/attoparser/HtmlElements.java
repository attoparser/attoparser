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

import org.attoparser.util.TextUtil;


/*
 * Repository class for all the standard HTML elements, modeled as objects implementing
 * the HtmlElement by means of one of its concrete implementations.
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


    // Set containing all the standard elements, for possible external reference
    static final Set<HtmlElement> ALL_STANDARD_ELEMENTS;


    // Root
    static final HtmlElement HTML = new HtmlElement("html");

    // Document metadata
    static final HtmlElement HEAD = new HtmlAutoOpenElement("head", new String[] { "html" }, null);
    static final HtmlElement TITLE = new HtmlHeadElement("title");
    static final HtmlElement BASE = new HtmlVoidHeadElement("base");
    static final HtmlElement LINK = new HtmlVoidHeadElement("link");
    static final HtmlElement META = new HtmlVoidHeadElement("meta");
    static final HtmlElement STYLE = new HtmlHeadCDATAContentElement("style");

    // Scripting
    static final HtmlElement SCRIPT = new HtmlHeadCDATAContentElement("script");
    static final HtmlElement NOSCRIPT = new HtmlHeadElement("noscript");

    // Sections
    static final HtmlElement BODY = new HtmlAutoOpenCloseElement("body", new String[] { "html" }, null, new String[] { "head" }, null);
    static final HtmlElement ARTICLE = new HtmlBodyBlockElement("article");
    static final HtmlElement SECTION = new HtmlBodyBlockElement("section");
    static final HtmlElement NAV = new HtmlBodyBlockElement("nav");
    static final HtmlElement ASIDE = new HtmlBodyBlockElement("aside");
    static final HtmlElement H1 = new HtmlBodyBlockElement("h1");
    static final HtmlElement H2 = new HtmlBodyBlockElement("h2");
    static final HtmlElement H3 = new HtmlBodyBlockElement("h3");
    static final HtmlElement H4 = new HtmlBodyBlockElement("h4");
    static final HtmlElement H5 = new HtmlBodyBlockElement("h5");
    static final HtmlElement H6 = new HtmlBodyBlockElement("h6");
    static final HtmlElement HGROUP = new HtmlBodyBlockElement("hgroup");
    static final HtmlElement HEADER = new HtmlBodyBlockElement("header");
    static final HtmlElement FOOTER = new HtmlBodyBlockElement("footer");
    static final HtmlElement ADDRESS = new HtmlBodyBlockElement("address");
    static final HtmlElement MAIN = new HtmlBodyBlockElement("main");

    // Grouping content
    static final HtmlElement P = new HtmlBodyBlockElement("p");
    static final HtmlElement HR = new HtmlVoidBodyBlockElement("hr");
    static final HtmlElement PRE = new HtmlBodyBlockElement("pre");
    static final HtmlElement BLOCKQUOTE = new HtmlBodyBlockElement("blockquote");
    static final HtmlElement OL = new HtmlBodyBlockElement("ol");
    static final HtmlElement UL = new HtmlBodyBlockElement("ul");
    static final HtmlElement LI = new HtmlBodyAutoCloseElement("li", new String[] { "li" }, new String[] { "ul", "ol" });
    static final HtmlElement DL = new HtmlBodyBlockElement("dl");
    static final HtmlElement DT = new HtmlBodyAutoCloseElement("dt", new String[] { "dt", "dd" }, new String[] { "dl" });
    static final HtmlElement DD = new HtmlBodyAutoCloseElement("dd", new String[] { "dt", "dd" }, new String[] { "dl" });
    static final HtmlElement FIGURE = new HtmlBodyElement("figure");
    static final HtmlElement FIGCAPTION = new HtmlBodyElement("figcaption");
    static final HtmlElement DIV = new HtmlBodyBlockElement("div");

    // Text-level semantics
    static final HtmlElement A = new HtmlBodyElement("a");
    static final HtmlElement EM = new HtmlBodyElement("em");
    static final HtmlElement STRONG = new HtmlBodyElement("strong");
    static final HtmlElement SMALL = new HtmlBodyElement("small");
    static final HtmlElement S = new HtmlBodyElement("s");
    static final HtmlElement CITE = new HtmlBodyElement("cite");
    static final HtmlElement G = new HtmlBodyElement("g");
    static final HtmlElement DFN = new HtmlBodyElement("dfn");
    static final HtmlElement ABBR = new HtmlBodyElement("abbr");
    static final HtmlElement TIME = new HtmlBodyElement("time");
    static final HtmlElement CODE = new HtmlBodyElement("code");
    static final HtmlElement VAR = new HtmlBodyElement("var");
    static final HtmlElement SAMP = new HtmlBodyElement("samp");
    static final HtmlElement KBD = new HtmlBodyElement("kbd");
    static final HtmlElement SUB = new HtmlBodyElement("sub");
    static final HtmlElement SUP = new HtmlBodyElement("sup");
    static final HtmlElement I = new HtmlBodyElement("i");
    static final HtmlElement B = new HtmlBodyElement("b");
    static final HtmlElement U = new HtmlBodyElement("u");
    static final HtmlElement MARK = new HtmlBodyElement("mark");
    static final HtmlElement RUBY = new HtmlBodyElement("ruby");
    static final HtmlElement RB = new HtmlBodyAutoCloseElement("rb", new String[] { "rb", "rt", "rtc", "rp" }, new String[] { "ruby" });
    static final HtmlElement RT = new HtmlBodyAutoCloseElement("rt", new String[] { "rb", "rt", "rp" }, new String[] { "ruby", "rtc" });
    static final HtmlElement RTC = new HtmlBodyAutoCloseElement("rtc", new String[] { "rb", "rt", "rtc", "rp" }, new String[] { "ruby" });
    static final HtmlElement RP = new HtmlBodyAutoCloseElement("rp", new String[] { "rb", "rt", "rp" }, new String[] { "ruby", "rtc" });
    static final HtmlElement BDI = new HtmlBodyElement("bdi");
    static final HtmlElement BDO = new HtmlBodyElement("bdo");
    static final HtmlElement SPAN = new HtmlBodyElement("span");
    static final HtmlElement BR = new HtmlVoidBodyElement("br");
    static final HtmlElement WBR = new HtmlVoidBodyElement("wbr");

    // Edits
    static final HtmlElement INS = new HtmlBodyElement("ins");
    static final HtmlElement DEL = new HtmlBodyElement("del");

    // Embedded content
    static final HtmlElement IMG = new HtmlVoidBodyElement("img");
    static final HtmlElement IFRAME = new HtmlBodyElement("iframe");
    static final HtmlElement EMBED = new HtmlVoidBodyElement("embed");
    static final HtmlElement OBJECT = new HtmlHeadElement("object");
    static final HtmlElement PARAM = new HtmlVoidBodyElement("param");
    static final HtmlElement VIDEO = new HtmlBodyElement("video");
    static final HtmlElement AUDIO = new HtmlBodyElement("audio");
    static final HtmlElement SOURCE = new HtmlVoidBodyElement("source");
    static final HtmlElement TRACK = new HtmlVoidBodyElement("track");
    static final HtmlElement CANVAS = new HtmlBodyElement("canvas");
    static final HtmlElement MAP = new HtmlBodyElement("map");
    static final HtmlElement AREA = new HtmlVoidBodyElement("area");
    
    // Tabular data
    static final HtmlElement TABLE = new HtmlBodyBlockElement("table");
    static final HtmlElement CAPTION = new HtmlBodyAutoCloseElement("caption", new String[] { "tr", "td", "th", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final HtmlElement COLGROUP = new HtmlBodyAutoCloseElement("colgroup", new String[] { "tr", "td", "th", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final HtmlElement COL = new HtmlVoidAutoOpenCloseElement("col", new String[] { "colgroup" }, new String[] { "colgroup" }, new String[] { "tr", "td", "th", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final HtmlElement TBODY = new HtmlBodyAutoCloseElement("tbody", new String[] { "tr", "td", "th", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final HtmlElement THEAD = new HtmlBodyAutoCloseElement("thead", new String[] { "tr", "td", "th", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final HtmlElement TFOOT = new HtmlBodyAutoCloseElement("tfoot", new String[] { "tr", "td", "th", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final HtmlElement TR = new HtmlAutoOpenCloseElement("tr", new String[] { "tbody" }, new String[] { "thead", "tfoot", "tbody" }, new String[] { "tr", "td", "th", "caption", "colgroup" }, new String[] { "table", "thead", "tbody", "tfoot" });
    static final HtmlElement TD = new HtmlBodyAutoCloseElement("td", new String[] { "td", "th" }, new String[] { "tr" });
    static final HtmlElement TH = new HtmlBodyAutoCloseElement("th", new String[] { "td", "th" }, new String[] { "tr" });
    
    // Forms
    static final HtmlElement FORM = new HtmlBodyBlockElement("form");
    static final HtmlElement FIELDSET = new HtmlBodyBlockElement("fieldset");
    static final HtmlElement LEGEND = new HtmlBodyElement("legend");
    static final HtmlElement LABEL = new HtmlBodyElement("label");
    static final HtmlElement INPUT = new HtmlVoidBodyElement("input");
    static final HtmlElement BUTTON = new HtmlBodyElement("button");
    static final HtmlElement SELECT = new HtmlBodyElement("select");
    static final HtmlElement DATALIST = new HtmlBodyElement("datalist");
    static final HtmlElement OPTGROUP = new HtmlBodyAutoCloseElement("optgroup", new String[] { "optgroup", "option" }, new String[] { "select" });
    static final HtmlElement OPTION = new HtmlBodyAutoCloseElement("option", new String[] { "option" }, new String[] { "select", "optgroup", "datalist" });
    static final HtmlElement TEXTAREA = new HtmlBodyElement("textarea");
    static final HtmlElement KEYGEN = new HtmlVoidBodyElement("keygen");
    static final HtmlElement OUTPUT = new HtmlBodyElement("output");
    static final HtmlElement PROGRESS = new HtmlBodyElement("progress");
    static final HtmlElement METER = new HtmlBodyElement("meter");
    
    // Interactive elements
    static final HtmlElement DETAILS = new HtmlBodyElement("details");
    static final HtmlElement SUMMARY = new HtmlBodyElement("summary");
    static final HtmlElement COMMAND = new HtmlBodyElement("command");
    static final HtmlElement MENU = new HtmlBodyBlockElement("menu");
    static final HtmlElement MENUITEM = new HtmlVoidBodyElement("menuitem");
    static final HtmlElement DIALOG = new HtmlBodyElement("dialog");

    // WebComponents
    static final HtmlElement TEMPLATE = new HtmlHeadElement("template");
    static final HtmlElement ELEMENT = new HtmlHeadElement("element");
    static final HtmlElement DECORATOR = new HtmlHeadElement("decorator");
    static final HtmlElement CONTENT = new HtmlHeadElement("content");
    static final HtmlElement SHADOW = new  HtmlHeadElement("shadow");



    static {

        ALL_STANDARD_ELEMENTS =
                Collections.unmodifiableSet(new LinkedHashSet<HtmlElement>(Arrays.asList(
                        new HtmlElement[] {
                                HTML, HEAD, TITLE, BASE, LINK, META, STYLE, SCRIPT, NOSCRIPT, BODY, ARTICLE,
                                SECTION, NAV, ASIDE, H1, H2, H3, H4, H5, H6, HGROUP, HEADER, FOOTER,
                                ADDRESS, P, HR, PRE, BLOCKQUOTE, OL, UL, LI, DL, DT, DD, FIGURE,
                                FIGCAPTION, DIV, A, EM, STRONG, SMALL, S, CITE, G, DFN, ABBR, TIME,
                                CODE, VAR, SAMP, KBD, SUB, SUP, I, B, U, MARK, RUBY, RB, RT, RTC,
                                RP, BDI, BDO, SPAN, BR, WBR, INS, DEL, IMG, IFRAME, EMBED, OBJECT,
                                PARAM, VIDEO, AUDIO, SOURCE, TRACK, CANVAS, MAP, AREA, TABLE, CAPTION,
                                COLGROUP, COL, TBODY, THEAD, TFOOT, TR, TD, TH, FORM, FIELDSET, LEGEND, LABEL,
                                INPUT, BUTTON, SELECT, DATALIST, OPTGROUP, OPTION, TEXTAREA, KEYGEN, OUTPUT, PROGRESS,
                                METER, DETAILS, SUMMARY, COMMAND, MENU, MENUITEM, DIALOG, MAIN, TEMPLATE,
                                ELEMENT, DECORATOR, CONTENT, SHADOW
                        })));

        /*
         * Register the standard elements at the element repository, in order to initialize it
         */
        for (final HtmlElement element : ALL_STANDARD_ELEMENTS) {
            ELEMENTS.storeStandardElement(element);
        }


    }


    /*
     * Note this will always be case-insensitive, because we are dealing with HTML.
     */
    static HtmlElement forName(final char[] elementNameBuffer, final int offset, final int len) {
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
     * standard elements, but will also contain new instances of HtmlElement created during parsing (created
     * when asking the repository for them when they do not exist yet. As any thread can create a new element,
     * this has to be lock-protected.
     */
    static final class HtmlElementRepository {

        private final List<HtmlElement> standardRepository; // read-only, no sync needed
        private final List<HtmlElement> repository;  // read-write, sync will be needed

        private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
        private final Lock readLock = this.lock.readLock();
        private final Lock writeLock = this.lock.writeLock();


        HtmlElementRepository() {
            this.standardRepository = new ArrayList<HtmlElement>(150);
            this.repository = new ArrayList<HtmlElement>(150);
        }



        HtmlElement getElement(final char[] text, final int offset, final int len) {

            /*
             * We first try to find it in the repository containing the standard elements, which does not need
             * any synchronization.
             */
            int index = binarySearch(this.standardRepository, text, offset, len);

            if (index >= 0) {
                return this.standardRepository.get(index);
            }

            /*
             * We did not find it in the repository of standard elements, so let's try in the read+write one,
             * which does require synchronization through a readwrite lock.
             */

            this.readLock.lock();
            try {

                index = binarySearch(this.repository, text, offset, len);

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


        private HtmlElement storeElement(final char[] text, final int offset, final int len) {

            final int index = binarySearch(this.repository, text, offset, len);
            if (index >= 0) {
                // It was already added while we were waiting for the lock!
                return this.repository.get(index);
            }

            final HtmlElement element = new HtmlElement(new String(text, offset, len).toLowerCase());

            // binary Search returned (-(insertion point) - 1)
            this.repository.add(((index + 1) * -1), element);

            return element;

        }


        private HtmlElement storeStandardElement(final HtmlElement element) {

            // This method will only be called from within the HtmlElements class itself, during initialization of
            // standard elements.

            this.standardRepository.add(element);
            this.repository.add(element);
            Collections.sort(this.standardRepository,ElementComparator.INSTANCE);
            Collections.sort(this.repository,ElementComparator.INSTANCE);

            return element;

        }



        private static int binarySearch(final List<HtmlElement> values,
                                        final char[] text, final int offset, final int len) {

            int low = 0;
            int high = values.size() - 1;

            int mid, cmp;
            char[] midVal;

            while (low <= high) {

                mid = (low + high) >>> 1;
                midVal = values.get(mid).name;

                cmp = TextUtil.compareTo(false, midVal, 0, midVal.length, text, offset, len);

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


        private static class ElementComparator implements Comparator<HtmlElement> {

            private static ElementComparator INSTANCE = new ElementComparator();

            public int compare(final HtmlElement o1, final HtmlElement o2) {
                return TextUtil.compareTo(false, o1.name, o2.name);
            }
        }

    }




}