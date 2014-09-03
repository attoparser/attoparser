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
import java.util.Arrays;


/**
 * <p>
 *   Base abstract class for {@link IAttoParser} implementations reading
 *   the parsed document using a buffer.
 * </p>
 * <p>
 *   Subclasses of this abstract class should only implement the abstract
 *   {@link #parseBuffer(char[], int, int, IAttoHandler, int, int, char[])} method.
 * </p>
 * <p>
 *   This class closes the document {@link Reader} provided after parsing.  
 * </p>
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
     *   Value: 4096 chars (= 8192 bytes).
     * </p>
     */
    public static final int DEFAULT_BUFFER_SIZE = 4096;

    /**
     * <p>
     *   Default pool size to be used. Buffers will be kept in a pool and
     *   reused in order to increase performance. Pool will be non-exclusive
     *   so that if pool size = 3 and a 4th request arrives, it is assigned
     *   a new buffer object (not linked to the pool, and therefore GC-ed
     *   at the end).
     * </p>
     *
     * @since 2.0.0
     */
    public static final int DEFAULT_POOL_SIZE = 2;


    private final BufferPool pool;



    protected AbstractBufferedAttoParser() {
        this(DEFAULT_POOL_SIZE, DEFAULT_BUFFER_SIZE);
    }

    protected AbstractBufferedAttoParser(final int poolSize, final int bufferSize) {
        super();
        this.pool = new BufferPool(poolSize, bufferSize);
    }



    
    @Override
    protected final void parseDocument(
            final Reader reader, final IAttoHandler handler) 
            throws AttoParseException {
        parseDocument(reader, handler, this.pool.defaultBufferSize);
    }




    /*
     * This method receiving the buffer size with package visibility allows
     * testing different buffer sizes.
     */
    final void parseDocument(
            final Reader reader, final IAttoHandler handler, final int initialBufferSize) 
            throws AttoParseException {


        char[] buffer = null;

        try {

            handler.handleDocumentStart(1, 1);
            
            int bufferSize = initialBufferSize;
            buffer = this.pool.allocateBuffer(bufferSize);
            
            int bufferContentSize = reader.read(buffer);
            boolean cont = (bufferContentSize != -1);
            
            int bufferParseOffset = -1;
            int bufferParseLine = 1;
            int bufferParseCol = 1;
            boolean bufferParseInStructure = false;

            char[] skipUntilSeq = null;

            while (cont) {

                final BufferParseResult bufferParseResult = 
                    parseBuffer(buffer, 0, bufferContentSize, handler, bufferParseLine, bufferParseCol, skipUntilSeq);

                int readOffset = 0;
                int readLen = bufferSize;
                
                bufferParseOffset = bufferParseResult.getOffset();
                bufferParseLine = bufferParseResult.getLine();
                bufferParseCol = bufferParseResult.getCol();
                bufferParseInStructure = bufferParseResult.isInStructure();
                skipUntilSeq = bufferParseResult.getSkipUntilSequence();
                
                if (bufferParseOffset == 0) {
                    
                    if (bufferContentSize == bufferSize) {
                        // Buffer is not big enough, double it! 

                        char[] newBuffer = null;
                        try {

                            bufferSize *= 2;

                            newBuffer = this.pool.allocateBuffer(bufferSize);
                            System.arraycopy(buffer, 0, newBuffer, 0, bufferContentSize);

                            this.pool.releaseBuffer(buffer);

                            buffer = newBuffer;

                        } catch (final Exception e) {
                            this.pool.releaseBuffer(newBuffer);
                        }

                    }

                    // it's possible for two reads to occur in a row and 1) read less than the bufferSize and 2)
                    // still not find the next tag/end of structure
                    readOffset = bufferContentSize;
                    readLen = bufferSize - readOffset;

                } else if (bufferParseOffset < bufferContentSize) {

                    System.arraycopy(buffer, bufferParseOffset, buffer, 0, bufferContentSize - bufferParseOffset);
                    
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

            int lastLine = bufferParseLine;
            int lastCol = bufferParseCol;
            
            final int lastStart = bufferParseOffset;
            final int lastLen = bufferContentSize - lastStart;
            
            if (lastLen > 0) {
                
                if (bufferParseInStructure) {
                    throw new AttoParseException(
                            "Incomplete structure: \"" + new String(buffer, lastStart, lastLen) + "\"", bufferParseLine, bufferParseCol);
                }
                
                handler.handleText(buffer, lastStart, lastLen, bufferParseLine, bufferParseCol);
                
                // As we have produced an additional text event, we need to fast-forward the
                // lastLine and lastCol position to include the last text structure.
                for (int i = lastStart; i < (lastStart + lastLen); i++) {
                    final char c = buffer[i];
                    if (c == '\n') {
                        lastLine++;
                        lastCol = 1;
                    } else {
                        lastCol++;
                    }

                }
                
            }
            
            handler.handleDocumentEnd(lastLine, lastCol);
            
        } catch (final AttoParseException e) {
            throw e;
        } catch (final Exception e) {
            throw new AttoParseException(e);
        } finally {
            this.pool.releaseBuffer(buffer);
            try {
                reader.close();
            } catch (final Throwable t) {
                // This exception can be safely ignored
            }
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
            final IAttoHandler handler, final int line, final int col,
            final char[] skipUntil)
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
        private final char[] skipUntilSequence;
        
        
        /**
         * <p>
         *   Create a new instance of this class.
         * </p>
         * 
         * @param offset the offset of the last unfinished artifact.
         * @param line line of the last unfinished artifact.
         * @param col column of the last unfinished artifact.
         * @param inStructure whether the last unfinished artifact is a structure or not.
         * @param skipUntilSequence whether parsing must be disabled until a specific char sequence is found, or null.
         */
        public BufferParseResult(final int offset, final int line, final int col, final boolean inStructure,
                                 final char[] skipUntilSequence) {
            super();
            this.offset = offset;
            this.line = line;
            this.col = col;
            this.inStructure = inStructure;
            this.skipUntilSequence = skipUntilSequence;
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

        public char[] getSkipUntilSequence() {
            return skipUntilSequence;
        }

    }



    static final class BufferPool {

        private char[][] pool;
        private boolean[] allocated;
        private int defaultBufferSize;

        private BufferPool(final int poolSize, final int defaultBufferSize) {

            super();

            this.pool = new char[poolSize][];
            this.allocated = new boolean[poolSize];
            this.defaultBufferSize = defaultBufferSize;

            for (int i = 0; i < this.pool.length; i++) {
                this.pool[i] = new char[this.defaultBufferSize];
            }
            Arrays.fill(this.allocated, false);

        }

        private synchronized char[] allocateBuffer(final int bufferSize) {
            if (bufferSize != this.defaultBufferSize) {
                // We will only allocate buffers of the default size
                return new char[bufferSize];
            }
            for (int i = 0; i < this.pool.length; i++) {
                if (!this.allocated[i]) {
                    this.allocated[i] = true;
                    return this.pool[i];
                }
            }
            return new char[bufferSize];
        }

        private synchronized void releaseBuffer(final char[] buffer) {
            if (buffer == null) {
                return;
            }
            if (buffer.length != this.defaultBufferSize) {
                // This buffer is not part of the pool
                return;
            }
            for (int i = 0; i < this.pool.length; i++) {
                if (this.pool[i] == buffer) {
                    // Found it. Mark it as non-allocated
                    this.allocated[i] = false;
                    return;
                }
            }
            // The buffer wasn't part of our pool
            return;
        }


    }


}
