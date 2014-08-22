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
 *   Common interface for encapsulating the information given back to the parser after
 *   handling one of the events it produces.
 * </p>
 * <p><b>Mutability</b></p>
 * <p>
 *   It is recommended that all implementations of this interface are <strong>immutable</strong>.
 * </p>
 * <p><b>Thread safety</b></p>
 * <p>
 *   Unless contrary specified, <b>implementations of this interface are not thread-safe</b>.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 1.5.0
 *
 */
public interface IAttoHandleResult {


    /**
     * <p>
     *   Return the char sequence until which parsing should be disabled (if applies).
     * </p>
     * <p>
     *   This method will return a <kbd>char[]</kbd> if, as a result of handling this structure, parsing
     *   should be disabled until the returned char sequence is found in input. This allows
     *   e.g. ignoring the contents of non-processable elements like HTML's <kbd>&lt;script&gt;</kbd>
     *   or <kbd>&lt;style&gt;</kbd>.
     * </p>
     *
     * @return a char[] if, as a result of handling this structure, parsing should
     *         be disabled until the returned char sequence is found in input. Will return
     *         <kbd>null</kbd> if parsing should not be disabled (most common case).
     */
    public char[] getParsingDisableLimit();

}
