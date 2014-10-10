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
 *   Implementation of {@link org.attoparser.IMarkupHandler} used for writing received parsing events as text output,
 *   by ignoring all events except the <em>Text</em> ones. This means this handler will effectively strip all
 *   markup tags (and other structures like comments, CDATA, etc.) away.
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
 *   final IMarkupHandler handler = new TextOutputMarkupHandler(writer);
 *   parser.parse(document, handler);
 *   return writer.toString();
 * </code></pre>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public final class TextOutputMarkupHandler extends AbstractMarkupHandler {


    private final Writer writer;



    public TextOutputMarkupHandler(final Writer writer) {
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

    
}