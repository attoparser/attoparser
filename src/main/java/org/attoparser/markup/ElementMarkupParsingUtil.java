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
 * <p>
 *   Class containing utility methods for parsing elements (<i>tags</i>).
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class ElementMarkupParsingUtil {


    

    
    private ElementMarkupParsingUtil() {
        super();
    }

    
    

    
    
    
    
    public static void parseElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IBasicElementHandling handler)
            throws AttoParseException {
        
        if (!tryParseElement(buffer, offset, len, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as markup element: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        
    }
    
    
    
    public static boolean tryParseElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col,
            final IBasicElementHandling handler)
            throws AttoParseException {

        if (len > 3 &&
                isCloseElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>') {
            
            handler.handleCloseElement(buffer, offset + 2, len - 3, offset, len, line, col);
            return true;
            
        } else if (len > 3 &&
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 2] == '/' &&
                buffer[offset + len - 1] == '>') {
            
            handler.handleStandaloneElement(buffer, offset + 1, len - 3, offset, len, line, col);
            return true;
            
        } else if (len > 2 && 
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>'){
            
            handler.handleOpenElement(buffer, offset + 1, len - 2, offset, len, line, col);
            return true;
            
        }
        
        return false;
        
    }
    
    
    
    
    
    
    
    
    public static void parseDetailedStandaloneElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {
        
        if (!tryParseDetailedStandaloneElement(buffer, offset, len, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as a broken down standalone tag: \"" + new String(buffer, offset, len) + "\"", line, col);
        }

    }
    
    
    public static void parseDetailedOpenElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {
        
        if (!tryParseDetailedOpenElement(buffer, offset, len, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as a broken down opening tag: \"" + new String(buffer, offset, len) + "\"", line, col);
        }

    }
    
    
    public static void parseDetailedCloseElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {
        
        if (!tryParseDetailedCloseElement(buffer, offset, len, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as a broken down closing tag: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        
    }
    
    

    public static boolean tryParseDetailedStandaloneElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {
        
        if (len > 3 &&
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 2] == '/' &&
                buffer[offset + len - 1] == '>') {
            doTryParseDetailedOpenOrStandaloneElement(
                    buffer, offset + 1, len - 3, offset, len, line, col, handler, true);
            return true;
        }
        return false;

    }
    
    
    public static boolean tryParseDetailedOpenElement(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {
        
        if (len > 2 && 
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>'){
            doTryParseDetailedOpenOrStandaloneElement(
                    buffer, offset + 1, len - 2, offset, len, line, col, handler, false);
            return true;
        }
        return false;
        
    }
    
    
    public static boolean tryParseDetailedCloseElement(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {

        if (len > 3 &&
                isCloseElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>') {
            doTryParseDetailedCloseElement(
                    buffer, offset + 2, len - 3, offset, len, line, col, handler);
            return true;
        }
        return false;
        
    }
    

    

    
    
    private static void doTryParseDetailedOpenOrStandaloneElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IDetailedElementHandling handler, 
            final boolean isStandalone)
            throws AttoParseException {

        if (isStandalone) {
            handler.handleStandaloneElementStart(buffer, outerOffset, 1, line, col);
        } else {
            handler.handleOpenElementStart(buffer, outerOffset, 1, line, col);
        }
        
        final int maxi = contentOffset + contentLen;
        
        final int[] locator = new int[] {line, col + 1};
        
        /*
         * Extract the element name first 
         */
        
        final int elementNameEnd = 
            MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, contentOffset, maxi, true, locator);
        
        if (elementNameEnd == -1) {
            // The buffer only contains the element name
            
            if (isStandalone) {
                handler.handleStandaloneElementName(
                        buffer, contentOffset, contentLen, 
                        line, col + 1);
                handler.handleStandaloneElementEnd(
                        buffer, (outerOffset + outerLen - 2), 2, 
                        locator[0], locator[1]);
            } else {
                handler.handleOpenElementName(
                        buffer, contentOffset, contentLen, 
                        line, col + 1);
                handler.handleOpenElementEnd(
                        buffer, (outerOffset + outerLen - 1), 1, 
                        locator[0], locator[1]);
            }
            
            return;
            
        }

        
        if (isStandalone) {
            handler.handleStandaloneElementName(
                    buffer, contentOffset, (elementNameEnd - contentOffset),
                    line, col + 1);
        } else {
            handler.handleOpenElementName(
                    buffer, contentOffset, (elementNameEnd - contentOffset),
                    line, col + 1);
        }
        
        
        AttributeSequenceMarkupParsingUtil.parseAttributeSequence(
                buffer, elementNameEnd, maxi - elementNameEnd, locator, handler);

        
        
        if (isStandalone) {
            handler.handleStandaloneElementEnd(
                    buffer, (outerOffset + outerLen - 2), 2, 
                    locator[0], locator[1]);
        } else {
            handler.handleOpenElementEnd(
                    buffer, (outerOffset + outerLen - 1), 1, 
                    locator[0], locator[1]);
        }
        
        return;
        
    }


    
    
    private static void doTryParseDetailedCloseElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {

        handler.handleCloseElementStart(buffer, outerOffset, 2, line, col);
        
        final int maxi = contentOffset + contentLen;
        
        final int[] locator = new int[] {line, col + 2};
        
        /*
         * Extract the element name first 
         */
        
        final int elementNameEnd = 
            MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, contentOffset, maxi, true, locator);
        
        if (elementNameEnd == -1) {
            // The buffer only contains the element name
        
            handler.handleCloseElementName(
                    buffer, contentOffset, contentLen, 
                    line, col + 2);
            handler.handleCloseElementEnd(
                    buffer, (outerOffset + outerLen - 1), 1, 
                    locator[0], locator[1]);
            
            return;
            
        }

        
        handler.handleCloseElementName(
                buffer, contentOffset, (elementNameEnd - contentOffset),
                line, col + 2);
        
               
        int i = elementNameEnd;
        int current = i;

        int currentArtifactLine = locator[0];
        int currentArtifactCol = locator[1];
        
        final int wsEnd = 
            MarkupParsingUtil.findNextNonWhitespaceCharWildcard(buffer, i, maxi, locator);
        
        if (wsEnd != -1) {
            // This is a close tag, so everything should be whitespace
            // until the end of the close tag
            throw new AttoParseException(
                    "Could not parse as a well-formed closing element " +
                    "\"" + new String(buffer, outerOffset, outerLen) + "\"" +
                    ": No attributes are allowed here", line, col);
        }
        
        final int wsOffset = current;
        final int wsLen = maxi - current;
        handler.handleAttributeSeparator(buffer, wsOffset, wsLen, currentArtifactLine, currentArtifactCol);

        
        handler.handleCloseElementEnd(
                buffer, (outerOffset + outerLen - 1), 1, 
                locator[0], locator[1]);
        
    }
    
    

    
    
    
    static boolean isOpenElementStart(final char[] buffer, final int offset, final int maxi) {
        
        final int len = maxi - offset;
        
        return (len > 1 && 
                    buffer[offset] == '<' &&
                    isElementName(buffer, offset + 1, maxi));
        
    }

    
    static boolean isCloseElementStart(final char[] buffer, final int offset, final int maxi) {
        
        final int len = maxi - offset;
        
        return (len > 2 && 
                    buffer[offset] == '<' &&
                    buffer[offset + 1] == '/' &&
                    isElementName(buffer, offset + 2, maxi));
        
    }


    
    
    private static boolean isElementName(final char[] buffer, final int offset, final int maxi) {
        
        final int len = maxi - offset;
        
        if (len > 1 && buffer[offset] == '!') {
            if (len > 8) {
                return (buffer[offset + 1] != '-' && buffer[offset + 1] != '!' && 
                        buffer[offset + 1] != '/' && buffer[offset + 1] != '?' && 
                        buffer[offset + 1] != '[' &&
                        !((buffer[offset + 1] == 'D' || buffer[offset + 1] == 'd') && 
                          (buffer[offset + 2] == 'O' || buffer[offset + 2] == 'o') && 
                          (buffer[offset + 3] == 'C' || buffer[offset + 3] == 'c') && 
                          (buffer[offset + 4] == 'T' || buffer[offset + 4] == 't') && 
                          (buffer[offset + 5] == 'Y' || buffer[offset + 5] == 'y') && 
                          (buffer[offset + 6] == 'P' || buffer[offset + 6] == 'p') && 
                          (buffer[offset + 7] == 'E' || buffer[offset + 7] == 'e') &&
                          (Character.isWhitespace(buffer[offset + 8]) || buffer[offset + 8] == '>'))); 
            }
            return (buffer[offset + 1] != '-' && buffer[offset + 1] != '!' && 
                    buffer[offset + 1] != '/' && buffer[offset + 1] != '?' && 
                    buffer[offset + 1] != '[' && !Character.isWhitespace(buffer[offset + 1])); 
        }
        return (len > 0 &&
                buffer[offset] != '-' && buffer[offset] != '!' && 
                buffer[offset] != '/' && buffer[offset] != '?' && 
                buffer[offset] != '[' && !Character.isWhitespace(buffer[offset]));
        
    }
    
    
    
}
