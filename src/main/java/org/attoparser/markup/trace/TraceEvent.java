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
package org.attoparser.markup.trace;





/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.2
 *
 */
public final class TraceEvent {
        
    private final String type;
    private final String[] content;
    private final int line;
    private final int col;
    
    
    public TraceEvent(final int line, final int col, final String type, final String... content) {
        
        super();
        
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        
        this.type = type;
        this.content = content;
        this.line = line;
        this.col = col;
        
    }


    public String getType() {
        return this.type;
    }

    public boolean hasContent() {
        return this.content != null;
    }
    
    public String[] getContent() {
        return this.content;
    }

    public int getLine() {
        return this.line;
    }

    public int getCol() {
        return this.col;
    }

    
}