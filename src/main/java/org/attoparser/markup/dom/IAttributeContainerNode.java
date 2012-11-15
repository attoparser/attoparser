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

import java.util.Map;



/**
 * <p>
 *   Common interface for all nodes in attoDOM trees that
 *   can serve as attribute containers.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public interface IAttributeContainerNode extends INode {
    
    
    public int numAttributes();
    public boolean hasAttributes();
    
    public boolean hasAttribute(final String name);
    public String getAttributeValue(final String name);
    
    public Map<String,String> getAttributeMap();

    public void addAttribute(final String name, final String value);
    public void addAttributes(final Map<String,String> newAttributes);
    
    public void removeAttribute(final String attributeName);
    public void removeAttributeIgnoreCase(final String attributeName);
    public void clearAttributes();
    

}
