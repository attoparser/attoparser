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
package org.attoparser.select;

import java.util.List;

import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;

/*
 *
 * @author Daniel Fernandez
 * @since 2.0.0
 */
public class MarkupSelectorItemsTest extends TestCase {

    private static TestingFragmentReferenceResolver referenceResolver = new TestingFragmentReferenceResolver();

    public MarkupSelectorItemsTest() {
        super();
    }

    
    public void test() throws Exception {

        checkNoRef(true, "//div/content()", "//div/content()");
        check(true, "//div", "(//div || //*[th:fragment='div' OR data-th-fragment='div'])");
        check(true, "//DIV", "(//div || //*[th:fragment='DIV' OR data-th-fragment='DIV'])");
        check(false, "//DIV", "(//DIV || //*[th:fragment='DIV' OR data-th-fragment='DIV'])");
        checkNoRef(true, "//div", "//div");
        check(true, ".main", "//*[class='main']");
        check(true, "#main", "//*[id='main']");
        check(true, "[class='main']", "//*[class='main']");
        check(true, "[id='main']", "//*[id='main']");
        check(true, "%ref", "(//* && //*[th:fragment='ref' OR data-th-fragment='ref'])");
        check(true, "p.main", "(//p[class='main'] || //*[class='main' AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "p#main", "(//p[id='main'] || //*[id='main' AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "P#main", "(//p[id='main'] || //*[id='main' AND (th:fragment='P' OR data-th-fragment='P')])");
        check(true, "p%ref", "(//p && //*[th:fragment='ref' OR data-th-fragment='ref'])");
        check(true, "/p.main", "(/p[class='main'] || /*[class='main' AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "/p#main", "(/p[id='main'] || /*[id='main' AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "/p%ref", "(/p && /*[th:fragment='ref' OR data-th-fragment='ref'])");
        check(true, "/P%REF", "(/p && /*[th:fragment='REF' OR data-th-fragment='REF'])");
        check(true, "/p%ref[a = 'x']", "(/p[a='x'] && /*[th:fragment='ref' OR data-th-fragment='ref'])");
        check(true, "/p.someclass[a = \"x\"]", "(/p[class='someclass' AND a='x'] || /*[(class='someclass' AND a='x') AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "/P.SOMECLASS[A = \"x\"]", "(/p[class='SOMECLASS' AND a='x'] || /*[(class='SOMECLASS' AND a='x') AND (th:fragment='P' OR data-th-fragment='P')])");
        check(true, "/P[CLASS = 'someClass' AND A = \"x\"]", "(/p[class='someClass' AND a='x'] || /*[(class='someClass' AND a='x') AND (th:fragment='P' OR data-th-fragment='P')])");
        check(false, "/P[CLASS = 'someClass' and A = \"x\"]", "(/P[CLASS='someClass' AND A='x'] || /*[(CLASS='someClass' AND A='x') AND (th:fragment='P' OR data-th-fragment='P')])");
        checkNoRef(false, "/P[CLASS = 'someClass' and A = \"x\"]", "/P[CLASS='someClass' AND A='x']");
        check(true, ".MAIN", "//*[class='MAIN']");
        check(true, "#MAIN", "//*[id='MAIN']");
        check(true, "%REF", "(//* && //*[th:fragment='REF' OR data-th-fragment='REF'])");
        check(true, "P.MAIN", "(//p[class='MAIN'] || //*[class='MAIN' AND (th:fragment='P' OR data-th-fragment='P')])");
        check(true, "P#MAIN", "(//p[id='MAIN'] || //*[id='MAIN' AND (th:fragment='P' OR data-th-fragment='P')])");
        check(true, "P%REF", "(//p && //*[th:fragment='REF' OR data-th-fragment='REF'])");
        checkNoRef(true, "P%REF", "//p");
        check(false, ".MAIN", "(//.MAIN || //*[th:fragment='.MAIN' OR data-th-fragment='.MAIN'])");
        check(false, "#MAIN", "(//#MAIN || //*[th:fragment='#MAIN' OR data-th-fragment='#MAIN'])");
        check(false, "%REF", "(//* && //*[th:fragment='REF' OR data-th-fragment='REF'])");
        check(false, "P.MAIN", "(//P.MAIN || //*[th:fragment='P.MAIN' OR data-th-fragment='P.MAIN'])");
        check(false, "P#MAIN", "(//P#MAIN || //*[th:fragment='P#MAIN' OR data-th-fragment='P#MAIN'])");
        check(false, "P%REF", "(//P && //*[th:fragment='REF' OR data-th-fragment='REF'])");
        check(true, "/p.someclass[a = \"x\" and b!='y']", "(/p[class='someclass' AND (a='x' AND b!='y')] || /*[(class='someclass' AND (a='x' AND b!='y')) AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "/p.someclass[a = \"x\" aNd b!='y' AND c  ^= 'z']", "(/p[class='someclass' AND (a='x' AND (b!='y' AND c^='z'))] || /*[(class='someclass' AND (a='x' AND (b!='y' AND c^='z'))) AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "/p.someclass[a = \"x\" or b!='y']", "(/p[class='someclass' AND (a='x' OR b!='y')] || /*[(class='someclass' AND (a='x' OR b!='y')) AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "/p.someclass[a = \"x\" or (b!='y')]", "(/p[class='someclass' AND (a='x' OR b!='y')] || /*[(class='someclass' AND (a='x' OR b!='y')) AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "/p.someclass[a = \"x\" OR (b!='y')]", "(/p[class='someclass' AND (a='x' OR b!='y')] || /*[(class='someclass' AND (a='x' OR b!='y')) AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "/p[a = \"x\" OR (b!='y')]", "(/p[a='x' OR b!='y'] || /*[(a='x' OR b!='y') AND (th:fragment='p' OR data-th-fragment='p')])");
        checkNoRef(true, "/p[a OR (b!='y')]", "/p[a* OR b!='y']");
        check(true, "[a OR (b!='y')]", "//*[a* OR b!='y']");
        checkNoRef(true, "[a OR (b!='y')]", "//*[a* OR b!='y']");
        check(true, "/p#someclass[a = \"x\" or (b!='y')]", "(/p[id='someclass' AND (a='x' OR b!='y')] || /*[(id='someclass' AND (a='x' OR b!='y')) AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "div[th:fragment='copy' or data-th-fragment='copy']","(//div[th:fragment='copy' OR data-th-fragment='copy'] || //*[(th:fragment='copy' OR data-th-fragment='copy') AND (th:fragment='div' OR data-th-fragment='div')])");
        check(true, "html//p","(//html || //*[th:fragment='html' OR data-th-fragment='html'])(//p || //*[th:fragment='p' OR data-th-fragment='p'])");
        checkNoRef(true, "html//p","//html//p");
        check(true, "html//p[2]","(//html || //*[th:fragment='html' OR data-th-fragment='html'])(//p[2] || //*[th:fragment='p' OR data-th-fragment='p'][2])");
        checkNoRef(true, "html//p[2]","//html//p[2]");
        checkNoRef(true, "html/comment()","//html/comment()");
        checkNoRef(true, "comment()","//comment()");
        checkNoRef(true, "p//comment()","//p//comment()");
        check(true, "p//comment()","(//p || //*[th:fragment='p' OR data-th-fragment='p'])//comment()");
        checkNoRef(true, "p//comment()[2]","//p//comment()[2]");
        checkNoRef(true, "p//comment()[even()]","//p//comment()[even()]");
        checkNoRef(true, "p//text()[2]","//p//text()[2]");
        checkNoRef(true, "p//text()[even()]","//p//text()[even()]");
        checkNoRef(true, "html/cdata()","//html/cdata()");
        checkNoRef(true, "cdata()","//cdata()");
        checkNoRef(true, "p//cdata()","//p//cdata()");
        check(true, "p//cdata()","(//p || //*[th:fragment='p' OR data-th-fragment='p'])//cdata()");
        checkNoRef(true, "p//cdata()[2]","//p//cdata()[2]");
        checkNoRef(true, "p//cdata()[even()]","//p//cdata()[even()]");
        checkNoRef(true, "html/doctype()","//html/doctype()");
        checkNoRef(true, "doctype()","//doctype()");
        checkNoRef(true, "p//doctype()","//p//doctype()");
        check(true, "p//doctype()","(//p || //*[th:fragment='p' OR data-th-fragment='p'])//doctype()");
        checkNoRef(true, "p//doctype()[2]","//p//doctype()[2]");
        checkNoRef(true, "p//doctype()[even()]","//p//doctype()[even()]");
        checkNoRef(true, "html/xmldecl()","//html/xmldecl()");
        checkNoRef(true, "xmldecl()","//xmldecl()");
        checkNoRef(true, "p//xmldecl()","//p//xmldecl()");
        check(true, "p//xmldecl()","(//p || //*[th:fragment='p' OR data-th-fragment='p'])//xmldecl()");
        checkNoRef(true, "p//xmldecl()[2]","//p//xmldecl()[2]");
        checkNoRef(true, "p//xmldecl()[even()]","//p//xmldecl()[even()]");
        checkNoRef(true, "html/procinstr()","//html/procinstr()");
        checkNoRef(true, "procinstr()","//procinstr()");
        checkNoRef(true, "p//procinstr()","//p//procinstr()");
        check(true, "p//procinstr()","(//p || //*[th:fragment='p' OR data-th-fragment='p'])//procinstr()");
        checkNoRef(true, "p//procinstr()[2]","//p//procinstr()[2]");
        checkNoRef(true, "p//procinstr()[even()]","//p//procinstr()[even()]");
        checkNoRef(true, "/p[a OR !b]", "/p[a* OR b!]");
        checkNoRef(true, "/p[a OR b!]", "/p[a* OR b!*]");
        check(true, "p[(id='x') OR (id='y')]", "(//p[id='x' OR id='y'] || //*[(id='x' OR id='y') AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "p[id='x'x'x'(']", "(//p[id='x'x'x'('] || //*[id='x'x'x'(' AND (th:fragment='p' OR data-th-fragment='p')])");
        check(true, "x'x'x'(", "(//x'x'x'( || //*[th:fragment='x'x'x'(' or data-th-fragment='x'x'x'('])");
        check(true, "x'x'x'", "(//x'x'x' || //*[th:fragment='x'x'x'' or data-th-fragment='x'x'x''])");
    }
    




    private static void check(final boolean html, final String blockSelector, final String expected) throws Exception{

        final List<IMarkupSelectorItem> items = MarkupSelectorItems.parseSelector(html, blockSelector, referenceResolver);
        final String result = StringUtils.join(items, "");
        assertEquals(expected, result);

    }

    private static void checkNoRef(final boolean html, final String blockSelector, final String expected) throws Exception{

        final List<IMarkupSelectorItem> items = MarkupSelectorItems.parseSelector(html, blockSelector, null);
        final String result = StringUtils.join(items, "");
        assertEquals(expected, result);

    }





    static final class TestingFragmentReferenceResolver implements IMarkupSelectorReferenceResolver {

        public String resolveSelectorFromReference(final String reference) {
            return "/[th:fragment='" + reference + "' or data-th-fragment='" + reference + "']";
        }
    }



}
