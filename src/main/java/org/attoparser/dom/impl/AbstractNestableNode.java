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
package org.attoparser.dom.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.attoparser.dom.INestableNode;
import org.attoparser.dom.INode;



/**
 * <p>
 *   Abstract base class for all implementation of {@link INestableNode}.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public abstract class AbstractNestableNode 
        extends AbstractNode
        implements INestableNode {

    
    private List<INode> children = null;
    private int childrenLen = 0;
    



    protected AbstractNestableNode() {
        super();
    }

    

    
    
    
    public boolean hasChildren() {
        return this.childrenLen != 0;
    }
    

    public int numChildren() {
        return this.childrenLen;
    }
    
    
    

    public List<INode> getChildren() {
        if (this.childrenLen == 0) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.children);
    }

    
    @SuppressWarnings("unchecked")
    public <T extends INode> List<T> getChildrenOfType(final Class<T> type) {
        if (this.childrenLen == 0) {
            return Collections.emptyList();
        }
        final List<T> selectedChildren = new ArrayList<T>();
        for (final INode child : this.children) {
            if (type.isInstance(child)) {
                selectedChildren.add((T)child);
            }
        }
        return Collections.unmodifiableList(selectedChildren);
    }


    
    public INode getFirstChild() {
        if (this.childrenLen == 0) {
            return null;
        }
        return this.children.get(0);
    }


    @SuppressWarnings("unchecked")
    public <T extends INode> T getFirstChildOfType(final Class<T> type) {
        if (this.childrenLen == 0) {
            return null;
        }
        for (final INode child : this.children) {
            if (type.isInstance(child)) {
                return (T) child;
            }
        }
        return null;
    }

    

    
    public void addChild(final INode newChild) {
        
        if (newChild != null) {
            
            if (this.childrenLen == 0) {
                this.children = new ArrayList<INode>();
            }
            this.children.add(newChild);
            this.childrenLen++;
            
            newChild.setParent(this);
            
        }
        
    }
    
    
    public final void insertChild(final int index, final INode newChild) {
        
        if (newChild != null) {
            
            if (this.childrenLen == 0) {
                this.children = new ArrayList<INode>();
            }
            
            if (index <= this.childrenLen) {
                
                this.children.add(index, newChild);
                this.childrenLen++;
                
                newChild.setParent(this);
                
            }
            
        }
        
    }

    
    public final void insertChildBefore(final INode before, final INode newChild) {
        
        if (newChild != null) {
            
            if (this.childrenLen > 0) {
                for (int i = 0; i < this.childrenLen; i++) {
                    final INode currentChild = this.children.get(i);
                    if (currentChild == before) {
                        insertChild(i, newChild);
                        return;
                    }
                }
            }
            
        }
        
    }
    
    
    public final void insertChildAfter(final INode after, final INode newChild) {
        
        if (newChild != null) {
            
            if (this.childrenLen > 0) {
                for (int i = 0; i < this.childrenLen; i++) {
                    final INode currentChild = this.children.get(i);
                    if (currentChild == after) {
                        insertChild(i + 1, newChild);
                        return;
                    }
                }
            }
            
        }
        
    }

    
    public final void removeChild(final INode child) {
        
        if (child != null && child.getParent() == this) {
            
            final Iterator<INode> childrenIter = this.children.iterator();
            while (childrenIter.hasNext()) {
                final INode nodeChild = childrenIter.next();
                if (nodeChild == child) {
                    childrenIter.remove();
                    this.childrenLen--;
                    break;
                }
            }
            if (this.childrenLen == 0) {
                this.children = null;
            }
            
        }
        
    }
    
    

    public final void clearChildren() {
        this.children = null;
        this.childrenLen = 0;
    }




    

}