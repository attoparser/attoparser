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
 *   Base abstract class for {@link IAttoHandler} implementations,
 *   providing timing.
 * </p>
 * <p>
 *   This class provides empty implementations for all event handlers, so that
 *   subclasses can override only the methods they need.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractAttoHandler 
        implements IAttoHandler, ITimedDocumentHandling {

    private long parsingStartTimeNanos = -1L;
    private long parsingEndTimeNanos = -1L;
    
    
    protected AbstractAttoHandler() {
        super();
    }
    
    
    public final void handleDocumentStart(final int line, final int col) throws AttoParseException {
        this.parsingStartTimeNanos = System.nanoTime();
        handleDocumentStart(this.parsingStartTimeNanos, line, col);
    }

    
    public final void handleDocumentEnd(final int line, final int col) throws AttoParseException {
        this.parsingEndTimeNanos = System.nanoTime();
        final long totalTimeNanos = this.parsingEndTimeNanos - this.parsingStartTimeNanos;
        handleDocumentEnd(this.parsingEndTimeNanos, totalTimeNanos, line, col);
    }


    public void handleText(final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here
    }

    public char[] handleStructure(final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here
        return null;
    }
    
    
    public void handleDocumentStart(final long startTimeNanos, final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here
    }

    
    public void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
           throws AttoParseException {
        // Nothing to be done here
    }

    

    public final long getStartTimeNanos() {
        return this.parsingStartTimeNanos;
    }


    public final long getEndTimeNanos() {
        return this.parsingEndTimeNanos;
    }


    public final long getTotalTimeNanos() {
        return this.parsingEndTimeNanos - this.parsingStartTimeNanos;
    }
    
}