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
 *   Implementation of the {@link org.attoparser.IAttoHandleResult} able to instruct stack-aware handling layers
 *   to avoid stacking the handled element, or to auto-close (unstack) stacked elements before handling the current
 *   one.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class StackableElementAttoHandleResult extends AttoHandleResult {

    public static final StackableElementAttoHandleResult DONT_STACK = new StackableElementAttoHandleResult(null, null, false);


    private final char[] unstackUntil;
    private final boolean shouldStack;


    public StackableElementAttoHandleResult(
            final char[] parsingDisableLimit, final char[] unstackUntil, final boolean shouldStack) {
        super(parsingDisableLimit);
        this.unstackUntil = unstackUntil;
        this.shouldStack = shouldStack;
    }


    public char[] getUnstackUntil() {
        return this.unstackUntil;
    }

    public boolean getShouldStack() {
        return this.shouldStack;
    }

}
