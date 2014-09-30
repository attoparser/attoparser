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
package org.attoparser.html;

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


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
final class HtmlElements {

    private static final HtmlElementRepository ELEMENTS = new HtmlElementRepository();


    // Set containing all the standard elements, for posible external reference
    static final Set<IHtmlElement> ALL_STANDARD_ELEMENTS;


    // Root
    static final IHtmlElement HTML = new BasicHtmlElement("html");
    
    // Document metadata
    static final IHtmlElement HEAD = new BasicHtmlElement("head");
    static final IHtmlElement TITLE = new BasicHtmlElement("title");
    static final IHtmlElement BASE = new VoidHtmlElement("base");
    static final IHtmlElement LINK = new VoidHtmlElement("link");
    static final IHtmlElement META = new VoidHtmlElement("meta");
    static final IHtmlElement STYLE = new CDATAContentHtmlElement("style");
    
    // Scripting
    static final IHtmlElement SCRIPT = new CDATAContentHtmlElement("script");
    static final IHtmlElement NOSCRIPT = new BasicHtmlElement("noscript");
    
    // Sections
    static final IHtmlElement BODY = new BasicHtmlElement("body");
    static final IHtmlElement ARTICLE = new AutoCloserHtmlElement("article", new String[] { "p" }, null);
    static final IHtmlElement SECTION = new AutoCloserHtmlElement("section", new String[] { "p" }, null);
    static final IHtmlElement NAV = new AutoCloserHtmlElement("nav", new String[] { "p" }, null);
    static final IHtmlElement ASIDE = new AutoCloserHtmlElement("aside", new String[] { "p" }, null);
    static final IHtmlElement H1 = new AutoCloserHtmlElement("h1", new String[] { "p" }, null);
    static final IHtmlElement H2 = new AutoCloserHtmlElement("h2", new String[] { "p" }, null);
    static final IHtmlElement H3 = new AutoCloserHtmlElement("h3", new String[] { "p" }, null);
    static final IHtmlElement H4 = new AutoCloserHtmlElement("h4", new String[] { "p" }, null);
    static final IHtmlElement H5 = new AutoCloserHtmlElement("h5", new String[] { "p" }, null);
    static final IHtmlElement H6 = new AutoCloserHtmlElement("h6", new String[] { "p" }, null);
    static final IHtmlElement HGROUP = new AutoCloserHtmlElement("hgroup", new String[] { "p" }, null);
    static final IHtmlElement HEADER = new AutoCloserHtmlElement("header", new String[] { "p" }, null);
    static final IHtmlElement FOOTER = new AutoCloserHtmlElement("footer", new String[] { "p" }, null);
    static final IHtmlElement ADDRESS = new AutoCloserHtmlElement("address", new String[] { "p" }, null);
    static final IHtmlElement MAIN = new AutoCloserHtmlElement("main", new String[] { "p" }, null);

    // Grouping content
    static final IHtmlElement P = new AutoCloserHtmlElement("p", new String[] { "p" }, null);
    static final IHtmlElement HR = new VoidAutoCloserHtmlElement("hr", new String[] { "p" }, null);
    static final IHtmlElement PRE = new AutoCloserHtmlElement("pre", new String[] { "p" }, null);
    static final IHtmlElement BLOCKQUOTE = new AutoCloserHtmlElement("blockquote", new String[] { "p" }, null);
    static final IHtmlElement OL = new AutoCloserHtmlElement("ol", new String[] { "p" }, null);
    static final IHtmlElement UL = new AutoCloserHtmlElement("ul", new String[] { "p" }, null);
    static final IHtmlElement LI = new AutoCloserHtmlElement("li", new String[] { "li" }, new String[] { "ul", "ol" });
    static final IHtmlElement DL = new AutoCloserHtmlElement("dl", new String[] { "p" }, null);
    static final IHtmlElement DT = new AutoCloserHtmlElement("dt", new String[] { "dt", "dd" }, new String[] { "dl" });
    static final IHtmlElement DD = new AutoCloserHtmlElement("dd", new String[] { "dt", "dd" }, new String[] { "dl" });
    static final IHtmlElement FIGURE = new BasicHtmlElement("figure");
    static final IHtmlElement FIGCAPTION = new BasicHtmlElement("figcaption");
    static final IHtmlElement DIV = new AutoCloserHtmlElement("div", new String[] { "p" }, null);
    
    // Text-level semantics
    static final IHtmlElement A = new BasicHtmlElement("a");
    static final IHtmlElement EM = new BasicHtmlElement("em");
    static final IHtmlElement STRONG = new BasicHtmlElement("strong");
    static final IHtmlElement SMALL = new BasicHtmlElement("small");
    static final IHtmlElement S = new BasicHtmlElement("s");
    static final IHtmlElement CITE = new BasicHtmlElement("cite");
    static final IHtmlElement G = new BasicHtmlElement("g");
    static final IHtmlElement DFN = new BasicHtmlElement("dfn");
    static final IHtmlElement ABBR = new BasicHtmlElement("abbr");
    static final IHtmlElement TIME = new BasicHtmlElement("time");
    static final IHtmlElement CODE = new BasicHtmlElement("code");
    static final IHtmlElement VAR = new BasicHtmlElement("var");
    static final IHtmlElement SAMP = new BasicHtmlElement("samp");
    static final IHtmlElement KBD = new BasicHtmlElement("kbd");
    static final IHtmlElement SUB = new BasicHtmlElement("sub");
    static final IHtmlElement SUP = new BasicHtmlElement("sup");
    static final IHtmlElement I = new BasicHtmlElement("i");
    static final IHtmlElement B = new BasicHtmlElement("b");
    static final IHtmlElement U = new BasicHtmlElement("u");
    static final IHtmlElement MARK = new BasicHtmlElement("mark");
    static final IHtmlElement RUBY = new BasicHtmlElement("ruby");
    static final IHtmlElement RB = new AutoCloserHtmlElement("rb", new String[] { "rb", "rt", "rtc", "rp" }, new String[] { "ruby" });
    static final IHtmlElement RT = new AutoCloserHtmlElement("rt", new String[] { "rb", "rt", "rp" }, new String[] { "ruby", "rtc" });
    static final IHtmlElement RTC = new AutoCloserHtmlElement("rtc", new String[] { "rb", "rt", "rtc", "rp" }, new String[] { "ruby" });
    static final IHtmlElement RP = new AutoCloserHtmlElement("rp", new String[] { "rb", "rt", "rp" }, new String[] { "ruby", "rtc" });
    static final IHtmlElement BDI = new BasicHtmlElement("bdi");
    static final IHtmlElement BDO = new BasicHtmlElement("bdo");
    static final IHtmlElement SPAN = new BasicHtmlElement("span");
    static final IHtmlElement BR = new VoidHtmlElement("br");
    static final IHtmlElement WBR = new VoidHtmlElement("wbr");

