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
package org.attoparser.markup.dom;




/**
 * <p>
 *   A CDATA Section node in a attoDOM tree.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class CDATASection extends Node {
    
    private static final long serialVersionUID = -131121996532074777L;
    
    
    private String content;



    public CDATASection(final String content, final int line, final int col) {
        super(line, col);
        Validate.notNull(content, "Content cannot be null");
        this.content = content;
    }

    public CDATASection(final String content) {
        super();
        Validate.notNull(content, "Content cannot be null");
        this.content = content;
    }

    
    
    /**
     * <p>
     *   Returns the textual content of this node.
     * </p>
     * 
     * @return the textual content of this node.
     */
    public String getContent() {
        return this.content;
    }
    
    
    public void setContent(final String content) {
        Validate.notNull(content, "Content cannot be null");
        this.content = content;
    }

    

    
    @Override
    public final void visit(final AttoDOMVisitor visitor)
            throws AttoDOMVisitorException {
        visitor.visitCDATASection(this);
    }
    
    
}
