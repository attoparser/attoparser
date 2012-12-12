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
package org.attoparser.markup.html.warnings;



/**
 * <p>
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public abstract class AbstractHtmlParsingEventWarning implements IHtmlParsingEventWarning {


    private final String code;
    private final String message;
    
    
    protected AbstractHtmlParsingEventWarning(final String code, final String message) {
        super();
        this.code = code;
        this.message = message;
    }

    
    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    
    public String getCompleteMessage() {
        return "[" + this.code + "]" + this.message;
    }
    
    @Override
    public String toString() {
        return getCompleteMessage();
    }
    
    
}
