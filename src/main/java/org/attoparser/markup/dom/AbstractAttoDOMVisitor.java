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
 *   Common abstract class for visitors capable of traversing a attoDOM tree.
 * </p>
 * <p>
 *   This class provides empty implementations for every method in the visitor,
 *   so that subclasses can override just the methods that are of interest for them.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractAttoDOMVisitor implements AttoDOMVisitor {

    
    
    protected AbstractAttoDOMVisitor() {
        super();
    }
    
    
    
    public void visitStartDocument(final Document document)
            throws AttoDOMVisitorException {
        // Nothing to be done here. Meant to be overridden.        
    }

    public void visitEndDocument(final Document document)
            throws AttoDOMVisitorException {
        // Nothing to be done here. Meant to be overridden.        
    }

    public void visitXmlDeclaration(final XmlDeclaration xmlDeclaration)
            throws AttoDOMVisitorException {
        // Nothing to be done here. Meant to be overridden.        
    }

    public void visitDocType(final DocType docType)
            throws AttoDOMVisitorException {
        // Nothing to be done here. Meant to be overridden.        
    }

    public void visitProcessingInstruction(final ProcessingInstruction processingInstruction)
            throws AttoDOMVisitorException {
        // Nothing to be done here. Meant to be overridden.        
    }

    public void visitStandaloneElement(final Element element)
            throws AttoDOMVisitorException {
        // Nothing to be done here. Meant to be overridden.        
    }

    public void visitOpenElement(final Element element)
            throws AttoDOMVisitorException {
        // Nothing to be done here. Meant to be overridden.        
    }

    public void visitCloseElement(final Element element)
            throws AttoDOMVisitorException {
        // Nothing to be done here. Meant to be overridden.        
    }

    public void visitText(final Text text)
            throws AttoDOMVisitorException {
        // Nothing to be done here. Meant to be overridden.        
    }

    public void visitCDATASection(final CDATASection cdataSection)
            throws AttoDOMVisitorException {
        // Nothing to be done here. Meant to be overridden.        
    }

    public void visitComment(final Comment comment)
            throws AttoDOMVisitorException {
        // Nothing to be done here. Meant to be overridden.        
    }
    
}
