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

import java.io.Serializable;


/**
 * <p>
 *   Comment node in a DOM tree.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public class Comment 
        extends AbstractNode 
        implements Serializable {
    
    private static final long serialVersionUID = -5222507854714214977L;
    
    
    private String content;


    public Comment(final String content) {
        super();
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        this.content = content;
    }

    
    
    
    public String getContent() {
        return this.content;
    }
    

    public void setContent(final String content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        this.content = content;
    }

    
    public void setContent(final char[] buffer, final int offset, final int len) {
        this.content = new String(buffer, offset, len);
    }

    
    
    public Comment cloneNode(final INestableNode parent) {
        final Comment comment = new Comment(this.content);
        comment.setLine(getLine());
        comment.setCol(getCol());
        comment.setParent(parent);
        return comment;
    }

    


    
}
