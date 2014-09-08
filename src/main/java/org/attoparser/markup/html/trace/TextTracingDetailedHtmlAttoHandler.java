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
package org.attoparser.markup.html.trace;

import java.io.IOException;
import java.io.Writer;

import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.markup.MarkupParsingConfiguration;
import org.attoparser.markup.html.AbstractDetailedNonValidatingHtmlAttoHandler;
import org.attoparser.markup.html.HtmlParsing;
import org.attoparser.markup.html.elements.IHtmlElement;







/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class TextTracingDetailedHtmlAttoHandler extends AbstractDetailedNonValidatingHtmlAttoHandler {

    
    private final Writer writer;
    
    
    public TextTracingDetailedHtmlAttoHandler(final Writer writer) {
        super(HtmlParsing.baseHtmlMarkupParsingConfiguration());
        this.writer = writer;
    }

    
    public TextTracingDetailedHtmlAttoHandler(final Writer writer, final MarkupParsingConfiguration configuration) {
        super(configuration);
        this.writer = writer;
    }
    
    
    
    
    
    
    @Override
    public IAttoHandleResult handleDocumentStart(final long startTimeNanos,
            final int line, final int col,
            final MarkupParsingConfiguration configuration)
            throws AttoParseException {
        try {
            this.writer.write('[');
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;
    }

    
    
    @Override
    public IAttoHandleResult handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos,
            final int line, final int col, 
            final MarkupParsingConfiguration configuration)
            throws AttoParseException {
        try {
            this.writer.write(']');
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }
        return null;
    }
    
    

    
    @Override
    public IAttoHandleResult handleHtmlStandaloneElementStart(
            final IHtmlElement element,
            final boolean minimized,
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write((minimized? 'M' : 'U'));
            this.writer.write('S');
            this.writer.write('E');
            this.writer.write('S');
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }

    
    @Override
    public IAttoHandleResult handleHtmlStandaloneElementEnd(
            final IHtmlElement element,
            final boolean minimized,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write((minimized? 'M' : 'U'));
            this.writer.write('S');
            this.writer.write('E');
            this.writer.write('E');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }

    
    
    
    @Override
    public IAttoHandleResult handleHtmlOpenElementStart(
            final IHtmlElement element,
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write('O');
            this.writer.write('E');
            this.writer.write('S');
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }

    
    @Override
    public IAttoHandleResult handleHtmlOpenElementEnd(
            final IHtmlElement element,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write('O');
            this.writer.write('E');
            this.writer.write('E');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }

    
    
    
    @Override
    public IAttoHandleResult handleHtmlCloseElementStart(
            final IHtmlElement element,
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        
        try {
            
            this.writer.write('C');
            this.writer.write('E');
            this.writer.write('S');
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }

    
    @Override
    public IAttoHandleResult handleHtmlCloseElementEnd(
            final IHtmlElement element,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write('C');
            this.writer.write('E');
            this.writer.write('E');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }


    @Override
    public IAttoHandleResult handleHtmlAutoCloseElementStart(
            final IHtmlElement element,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        try {

            this.writer.write('A');
            this.writer.write('C');
            this.writer.write('E');
            this.writer.write('S');
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);

        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }


    @Override
    public IAttoHandleResult handleHtmlAutoCloseElementEnd(
            final IHtmlElement element,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        try {

            this.writer.write('A');
            this.writer.write('C');
            this.writer.write('E');
            this.writer.write('E');
            writePosition(this.writer, line, col);

        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }


    @Override
    public IAttoHandleResult handleHtmlUnmatchedCloseElementStart(
            final IHtmlElement element,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        try {

            this.writer.write('U');
            this.writer.write('C');
            this.writer.write('E');
            this.writer.write('S');
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);

        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }


    @Override
    public IAttoHandleResult handleHtmlUnmatchedCloseElementEnd(
            final IHtmlElement element,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        try {

            this.writer.write('U');
            this.writer.write('C');
            this.writer.write('E');
            this.writer.write('E');
            writePosition(this.writer, line, col);

        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }


    @Override
    public IAttoHandleResult handleHtmlAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {
        
        try {
            
            this.writer.write('A');
            this.writer.write('(');
            this.writer.write(buffer, nameOffset, nameLen);
            this.writer.write(')');
            writePosition(this.writer, nameLine, nameCol);
            this.writer.write('(');
            this.writer.write(buffer, operatorOffset, operatorLen);
            this.writer.write(')');
            writePosition(this.writer, operatorLine, operatorCol);
            this.writer.write('(');
            this.writer.write(buffer, valueOuterOffset, valueOuterLen);
            this.writer.write(')');
            writePosition(this.writer, valueLine, valueCol);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }


    @Override
    public IAttoHandleResult handleHtmlInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        
        try {
            
            this.writer.write('A');
            this.writer.write('S');
            this.writer.write('(');
            this.writer.write(buffer, offset, len);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }


    

    
    
    @Override
    public IAttoHandleResult handleText(final char[] buffer, final int offset, final int len,
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
            
            this.writer.write('C');
            this.writer.write('(');
            this.writer.write(buffer, contentOffset, contentLen);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
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
            
            this.writer.write('D');
            this.writer.write('(');
            this.writer.write(buffer, contentOffset, contentLen);
            this.writer.write(')');
            writePosition(this.writer, line, col);
            
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
            
            this.writer.write("X");
            this.writer.write('(');
            this.writer.write(buffer, versionOffset, versionLen);
            this.writer.write(')');
            writePosition(this.writer, versionLine, versionCol);
            this.writer.write('(');
            if (encodingOffset != 0) {
                this.writer.write(buffer, encodingOffset, encodingLen);
            } else {
                this.writer.write("null");
            }
            this.writer.write(')');
            writePosition(this.writer, encodingLine, encodingCol);
            this.writer.write('(');
            if (standaloneOffset != 0) {
                this.writer.write(buffer, standaloneOffset, standaloneLen);
            } else {
                this.writer.write("null");
            }
            this.writer.write(')');
            writePosition(this.writer, standaloneLine, standaloneCol);
            
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
            final int outerLine, final int outerCol) 
            throws AttoParseException {
        
        try {
            
            this.writer.write('D');
            this.writer.write('T');
            this.writer.write('(');
            this.writer.write(buffer, keywordOffset, keywordLen);
            this.writer.write(')');
            writePosition(this.writer, keywordLine, keywordCol);
            this.writer.write('(');
            this.writer.write(buffer, elementNameOffset, elementNameLen);
            this.writer.write(')');
            writePosition(this.writer, elementNameLine, elementNameCol);
            this.writer.write('(');
            this.writer.write(buffer, typeOffset, typeLen);
            this.writer.write(')');
            writePosition(this.writer, typeLine, typeCol);
            this.writer.write('(');
            this.writer.write(buffer, publicIdOffset, publicIdLen);
            this.writer.write(')');
            writePosition(this.writer, publicIdLine, publicIdCol);
            this.writer.write('(');
            this.writer.write(buffer, systemIdOffset, systemIdLen);
            this.writer.write(')');
            writePosition(this.writer, systemIdLine, systemIdCol);
            this.writer.write('(');
            this.writer.write(buffer, internalSubsetOffset, internalSubsetLen);
            this.writer.write(')');
            writePosition(this.writer, internalSubsetLine, internalSubsetCol);
            
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
            
            this.writer.write("P");
            this.writer.write('(');
            this.writer.write(buffer, targetOffset, targetLen);
            this.writer.write(')');
            writePosition(this.writer, targetLine, targetCol);
            this.writer.write('(');
            if (contentOffset != 0) {
                this.writer.write(buffer, contentOffset, contentLen);
            } else {
                this.writer.write("null");
            }
            this.writer.write(')');
            writePosition(this.writer, contentLine, contentCol);
            
        } catch (final Exception e) {
            throw new AttoParseException(e);
        }

        return null;

    }

    
    
    
    
    
    
    
    
    
    private static void writePosition(final Writer writer, final int line, final int col) throws IOException {
        writer.write('{');
        writer.write(String.valueOf(line));
        writer.write(',');
        writer.write(String.valueOf(col));
        writer.write('}');
    }



    
    
}