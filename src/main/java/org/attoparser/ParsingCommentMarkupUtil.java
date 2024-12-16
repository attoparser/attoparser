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
package org.attoparser;

/**
 * Class containing utility methods for parsing comments.
 *
 * @author Daniel Fern&aacute;ndez
 * @since 2.0.0
 */
public final class ParsingCommentMarkupUtil {
    


    
    private ParsingCommentMarkupUtil() {
        super();
    }



    public static void parseComment(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final ICommentHandler handler)
            throws ParseException {

        if (len < 7 || !isCommentStart(buffer, offset, offset + len) || !isCommentEnd(buffer, (offset + len) - 3, offset + len)) {
            throw new ParseException(
                    "Could not parse as a well-formed Comment: \"" + new String(buffer, offset, len) + "\"", line, col);
        }

        final int contentOffset = offset + 4;
        final int contentLen = len - 7;

        handler.handleComment(
                buffer,
                contentOffset, contentLen,
                offset, len,
                line, col);

    }



    
    static boolean isCommentStart(final char[] buffer, final int offset, final int maxi) {
        return ((maxi - offset > 3) && 
                    buffer[offset] == '<' &&
                    buffer[offset + 1] == '!' &&
                    buffer[offset + 2] == '-' && 
                    buffer[offset + 3] == '-');
    }


    static boolean isCommentEnd(final char[] buffer, final int offset, final int maxi) {
        return ((maxi - offset > 2) &&
                buffer[offset] == '-' &&
                buffer[offset + 1] == '-' &&
                buffer[offset + 2] == '>');
    }

}
