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
package org.attoparser.dom;

import java.io.Reader;

import org.attoparser.ParseException;


/**
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public interface IDOMMarkupParser {



    public Document parse(final String document)
            throws ParseException;


    public Document parse(final char[] document)
            throws ParseException;


    public Document parse(final char[] document, final int offset, final int len)
            throws ParseException;


    public Document parse(final Reader reader)
            throws ParseException;




    public Document parse(final String documentName, final String document)
            throws ParseException;


    public Document parse(final String documentName, final char[] document)
            throws ParseException;


    public Document parse(final String documentName, final char[] document, final int offset, final int len)
            throws ParseException;


    public Document parse(final String documentName, final Reader reader)
            throws ParseException;

}
