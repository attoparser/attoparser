/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2022, The ATTOPARSER team (https://www.attoparser.org)
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




/**
 * <p>
 *   Base interface for all nodes in a DOM tree.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public interface INode {

    public boolean hasLine();
    public Integer getLine();
    public void setLine(final Integer line);
    
    public boolean hasCol();
    public Integer getCol();
    public void setCol(final Integer col);

    public boolean hasParent();
    public INestableNode getParent();
    public void setParent(final INestableNode parent);
   
    public INode cloneNode(final INestableNode parent);

}
