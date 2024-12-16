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

import java.io.IOException;
import java.io.Writer;
import java.util.Map;


/**
 * <p>
 *   Static utility class able to write a DOM tree (or a fragment of it) as markup.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public final class DOMWriter {

    



    public static void write(final INode node, final Writer writer) throws IOException {

        if (node == null) {
            return;
        }

        if (node instanceof Text) {
            writeText((Text)node, writer);
            return;
        }
        if (node instanceof Element) {
            writeElement((Element)node, writer);
            return;
        }
        if (node instanceof Comment) {
            writeComment((Comment)node, writer);
            return;
        }
        if (node instanceof CDATASection) {
            writeCDATASection((CDATASection)node, writer);
            return;
        }
        if (node instanceof DocType) {
            writeDocType((DocType)node, writer);
            return;
        }
        if (node instanceof Document) {
            writeDocument((Document)node, writer);
            return;
        }
        if (node instanceof XmlDeclaration) {
            writeXmlDeclaration((XmlDeclaration)node, writer);
            return;
        }
        if (node instanceof ProcessingInstruction) {
            writeProcessingInstruction((ProcessingInstruction)node, writer);
            return;
        }

    }

    
    
    public static void writeCDATASection(final CDATASection cdataSection, final Writer writer)
            throws IOException{
        
        writer.write("<![CDATA[");
        writer.write(cdataSection.getContent());
        writer.write("]]>");
        
    }

    

    public static void writeComment(final Comment comment, final Writer writer) throws IOException {
        
        writer.write("<!--");
        writer.write(comment.getContent());
        writer.write("-->");
        
    }

    

    public static void writeDocType(final DocType docType, final Writer writer) throws IOException {
        
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

    

    public static void writeDocument(final Document document, final Writer writer) throws IOException {

        if (!document.hasChildren()) {
            return;
        }
        
        for (final INode child : document.getChildren()) {
            write(child, writer);
        }
        
    }


    
    public static void writeElement(final Element element, final Writer writer) throws IOException {
        
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


    public static void writeProcessingInstruction(
            final ProcessingInstruction processingInstruction, final Writer writer)
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

    

    public static void writeText(final Text text, final Writer writer) throws IOException {

        validateNotNull(text, "Text node cannot be null");
        validateNotNull(writer, "Writer cannot be null");
        
        writer.write(text.getContent());
        
    }


    
    public static void writeXmlDeclaration(final XmlDeclaration xmlDeclaration, final Writer writer) throws IOException {

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



    private DOMWriter() {
        super();
    }


}
