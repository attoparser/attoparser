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
 *   Common interface for visitors capable of traversing a attoDOM tree.
 * </p>
 * <p>
 *   A typical implementation is {@link MarkupWriterAttoDOMVisitor}, which
 *   writes markup from a DOM tree.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public interface AttoDOMVisitor {
    
    /**
     * <p>
     *   Visit a {@link Document} node before its contents (children)
     *   have been visited.
     * </p>
     * <p>
     *   This method does not need to traverse the document components
     *   (XML Declaration, DOCTYPE, root nodes).
     * </p>
     * 
     * @param document the document to be visited.
     * @throws AttoDOMVisitorException
     */
    public void visitStartDocument(final Document document)
            throws AttoDOMVisitorException;
    
    /**
     * <p>
     *   Visit a {@link Document} node after its contents (children)
     *   have been visited.
     * </p>
     * <p>
     *   This method does not need to traverse the document components
     *   (XML Declaration, DOCTYPE, root nodes).
     * </p>
     * 
     * @param document the document to be visited.
     * @throws AttoDOMVisitorException
     */
    public void visitEndDocument(final Document document)
            throws AttoDOMVisitorException;
    
    /**
     * <p>
     *   Visit an {@link XmlDeclaration} node.
     * </p>
     * 
     * @param xmlDeclaration the node to be visited.
     * @throws AttoDOMVisitorException
     */
    public void visitXmlDeclaration(final XmlDeclaration xmlDeclaration)
            throws AttoDOMVisitorException;
    
    /**
     * <p>
     *   Visit a {@link DocType} node.
     * </p>
     * 
     * @param docType the node to be visited.
     * @throws AttoDOMVisitorException
     */
    public void visitDocType(final DocType docType)
            throws AttoDOMVisitorException;

    
    /**
     * <p>
     *   Visit a {@link ProcessingInstruction} node.
     * </p>
     * 
     * @param processingInstruction the node to be visited.
     * @throws AttoDOMVisitorException
     */
    public void visitProcessingInstruction(final ProcessingInstruction processingInstruction)
            throws AttoDOMVisitorException;
    
    /**
     * <p>
     *   Visit a standalone {@link Element} node.
     * </p>
     * 
     * @param element the element to be visited.
     * @throws AttoDOMVisitorException
     */
    public void visitStandaloneElement(final Element element)
            throws AttoDOMVisitorException;
    
    /**
     * <p>
     *   Visit a non-standalone {@link Element} node before 
     *   its children are visited.
     * </p>
     * <p>
     *   This method does not need to traverse the element children, this
     *   will be done automatically after calling this handler.
     * </p>
     * 
     * @param element the element to be visited.
     * @throws AttoDOMVisitorException
     */
    public void visitOpenElement(final Element element)
            throws AttoDOMVisitorException;
    
    /**
     * <p>
     *   Visit a non-standalone {@link Element} node after 
     *   its children have been visited.
     * </p>
     * <p>
     *   This method does not need to traverse the element children,
     *   this is done before calling this <i>closing</i> handler.
     * </p>
     * 
     * @param element the element to be visited.
     * @throws AttoDOMVisitorException
     */
    public void visitCloseElement(final Element element)
            throws AttoDOMVisitorException;

    
    /**
     * <p>
     *   Visit a {@link Text} node.
     * </p>
     * 
     * @param text the node to be visited.
     * @throws AttoDOMVisitorException
     */
    public void visitText(final Text text)
            throws AttoDOMVisitorException;

    
    /**
     * <p>
     *   Visit a {@link CDATASection} node.
     * </p>
     * 
     * @param cdataSection the node to be visited.
     * @throws AttoDOMVisitorException
     */
    public void visitCDATASection(final CDATASection cdataSection)
            throws AttoDOMVisitorException;

    
    /**
     * <p>
     *   Visit a {@link Comment} node.
     * </p>
     * 
     * @param comment the node to be visited.
     * @throws AttoDOMVisitorException
     */
    public void visitComment(final Comment comment)
            throws AttoDOMVisitorException;
    
}
