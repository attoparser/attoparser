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

import java.util.List;



/**
 * <p>
 *   Common interface for all nodes in DOM trees that
 *   can have children nodes.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public interface INestableNode extends INode {

    public boolean hasChildren();
    public int numChildren();
    
    public List<INode> getChildren();
    public <T extends INode> List<T> getChildrenOfType(final Class<T> type);
    
    public INode getFirstChild();
    public <T extends INode> T getFirstChildOfType(final Class<T> type);
    
    public void addChild(final INode newChild);
    public void insertChild(final int index, final INode newChild);
    public void insertChildBefore(final INode before, final INode newChild);
    public void insertChildAfter(final INode after, final INode newChild);
    
    public void removeChild(final INode child);
    public void clearChildren();

}
