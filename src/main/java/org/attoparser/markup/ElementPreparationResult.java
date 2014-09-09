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
package org.attoparser.markup;


/**
 * <p>
 *   Standard implementation of the {@link org.attoparser.markup.IElementPreparationResult} interface.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class ElementPreparationResult implements IElementPreparationResult {

    public static final ElementPreparationResult DONT_STACK = new ElementPreparationResult(null, null, Boolean.FALSE);


    private final char[][] unstackElements;
    private final char[][] unstackLimits;
    private final Boolean shouldStack;


    public ElementPreparationResult(
            final char[][] unstackElements, final char[][] unstackLimits, final Boolean shouldStack) {
        super();
        this.unstackElements = unstackElements;
        this.unstackLimits = unstackLimits;
        this.shouldStack = shouldStack;
    }

    public char[][] getUnstackElements() {
        return this.unstackElements;
    }

    public char[][] getUnstackLimits() {
        return this.unstackLimits;
    }

    public Boolean getShouldStack() {
        return this.shouldStack;
    }

}
