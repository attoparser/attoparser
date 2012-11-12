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
package org.attoparser.markup.html.elements;

import org.attoparser.markup.html.IDetailedHtmlElementHandling;







/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class DefaultHtmlElement extends AbstractHtmlElement {

    
    DefaultHtmlElement(final String name) {
        super(name);
    }


    
    public boolean canBeChildOf(final IHtmlElement parent) {
        return true;
    }


    public void fixBeingChildOf(final IHtmlElement parent, final IHtmlElement[] stack,
            final IDetailedHtmlElementHandling handler) {
        // Nothing to do here
    }


    public boolean canBeSiblingOf(final IHtmlElement parent, final IHtmlElement[] siblings) {
        return false;
    }


    public void fixBeingSiblingOf(final IHtmlElement parent, final IHtmlElement[] siblings,
            final IHtmlElement[] stack, final IDetailedHtmlElementHandling handler) {
        // Nothing to do here
    }


    public Boolean isEmptyElement() {
        // If we don't have specific information about an
        // element having or not a body, we should declare this
        // as 'unknown' by simply returning null.
        return null;
    }
    
}