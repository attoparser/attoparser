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
package org.attoparser;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
final class MarkupParsingLocator {
    
    public int line;
    public int col;
    
    
    MarkupParsingLocator() {
        super();
        this.line = 1;
        this.col = 1;
    }
    
    MarkupParsingLocator(final int line, final int col) {
        super();
        this.line = line;
        this.col = col;
    }

    public static void countChar(final MarkupParsingLocator locator, final char c) {
        if (c == '\n') {
            locator.line++;
            locator.col = 1;
        } else {
            locator.col++;
        }
    }
    
}