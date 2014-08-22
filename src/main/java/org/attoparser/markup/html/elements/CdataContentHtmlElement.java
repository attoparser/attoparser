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
package org.attoparser.markup.html.elements;

import org.attoparser.AttoHandleResult;
import org.attoparser.AttoHandleResultUtil;
import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.markup.html.IDetailedHtmlElementHandling;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class CdataContentHtmlElement extends BasicHtmlElement {


    private final char[] nameLower;
    private final char[] nameUpper;
    private final IAttoHandleResult openElementEndResultLower;
    private final IAttoHandleResult openElementEndResultUpper;


    public CdataContentHtmlElement(final String name) {

        super(name);

        // This result will mean parsing will be disabled until we find the closing tag for this element.
        final String nameLower = name.toLowerCase();
        final String nameUppoer = name.toUpperCase();

        this.nameLower = nameLower.toCharArray();
        this.nameUpper = nameUppoer.toCharArray();

        this.openElementEndResultLower = new AttoHandleResult(("</" + nameLower + ">").toCharArray());
        this.openElementEndResultUpper = new AttoHandleResult(("</" + nameUppoer + ">").toCharArray());

    }


    
    @Override
    public IAttoHandleResult handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {


        final IAttoHandleResult result1 =
                handler.handleHtmlOpenElementEnd(this, buffer, nameOffset, nameLen, line, col);

        final IAttoHandleResult result2 = computeResult(buffer, nameOffset, nameLen);

        // Disable parsing until the closing element is found (this element's content is #CDATA, not #PCDATA)
        return AttoHandleResultUtil.combinePriorityLast(result1, result2);
        
    }



    private final IAttoHandleResult computeResult(final char[] buffer, final int nameOffset, final int nameLen) {

        if (nameEquals(buffer, nameOffset, nameLen, this.nameLower)) {
            return this.openElementEndResultLower;
        }

        if (nameEquals(buffer, nameOffset, nameLen, this.nameUpper)) {
            return this.openElementEndResultUpper;
        }

        final char[] limit = new char[nameLen + 3];
        limit[0] = '<';
        limit[1] = '/';
        System.arraycopy(buffer,nameOffset,limit,2,nameLen);
        limit[nameLen + 2] = '>';
        return new AttoHandleResult(limit);

    }


    private static final boolean nameEquals(final char[] buffer, final int nameOffset, final int nameLen, final char[] comparedTo) {
        if (nameLen != comparedTo.length) {
            return false;
        }
        for (int i = 0; i < nameLen; i++) {
            if (buffer[i + nameOffset] != comparedTo[i]) {
                return false;
            }
        }
        return true;
    }


    
}