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





/**
 * <p>
 *   Common interface for DOCTYPE clauses attoDOM trees.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public interface IDocType extends INode {
    
    public String getRootElementName();
    public void setRootElementName(final String rootElementName);
    
    public String getPublicId();
    public void setPublicId(final String publicId);
    
    public String getSystemId();
    public void setSystemId(final String systemId);
    
    public String getInternalSubset();
    public void setInternalSubset(final String internalSubset);
    
}
