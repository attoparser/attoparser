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

import org.attoparser.IAttoHandler;
import org.attoparser.exception.AttoParseException;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
final class MarkupAttoParserUtil {

    
    static final char CHAR_WHITESPACE_WILDCARD = '\u01F7';
    static final char[] EMPTY_CHAR_ARRAY = new char[0];


    
    private MarkupAttoParserUtil() {
        super();
    }
    

    
    
    
    
    public static <T extends IAttoHandler & ICommentAttoHandling
                                          & ICdataAttoHandling
                                          & IElementAttoHandling> 
            void parseStructure(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col, final T handler)
            throws AttoParseException {
        
        if (!MarkupAttoParserUtil.parseElement(buffer, offset, len, line, col, handler)) {
            if (!MarkupAttoParserUtil.parseComment(buffer, offset, len, line, col, handler)) {
                MarkupAttoParserUtil.parseCdata(buffer, offset, len, line, col, handler );
            }
        }
        
    }
    
    
    
    public static <T extends IAttoHandler & ICommentAttoHandling> boolean parseComment(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col, final T handler)
            throws AttoParseException {

        if (len >= 5 && 
                buffer[offset] == '!' &&
                buffer[offset + 1] == '-' &&
                buffer[offset + 2] == '-' &&
                buffer[offset + len - 1] == '-' &&
                buffer[offset + len - 2] == '-') {
            handler.comment(buffer, offset + 3, len - 5, line, col);
            return true;
        }
        
        return false;
        
    }
    

    
    
    public static <T extends IAttoHandler & ICdataAttoHandling> boolean parseCdata(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col, final T handler)
            throws AttoParseException {

        if (len >= 10 && 
                buffer[offset] == '!' &&
                buffer[offset + 1] == '[' &&
                buffer[offset + 2] == 'C' &&
                buffer[offset + 3] == 'D' &&
                buffer[offset + 4] == 'A' &&
                buffer[offset + 5] == 'T' &&
                buffer[offset + 6] == 'A' &&
                buffer[offset + 7] == '[' &&
                buffer[offset + len - 1] == ']' &&
                buffer[offset + len - 2] == ']') {
            handler.cdata(buffer, offset + 8, len - 10, line, col);
            return true;
        }
        
        return false;
        
    }

    
    
    
    public static <T extends IAttoHandler & IElementAttoHandling> boolean parseElement(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col, final T handler)
            throws AttoParseException {

        if (len > 1 && buffer[offset] == '/') {
            handler.closeElement(buffer, offset + 1, len - 1, line, col);
            return true;
        } else if (len > 1 && buffer[offset + len - 1] == '/') {
            handler.standaloneElement(buffer, offset, len - 1, line, col);
            return true;
        } else if (len > 0 && (buffer[offset] != '!' && buffer[offset] != '?')){
            handler.openElement(buffer, offset, len, line, col);
            return true;
        }
        
        return false;
        
    }
    
    
    
    
    
