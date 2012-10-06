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
 *   Models a DOCTYPE clause in a attoDOM Document.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class DocType extends Node {

    private static final long serialVersionUID = 763084654353190744L;
    
    
    private String rootElementName;
    private String publicId;
    private String systemId;
    private String internalSubset;

    


    public DocType(final String rootElementName, final String publicId, final String systemId,
            final String internalSubset, final int line, final int col) {
        super(line, col);
        Validate.notNull(rootElementName, "Root element name cannot be null");
        this.rootElementName = rootElementName;
        this.publicId = publicId;
        this.systemId = systemId;
        this.internalSubset = internalSubset;
    }

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

    
    
    
    public final void visit(final AttoDOMVisitor visitor)
            throws AttoDOMVisitorException {
        visitor.visitDocType(this);
    }
    
    
}
