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
public final class ElementMarkupParsingUtil {


    

    
    private ElementMarkupParsingUtil() {
        super();
    }

    
    

    
    
    
    
    public static void parseElement(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IElementHandling handler)
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
            final IElementHandling handler)
            throws AttoParseException {

        if (len > 3 &&
                isCloseElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>') {
            
            handler.closeElement(buffer, offset + 2, len - 3, offset, len, line, col);
            return true;
            
        } else if (len > 3 &&
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 2] == '/' &&
                buffer[offset + len - 1] == '>') {
            
            handler.standaloneElement(buffer, offset + 1, len - 3, offset, len, line, col);
            return true;
            
        } else if (len > 2 && 
                isOpenElementStart(buffer, offset, offset + len) &&
                buffer[offset + len - 1] == '>'){
            
            handler.openElement(buffer, offset + 1, len - 2, offset, len, line, col);
            return true;
            
        }
        
        return false;
        
    }
    
    
    
    
    
    
    
    
    public static void parseStandaloneElementBreakDown(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IElementBreakDownHandling handler)
            throws AttoParseException {
        
        if (!tryParseStandaloneElementBreakDown(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as a broken down standalone tag: \"" + new String(buffer, outerOffset, outerLen) + "\"", line, col);
        }

    }
    
    
    public static void parseOpenElementBreakDown(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IElementBreakDownHandling handler)
            throws AttoParseException {
        
        if (!tryParseOpenElementBreakDown(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as a broken down opening tag: \"" + new String(buffer, outerOffset, outerLen) + "\"", line, col);
        }

    }
    
    
    public static void parseCloseElementBreakDown(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IElementBreakDownHandling handler)
            throws AttoParseException {
        
        if (!tryParseCloseElementBreakDown(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as a broken down closing tag: \"" + new String(buffer, outerOffset, outerLen) + "\"", line, col);
        }
        
    }
    
    

    public static boolean tryParseStandaloneElementBreakDown(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IElementBreakDownHandling handler)
            throws AttoParseException {
        return doTryParseOpenOrStandaloneElementBreakDown(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, handler, true);
    }
    
    
    public static boolean tryParseOpenElementBreakDown(
            final char[] buffer,
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IElementBreakDownHandling handler)
            throws AttoParseException {
        return doTryParseOpenOrStandaloneElementBreakDown(
                buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, handler, false);
    }
    
    
    public static boolean tryParseCloseElementBreakDown(
            final char[] buffer,
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IElementBreakDownHandling handler)
            throws AttoParseException {

        return doTryParseCloseElementBreakDown(
                buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, handler);
        
    }
    

    

    
    
    private static boolean doTryParseOpenOrStandaloneElementBreakDown(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IElementBreakDownHandling handler, 
            final boolean isStandalone)
            throws AttoParseException {

        if (isStandalone) {
            handler.standaloneElementStart(buffer, outerOffset, 1, line, col);
        } else {
            handler.openElementStart(buffer, outerOffset, 1, line, col);
        }
        
        final int maxi = contentOffset + contentLen;
        
        final MarkupParsingLocator elementBreakdownLocator = new MarkupParsingLocator(line, col + 1);
        
        /*
         * Extract the element name first 
         */
        
        final int elementNameEnd = 
            MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, contentOffset, maxi, true, elementBreakdownLocator);
        
        if (elementNameEnd == -1) {
            // The buffer only contains the element name
            
            if (isStandalone) {
                handler.standaloneElementName(
                        buffer, contentOffset, contentLen, 
                        line, col + 1);
                handler.standaloneElementEnd(
                        buffer, (outerOffset + outerLen - 2), 2, 
                        elementBreakdownLocator.line, elementBreakdownLocator.col);
            } else {
                handler.openElementName(
                        buffer, contentOffset, contentLen, 
                        line, col + 1);
                handler.openElementEnd(
                        buffer, (outerOffset + outerLen - 1), 1, 
                        elementBreakdownLocator.line, elementBreakdownLocator.col);
            }
            
            return true;
            
        }

        
        if (isStandalone) {
            handler.standaloneElementName(
                    buffer, contentOffset, (elementNameEnd - contentOffset),
                    line, col + 1);
        } else {
            handler.openElementName(
                    buffer, contentOffset, (elementNameEnd - contentOffset),
                    line, col + 1);
        }
        
               
        int i = elementNameEnd;
        int current = i;

        int currentArtifactLine = elementBreakdownLocator.line;
        int currentArtifactCol = elementBreakdownLocator.col;
        
        while (i < maxi) {

            /*
             * STEP ONE: Look for whitespaces between attributes
             */

            currentArtifactLine = elementBreakdownLocator.line;
            currentArtifactCol = elementBreakdownLocator.col;
            
            final int wsEnd = 
                    MarkupParsingUtil.findNextNonWhitespaceCharWildcard(buffer, i, maxi, elementBreakdownLocator);
            
            if (wsEnd == -1) {
                // Everything is whitespace until the end of the tag
                
                final int wsOffset = current;
                final int wsLen = maxi - current;
                handler.elementWhitespace(buffer, wsOffset, wsLen, currentArtifactLine, currentArtifactCol);
                i = maxi;
                continue;
                
            }
            
            if (wsEnd > current) {
                // We avoid empty whitespace fragments
                final int wsOffset = current;
                final int wsLen = wsEnd - current;
                handler.elementWhitespace(buffer, wsOffset, wsLen, currentArtifactLine, currentArtifactCol);
                i = wsEnd;
                current = i;
            }
            

            
            /*
             * STEP TWO: Detect the attribute name
             */

            
            currentArtifactLine = elementBreakdownLocator.line;
            currentArtifactCol = elementBreakdownLocator.col;
            
            final int attributeNameEnd = 
                    MarkupParsingUtil.findNextOperatorCharWildcard(buffer, i, maxi, elementBreakdownLocator);
            
            if (attributeNameEnd == -1) {
                // This is a no-value and no-equals-sign attribute, equivalent to value = ""
                
                final int attributeNameOffset = current;
                final int attributeNameLen = maxi - current;
                handler.elementAttribute(
                        buffer,                                                               // name 
                        attributeNameOffset, attributeNameLen,                                // name
                        currentArtifactLine, currentArtifactCol,                              // name
                        0, 0,                                                                 // operator
                        elementBreakdownLocator.line, elementBreakdownLocator.col,            // operator
                        0, 0, 0, 0,                                                           // value
                        elementBreakdownLocator.line, elementBreakdownLocator.col);           // value
                i = maxi;
                continue;
                
            }
            
            if (attributeNameEnd <= current) {
                // This attribute name starts by an equals sign, which is forbidden
                return false;  
            }

            
            final int attributeNameOffset = current;
            final int attributeNameLen = attributeNameEnd - current;
            final int attributeNameLine = currentArtifactLine;
            final int attributeNameCol = currentArtifactCol;
            i = attributeNameEnd;
            current = i;
            

            
            /*
             * STEP THREE: Detect the operator
             */

            
            currentArtifactLine = elementBreakdownLocator.line;
            currentArtifactCol = elementBreakdownLocator.col;

            final int operatorEnd = 
                    MarkupParsingUtil.findNextNonOperatorCharWildcard(buffer, i, maxi, elementBreakdownLocator);
            
            if (operatorEnd == -1) {
                // This could be: 
                //    1. A no-value and no-equals-sign attribute
                //    2. A no-value WITH equals sign attribute
                
                final int operatorOffset = current;
                final int operatorLen = (maxi - current);
                
                boolean equalsPresent = false;
                for (int j = i; j < maxi; j++) {
                    if (buffer[j] == '=') {
                        equalsPresent = true;
                        break;
                    }
                }
                
                if (equalsPresent) {
                    // It is a no value with equals, so we will consider everything
                    // to be an operator
                    
                    handler.elementAttribute(
                            buffer,                                                                // name 
                            attributeNameOffset, attributeNameLen,                                 // name 
                            attributeNameLine, attributeNameCol,                                   // name
                            operatorOffset, operatorLen,                                           // operator 
                            currentArtifactLine, currentArtifactCol,                               // operator
                            0, 0, 0, 0,                                                            // value
                            elementBreakdownLocator.line, elementBreakdownLocator.col);            // value
                    
                } else {
                    // There is no "=", so we will first output the attribute with no
                    // operator and then a whitespace
                    
                    handler.elementAttribute(
                            buffer,                                                                // name 
                            attributeNameOffset, attributeNameLen,                                 // name 
                            attributeNameLine, attributeNameCol,                                   // name
                            0, 0,                                                                  // operator 
                            currentArtifactLine, currentArtifactCol,                               // operator
                            0, 0, 0, 0,                                                            // value
                            currentArtifactLine, currentArtifactCol);                              // value
                    
                    handler.elementWhitespace(
                            buffer, 
                            operatorOffset, operatorLen, 
                            currentArtifactLine, currentArtifactCol);
                    
                }
                
                i = maxi;
                continue;
                
                // end: (operatorEnd == -1)
            }

            
            boolean equalsPresent = false;
            for (int j = current; j < operatorEnd; j++) {
                if (buffer[j] == '=') {
                    equalsPresent = true;
                    break;
                }
            }
            
            if (!equalsPresent) {
                // It is not an operator, but a whitespace between this and the next attribute,
                // so we will first output the attribute with no operator and then a whitespace
                
                handler.elementAttribute(
                        buffer,                                                                // name 
                        attributeNameOffset, attributeNameLen,                                 // name 
                        attributeNameLine, attributeNameCol,                                   // name
                        0, 0,                                                                  // operator 
                        currentArtifactLine, currentArtifactCol,                               // operator
                        0, 0, 0, 0,                                                            // value
                        currentArtifactLine, currentArtifactCol);                              // value
                
                handler.elementWhitespace(
                        buffer, 
                        current, (operatorEnd - current), 
                        currentArtifactLine, currentArtifactCol);
                
                i = operatorEnd;
                current = i;
                continue;
                
            }

            
            final int operatorOffset = current;
            final int operatorLen = operatorEnd - current;
            final int operatorLine = currentArtifactLine;
            final int operatorCol = currentArtifactCol;
            i = operatorEnd;
            current = i;
            

            
            /*
             * STEP FOUR: Detect the value
             */

            
            currentArtifactLine = elementBreakdownLocator.line;
            currentArtifactCol = elementBreakdownLocator.col;

            final boolean attributeEndsWithQuotes = (i < maxi && buffer[current] == '"');
            final int valueEnd =
                (attributeEndsWithQuotes?
                        MarkupParsingUtil.findNextAnyCharAvoidQuotesWildcard(buffer, i, maxi, elementBreakdownLocator) :
                        MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, i, maxi, false, elementBreakdownLocator));
            
            if (valueEnd == -1) {
                // This value ends the attribute
                
                final int valueOuterOffset = current;
                final int valueOuterLen = maxi - current;
                int valueContentOffset = valueOuterOffset;
                int valueContentLen = valueOuterLen;
                
                if (valueOuterLen >= 2 && 
                        buffer[valueOuterOffset] == '"' && buffer[valueOuterOffset + valueOuterLen -1] == '"') {
                    valueContentOffset = valueOuterOffset + 1;
                    valueContentLen = valueOuterLen - 2;
                }
                        
                handler.elementAttribute(
                        buffer,                                                               // name 
                        attributeNameOffset, attributeNameLen,                                // name
                        attributeNameLine, attributeNameCol,                                  // name
                        operatorOffset, operatorLen,                                          // operator
                        operatorLine, operatorCol,                                            // operator
                        valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, // value
                        currentArtifactLine, currentArtifactCol);                             // value
                i = maxi;
                continue;
                
            }

            
            final int valueOuterOffset = current;
            final int valueOuterLen = valueEnd - current;
            int valueContentOffset = valueOuterOffset;
            int valueContentLen = valueOuterLen;
            
            if (valueOuterLen >= 2 && 
                    buffer[valueOuterOffset] == '"' && buffer[valueOuterOffset + valueOuterLen -1] == '"') {
                valueContentOffset = valueOuterOffset + 1;
                valueContentLen = valueOuterLen - 2;
            }
                    
            handler.elementAttribute(
                    buffer,                                                               // name 
                    attributeNameOffset, attributeNameLen,                                // name
                    attributeNameLine, attributeNameCol,                                  // name
                    operatorOffset, operatorLen,                                          // operator
                    operatorLine, operatorCol,                                            // operator
                    valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, // value
                    currentArtifactLine, currentArtifactCol);                             // value

            i = valueEnd;
            current = i;
            
        }
        
        if (isStandalone) {
            handler.standaloneElementEnd(
                    buffer, (outerOffset + outerLen - 2), 2, 
                    elementBreakdownLocator.line, elementBreakdownLocator.col);
        } else {
            handler.openElementEnd(
                    buffer, (outerOffset + outerLen - 1), 1, 
                    elementBreakdownLocator.line, elementBreakdownLocator.col);
        }
        
        return true;
        
    }


    
    
    private static boolean doTryParseCloseElementBreakDown(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IElementBreakDownHandling handler)
            throws AttoParseException {

        handler.closeElementStart(buffer, outerOffset, 2, line, col);
        
        final int maxi = contentOffset + contentLen;
        
        final MarkupParsingLocator elementBreakdownLocator = new MarkupParsingLocator(line, col + 2);
        
        /*
         * Extract the element name first 
         */
        
        final int elementNameEnd = 
            MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, contentOffset, maxi, true, elementBreakdownLocator);
        
        if (elementNameEnd == -1) {
            // The buffer only contains the element name
        
            handler.closeElementName(
                    buffer, contentOffset, contentLen, 
                    line, col + 2);
            handler.closeElementEnd(
                    buffer, (outerOffset + outerLen - 1), 1, 
                    elementBreakdownLocator.line, elementBreakdownLocator.col);
            
            return true;
            
        }

        
        handler.closeElementName(
                buffer, contentOffset, (elementNameEnd - contentOffset),
                line, col + 2);
        
               
        int i = elementNameEnd;
        int current = i;

        int currentArtifactLine = elementBreakdownLocator.line;
        int currentArtifactCol = elementBreakdownLocator.col;
        
        final int wsEnd = 
            MarkupParsingUtil.findNextNonWhitespaceCharWildcard(buffer, i, maxi, elementBreakdownLocator);
        
        if (wsEnd != -1) {
            // This is a close tag, so everything should be whitespace
            // until the end of the close tag
            return false;
        }
        
        final int wsOffset = current;
        final int wsLen = maxi - current;
        handler.elementWhitespace(buffer, wsOffset, wsLen, currentArtifactLine, currentArtifactCol);

        
        handler.closeElementEnd(
                buffer, (outerOffset + outerLen - 1), 1, 
                elementBreakdownLocator.line, elementBreakdownLocator.col);
        
        return true;
        
    }
    
    

    
    
    
    static boolean isOpenElementStart(final char[] buffer, final int offset, final int maxi) {
        return ((maxi - offset > 1) && 
                    buffer[offset] == '<' &&
                    buffer[offset + 1] != '/' && 
                    buffer[offset + 1] != '!' && 
                    buffer[offset + 1] != '?' &&
                    !Character.isWhitespace(buffer[offset + 1]));
    }

    
    static boolean isCloseElementStart(final char[] buffer, final int offset, final int maxi) {
        return ((maxi - offset > 2) && 
                    buffer[offset] == '<' &&
                    buffer[offset + 1] == '/' &&
                    buffer[offset + 2] != '!' && 
                    buffer[offset + 2] != '?' &&
                    !Character.isWhitespace(buffer[offset + 2]));
    }

    
    
    
    
}
