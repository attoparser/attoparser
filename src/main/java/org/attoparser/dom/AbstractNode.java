/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2025 Attoparser (https://www.attoparser.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       https://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.attoparser.dom;


/*
 * Base abstract class for all nodes in a DOM tree.
 *
 * @author Daniel Fernandez
 * @since 2.0.0
 */
abstract class AbstractNode implements INode {
    
    
    private Integer line = null;
    private Integer col = null;
    
    private INestableNode parent;
    
    

    protected AbstractNode() {
        super();
    }



    public boolean hasLine() {
        return this.line != null;
    }

    public Integer getLine() {
        return this.line;
    }

    public void setLine(final Integer line) {
        this.line = line;
    }

    
    
    public boolean hasCol() {
        return this.col != null;
    }

    public Integer getCol() {
        return this.col;
    }

    public void setCol(final Integer col) {
        this.col = col;
    }


    
    public boolean hasParent() {
        return this.parent != null;
    }
    
    public INestableNode getParent() {
        return this.parent;
    }

    public void setParent(final INestableNode parent) {
        this.parent = parent;
    }

    
}
