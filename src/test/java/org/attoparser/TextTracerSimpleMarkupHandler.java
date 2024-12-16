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
package org.attoparser;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.attoparser.simple.AbstractSimpleMarkupHandler;


/*
 *
 * @author Daniel Fernandez
 * @since 2.0.0
 */
public final class TextTracerSimpleMarkupHandler extends AbstractSimpleMarkupHandler {

    
    private final Writer writer;
    

    
    public TextTracerSimpleMarkupHandler(final Writer writer) {
        super();
        this.writer = writer;
    }

    
    
    
    @Override
    public void handleDocumentStart(final long startTimeNanos,
            final int line, final int col)
            throws ParseException {
        
        try {
            this.writer.write('[');
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }

    
    
    @Override
    public void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos,
            final int line, final int col)
            throws ParseException {
        
        try {
            this.writer.write(']');
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }
    
    

    @Override
    public void handleStandaloneElement(
            final String elementName, final Map<String, String> attributes,
            final boolean minimized,
            final int line, final int col) 
            throws ParseException {
        
        try {
            
            this.writer.write("SE");
            this.writer.write('(');
            this.writer.write(elementName);
            writeAttributes(this.writer, attributes);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }
    
    

    @Override
    public void handleOpenElement(
            final String elementName, final Map<String, String> attributes, 
            final int line, final int col) 
            throws ParseException {
        
        try {
            
            this.writer.write("OE");
            this.writer.write('(');
            this.writer.write(elementName);
            writeAttributes(this.writer, attributes);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }



    @Override
    public void handleCloseElement(final String elementName, final int line, final int col)
            throws ParseException {
        
        try {
            
            this.writer.write("CE");
            this.writer.write('(');
            this.writer.write(elementName);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }



    @Override
    public void handleAutoCloseElement(final String elementName, final int line, final int col)
            throws ParseException {
        
        try {
            
            this.writer.write("ACE");
            this.writer.write('(');
            this.writer.write(elementName);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }



    @Override
    public void handleUnmatchedCloseElement(final String elementName, final int line, final int col)
            throws ParseException {
        
        try {
            
            this.writer.write("UCE");
            this.writer.write('(');
            this.writer.write(elementName);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }



    @Override
    public void handleDocType(
            final String elementName, 
            final String publicId, 
            final String systemId, 
            final String internalSubset,
            final int line, final int col) 
            throws ParseException {
        
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
            throw new ParseException(e);
        }

    }



    @Override
    public void handleComment(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws ParseException {
        
        try {
            
            this.writer.write("C");
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }



    @Override
    public void handleCDATASection(final char[] buffer,
            final int offset, final int len, final int line, final int col) 
            throws ParseException {
        
        try {
            
            this.writer.write("CD");
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }
    
    

    
    
    public void handleText(final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        
        try {
            
            this.writer.write('T');
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }

    
    
    
    @Override
    public void handleXmlDeclaration(
            final String version, 
            final String encoding,
            final String standalone,
            final int line,final int col) 
            throws ParseException {
        
        try {
            
            this.writer.write("XD");
            this.writer.write('(');
            this.writer.write((version == null? "" : version));
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write((encoding == null? "" : encoding));
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write((standalone == null? "" : standalone));
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }




    @Override
    public void handleProcessingInstruction(
            final String target, final String content, 
            final int line, final int col) 
            throws ParseException {
        
        try {
            
            this.writer.write("P");
            this.writer.write('(');
            this.writer.write(target);
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write((content == null? "" : content));
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
            
        } catch (final Exception e) {
            throw new ParseException(e);
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