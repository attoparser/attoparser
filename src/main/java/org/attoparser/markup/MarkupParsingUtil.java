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
public final class MarkupParsingUtil {

    
    private static final char[] DOCTYPE_TYPE_PUBLIC_UPPER = "PUBLIC".toCharArray();
    private static final char[] DOCTYPE_TYPE_PUBLIC_LOWER = "public".toCharArray();
    private static final char[] DOCTYPE_TYPE_SYSTEM_UPPER = "SYSTEM".toCharArray();
    private static final char[] DOCTYPE_TYPE_SYSTEM_LOWER = "system".toCharArray();
    

    
    private MarkupParsingUtil() {
        super();
    }

    
    

    
    
    
    
    public static <T extends IElementHandling 
                           & IDocTypeHandling
                           & ICommentHandling
                           & ICdataHandling> 
        void parseStructure(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final T handler)
            throws AttoParseException {
        
        if (!tryParseStructure(buffer, offset, len, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as markup structure: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        
    }

    
    
    public static void parseCdata(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final ICdataHandling handler)
            throws AttoParseException {
        
        if (!tryParseCdata(buffer, offset, len, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as markup CDATA: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        
    }

    
    
    public static void parseDocType(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IDocTypeHandling handler)
            throws AttoParseException {
        
        if (!tryParseDocType(buffer, offset, len, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as markup DOCTYPE clause: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        
    }
    
    
    
    public static void parseComment(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final ICommentHandling handler)
            throws AttoParseException {
        
        if (!tryParseComment(buffer, offset, len, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as markup comment: \"" + new String(buffer, offset, len) + "\"", line, col);
        }
        
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
    
    
    
    
    public static <T extends IElementHandling 
                           & IDocTypeHandling 
                           & ICommentHandling 
                           & ICdataHandling> 
        boolean tryParseStructure(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final T handler)
            throws AttoParseException {
        
        if (!MarkupParsingUtil.tryParseElement(buffer, offset, len, line, col, handler)) {
            if (!MarkupParsingUtil.tryParseComment(buffer, offset, len, line, col, handler)) {
                 if (!MarkupParsingUtil.tryParseCdata(buffer, offset, len, line, col, handler )) {
                     return MarkupParsingUtil.tryParseDocType(buffer, offset, len, line, col, handler);
                 }
            }
        }
        return true;
        
    }
    
    
    
    public static boolean tryParseComment(
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
            handler.comment(buffer, offset + 4, len - 7, offset, len, line, col);
            return true;
        }
        
        return false;
        
    }

    
    
    public static boolean tryParseCdata(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final ICdataHandling handler)
            throws AttoParseException {

        if (len >= 12 && 
                isCdataStart(buffer, offset, (offset + len)) &&
                buffer[offset + len - 3] == ']' &&
                buffer[offset + len - 2] == ']' &&
                buffer[offset + len - 1] == '>') {
            handler.cdata(buffer, offset + 9, len - 12, offset, len, line, col);
            return true;
        }
        
        return false;
        
    }

    
    
    public static boolean tryParseDocType(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col, 
            final IDocTypeHandling handler)
            throws AttoParseException {

        if (len >= 10 && 
                isDocTypeStart(buffer, offset, (offset + len)) &&
                buffer[offset + len - 1] == '>') {
            handler.docType(buffer, offset + 2, len - 3, offset, len, line, col);
            return true;
        }
        
        return false;
        
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
    
    
    public static void parseDocTypeBreakDown(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IDocTypeBreakDownHandling handler)
            throws AttoParseException {
        
        if (!tryParseDocTypeBreakDown(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col, handler)) {
            throw new AttoParseException(
                    "Could not parse as a broken down DOCTYPE clause: \"" + new String(buffer, outerOffset, outerLen) + "\"", line, col);
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
    
    
    public static boolean tryParseDocTypeBreakDown(
            final char[] buffer,
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col, 
            final IDocTypeBreakDownHandling handler)
            throws AttoParseException {
        
        final int maxi = contentOffset + contentLen;
        
        final MarkupParsingLocator docTypeBreakdownLocator = new MarkupParsingLocator(line, col + 2);
        
        int i = contentOffset;
        
        /*
         * Extract the keyword 
         */
        
        final int keywordEnd = 
            findNextWhitespaceCharWildcard(buffer, i, maxi, false, docTypeBreakdownLocator);
        
        if (keywordEnd == -1) {
            // The buffer only contains the DOCTYPE keyword. Weird but true.
            
            handler.docType(
                    buffer, 
                    i, maxi - i,                                                // keyword 
                    line, col + 2,                                              // keyword
                    0, 0,                                                       // element name 
                    docTypeBreakdownLocator.line, docTypeBreakdownLocator.col,  // element name
                    0, 0,                                                       // type
                    docTypeBreakdownLocator.line, docTypeBreakdownLocator.col,  // type
                    0, 0,                                                       // publicId
                    docTypeBreakdownLocator.line, docTypeBreakdownLocator.col,  // publicId
                    0, 0,                                                       // systemId
                    docTypeBreakdownLocator.line, docTypeBreakdownLocator.col,  // systemId
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer
            
            return true;
            
        }
        
        
        
        final int keywordOffset = i;
        final int keywordLen = keywordEnd - keywordOffset;
        final int keywordLine = line;
        final int keywordCol = col + 2;
        
        i = keywordEnd;

        
        /*
         * Fast-forward to the element name
         */
        
        int currentDocTypeLine = docTypeBreakdownLocator.line;
        int currentDocTypeCol = docTypeBreakdownLocator.col;
        
        final int elementNameStart = 
                findNextNonWhitespaceCharWildcard(buffer, i, maxi, docTypeBreakdownLocator);

        if (elementNameStart == -1) {
            // There is no element name. Only whitespace until the end of the DOCTYPE structure
            
            handler.docType(
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
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer
            
            return true;
            
        }

        
        i = elementNameStart;
        
        
        
        /*
         * Search the element name end
         */
        
        currentDocTypeLine = docTypeBreakdownLocator.line;
        currentDocTypeCol = docTypeBreakdownLocator.col;
        
        final int elementNameEnd = 
                findNextWhitespaceCharWildcard(buffer, i, maxi, false, docTypeBreakdownLocator);

        if (elementNameEnd == -1) {
            // The element name is the last thing to appear in the structure
            
            handler.docType(
                    buffer, 
                    keywordOffset, keywordLen,                                  // keyword 
                    keywordLine, keywordCol,                                    // keyword
                    i, maxi - i,                                                // element name 
                    currentDocTypeLine, currentDocTypeCol,                      // element name
                    0, 0,                                                       // type
                    docTypeBreakdownLocator.line, docTypeBreakdownLocator.col,  // type
                    0, 0,                                                       // publicId
                    docTypeBreakdownLocator.line, docTypeBreakdownLocator.col,  // publicId
                    0, 0,                                                       // systemId
                    docTypeBreakdownLocator.line, docTypeBreakdownLocator.col,  // systemId
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer
            
            return true;
            
        }
        
        
        
        final int elementNameOffset = elementNameStart;
        final int elementNameLen = elementNameEnd - elementNameOffset;
        final int elementNameLine = currentDocTypeLine;
        final int elementNameCol = currentDocTypeCol;
        
        i = elementNameEnd;

        
        /*
         * Fast-forward to the type
         */
        
        currentDocTypeLine = docTypeBreakdownLocator.line;
        currentDocTypeCol = docTypeBreakdownLocator.col;
        
        final int typeStart = 
                findNextNonWhitespaceCharWildcard(buffer, i, maxi, docTypeBreakdownLocator);

        if (typeStart == -1) {
            // There is no type. Only whitespace until the end of the DOCTYPE structure
            
            handler.docType(
                    buffer, 
                    keywordOffset, keywordLen,                                  // keyword 
                    keywordLine, keywordCol,                                    // keyword
                    elementNameOffset, elementNameLen,                          // element name 
                    elementNameLine, elementNameCol,                            // element name
                    0, 0,                                                       // type
                    currentDocTypeLine, currentDocTypeCol,                      // type
                    0, 0,                                                       // publicId
                    currentDocTypeLine, currentDocTypeCol,                      // publicId
                    0, 0,                                                       // systemId
                    currentDocTypeLine, currentDocTypeCol,                      // systemId
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer
            
            return true;
            
        }

        
        i = typeStart;
        
        
        
        /*
         * Search the type end
         */
        
        currentDocTypeLine = docTypeBreakdownLocator.line;
        currentDocTypeCol = docTypeBreakdownLocator.col;
        
        final int typeEnd = 
                findNextWhitespaceCharWildcard(buffer, i, maxi, true, docTypeBreakdownLocator);

        if (typeEnd == -1) {
            // The type is the last thing to appear in the structure
            
            // When there is a type, there must be a at least a spec1,
            // so this is an error
            
            return false;
            
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
            return false;
        }

        final boolean isTypePublic = 
                (buffer[typeOffset] == DOCTYPE_TYPE_PUBLIC_UPPER[0] || buffer[typeOffset] == DOCTYPE_TYPE_PUBLIC_LOWER[0]);  
        
        
        /*
         * Fast-forward to the spec1 (publicId or systemId, depending on type)
         */
        
        currentDocTypeLine = docTypeBreakdownLocator.line;
        currentDocTypeCol = docTypeBreakdownLocator.col;
        
        final int spec1Start = 
                findNextNonWhitespaceCharWildcard(buffer, i, maxi, docTypeBreakdownLocator);

        if (spec1Start == -1) {
            // When there is a type, there must be a at least a spec1,
            // so this is an error
            return false;
        }

        
        i = spec1Start;
        
        
        
        /*
         * Search the spec1 end
         */
        
        currentDocTypeLine = docTypeBreakdownLocator.line;
        currentDocTypeCol = docTypeBreakdownLocator.col;
        
        final int spec1End = 
                findNextWhitespaceCharWildcard(buffer, i, maxi, true, docTypeBreakdownLocator);

        if (spec1End == -1) {
            // The spec1 is the last thing to appear in the structure
            
            if (isTypePublic) {
                // If type is PUBLIC, we should have two specs instead of one
                return false;
            }
            
            if (!isValidDocTypeSpec(buffer, i, maxi - i)) {
                // The spec is not well-formed (surrounded by "'s)
                return false;
            }
            
            handler.docType(
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
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer
            
            return true;
            
        }

        
        
        final int spec1Offset = spec1Start;
        final int spec1Len = spec1End - spec1Offset;
        final int spec1Line = currentDocTypeLine;
        final int spec1Col = currentDocTypeCol;
        
        i = spec1End;

        
        if (!isValidDocTypeSpec(buffer, spec1Offset, spec1Len)) {
            // The spec is not well-formed (surrounded by "'s)
            return false;
        }
        
        
        /*
         * Fast-forward to the spec2 (systemId, only if type is PUBLIC)
         */
        
        currentDocTypeLine = docTypeBreakdownLocator.line;
        currentDocTypeCol = docTypeBreakdownLocator.col;
        
        final int spec2Start = 
                findNextNonWhitespaceCharWildcard(buffer, i, maxi, docTypeBreakdownLocator);

        if (spec2Start == -1) {
            // This could be a SYSTEM doctype with additional whitespace
            // or a PUBLIC one in error
            
            if (isTypePublic) {
                // If type is SYSTEM, we should have only one spec!
                return false;
            }
            
            handler.docType(
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
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer
            
            return true;
            
        }

        
        i = spec2Start;
        
        
        
        /*
         * Search the spec2 end
         */
        
        currentDocTypeLine = docTypeBreakdownLocator.line;
        currentDocTypeCol = docTypeBreakdownLocator.col;
        
        final int spec2End = 
                findNextWhitespaceCharWildcard(buffer, i, maxi, true, docTypeBreakdownLocator);

        if (spec2End == -1) {
            // The spec2 is the last thing to appear in the structure (no ending whitespaces)

            if (!isValidDocTypeSpec(buffer, i, maxi - i)) {
                // The spec is not well-formed (surrounded by "'s)
                return false;
            }
            
            handler.docType(
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
                    outerOffset, outerLen,                                      // outer 
                    line, col);                                                 // outer
            
            return true;
            
        }


        // There is some ending whitespace, which we will ignore
        

        if (!isValidDocTypeSpec(buffer, spec2Start, spec2End - spec2Start)) {
            // The spec is not well-formed (surrounded by "'s)
            return false;
        }
        
        handler.docType(
                buffer, 
                keywordOffset, keywordLen,                                  // keyword 
                keywordLine, keywordCol,                                    // keyword
                elementNameOffset, elementNameLen,                          // element name 
                elementNameLine, elementNameCol,                            // element name
                typeOffset, typeLen,                                        // type
                typeLine, typeCol,                                          // type
                spec1Offset + 1, spec1Len - 2,                              // publicId 
                spec1Line, spec1Col,                                        // publicId
                spec2Start + 1, spec2End - (spec2Start + 2),                // systemId
                currentDocTypeLine, currentDocTypeCol,                      // systemId
                outerOffset, outerLen,                                      // outer 
                line, col);                                                 // outer
        
        return true;
        
        
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
            findNextWhitespaceCharWildcard(buffer, contentOffset, maxi, true, elementBreakdownLocator);
        
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
                    findNextNonWhitespaceCharWildcard(buffer, i, maxi, elementBreakdownLocator);
            
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
                    findNextOperatorCharWildcard(buffer, i, maxi, elementBreakdownLocator);
            
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
                    findNextNonOperatorCharWildcard(buffer, i, maxi, elementBreakdownLocator);
            
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
                        findNextAnyCharAvoidQuotesWildcard(buffer, i, maxi, elementBreakdownLocator) :
                        findNextWhitespaceCharWildcard(buffer, i, maxi, false, elementBreakdownLocator));
            
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
            findNextWhitespaceCharWildcard(buffer, contentOffset, maxi, true, elementBreakdownLocator);
        
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
            findNextNonWhitespaceCharWildcard(buffer, i, maxi, elementBreakdownLocator);
        
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

    
    static boolean isCommentStart(final char[] buffer, final int offset, final int maxi) {
        return ((maxi - offset > 3) && 
                    buffer[offset] == '<' &&
                    buffer[offset + 1] == '!' &&
                    buffer[offset + 2] == '-' && 
                    buffer[offset + 3] == '-');
    }

    
    static boolean isCdataStart(final char[] buffer, final int offset, final int maxi) {
        return ((maxi - offset > 8) && 
                    buffer[offset] == '<' &&
                    buffer[offset + 1] == '!' &&
                    buffer[offset + 2] == '[' && 
                    (buffer[offset + 3] == 'C' || buffer[offset + 3] == 'c') && 
                    (buffer[offset + 4] == 'D' || buffer[offset + 4] == 'd') && 
                    (buffer[offset + 5] == 'A' || buffer[offset + 5] == 'a') && 
                    (buffer[offset + 6] == 'T' || buffer[offset + 6] == 't') && 
                    (buffer[offset + 7] == 'A' || buffer[offset + 7] == 'a') && 
                    buffer[offset + 8] == '[');
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
    
    
    
    
    static int findNext(
            final char[] text, final int offset, final int maxi, final char target, 
            final boolean avoidQuotes, final MarkupParsingLocator locator) {
        
        boolean inQuotes = false;

        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (avoidQuotes && c == '"') {
                inQuotes = !inQuotes;
            } else if (!inQuotes && c == target) {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }

    
    static int findNextWhitespaceCharWildcard(
            final char[] text, final int offset, final int maxi, 
            final boolean avoidQuotes, final MarkupParsingLocator locator) {
        
        boolean inQuotes = false;

        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            final boolean isWhitespace = (!inQuotes && Character.isWhitespace(c));
            
            if (avoidQuotes && c == '"') {
                inQuotes = !inQuotes;
            } else if (!inQuotes && isWhitespace) {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }

    
    static int findNextNonWhitespaceCharWildcard(
            final char[] text, final int offset, final int maxi, 
            final MarkupParsingLocator locator) {
        
        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (!Character.isWhitespace(c)) {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }

    
    static int findNextOperatorCharWildcard(
            final char[] text, final int offset, final int maxi,  
            final MarkupParsingLocator locator) {
        
        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (c == '=' || Character.isWhitespace(c)) {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }

    
    static int findNextNonOperatorCharWildcard(
            final char[] text, final int offset, final int maxi, 
            final MarkupParsingLocator locator) {
        
        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (c != '=' && !Character.isWhitespace(c)) {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }

    
    static int findNextAnyCharAvoidQuotesWildcard(
            final char[] text, final int offset, final int maxi,  
            final MarkupParsingLocator locator) {
        
        boolean inQuotes = false;

        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (!inQuotes) {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
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
        return (buffer[offset] == '"' && buffer[offset + len - 1] == '"');
        
    }
    
}
