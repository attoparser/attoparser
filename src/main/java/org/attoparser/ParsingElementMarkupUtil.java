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
 * Class containing utility methods for parsing elements (tags).
 *
 * @author Daniel Fern&aacute;ndez
 * @since 2.0.0
 */
public final class ParsingElementMarkupUtil {


    

    
    private ParsingElementMarkupUtil() {
        super();
    }




    public static void parseStandaloneElement(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final IMarkupHandler markupHandler)
            throws ParseException {

        if (len < 4 || !isOpenElementStart(buffer, offset, offset + len) || !isElementEnd(buffer, (offset + len) - 2, offset + len, true)) {
            throw new ParseException(
                    "Could not parse as a well-formed standalone element: \"" + new String(buffer, offset, len) + "\"", line, col);
        }

        final int contentOffset = offset + 1;
        final int contentLen = len - 3;

        final int maxi = contentOffset + contentLen;
        
        final int[] locator = new int[] {line, col + 1};
        
        /*
         * Extract the element name first 
         */
        
        final int elementNameEnd = 
            ParsingMarkupUtil.findNextWhitespaceCharWildcard(buffer, contentOffset, maxi, true, locator);
        
        if (elementNameEnd == -1) {
            // The buffer only contains the element name
            
            markupHandler.handleStandaloneElementStart(
                    buffer, contentOffset, contentLen,
                    true, line, col);

            markupHandler.handleStandaloneElementEnd(
                    buffer, contentOffset, contentLen,
                    true, locator[0], locator[1]);

            return;

        }


        markupHandler.handleStandaloneElementStart(
                buffer, contentOffset, (elementNameEnd - contentOffset),
                true, line, col);


        // This parseAttributeSequence will take care of calling handleInnerWhitespace when appropriate.
        ParsingAttributeSequenceUtil.parseAttributeSequence(
                buffer, elementNameEnd, maxi - elementNameEnd, locator[0], locator[1], markupHandler);

        // We need to forward the locator to the position corresponding with the element end (note we are discarding result)
        ParsingMarkupUtil.findNextStructureEndAvoidQuotes(buffer, elementNameEnd, maxi, locator);

        markupHandler.handleStandaloneElementEnd(
                buffer, contentOffset, (elementNameEnd - contentOffset),
                true, locator[0], locator[1]);

    }




    public static void parseOpenElement(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final IMarkupHandler markupHandler)
            throws ParseException {

        if (len < 3 || !isOpenElementStart(buffer, offset, offset + len) || !isElementEnd(buffer, (offset + len) - 1, offset + len, false)) {
            throw new ParseException(
                    "Could not parse as a well-formed open element: \"" + new String(buffer, offset, len) + "\"", line, col);
        }

        final int contentOffset = offset + 1;
        final int contentLen = len - 2;

        final int maxi = contentOffset + contentLen;

        final int[] locator = new int[] {line, col + 1};

        /*
         * Extract the element name first
         */

        final int elementNameEnd =
                ParsingMarkupUtil.findNextWhitespaceCharWildcard(buffer, contentOffset, maxi, true, locator);

        if (elementNameEnd == -1) {
            // The buffer only contains the element name

            markupHandler.handleOpenElementStart(
                    buffer, contentOffset, contentLen,
                    line, col);

            markupHandler.handleOpenElementEnd(
                    buffer, contentOffset, contentLen,
                    locator[0], locator[1]);

            return;

        }


        markupHandler.handleOpenElementStart(
                buffer, contentOffset, (elementNameEnd - contentOffset),
                line, col);


        // This parseAttributeSequence will take care of calling handleInnerWhitespace when appropriate.
        ParsingAttributeSequenceUtil.parseAttributeSequence(
                buffer, elementNameEnd, maxi - elementNameEnd, locator[0], locator[1], markupHandler);

        // We need to forward the locator to the position corresponding with the element end (note we are discarding result)
        ParsingMarkupUtil.findNextStructureEndAvoidQuotes(buffer, elementNameEnd, maxi, locator);

        markupHandler.handleOpenElementEnd(
                buffer, contentOffset, (elementNameEnd - contentOffset),
                locator[0], locator[1]);

    }




    public static void parseCloseElement(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final IMarkupHandler markupHandler)
            throws ParseException {

        if (len < 4 || !isCloseElementStart(buffer, offset, offset + len) || !isElementEnd(buffer, (offset + len) - 1, offset + len, false)) {
            throw new ParseException(
                    "Could not parse as a well-formed close element: \"" + new String(buffer, offset, len) + "\"", line, col);
        }

        final int contentOffset = offset + 2;
        final int contentLen = len - 3;

        final int maxi = contentOffset + contentLen;
        
        final int[] locator = new int[] {line, col + 2};
        
        /*
         * Extract the element name first 
         */
        
        final int elementNameEnd = 
            ParsingMarkupUtil.findNextWhitespaceCharWildcard(buffer, contentOffset, maxi, true, locator);
        
        if (elementNameEnd == -1) {
            // The buffer only contains the element name

            markupHandler.handleCloseElementStart(
                    buffer, contentOffset, contentLen,
                    line, col);

            markupHandler.handleCloseElementEnd(
                    buffer, contentOffset, contentLen,
                    locator[0], locator[1]);
            
            return;
            
        }


        markupHandler.handleCloseElementStart(
                buffer, contentOffset, (elementNameEnd - contentOffset),
                line, col);
        
               
        int currentArtifactLine = locator[0];
        int currentArtifactCol = locator[1];
        
        final int wsEnd = 
            ParsingMarkupUtil.findNextNonWhitespaceCharWildcard(buffer, elementNameEnd, maxi, locator);
        
        if (wsEnd != -1) {
            // This is a close tag, so everything should be whitespace
            // until the end of the close tag
            throw new ParseException(
                    "Could not parse as a well-formed closing element " +
                    "\"</" + new String(buffer, contentOffset, contentLen) + ">\"" +
                    ": No attributes are allowed here", line, col);
        }


        markupHandler.handleInnerWhiteSpace(buffer, elementNameEnd, (maxi - elementNameEnd), currentArtifactLine, currentArtifactCol);


        markupHandler.handleCloseElementEnd(
                buffer, contentOffset, (elementNameEnd - contentOffset),
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


    static boolean isElementEnd(final char[] buffer, final int offset, final int maxi, final boolean minimized) {

        final int len = maxi - offset;

        if (len < 1) {
            return false; // won't fit
        }

        if (minimized) {
            if (len < 2 || buffer[offset] != '/') {
                return false;
            }
            return buffer[offset + 1] == '>';
        }

        return buffer[offset] == '>';

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
