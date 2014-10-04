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
package org.attoparser.dom.impl;

import java.io.Serializable;

import org.attoparser.dom.INestableNode;
import org.attoparser.dom.IText;





/**
 * <p>
 *   Default implementation of the {@link IText} interface.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class Text 
        extends AbstractNode 
        implements IText, Serializable {

    private static final long serialVersionUID = -6449838157196892217L;
    
    
    private String content;


    public Text(final String content) {
        super();
        Validate.notNull(content, "Content cannot be null");
        this.content = content;
    }

    
    
    public String getContent() {
        return this.content;
    }
    

    public void setContent(final String content) {
        Validate.notNull(content, "Content cannot be null");
        this.content = content;
    }

    
    public void setContent(final char[] buffer, final int offset, final int len) {
        this.content = new String(buffer, offset, len);
    }

    
    
    public Text cloneNode(final INestableNode parent) {
        final Text text = new Text(this.content);
        text.setLine(getLine());
        text.setCol(getCol());
        text.setParent(parent);
        return text;
    }

    
}
