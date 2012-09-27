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
package org.attoparser.markup.duplicate;

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
public final class DuplicatingMarkupAttoHandler extends AbstractMarkupAttoHandler {

    
    private final Writer writer;
    
    
    public DuplicatingMarkupAttoHandler(final Writer writer) {
        super();
        this.writer = writer;
    }
    
    
    
    
    @Override
    public void startDocument()
            throws AttoParseException {
        // Nothing to be done here
    }

    
    
    @Override
    public void endDocument()
            throws AttoParseException {
        // Nothing to be done here
    }


    

    
    @Override
    public void docType(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            this.writer.write(buffer, outerOffset, outerLen);
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
            this.writer.write(buffer, outerOffset, outerLen);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }




    @Override
    public void openElement(
            final char[] buffer, 
            int contentOffset, int contentLen,
            int outerOffset, int outerLen, int line, int col)
            throws AttoParseException {
        
        try {
            this.writer.write(buffer, outerOffset, outerLen);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }




    @Override
    public void closeElement(char[] buffer, int contentOffset, int contentLen,
            int outerOffset, int outerLen, int line, int col)
            throws AttoParseException {
        
        try {
            this.writer.write(buffer, outerOffset, outerLen);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }




    
    
    @Override
    public void text(final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write(buffer, offset, len);
            
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
            
            this.writer.write(buffer, outerOffset, outerLen);
            
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
            
            this.writer.write(buffer, outerOffset, outerLen);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    
    
    @Override
    public void xmlDeclaration(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line,final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write(buffer, outerOffset, outerLen);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }





    @Override
    public void processingInstruction(
            final char[] buffer, 
            final int targetOffset, final int targetLen, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write(buffer, outerOffset, outerLen);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }


    
    
}