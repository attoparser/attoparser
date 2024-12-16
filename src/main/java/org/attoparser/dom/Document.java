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

import java.io.Serializable;


/**
 * <p>
 *   Root object for a DOM object tree produced by the {@link org.attoparser.dom.DOMBuilderMarkupHandler}
 *   handler or the {@link org.attoparser.dom.IDOMMarkupParser} parser implementations.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public class Document
        extends AbstractNestableNode
        implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    
    private String documentName = null;
    
    
    public Document(final String documentName) {
        super();
        this.documentName = documentName;
    }


    public String getDocumentName() {
        return this.documentName;
    }


    public void setDocumentName(final String documentName) {
        this.documentName = documentName;
    }


    
    
    public Document cloneNode(INestableNode parent) {
        final Document document = new Document(this.documentName);
        document.setLine(getLine());
        document.setCol(getCol());
        document.setParent(parent);
        return document;
    }
    

}
