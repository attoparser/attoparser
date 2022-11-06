/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2022, The ATTOPARSER team (https://www.attoparser.org)
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

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import org.attoparser.config.ParseConfiguration;
import org.attoparser.select.ParseSelection;


/**
 * <p>
 *   Default implementation of the {@link IMarkupParser} interface.
 * </p>
 * <p>
 *   <i>AttoParser</i> markup parsers work as SAX-style parsers that need
 *   a <i>markup handler</i> object for handling parsing events. These handlers implement
 *   the {@link org.attoparser.IMarkupHandler} interface, and are normally developed by
 *   users in order to perform the operations they require for their applications.
 * </p>
 * <p>
 *   See the documentation of the {@link org.attoparser.IMarkupHandler} interface for more
 *   information on the event handler methods, and also on the handler implementations
 *   AttoParser provides out-of-the-box.
 * </p>
 * <p>
 *   Also, note there are two different specialized parsers that use
 *   {@link org.attoparser.MarkupParser} underneath, but which are oriented towards allowing
 *   an easy use of specific parsing features: {@link org.attoparser.dom.IDOMMarkupParser} for
 *   DOM-oriented parsing and {@link org.attoparser.simple.ISimpleMarkupParser} for using
 *   a simplified version of the handler interface ({@link org.attoparser.simple.ISimpleMarkupHandler}).
 * </p>
 * <p>
 *   Sample usage:
 * </p>
 * <pre><code>
 *   // Obtain a java.io.Reader on the document to be parsed
 *   final Reader documentReader = ...;
 *
 *   // Create the handler instance. Extending the no-op AbstractMarkupHandler is a good start
 *   final IMarkupHandler handler = new AbstractMarkupHandler() {
 *       ... // some events implemented
 *   };
 *
 *   // Create or obtain the parser instance (can be reused). Example uses the default configuration for HTML
 *   final IMarkupParser parser = new MarkupParser(ParseConfiguration.htmlConfiguration());
 *
 *   // Parse it!
 *   parser.parse(documentReader, handler);
 * </code></pre>
 * <p>
 *   This parser class is <b>thread-safe</b>. However, take into account that, normally,
 *   {@link IMarkupHandler} implementations are not. So, even if parsers can be reused, handler objects
 *   usually cannot.
 * </p>
 * <p>
 *   This parser class uses a (configurable) pool of <tt>char[]</tt> buffers, in order to reduce the amount of
 *   memory used for parsing (buffers are large structures). This pool works in a non-blocking mode,
 *   so if a new buffer is needed and all are currently allocated, a new (unpooled) <tt>char[]</tt> object
 *   is created and returned without waiting for a pooled buffer to be available.
 * </p>
 * <p>
 *   <em>(Note that these pooled buffers will not be used when parsing documents specified as <tt>char[]</tt>
 *   objects. In such case, the <tt>char[]</tt> documents themselves will be used as buffers, avoiding the need
 *   to allocate pooled buffers or use any additional amount of memory.)</em>
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public final class MarkupParser implements IMarkupParser {

    /**
     * <p>
     *   Default buffer size to be used (buffer size will grow at runtime if
     *   an artifact (structure or text) is bigger than the whole buffer).
     *   Value: 4096 chars (= 8192 bytes).
     * </p>
     */
    public static final int DEFAULT_BUFFER_SIZE = 4096;

    /**
     * <p>
     *   Default pool size to be used. Buffers will be kept in a pool and
     *   reused in order to increase performance. Pool will be non-exclusive
     *   so that if pool size = 2 and a 3rd request arrives, it is assigned
     *   a new buffer object (not linked to the pool, and therefore GC-ed
     *   at the end). Value: 2.
     * </p>
     */
    public static final int DEFAULT_POOL_SIZE = 2;


    private final ParseConfiguration configuration;
    private final BufferPool pool;




    /**
     * <p>
     *   Creates a new instance of this parser, using the specified configuration and default
     *   sizes for pool ({@link #DEFAULT_POOL_SIZE}) and pooled buffers ({@link #DEFAULT_BUFFER_SIZE}).
     * </p>
     *
     * @param configuration the parsing configuration to be used.
     */
    public MarkupParser(final ParseConfiguration configuration) {
        this(configuration, DEFAULT_POOL_SIZE, DEFAULT_BUFFER_SIZE);
    }


    /**
     * <p>
     *   Creates a new instance of this parser, specifying the pool and buffer size.
     * </p>
     * <p>
     *   Buffer size (in chars) will be the size of the <tt>char[]</tt> structures used as buffers for parsing,
     *   which might grow if a certain markup structure does not fit inside (e.g. a text). Default size is
     *   {@link MarkupParser#DEFAULT_BUFFER_SIZE}.
     * </p>
     * <p>
     *   Pool size is the size of the pool of <tt>char[]</tt> buffers that will be kept in memory in order to
     *   allow their reuse. This pool works in a non-exclusive mode, so that if pool size is 3 and a 4th request
     *   arrives, it is served a new non-pooled buffer without the need to block waiting for one of the pooled
     *   instances. Default size is {@link MarkupParser#DEFAULT_POOL_SIZE}.
     * </p>
     * <p>
     *   Note that these pooled buffers will not be used when parsing documents specified as <tt>char[]</tt>
     *   objects. In such case, the <tt>char[]</tt> documents themselves will be used as buffers, avoiding the need
     *   to allocate buffers or use any additional amount of memory.
     * </p>
     *
     * @param configuration the parsing configuration to be used.
     * @param poolSize the size of the pool of buffers to be used.
     * @param bufferSize the default size of the buffers to be instanced for this parser.
     */
    public MarkupParser(final ParseConfiguration configuration, final int poolSize, final int bufferSize) {
        super();
        this.configuration = configuration;
        this.pool = new BufferPool(poolSize, bufferSize);
    }






    public void parse(final String document, final IMarkupHandler handler)
            throws ParseException {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        parse(new StringReader(document), handler);
    }


    public void parse(final char[] document, final IMarkupHandler handler)
            throws ParseException {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        parse(document, 0, document.length, handler);
    }


    public void parse(
            final char[] document, final int offset, final int len, final IMarkupHandler handler)
            throws ParseException {

        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        if (offset < 0 || len < 0) {
            throw new IllegalArgumentException(
                    "Neither document offset (" + offset + ") nor document length (" +
                            len + ") can be less than zero");
        }

        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        IMarkupHandler markupHandler =
                (ParseConfiguration.ParsingMode.HTML.equals(this.configuration.getMode()) ?
                        new HtmlMarkupHandler(handler) : handler);

        // We will not report directly to the specified handler, but instead to an intermediate class that will be in
        // charge of applying the required markup logic and rules, according to the specified configuration
        markupHandler = new MarkupEventProcessorHandler(markupHandler);

        markupHandler.setParseConfiguration(this.configuration);

        final ParseStatus status = new ParseStatus();
        markupHandler.setParseStatus(status);

        final ParseSelection selection = new ParseSelection();
        markupHandler.setParseSelection(selection);

        // We already have a suitable char[] buffer, so there is no need to use one from the pool.
        parseDocument(document, offset, len, markupHandler, status);

    }



    public void parse(
            final Reader reader, final IMarkupHandler handler)
            throws ParseException {

        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        }

        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        IMarkupHandler markupHandler =
                (ParseConfiguration.ParsingMode.HTML.equals(this.configuration.getMode()) ?
                        new HtmlMarkupHandler(handler) : handler);

        // We will not report directly to the specified handler, but instead to an intermediate class that will be in
        // charge of applying the required markup logic and rules, according to the specified configuration
        markupHandler = new MarkupEventProcessorHandler(markupHandler);

        markupHandler.setParseConfiguration(this.configuration);

        final ParseStatus status = new ParseStatus();
        markupHandler.setParseStatus(status);

        final ParseSelection selection = new ParseSelection();
        markupHandler.setParseSelection(selection);

        // We don't already have a suitable char[] buffer, so we expect the parser to use one of its pooled buffers.
        parseDocument(reader, this.pool.poolBufferSize, markupHandler, status);

    }





    /*
     * This method receiving the buffer size with package visibility allows
     * testing different buffer sizes.
     */
    void parseDocument(
            final Reader reader, final int suggestedBufferSize,
            final IMarkupHandler handler, final ParseStatus status)
            throws ParseException {


        final long parsingStartTimeNanos = System.nanoTime();

        char[] buffer = null;

        try {

            handler.handleDocumentStart(parsingStartTimeNanos, 1, 1);

            int bufferSize = suggestedBufferSize;
            buffer = this.pool.allocateBuffer(bufferSize);

            int bufferContentSize = reader.read(buffer);

            boolean cont = (bufferContentSize != -1);

            status.offset = -1;
            status.line = 1;
            status.col = 1;
            status.inStructure = false;
            status.parsingDisabled = true;
            status.parsingDisabledLimitSequence = null;
            status.autoCloseRequired = null;
            status.autoCloseLimits = null;

            while (cont) {

                parseBuffer(buffer, 0, bufferContentSize, handler, status);

                int readOffset = 0;
                int readLen = bufferSize;

                if (status.offset == 0) {

                    if (bufferContentSize == bufferSize) {
                        // Buffer is not big enough, double it!

                        char[] newBuffer = null;
                        try {

                            bufferSize *= 2;

                            newBuffer = this.pool.allocateBuffer(bufferSize);
                            System.arraycopy(buffer, 0, newBuffer, 0, bufferContentSize);

                            this.pool.releaseBuffer(buffer);

                            buffer = newBuffer;

                        } catch (final Exception ignored) {
                            this.pool.releaseBuffer(newBuffer);
                        }

                    }

                    // it's possible for two reads to occur in a row and 1) read less than the bufferSize and 2)
                    // still not find the next tag/end of structure
                    readOffset = bufferContentSize;
                    readLen = bufferSize - readOffset;

                } else if (status.offset < bufferContentSize) {

                    System.arraycopy(buffer, status.offset, buffer, 0, bufferContentSize - status.offset);

                    readOffset = bufferContentSize - status.offset;
                    readLen = bufferSize - readOffset;

                    status.offset = 0;
                    bufferContentSize = readOffset;

                }

                final int read = reader.read(buffer, readOffset, readLen);
                if (read != -1) {
                    bufferContentSize = readOffset + read;
                } else {
                    cont = false;
                }

            }

            // Iteration done, now it's time to clean up in case we still have some text to be notified

            int lastLine = status.line;
            int lastCol = status.col;

            final int lastStart = status.offset;
            final int lastLen = bufferContentSize - lastStart;

            if (lastLen > 0) {

                if (status.inStructure) {
                    throw new ParseException(
                            "Incomplete structure: \"" + new String(buffer, lastStart, lastLen) + "\"", status.line, status.col);
                }

                handler.handleText(buffer, lastStart, lastLen, status.line, status.col);

                // As we have produced an additional text event, we need to fast-forward the
                // lastLine and lastCol position to include the last text structure.
                for (int i = lastStart; i < (lastStart + lastLen); i++) {
                    final char c = buffer[i];
                    if (c == '\n') {
                        lastLine++;
                        lastCol = 1;
                    } else {
                        lastCol++;
                    }

                }

            }

            final long parsingEndTimeNanos = System.nanoTime();
            handler.handleDocumentEnd(parsingEndTimeNanos, (parsingEndTimeNanos - parsingStartTimeNanos), lastLine, lastCol);

        } catch (final ParseException e) {
            throw e;
        } catch (final Exception e) {
            throw new ParseException(e);
        } finally {
            this.pool.releaseBuffer(buffer);
            try {
                reader.close();
            } catch (final Throwable ignored) {
                // This exception can be safely ignored
            }
        }

    }









    /*
     * This method is roughly equivalent to the one receiving a Reader, but oriented to parsing an already-existing
     * buffer without the need to allocate one from the pool.
     */
    void parseDocument(
            final char[] buffer, final int offset, final int len,
            final IMarkupHandler handler, final ParseStatus status)
            throws ParseException {


        final long parsingStartTimeNanos = System.nanoTime();

        try {

            handler.handleDocumentStart(parsingStartTimeNanos, 1, 1);

            status.offset = -1;
            status.line = 1;
            status.col = 1;
            status.inStructure = false;
            status.parsingDisabled = true;
            status.parsingDisabledLimitSequence = null;
            status.autoCloseRequired = null;
            status.autoCloseLimits = null;

            parseBuffer(buffer, offset, len, handler, status);

            // First parse done, now it's time to clean up in case we still have some text to be notified

            int lastLine = status.line;
            int lastCol = status.col;

            final int lastStart = status.offset;
            final int lastLen = (offset + len) - lastStart;

            if (lastLen > 0) {

                if (status.inStructure) {
                    throw new ParseException(
                            "Incomplete structure: \"" + new String(buffer, lastStart, lastLen) + "\"", status.line, status.col);
                }

                handler.handleText(buffer, lastStart, lastLen, status.line, status.col);

                // As we have produced an additional text event, we need to fast-forward the
                // lastLine and lastCol position to include the last text structure.
                for (int i = lastStart; i < (lastStart + lastLen); i++) {
                    final char c = buffer[i];
                    if (c == '\n') {
                        lastLine++;
                        lastCol = 1;
                    } else {
                        lastCol++;
                    }

                }

            }

            final long parsingEndTimeNanos = System.nanoTime();
            handler.handleDocumentEnd(parsingEndTimeNanos, (parsingEndTimeNanos - parsingStartTimeNanos), lastLine, lastCol);

        } catch (final ParseException e) {
            throw e;
        } catch (final Exception e) {
            throw new ParseException(e);
        }

    }












    
    
    private void parseBuffer(
            final char[] buffer, final int offset, final int len,
            final IMarkupHandler handler,
            final ParseStatus status)
            throws ParseException {


        final int[] locator = new int[] {status.line, status.col};
        
        int currentLine;
        int currentCol;
        
        final int maxi = offset + len;
        int i = offset;
        int current = i;

        boolean inStructure;

        boolean inOpenElement = false;
        boolean inCloseElement = false;
        boolean inComment = false;
        boolean inCdata = false;
        boolean inDocType = false;
        boolean inXmlDeclaration = false;
        boolean inProcessingInstruction = false;

        int tagStart;
        int tagEnd;
        
        while (i < maxi) {

            currentLine = locator[0];
            currentCol = locator[1];

            if (status.parsingDisabledLimitSequence != null) {
                // We need to disable parsing until we find a specific character sequence.
                // This allows correct parsing of CDATA (not PCDATA) sections (e.g. <script> tags).
                final int sequenceIndex =
                        ParsingMarkupUtil.findCharacterSequence(buffer, i, maxi, locator, status.parsingDisabledLimitSequence);
                if (sequenceIndex == -1) {

                    // Not found, should ask for more buffer
                    if (this.configuration.isTextSplittable()) {
                        handler.handleText(buffer, current, len - current, currentLine, currentCol);
                        // No need to change the disability limit, as we havent reached the sequence yet
                        current = len;
                    }

                    status.offset = current;
                    status.line = currentLine;
                    status.col = currentCol;
                    status.inStructure = false;
                    return;

                }

                // Return the unparsed text sequence (even if more text comes afterwards, this unparsed sequence
                // should be returned now so that the 'skipUntil' sequence is not included in the middle of
                // a returned Text event (if parsing is not re-enabled with a structure). Parsing-disabled and
                // parsing-enabled events should not be mixed in order to improve event handling.

                handler.handleText(buffer, current, sequenceIndex - current, currentLine, currentCol);
                status.parsingDisabledLimitSequence = null;
                status.parsingDisabled = true;

                current = sequenceIndex;
                i = current;

            }

            inStructure =
                    (inOpenElement || inCloseElement || inComment || inCdata || inDocType || inXmlDeclaration || inProcessingInstruction);

            if (!inStructure) {
                
                tagStart = ParsingMarkupUtil.findNextStructureStart(buffer, i, maxi, locator);
                
                if (tagStart == -1) {

                    if (this.configuration.isTextSplittable()) {

                        handler.handleText(buffer, current, len - current, currentLine, currentCol);
                        if (status.parsingDisabledLimitSequence != null) {
                            status.parsingDisabled = false;
                        }

                        current = len;

                    }

                    status.offset = current;
                    status.line = currentLine;
                    status.col = currentCol;
                    status.inStructure = false;
                    return;

                }

                inOpenElement = ParsingElementMarkupUtil.isOpenElementStart(buffer, tagStart, maxi);
                if (!inOpenElement) {
                    inCloseElement = ParsingElementMarkupUtil.isCloseElementStart(buffer, tagStart, maxi);
                    if (!inCloseElement) {
                        inComment = ParsingCommentMarkupUtil.isCommentStart(buffer, tagStart, maxi);
                        if (!inComment) {
                            inCdata = ParsingCDATASectionMarkupUtil.isCDATASectionStart(buffer, tagStart, maxi);
                            if (!inCdata) {
                                inDocType = ParsingDocTypeMarkupUtil.isDocTypeStart(buffer, tagStart, maxi);
                                if (!inDocType) {
                                    inXmlDeclaration = ParsingXmlDeclarationMarkupUtil.isXmlDeclarationStart(buffer, tagStart, maxi);
                                    if (!inXmlDeclaration) {
                                        inProcessingInstruction = ParsingProcessingInstructionUtil.isProcessingInstructionStart(buffer, tagStart, maxi);
                                    }
                                }
                            }
                        }
                    }
                }
                
                inStructure =
                        (inOpenElement || inCloseElement || inComment || inCdata || inDocType || inXmlDeclaration || inProcessingInstruction);
                
                
                while (!inStructure) {
                    // We found a '<', but it cannot be considered a tag because it is not
                    // the beginning of any known structure
                    
                    ParsingLocatorUtil.countChar(locator, buffer[tagStart]);
                    tagStart = ParsingMarkupUtil.findNextStructureStart(buffer, tagStart + 1, maxi, locator);
                    
                    if (tagStart == -1) {
                        status.offset = current;
                        status.line = currentLine;
                        status.col = currentCol;
                        status.inStructure = false;
                        return;
                    }

                    inOpenElement = ParsingElementMarkupUtil.isOpenElementStart(buffer, tagStart, maxi);
                    if (!inOpenElement) {
                        inCloseElement = ParsingElementMarkupUtil.isCloseElementStart(buffer, tagStart, maxi);
                        if (!inCloseElement) {
                            inComment = ParsingCommentMarkupUtil.isCommentStart(buffer, tagStart, maxi);
                            if (!inComment) {
                                inCdata = ParsingCDATASectionMarkupUtil.isCDATASectionStart(buffer, tagStart, maxi);
                                if (!inCdata) {
                                    inDocType = ParsingDocTypeMarkupUtil.isDocTypeStart(buffer, tagStart, maxi);
                                    if (!inDocType) {
                                        inXmlDeclaration = ParsingXmlDeclarationMarkupUtil.isXmlDeclarationStart(buffer, tagStart, maxi);
                                        if (!inXmlDeclaration) {
                                            inProcessingInstruction = ParsingProcessingInstructionUtil.isProcessingInstructionStart(buffer, tagStart, maxi);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    inStructure =
                            (inOpenElement || inCloseElement || inComment || inCdata || inDocType || inXmlDeclaration || inProcessingInstruction);
                
                }
            
                
                if (tagStart > current) {
                    // We avoid empty-string text events

                    handler.handleText(
                            buffer, current, (tagStart - current),
                            currentLine, currentCol);

                    if (status.parsingDisabledLimitSequence != null) {
                        status.parsingDisabled = false;
                    }

                }
                
                current = tagStart;
                i = current;
                
            } else {

                // We do not include processing instructions here because their format
                // is undefined, and everything should be allowed except the "?>" sequence,
                // which will terminate the instruction.
                final boolean avoidQuotes =
                        (inOpenElement || inCloseElement || inDocType || inXmlDeclaration);

                
                tagEnd =
                        (inDocType?
                                ParsingDocTypeMarkupUtil.findNextDocTypeStructureEnd(buffer, i, maxi, locator) :
                                (avoidQuotes?
                                        ParsingMarkupUtil.findNextStructureEndAvoidQuotes(buffer, i, maxi, locator) :
                                        ParsingMarkupUtil.findNextStructureEndDontAvoidQuotes(buffer, i, maxi, locator)));
                
                if (tagEnd < 0) {
                    // This is an unfinished structure
                    status.offset = current;
                    status.line = currentLine;
                    status.col = currentCol;
                    status.inStructure = true;
                    return;
                }

                
                if (inOpenElement) {
                    // This is a open/standalone tag (to be determined by looking at the penultimate character)

                    if ((buffer[tagEnd - 1] == '/')) {
                        ParsingElementMarkupUtil.
                                parseStandaloneElement(
                                        buffer, current, (tagEnd - current) + 1, currentLine, currentCol, handler);
                    } else {
                        ParsingElementMarkupUtil.
                                parseOpenElement(
                                        buffer, current, (tagEnd - current) + 1, currentLine, currentCol, handler);
                    }


                    if (status.parsingDisabledLimitSequence != null) {
                        status.parsingDisabled = false;
                    }

                    inOpenElement = false;
                    
                } else if (inCloseElement) {
                    // This is a closing tag

                    ParsingElementMarkupUtil.
                            parseCloseElement(
                                    buffer, current, (tagEnd - current) + 1, currentLine, currentCol, handler);

                    if (status.parsingDisabledLimitSequence != null) {
                        status.parsingDisabled = false;
                    }

                    inCloseElement = false;
                    
                } else if (inComment) {
                    // This is a comment! (obviously ;-))
                    
                    while (tagEnd - current < 6 || buffer[tagEnd - 1] != '-' || buffer[tagEnd - 2] != '-') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        ParsingLocatorUtil.countChar(locator, buffer[tagEnd]);
                        tagEnd = ParsingMarkupUtil.findNextStructureEndDontAvoidQuotes(buffer, tagEnd + 1, maxi, locator);
                        
                        if (tagEnd == -1) {
                            status.offset = current;
                            status.line = currentLine;
                            status.col = currentCol;
                            status.inStructure = true;
                            return;
                        }
                        
                    }

                    ParsingCommentMarkupUtil.parseComment(buffer, current, (tagEnd - current) + 1, currentLine, currentCol, handler);

                    if (status.parsingDisabledLimitSequence != null) {
                        status.parsingDisabled = false;
                    }

                    inComment = false;
                    
                } else if (inCdata) {
                    // This is a CDATA section
                    
                    while (tagEnd - current < 11 || buffer[tagEnd - 1] != ']' || buffer[tagEnd - 2] != ']') {
                        // the '>' we chose is not the comment-closing one. Let's find again
                        
                        ParsingLocatorUtil.countChar(locator, buffer[tagEnd]);
                        tagEnd = ParsingMarkupUtil.findNextStructureEndDontAvoidQuotes(buffer, tagEnd + 1, maxi, locator);
                        
                        if (tagEnd == -1) {
                            status.offset = current;
                            status.line = currentLine;
                            status.col = currentCol;
                            status.inStructure = true;
                            return;
                        }
                        
                    }

                    ParsingCDATASectionMarkupUtil.parseCDATASection(buffer, current, (tagEnd - current) + 1, currentLine, currentCol, handler);

                    if (status.parsingDisabledLimitSequence != null) {
                        status.parsingDisabled = false;
                    }

                    inCdata = false;
                    
                } else if (inDocType) {
                    // This is a DOCTYPE clause

                    ParsingDocTypeMarkupUtil.parseDocType(
                            buffer, current, ((tagEnd - current) + 1), currentLine, currentCol, handler);

                    if (status.parsingDisabledLimitSequence != null) {
                        status.parsingDisabled = false;
                    }

                    inDocType = false;
                    
                } else if (inXmlDeclaration) {
                    // This is an XML Declaration

                    ParsingXmlDeclarationMarkupUtil.parseXmlDeclaration(
                            buffer, current, (tagEnd - current) + 1, currentLine, currentCol, handler);

                    if (status.parsingDisabledLimitSequence != null) {
                        status.parsingDisabled = false;
                    }

                    inXmlDeclaration = false;
                    
                } else if (inProcessingInstruction) {
                    // This is a processing instruction

                    while (tagEnd - current < 5 || buffer[tagEnd - 1] != '?') {
                        // the '>' we chose is not the PI-closing one. Let's find again

                        ParsingLocatorUtil.countChar(locator, buffer[tagEnd]);
                        tagEnd = ParsingMarkupUtil.findNextStructureEndDontAvoidQuotes(buffer, tagEnd + 1, maxi, locator);
                        
                        if (tagEnd == -1) {
                            status.offset = current;
                            status.line = currentLine;
                            status.col = currentCol;
                            status.inStructure = true;
                            return;
                        }
                        
                    }

                    ParsingProcessingInstructionUtil.parseProcessingInstruction(
                            buffer, current, (tagEnd - current) + 1, currentLine, currentCol, handler);

                    if (status.parsingDisabledLimitSequence != null) {
                        status.parsingDisabled = false;
                    }

                    inProcessingInstruction = false;
                    
                } else {

                    throw new IllegalStateException(
                            "Illegal parsing state: structure is not of a recognized type");
                    
                }
                
                // The '>' char will be considered as processed too
                ParsingLocatorUtil.countChar(locator, buffer[tagEnd]);
                
                current = tagEnd + 1;
                i = current;
                
            }
            
        }

        status.offset = current;
        status.line = locator[0];
        status.col = locator[1];
        status.inStructure = false;

    }







    /*
     * This class models a pool of buffers, used to keep the amount of
     * large char[] buffer objects required to operate to a minimum.
     *
     * Note this pool never blocks, so if a new buffer is needed and all
     * are currently allocated, a new char[] object is created and returned.
     *
     */
    private static final class BufferPool {

        private final char[][] pool;
        private final boolean[] allocated;
        private final int poolBufferSize;

        private BufferPool(final int poolSize, final int poolBufferSize) {

            super();

            this.pool = new char[poolSize][];
            this.allocated = new boolean[poolSize];
            this.poolBufferSize = poolBufferSize;

            for (int i = 0; i < this.pool.length; i++) {
                this.pool[i] = new char[this.poolBufferSize];
            }
            Arrays.fill(this.allocated, false);

        }

        private synchronized char[] allocateBuffer(final int bufferSize) {
            if (bufferSize != this.poolBufferSize) {
                // We will only pool buffers of the default size. If a different size is required, we just
                // create it without pooling.
                return new char[bufferSize];
            }
            for (int i = 0; i < this.pool.length; i++) {
                if (!this.allocated[i]) {
                    this.allocated[i] = true;
                    return this.pool[i];
                }
            }
            return new char[bufferSize];
        }

        private synchronized void releaseBuffer(final char[] buffer) {
            if (buffer == null) {
                return;
            }
            if (buffer.length != this.poolBufferSize) {
                // This buffer cannot be part of the pool - only buffers with a specific size are contained
                return;
            }
            for (int i = 0; i < this.pool.length; i++) {
                if (this.pool[i] == buffer) {
                    // Found it. Mark it as non-allocated
                    this.allocated[i] = false;
                    return;
                }
            }
            // The buffer wasn't part of our pool. Just return.
        }


    }


}
