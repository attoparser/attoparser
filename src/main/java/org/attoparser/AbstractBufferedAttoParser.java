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

import org.attoparser.exception.AttoParseException;



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
            
            int lastArtifactStart = -1;
            int lastArtifactLine = 1;
            int lastArtifactCol = 1;
            
            while (cont) {

                final BufferParseResult bufferParseResult = 
                        parseBuffer(buffer, 0, bufferContentSize, handler, lastArtifactLine, lastArtifactCol);

                int readOffset = 0;
                int readLen = bufferSize;
                
                lastArtifactStart = bufferParseResult.getLastArtifactStart();
                lastArtifactLine = bufferParseResult.getLastArtifactLine();
                lastArtifactCol = bufferParseResult.getLastArtifactCol();
                
                if (lastArtifactStart == 0) {
                    
                    if (bufferContentSize == bufferSize) {
                        // Buffer is not big enough, double it! 
                        
                        bufferSize *= 2;
                        final char[] newBuffer = new char[bufferSize];
                        System.arraycopy(buffer, 0, newBuffer, 0, bufferContentSize);
                        buffer = newBuffer;
                        
                        readOffset = bufferContentSize;
                        readLen = bufferSize - readOffset;
                        
                    }
                    
                } else if (lastArtifactStart < bufferContentSize) {
                    
                    for (int i = lastArtifactStart; i < bufferContentSize; i++) {
                        buffer[i - lastArtifactStart] = buffer[i];
                    }
                    
                    readOffset = bufferContentSize - lastArtifactStart;
                    readLen = bufferSize - readOffset;
                    
                    lastArtifactStart = 0;
                    bufferContentSize = readOffset;
                    
                }
                
                final int read = reader.read(buffer, readOffset, readLen);
                if (read != -1) {
                    bufferContentSize = readOffset + read;
                } else {
                    cont = false;
                }

            }

            final int lastStart = lastArtifactStart;
            final int lastLen = bufferContentSize - lastStart; 
            if (lastLen > 0) {
                handler.text(buffer, lastStart, lastLen, lastArtifactLine, lastArtifactCol);
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
    
    
    
    
    

    protected static final class BufferParseResult {
        
        private final int lastArtifactStart;
        private final int lastArtifactLine;
        private final int lastArtifactCol;
        
        
        public BufferParseResult(final int lastArtifactStart, final int lastArtifactLine, final int lastArtifactCol) {
            super();
            this.lastArtifactStart = lastArtifactStart;
            this.lastArtifactLine = lastArtifactLine;
            this.lastArtifactCol = lastArtifactCol;
        }
        
        public int getLastArtifactStart() {
            return this.lastArtifactStart;
        }

        public int getLastArtifactLine() {
            return this.lastArtifactLine;
        }

        public int getLastArtifactCol() {
            return this.lastArtifactCol;
        }
    
    }
    
}
