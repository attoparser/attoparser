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

/**
 * <p>
 *   Class containing utility methods for parsing CDATA sections.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
final class ParsingCDATASectionMarkupUtil {

    

    
    private ParsingCDATASectionMarkupUtil() {
        super();
    }

    



    
    static boolean isCDATASectionStart(final char[] buffer, final int offset, final int maxi) {
        return ((maxi - offset > 8) && 
                    buffer[offset] == '<' &&
                    buffer[offset + 1] == '!' &&
                    buffer[offset + 2] == '[' && 
                    (buffer[offset + 3] == 'C' || buffer[offset + 3] == 'c') && 
                    (buffer[offset + 4] == 'D' || buffer[offset + 4] == 'd') && 
                    (buffer[offset + 5] == 'A' || buffer[offset + 5] == 'a') && 
                    (buffer[offset + 6] == 'T' || buffer[offset + 6] == 't') && 
                    (buffer[offset + 7] == 'A' || buffer[offset + 7] == 'a') && 
                    buffer[offset + 8] == '[');
    }

    
}
