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

import java.io.IOException;
import java.io.Writer;



/**
 * <p>
 *   Abstract base class for objects implementing the {@link IDOMWriter}
 *   interface.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public abstract class AbstractDOMWriter implements IDOMWriter {

    
    public void write(final INode node, final Writer writer) throws IOException {
        
        if (node == null) {
            return;
        }
        
        if (node instanceof IText) {
            writeText((IText)node, writer);
            return;
        }
        if (node instanceof IElement) {
            writeElement((IElement)node, writer);
            return;
        }
        if (node instanceof IComment) {
            writeComment((IComment)node, writer);
            return;
        }
        if (node instanceof ICDATASection) {
            writeCDATASection((ICDATASection)node, writer);
            return;
        }
        if (node instanceof IDocType) {
            writeDocType((IDocType)node, writer);
            return;
        }
        if (node instanceof IDocument) {
            writeDocument((IDocument)node, writer);
            return;
        }
        if (node instanceof IXmlDeclaration) {
            writeXmlDeclaration((IXmlDeclaration)node, writer);
            return;
        }
        if (node instanceof IProcessingInstruction) {
            writeProcessingInstruction((IProcessingInstruction)node, writer);
            return;
        }
        
    }
    
    
    public abstract void writeCDATASection(final ICDATASection cdataSection, final Writer writer) throws IOException;
    public abstract void writeComment(final IComment comment, final Writer writer) throws IOException;
    public abstract void writeDocType(final IDocType docType, final Writer writer) throws IOException;
    public abstract void writeDocument(final IDocument document, final Writer writer) throws IOException;
    public abstract void writeElement(final IElement element, final Writer writer) throws IOException;
    public abstract void writeProcessingInstruction(final IProcessingInstruction processingInstruction, final Writer writer) throws IOException;
    public abstract void writeText(final IText text, final Writer writer) throws IOException;
    public abstract void writeXmlDeclaration(final IXmlDeclaration xmlDeclaration, final Writer writer) throws IOException;
    
    
}
