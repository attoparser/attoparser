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
 *   Utility class for operations on {@link IAttoHandleResult} instances.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 1.5.0
 *
 */
public final class AttoHandleResultUtil {


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * <p>
     *     Combines both instances of {@link org.attoparser.IAttoHandleResult}, giving priority to the data contained
     *     in the <kbd>last</kbd> instance.
     * </p>
     * <p>
     *     If any of the instances is null, the other one will be returned. If both are, null will be returned.
     * </p>
     * @param first the first instance to be combined.
     * @param last the last instance to be combined.
     * @return the result of combining both instances.
     */
    public static IAttoHandleResult combinePriorityLast(final IAttoHandleResult first, final IAttoHandleResult last) {

        if (first == null && last == null) {
            // Just a fail-fast -- This will be by very far the most common case, so we want to simplify it to the max
            return null;
        }
        if (first == null) {
            return last;
        }
        if (last == null) {
            return first;
        }

        final boolean firstIsStackable = (first instanceof StackableElementAttoHandleResult);
        final boolean lastIsStackable = (last instanceof StackableElementAttoHandleResult);

        if (!lastIsStackable && !firstIsStackable) {
            return new AttoHandleResult(
                    last.getParsingDisableLimit() != null? last.getParsingDisableLimit() : first.getParsingDisableLimit()
            );
        }

        if (lastIsStackable && firstIsStackable) {
            final StackableElementAttoHandleResult stackableLast = (StackableElementAttoHandleResult) last;
            final StackableElementAttoHandleResult stackableFirst = (StackableElementAttoHandleResult) first;
            return new StackableElementAttoHandleResult(
                    last.getParsingDisableLimit() != null? last.getParsingDisableLimit() : first.getParsingDisableLimit(),
                    (stackableLast.getUnstackUntil() != null? stackableLast.getUnstackUntil() : stackableFirst.getUnstackUntil()),
                    (!stackableLast.getShouldStack()? stackableLast.getShouldStack() : stackableFirst.getShouldStack())
            );
        }

        if (lastIsStackable) {
            final StackableElementAttoHandleResult stackableLast = (StackableElementAttoHandleResult) last;
            return new StackableElementAttoHandleResult(
                    last.getParsingDisableLimit() != null? last.getParsingDisableLimit() : first.getParsingDisableLimit(),
                    stackableLast.getUnstackUntil(),
                    stackableLast.getShouldStack()
            );
        }

        // Then first is stackable, last is not
        final StackableElementAttoHandleResult stackableFirst = (StackableElementAttoHandleResult) first;
        return new StackableElementAttoHandleResult(
                last.getParsingDisableLimit() != null? last.getParsingDisableLimit() : first.getParsingDisableLimit(),
                stackableFirst.getUnstackUntil(),
                stackableFirst.getShouldStack()
        );

    }




    private AttoHandleResultUtil() {
        super();
    }


}
