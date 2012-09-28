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

import org.attoparser.AttoParseException;
import org.attoparser.markup.AbstractMarkupAttoHandler;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class TracingMarkupAttoHandler extends AbstractMarkupAttoHandler {

    
    private final Writer writer;
    
    
    public TracingMarkupAttoHandler(final Writer writer) {
        super();
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
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write('S');
            this.writer.write('E');
            this.writer.write('S');
            this.writer.write('(');
            this.writer.write(buffer, contentOffset, contentLen);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }
    
    

    
    @Override
    public void openElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write('O');
            this.writer.write('E');
            this.writer.write('S');
            this.writer.write('(');
            this.writer.write(buffer, contentOffset, contentLen);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }
    
    

    
    @Override
    public void closeElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write('C');
            this.writer.write('E');
            this.writer.write('S');
            this.writer.write('(');
            this.writer.write(buffer, contentOffset, contentLen);
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
    public void comment(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write('C');
            this.writer.write('(');
            this.writer.write(buffer, contentOffset, contentLen);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    @Override
    public void cdata(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write('D');
            this.writer.write('(');
            this.writer.write(buffer, contentOffset, contentLen);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    
    
    @Override
    public void xmlDeclaration(
            final char[] buffer, 
            final int keywordOffset, final int keywordLen,
            final int keywordLine, final int keywordCol,
            final int versionOffset, final int versionLen,
            final int versionLine, final int versionCol,
            final int encodingOffset, final int encodingLen,
            final int encodingLine, final int encodingCol,
            final int standaloneOffset, final int standaloneLen,
            final int standaloneLine, final int standaloneCol,
            final int outerOffset, final int outerLen,
            final int line,final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write("X");
            this.writer.write('(');
            this.writer.write(buffer, versionOffset, versionLen);
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write(buffer, encodingOffset, encodingLen);
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write(buffer, standaloneOffset, standaloneLen);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }





    @Override
    public void processingInstruction(
            final char[] buffer, 
            final int targetOffset, final int targetLen, 
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write("P");
            this.writer.write('(');
            this.writer.write(buffer, targetOffset, targetLen);
            this.writer.write(')');
            this.writer.write('(');
            this.writer.write(buffer, contentOffset, contentLen);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    
    




    @Override
    public void docType(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write('D');
            this.writer.write('T');
            this.writer.write('(');
            this.writer.write(buffer, contentOffset, contentLen);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }




    private static void writePosition(final Writer writer, final int line, final int col) throws IOException {
        writer.write('{');
        writer.write(String.valueOf(line));
        writer.write(',');
        writer.write(String.valueOf(col));
        writer.write('}');
    }




    
    
}