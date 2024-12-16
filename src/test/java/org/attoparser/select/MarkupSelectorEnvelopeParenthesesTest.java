/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2025 Attoparser (https://www.attoparser.org)
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

import junit.framework.TestCase;

import static org.attoparser.select.MarkupSelectorItems.removeEnvelopingParentheses;

/*
 *
 * @author Daniel Fernandez
 * @since 2.0.6
 */
public class MarkupSelectorEnvelopeParenthesesTest extends TestCase {

    public MarkupSelectorEnvelopeParenthesesTest() {
        super();
    }

    
    public void test() throws Exception {
        assertEquals("", removeEnvelopingParentheses(""));
        assertEquals("", removeEnvelopingParentheses("()"));
        assertEquals("", removeEnvelopingParentheses("(())"));
        assertEquals("(", removeEnvelopingParentheses("("));
        assertEquals("(id='a') OR (id='b')", removeEnvelopingParentheses("(id='a') OR (id='b')"));
        assertEquals("(id='a') OR (id='b')", removeEnvelopingParentheses("((id='a') OR (id='b'))"));
        assertEquals("(id='a') OR (id='b')", removeEnvelopingParentheses("(((id='a') OR (id='b')))"));
        assertEquals("(id='a') OR (id='b')", removeEnvelopingParentheses("(((id='a') OR (id='b')))"));
        assertEquals("id='a'", removeEnvelopingParentheses("(id='a')"));
        assertEquals("id='a'", removeEnvelopingParentheses("((id='a'))"));
        assertEquals("id='a'", removeEnvelopingParentheses("(((id='a')))"));
        assertEquals("id='(a'", removeEnvelopingParentheses("(id='(a')"));
        assertEquals("id='a)'", removeEnvelopingParentheses("(id='a)')"));
        assertEquals("(id='a)'", removeEnvelopingParentheses("(id='a)'"));
        assertEquals("(id='a)", removeEnvelopingParentheses("(id='a)"));
        assertEquals("((id='a)')", removeEnvelopingParentheses("((id='a)')"));
    }
    



}
