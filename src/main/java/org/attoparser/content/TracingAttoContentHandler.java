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
package org.attoparser.content;

import java.io.IOException;
import java.io.Writer;

import org.attoparser.exception.AttoParseException;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class TracingAttoContentHandler implements IAttoContentHandler {

    
    private final Writer writer;
    
    
    public TracingAttoContentHandler(final Writer writer) {
        super();
        this.writer = writer;
    }
    
    
    
    public void startDocument()
            throws AttoParseException {
        try {
            this.writer.write('[');
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
    }

    
    public void endDocument()
            throws AttoParseException {
        try {
            this.writer.write(']');
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
    }

    
    public void startElement(char[] buffer, int offset, int len,
            boolean hasBody, int line, int pos)
            throws AttoParseException {
        
        try {
            
            this.writer.write('E');
            if (hasBody) {
                this.writer.write('S');
            }
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, pos);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    public void endElement(char[] buffer, int offset, int len,
            int line, int pos)
            throws AttoParseException {
        
        try {
            
            this.writer.write('E');
            this.writer.write('E');
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, pos);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    public void attribute(
            char[] nameBuffer, int nameOffset, int nameLen, 
            char[] valueBuffer, int valueOffset, int valueLen, 
            int line, int pos)
            throws AttoParseException {

        
        try {
            
            this.writer.write('A');
            this.writer.write('(');
            this.writer.write(nameBuffer, nameOffset, nameLen);
            this.writer.write('=');
            this.writer.write('"');
            this.writer.write(valueBuffer, valueOffset, valueLen);
            this.writer.write('"');
            this.writer.write(')');
            writePosition(this.writer, line, pos);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    public void text(final char[] buffer, final int offset, final int len, final int line, final int pos)
            throws AttoParseException {
        
        try {
            
            this.writer.write('T');
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, pos);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    public void comment(final char[] buffer, final int offset, final int len, final int line, final int pos)
            throws AttoParseException {
        
        try {
            
            this.writer.write('C');
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, pos);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    public void cdata(final char[] cdata, final int offset, final int len, final int line, final int pos)
            throws AttoParseException {
        
        try {
            
            this.writer.write('D');
            this.writer.write('(');
            this.writer.write(cdata, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, pos);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    
    private static void writePosition(final Writer writer, final int line, final int pos) throws IOException {
        writer.write('{');
        writer.write(String.valueOf(line));
        writer.write(',');
        writer.write(String.valueOf(pos));
        writer.write('}');
    }
    
    
}