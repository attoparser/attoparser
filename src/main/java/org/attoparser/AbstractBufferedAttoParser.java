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




/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractBufferedAttoParser extends AbstractAttoParser {

    
    private static final int BUFFER_SIZE = 4096;


    
    protected AbstractBufferedAttoParser() {
        super();
    }
    

    
    
    public final void parse(
            final Reader reader, final IAttoHandler handler) 
            throws AttoParseException {
        
        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        
        parseDocument(reader, handler, BUFFER_SIZE);
        
    }



    
    final void parseDocument(
            final Reader reader, final IAttoHandler handler, final int initialBufferSize) 
            throws AttoParseException {


        try {

            handler.startDocument();
            
            int bufferSize = initialBufferSize;
            char[] buffer = new char[bufferSize];
            
            int bufferContentSize = reader.read(buffer);
            boolean cont = (bufferContentSize != -1);
            
            int bufferParseOffset = -1;
            int bufferParseLine = 1;
            int bufferParseCol = 1;
            boolean bufferParseInStructure = false;
            
            while (cont) {

                final BufferParseResult bufferParseResult = 
                        parseBuffer(buffer, 0, bufferContentSize, handler, bufferParseLine, bufferParseCol);

                int readOffset = 0;
                int readLen = bufferSize;
                
                bufferParseOffset = bufferParseResult.getOffset();
                bufferParseLine = bufferParseResult.getLine();
                bufferParseCol = bufferParseResult.getCol();
                bufferParseInStructure = bufferParseResult.isInStructure();
                
                if (bufferParseOffset == 0) {
                    
                    if (bufferContentSize == bufferSize) {
                        // Buffer is not big enough, double it! 
                        
                        bufferSize *= 2;
                        final char[] newBuffer = new char[bufferSize];
                        System.arraycopy(buffer, 0, newBuffer, 0, bufferContentSize);
                        buffer = newBuffer;
                        
                        readOffset = bufferContentSize;
                        readLen = bufferSize - readOffset;
                        
                    }
                    
                } else if (bufferParseOffset < bufferContentSize) {
                    
                    for (int i = bufferParseOffset; i < bufferContentSize; i++) {
                        buffer[i - bufferParseOffset] = buffer[i];
                    }
                    
                    readOffset = bufferContentSize - bufferParseOffset;
                    readLen = bufferSize - readOffset;
                    
                    bufferParseOffset = 0;
                    bufferContentSize = readOffset;
                    
                }
                
                final int read = reader.read(buffer, readOffset, readLen);
                if (read != -1) {
                    bufferContentSize = readOffset + read;
                } else {
                    cont = false;
                }

            }

            final int lastStart = bufferParseOffset;
            final int lastLen = bufferContentSize - lastStart; 
            if (lastLen > 0) {
                if (bufferParseInStructure) {
                    throw new AttoParseException(
                            "Incomplete structure: \"" + new String(buffer, lastStart, lastLen) + "\"", bufferParseLine, bufferParseCol);
                }
                handler.text(buffer, lastStart, lastLen, bufferParseLine, bufferParseCol);
            }
            
            handler.endDocument();
            
        } catch (final AttoParseException e) {
            throw e;
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    
    
    
    
    
    protected abstract BufferParseResult parseBuffer(
            final char[] buffer, final int offset, final int len, 
            final IAttoHandler handler, final int line, final int col) 
            throws AttoParseException;
    
    
    
    
    

    public static final class BufferParseResult {
        
        private final int offset;
        private final int line;
        private final int col;
        private final boolean inStructure;
        
        
        public BufferParseResult(final int offset, final int line, final int col, final boolean inStructure) {
            super();
            this.offset = offset;
            this.line = line;
            this.col = col;
            this.inStructure = inStructure;
        }
        
        public int getOffset() {
            return this.offset;
        }

        public int getLine() {
            return this.line;
        }

        public int getCol() {
            return this.col;
        }

        public boolean isInStructure() {
            return this.inStructure;
        }
    
    }
    
}
