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
package org.attoparser.markup.dom;

import java.io.Serializable;



/**
 * <p>
 *   Base abstract class for all nodes in a attoDOM tree.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class Node implements Serializable {

    private static final long serialVersionUID = 8123273476323610489L;
    
    private final Integer line;
    private final Integer col;
    
    Element parent;
    
    

    protected Node(final int line, final int col) {
        super();
        this.line = Integer.valueOf(line);
        this.col = Integer.valueOf(col);
    }

    
    protected Node() {
        super();
        this.line = null;
        this.col = null;
    }


    

    public Integer getLine() {
        return this.line;
    }

    
    public Integer getCol() {
        return this.col;
    }

    
    

    
    public final boolean hasParent() {
        return this.parent != null;
    }

    
    public final Element getParent() {
        return this.parent;
    }
    


    
    public abstract void visit(final AttoDOMVisitor visitor)
            throws AttoDOMVisitorException;
    

}
