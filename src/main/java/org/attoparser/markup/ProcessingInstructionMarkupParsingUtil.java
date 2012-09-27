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



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class ProcessingInstructionMarkupParsingUtil {

    

    
    private ProcessingInstructionMarkupParsingUtil() {
        super();
    }

    
    

    
    
    
    
    public static void parseProcessingInstruction(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IProcessingInstructionHandling handler)
            throws AttoParseException {
        
        if (!tryParseProcessingInstruction(buffer, offset, len, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as processing instruction: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        
    }

    

    
    
    public static boolean tryParseProcessingInstruction(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IProcessingInstructionHandling handler)
            throws AttoParseException {

        if (len >= 8 && 
                isProcessingInstructionStart(buffer, offset, (offset + len)) &&
                buffer[offset + len - 2] == '?' &&
                buffer[offset + len - 1] == '>') {
            handler.processingInstruction(buffer, 0, 0, offset + 2, len - 4, offset, len, line, col);
            return true;
        }
        
        return false;
        
    }


    
    static boolean isProcessingInstructionStart(final char[] buffer, final int offset, final int maxi) {
        return ((maxi - offset > 5) && 
                    buffer[offset] == '<' &&
                    buffer[offset + 1] == '?' &&
                    (buffer[offset + 2] == 'X' || buffer[offset + 2] == 'x') && 
                    (buffer[offset + 3] == 'M' || buffer[offset + 3] == 'm') && 
                    (buffer[offset + 4] == 'L' || buffer[offset + 4] == 'l') && 
                    Character.isWhitespace(buffer[offset + 5]));
    }

    
}
