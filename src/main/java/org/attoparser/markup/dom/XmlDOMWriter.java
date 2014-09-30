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
import java.util.Map;


/**
 * <p>
 *   Specialization of {@link XmlDOMWriter} for writing attoDOM trees
 *   as XML markup. 
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class XmlDOMWriter extends AbstractDOMWriter {

    
    

    /**
     * <p>
     *   Create a new instance of this DOM writer.
     * </p>
     */
    public XmlDOMWriter() {
        super();
    }
    
    
    
    
    @Override
    public void writeCDATASection(final ICDATASection cdataSection, final Writer writer)
            throws IOException{
        
        writer.write("<![CDATA[");
        writer.write(cdataSection.getContent());
        writer.write("]]>");
        
    }

    

    @Override
    public void writeComment(final IComment comment, final Writer writer) throws IOException {
        
        writer.write("<!--");
        writer.write(comment.getContent());
        writer.write("-->");
        
    }

    

    @Override
    public void writeDocType(final IDocType docType, final Writer writer) throws IOException {
        
        writer.write("<!DOCTYPE ");
        writer.write(docType.getRootElementName());
        
        final String publicId = docType.getPublicId();
        final String systemId = docType.getSystemId();
        final String internalSubset = docType.getInternalSubset();

        if (publicId != null || systemId != null) {
            
            final String type = (publicId == null? "SYSTEM" : "PUBLIC");
            
            writer.write(' ');
            writer.write(type);
            
            if (publicId != null) {
                writer.write(' ');
                writer.write('"');
                writer.write(publicId);
                writer.write('"');
            }
            
            if (systemId != null) {
                writer.write(' ');
                writer.write('"');
                writer.write(systemId);
                writer.write('"');
            }
            
        }
        
        if (internalSubset != null) {
            writer.write(' ');
            writer.write('[');
            writer.write(internalSubset);
            writer.write(']');
        }
        
        writer.write('>');
        
    }

    

    @Override
    public void writeDocument(final IDocument document, final Writer writer) throws IOException {

        if (!document.hasChildren()) {
            return;
        }
        
        for (final INode child : document.getChildren()) {
            write(child, writer);
        }
        
    }


    
    @Override
    public void writeElement(final IElement element, final Writer writer) throws IOException {
        
        writer.write('<');
        writer.write(element.getElementName());
        
        if (element.hasAttributes()) {
            final Map<String,String> attributes = element.getAttributeMap();
            for (final Map.Entry<String,String> attributeEntry : attributes.entrySet()) {
                writer.write(' ');
                writer.write(attributeEntry.getKey());
                writer.write('=');
                writer.write('"');
                writer.write(attributeEntry.getValue());
                writer.write('"');
            }
        }

        if (!element.hasChildren()) {
            writer.write('/');
            writer.write('>');
            return;
        }
        
        writer.write('>');

        for (final INode child : element.getChildren()) {
            write(child, writer);
        }

        
        writer.write('<');
        writer.write('/');
        writer.write(element.getElementName());
        writer.write('>');
        
    }


    @Override
    public void writeProcessingInstruction(
            final IProcessingInstruction processingInstruction, final Writer writer)
            throws IOException {
        
        writer.write('<');
        writer.write('?');
        writer.write(processingInstruction.getTarget());
        
        final String content = processingInstruction.getContent();
        if (content != null) {
            writer.write(' ');
            writer.write(content);
        }
        
        writer.write('?');
        writer.write('>');
        
    }

    

    @Override
    public void writeText(final IText text, final Writer writer) throws IOException {

        validateNotNull(text, "Text node cannot be null");
        validateNotNull(writer, "Writer cannot be null");
        
        writer.write(text.getContent());
        
    }


    
    @Override
    public void writeXmlDeclaration(final IXmlDeclaration xmlDeclaration, final Writer writer) throws IOException {

        validateNotNull(xmlDeclaration, "XML declaration cannot be null");
        validateNotNull(writer, "Writer cannot be null");
        
        writer.write("<?xml version=\"");
        writer.write(xmlDeclaration.getVersion());
        writer.write('"');
        
        final String encoding = xmlDeclaration.getEncoding();
        if (encoding != null) {
            writer.write(" encoding=\"");
            writer.write(encoding);
            writer.write('"');
        }
        
        final String standalone = xmlDeclaration.getStandalone();
        if (standalone != null) {
            writer.write(" standalone=\"");
            writer.write(standalone);
            writer.write('"');
        }
        
        writer.write('?');
        writer.write('>');
        
    }




        
    

    
    
    
    private static void validateNotNull(final Object obj, final String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
    
}
