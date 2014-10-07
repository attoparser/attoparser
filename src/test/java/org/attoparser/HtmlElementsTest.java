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

import junit.framework.TestCase;
import org.attoparser.HtmlElement;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public class HtmlElementsTest extends TestCase {


    public void test() throws Exception {

        {
            final HtmlElement element1 = HtmlElements.forName("something".toCharArray(), 0, "something".length());
            final HtmlElement element2 = HtmlElements.forName("Something".toCharArray(), 0, "Something".length());
            assertSame(element1, element2);
        }

        {

            final HtmlElement element1 = HtmlElements.forName("something2".toCharArray(), 0, "something2".length());
            final HtmlElement element2 = HtmlElements.forName("Something2".toCharArray(), 0, "Something2".length());
            assertSame(element1, element2);
        }

        {
            final HtmlElement element1 = HtmlElements.forName("somethiNG".toCharArray(), 0, "something".length());
            final HtmlElement element2 = HtmlElements.forName("something".toCharArray(), 0, "something".length());
            final HtmlElement element3 = HtmlElements.forName("Something".toCharArray(), 0, "Something".length());
            assertSame(element1, element2);
            assertSame(element2, element3);
        }

        {

            final HtmlElement element1 = HtmlElements.forName("somethiNG2".toCharArray(), 0, "somethiNG2".length());
            final HtmlElement element2 = HtmlElements.forName("something2".toCharArray(), 0, "Something2".length());
            final HtmlElement element3 = HtmlElements.forName("Something2".toCharArray(), 0, "Something2".length());
            assertSame(element1, element2);
            assertSame(element2, element3);
        }

        for (final String elementName : HtmlNames.ALL_STANDARD_ELEMENT_NAMES) {
            final HtmlElement element1 = HtmlElements.forName(elementName.toCharArray(), 0, elementName.length());
            final HtmlElement element2 = HtmlElements.forName(elementName.toUpperCase().toCharArray(), 0, elementName.toUpperCase().length());
            assertSame(element1, element2);
        }



    }

    
}