    // Edits
    static final IHtmlElement INS = new BasicHtmlElement("ins");
    static final IHtmlElement DEL = new BasicHtmlElement("del");
    
    // Embedded content
    static final IHtmlElement IMG = new VoidHtmlElement("img");
    static final IHtmlElement IFRAME = new BasicHtmlElement("iframe");
    static final IHtmlElement EMBED = new VoidHtmlElement("embed");
    static final IHtmlElement OBJECT = new BasicHtmlElement("object");
    static final IHtmlElement PARAM = new VoidHtmlElement("param");
    static final IHtmlElement VIDEO = new BasicHtmlElement("video");
    static final IHtmlElement AUDIO = new BasicHtmlElement("audio");
    static final IHtmlElement SOURCE = new VoidHtmlElement("source");
    static final IHtmlElement TRACK = new VoidHtmlElement("track");
    static final IHtmlElement CANVAS = new BasicHtmlElement("canvas");
    static final IHtmlElement MAP = new BasicHtmlElement("map");
    static final IHtmlElement AREA = new VoidHtmlElement("area");
    
    // Tabular data
    static final IHtmlElement TABLE = new AutoCloserHtmlElement("table", new String[] { "p" }, null);
    static final IHtmlElement CAPTION = new AutoCloserHtmlElement("caption", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final IHtmlElement COLGROUP = new AutoCloserHtmlElement("colgroup", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final IHtmlElement COL = new VoidHtmlElement("col");
    static final IHtmlElement TBODY = new AutoCloserHtmlElement("tbody", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final IHtmlElement THEAD = new AutoCloserHtmlElement("thead", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final IHtmlElement TFOOT = new AutoCloserHtmlElement("tfoot", new String[] { "tr", "td", "thead", "tfoot", "tbody", "caption", "colgroup" }, new String[] { "table" });
    static final IHtmlElement TR = new AutoCloserHtmlElement("tr", new String[] { "tr", "caption", "colgroup" }, new String[] { "table", "thead", "tbody", "tfoot" });
    static final IHtmlElement TD = new AutoCloserHtmlElement("td", new String[] { "td", "th" }, new String[] { "tr" });
    static final IHtmlElement TH = new AutoCloserHtmlElement("th", new String[] { "td", "th" }, new String[] { "tr" });
    
    // Forms
    static final IHtmlElement FORM = new AutoCloserHtmlElement("form", new String[] { "p" }, null);
    static final IHtmlElement FIELDSET = new AutoCloserHtmlElement("fieldset", new String[] { "p" }, null);
    static final IHtmlElement LEGEND = new BasicHtmlElement("legend");
    static final IHtmlElement LABEL = new BasicHtmlElement("label");
    static final IHtmlElement INPUT = new VoidHtmlElement("input");
    static final IHtmlElement BUTTON = new BasicHtmlElement("button");
    static final IHtmlElement SELECT = new BasicHtmlElement("select");
    static final IHtmlElement DATALIST = new BasicHtmlElement("datalist");
    static final IHtmlElement OPTGROUP = new AutoCloserHtmlElement("optgroup", new String[] { "optgroup", "option" }, new String[] { "select" });
    static final IHtmlElement OPTION = new AutoCloserHtmlElement("option", new String[] { "option" }, new String[] { "select", "optgroup", "datalist" });
    static final IHtmlElement TEXTAREA = new BasicHtmlElement("textarea");
    static final IHtmlElement KEYGEN = new VoidHtmlElement("keygen");
    static final IHtmlElement OUTPUT = new BasicHtmlElement("output");
    static final IHtmlElement PROGRESS = new BasicHtmlElement("progress");
    static final IHtmlElement METER = new BasicHtmlElement("meter");
    
    // Interactive elements
    static final IHtmlElement DETAILS = new BasicHtmlElement("details");
    static final IHtmlElement SUMMARY = new BasicHtmlElement("summary");
    static final IHtmlElement COMMAND = new BasicHtmlElement("command");
    static final IHtmlElement MENU = new AutoCloserHtmlElement("menu", new String[] { "p" }, null);
    static final IHtmlElement MENUITEM = new VoidHtmlElement("menuitem");
    static final IHtmlElement DIALOG = new BasicHtmlElement("dialog");
    
    

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
     * <p>
     *     This class is <strong>thread-safe</strong>. The reason for this is that it not only contains the
     *     standard elements, but will also contain new instances of IHtmlElement created during parsing (created
     *     when asking the repository for them when they do not exist yet. As any thread can create a new element,
     *     this has to be lock-protected.
     * </p>
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