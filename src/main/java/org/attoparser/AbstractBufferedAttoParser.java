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
 * <p>
 *   Base abstract class for {@link IAttoParser} implementations reading
 *   the parsed document using a buffer.
 * </p>
 * <p>
 *   Subclasses of this abstract class should only implement the abstract
 *   {@link #parseBuffer(char[], int, int, IAttoHandler, int, int)} method.
 * </p>
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractBufferedAttoParser extends AbstractAttoParser {

    
    /**
     * <p>
     *   Default buffer size to be used (buffer size will grow at runtime if 
     *   an artifact (structure or text) is bigger than the whole buffer). 
     *   Value: 4096 chars.
     * </p>
     */
    public static final int BUFFER_SIZE = 4096;


    
    protected AbstractBufferedAttoParser() {
        super();
    }
    

    
    @Override
    protected final void parseDocument(
            final Reader reader, final IAttoHandler handler) 
            throws AttoParseException {
        parseDocument(reader, handler, BUFFER_SIZE);
    }




    /*
     * This method receiving the buffer size with package visibility allows
     * testing different buffer sizes.
     */
    final void parseDocument(
            final Reader reader, final IAttoHandler handler, final int initialBufferSize) 
            throws AttoParseException {


        try {

            handler.handleDocumentStart();
            
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
                handler.handleText(buffer, lastStart, lastLen, bufferParseLine, bufferParseCol);
            }
            
            handler.handleDocumentEnd();
            
        } catch (final AttoParseException e) {
            throw e;
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    
    
    
    
    /**
     * <p>
     *   Parse a buffer segment and return a {@link BufferParseResult} reporting
     *   the results of this parsing.
     * </p>
     * 
     * @param buffer the document buffer.
     * @param offset the offset (in the document buffer) of the segment to be parsed.
     * @param len the length (in chars) of the segment to be parsed.
     * @param handler the {@link IAttoHandler} implementation to be used for reporting events.
     * @param line the line of the first position (offset) in buffer.
     * @param col the column of the first position (offset) in buffer.
     * @return a {@link BufferParseResult} reporting the parsing status.
     * @throws AttoParseException if an error occurs.
     */
    protected abstract BufferParseResult parseBuffer(
            final char[] buffer, final int offset, final int len, 
            final IAttoHandler handler, final int line, final int col) 
            throws AttoParseException;
    
    
    
    
    
    /**
     * <p>
     *   This class encapsulates the results of parsing a fragment
     *   (a buffer) of a document.
     * </p>
     * <p>
     *   Will only be used by implementations of {@link AbstractBufferedAttoParser}.
     * </p>
     * <p>
     *   It contains:
     * </p>
     * <ul>
     *   <li><tt>offset</tt>: The <i>current artifact position</i>, initial position
     *       of the last unfinished artifact found while parsing
     *       a buffer segment.</li>
     *   <li><tt>line</tt>, <tt>col</tt>: line and column number of the last unfinished 
     *       artifact found while parsing a buffer segment.</li>
     *   <li><tt>inStructure</tt>: signals whether the last unfinished artifact is
     *       suspected to be a structure (in contrast to a text).</li>
     * </ul>
     * 
     * @author Daniel Fern&aacute;ndez
     * 
     * @since 1.0
     *
     */
    public static final class BufferParseResult {
        
        private final int offset;
        private final int line;
        private final int col;
        private final boolean inStructure;
        
        
        /**
         * <p>
         *   Create a new instance of this class.
         * </p>
         * 
         * @param offset the offset of the last unfinished artifact.
         * @param line line of the last unfinished artifact.
         * @param col column of the last unfinished artifact.
         * @param inStructure whether the last unfinished artifact is a structure or not.
         */
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
