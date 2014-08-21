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
 *   Standard implementation of the {@link org.attoparser.IAttoHandleResult} interface.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 1.5.0
 *
 */
public final class AttoHandleResult implements IAttoHandleResult {

    /**
     * <p>
     *   Just go on with parsing - no additional instructions for parser after event handling.
     * </p>
     * <p>
     *   In all scenarios, returning this instance as a result of a <kbd>handle*</kbd> method
     *   should be completely equivalent to returning <kbd>null</kbd>. This instance is provided as
     *   an option in case code guidelines don't allow returning <kbd>null</kbd> for <em>OK</em>.
     * </p>
     */
    public static final AttoHandleResult CONTINUE = new AttoHandleResult(null, null);

    private final char[] parsingDisableLimit;
    private final char[] pushBackSequence;


    public AttoHandleResult(final char[] parsingDisableLimit, final char[] pushBackSequence) {
        super();
        this.parsingDisableLimit = parsingDisableLimit;
        this.pushBackSequence = pushBackSequence;
    }


    public char[] getParsingDisableLimit() {
        return this.parsingDisableLimit;
    }

    public char[] getPushBackSequence() {
        return this.pushBackSequence;
    }

}
