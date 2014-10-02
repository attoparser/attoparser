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
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class MarkupParsingStatus {

    /*
     *   This class encapsulates the results of parsing a fragment
     *   (a buffer) of a document.
     *
     *   It contains:
     *
     *     * offset: The current artifact position, initial position
     *               of the last unfinished artifact found while parsing
     *               a buffer segment.
     *     * line, col: line and column number of the last unfinished
     *                  artifact found while parsing a buffer segment.
     *     * inStructure: signals whether the last unfinished artifact is
     *                    suspected to be a structure (in contrast to a text).
     *     * skipUntilSequence: whether we should skip parsing until the specified
     *                          markup sequence is found.
     *
     */

    int offset;
    int line;
    int col;
    boolean inStructure;
    boolean parsingEnabled;
    char[] disabilityLimitSequence;



    MarkupParsingStatus() {
        super();
    }



    public boolean isParsingEnabled() {
        return this.parsingEnabled;
    }

    public void disableParsingAfterStructure(final char[] untilSequence) {
        this.disabilityLimitSequence = untilSequence;
    }


}
