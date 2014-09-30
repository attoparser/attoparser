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

import java.io.Serializable;

import org.attoparser.dom.IDocType;
import org.attoparser.dom.INestableNode;


/**
 * <p>
 *   Default implementation of the {@link IDocType} interface.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class DocType 
        extends AbstractNode
        implements IDocType, Serializable {

    private static final long serialVersionUID = 763084654353190744L;
    
    
    private String rootElementName;
    private String publicId;
    private String systemId;
    private String internalSubset;

    


    public DocType(final String rootElementName, final String publicId, final String systemId,
            final String internalSubset) {
        super();
        Validate.notNull(rootElementName, "Root element name cannot be null");
        this.rootElementName = rootElementName;
        this.publicId = publicId;
        this.systemId = systemId;
        this.internalSubset = internalSubset;
    }

    
    
    
    public String getRootElementName() {
        return this.rootElementName;
    }
    
    public void setRootElementName(final String rootElementName) {
        Validate.notNull(rootElementName, "Root element name cannot be null");
        this.rootElementName = rootElementName;
    }
    
    
    public String getPublicId() {
        return this.publicId;
    }
    
    public void setPublicId(final String publicId) {
        this.publicId = publicId;
    }
    
    
    public String getSystemId() {
        return this.systemId;
    }
    
    public void setSystemId(final String systemId) {
        this.systemId = systemId;
    }
    
    
    public String getInternalSubset() {
        return this.internalSubset;
    }
    
    public void setInternalSubset(final String internalSubset) {
        this.internalSubset = internalSubset;
    }




    public DocType cloneNode(final INestableNode parent) {
        final DocType docType = new DocType(this.rootElementName, this.publicId, this.systemId, this.internalSubset);
        docType.setLine(getLine());
        docType.setCol(getCol());
        docType.setParent(parent);
        return docType;
    }

    
    
}
