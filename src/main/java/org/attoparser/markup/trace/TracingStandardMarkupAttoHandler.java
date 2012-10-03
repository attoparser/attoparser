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
package org.attoparser.markup.trace;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.attoparser.AttoParseException;
import org.attoparser.markup.AbstractStandardMarkupAttoHandler;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class TracingStandardMarkupAttoHandler extends AbstractStandardMarkupAttoHandler {

    
    private final Writer writer;
    

    
    public TracingStandardMarkupAttoHandler(final Writer writer) {
        this(writer, false);
    }
    
    public TracingStandardMarkupAttoHandler(final Writer writer, final boolean wellFormed) {
        super(wellFormed);
        this.writer = writer;
    }
    
    
    
    
    @Override
    public void startDocument()
            throws AttoParseException {
        
        try {
            this.writer.write('[');
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    
    @Override
    public void endDocument()
            throws AttoParseException {
        
        try {
            this.writer.write(']');
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }
    
    

    @Override
    public void standaloneElement(
            final String elementName, final Map<String, String> attributes, 
            final int line, final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write("SE");
            this.writer.write('(');
            this.writer.write(elementName);
            writeAttributes(this.writer, attributes);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }
    
    

    @Override
    public void openElement(
            final String elementName, final Map<String, String> attributes, 
            final int line, final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write("OE");
            this.writer.write('(');
            this.writer.write(elementName);
            writeAttributes(this.writer, attributes);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }



    @Override
    public void closeElement(final String elementName, final int line, final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write("CE");
            this.writer.write('(');
            this.writer.write(elementName);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }



    @Override
    public void docType(
            final String elementName, 
            final String publicId, 
            final String systemId, 
            final String internalSubset,
            final int line, final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write("DT");
            this.writer.write('(');
            this.writer.write(elementName);
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write((publicId == null? "" : publicId));
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write((systemId == null? "" : systemId));
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write((internalSubset == null? "" : internalSubset));
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }



    @Override
    public void comment(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write("C");
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }



    @Override
    public void cdata(final char[] buffer, 
            final int offset, final int len, final int line, final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write("D");
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }
    
    

    
    
    @Override
    public void text(final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write('T');
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    
    
    @Override
    public void xmlDeclaration(
            final String version, 
            final String encoding,
            final String standalone,
            final int line,final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write("X");
            this.writer.write('(');
            this.writer.write(version);
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write(encoding);
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write(standalone);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }




    @Override
    public void processingInstruction(
            final String target, final String content, 
            final int line, final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write("P");
            this.writer.write('(');
            this.writer.write(target);
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write(content);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }



    
    

    private static void writeAttributes(final Writer writer, final Map<String,String> attributes) throws IOException {
        if (attributes == null || attributes.size() == 0) {
            return;
        }
        writer.write('[');
        boolean first = true;
        for (final Map.Entry<String,String> entry : attributes.entrySet()) {
            if (!first) {
                writer.write(',');
            }
            writer.write(entry.getKey());
            writer.write('=');
            writer.write('\'');
            writer.write(entry.getValue());
            writer.write('\'');
            first = false;
        }
        writer.write(']');
    }



    private static void writePosition(final Writer writer, final int line, final int col) throws IOException {
        writer.write('{');
        writer.write(String.valueOf(line));
        writer.write(',');
        writer.write(String.valueOf(col));
        writer.write('}');
    }




    
    
}