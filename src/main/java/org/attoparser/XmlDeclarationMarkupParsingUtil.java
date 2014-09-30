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
 *   Class containing utility methods for parsing XML Declarations.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
final class XmlDeclarationMarkupParsingUtil {


    
    

    
    private XmlDeclarationMarkupParsingUtil() {
        super();
    }

    
    

    


    
    static IAttoHandleResult parseXmlDeclaration(
            final char[] buffer,
            final int internalOffset, final int internalLen,
            final int outerOffset, final int outerLen,
            final int line, final int col,
            final MarkupEventProcessor eventProcessor)
            throws AttoParseException {

        final int maxi = internalOffset + internalLen;
        
        final int[] locator = new int[] {line, col + 2};
        
        final int keywordLine = locator[0];
        final int keywordCol = locator[1];
        
        int i = internalOffset;
        
        /*
         * Extract the target 
         */
        
        final int keywordEnd = 
            MarkupParsingUtil.findNextWhitespaceCharWildcard(buffer, i, maxi, false, locator);
        
        if (keywordEnd == -1) {
            // There is no content, only keyword. But an XML declaration should
            // contain at least the XML version, so this XML declaration doesn't
            // have a correct format.
            
            throw new AttoParseException(
                    "XML Declaration must at least contain a \"version\" attribute: " +
                    "\"" + new String(buffer, outerOffset, outerLen) + "\"", line, col);
            
        }
        
        
        final int keywordOffset = i;
        final int keywordLen = keywordEnd - keywordOffset;
        
        i = keywordEnd;

        
        /*
         * Fast-forward to the content
         */
        
        final int contentOffset = 
                MarkupParsingUtil.findNextNonWhitespaceCharWildcard(buffer, i, maxi, locator);

        if (contentOffset == -1) {
            // There is no content. Only whitespace until the end of the structure.
            // But an XML declaration should contain at least the XML version, so 
            // this XML declaration doesn't have a correct format.
            
            throw new AttoParseException(
                    "XML Declaration must at least contain a \"version\" attribute: " +
                    "\"" + new String(buffer, outerOffset, outerLen) + "\"", line, col);
            
        }
        
        final int contentLen = maxi - contentOffset;
        
        final XmlDeclarationAttributeProcessor attHandling =
                new XmlDeclarationAttributeProcessor(outerOffset, outerLen, line, col);


        final IAttoHandleResult attrHandleResult =
                AttributeSequenceMarkupParsingUtil.
                        parseAttributeSequence(buffer, contentOffset, contentLen, locator, attHandling);

        attHandling.finalChecks(locator, buffer);


        final IAttoHandleResult finalHandlerResult =
                eventProcessor.processXmlDeclaration(
                        buffer,
                        keywordOffset, keywordLen,                                // keyword
                        keywordLine, keywordCol,                                  // keyword
                        attHandling.versionOffset, attHandling.versionLen,        // version
                        attHandling.versionLine, attHandling.versionCol,          // version
                        attHandling.encodingOffset, attHandling.encodingLen,      // encoding
                        attHandling.encodingLine, attHandling.encodingCol,        // encoding
                        attHandling.standaloneOffset, attHandling.standaloneLen,  // standalone
                        attHandling.standaloneLine, attHandling.standaloneCol,    // standalone
                        outerOffset, outerLen,                                    // outer
                        line, col);                                               // outer

        return AttoHandleResultUtil.combinePriorityLast(attrHandleResult, finalHandlerResult);
        
    }
    
    
    


    
    static boolean isXmlDeclarationStart(final char[] buffer, final int offset, final int maxi) {
        // No upper case allowed in XML Declaration. XML is case-sensitive!!
        return ((maxi - offset > 5) && 
                    buffer[offset] == '<' &&
                    buffer[offset + 1] == '?' &&
                    buffer[offset + 2] == 'x' && 
                    buffer[offset + 3] == 'm' && 
                    buffer[offset + 4] == 'l' && 
                    (Character.isWhitespace(buffer[offset + 5]) || 
                            ((maxi - offset > 6) && buffer[offset + 5] == '?' && buffer[offset + 6] == '>')));
    }

    
    
    

    
    
    private static class XmlDeclarationAttributeProcessor implements IMarkupEventAttributeProcessor {

        private final int outerOffset;
        private final int outerLen;
        private final int outerLine;
        private final int outerCol;
        
        // attribute names will only be in lowercase, as XML is case sensitive and upper
        // case would be wrong.
        
        static final char[] VERSION = "version".toCharArray();
        boolean versionPresent = false;
        int versionOffset = 0;
        int versionLen = 0;
        int versionLine = -1;
        int versionCol = -1;
        
        static final char[] ENCODING = "encoding".toCharArray();
        boolean encodingPresent = false;
        int encodingOffset = 0;
        int encodingLen = 0;
        int encodingLine = -1;
        int encodingCol = -1;

        static final char[] STANDALONE = "standalone".toCharArray();
        boolean standalonePresent = false;
        int standaloneOffset = 0;
        int standaloneLen = 0;
        int standaloneLine = -1;
        int standaloneCol = -1;

        
        
