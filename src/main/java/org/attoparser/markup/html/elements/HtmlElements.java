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
            
    


    // Root
    public static final IHtmlElement HTML = new DefaultHtmlElement("html");
    
    // Document metadata
    public static final IHtmlElement HEAD = new DefaultHtmlElement("head");
    public static final IHtmlElement TITLE = new DefaultHtmlElement("title");
    public static final IHtmlElement BASE = new DefaultHtmlElement("base");
    public static final IHtmlElement LINK = new DefaultHtmlElement("link");
    public static final IHtmlElement META = new DefaultHtmlElement("meta");
    public static final IHtmlElement STYLE = new DefaultHtmlElement("style");
    
    // Scripting
    public static final IHtmlElement SCRIPT = new DefaultHtmlElement("script");
    public static final IHtmlElement NOSCRIPT = new DefaultHtmlElement("noscript");
    
    // Sections
    public static final IHtmlElement BODY = new DefaultHtmlElement("body");
    public static final IHtmlElement ARTICLE = new DefaultHtmlElement("article");
    public static final IHtmlElement SECTION = new DefaultHtmlElement("section");
    public static final IHtmlElement NAV = new DefaultHtmlElement("nav");
    public static final IHtmlElement ASIDE = new DefaultHtmlElement("aside");
    public static final IHtmlElement H1 = new DefaultHtmlElement("h1");
    public static final IHtmlElement H2 = new DefaultHtmlElement("h2");
    public static final IHtmlElement H3 = new DefaultHtmlElement("h3");
    public static final IHtmlElement H4 = new DefaultHtmlElement("h4");
    public static final IHtmlElement H5 = new DefaultHtmlElement("h5");
    public static final IHtmlElement H6 = new DefaultHtmlElement("h6");
    public static final IHtmlElement HGROUP = new DefaultHtmlElement("hgroup");
    public static final IHtmlElement HEADER = new DefaultHtmlElement("header");
    public static final IHtmlElement FOOTER = new DefaultHtmlElement("footer");
    public static final IHtmlElement ADDRESS = new DefaultHtmlElement("address");
    
    // Grouping content
    public static final IHtmlElement P = new DefaultHtmlElement("p");
    public static final IHtmlElement HR = new DefaultHtmlElement("hr");
    public static final IHtmlElement PRE = new DefaultHtmlElement("pre");
    public static final IHtmlElement BLOCKQUOTE = new DefaultHtmlElement("blockquote");
    public static final IHtmlElement OL = new DefaultHtmlElement("ol");
    public static final IHtmlElement UL = new DefaultHtmlElement("ul");
    public static final IHtmlElement LI = new DefaultHtmlElement("li");
    public static final IHtmlElement DL = new DefaultHtmlElement("dl");
    public static final IHtmlElement DT = new DefaultHtmlElement("dt");
    public static final IHtmlElement DD = new DefaultHtmlElement("dd");
    public static final IHtmlElement FIGURE = new DefaultHtmlElement("figure");
    public static final IHtmlElement FIGCAPTION = new DefaultHtmlElement("figcaption");
    public static final IHtmlElement DIV = new DefaultHtmlElement("div");
    
    // Text-level semantics
    public static final IHtmlElement A = new DefaultHtmlElement("a");
    public static final IHtmlElement EM = new DefaultHtmlElement("em");
    public static final IHtmlElement STRONG = new DefaultHtmlElement("strong");
    public static final IHtmlElement SMALL = new DefaultHtmlElement("small");
    public static final IHtmlElement S = new DefaultHtmlElement("s");
    public static final IHtmlElement CITE = new DefaultHtmlElement("cite");
    public static final IHtmlElement G = new DefaultHtmlElement("g");
    public static final IHtmlElement DFN = new DefaultHtmlElement("dfn");
    public static final IHtmlElement ABBR = new DefaultHtmlElement("abbr");
    public static final IHtmlElement TIME = new DefaultHtmlElement("time");
    public static final IHtmlElement CODE = new DefaultHtmlElement("code");
    public static final IHtmlElement VAR = new DefaultHtmlElement("var");
    public static final IHtmlElement SAMP = new DefaultHtmlElement("samp");
    public static final IHtmlElement KBD = new DefaultHtmlElement("kbd");
    public static final IHtmlElement SUB = new DefaultHtmlElement("sub");
    public static final IHtmlElement SUP = new DefaultHtmlElement("sup");
    public static final IHtmlElement I = new DefaultHtmlElement("i");
    public static final IHtmlElement B = new DefaultHtmlElement("b");
    public static final IHtmlElement U = new DefaultHtmlElement("u");
    public static final IHtmlElement MARK = new DefaultHtmlElement("mark");
    public static final IHtmlElement RUBY = new DefaultHtmlElement("ruby");
    public static final IHtmlElement RT = new DefaultHtmlElement("rt");
    public static final IHtmlElement RP = new DefaultHtmlElement("rp");
    public static final IHtmlElement BDI = new DefaultHtmlElement("bdi");
    public static final IHtmlElement BDO = new DefaultHtmlElement("bdo");
    public static final IHtmlElement SPAN = new DefaultHtmlElement("span");
    public static final IHtmlElement BR = new DefaultHtmlElement("br");
    public static final IHtmlElement WBR = new DefaultHtmlElement("wbr");
    
    // Edits
    public static final IHtmlElement INS = new DefaultHtmlElement("ins");
    public static final IHtmlElement DEL = new DefaultHtmlElement("del");
    
    // Embedded content
    public static final IHtmlElement IMG = new DefaultHtmlElement("img");
    public static final IHtmlElement IFRAME = new DefaultHtmlElement("iframe");
    public static final IHtmlElement EMBED = new DefaultHtmlElement("embed");
    public static final IHtmlElement OBJECT = new DefaultHtmlElement("object");
    public static final IHtmlElement PARAM = new DefaultHtmlElement("param");
    public static final IHtmlElement VIDEO = new DefaultHtmlElement("video");
    public static final IHtmlElement AUDIO = new DefaultHtmlElement("audio");
    public static final IHtmlElement SOURCE = new DefaultHtmlElement("source");
    public static final IHtmlElement TRACK = new DefaultHtmlElement("track");
    public static final IHtmlElement CANVAS = new DefaultHtmlElement("canvas");
    public static final IHtmlElement MAP = new DefaultHtmlElement("map");
    public static final IHtmlElement AREA = new DefaultHtmlElement("area");
    
    // Tabular data
    public static final IHtmlElement TABLE = new DefaultHtmlElement("table");
    public static final IHtmlElement CAPTION = new DefaultHtmlElement("caption");
    public static final IHtmlElement COLGROUP = new DefaultHtmlElement("colgroup");
    public static final IHtmlElement COL = new DefaultHtmlElement("col");
    public static final IHtmlElement TBODY = new DefaultHtmlElement("tbody");
    public static final IHtmlElement THEAD = new DefaultHtmlElement("thead");
    public static final IHtmlElement TFOOT = new DefaultHtmlElement("tfoot");
    public static final IHtmlElement TR = new DefaultHtmlElement("tr");
    public static final IHtmlElement TD = new DefaultHtmlElement("td");
    public static final IHtmlElement TH = new DefaultHtmlElement("th");
    
    // Forms
    public static final IHtmlElement FORM = new DefaultHtmlElement("form");
    public static final IHtmlElement FIELDSET = new DefaultHtmlElement("fieldset");
    public static final IHtmlElement LEGEND = new DefaultHtmlElement("legend");
    public static final IHtmlElement LABEL = new DefaultHtmlElement("label");
    public static final IHtmlElement INPUT = new DefaultHtmlElement("input");
    public static final IHtmlElement BUTTON = new DefaultHtmlElement("button");
    public static final IHtmlElement SELECT = new DefaultHtmlElement("select");
    public static final IHtmlElement DATALIST = new DefaultHtmlElement("datalist");
    public static final IHtmlElement OPTGROUP = new DefaultHtmlElement("optgroup");
    public static final IHtmlElement OPTION = new DefaultHtmlElement("option");
    public static final IHtmlElement TEXTAREA = new DefaultHtmlElement("textarea");
    public static final IHtmlElement KEYGEN = new DefaultHtmlElement("keygen");
    public static final IHtmlElement OUTPUT = new DefaultHtmlElement("output");
    public static final IHtmlElement PROGRESS = new DefaultHtmlElement("progress");
    public static final IHtmlElement METER = new DefaultHtmlElement("meter");
    
    // Interactive elements
    public static final IHtmlElement DETAILS = new DefaultHtmlElement("details");
    public static final IHtmlElement SUMMARY = new DefaultHtmlElement("summary");
    public static final IHtmlElement COMMAND = new DefaultHtmlElement("command");
    public static final IHtmlElement MENU = new DefaultHtmlElement("menu");
    public static final IHtmlElement DIALOG = new DefaultHtmlElement("dialog");
    
    
    
    
    
    
    
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
            
            if (c >= 'a' && c <= 'z') {
                return (c - 'a');
            } else if (c >= 'A' && c <= 'Z') {
                return ((char)(c + CASE_DIFF) - 'a');
            } else {
                // Non-alphabetic will go after position 'z'
                return ('z' + 1);
            }
            
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
    
    

    
    

    public static void main(String[] args) {

        
        System.out.println(lookFor("A"));
        System.out.println(lookFor("link"));
        System.out.println(lookFor("html"));
        System.out.println(lookFor("META"));

        System.out.println(ELEMENTS.toString());
        
        
        final long start = System.currentTimeMillis();
        
        for (int i = 0; i < 10000000; i++) {
            IHtmlElement e1 = lookFor("A");
            IHtmlElement e2 = lookFor("link");
            IHtmlElement e3 = lookFor("html");
            IHtmlElement e4 = lookFor("META");
        }
        
        final long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start) + "ms");
        
    }
    
    
}