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

import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.StackableElementAttoHandleResult;
import org.attoparser.markup.html.IDetailedHtmlElementHandling;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class SiblingAutoCloserHtmlElement extends BasicHtmlElement {


    private final char[][] siblingAutoCloserLimits;


    public SiblingAutoCloserHtmlElement(final String name, final String[] siblingAutoCloserLimits) {
        super(name);
        this.siblingAutoCloserLimits = new char[siblingAutoCloserLimits.length][];
        for (int i = 0; i < this.siblingAutoCloserLimits.length; i++) {
            this.siblingAutoCloserLimits[i] = siblingAutoCloserLimits[i].toCharArray();
        }
    }

    
    
    
    
    @Override
    public IAttoHandleResult handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col, 
            final HtmlElementStack stack, final IDetailedHtmlElementHandling handler) 
            throws AttoParseException {

        final IHtmlElement limit = checkForUnstackableLimit(stack, siblingAutoCloserLimits);
        if (limit != null) {
            return new StackableElementAttoHandleResult(null, limit.getNameCharArray(), true);
        }

        return super.handleOpenElementStart(buffer, nameOffset, nameLen, line, col, stack, handler);

    }


    private static IHtmlElement checkForUnstackableLimit(final HtmlElementStack stack, final char[][] limits) {

        int delta = 0;
        IHtmlElement peek = stack.peekElement(delta);
        while (peek != null) {
            for (final char[] limit : limits) {
                if (peek.matches(limit)) {
                    // We will only need an unstacking operation if we didn't find our limit as a parent
                    return (delta > 0? peek : null);
                }
            }
            peek = stack.peekElement(++delta);
        }

        return null;

    }

    
    
}