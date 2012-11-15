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

import org.attoparser.AttoParseException;
import org.attoparser.markup.AbstractDetailedMarkupAttoHandler;
import org.attoparser.markup.html.elements.HtmlElements;
import org.attoparser.markup.html.elements.IHtmlElement;






/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public abstract class AbstractDetailedHtmlAttoHandler 
        extends AbstractDetailedMarkupAttoHandler
        implements IDetailedHtmlElementHandling {

    private static final int DEFAULT_STACK_SIZE = 15;

    private static final char[] STANDALONE_START = "<".toCharArray();
    private static final char[] STANDALONE_END = "/>".toCharArray();

    private static final char[] OPEN_START = "</".toCharArray();
    private static final char[] OPEN_END = ">".toCharArray();

    private static final char[] CLOSE_START = "</".toCharArray();
    private static final char[] CLOSE_END = ">".toCharArray();
    

    
    private IHtmlElement[] elementStack;
    private IHtmlElement[][] siblingStack;
    private int elementStackSize;
    private int[] siblingStackSizes;

    // JUST ONE ELEMENT AND ONE PARENT STACK WILL BE ENOUGH!!

    
    public AbstractDetailedHtmlAttoHandler() {
        // TODO Set restrictions!!
        super();
        
        this.elementStack = new IHtmlElement[DEFAULT_STACK_SIZE];
        this.siblingStack = new IHtmlElement[DEFAULT_STACK_SIZE][];
        this.elementStackSize = 0;
        this.siblingStackSizes = new int[DEFAULT_STACK_SIZE];
        
    }
    
//    
//    
//    @Override
//    public void handleStandaloneElementStart(
//            final char[] buffer, 
//            final int offset, final int len, 
//            final int line, final int col) 
//            throws AttoParseException {
//        
//        super.handleStandaloneElementStart(buffer, offset, len, line, col);
//        // Start event is delayed until we are sure it has to be fired
//        
//    }
//    
//
//    @Override
//    public void handleStandaloneElementName(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col) 
//            throws AttoParseException {
//        
//        super.handleStandaloneElementName(buffer, offset, len, line, col);
//        
//        final IHtmlElement element = HtmlElements.lookFor(buffer, offset, len);
//        
//        
//        handleHtmlStandaloneElementStart(buffer, offset, len, line, col);
//        handleHtmlStandaloneElementName(buffer, offset, len, line, col);
//    }
//    
//
//    @Override
//    public void handleStandaloneElementEnd(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col) 
//            throws AttoParseException {
//        super.handleStandaloneElementEnd(buffer, offset, len, line, col);
//        handleHtmlStandaloneElementEnd(buffer, offset, len, line, col);
//    }
//
//    
//    
//    
//    @Override
//    public void handleOpenElementStart(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col) 
//            throws AttoParseException {
//        // TODO Auto-generated method stub
//        super.handleOpenElementStart(buffer, offset, len, line, col);
//    }
//
//    @Override
//    public void handleOpenElementName(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col) 
//            throws AttoParseException {
//        // TODO Auto-generated method stub
//        super.handleOpenElementName(buffer, offset, len, line, col);
//    }
//
//    @Override
//    public void handleOpenElementEnd(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col) 
//            throws AttoParseException {
//        // TODO Auto-generated method stub
//        super.handleOpenElementEnd(buffer, offset, len, line, col);
//    }
//
//    
//    
//    
//    @Override
//    public void handleCloseElementStart(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col) 
//            throws AttoParseException {
//        // TODO Auto-generated method stub
//        super.handleCloseElementStart(buffer, offset, len, line, col);
//    }
//
//    @Override
//    public void handleCloseElementName(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col) 
//            throws AttoParseException {
//        // TODO Auto-generated method stub
//        super.handleCloseElementName(buffer, offset, len, line, col);
//    }
//
//    @Override
//    public void handleCloseElementEnd(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col)
//            throws AttoParseException {
//        // TODO Auto-generated method stub
//        super.handleCloseElementEnd(buffer, offset, len, line, col);
//    }
//
//    
//    
//    
//    @Override
//    public void handleBalancedCloseElementStart(
//            final char[] buffer, 
//            final int offset, final int len, 
//            final int line, final int col)
//            throws AttoParseException {
//        // TODO Auto-generated method stub
//        super.handleBalancedCloseElementStart(buffer, offset, len, line, col);
//    }
//
//    @Override
//    public void handleBalancedCloseElementName(
//            final char[] buffer, 
//            final int offset, final int len, 
//            final int line, final int col) 
//            throws AttoParseException {
//        // TODO Auto-generated method stub
//        super.handleBalancedCloseElementName(buffer, offset, len, line, col);
//    }
//
//    @Override
//    public void handleBalancedCloseElementEnd(
//            final char[] buffer, 
//            final int offset, final int len, 
//            final int line, final int col) 
//            throws AttoParseException {
//        // TODO Auto-generated method stub
//        super.handleBalancedCloseElementEnd(buffer, offset, len, line, col);
//    }
//    
//    
//    
//    
//    
//    
//
//    public void handleHtmlStandaloneElementStart(
//            final char[] buffer, 
//            final int offset, final int len, 
//            final int line, final int col) 
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//
//    public void handleHtmlStandaloneElementName(
//            final char[] buffer, 
//            final int offset, final int len, 
//            final int line, final int col)
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//
//    public void handleHtmlStandaloneElementEnd(
//            final char[] buffer, 
//            final int offset, final int len, 
//            final int line, final int col)
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//
//    
//    
//    
//    public void handleHtmlUnclosedStandaloneElementStart(
//            final char[] buffer,
//            final int offset, final int len, 
//            final int line, final int col)
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//
//    public void handleHtmlUnclosedStandaloneElementName(
//            final char[] buffer,
//            final int offset, final int len, 
//            final int line, final int col)
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//
//    public void handleHtmlUnclosedStandaloneElementEnd(
//            final char[] buffer,
//            final int offset, final int len, 
//            final int line, final int col) 
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//
//    
//    
//    
//    
//    public void handleHtmlOpenElementStart(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col)
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//
//    public void handleHtmlOpenElementName(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col)
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//
//    public void handleHtmlOpenElementEnd(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col)
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//
//    
//    
//    
//    public void handleHtmlCloseElementStart(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col)
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//    
//    public void handleHtmlCloseElementName(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col) 
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//    
//    public void handleHtmlCloseElementEnd(
//            final char[] buffer, 
//            final int offset, final int len,
//            final int line, final int col) 
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//
//
//    
//    
//    public void handleHtmlBalancedCloseElementStart(
//            final char[] buffer, 
//            final int offset, final int len, 
//            final int line, final int col)
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//    
//    public void handleHtmlBalancedCloseElementName(
//            final char[] buffer, 
//            final int offset, final int len, 
//            final int line, final int col)
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//    
//    public void handleHtmlBalancedCloseElementEnd(
//            final char[] buffer, 
//            final int offset, final int len, 
//            final int line, final int col) 
//            throws AttoParseException {
//        // Nothing to be done here, meant to be overridden if required
//    }
//
//    
//    
//    
//    
//
//    
//    
//    private void checkElementPosition(
//            final IHtmlElement element, final int line, final int col) 
//            throws AttoParseException {
//        
//        final IHtmlElement parent = peekFromStack();
//        
//        if (!element.canBeChildOf(parent)) {
//            element.fixBeingChildOf(parent, this.elementStack, this);
//        }
//        if (!element.canBeSiblingOf(parent, siblings)) {
//            element.fixBeingSiblingOf(parent, this.elementStack, this);
//        }
//        
//    }
//    
//    
//    
//    private void checkStackForElement(
//            final IHtmlElement element, final int line, final int col) 
//            throws AttoParseException {
//        
//        IHtmlElement popped = popFromElementStack();
//
//        while (popped != null) {
//
//            if (popped == element) {
//                // We found the corresponding opening element!
//                return;
//            }
//            
//            popped = popFromElementStack();
//            
//        }
//        
//        // Nothing to check! just return.
//        
//    }
//    
//
//    
//    
//    private void cleanStack(final int line, final int col) 
//            throws AttoParseException {
//        
//        if (this.elementStackSize > 0) {
//            IHtmlElement popped = popFromElementStack();
//            while (popped != null) {
//                popped = popFromElementStack();
//            }
//        }
//        
//    }
//    
//    
//    
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