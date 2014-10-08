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
package org.attoparser.simple;

import java.io.Reader;

import org.attoparser.MarkupParser;
import org.attoparser.ParseException;
import org.attoparser.config.ParseConfiguration;


/**
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class SimpleMarkupParser implements ISimpleMarkupParser {


    private final MarkupParser markupParser;




    public SimpleMarkupParser(final ParseConfiguration configuration) {
        super();
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        this.markupParser = new MarkupParser(configuration);
    }





    public void parse(final String document, final ISimpleMarkupHandler handler)
            throws ParseException {

        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        this.markupParser.parse(document, new SimplifierMarkupHandler(handler));

    }


    public void parse(final char[] document, final ISimpleMarkupHandler handler)
            throws ParseException {

        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        this.markupParser.parse(document, new SimplifierMarkupHandler(handler));

    }


    public void parse(
            final char[] document, final int offset, final int len, final ISimpleMarkupHandler handler)
            throws ParseException {

        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        this.markupParser.parse(document, offset, len, new SimplifierMarkupHandler(handler));

    }


    public void parse(
            final Reader reader, final ISimpleMarkupHandler handler)
            throws ParseException {

        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        this.markupParser.parse(reader, new SimplifierMarkupHandler(handler));

    }


}
