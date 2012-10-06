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
 *   A Processing Instruction node in a attoDOM tree.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class ProcessingInstruction extends Node {
    
    private static final long serialVersionUID = 7832638382597687056L;
    
    
    private final String target;
    private final String content;



    ProcessingInstruction(final String target, final String content, final int line, final int col) {
        super(line, col);
        this.target = target;
        this.content = content;
    }

    
    
    /**
     * <p>
     *   Returns the target of the Processing Instruction.
     * </p>
     * 
     * @return the target of the PI.
     */
    public String getTarget() {
        return this.target;
    }

    
    /**
     * <p>
     *   Returns the content of the Processing Instruction.
     * </p>
     * 
     * @return the content of the PI.
     */
    public String getContent() {
        return this.content;
    }
    

    

    @Override
    public final void visit(final AttoDOMVisitor visitor)
            throws AttoDOMVisitorException {
        visitor.visitProcessingInstruction(this);
    }
    
    
}
