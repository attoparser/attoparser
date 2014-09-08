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

    private static final int DEFAULT_STACK_SIZE = 20;
    private static final int NO_PARENT = -1;
    
    
    public IHtmlElement[] elements;
    public int[] parents;
    public int size = 0;
    public int openIndex = -1;


    
    
    public HtmlElementStack() {
        
        super();
        
        this.elements = new IHtmlElement[DEFAULT_STACK_SIZE];
        Arrays.fill(this.elements, (IHtmlElement)null);
        
        this.parents = new int[DEFAULT_STACK_SIZE];
        Arrays.fill(this.parents, NO_PARENT);

        this.size = 0;
        this.openIndex = NO_PARENT;
        
    }

    
    
    
    
    
    public void openElement(final IHtmlElement element) {
        // A new element is added to the stack, and every new element
        // added after this will be considered its child.
        if (this.size == this.elements.length) {
            growStack();
        }
        this.elements[this.size] = element;
        this.parents[this.size] = this.openIndex;
        this.openIndex = this.size;
        this.size++;
    }

    
    public void closeElement() {
        // We will close the element currently considered "open", and
        // remove all of its children from the stack.
        // New "open" element will be the old open one's parent.
        
        if (this.size == 0 || this.openIndex == NO_PARENT) {
            // An "unmatched close" event will be probably generated somewhere. But anyway, we cannot
            // close any elements here, so just return.
            return;
        }
        
        int i = this.size - 1;
        while (i > this.openIndex) {
            this.elements[i] = null;
            this.parents[i] = NO_PARENT;
            i--;
        }
        
        // Now "i" must be equal to openIndex
        if (i != this.openIndex) {
            throw new IllegalStateException("Malformed stack");
        }
        
        this.size = this.openIndex + 1;
        this.openIndex = this.parents[this.openIndex];
        
    }
    
    

    
    private void growStack() {
        
        final int newStackSize = this.elements.length + DEFAULT_STACK_SIZE;
        
        final IHtmlElement[] newElements = new IHtmlElement[newStackSize];
        System.arraycopy(this.elements, 0, newElements, 0, this.elements.length);
        
        final int[] newParents = new int[newStackSize];
        System.arraycopy(this.parents, 0, newParents, 0, this.parents.length);
        
        this.elements = newElements;
        this.parents = newParents;
        
    }
    
    

    
    
    @Override
    public String toString() {
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("[ ");
        if (this.size > 0) {
            strBuilder.append(0);
            strBuilder.append('^');
            strBuilder.append(this.parents[0]);
            strBuilder.append(':');
            strBuilder.append(this.elements[0]);
            for (int i = 1; i < this.size; i++) {
                strBuilder.append(" | ");
                strBuilder.append(i);
                strBuilder.append('^');
                strBuilder.append(this.parents[i]);
                strBuilder.append(':');
                strBuilder.append(this.elements[i]);
            }
        }
        strBuilder.append(" ] {^");
        strBuilder.append(this.openIndex);
        strBuilder.append('}');
        return strBuilder.toString();
    }

    
    
}