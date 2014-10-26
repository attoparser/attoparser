/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2014, The ATTOPARSER team (http://www.attoparser.org)
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
package org.attoparser.output;

import java.io.Writer;

import org.attoparser.AbstractMarkupHandler;
import org.attoparser.ParseException;


/**
 * <p>
 *   Implementation of {@link org.attoparser.IMarkupHandler} used for writing received parsing events as markup output.
 * </p>
 * <p>
 *   This handler allows writing markup without any loss of information (e.g. preserving case in keywords, attribute
 *   quoting, whitespaces, etc.) and can be used at the end of a handler chain in order to observe, in markup form,
 *   the results of the operations that might have been performed.
 * </p>
 * <p>
 *   This handler will ignore all auto* events, as they are synthetically generated events that did not appear
 *   at the original markup input.
 * </p>
 * <p>
 *   Note that, as with most handlers, this class is <strong>not thread-safe</strong>. Also, instances of this class
 *   should not be reused across parsing operations.
 * </p>
 * <p>
 *   Sample usage:
 * </p>
 * <pre><code>
 *   final Writer writer = new StringWriter();
 *   final IMarkupHandler handler = new OutputMarkupHandler(writer);
 *   parser.parse(document, handler);
 *   return writer.toString();
 * </code></pre>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public final class OutputMarkupHandler extends AbstractMarkupHandler {

    
    private final Writer writer;



    /**
     * <p>
     *   Creates a new instance of this handler.
     * </p>
     *
     * @param writer the writer to which output will be written.
     */
    public OutputMarkupHandler(final Writer writer) {
        super();
        if (writer == null) {
            throw new IllegalArgumentException("Writer cannot be null");
        }
        this.writer = writer;
    }

    
    



    @Override
    public void handleText(final char[] buffer, final int offset, final int len, final int line, final int col)
            throws ParseException {
        
        try {
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }



    @Override
    public void handleComment(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws ParseException {
        
        try {
            this.writer.write(buffer, outerOffset, outerLen);
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }

    
    @Override
    public void handleCDATASection(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {
        
        try {
            this.writer.write(buffer, outerOffset, outerLen);
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }




    @Override
    public void handleStandaloneElementStart(
            final char[] buffer, final int offset, final int len,
            final boolean minimized, final int line, final int col) throws ParseException {
        
        try {
            this.writer.write('<');
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }




    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer, final int offset, final int len,
            final boolean minimized, final int line, final int col) throws ParseException {
        
        try {
            if (minimized) {
                this.writer.write('/');
            }
            this.writer.write('>');
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }




    @Override
    public void handleOpenElementStart(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) throws ParseException {

        try {
            this.writer.write('<');
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }




    @Override
    public void handleOpenElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) throws ParseException {

        try {
            this.writer.write('>');
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }




    @Override
    public void handleAutoOpenElementStart(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) throws ParseException {
        // Nothing to be done... balanced elements were not present at the original template!
    }




    @Override
    public void handleAutoOpenElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) throws ParseException {
        // Nothing to be done... balanced elements were not present at the original template!
    }




    @Override
    public void handleCloseElementStart(final char[] buffer, final int offset, final int len, final int line,
            final int col) throws ParseException {
        
        try {
            this.writer.write("</");
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }




    @Override
    public void handleCloseElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) throws ParseException {
        
        try {
            this.writer.write('>');
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }




    @Override
    public void handleAutoCloseElementStart(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done... balanced elements were not present at the original template!
    }





    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        // Nothing to be done... balanced elements were not present at the original template!
    }




    @Override
    public void handleUnmatchedCloseElementStart(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        // They were present at the original template, so simply output them.
        handleCloseElementStart(buffer, offset, len, line, col);
    }




    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer, final int offset, final int len,
            final int line, final int col)
            throws ParseException {
        // They were present at the original template, so simply output them.
        handleCloseElementEnd(buffer, offset, len, line, col);
    }




    @Override
    public void handleAttribute(final char[] buffer, final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, final int valueContentOffset,
            final int valueContentLen, final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol) throws ParseException {
        
        try {
            this.writer.write(buffer, nameOffset, nameLen);
            this.writer.write(buffer, operatorOffset, operatorLen);
            this.writer.write(buffer, valueOuterOffset, valueOuterLen);
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }




    @Override
    public void handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws ParseException {
        
        try {
            this.writer.write(buffer, offset, len);
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }




    @Override
    public void handleDocType(
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
            final int outerLine, final int outerCol) throws ParseException {
        
        try {
            this.writer.write(buffer, outerOffset, outerLen);
        } catch (final Exception e) {
            throw new ParseException(e);
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
            throws ParseException {

        try {
            this.writer.write(buffer, outerOffset, outerLen);
        } catch (final Exception e) {
            throw new ParseException(e);
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
            throws ParseException {
        
        try {
            this.writer.write(buffer, outerOffset, outerLen);
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }


    
    
}