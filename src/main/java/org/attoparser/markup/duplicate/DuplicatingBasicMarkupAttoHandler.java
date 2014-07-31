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
import org.attoparser.markup.AbstractBasicMarkupAttoHandler;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class DuplicatingBasicMarkupAttoHandler extends AbstractBasicMarkupAttoHandler {

    
    private final Writer writer;
    
    
    public DuplicatingBasicMarkupAttoHandler(final Writer writer) {
        super();
        this.writer = writer;
    }
    
    
    
    
    @Override
    public void handleDocumentStart(final long startTimeNanos, final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here
    }

    
    
    @Override
    public void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here
    }


    

    
    @Override
    public void handleDocType(
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
    public char[] handleStandaloneElement(
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

        return null;

    }




    @Override
    public char[] handleOpenElement(
            final char[] buffer, 
            int contentOffset, int contentLen,
            int outerOffset, int outerLen, int line, int col)
            throws AttoParseException {
        
        try {
            this.writer.write(buffer, outerOffset, outerLen);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }




    @Override
    public char[] handleCloseElement(char[] buffer, int contentOffset, int contentLen,
            int outerOffset, int outerLen, int line, int col)
            throws AttoParseException {
        
        try {
            this.writer.write(buffer, outerOffset, outerLen);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }




    
    
    @Override
    public void handleText(final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write(buffer, offset, len);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }


    
    @Override
    public void handleComment(
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
    public void handleCDATASection(
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
    public void handleXmlDeclaration(
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
            
            this.writer.write(buffer, outerOffset, outerLen);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }





    @Override
    public void handleProcessingInstruction(
            final char[] buffer, 
            final int targetOffset, final int targetLen, 
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
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