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
import org.attoparser.ElementPreparationResult;
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
final class VoidAutoCloserHtmlElement extends VoidHtmlElement {


    private final ElementPreparationResult preparationResult;


    VoidAutoCloserHtmlElement(final String name, final String[] autoCloseElements, final String[] autoCloseLimits) {

        super(name);

        if (autoCloseElements == null) {
            throw new IllegalArgumentException("The array of auto-close elements cannot be null");
        }

        final char[][] autoCloseElementsCharArray = new char[autoCloseElements.length][];
        for (int i = 0; i < autoCloseElementsCharArray.length; i++) {
            autoCloseElementsCharArray[i] = autoCloseElements[i].toCharArray();
        }

        final char[][] autoCloseLimitsCharArray;
        if (autoCloseLimits != null) {
            autoCloseLimitsCharArray = new char[autoCloseLimits.length][];
            for (int i = 0; i < autoCloseLimitsCharArray.length; i++) {
                autoCloseLimitsCharArray[i] = autoCloseLimits[i].toCharArray();
            }
        } else {
            autoCloseLimitsCharArray = null;
        }

        this.preparationResult = new ElementPreparationResult(autoCloseElementsCharArray, autoCloseLimitsCharArray, Boolean.FALSE);

    }


    @Override
    public IElementPreparationResult prepareForElement(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupAttoHandler handler,
            final MarkupParsingStatus status)
            throws AttoParseException {

        return this.preparationResult;

    }



    
    
}