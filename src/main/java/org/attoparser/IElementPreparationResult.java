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
 *   Common interface for the objects resulting from calling methods that prepare the parser (or its handlers)
 *   for handling a specific element, so that operations such as stack reorganization can be adequately done in time.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public interface IElementPreparationResult {

    /**
     * <p>
     *     Return the names of the elements (as <kdb>char[]</kdb>) that should be unstacked if open before
     *     opening an element of the type of the one being prepared. For example, any open &lt;li%gt;'s in an
     *     HTML document should be auto-closed before opening a new &lt;li%gt;.
     * </p>
     * @return the array of element names to be unstacked, or null if this operation does not apply
     */
    public char[][] getUnstackElements();

    /**
     * <p>
     *     Return the names of the elements that, during an unstack operation, should serve as a limit for this
     *     unstacking operation. For example, unstacking open &lt;li%gt;'s in an HTML document when a new &lt;li%gt;
     *     appears should stop as soon as an open &lt;ul%gt; or &lt;ol%gt; are found in the stack.
     * </p>
     * @return the array of element names to serve as unstacking limits, of null if no limits apply.
     */
    public char[][] getUnstackLimits();

    /**
     * <p>
     *     Return whether the element being prepared should be included in the parsing/handling stack or not
     *     (if a stack is being used). Standalone (void) elements should not be stacked.
     * </p>
     * @return true if the element should be stacked, false if not. And null if the default value should be applied
     *         (e.g. false for standalone elements, true for open elements).
     */
    public Boolean getShouldStack();

}
