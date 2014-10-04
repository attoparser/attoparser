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
 * <p>
 *   Class containing utility methods for parsing processing instructions.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
final class ParsingProcessingInstructionUtil {

    

    
    private ParsingProcessingInstructionUtil() {
        super();
    }

    
    

    
    
    static void parseProcessingInstruction(
            final char[] buffer,
            final int internalOffset, final int internalLen,
            final int outerOffset, final int outerLen,
            final int line, final int col,
            final MarkupEventProcessor eventProcessor)
            throws ParseException {

        final int maxi = internalOffset + internalLen;
        
        final int[] locator = new int[] {line, col + 2};
        
        int i = internalOffset;
        
        /*
         * Extract the target 
         */
        
        final int targetEnd = 
            ParsingMarkupUtil.findNextWhitespaceCharWildcard(buffer, i, maxi, false, locator);
        
        if (targetEnd == -1) {
            // There is no content, only target
            
            eventProcessor.processProcessingInstruction(
                    buffer, 
                    i, maxi - i,                                      // target
                    line, col + 2,                                    // target
                    0, 0,                                             // content
                    locator[0], locator[1],                           // content
                    outerOffset, outerLen,                            // outer 
                    line, col);                                       // outer
            return;

        }
        
        
        
        final int targetOffset = i;
        final int targetLen = targetEnd - targetOffset;
        
        i = targetEnd;

        
        /*
         * Fast-forward to the content
         */
        
        final int contentStart = 
                ParsingMarkupUtil.findNextNonWhitespaceCharWildcard(buffer, i, maxi, locator);

        if (contentStart == -1) {
            // There is no content. Only whitespace until the end of the structure
            
            eventProcessor.processProcessingInstruction(
                    buffer, 
                    targetOffset, targetLen,                          // target
                    line, col + 2,                                    // target
                    0, 0,                                             // content
                    locator[0], locator[1],                           // content
                    outerOffset, outerLen,                            // outer 
                    line, col);                                       // outer
            return;

        }

        
        eventProcessor.processProcessingInstruction(
                buffer, 
                targetOffset, targetLen,                          // target
                line, col + 2,                                    // target
                contentStart, maxi - contentStart,                // content
                locator[0], locator[1],                           // content
                outerOffset, outerLen,                            // outer 
                line, col);                                       // outer

    }
    
    
    
    static boolean isProcessingInstructionStart(final char[] buffer, final int offset, final int maxi) {
        
        final int len = maxi - offset;
        
        if (len > 5) {
            // Make sure we do not match an XML declaration by error.
            
            return (buffer[offset] == '<' &&
                    buffer[offset + 1] == '?' &&
                    (buffer[offset + 2] != ' ' && !Character.isWhitespace(buffer[offset + 2])) &&
                    !(
                      buffer[offset + 2] == 'x' && 
                      buffer[offset + 3] == 'm' && 
                      buffer[offset + 4] == 'l' && 
                      Character.isWhitespace(buffer[offset + 5])
                    ));
            
        }
        
        return (len > 2 && 
                buffer[offset] == '<' &&
                buffer[offset + 1] == '?' &&
                (buffer[offset + 2] != ' ' && !Character.isWhitespace(buffer[offset + 2])));
        
    }

    
    
}
