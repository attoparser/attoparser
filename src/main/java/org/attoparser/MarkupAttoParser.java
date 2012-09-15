package org.attoparser;

import org.attoparser.config.IAttoConfig;
import org.attoparser.content.IAttoContentHandler;
import org.attoparser.exception.AttoParseException;

public final class MarkupAttoParser extends AbstractAttoParser {

    
    private static final char CHAR_WHITESPACE_WILDCARD = '\u01F7';
    private static final char[] EMPTY_CHAR_ARRAY = new char[0];
    
    
    
    public MarkupAttoParser() {
        super();
    }
    

    
    
    public final void parse(
            final char[] document, final int offset, final int len, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException {
        parseDocument(document, offset, len, handler, config);
    }

    
    
    
    
    
    private static final void parseDocument(
            final char[] document, final int offset, final int len, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException {

        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        if (offset < 0 || len < 0) {
            throw new IllegalArgumentException(
                    "Neither document offset (" + offset + ") nor document length (" + 
                    len + ") can be less than zero");
        }

        handler.startDocument();
        
        if (len == 0) {
            handler.endDocument();
            return;
        }
        
        final Locator locator = new Locator();
        int currentLine = locator.getLine();
        int currentPos = locator.getPos();
        
        final int maxi = offset + len;
        int i = offset;
        int current = i;
        boolean inTag = false;
        
        while (i < maxi) {
            
            currentLine = locator.getLine();
            currentPos = locator.getPos();
            
            if (!inTag) {
                
                final int tagStart = findNext(document, i, maxi, '<', locator);
                
                if (tagStart == -1) {
                    handler.text(
                            document, current, (maxi - current), 
                            currentLine, currentPos);
                    i = maxi;
                    continue;
                }
            
                if (tagStart > current) {
                    // We avoid empty-string text events
                    handler.text(
                            document, current, (tagStart - current), 
                            currentLine, currentPos);
                }
                
                current = tagStart;
                i = current;
                inTag = true;
                
            } else {
                
                int tagEnd = findNext(document, i, maxi, '>', locator);
                
                if (tagEnd == -1) {
                    
                    if (!config.returnUnfinishedTagsAsText()) {
                        throw new AttoParseException(
                                "Unfinished tag: " + (new String(document, current, (maxi-current))), 
                                currentLine, currentPos);
                    }
                    
                    handler.text(
                            document, current, (maxi - current), 
                            currentLine, currentPos);
                    i = maxi;
                    continue;
                }
                
                if (document[current + 1] == '/') {
                    // This is a closing tag
                    
                    handler.endElement(
                            document, (current + 2), (tagEnd - (current + 2)), 
                            currentLine, currentPos);
                    
                } else if ((tagEnd - current >= 7) && 
                           document[current + 1] == '!' && 
                           document[current + 2] == '-' && 
                           document[current + 3] == '-') {
                    //This is a Comment!
                    
                    while (document[tagEnd - 1] != '-' || document[tagEnd - 2] != '-') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        countChar(document[tagEnd], locator);
                        tagEnd = findNext(document, tagEnd + 1, maxi, '>', locator);
                        
                        if (tagEnd == -1) {
                            handler.text(document, current, (maxi - current), currentLine, currentPos);
                            i = maxi;
                            continue;
                        }
                        
                    }
                    
                    final int commentContentOffset = current + 4;
                    final int commentContentLen = tagEnd - (current + 6);

                    handler.comment(document, commentContentOffset, commentContentLen, currentLine, currentPos);
                    
                } else if ((tagEnd - current >= 12) &&
                           document[current + 1] == '!' && 
                           document[current + 2] == '[' && 
                           document[current + 3] == 'C' &&
                           document[current + 4] == 'D' &&
                           document[current + 5] == 'A' &&
                           document[current + 6] == 'T' &&
                           document[current + 7] == 'A' &&
                           document[current + 8] == '[') {
                    //This is a CDATA section!
                    
                    while (document[tagEnd - 1] != ']' || document[tagEnd - 2] != ']') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        countChar(document[tagEnd], locator);
                        tagEnd = findNext(document, tagEnd + 1, maxi, '>', locator);
                        
                        if (tagEnd == -1) {
                            handler.text(document, current, (maxi - current), currentLine, currentPos);
                            i = maxi;
                            continue;
                        }
                        
                    }
                    
                    final int cdataContentOffset = current + 9;
                    final int cdataContentLen = tagEnd - (current + 11);

                    handler.cdata(document, cdataContentOffset, cdataContentLen, currentLine, currentPos);
                    
                } else {

                    final boolean minimized = (document[tagEnd - 1] == '/');
                    final int tagContentOffset = current + 1;
                    final int tagContentLen = tagEnd - (current + (minimized? 2 : 1)); 
                    
                    parseStartTag(
                            document, tagContentOffset, tagContentLen, handler, minimized,
                            currentLine, currentPos);
                    
                }
                
                // The '>' char will be considered as processed too
                countChar(document[tagEnd], locator);
                
                current = tagEnd + 1;
                i = current;
                inTag = false;
                
            }
            
        }
        
        handler.endDocument();
        
    }
    
    
    private static void parseStartTag(
            final char[] document, final int offset, final int len, 
            final IAttoContentHandler handler, final boolean minimized,
            final int currentLine, final int currentPos)
            throws AttoParseException {

        final int maxi = offset + len;
        
        final Locator attributeLocator = new Locator(currentLine, currentPos + 1);
        
        /*
         * Extract the element name first 
         */
        
        final int elementNameEnd = 
            findNext(document, offset, maxi, CHAR_WHITESPACE_WILDCARD, attributeLocator);
        
        if (elementNameEnd == -1) {
            handler.startElement(
                    document, offset, len, minimized,
                    currentLine, currentPos);
            return;
        }

        
        handler.startElement(
                document, offset, (elementNameEnd - offset), minimized,
                currentLine, currentPos);

        int i = elementNameEnd + 1;
        int current = i;
        countChar(document[elementNameEnd], attributeLocator);

        int currentAttributeLine = attributeLocator.getLine();
        int currentAttributePos = attributeLocator.getPos();
        
        while (i < maxi) {
            
            currentAttributeLine = attributeLocator.getLine();
            currentAttributePos = attributeLocator.getPos();
            
            final int attributeEnd = 
                findNext(document, i, maxi, CHAR_WHITESPACE_WILDCARD, attributeLocator);
            
            if (attributeEnd == -1) {
                
                final int attributeOffset = current;
                final int attributeLen = maxi - current;
                parseAttribute(document, attributeOffset, attributeLen, handler, currentAttributeLine, currentAttributePos);
                i = maxi;
                continue;
                
            }
            
            if (attributeEnd > current) {
                
                final int attributeOffset = current;
                final int attributeLen = attributeEnd - current;
                parseAttribute(document, attributeOffset, attributeLen, handler, currentAttributeLine, currentAttributePos);
                countChar(document[attributeEnd], attributeLocator);
                i = attributeEnd + 1;
                current = i;
                continue;
                
            }
            
            // skip any contiguous whitespaces
            countChar(document[current], attributeLocator);
            i++;
            current = i;
            
        }
        
        
    }

    
    
