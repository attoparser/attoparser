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
package org.attoparser.markup.html;

import org.attoparser.markup.html.elements.IHtmlElement;





/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class HtmlElementStack {

    private static final int DEFAULT_STACK_SIZE = 20;
    
    
    public IHtmlElement[] elements;
    public int[] parents;
    public int size = 0;

    
    public HtmlElementStack() {
        
        super();
        
        this.elements = new IHtmlElement[DEFAULT_STACK_SIZE];
        this.parents = new int[DEFAULT_STACK_SIZE];
        this.size = 0;
        
        cleanStack(0);
        
    }

    
    
    
    
    
    
    
    


    
    

    private void cleanStack(final int from) {
        
        for (int i = from; i < this.elements.length; i++) {
            this.elements[i] = null;
            this.parents[i] = -1;
        }
        
    }
    
    
    
//    private void addToElementStack(final IHtmlElement element) {
//        
//        if (this.elementStackSize == this.elementStack.length) {
//            this.elementStack = growStack(this.elementStack);
//            this.siblingStack = growStack(this.siblingStack);
//            this.siblingStackSizes = growStack(this.siblingStackSizes);
//        }
//        
//        this.elementStack[this.elementStackSize] = element;
//        this.elementStackSize++;
//        
//        t
//        
//    }
//
//    
//    private IHtmlElement peekFromStack() {
//        
//        if (this.elementStackSize == 0) {
//            return null;
//        }
//        
//        return this.elementStack[this.elementStackSize - 1];
//        
//    }
//
//    
//    private IHtmlElement popFromElementStack() {
//        
//        if (this.elementStackSize == 0) {
//            return null;
//        }
//        
//        this.elementStackSize--;
//        
//        final IHtmlElement popped = this.elementStack[this.elementStackSize];
//        this.elementStack[this.elementStackSize] = null;
//
//        return popped;
//        
//    }
//    
//
//    
//    private void growElementStack() {
//        if (this.elementStack == null) {
//            this.elementStack = new IHtmlElement[DEFAULT_STACK_SIZE];
//            return;
//        }
//        final int newStackSize = stack.length * 2;
//        final IHtmlElement[] newStack = new IHtmlElement[newStackSize];
//        System.arraycopy(stack, 0, newStack, 0, stack.length);
//        return newStack;
//    }
//    
//    private IHtmlElement[] growStack(final IHtmlElement[] stack) {
//        if (stack == null) {
//            return new IHtmlElement[DEFAULT_STACK_SIZE];
//        }
//        final int newStackSize = stack.length * 2;
//        final IHtmlElement[] newStack = new IHtmlElement[newStackSize];
//        System.arraycopy(stack, 0, newStack, 0, stack.length);
//        return newStack;
//    }
//    
//    private IHtmlElement[] growStack(final IHtmlElement[] stack) {
//        if (stack == null) {
//            return new IHtmlElement[DEFAULT_STACK_SIZE];
//        }
//        final int newStackSize = stack.length * 2;
//        final IHtmlElement[] newStack = new IHtmlElement[newStackSize];
//        System.arraycopy(stack, 0, newStack, 0, stack.length);
//        return newStack;
//    }
//    
    

    
    
}