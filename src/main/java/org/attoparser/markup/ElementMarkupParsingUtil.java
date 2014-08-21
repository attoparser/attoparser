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

import org.attoparser.AttoHandleResultUtil;
import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;


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








    public static IAttoHandleResult parseElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IBasicElementHandling handler)
            throws AttoParseException {

        final BestEffortParsingResult bestEffortResult =
                tryParseElement(buffer, offset, len, line, col, handler);
        if (bestEffortResult == null) {
            throw new AttoParseException(
                    "Could not parse as markup element: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        return bestEffortResult.getHandleResult();

    }



    public static BestEffortParsingResult tryParseElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col,
            final IBasicElementHandling handler)
            throws AttoParseException {

        if (len > 3 &&
                isCloseElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>') {
            
            return BestEffortParsingResult.forHandleResult(
                    handler.handleCloseElement(buffer, offset + 2, len - 3, offset, len, line, col));

        } else if (len > 3 &&
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 2] == '/' &&
                buffer[offset + len - 1] == '>') {
            
            return BestEffortParsingResult.forHandleResult(
                    handler.handleStandaloneElement(buffer, offset + 1, len - 3, offset, len, line, col));

        } else if (len > 2 && 
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>'){
            
            return BestEffortParsingResult.forHandleResult(
                    handler.handleOpenElement(buffer, offset + 1, len - 2, offset, len, line, col));

        }
        
        return null;
        
    }
    
    
    
    
    
    
    
    
    public static IAttoHandleResult parseDetailedStandaloneElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {

        final BestEffortParsingResult bestEffortResult =
                tryParseDetailedStandaloneElement(buffer, offset, len, line, col, handler);
        if (bestEffortResult == null) {
            throw new AttoParseException(
                    "Could not parse as a broken down standalone tag: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        return bestEffortResult.getHandleResult();

    }
    
    
    public static IAttoHandleResult parseDetailedOpenElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {

        final BestEffortParsingResult bestEffortResult =
                tryParseDetailedOpenElement(buffer, offset, len, line, col, handler);
        if (bestEffortResult == null) {
            throw new AttoParseException(
                    "Could not parse as a broken down opening tag: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        return bestEffortResult.getHandleResult();

    }
    
    
    public static IAttoHandleResult parseDetailedCloseElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {

        final BestEffortParsingResult bestEffortResult =
                tryParseDetailedCloseElement(buffer, offset, len, line, col, handler);
        if (bestEffortResult == null) {
            throw new AttoParseException(
                    "Could not parse as a broken down closing tag: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        return bestEffortResult.getHandleResult();

    }
    
    

    public static BestEffortParsingResult tryParseDetailedStandaloneElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {
        
        if (len > 3 &&
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 2] == '/' &&
                buffer[offset + len - 1] == '>') {
            return BestEffortParsingResult.forHandleResult(
                    doTryParseDetailedOpenOrStandaloneElement(
                            buffer, offset + 1, len - 3, offset, len, line, col, handler, true));
        }
        return null;

    }
    
    
    public static BestEffortParsingResult tryParseDetailedOpenElement(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {
        
        if (len > 2 && 
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>'){
            return BestEffortParsingResult.forHandleResult(
                    doTryParseDetailedOpenOrStandaloneElement(
                            buffer, offset + 1, len - 2, offset, len, line, col, handler, false));
        }
        return null;
        
    }
    
    
    public static BestEffortParsingResult tryParseDetailedCloseElement(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
            throws AttoParseException {

        if (len > 3 &&
                isCloseElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>') {
            return BestEffortParsingResult.forHandleResult(
                    doTryParseDetailedCloseElement(
                            buffer, offset + 2, len - 3, offset, len, line, col, handler));
        }
        return null;
        
    }
    

    

    
    
    private static IAttoHandleResult doTryParseDetailedOpenOrStandaloneElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            @SuppressWarnings("unused") final int outerOffset, @SuppressWarnings("unused") final int outerLen, 
            final int line, final int col, 
            final IDetailedElementHandling handler,
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

                final IAttoHandleResult handleResult1 =
                        handler.handleStandaloneElementStart(
                            buffer, contentOffset, contentLen,
                            line, col);
                final IAttoHandleResult handleResult2 =
                        handler.handleStandaloneElementEnd(
                                locator[0], locator[1]);

                return AttoHandleResultUtil.combinePriorityLast(handleResult1, handleResult2);

            } else {

                final IAttoHandleResult handleResult1 =
                        handler.handleOpenElementStart(
                                buffer, contentOffset, contentLen,
                                line, col);
                final IAttoHandleResult handleResult2 =
                        handler.handleOpenElementEnd(
                                locator[0], locator[1]);

                return AttoHandleResultUtil.combinePriorityLast(handleResult1, handleResult2);

            }

        }


        final IAttoHandleResult handleResult1;
        if (isStandalone) {
            handleResult1 =
                    handler.handleStandaloneElementStart(
                            buffer, contentOffset, (elementNameEnd - contentOffset),
                            line, col);
        } else {
            handleResult1 =
                    handler.handleOpenElementStart(
                            buffer, contentOffset, (elementNameEnd - contentOffset),
                            line, col);
        }


        // This parseAttributeSequence will take care of calling handleInnerWhitespace when appropriate.
        final IAttoHandleResult handleResult2 =
                AttributeSequenceMarkupParsingUtil.parseAttributeSequence(
                        buffer, elementNameEnd, maxi - elementNameEnd, locator, handler);



        final IAttoHandleResult handleResult3;
        if (isStandalone) {
            handleResult3 =
                    handler.handleStandaloneElementEnd(
                            locator[0], locator[1]);
        } else {
            handleResult3 =
                    handler.handleOpenElementEnd(
                            locator[0], locator[1]);
        }



        return AttoHandleResultUtil.combinePriorityLast(
                        AttoHandleResultUtil.combinePriorityLast(handleResult1, handleResult2),
                        handleResult3);
        
    }


    
    
    private static IAttoHandleResult doTryParseDetailedCloseElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IDetailedElementHandling handler)
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

            final IAttoHandleResult handleResult1 =
                    handler.handleCloseElementStart(
                            buffer, contentOffset, contentLen,
                            line, col);
            final IAttoHandleResult handleResult2 =
                    handler.handleCloseElementEnd(
                            locator[0], locator[1]);
            
            return AttoHandleResultUtil.combinePriorityLast(handleResult1,handleResult2);
            
        }


        final IAttoHandleResult handleResult1 =
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

        final IAttoHandleResult handleResult2 =
                handler.handleInnerWhiteSpace(buffer, wsOffset, wsLen, currentArtifactLine, currentArtifactCol);


        final IAttoHandleResult handleResult3 =
                handler.handleCloseElementEnd(
                        locator[0], locator[1]);


        return AttoHandleResultUtil.combinePriorityLast(
                AttoHandleResultUtil.combinePriorityLast(handleResult1, handleResult2),
                handleResult3);

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
