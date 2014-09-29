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
 *   Class containing utility methods for parsing DOCTYPE clauses.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
final class DocTypeMarkupParsingUtil {

    
    private static final char[] DOCTYPE_TYPE_PUBLIC_UPPER = "PUBLIC".toCharArray();
    private static final char[] DOCTYPE_TYPE_PUBLIC_LOWER = "public".toCharArray();
    private static final char[] DOCTYPE_TYPE_SYSTEM_UPPER = "SYSTEM".toCharArray();
    private static final char[] DOCTYPE_TYPE_SYSTEM_LOWER = "system".toCharArray();
    

    
    private DocTypeMarkupParsingUtil() {
        super();
    }

    
    

    
    
    static IAttoHandleResult parseDocType(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final MarkupEventProcessor eventProcessor)
            throws AttoParseException {
        
        final int internalOffset = offset + 2;
        final int internalLen = len - 3;
        
        final int internalSubsetLastChar =
                findInternalSubsetEndChar(buffer, internalOffset, internalLen);
        
        if (internalSubsetLastChar == -1) {
            return doParseDetailedDocTypeWithInternalSubset(
                    buffer, internalOffset, internalLen, offset, len, line, col, 
                    0, 0, 0, 0, eventProcessor);
        }

        final int maxi = internalOffset + internalLen;
        
        final int[] locator = new int[] {line, col + 2};
        
        int i = internalOffset;
        
        /*
         * Extract the keyword 
         */
        
        final int internalSubsetStart =
            findInternalSubsetStartCharWildcard(buffer, i, maxi, locator);
        
        if (internalSubsetStart == -1) {
            // We identified this as having an internal subset, but it doesn't. Not valid.
            throw new AttoParseException(
                    "Could not parse as a well-formed DOCTYPE clause: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        
        return doParseDetailedDocTypeWithInternalSubset(
                buffer, internalOffset, (internalSubsetStart - internalOffset), offset, len, line, col, 
                internalSubsetStart + 1, (internalSubsetLastChar - internalSubsetStart) - 1, 
                locator[0], locator[1], 
                eventProcessor);
        
        
    }
    
    
    
    

    
    private static IAttoHandleResult doParseDetailedDocTypeWithInternalSubset(
            final char[] buffer,
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final int internalSubsetOffset, final int internalSubsetLen,
            final int internalSubsetLine, final int internalSubsetCol,
            final MarkupEventProcessor eventProcessor)
            throws AttoParseException {

        final int maxi = contentOffset + contentLen;
        
        final int[] locator = new int[] {line, col + 2};

        int i = contentOffset;
        
        /*
         * Extract the keyword 
         */
        
        final int keywordEnd = 
            MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, i, maxi, false, locator);
        
        if (keywordEnd == -1) {
            // The buffer only contains the DOCTYPE keyword. Weird but true.
            
            return eventProcessor.processDocType(
                    buffer, 
                    i, maxi - i,                                                // keyword 
                    line, col + 2,                                              // keyword
                    0, 0,                                                       // element name 
                    locator[0], locator[1],  // element name
                    0, 0,                                                       // type
                    locator[0], locator[1],  // type
                    0, 0,                                                       // publicId
                    locator[0], locator[1],  // publicId
                    0, 0,                                                       // systemId
                    locator[0], locator[1],  // systemId
                    internalSubsetOffset, internalSubsetLen,                    // internalSubset
                    Math.max(locator[0], internalSubsetLine), // internalSubset
                    Math.max(locator[1], internalSubsetCol),   // internalSubset
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer

        }
        
        
        
        final int keywordOffset = i;
        final int keywordLen = keywordEnd - keywordOffset;
        final int keywordLine = line;
        final int keywordCol = col + 2;
        
        i = keywordEnd;

        
        /*
         * Fast-forward to the element name
         */
        
        int currentDocTypeLine = locator[0];
        int currentDocTypeCol = locator[1];
        
        final int elementNameStart = 
                MarkupParsingUtil.findNextNonWhitespaceCharWildcard(buffer, i, maxi, locator);

        if (elementNameStart == -1) {
            // There is no element name. Only whitespace until the end of the DOCTYPE structure
            
            return eventProcessor.processDocType(
                    buffer, 
                    keywordOffset, keywordLen,                                  // keyword 
                    keywordLine, keywordCol,                                    // keyword
                    0, 0,                                                       // element name 
                    currentDocTypeLine, currentDocTypeCol,                      // element name
                    0, 0,                                                       // type
                    currentDocTypeLine, currentDocTypeCol,                      // type
                    0, 0,                                                       // publicId
                    currentDocTypeLine, currentDocTypeCol,                      // publicId
                    0, 0,                                                       // systemId
                    currentDocTypeLine, currentDocTypeCol,                      // systemId
                    internalSubsetOffset, internalSubsetLen,                    // internalSubset
                    Math.max(currentDocTypeLine, internalSubsetLine),           // internalSubset
                    Math.max(currentDocTypeCol, internalSubsetCol),             // internalSubset
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer

        }

        
        i = elementNameStart;
        
        
        
        /*
         * Search the element name end
         */
        
        currentDocTypeLine = locator[0];
        currentDocTypeCol = locator[1];
        
        final int elementNameEnd = 
                MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, i, maxi, false, locator);

        if (elementNameEnd == -1) {
            // The element name is the last thing to appear in the structure
            
            return eventProcessor.processDocType(
                    buffer, 
                    keywordOffset, keywordLen,                                  // keyword 
                    keywordLine, keywordCol,                                    // keyword
                    i, maxi - i,                                                // element name 
                    currentDocTypeLine, currentDocTypeCol,                      // element name
                    0, 0,                                                       // type
                    locator[0], locator[1],  // type
                    0, 0,                                                       // publicId
                    locator[0], locator[1],  // publicId
                    0, 0,                                                       // systemId
                    locator[0], locator[1],  // systemId
                    internalSubsetOffset, internalSubsetLen,                    // internalSubset
                    Math.max(locator[0], internalSubsetLine), // internalSubset
                    Math.max(locator[1], internalSubsetCol),   // internalSubset
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer

        }
        
        
        
        final int elementNameOffset = elementNameStart;
        final int elementNameLen = elementNameEnd - elementNameOffset;
        final int elementNameLine = currentDocTypeLine;
        final int elementNameCol = currentDocTypeCol;
        
        i = elementNameEnd;

        
        /*
         * Fast-forward to the type
         */
        
        currentDocTypeLine = locator[0];
        currentDocTypeCol = locator[1];
        
        final int typeStart = 
                MarkupParsingUtil.findNextNonWhitespaceCharWildcard(buffer, i, maxi, locator);

        if (typeStart == -1) {
            // There is no type. Only whitespace until the end of the DOCTYPE structure
            
            return eventProcessor.processDocType(
                    buffer, 
                    keywordOffset, keywordLen,                                  // keyword 
                    keywordLine, keywordCol,                                    // keyword
                    elementNameOffset, elementNameLen,                          // element name 
                    elementNameLine, elementNameCol,                            // element name
                    0, 0,                                                       // type
                    locator[0], locator[1],  // type
                    0, 0,                                                       // publicId
                    locator[0], locator[1],  // publicId
                    0, 0,                                                       // systemId
                    locator[0], locator[1],  // systemId
                    internalSubsetOffset, internalSubsetLen,                    // internalSubset
                    Math.max(locator[0], internalSubsetLine), // internalSubset
                    Math.max(locator[1], internalSubsetCol),   // internalSubset
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer

        }

        
        i = typeStart;
        
        
        
        /*
         * Search the type end
         */
        
        currentDocTypeLine = locator[0];
        currentDocTypeCol = locator[1];
        
        final int typeEnd = 
                MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, i, maxi, true, locator);

        if (typeEnd == -1) {
            // The type is the last thing to appear in the structure
            
            // When there is a type, there must be a at least a spec1,
            // so this is an error
            
            throw new AttoParseException(
                    "Could not parse as a well-formed DOCTYPE clause " +
                    "\"" + new String(buffer, outerOffset, outerLen) + "\"" +
            		": If a type is specified (PUBLIC or SYSTEM), at least a public or a system ID " +
                    "has to be specified", line, col);
            
        }
        
        
        
        final int typeOffset = typeStart;
        final int typeLen = typeEnd - typeOffset;
        final int typeLine = currentDocTypeLine;
        final int typeCol = currentDocTypeCol;
        
        i = typeEnd;

        
        
        /*
         * Test the validity of the "type" value
         */
        
        if (!isValidDocTypeType(buffer, typeOffset, typeLen)) {

            throw new AttoParseException(
                    "Could not parse as a well-formed DOCTYPE clause " +
                    "\"" + new String(buffer, outerOffset, outerLen) + "\"" +
                    ": DOCTYPE type must be either \"PUBLIC\" or \"SYSTEM\"",
                    line, col);
            
        }

        final boolean isTypePublic = 
                (buffer[typeOffset] == DOCTYPE_TYPE_PUBLIC_UPPER[0] || buffer[typeOffset] == DOCTYPE_TYPE_PUBLIC_LOWER[0]);  
        
        
        /*
         * Fast-forward to the spec1 (publicId or systemId, depending on type)
         */
        
        currentDocTypeLine = locator[0];
        currentDocTypeCol = locator[1];
        
        final int spec1Start = 
                MarkupParsingUtil.findNextNonWhitespaceCharWildcard(buffer, i, maxi, locator);

        if (spec1Start == -1) {
            // When there is a type, there must be a at least a spec1,
            // so this is an error
            
            throw new AttoParseException(
                    "Could not parse as a well-formed DOCTYPE clause " +
                    "\"" + new String(buffer, outerOffset, outerLen) + "\"" +
                    ": If a type is specified (PUBLIC or SYSTEM), at least a public or a system ID " +
                    "has to be specified", line, col);
            
        }

        
        i = spec1Start;
        
        
        
        /*
         * Search the spec1 end
         */
        
        currentDocTypeLine = locator[0];
        currentDocTypeCol = locator[1];
        
        final int spec1End = 
                MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, i, maxi, true, locator);

        if (spec1End == -1) {
            // The spec1 is the last thing to appear in the structure
            
            if (!isValidDocTypeSpec(buffer, i, maxi - i)) {
                // The spec is not well-formed (surrounded by "'s)
                
                throw new AttoParseException(
                        "Could not parse as a well-formed DOCTYPE clause " +
                        "\"" + new String(buffer, outerOffset, outerLen) + "\"" +
                        ": Public and Systen IDs must be surrounded by quotes (\")", 
                        line, col);

            }
            
            if (isTypePublic) {
                // If type is PUBLIC and we only have one spec, it is the publicId
                
                return eventProcessor.processDocType(
                        buffer, 
                        keywordOffset, keywordLen,                                  // keyword 
                        keywordLine, keywordCol,                                    // keyword
                        elementNameOffset, elementNameLen,                          // element name 
                        elementNameLine, elementNameCol,                            // element name
                        typeOffset, typeLen,                                        // type
                        typeLine, typeCol,                                          // type
                        i + 1, maxi - (i + 2),                                      // publicId
                        currentDocTypeLine, currentDocTypeCol,                      // publicId
                        0, 0,                                                       // systemId 
                        locator[0], locator[1],  // systemId
                        internalSubsetOffset, internalSubsetLen,                    // internalSubset
                        Math.max(locator[0], internalSubsetLine), // internalSubset
                        Math.max(locator[1], internalSubsetCol),   // internalSubset
                        outerOffset, outerLen,                                      // outer 
                        line, col);                                                 // outer

            }
            
            return eventProcessor.processDocType(
                    buffer, 
                    keywordOffset, keywordLen,                                  // keyword 
                    keywordLine, keywordCol,                                    // keyword
                    elementNameOffset, elementNameLen,                          // element name 
                    elementNameLine, elementNameCol,                            // element name
                    typeOffset, typeLen,                                        // type
                    typeLine, typeCol,                                          // type
                    0, 0,                                                       // publicId 
                    currentDocTypeLine, currentDocTypeCol,                      // publicId
                    i + 1, maxi - (i + 2),                                      // systemId
                    currentDocTypeLine, currentDocTypeCol,                      // systemId
                    internalSubsetOffset, internalSubsetLen,                    // internalSubset
                    Math.max(locator[0], internalSubsetLine), // internalSubset
                    Math.max(locator[1], internalSubsetCol),   // internalSubset
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer

        }

        
        
        final int spec1Offset = spec1Start;
        final int spec1Len = spec1End - spec1Offset;
        final int spec1Line = currentDocTypeLine;
        final int spec1Col = currentDocTypeCol;
        
        i = spec1End;

        
        if (!isValidDocTypeSpec(buffer, spec1Offset, spec1Len)) {
            // The spec is not well-formed (surrounded by "'s)
            
            throw new AttoParseException(
                    "Could not parse as a well-formed DOCTYPE clause " +
                    "\"" + new String(buffer, outerOffset, outerLen) + "\"" +
                    ": Public and Systen IDs must be surrounded by quotes (\")", 
                    line, col);
            
        }
        
        
        /*
         * Fast-forward to the spec2 (systemId, only if type is PUBLIC)
         */
        
        currentDocTypeLine = locator[0];
        currentDocTypeCol = locator[1];
        
        final int spec2Start = 
                MarkupParsingUtil.findNextNonWhitespaceCharWildcard(buffer, i, maxi, locator);

        if (spec2Start == -1) {
            // There is no spec2
            
            if (isTypePublic) {
                // If type is PUBLIC and we only have one spec, it is the publicId
                
                return eventProcessor.processDocType(
                        buffer, 
                        keywordOffset, keywordLen,                                  // keyword 
                        keywordLine, keywordCol,                                    // keyword
                        elementNameOffset, elementNameLen,                          // element name 
                        elementNameLine, elementNameCol,                            // element name
                        typeOffset, typeLen,                                        // type
                        typeLine, typeCol,                                          // type
                        spec1Offset + 1, spec1Len - 2,                              // publicId 
                        spec1Line, spec1Col,                                        // publicId
                        0, 0,                                                       // systemId
                        locator[0], locator[1],  // systemId
                        internalSubsetOffset, internalSubsetLen,                    // internalSubset
                        Math.max(locator[0], internalSubsetLine), // internalSubset
                        Math.max(locator[1], internalSubsetCol),   // internalSubset
                        outerOffset, outerLen,                                      // outer 
                        line, col);                                                 // outer

            }
            
            return eventProcessor.processDocType(
                    buffer, 
                    keywordOffset, keywordLen,                                  // keyword 
                    keywordLine, keywordCol,                                    // keyword
                    elementNameOffset, elementNameLen,                          // element name 
                    elementNameLine, elementNameCol,                            // element name
                    typeOffset, typeLen,                                        // type
                    typeLine, typeCol,                                          // type
                    0, 0,                                                       // publicId 
                    spec1Line, spec1Col,                                        // publicId
                    spec1Offset + 1, spec1Len - 2,                              // systemId
                    spec1Line, spec1Col,                                        // systemId
                    internalSubsetOffset, internalSubsetLen,                    // internalSubset
                    Math.max(locator[0], internalSubsetLine), // internalSubset
                    Math.max(locator[1], internalSubsetCol),   // internalSubset
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer

        }

        
        i = spec2Start;
        
        
        
        /*
         * Search the spec2 end
         */
        
        currentDocTypeLine = locator[0];
        currentDocTypeCol = locator[1];
        
        final int spec2End = 
                MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, i, maxi, true, locator);

        if (spec2End == -1) {
            // The spec2 is the last thing to appear in the structure (no ending whitespaces)

            if (!isValidDocTypeSpec(buffer, i, maxi - i)) {
                // The spec is not well-formed (surrounded by "'s). 
                
                throw new AttoParseException(
                        "Could not parse as a well-formed DOCTYPE clause " +
                        "\"" + new String(buffer, outerOffset, outerLen) + "\"" +
                        ": Public and Systen IDs must be surrounded by quotes (\")", 
                        line, col);
                
            }
            
            // There is no internal subset, and what we have is a valid spec2

            if (!isTypePublic) {
                // Type SYSTEM cannot have two specs!
                
                throw new AttoParseException(
                        "Could not parse as a well-formed DOCTYPE clause " +
                        "\"" + new String(buffer, outerOffset, outerLen) + "\"" +
                        ": type SYSTEM only allows specifying one element (a system ID)", 
                        line, col);

            }
            
            return eventProcessor.processDocType(
                    buffer, 
                    keywordOffset, keywordLen,                                  // keyword 
                    keywordLine, keywordCol,                                    // keyword
                    elementNameOffset, elementNameLen,                          // element name 
                    elementNameLine, elementNameCol,                            // element name
                    typeOffset, typeLen,                                        // type
                    typeLine, typeCol,                                          // type
                    spec1Offset + 1, spec1Len - 2,                              // publicId 
                    spec1Line, spec1Col,                                        // publicId
                    i + 1, maxi - (i + 2),                                      // systemId
                    currentDocTypeLine, currentDocTypeCol,                      // systemId
                    internalSubsetOffset, internalSubsetLen,                    // internalSubset
                    Math.max(locator[0], internalSubsetLine), // internalSubset
                    Math.max(locator[1], internalSubsetCol),   // internalSubset
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer

        }

        
        
        final int spec2Offset = spec2Start;
        final int spec2Len = spec2End - spec2Offset;
        final int spec2Line = currentDocTypeLine;
        final int spec2Col = currentDocTypeCol;
        
        i = spec2End;

        
        if (!isValidDocTypeSpec(buffer, spec2Offset, spec2Len)) {
            // The spec is not well-formed (surrounded by "'s)
            
            throw new AttoParseException(
                    "Could not parse as a well-formed DOCTYPE clause " +
                    "\"" + new String(buffer, outerOffset, outerLen) + "\"" +
                    ": Public and Systen IDs must be surrounded by quotes (\")", 
                    line, col);

        }
        

        if (!isTypePublic) {
            // Type SYSTEM cannot have two specs!
            
            throw new AttoParseException(
                    "Could not parse as a well-formed DOCTYPE clause " +
                    "\"" + new String(buffer, outerOffset, outerLen) + "\"" +
                    ": type SYSTEM only allows specifying one element (a system ID)", 
                    line, col);

        }
        
        
        /*
         * Fast-forward to the end of the DOCTYPE clause
         */
        
        currentDocTypeLine = locator[0];
        currentDocTypeCol = locator[1];
        
        final int clauseEndStart = 
                MarkupParsingUtil.findNextNonWhitespaceCharWildcard(buffer, i, maxi, locator);

        if (clauseEndStart != -1) {
            // We have found more elements inside the DOCTYPE clause after all valid ones.
            // This is not valid.
            
            throw new AttoParseException(
                    "Could not parse as a well-formed DOCTYPE clause " +
                    "\"" + new String(buffer, outerOffset, outerLen) + "\"" +
                    ": More elements found than allowed", 
                    line, col);

        }
            
        
        // If everything we can find until the end of the clause is whitespace, we are fine
        
        return eventProcessor.processDocType(
                buffer, 
                keywordOffset, keywordLen,                                  // keyword 
                keywordLine, keywordCol,                                    // keyword
                elementNameOffset, elementNameLen,                          // element name 
                elementNameLine, elementNameCol,                            // element name
                typeOffset, typeLen,                                        // type
                typeLine, typeCol,                                          // type
                spec1Offset + 1, spec1Len - 2,                              // publicId 
                spec1Line, spec1Col,                                        // publicId
                spec2Offset + 1, spec2Len - 2,                              // systemId
                spec2Line, spec2Col,                                        // systemId
                internalSubsetOffset, internalSubsetLen,                    // internalSubset
                Math.max(locator[0], internalSubsetLine), // internalSubset
                Math.max(locator[1], internalSubsetCol),   // internalSubset
                outerOffset, outerLen,                                      // outer 
                line, col);                                                 // outer
        
        
    }

    

    
    
    
    
    
    
    
    
    
    
    static boolean isDocTypeStart(final char[] buffer, final int offset, final int maxi) {
        return ((maxi - offset > 9) && 
                    buffer[offset] == '<' &&
                    buffer[offset + 1] == '!' &&
                    (buffer[offset + 2] == 'D' || buffer[offset + 2] == 'd') && 
                    (buffer[offset + 3] == 'O' || buffer[offset + 3] == 'o') && 
                    (buffer[offset + 4] == 'C' || buffer[offset + 4] == 'c') && 
                    (buffer[offset + 5] == 'T' || buffer[offset + 5] == 't') && 
                    (buffer[offset + 6] == 'Y' || buffer[offset + 6] == 'y') && 
                    (buffer[offset + 7] == 'P' || buffer[offset + 7] == 'p') && 
                    (buffer[offset + 8] == 'E' || buffer[offset + 8] == 'e') && 
                    (Character.isWhitespace(buffer[offset + 9]) || buffer[offset + 9] == '>'));
    }

    
    


    private static boolean isValidDocTypeType(final char[] buffer, final int offset, final int len) {
        
        if (len != 6) {
            return false;
        }
        
        if (buffer[offset] == DOCTYPE_TYPE_PUBLIC_UPPER[0] || buffer[offset] == DOCTYPE_TYPE_PUBLIC_LOWER[0]) {
            for (int i = 1; i < 6; i++) {
                if (buffer[offset + i] != DOCTYPE_TYPE_PUBLIC_UPPER[i] && buffer[offset + i] != DOCTYPE_TYPE_PUBLIC_LOWER[i]) {
                    return false;
                }
            }
            return true;
        } else if (buffer[offset] == DOCTYPE_TYPE_SYSTEM_UPPER[0] || buffer[offset] == DOCTYPE_TYPE_SYSTEM_LOWER[0]) {
            for (int i = 1; i < 6; i++) {
                if (buffer[offset + i] != DOCTYPE_TYPE_SYSTEM_UPPER[i] && buffer[offset + i] != DOCTYPE_TYPE_SYSTEM_LOWER[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }

    }
    


    private static boolean isValidDocTypeSpec(final char[] buffer, final int offset, final int len) {
        
        if (len < 2) {
            return false;
        }
        return (buffer[offset] == '"' && buffer[offset + len - 1] == '"') ||
               (buffer[offset] == '\'' && buffer[offset + len - 1] == '\'');
        
    }
    


    private static int findInternalSubsetEndChar(final char[] buffer, final int offset, final int len) {

        final int maxi = offset + len;
        
        for (int i = maxi - 1; i > offset; i--) {
            
            final char c = buffer[i];
            if (!Character.isWhitespace(c)) {
                if (c == ']') {
                    return i;
                }
                return -1;
            }
            
        }
        
        return -1;
        
    }


    
    private static int findInternalSubsetStartCharWildcard(
            final char[] text, final int offset, final int maxi, final int[] locator) {
        
        boolean inQuotes = false;
        boolean inApos = false;

        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (!inApos && c == '"') {
                inQuotes = !inQuotes;
            } else if (!inQuotes && c == '\'') {
                inApos = !inApos;
            } else if (!inQuotes && !inApos && c == '[') {
                return i;
            }

            LocatorUtils.countChar(locator, c);
            
        }
            
        return -1;
        
    }
    
    
    
    
    static int findNextDocTypeStructureEnd(
            final char[] text, final int offset, final int maxi, final int[] locator) {
        
        boolean inQuotes = false;
        boolean inApos = false;
        int bracketLevel = 0;

        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (!inApos && c == '"') {
                inQuotes = !inQuotes;
            } else if (!inQuotes && c == '\'') {
                inApos = !inApos;
            } else if (!inQuotes && !inApos && c == '[') {
                bracketLevel++;
            } else  if (!inQuotes && !inApos && c == ']') {
                bracketLevel--;
            } else if (!inQuotes && !inApos && bracketLevel == 0 && c == '>') {
                return i;
            }

            LocatorUtils.countChar(locator, c);
            
        }
        
        if (bracketLevel != 0) {
            // We've reached the end of buffer, but not cleanly!
            return -2;
        }

        return -1;
        
    }
    
    
    
}
