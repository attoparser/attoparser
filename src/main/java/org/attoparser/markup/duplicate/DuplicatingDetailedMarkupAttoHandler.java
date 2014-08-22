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
import org.attoparser.IAttoHandleResult;
import org.attoparser.markup.AbstractDetailedMarkupAttoHandler;
import org.attoparser.markup.MarkupParsingConfiguration;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class DuplicatingDetailedMarkupAttoHandler extends AbstractDetailedMarkupAttoHandler {

    
    private final Writer writer;
    

    
    public DuplicatingDetailedMarkupAttoHandler(final Writer writer) {
        super();
        this.writer = writer;
    }
    
    public DuplicatingDetailedMarkupAttoHandler(final Writer writer, final MarkupParsingConfiguration markupParsingConfiguration) {
        super(markupParsingConfiguration);
        this.writer = writer;
    }
    
    
    
    
    @Override
    public IAttoHandleResult handleDocumentStart(final long startTimeNanos,
            final int line, final int col,
            final MarkupParsingConfiguration markupParsingConfiguration)
            throws AttoParseException {
        // Nothing to be done here
        return null;
    }

    
    
    @Override
    public IAttoHandleResult handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos,
            final int line, final int col,
            final MarkupParsingConfiguration markupParsingConfiguration)
            throws AttoParseException {
        // Nothing to be done here
        return null;
    }






    @Override
    public IAttoHandleResult handleText(final char[] buffer, final int offset, final int len, final int line, final int col)
            throws AttoParseException {
        
        try {
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;

    }
    

    
    @Override
    public IAttoHandleResult handleComment(
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
    public IAttoHandleResult handleCDATASection(
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
    public IAttoHandleResult handleStandaloneElementStart(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) throws AttoParseException {
        
        try {
            this.writer.write('<');
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;

    }




    @Override
    public IAttoHandleResult handleStandaloneElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) throws AttoParseException {
        
        try {
            this.writer.write("/>");
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;

    }




    @Override
    public IAttoHandleResult handleOpenElementStart(final char[] buffer, final int offset, final int len, final int line,
            final int col) throws AttoParseException {
        
        try {
            this.writer.write('<');
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;

    }




    @Override
    public IAttoHandleResult handleOpenElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) throws AttoParseException {
        
        try {
            this.writer.write('>');
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;

    }




    @Override
    public IAttoHandleResult handleCloseElementStart(final char[] buffer, final int offset, final int len, final int line,
            final int col) throws AttoParseException {
        
        try {
            this.writer.write("</");
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;

    }




    @Override
    public IAttoHandleResult handleCloseElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) throws AttoParseException {
        
        try {
            this.writer.write('>');
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;

    }




    @Override
    public IAttoHandleResult handleAutoCloseElementStart(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done... balanced elements were not present at the original template!
        return null;
    }





    @Override
    public IAttoHandleResult handleAutoCloseElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done... balanced elements were not present at the original template!
        return null;
    }




    @Override
    public IAttoHandleResult handleUnmatchedCloseElementStart(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // They were present at the original template, so simply output them.
        return handleCloseElementStart(buffer, offset, len, line, col);
    }




    @Override
    public IAttoHandleResult handleUnmatchedCloseElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // They were present at the original template, so simply output them.
        return handleCloseElementEnd(buffer, offset, len, line, col);
    }




    @Override
    public IAttoHandleResult handleAttribute(final char[] buffer, final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, final int valueContentOffset,
            final int valueContentLen, final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol) throws AttoParseException {
        
        try {
            this.writer.write(buffer, nameOffset, nameLen);
            this.writer.write(buffer, operatorOffset, operatorLen);
            this.writer.write(buffer, valueOuterOffset, valueOuterLen);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;

    }




    @Override
    public IAttoHandleResult handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;

    }




    @Override
    public IAttoHandleResult handleDocType(
            final char[] buffer, 
            final int keywordOffset, final int keywordLen,
            final int keywordLine, final int keywordCol, 
            final int elementNameOffset, final int elementNameLen, 
            final int elementNameLine, final int elementNameCol,
            final int typeOffset, final int typeLen, 
            final int typeLine, final int typeCol,
            final int publicIdOffset, final int publicIdLen, 
            final int publicIdLine, final int publicIdCol, 
            final int systemIdOffset, final int systemIdLen,
            final int systemIdLine, final int systemIdCol, 
            final int internalSubsetOffset, final int internalSubsetLen,
            final int internalSubsetLine, final int internalSubsetCol,
            final int outerOffset, final int outerLen,
            final int outerLine, final int outerCol) throws AttoParseException {
        
        try {
            this.writer.write(buffer, outerOffset, outerLen);
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;

    }

    
    
    
    @Override
    public IAttoHandleResult handleXmlDeclarationDetail(
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

            final int outerContentEnd = (outerOffset  + outerLen) - 2;
            
            this.writer.write('<');
            this.writer.write('?');
            this.writer.write(buffer, keywordOffset, keywordLen);

            /*
             * VERSION (required) 
             */
            int lastStructureEnd = keywordOffset + keywordLen;
            int thisStructureOffset = versionOffset;
            int thisStructureLen = versionLen;
            int thisStructureEnd = thisStructureOffset + thisStructureLen;
            
            this.writer.write(buffer, lastStructureEnd, thisStructureOffset - lastStructureEnd);
            this.writer.write(buffer, thisStructureOffset, thisStructureLen);

            /*
             * ENCODING (optional)
             */
            if (encodingLen > 0)  {
                
                lastStructureEnd = thisStructureEnd;
                thisStructureOffset = encodingOffset;
                thisStructureLen = encodingLen;
                thisStructureEnd = thisStructureOffset + thisStructureLen;
            
                this.writer.write(buffer, lastStructureEnd, thisStructureOffset - lastStructureEnd);
                this.writer.write(buffer, thisStructureOffset, thisStructureLen);

            }

            /*
             * STANDALONE (optional)
             */
            
            if (standaloneLen > 0) {
                
                lastStructureEnd = thisStructureEnd;
                thisStructureOffset = standaloneOffset;
                thisStructureLen = standaloneLen;
                thisStructureEnd = thisStructureOffset + thisStructureLen;
            
                this.writer.write(buffer, lastStructureEnd, thisStructureOffset - lastStructureEnd);
                this.writer.write(buffer, thisStructureOffset, thisStructureLen);
                
            }
            
            this.writer.write(buffer, thisStructureEnd, (outerContentEnd - thisStructureEnd));
            
            this.writer.write('?');
            this.writer.write('>');
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }






    @Override
    public IAttoHandleResult handleProcessingInstruction(
            final char[] buffer, 
            final int targetOffset, final int targetLen, 
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        try {

            this.writer.write('<');
            this.writer.write('?');
            this.writer.write(buffer, targetOffset, targetLen);
            if (contentLen > 0)  {
                this.writer.write(buffer, (targetOffset + targetLen), contentOffset - (targetOffset + targetLen));
                this.writer.write(buffer, contentOffset, contentLen);
            } else {
                this.writer.write(buffer, (targetOffset + targetLen), ((outerOffset  + outerLen) - 2) - (targetOffset + targetLen));
            }
            this.writer.write('?');
            this.writer.write('>');
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }


    
    
}