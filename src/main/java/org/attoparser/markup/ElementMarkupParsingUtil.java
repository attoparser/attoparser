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









    /*
     * We need this structure in order to determine whether the parsing did not succeed as an element
     * (return == FALSE constant) or rather it suceeded but the result means that parsing should NOT be disabled until
     * a specific char[] is found in input (return == null)
     */
    public static final char[] TRY_PARSE_ELEMENT_FALSE = new char[] { (char)0 };

    public static char[] parseElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IBasicElementHandling handler)
            throws AttoParseException {

        final char[] tryParseElementResult = tryParseElement(buffer, offset, len, line, col, handler);
        if (tryParseElementResult == TRY_PARSE_ELEMENT_FALSE) {
            throw new AttoParseException(
                    "Could not parse as markup element: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        return tryParseElementResult;

    }

    public static char[] tryParseElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col,
            final IBasicElementHandling handler)
            throws AttoParseException {

        if (len > 3 &&
                isCloseElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>') {
            
            return handler.handleCloseElement(buffer, offset + 2, len - 3, offset, len, line, col);

        } else if (len > 3 &&
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 2] == '/' &&
                buffer[offset + len - 1] == '>') {
            
            return handler.handleStandaloneElement(buffer, offset + 1, len - 3, offset, len, line, col);

        } else if (len > 2 && 
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>'){
            
            return handler.handleOpenElement(buffer, offset + 1, len - 2, offset, len, line, col);

        }
        
        return TRY_PARSE_ELEMENT_FALSE;
        
    }
    
    
    
    
    
    
    
    
    public static <T extends IDetailedElementHandling & IConfigurableMarkupHandling> char[] parseDetailedStandaloneElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final T handler)
            throws AttoParseException {

        final char[] tryParseDefailedStandaloneElementResult =
                tryParseDetailedStandaloneElement(buffer, offset, len, line, col, handler);
        if (tryParseDefailedStandaloneElementResult == TRY_PARSE_ELEMENT_FALSE) {
            throw new AttoParseException(
                    "Could not parse as a broken down standalone tag: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        return tryParseDefailedStandaloneElementResult;

    }
    
    
    public static <T extends IDetailedElementHandling & IConfigurableMarkupHandling> char[] parseDetailedOpenElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final T handler)
            throws AttoParseException {

        final char[] tryParseDefailedOpenElementResult =
                tryParseDetailedOpenElement(buffer, offset, len, line, col, handler);
        if (tryParseDefailedOpenElementResult == TRY_PARSE_ELEMENT_FALSE) {
            throw new AttoParseException(
                    "Could not parse as a broken down opening tag: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        return tryParseDefailedOpenElementResult;

    }
    
    
    public static <T extends IDetailedElementHandling & IConfigurableMarkupHandling> char[] parseDetailedCloseElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final T handler)
            throws AttoParseException {

        final char[] tryParseDefailedCloseElementResult =
                tryParseDetailedCloseElement(buffer, offset, len, line, col, handler);
        if (tryParseDefailedCloseElementResult == TRY_PARSE_ELEMENT_FALSE) {
            throw new AttoParseException(
                    "Could not parse as a broken down closing tag: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        return tryParseDefailedCloseElementResult;

    }
    
    

    public static <T extends IDetailedElementHandling & IConfigurableMarkupHandling> char[] tryParseDetailedStandaloneElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final T handler)
            throws AttoParseException {
        
        if (len > 3 &&
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 2] == '/' &&
                buffer[offset + len - 1] == '>') {
            return doTryParseDetailedOpenOrStandaloneElement(
                    buffer, offset + 1, len - 3, offset, len, line, col, handler, true);
        }
        return TRY_PARSE_ELEMENT_FALSE;

    }
    
    
    public static <T extends IDetailedElementHandling & IConfigurableMarkupHandling> char[] tryParseDetailedOpenElement(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col, 
            final T handler)
            throws AttoParseException {
        
        if (len > 2 && 
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>'){
            return doTryParseDetailedOpenOrStandaloneElement(
                    buffer, offset + 1, len - 2, offset, len, line, col, handler, false);
        }
        return TRY_PARSE_ELEMENT_FALSE;
        
    }
    
    
    public static <T extends IDetailedElementHandling & IConfigurableMarkupHandling> char[] tryParseDetailedCloseElement(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col, 
            final T handler)
            throws AttoParseException {

        if (len > 3 &&
                isCloseElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>') {
            return doTryParseDetailedCloseElement(
                    buffer, offset + 2, len - 3, offset, len, line, col, handler);
        }
        return TRY_PARSE_ELEMENT_FALSE;
        
    }
    

    

    
    
    private static <T extends IDetailedElementHandling & IConfigurableMarkupHandling> char[] doTryParseDetailedOpenOrStandaloneElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            @SuppressWarnings("unused") final int outerOffset, @SuppressWarnings("unused") final int outerLen, 
            final int line, final int col, 
            final T handler,
            final boolean isStandalone)
            throws AttoParseException {

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

                handler.handleStandaloneElementStart(
                        buffer, contentOffset, contentLen, 
                        line, col);
                handler.handleStandaloneElementEnd(
                        locator[0], locator[1]);

            } else {

                handler.handleOpenElementStart(
                        buffer, contentOffset, contentLen, 
                        line, col);
                handler.handleOpenElementEnd(
                        locator[0], locator[1]);

                /*
                 * Once the events have been fired, we need to know whether we must disable parsing until
                 * the "close element" structure is found. This will enable support for certain elements
                 * which contents must not be parsed (e.g. in HTML, <script> and <style> tags).
                 */

                final char[][] nonProcessableElementNames =
                        handler.getMarkupParsingConfiguration().getNonProcessableElementNames();
                final int nonProcessableIndex =
                    checkElementNameInArray(buffer, contentOffset, contentLen, nonProcessableElementNames);

                if (nonProcessableIndex >= 0) {

                    final char[] closingSequence = new char[nonProcessableElementNames[nonProcessableIndex].length + 3];
                    closingSequence[0] = '<';
                    closingSequence[1] = '/';
                    System.arraycopy(
                            nonProcessableElementNames[nonProcessableIndex], 0,
                            closingSequence, 2,
                            nonProcessableElementNames[nonProcessableIndex].length);
                    closingSequence[closingSequence.length - 1] = '>';

                    return closingSequence;

                }

            }
            
            return null;
            
        }

        
        if (isStandalone) {
            handler.handleStandaloneElementStart(
                    buffer, contentOffset, (elementNameEnd - contentOffset),
                    line, col);
        } else {
            handler.handleOpenElementStart(
                    buffer, contentOffset, (elementNameEnd - contentOffset),
                    line, col);
        }
        
        
        AttributeSequenceMarkupParsingUtil.parseAttributeSequence(
                buffer, elementNameEnd, maxi - elementNameEnd, locator, handler);

        
        
        if (isStandalone) {
            handler.handleStandaloneElementEnd(
                    locator[0], locator[1]);
        } else {
            handler.handleOpenElementEnd(
                    locator[0], locator[1]);
        }



        /*
         * Once the events have been fired, we need to know whether we must disable parsing until
         * the "close element" structure is found. This will enable support for certain elements
         * which contents must not be parsed (e.g. in HTML, <script> and <style> tags).
         */

        if (!isStandalone) {

            final char[][] nonProcessableElementNames =
                    handler.getMarkupParsingConfiguration().getNonProcessableElementNames();
            final int nonProcessableIndex =
                    checkElementNameInArray(buffer, contentOffset, (elementNameEnd - contentOffset), nonProcessableElementNames);

            if (nonProcessableIndex >= 0) {

                final char[] closingSequence = new char[nonProcessableElementNames[nonProcessableIndex].length + 3];
                closingSequence[0] = '<';
                closingSequence[1] = '/';
                System.arraycopy(
                        nonProcessableElementNames[nonProcessableIndex], 0,
                        closingSequence, 2,
                        nonProcessableElementNames[nonProcessableIndex].length);
                closingSequence[closingSequence.length - 1] = '>';

                return closingSequence;

            }

        }

        
        return null;
        
    }


    
    
    private static <T extends IDetailedElementHandling & IConfigurableMarkupHandling> char[] doTryParseDetailedCloseElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final T handler)
            throws AttoParseException {

        final int maxi = contentOffset + contentLen;
        
        final int[] locator = new int[] {line, col + 2};
        
        /*
         * Extract the element name first 
         */
        
        final int elementNameEnd = 
            MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, contentOffset, maxi, true, locator);
        
        if (elementNameEnd == -1) {
            // The buffer only contains the element name
        
            handler.handleCloseElementStart(
                    buffer, contentOffset, contentLen, 
                    line, col);
            handler.handleCloseElementEnd(
                    locator[0], locator[1]);
            
            return null;
            
        }

        
        handler.handleCloseElementStart(
                buffer, contentOffset, (elementNameEnd - contentOffset),
                line, col);
        
               
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
        handler.handleInnerWhiteSpace(buffer, wsOffset, wsLen, currentArtifactLine, currentArtifactCol);

        
        handler.handleCloseElementEnd(
                locator[0], locator[1]);

        return null;

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







    static int checkElementNameInArray(
            final char[] buffer, final int nameOffset, final int nameLen, final char[][] names) {

        if (names == null) {
            return -1;
        }

        for (int i = 0; i < names.length; i++) {

            if (nameLen != names[i].length) {
                // Name doesn't fit in structure!
                continue;
            }

            int j = 0;
            for ( ; j < names[i].length; j++) {
                if (buffer[nameOffset + j] != names[i][j]) {
                    break;
                }
            }

            if (j >= names[i].length) {
                return i;
            }

        }

        return -1;

    }


    
}
