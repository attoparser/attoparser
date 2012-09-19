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

import org.attoparser.AttoParseException;






/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractBreakDownMarkupAttoHandler 
        extends AbstractMarkupAttoHandler
        implements IElementBreakDownHandling {


    
    protected AbstractBreakDownMarkupAttoHandler() {
        super();
    }

    
    @Override
    public final void standaloneElement(
            final char[] buffer, 
            final int innerOffset, final int innerLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col) 
            throws AttoParseException {
        MarkupParsingUtil.parseStandaloneElementBreakDown(buffer, innerOffset, innerLen, outerOffset, outerLen, line, col, this);
    }

    
    @Override
    public final void openElement(
            final char[] buffer, 
            final int innerOffset, final int innerLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col) 
            throws AttoParseException {
        MarkupParsingUtil.parseOpenElementBreakDown(buffer, innerOffset, innerLen, outerOffset, outerLen, line, col, this);
    }

    
    @Override
    public final void closeElement(
            final char[] buffer, 
            final int innerOffset, final int innerLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        MarkupParsingUtil.parseCloseElementBreakDown(buffer, innerOffset, innerLen, outerOffset, outerLen, line, col, this);

    }


    public void standaloneElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void standaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void standaloneElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    

    public void openElementStart(
            final char[] buffer,
            final int offset,
            final int len, final int line,
            final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void openElementName(
            final char[] buffer,
            final int offset,
            final int len, final int line,
            final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void openElementEnd(
            final char[] buffer,
            final int offset,
            final int len, final int line,
            final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
    public void closeElementStart(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void closeElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void closeElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
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
        // Nothing to be done here, meant to be overridden if required
    }


    
    public void elementWhitespace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
}