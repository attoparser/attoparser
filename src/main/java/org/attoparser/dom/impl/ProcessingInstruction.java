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
package org.attoparser.dom.impl;

import java.io.Serializable;

import org.attoparser.dom.INestableNode;
import org.attoparser.dom.IProcessingInstruction;




/**
 * <p>
 *   Default implementation of the {@link IProcessingInstruction} interface.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class ProcessingInstruction 
        extends AbstractNode
        implements IProcessingInstruction, Serializable {
    
    private static final long serialVersionUID = 7832638382597687056L;
    
    private String target;
    private String content;


    public ProcessingInstruction(final String target, final String content) {
        super();
        Validate.notNull(target, "Target cannot be null");
        this.target = target;
        this.content = content;
    }

    
    
    public String getTarget() {
        return this.target;
    }
    
    public void setTarget(final String target) {
        Validate.notNull(target, "Target cannot be null");
        this.target = target;
    }

    
    public String getContent() {
        return this.content;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }


    public ProcessingInstruction cloneNode(final INestableNode parent) {
        final ProcessingInstruction processingInstruction = new ProcessingInstruction(this.target, this.content);
        processingInstruction.setLine(getLine());
        processingInstruction.setCol(getCol());
        processingInstruction.setParent(parent);
        return processingInstruction;
    }
    
    
}
