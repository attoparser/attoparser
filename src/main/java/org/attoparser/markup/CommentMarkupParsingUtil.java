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

import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;


/**
 * <p>
 *   Class containing utility methods for parsing comments.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class CommentMarkupParsingUtil {
    


    
    private CommentMarkupParsingUtil() {
        super();
    }

    
    

    
    
    
    
    public static IAttoHandleResult parseComment(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final ICommentHandling handler)
            throws AttoParseException {

        final BestEffortParsingResult bestEffortParsingResult =
                tryParseComment(buffer, offset, len, line, col, handler);
        if (bestEffortParsingResult == null) {
            throw new AttoParseException(
                    "Could not parse as markup comment: \"" + new String(buffer, offset, len) + "\"", line, col);
        }

        return bestEffortParsingResult.getHandleResult();

    }
    
    
    
    
    static BestEffortParsingResult tryParseComment(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final ICommentHandling handler)
            throws AttoParseException {

        if (len >= 7 &&
                isCommentStart(buffer, offset, offset + len) &&
                buffer[offset + len - 3] == '-' &&
                buffer[offset + len - 2] == '-' &&
                buffer[offset + len - 1] == '>') {
            return BestEffortParsingResult.forHandleResult(
                    handler.handleComment(buffer, offset + 4, len - 7, offset, len, line, col));
        }
        
        return null;
        
    }

    

    
    static boolean isCommentStart(final char[] buffer, final int offset, final int maxi) {
        return ((maxi - offset > 3) && 
                    buffer[offset] == '<' &&
                    buffer[offset + 1] == '!' &&
                    buffer[offset + 2] == '-' && 
                    buffer[offset + 3] == '-');
    }
    
}
