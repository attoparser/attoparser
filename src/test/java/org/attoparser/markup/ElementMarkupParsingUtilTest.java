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
package org.attoparser.markup;

import junit.framework.Assert;
import junit.framework.TestCase;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public class ElementMarkupParsingUtilTest extends TestCase {


    public void testcheckElementNameInArray() throws Exception {

        final char[][] names01 = null;
        final char[][] names02 = new char[][] { "a".toCharArray() };
        final char[][] names03 = new char[][] { "script".toCharArray(), "style".toCharArray() };

        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "a".toCharArray(), 0, 1, names01));
        Assert.assertEquals(
                0,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "a".toCharArray(), 0, 1, names02));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "a".toCharArray(), 0, 1, names03));

        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "ab".toCharArray(), 0, 2, names01));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "ab".toCharArray(), 0, 2, names02));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "ab".toCharArray(), 0, 2, names03));

        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "script".toCharArray(), 0, 6, names01));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "script".toCharArray(), 0, 6, names02));
        Assert.assertEquals(
                0,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "script".toCharArray(), 0, 6, names03));

        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "scriptb".toCharArray(), 0, 7, names01));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "scriptb".toCharArray(), 0, 7, names02));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "scriptb".toCharArray(), 0, 7, names03));

        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "style".toCharArray(), 0, 5, names01));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "style".toCharArray(), 0, 5, names02));
        Assert.assertEquals(
                1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "style".toCharArray(), 0, 5, names03));

        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "styleb".toCharArray(), 0, 6, names01));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "styleb".toCharArray(), 0, 6, names02));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "styleb".toCharArray(), 0, 6, names03));

        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "scrip".toCharArray(), 0, 5, names01));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "scrip".toCharArray(), 0, 5, names02));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "scrip".toCharArray(), 0, 5, names03));

        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "styl".toCharArray(), 0, 4, names01));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "styl".toCharArray(), 0, 4, names02));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "styl".toCharArray(), 0, 4, names03));


        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "scripw".toCharArray(), 0, 6, names01));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "scripw".toCharArray(), 0, 6, names02));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "scripw".toCharArray(), 0, 6, names03));

        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "stylw".toCharArray(), 0, 5, names01));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "stylw".toCharArray(), 0, 5, names02));
        Assert.assertEquals(
                -1,
                ElementMarkupParsingUtil.checkElementNameInArray(
                        "stylw".toCharArray(), 0, 5, names03));

    }
    
    
    
}
