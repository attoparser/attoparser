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
 *   Common abstract class for visitors capable of traversing a attoDOM tree.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class WriterAttoDOMVisitor extends AbstractAttoDOMVisitor {

    
    private final Writer writer;
    
    
    
    public WriterAttoDOMVisitor(final Writer writer) {
        super();
        this.writer = writer;
    }
    
    
    public Writer getWriter() {
        return this.writer;
    }
    
    
    
    @Override
    public void visitXmlDeclaration(final XmlDeclaration xmlDeclaration)
            throws AttoDOMVisitorException {
        
        try {
            
            this.writer.write("<?xml version=\"");
            this.writer.write(xmlDeclaration.getVersion());
            this.writer.write('"');
            
            final String encoding = xmlDeclaration.getEncoding();
            if (encoding != null) {
                this.writer.write(" encoding=\"");
                this.writer.write(encoding);
                this.writer.write('"');
            }
            
            final String standalone = xmlDeclaration.getStandalone();
            if (standalone != null) {
                this.writer.write(" standalone=\"");
                this.writer.write(standalone);
                this.writer.write('"');
            }
            
            this.writer.write('?');
            this.writer.write('>');
            
        } catch (final IOException e) {
            throw new AttoDOMVisitorException("I/O Exception writing attoDOM tree", e);
        }
        
    }

    
    
    @Override
    public void visitDocType(final DocType docType)
            throws AttoDOMVisitorException {
        
        try {
            
            this.writer.write("<!DOCTYPE ");
            this.writer.write(docType.getRootElementName());
            
            final String publicId = docType.getPublicId();
            final String systemId = docType.getSystemId();
            final String internalSubset = docType.getInternalSubset();

            if (publicId != null || systemId != null) {
                
                final String type = (publicId == null? "SYSTEM" : "PUBLIC");
                
                this.writer.write(' ');
                this.writer.write(type);
                
                if (publicId != null) {
                    this.writer.write(' ');
                    this.writer.write('"');
                    this.writer.write(publicId);
                    this.writer.write('"');
                }
                
                if (systemId != null) {
                    this.writer.write(' ');
                    this.writer.write('"');
                    this.writer.write(systemId);
                    this.writer.write('"');
                }
                
            }
            
            if (internalSubset != null) {
                this.writer.write(' ');
                this.writer.write('[');
                this.writer.write(internalSubset);
                this.writer.write(']');
            }
            
            this.writer.write('>');
            
        } catch (final IOException e) {
            throw new AttoDOMVisitorException("I/O Exception writing attoDOM tree", e);
        }
        
    }

    

    
    
    @Override
    public void visitProcessingInstruction(final ProcessingInstruction processingInstruction)
            throws AttoDOMVisitorException {
        
        try {
            
            this.writer.write('<');
            this.writer.write('?');
            this.writer.write(processingInstruction.getTarget());
            
            final String content = processingInstruction.getContent();
            if (content != null) {
                this.writer.write(' ');
                this.writer.write(content);
            }
            
            this.writer.write('?');
            this.writer.write('>');
            
        } catch (final IOException e) {
            throw new AttoDOMVisitorException("I/O Exception writing attoDOM tree", e);
        }
        
    }

    
    
    
    @Override
    public void visitStandaloneElement(final Element element)
            throws AttoDOMVisitorException {
        
        try {
            
            this.writer.write('<');
            this.writer.write(element.getName());
            
            if (element.hasAttributes()) {
                final Map<String,String> attributes = element.getAttributeMap();
                for (final Map.Entry<String,String> attributeEntry : attributes.entrySet()) {
                    this.writer.write(' ');
                    this.writer.write(attributeEntry.getKey());
                    this.writer.write('=');
                    this.writer.write('"');
                    this.writer.write(attributeEntry.getValue());
                    this.writer.write('"');
                }
            }
            
            this.writer.write(' ');
            this.writer.write('/');
            this.writer.write('>');
            
        } catch (final IOException e) {
            throw new AttoDOMVisitorException("I/O Exception writing attoDOM tree", e);
        }
        
    }

    
    
    
    @Override
    public void visitOpenElement(final Element element)
            throws AttoDOMVisitorException {
        
        try {
            
            this.writer.write('<');
            this.writer.write(element.getName());
            
            if (element.hasAttributes()) {
                final Map<String,String> attributes = element.getAttributeMap();
                for (final Map.Entry<String,String> attributeEntry : attributes.entrySet()) {
                    this.writer.write(' ');
                    this.writer.write(attributeEntry.getKey());
                    this.writer.write('=');
                    this.writer.write('"');
                    this.writer.write(attributeEntry.getValue());
                    this.writer.write('"');
                }
            }
            
            this.writer.write('>');
            
        } catch (final IOException e) {
            throw new AttoDOMVisitorException("I/O Exception writing attoDOM tree", e);
        }
        
    }

    
    
    
    @Override
    public void visitCloseElement(final Element element)
            throws AttoDOMVisitorException {
        
        try {
            
            this.writer.write('<');
            this.writer.write('/');
            this.writer.write(element.getName());
            this.writer.write('>');
            
        } catch (final IOException e) {
            throw new AttoDOMVisitorException("I/O Exception writing attoDOM tree", e);
        }
        
    }

    
    
    
    @Override
    public void visitText(final Text text)
            throws AttoDOMVisitorException {
        
        try {
            
            this.writer.write(text.getContent());
            
        } catch (final IOException e) {
            throw new AttoDOMVisitorException("I/O Exception writing attoDOM tree", e);
        }
        
    }

    
    
    
    @Override
    public void visitCDATASection(final CDATASection cdataSection)
            throws AttoDOMVisitorException {
        
        try {
            
            this.writer.write("<![CDATA[");
            this.writer.write(cdataSection.getContent());
            this.writer.write("]]>");
            
        } catch (final IOException e) {
            throw new AttoDOMVisitorException("I/O Exception writing attoDOM tree", e);
        }
        
    }


    
    @Override
    public void visitComment(final Comment comment)
            throws AttoDOMVisitorException {
        
        try {
            
            this.writer.write("<!--");
            this.writer.write(comment.getContent());
            this.writer.write("-->");
            
        } catch (final IOException e) {
            throw new AttoDOMVisitorException("I/O Exception writing attoDOM tree", e);
        }
        
    }
    
}