        XmlDeclarationAttributeProcessor(final int outerOffset, final int outerLen,
                                         final int outerLine, final int outerCol) {
            super();
            this.outerOffset = outerOffset;
            this.outerLen = outerLen;
            this.outerLine = outerLine;
            this.outerCol = outerCol;
        }

        public IAttoHandleResult processAttribute(
                final char[] buffer, 
                final int nameOffset, final int nameLen,
                final int nameLine, final int nameCol, 
                final int operatorOffset, final int operatorLen,
                final int operatorLine, final int operatorCol, 
                final int valueContentOffset, final int valueContentLen, 
                final int valueOuterOffset, final int valueOuterLen,
                final int valueLine, final int valueCol)
                throws AttoParseException {

            if (charArrayEquals(buffer, nameOffset, nameLen, VERSION, 0, VERSION.length)) {
                if (this.versionPresent) {
                    throw new AttoParseException(
                            "XML Declaration can declare only one \"version\" attribute: " +
                            "\"" + new String(buffer, this.outerOffset, this.outerLen) + "\"", 
                            this.outerLine, this.outerCol);
                }
                if (this.encodingPresent || this.standalonePresent) {
                    throw new AttoParseException(
                            "XML Declaration must declare \"version\" as its first attribute: " +
                            "\"" + new String(buffer, this.outerOffset, this.outerLen) + "\"", 
                            this.outerLine, this.outerCol);
                }
                this.versionOffset = valueContentOffset;
                this.versionLen = valueContentLen;
                this.versionLine = valueLine;
                this.versionCol = valueCol;
                this.versionPresent = true;
                return null;
            }
            
            if (charArrayEquals(buffer, nameOffset, nameLen, ENCODING, 0, ENCODING.length)) {
                if (this.encodingPresent) {
                    throw new AttoParseException(
                            "XML Declaration can declare only one \"encoding\" attribute: " +
                            "\"" + new String(buffer, this.outerOffset, this.outerLen) + "\"", 
                            this.outerLine, this.outerCol);
                }
                if (!this.versionPresent) {
                    throw new AttoParseException(
                            "XML Declaration must declare \"encoding\" after \"version\": " +
                            "\"" + new String(buffer, this.outerOffset, this.outerLen) + "\"", 
                            this.outerLine, this.outerCol);
                }
                if (this.standalonePresent) {
                    throw new AttoParseException(
                            "XML Declaration must declare \"encoding\" before \"standalone\": " +
                            "\"" + new String(buffer, this.outerOffset, this.outerLen) + "\"", 
                            this.outerLine, this.outerCol);
                }
                this.encodingOffset = valueContentOffset;
                this.encodingLen = valueContentLen;
                this.encodingLine = valueLine;
                this.encodingCol = valueCol;
                this.encodingPresent = true;
                return null;
            }
            
            if (charArrayEquals(buffer, nameOffset, nameLen, STANDALONE, 0, STANDALONE.length)) {
                if (this.standalonePresent) {
                    throw new AttoParseException(
                            "XML Declaration can declare only one \"standalone\" attribute: " +
                            "\"" + new String(buffer, this.outerOffset, this.outerLen) + "\"", 
                            this.outerLine, this.outerCol);
                }
                this.standaloneOffset = valueContentOffset;
                this.standaloneLen = valueContentLen;
                this.standaloneLine = valueLine;
                this.standaloneCol = valueCol;
                this.standalonePresent = true;
                return null;
            }
            
            throw new AttoParseException(
                    "XML Declaration does not allow attribute with name \"" + (new String(buffer, nameOffset, nameLen)) + "\". " +
            		"Only \"version\", \"encoding\" and \"standalone\" are allowed (in that order): " +
                    "\"" + new String(buffer, this.outerOffset, this.outerLen) + "\"", 
                    this.outerLine, this.outerCol);
            
        }
        

        public IAttoHandleResult processInnerWhiteSpace(
                final char[] buffer, 
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            // We will ignore separators
            return null;
        }



        public void finalChecks(final int[] locator, final char[] buffer) throws AttoParseException {
            if (!this.versionPresent) {
                throw new AttoParseException(
                        "Attribute \"version\" is required in XML Declaration: " +
                        "\"" + new String(buffer, this.outerOffset, this.outerLen) + "\"", 
                        this.outerLine, this.outerLine);
            }
            if (!this.standalonePresent) {
                this.standaloneLine = locator[0];
                this.standaloneCol = locator[1];
            }
            if (!this.encodingPresent) {
                if (!this.standalonePresent) {
                    this.encodingLine = locator[0];
                    this.encodingCol = locator[1];
                } else {
                    this.encodingLine = this.standaloneLine;
                    this.encodingCol = this.standaloneCol;
                }
            }
        }
        
        
        
        
        private static boolean charArrayEquals(
                final char[] arr1, final int arr1Offset, final int arr1Len,
                final char[] arr2, final int arr2Offset, final int arr2Len) {
            if (arr1Len != arr2Len) {
                return false;
            }
            for (int i = 0; i < arr1Len; i++) {
                if (arr1[arr1Offset + i] != arr2[arr2Offset + i]) {
                    return false;
                }
            }
            return true;
        }


    }



    
    
}
