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

import java.io.Reader;

import org.attoparser.content.IAttoContentHandler;
import org.attoparser.exception.AttoParseException;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class MarkupAttoParser extends AbstractAttoParser {

    
    private static final char CHAR_WHITESPACE_WILDCARD = '\u01F7';
    private static final int BUFFER_SIZE = 4096;
    private static final char[] EMPTY_CHAR_ARRAY = new char[0];


    
    public MarkupAttoParser() {
        super();
    }
    

    
    
    public final void parse(
            final Reader reader, final IAttoContentHandler handler) 
            throws AttoParseException {
        
        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        
        parseDocument(reader, handler, BUFFER_SIZE);
        
    }



    
    static final void parseDocument(
            final Reader reader, final IAttoContentHandler handler, final int initialBufferSize) 
            throws AttoParseException {


        try {

            handler.startDocument();
            
            int bufferSize = initialBufferSize;
            char[] buffer = new char[bufferSize];
            BufferStatus status = new BufferStatus();
            
            int read = reader.read(buffer);
            int bufferContentSize = read;
            int lastArtifactStart = -1;
            
            while (read != -1) {
                
                parseBuffer(buffer, 0, read, handler, status);
                
                int readOffset = 0;
                int readLen = bufferSize;
                lastArtifactStart = status.getLastArtifactStart();
                BufferStatus newStatus = null;
                
                if (lastArtifactStart == 0) {
                    
                    if (read == bufferSize) {
                        // Buffer is not big enough, double it! 
                        
                        bufferSize *= 2;
                        final char[] newBuffer = new char[bufferSize];
                        System.arraycopy(buffer, 0, newBuffer, 0, read);
                        buffer = newBuffer;
                        
                        readOffset = read;
                        readLen = bufferSize - readOffset;
                        newStatus = new BufferStatus(status.getLastLine(), status.getLastPos());
                        
                    }
                    
                } else if (lastArtifactStart < read) {
                    
                    for (int i = lastArtifactStart; i < read; i++) {
                        buffer[i - lastArtifactStart] = buffer[i];
                    }
                    
                    readOffset = read - lastArtifactStart;
                    readLen = bufferSize - readOffset;
                    
                    lastArtifactStart = 0;
                    bufferContentSize = readOffset;
                    
                    newStatus = new BufferStatus(status.getLastLine(), status.getLastPos());
                    
                }
                
                read = reader.read(buffer, readOffset, readLen);
                if (read != -1) {
                    bufferContentSize = readOffset + read;
                    read += readOffset;
                    if (newStatus != null) {
                        status = newStatus;
                    }
                }

            }
            
            final int lastStart = lastArtifactStart;
            final int lastLen = bufferContentSize - lastStart; 
            if (lastLen > 0) {
                handler.text(buffer, lastStart, lastLen, status.getLastLine(), status.getLastPos());
            }
            
            handler.endDocument();
            
        } catch (final AttoParseException e) {
            throw e;
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    
    
    
    
    
    private static final void parseBuffer(
            final char[] buffer, final int offset, final int len, 
            final IAttoContentHandler handler, final BufferStatus status) 
            throws AttoParseException {

        
        int currentLine = status.getLine();
        int currentPos = status.getPos();
        
        final int maxi = offset + len;
        int i = offset;
        int current = i;
        
        boolean inTag = false;
        boolean inClosingTag = false;
        boolean inComment = false;
        boolean inCdata = false;
        
        int tagStart;
        int tagEnd;
        
        while (i < maxi) {
            
            currentLine = status.getLine();
            currentPos = status.getPos();
            
            if (!inTag) {
                
                tagStart = findNext(buffer, i, maxi, '<', inTag, status);
                
                if (tagStart == -1) {
                    status.reportBufferStatus(current, currentLine, currentPos);
                    return;
                }
            
                if (tagStart > current) {
                    // We avoid empty-string text events
                    handler.text(
                            buffer, current, (tagStart - current), 
                            currentLine, currentPos);
                }
                
                current = tagStart;
                i = current;
                inTag = true;
                
            } else {

                inClosingTag = false;
                inComment = false;
                inCdata = false;
                
                if (maxi - current > 1 &&
                        buffer[current + 1] == '/') {

                    inClosingTag = true;
                    inTag = false;
                    
                } else if (maxi - current > 3 &&
                        buffer[current + 1] == '!' && 
                        buffer[current + 2] == '-' && 
                        buffer[current + 3] == '-') {
                    
                    inComment = true;
                    inTag = false;
                    
                } else if (maxi - current > 8 &&
                        buffer[current + 1] == '!' && 
                        buffer[current + 2] == '[' && 
                        buffer[current + 3] == 'C' &&
                        buffer[current + 4] == 'D' &&
                        buffer[current + 5] == 'A' &&
                        buffer[current + 6] == 'T' &&
                        buffer[current + 7] == 'A' &&
                        buffer[current + 8] == '[') {
                    
                    inCdata = true;
                    inTag = false;
                    
                }
                        
                
                tagEnd = findNext(buffer, i, maxi, '>', inTag, status);
                
                if (tagEnd == -1) {
                    // This is an unfinished structure
                    status.reportBufferStatus(current, currentLine, currentPos);
                    return;
                }
                
                if (inClosingTag) {
                    // This is a closing tag
                    
                    handler.endElement(
                            buffer, (current + 2), (tagEnd - (current + 2)), 
                            currentLine, currentPos);
                    
                } else if (inComment) {
                    // This is a comment! (obviously ;-))
                    
                    while (tagEnd - current < 7 || buffer[tagEnd - 1] != '-' || buffer[tagEnd - 2] != '-') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        countChar(buffer[tagEnd], status);
                        tagEnd = findNext(buffer, tagEnd + 1, maxi, '>', false, status);
                        
                        if (tagEnd == -1) {
                            status.reportBufferStatus(current, currentLine, currentPos);
                            return;
                        }
                        
                    }
                    
                    final int commentContentOffset = current + 4;
                    final int commentContentLen = tagEnd - (current + 6);

                    handler.comment(buffer, commentContentOffset, commentContentLen, currentLine, currentPos);
                    
                } else if (inCdata) {
                    // This is a CDATA section
                    
                    while (tagEnd - current < 12 || buffer[tagEnd - 1] != ']' || buffer[tagEnd - 2] != ']') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        countChar(buffer[tagEnd], status);
                        tagEnd = findNext(buffer, tagEnd + 1, maxi, '>', false, status);
                        
                        if (tagEnd == -1) {
                            status.reportBufferStatus(current, currentLine, currentPos);
                            return;
                        }
                        
                    }
                    
                    final int cdataContentOffset = current + 9;
                    final int cdataContentLen = tagEnd - (current + 11);

                    handler.cdata(buffer, cdataContentOffset, cdataContentLen, currentLine, currentPos);
                    
                } else {

                    final boolean hasBody = (buffer[tagEnd - 1] != '/');
                    final int tagContentOffset = current + 1;
                    final int tagContentLen = tagEnd - (current + (hasBody? 1 : 2)); 
                    
                    parseStartTag(
                            buffer, tagContentOffset, tagContentLen, handler, hasBody,
                            currentLine, currentPos);
                    
                }
                
                // The '>' char will be considered as processed too
                countChar(buffer[tagEnd], status);
                
                current = tagEnd + 1;
                i = current;
                inTag = false;
                
            }
            
        }
        
        status.reportBufferStatus(current, currentLine, currentPos);
        
    }
    
    
    
    private static void parseStartTag(
            final char[] document, final int offset, final int len, 
            final IAttoContentHandler handler, final boolean hasBody,
            final int currentLine, final int currentPos)
            throws AttoParseException {

        final int maxi = offset + len;
        
        final BufferStatus attributeStatus = new BufferStatus(currentLine, currentPos + 1);
        
        /*
         * Extract the element name first 
         */
        
        final int elementNameEnd = 
            findNext(document, offset, maxi, CHAR_WHITESPACE_WILDCARD, true, attributeStatus);
        
        if (elementNameEnd == -1) {
            handler.startElement(
                    document, offset, len, hasBody,
                    currentLine, currentPos);
            return;
        }

        
        handler.startElement(
                document, offset, (elementNameEnd - offset), hasBody,
                currentLine, currentPos);

        int i = elementNameEnd + 1;
        int current = i;
        countChar(document[elementNameEnd], attributeStatus);

        int currentAttributeLine = attributeStatus.getLine();
        int currentAttributePos = attributeStatus.getPos();
        
        int attributeEnd = -1;
        
        while (i < maxi) {
            
            currentAttributeLine = attributeStatus.getLine();
            currentAttributePos = attributeStatus.getPos();
            
            attributeEnd = 
                findNext(document, i, maxi, CHAR_WHITESPACE_WILDCARD, true, attributeStatus);
            
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
                countChar(document[attributeEnd], attributeStatus);
                i = attributeEnd + 1;
                current = i;
                continue;
                
            }
            
            // skip any contiguous whitespaces
            countChar(document[current], attributeStatus);
            i++;
            current = i;
            
        }
        
        
    }

    
    
    private static void parseAttribute(
            final char[] document, final int offset, final int len, final IAttoContentHandler handler, 
            final int attributeLine, final int attributePos)
            throws AttoParseException {

        final int maxi = offset + len;

        final int attributeNameEnd = findNext(document, offset, maxi, '=', true, null);
        
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
            final boolean inTag, final BufferStatus context) {
        
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
    
    

    
    
    private static void countChar(final char c, final BufferStatus context) {
        if (context != null) {
            context.countChar(c);
        }
    }

    
    
    
    private static final class BufferStatus {
        
        private int line;
        private int pos;
        
        private int lastArtifactStart = -1;
        private int lastLine = -1;
        private int lastPos = -1;
        
        
        BufferStatus() {
            super();
            this.line = 1;
            this.pos = 1;
        }
        
        BufferStatus(final int line, final int pos) {
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
        
        public int getLastArtifactStart() {
            return this.lastArtifactStart;
        }

        public int getLastLine() {
            return this.lastLine;
        }

        public int getLastPos() {
            return this.lastPos;
        }

        public void reportBufferStatus(final int newLastArtifactStart, final int newLastLine, final int newLastPos) {
            this.lastArtifactStart = newLastArtifactStart;
            this.lastLine = newLastLine;
            this.lastPos = newLastPos;
        }
        
    }
    
    
}