    public static <T extends IAttoHandler & IElementNameAndAttributeHandling> 
            boolean parseStandaloneElementNameAndAttributes(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col, final T handler)
            throws AttoParseException {
        return parseElementNameAndAttributes(buffer, offset, len, line, col, handler, true);
    }
    
    
    public static <T extends IAttoHandler & IElementNameAndAttributeHandling> 
            boolean parseOpenElementNameAndAttributes(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col, final T handler)
            throws AttoParseException {
        return parseElementNameAndAttributes(buffer, offset, len, line, col, handler, false);
    }
    
    
    public static <T extends IAttoHandler & IElementNameAndAttributeHandling> 
            boolean parseCloseElementNameAndAttributes(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col, final T handler)
            throws AttoParseException {

        handler.closeElementName(buffer, offset, len, line, col);
        return true;
        
    }
    
    
    private static <T extends IAttoHandler & IElementNameAndAttributeHandling> 
            boolean parseElementNameAndAttributes(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col, final T handler, 
            final boolean isStandalone)
            throws AttoParseException {

        final int maxi = offset + len;
        
        final MarkupParsingLocator attributeStatus = new MarkupParsingLocator(line, col + 1);
        
        /*
         * Extract the element name first 
         */
        
        final int elementNameEnd = 
            findNext(buffer, offset, maxi, CHAR_WHITESPACE_WILDCARD, true, attributeStatus);
        
        if (elementNameEnd == -1) {
            // The buffer only contains the element name, so just return it
            if (isStandalone) {
                handler.standaloneElementName(
                        buffer, offset, len, 
                        line, col);
            } else {
                handler.openElementName(
                        buffer, offset, len, 
                        line, col);
            }
            return true;
        }

        
        if (isStandalone) {
            handler.standaloneElementName(
                    buffer, offset, (elementNameEnd - offset),
                    line, col);
        } else {
            handler.openElementName(
                    buffer, offset, (elementNameEnd - offset),
                    line, col);
        }
        

        int i = elementNameEnd + 1;
        int current = i;
        countChar(buffer[elementNameEnd], attributeStatus);

        int currentAttributeLine = attributeStatus.getLine();
        int currentAttributeCol = attributeStatus.getCol();
        
        int attributeEnd = -1;
        
        while (i < maxi) {
            
            currentAttributeLine = attributeStatus.getLine();
            currentAttributeCol = attributeStatus.getCol();
            
            attributeEnd = 
                findNext(buffer, i, maxi, CHAR_WHITESPACE_WILDCARD, true, attributeStatus);
            
            if (attributeEnd == -1) {
                
                final int attributeOffset = current;
                final int attributeLen = maxi - current;
                parseAttribute(buffer, attributeOffset, attributeLen, handler, currentAttributeLine, currentAttributeCol);
                i = maxi;
                continue;
                
            }
            
            if (attributeEnd > current) {
                
                final int attributeOffset = current;
                final int attributeLen = attributeEnd - current;
                parseAttribute(buffer, attributeOffset, attributeLen, handler, currentAttributeLine, currentAttributeCol);
                countChar(buffer[attributeEnd], attributeStatus);
                i = attributeEnd + 1;
                current = i;
                continue;
                
            }
            
            // skip any contiguous whitespaces
            countChar(buffer[current], attributeStatus);
            i++;
            current = i;
            
        }
        
        return true;
        
    }

    
    
    private static <T extends IAttoHandler & IElementNameAndAttributeHandling> 
            void parseAttribute(
            final char[] buffer, final int offset, final int len, final T handler, 
            final int attributeLine, final int attributeCol)
            throws AttoParseException {

        final int maxi = offset + len;

        final int attributeNameEnd = findNext(buffer, offset, maxi, '=', true, null);
        
        if (attributeNameEnd == -1) {
            // This is a no-value attribute, equivalent to value = ""
            handler.elementAttribute(
                    buffer, offset, len, EMPTY_CHAR_ARRAY, 0, 0, attributeLine, attributeCol);
            return;
        }
        
        if (attributeNameEnd + 1 < maxi) {
            
            final int attributeValueOffset = attributeNameEnd + 1;
            final int attributeValueLen = maxi - attributeValueOffset;
            
            if (attributeValueLen >= 2 && 
                    buffer[attributeValueOffset] == '"' && buffer[maxi - 1] == '"') {
                // Value has surrounding quotes 
                handler.elementAttribute(
                        buffer, offset, (attributeNameEnd - offset),
                        buffer, (attributeValueOffset + 1), (attributeValueLen - 2),
                        attributeLine, attributeCol);
                return;
                
            }
            
            handler.elementAttribute(
                    buffer, offset, (attributeNameEnd - offset),
                    buffer, attributeValueOffset, attributeValueLen,
                    attributeLine, attributeCol);
            
            return;
            
        }
            
        handler.elementAttribute(
                buffer, offset, (attributeNameEnd - offset), 
                EMPTY_CHAR_ARRAY, 0, 0, 
                attributeLine, attributeCol);
        
    }
    
    
    
    
    
    static int findNext(
            final char[] text, final int offset, final int maxi, final char target,
            final boolean inTag, final MarkupParsingLocator context) {
        
        boolean inValue = false;

        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (inTag && c == '"') {
                inValue = !inValue;
            } else if (!inValue && (c == target || (target == CHAR_WHITESPACE_WILDCARD && Character.isWhitespace(c)))) {
                return i;
            }

            countChar(c, context);
            
        }
            
        return -1;
        
    }
    
    

    
    
    static void countChar(final char c, final MarkupParsingLocator context) {
        if (context != null) {
            context.countChar(c);
        }
    }
    
    
}
