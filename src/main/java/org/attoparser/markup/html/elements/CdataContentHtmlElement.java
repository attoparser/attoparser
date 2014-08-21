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


    private final IAttoHandleResult openElementEndResult;


    public CdataContentHtmlElement(final String name) {
        super(name);
        // This result will mean parsing will be disabled until we find the closing tag for this element.
        this.openElementEndResult = new AttoHandleResult(("</" + name + ">").toCharArray(), null);
    }


    
    @Override
    public IAttoHandleResult handleOpenElementEnd(
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {


        final IAttoHandleResult result1 = handler.handleHtmlOpenElementEnd(this, line, col);

        // Disable parsing until the closing element is found (this element's content is #CDATA, not #PCDATA)
        return AttoHandleResultUtil.combinePriorityLast(result1, this.openElementEndResult);
        
    }

    
}