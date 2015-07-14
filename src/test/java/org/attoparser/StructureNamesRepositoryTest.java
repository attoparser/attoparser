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


/*
 *
 * @author Daniel Fernandez
 * @since 2.0.0
 */
public class StructureNamesRepositoryTest extends TestCase {



    public void test() throws Exception {

        final MarkupEventProcessorHandler.StructureNamesRepository structureNamesRepository = new MarkupEventProcessorHandler.StructureNamesRepository();


        final char[][] structureNamesArr = new char[HtmlNames.ALL_STANDARD_ELEMENT_NAMES.size() * 2 + HtmlNames.ALL_STANDARD_ATTRIBUTE_NAMES.size() * 2][];
        int j = 0;
        for (final String elementName : HtmlNames.ALL_STANDARD_ELEMENT_NAMES) {
            structureNamesArr[j++] =
                    structureNamesRepository.getStructureName(elementName.toCharArray(), 0, elementName.length());
            structureNamesArr[j++] =
                    structureNamesRepository.getStructureName(elementName.toUpperCase().toCharArray(), 0, elementName.length());
        }
        for (final String attributeName : HtmlNames.ALL_STANDARD_ATTRIBUTE_NAMES) {
            structureNamesArr[j++] =
                    structureNamesRepository.getStructureName(attributeName.toCharArray(), 0, attributeName.length());
            structureNamesArr[j++] =
                    structureNamesRepository.getStructureName(attributeName.toUpperCase().toCharArray(), 0, attributeName.length());
        }


        for (int i = 0; i < 1000000; i++) {
            for (final char[] structureName : structureNamesArr) {
                final char[] sn =
                        structureNamesRepository.getStructureName(structureName, 0, structureName.length);
                assertSame(structureName, sn);
            }
        }



    }



}
