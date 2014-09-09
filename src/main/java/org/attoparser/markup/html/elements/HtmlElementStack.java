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

import java.util.Arrays;






/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class HtmlElementStack {

    private static final int DEFAULT_STACK_LEN = 10;

    
    public IHtmlElement[] elements;
    public int size = 0;


    
    
    public HtmlElementStack() {
        
        super();
        
        this.elements = new IHtmlElement[DEFAULT_STACK_LEN];
        Arrays.fill(this.elements, (IHtmlElement)null);

        this.size = 0;

    }

    
    
    
    
    
    public void pushElement(final IHtmlElement element) {
        // A new element is added to the stack, and every new element
        // added after this will be considered its child.
        if (this.size == this.elements.length) {
            growStack();
        }
        this.elements[this.size] = element;
        this.size++;
    }

    
    public void popElement() {
        // We will close the element currently considered "open"
        // New "open" element will be the old open one's parent.
        
        if (this.size == 0) {
            // An "unmatched close" event will be probably generated somewhere. But anyway, we cannot
            // close any elements here, so just return.
            return;
        }

        this.size--;

    }


    public IHtmlElement peekElement(final int delta) {
        if (this.size <= delta) {
            return null;
        }
        return this.elements[(this.size - 1) - delta];
    }
    
    

    
    private void growStack() {
        
        final int newStackSize = this.elements.length + DEFAULT_STACK_LEN;
        
        final IHtmlElement[] newElements = new IHtmlElement[newStackSize];
        System.arraycopy(this.elements, 0, newElements, 0, this.elements.length);

        this.elements = newElements;

    }
    
    

    
    
    @Override
    public String toString() {
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("[ ");
        if (this.size > 0) {
            strBuilder.append(0);
            strBuilder.append(':');
            strBuilder.append(this.elements[0]);
            for (int i = 1; i < this.size; i++) {
                strBuilder.append(" | ");
                strBuilder.append(i);
                strBuilder.append(':');
                strBuilder.append(this.elements[i]);
            }
        }
        strBuilder.append(" ]");
        return strBuilder.toString();
    }

    
    
}