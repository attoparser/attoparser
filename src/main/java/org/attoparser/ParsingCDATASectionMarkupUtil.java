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
package org.attoparser;

/**
 * Class containing utility methods for parsing CDATA sections.
 *
 * @author Daniel Fern&aacute;ndez
 * @since 2.0.0
 */
public final class ParsingCDATASectionMarkupUtil {

    

    
    private ParsingCDATASectionMarkupUtil() {
        super();
    }






    public static void parseCDATASection(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final ICDATASectionHandler handler)
            throws ParseException {

        if (len < 12 || !isCDATASectionStart(buffer, offset, offset + len) || !isCDATASectionEnd(buffer, (offset + len) - 3, offset + len)) {
            throw new ParseException(
                    "Could not parse as a well-formed CDATA Section: \"" + new String(buffer, offset, len) + "\"", line, col);
        }

        final int contentOffset = offset + 9;
        final int contentLen = len - 12;

        handler.handleCDATASection(
                buffer,
                contentOffset, contentLen,
                offset, len,
                line, col);

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


    static boolean isCDATASectionEnd(final char[] buffer, final int offset, final int maxi) {
        return ((maxi - offset > 2) &&
                buffer[offset] == ']' &&
                buffer[offset + 1] == ']' &&
                buffer[offset + 2] == '>');
    }

    
}
