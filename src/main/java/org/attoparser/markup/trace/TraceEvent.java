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

import java.util.Arrays;





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

    
    
    @Override
    public String toString() {
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("[" + this.line + "," + this.col + "] " + this.type);
        if (hasContent()) {
            for (final String contentItem : getContent()) {
                strBuilder.append("(" + contentItem + ")");
            }
        }
        return strBuilder.toString();
    }

    
    
    
    public boolean matchesTypeAndContent(final TraceEvent event) {
        if (this == event) {
            return true;
        }
        if (event == null) {
            return false;
        }
        if (this.content == null) {
            if (event.content != null) {
                return false;
            }
        } else if (!Arrays.equals(this.content, event.content)) {
            return false;
        }
        if (this.type == null) {
            if (event.type != null) {
                return false;
            }
        } else if (!this.type.equals(event.type)) {
            return false;
        }
        return true;
    }
    
    
    
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.col;
        result = prime * result + Arrays.hashCode(this.content);
        result = prime * result + this.line;
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TraceEvent other = (TraceEvent) obj;
        if (this.col != other.col) {
            return false;
        }
        if (!Arrays.equals(this.content, other.content)) {
            return false;
        }
        if (this.line != other.line) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        return true;
    }
    
    
}