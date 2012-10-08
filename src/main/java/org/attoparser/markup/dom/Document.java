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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * <p>
 *   An attoDOM document, obtained from parsing a document using a
 *   {@link DOMMarkupAttoHandler} handler object.
 * </p>
 * <p>
 *   Note that attoDOM trees are <b>mutable</b>.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class Document implements Serializable {
    
    private static final long serialVersionUID = 8618216149622786146L;
    
    
    private List<Node> rootNodes = new ArrayList<Node>();
    
    
    
    public Document() {
        super();
    }
    
    

    
    /**
     * <p>
     *   Return the XML Declaration found at the document, if it exists.
     * </p>
     * 
     * @return the XML declaration, or null if none was found.
     */
    public XmlDeclaration getXmlDeclaration() {
        for (final Node rootNode : this.rootNodes) {
            if (rootNode instanceof XmlDeclaration) {
                return (XmlDeclaration) rootNode;
            }
        }
        return null;
    }

    
    /**
     * <p>
     *   Return the DOCTYPE clause found at the document, if it exists.
     * </p>
     * 
     * @return the DOCTYPE clause, or null if none was found.
     */
    public DocType getDocType() {
        for (final Node rootNode : this.rootNodes) {
            if (rootNode instanceof DocType) {
                return (DocType) rootNode;
            }
        }
        return null;
    }

    
    
    
    
    
    /**
     * <p>
     *   Returns the number of root nodes. Only one of them can be an
     *   {@link Element}.
     * </p>
     * 
     * @return the number of root nodes.
     */
    public final int numRootNodes() {
        return this.rootNodes.size();
    }
    

    /**
     * <p>
     *   Returns the document root nodes. The returned list is immutable.
     * </p>
     * 
     * @return the root nodes.
     */
    public final List<Node> getRootNodes() {
        
        if (this.rootNodes.size() == 0) {
            throw new IllegalStateException("No root Element!");
        }
        return Collections.unmodifiableList(this.rootNodes);
    }
    
    
    /**
     * <p>
     *   Returns the only {@link Element} root node.
     * </p>
     * 
     * @return the root Element.
     */
    public final Element getRootElement() {
        if (this.rootNodes.size() == 0) {
            throw new IllegalStateException("No root Element!");
        }
        for (final Node child : this.rootNodes) {
            if (child instanceof Element) {
                return (Element)child;
            }
        }
        // This should never happen
        throw new IllegalStateException("No root Element!");
    }
    
    

    
    void addRootNode(final Node newRootNode) {
        
        if (newRootNode != null) {
            this.rootNodes.add(newRootNode);
        }
        
    }

    

    
    

    /**
     * <p>
     *   Apply a visitor (implementation of {@link AttoDOMVisitorException}) 
     *   to this document, traversing all its nodes.
     * </p>
     * <p>
     *   A typical visitor implementation is {@link MarkupWriterAttoDOMVisitor}.
     * </p>
     * 
     * @param visitor the visitor to be applied
     * @throws AttoDOMVisitorException
     */
    public final void visit(final AttoDOMVisitor visitor)
            throws AttoDOMVisitorException {
        visitor.visitStartDocument(this);
        for (final Node rootNode : this.rootNodes) {
            rootNode.visit(visitor);
        }
        visitor.visitEndDocument(this);
    }
    
    
    

}