    private static void parseAttribute(
            final char[] document, final int offset, final int len, final IAttoContentHandler handler, 
            final int attributeLine, final int attributePos)
            throws AttoParseException {

        final int maxi = offset + len;

        final int attributeNameEnd = findNext(document, offset, maxi, '=', null);
        
        if (attributeNameEnd == -1) {
            // This is a no-value attribute, equivalent to value = ""
            handler.attribute(
                    document, offset, len, EMPTY_CHAR_ARRAY, 0, 0, attributeLine, attributePos);
            return;
        }
        
        if (attributeNameEnd + 1 < maxi) {
            
            final int attributeValueOffset = attributeNameEnd + 1;
            final int attributeValueLen = maxi - attributeValueOffset;
            
            if (attributeValueLen >= 2 && 
                    document[attributeValueOffset] == '"' && document[maxi - 1] == '"') {
                // Value has surrounding quotes 
                handler.attribute(
                        document, offset, (attributeNameEnd - offset),
                        document, (attributeValueOffset + 1), (attributeValueLen - 2),
                        attributeLine, attributePos);
                return;
                
            }
            
            handler.attribute(
                    document, offset, (attributeNameEnd - offset),
                    document, attributeValueOffset, attributeValueLen,
                    attributeLine, attributePos);
            
            return;
            
        }
            
        handler.attribute(
                document, offset, (attributeNameEnd - offset), 
                EMPTY_CHAR_ARRAY, 0, 0, 
                attributeLine, attributePos);
        
    }
    
    
    
    
    
    private static int findNext(
            final char[] text, final int offset, final int maxi, final char target,
            final Locator locator) {
        
        boolean inValue = false;

        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (c == '"') {
                inValue = !inValue;
            } else if (!inValue && (c == target || (target == CHAR_WHITESPACE_WILDCARD && Character.isWhitespace(c)))) {
                return i;
            }

            countChar(c, locator);
            
        }
            
        return -1;
        
    }
    
    

    
    
    private static void countChar(final char c, final Locator locator) {
        if (locator != null) {
            locator.countChar(c);
        }
    }

    
    
    
    private static final class Locator {
        
        private int line;
        private int pos;
        
        Locator() {
            super();
            this.line = 1;
            this.pos = 1;
        }
        
        Locator(final int line, final int pos) {
            super();
            this.line = line;
            this.pos = pos;
        }

        public int getLine() {
            return this.line;
        }

        public int getPos() {
            return this.pos;
        }
        
        public void countChar(final char c) {
            if (c == '\n') {
                this.line++;
                this.pos = 1;
            } else {
                this.pos++;
            }
        }
        
    }
    
    
}
