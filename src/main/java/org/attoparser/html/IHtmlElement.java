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
package org.attoparser.html;

import org.attoparser.AttoParseException;
import org.attoparser.IElementPreparationResult;
import org.attoparser.IMarkupAttoHandler;
import org.attoparser.MarkupParsingStatus;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
interface IHtmlElement {
    
    String getName();

    boolean matches(final String elementName);
    boolean matches(final char[] elementName);
    boolean matches(final char[] elementNameBuffer, final int offset, final int len);



    IElementPreparationResult prepareForElement(
            final char[] buffer, final int nameOffset, final int nameLen, final int line, final int col,
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;

    
    void handleStandaloneElementStart(
            final char[] buffer, final int offset, final int len, final boolean minimized,
            final int line, final int col,
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;
    
    void handleStandaloneElementEnd(
            final char[] buffer, final int offset, final int len, final boolean minimized,
            final int line, final int col,
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;

    
    
    void handleOpenElementStart(
            final char[] buffer, final int offset, final int len, final int line, final int col, 
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;

    void handleOpenElementEnd(
            final char[] buffer, final int offset, final int len, final int line, final int col,
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;



    void handleCloseElementStart(
            final char[] buffer, final int offset, final int len, final int line, final int col, 
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;

    void handleCloseElementEnd(
            final char[] buffer, final int offset, final int len, final int line, final int col,
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;



    void handleAutoCloseElementStart(
            final char[] buffer, final int offset, final int len, final int line, final int col, 
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;

    void handleAutoCloseElementEnd(
            final char[] buffer, final int offset, final int len, final int line, final int col,
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;



    void handleUnmatchedCloseElementStart(
            final char[] buffer, final int offset, final int len, final int line, final int col, 
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;

    void handleUnmatchedCloseElementEnd(
            final char[] buffer, final int offset, final int len, final int line, final int col,
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;



    void handleAttribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen, final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen, final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol,
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;

    void handleInnerWhiteSpace(
            final char[] buffer, final int offset, final int len, final int line, final int col,
            final IMarkupAttoHandler handler, final MarkupParsingStatus status)
            throws AttoParseException;
    
}