package org.attoparser.content;

import java.io.IOException;
import java.io.Writer;

import org.attoparser.exception.AttoParseException;

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

    
    public void startElement(char[] elementName, int offset, int len,
            boolean isMinimized, int line, int pos)
            throws AttoParseException {
        
        try {
            
            this.writer.write('E');
            if (isMinimized) {
                this.writer.write('M');
            } else {
                this.writer.write('S');
            }
            this.writer.write('(');
            this.writer.write(elementName, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, pos);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    public void endElement(char[] elementName, int offset, int len,
            int line, int pos)
            throws AttoParseException {
        
        try {
            
            this.writer.write('E');
            this.writer.write('E');
            this.writer.write('(');
            this.writer.write(elementName, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, pos);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    public void attribute(
            char[] attributeName, int nameOffset, int nameLen, 
            char[] attributeValue, int valueOffset, int valueLen, 
            int line, int pos)
            throws AttoParseException {

        
        try {
            
            this.writer.write('A');
            this.writer.write('(');
            this.writer.write(attributeName, nameOffset, nameLen);
            this.writer.write('=');
            this.writer.write('"');
            this.writer.write(attributeValue, valueOffset, valueLen);
            this.writer.write('"');
            this.writer.write(')');
            writePosition(this.writer, line, pos);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    public void text(final char[] text, final int offset, final int len, final int line, final int pos)
            throws AttoParseException {
        
        try {
            
            this.writer.write('T');
            this.writer.write('(');
            this.writer.write(text, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, pos);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    public void comment(final char[] comment, final int offset, final int len, final int line, final int pos)
            throws AttoParseException {
        
        try {
            
            this.writer.write('C');
            this.writer.write('(');
            this.writer.write(comment, offset, len);
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