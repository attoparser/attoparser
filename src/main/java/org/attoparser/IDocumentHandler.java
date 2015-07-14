/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2014, The ATTOPARSER team (http://www.attoparser.org)
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
 *   Interface to be implemented by all handlers capable of receiving events about document start/end.
 * </p>
 * <p>
 *   Events in this interface are a part of the {@link IMarkupHandler} interface, the main handling interface in
 *   AttoParser.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 * @since 2.0.0
 * @see org.attoparser.IMarkupHandler
 *
 */
public interface IDocumentHandler {

    /**
     * <p>
     *   Called at the beginning of document parsing.
     * </p>
     *
     * @param startTimeNanos the current time (in nanoseconds) obtained when parsing starts.
     * @param line the line of the document where parsing starts (usually number 1).
     * @param col the column of the document where parsing starts (usually number 1).
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleDocumentStart(final long startTimeNanos, final int line, final int col)
            throws ParseException;



    /**
     * <p>
     *   Called at the end of document parsing.
     * </p>
     *
     * @param endTimeNanos the current time (in nanoseconds) obtained when parsing ends.
     * @param totalTimeNanos the difference between current times at the start and end of
     *        parsing (in nanoseconds).
     * @param line the line of the document where parsing ends (usually the last one).
     * @param col the column of the document where the parsing ends (usually the last one).
     * @throws ParseException if any exceptions occur during handling.
     */
    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws ParseException;

}