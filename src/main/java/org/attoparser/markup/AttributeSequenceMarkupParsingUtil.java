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
public final class AttributeSequenceMarkupParsingUtil {


    

    
    private AttributeSequenceMarkupParsingUtil() {
        super();
    }

    
    
    
    
    
    
    public static void parseAttributeSequence(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IAttributeSequenceHandling handler)
            throws AttoParseException {
        
        if (!tryParseAttributeSequence(buffer, offset, len, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as attribute sequence: \"" + new String(buffer, offset, len) + "\"", line, col);
        }

    }

    
    
    static void parseAttributeSequence(
            final char[] buffer, 
            final int offset, final int len, 
            final MarkupParsingLocator locator, 
            final IAttributeSequenceHandling handler)
            throws AttoParseException {
        
        final int line = locator.line;
        final int col = locator.col;
        if (!tryParseAttributeSequence(buffer, offset, len, locator, handler)) {
            throw new AttoParseException(
                    "Could not parse as attribute sequence: \"" + new String(buffer, offset, len) + "\"", line, col);
        }

    }
    
    

    

    
    public static boolean tryParseAttributeSequence(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col, 
            final IAttributeSequenceHandling handler)
            throws AttoParseException {
        
        return tryParseAttributeSequence(buffer, offset, len, new MarkupParsingLocator(line, col), handler);
                
    }
    

    
    static boolean tryParseAttributeSequence(
            final char[] buffer,
            final int offset, final int len, 
            final MarkupParsingLocator locator, 
            final IAttributeSequenceHandling handler)
            throws AttoParseException {

        
        final int maxi = offset + len;
        
               
        int i = offset;
        int current = i;

        int currentArtifactLine = locator.line;
        int currentArtifactCol = locator.col;
        
        while (i < maxi) {

            /*
             * STEP ONE: Look for whitespaces between attributes
             */

            currentArtifactLine = locator.line;
            currentArtifactCol = locator.col;
            
            final int wsEnd = 
                    MarkupParsingUtil.findNextNonWhitespaceCharWildcard(buffer, i, maxi, locator);
            
            if (wsEnd == -1) {
                // Everything is whitespace until the end of the tag
                
                final int wsOffset = current;
                final int wsLen = maxi - current;
                handler.attributeSeparator(buffer, wsOffset, wsLen, currentArtifactLine, currentArtifactCol);
                i = maxi;
                continue;
                
            }
            
            if (wsEnd > current) {
                // We avoid empty whitespace fragments
                final int wsOffset = current;
                final int wsLen = wsEnd - current;
                handler.attributeSeparator(buffer, wsOffset, wsLen, currentArtifactLine, currentArtifactCol);
                i = wsEnd;
                current = i;
            }
            

            
            /*
             * STEP TWO: Detect the attribute name
             */

            
            currentArtifactLine = locator.line;
            currentArtifactCol = locator.col;
            
            final int attributeNameEnd = 
                    MarkupParsingUtil.findNextOperatorCharWildcard(buffer, i, maxi, locator);
            
            if (attributeNameEnd == -1) {
                // This is a no-value and no-equals-sign attribute, equivalent to value = ""
                
                final int attributeNameOffset = current;
                final int attributeNameLen = maxi - current;
                handler.attribute(
                        buffer,                                                               // name 
                        attributeNameOffset, attributeNameLen,                                // name
                        currentArtifactLine, currentArtifactCol,                              // name
                        0, 0,                                                                 // operator
                        locator.line, locator.col,                                            // operator
                        0, 0, 0, 0,                                                           // value
                        locator.line, locator.col);                                           // value
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

            
            currentArtifactLine = locator.line;
            currentArtifactCol = locator.col;

            final int operatorEnd = 
                    MarkupParsingUtil.findNextNonOperatorCharWildcard(buffer, i, maxi, locator);
            
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
                    
                    handler.attribute(
                            buffer,                                                                // name 
                            attributeNameOffset, attributeNameLen,                                 // name 
                            attributeNameLine, attributeNameCol,                                   // name
                            operatorOffset, operatorLen,                                           // operator 
                            currentArtifactLine, currentArtifactCol,                               // operator
                            0, 0, 0, 0,                                                            // value
                            locator.line, locator.col);                                            // value
                    
                } else {
                    // There is no "=", so we will first output the attribute with no
                    // operator and then a whitespace
                    
                    handler.attribute(
                            buffer,                                                                // name 
                            attributeNameOffset, attributeNameLen,                                 // name 
                            attributeNameLine, attributeNameCol,                                   // name
                            0, 0,                                                                  // operator 
                            currentArtifactLine, currentArtifactCol,                               // operator
                            0, 0, 0, 0,                                                            // value
                            currentArtifactLine, currentArtifactCol);                              // value
                    
                    handler.attributeSeparator(
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
                
                handler.attribute(
                        buffer,                                                                // name 
                        attributeNameOffset, attributeNameLen,                                 // name 
                        attributeNameLine, attributeNameCol,                                   // name
                        0, 0,                                                                  // operator 
                        currentArtifactLine, currentArtifactCol,                               // operator
                        0, 0, 0, 0,                                                            // value
                        currentArtifactLine, currentArtifactCol);                              // value
                
                handler.attributeSeparator(
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

            
            currentArtifactLine = locator.line;
            currentArtifactCol = locator.col;

            final boolean attributeEndsWithQuotes = (i < maxi && buffer[current] == '"');
            final int valueEnd =
                (attributeEndsWithQuotes?
                        MarkupParsingUtil.findNextAnyCharAvoidQuotesWildcard(buffer, i, maxi, locator) :
                        MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, i, maxi, false, locator));
            
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
                        
                handler.attribute(
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
                    
            handler.attribute(
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
        
        return true;
        
    }

    
    
    
    
}
