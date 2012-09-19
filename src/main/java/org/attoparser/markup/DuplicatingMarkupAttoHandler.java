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
package org.attoparser.markup;

import java.io.Writer;

import org.attoparser.AttoParseException;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class DuplicatingMarkupAttoHandler extends AbstractBreakDownMarkupAttoHandler {

    
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
    public void standaloneElementName(final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        
        try {
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }
    
    
    
    @Override
    public void openElementName(final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        
        try {
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }


    @Override
    public void closeElementName(final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        
        try {
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        
    }

    
    
    @Override
    public void elementAttribute(
            final char[] nameBuffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final char[] operatorBuffer,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final char[] valueBuffer,
            final int valueInnerOffset, final int valueInnerLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {

        
        try {
            
            this.writer.write(nameBuffer, nameOffset, nameLen);
            if (operatorLen > 0) {
                this.writer.write(operatorBuffer, operatorOffset, operatorLen);
                if (valueOuterLen > 0) {
                    this.writer.write(valueBuffer, valueOuterOffset, valueOuterLen);
                }
            }
            
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
            final int innerOffset, final int innerLen, 
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
            final int innerOffset, final int innerLen,
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