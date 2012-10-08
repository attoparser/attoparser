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
 *   Handler feature interface to be implemented by {@link IAttoHandler} implementations
 *   that time their execution.
 * </p>
 * <p>
 *   All times are taken in nanoseconds.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public interface ITimedDocumentHandling {

    
    /**
     * <p>
     *   Called at the beginning of document parsing, adding timing information.
     * </p>
     * 
     * @param startTimeNanos the current time (in nanoseconds) obtained when parsing starts.
     * @throws AttoParseException
     */
    public void handleDocumentStart(final long startTimeNanos) throws AttoParseException;

    /**
     * <p>
     *   Called at the end of document parsing, adding timing information.
     * </p>
     * 
     * @param endTimeNanos the current time (in nanoseconds) obtained when parsing ends.
     * @param totalTimeNanos the difference between current times at the start and end of
     *        parsing (in nanoseconds)
     * @throws AttoParseException
     */
    public void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos) throws AttoParseException;
    

    /**
     * <p>
     *   Return the current time (in nanoseconds) obtained when parsing starts.
     * </p>
     * 
     * @return the start time in nanos.
     */
    public long getStartTimeNanos();

    /**
     * <p>
     *   Return the current time (in nanoseconds) obtained when parsing ends.
     * </p>
     * 
     * @return the end time in nanos.
     */
    public long getEndTimeNanos();

    /**
     * <p>
     *   Return the difference (in nanoseconds) between parsing start and end times.
     * </p>
     * 
     * @return the difference between parsing start and end times.
     */
    public long getTotalTimeNanos();
    
}
