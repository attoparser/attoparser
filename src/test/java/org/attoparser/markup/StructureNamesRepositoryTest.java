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

import junit.framework.TestCase;
import org.attoparser.markup.html.elements.HtmlElements;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public class StructureNamesRepositoryTest extends TestCase {



    public void test() throws Exception {

        final AbstractDetailedMarkupAttoHandler.StructureNamesRepository structureNamesRepository = new AbstractDetailedMarkupAttoHandler.StructureNamesRepository();


        final char[][] elementNamesCharArr = new char[HtmlElements.ALL_STANDARD_ELEMENT_NAMES.size()][];
        int j = 0;
        for (final String elementName : HtmlElements.ALL_STANDARD_ELEMENT_NAMES) {
            elementNamesCharArr[j++] =
                    structureNamesRepository.getStructureName(elementName.toCharArray(), 0, elementName.length());
        }


        for (int i = 0; i < 1000000; i++) {
            for (final char[] elementName : elementNamesCharArr) {
                final char[] sn =
                        structureNamesRepository.getStructureName(elementName, 0, elementName.length);
                assertSame(elementName, sn);
            }
        }



    }



}
