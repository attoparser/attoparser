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
package org.attoparser.exception;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public class AttoParseException extends Exception {

    private static final long serialVersionUID = -7951733720511589140L;

    private final Integer line;
    private final Integer pos;

    

    public AttoParseException() {
        super();
        this.line = null;
        this.pos = null;
    }

    public AttoParseException(final String message, final Throwable throwable) {
        super(message, throwable);
        this.line = null;
        this.pos = null;
    }

    public AttoParseException(final String message) {
        super(message);
        this.line = null;
        this.pos = null;
    }

    public AttoParseException(final Throwable throwable) {
        super(throwable);
        this.line = null;
        this.pos = null;
    }
    

    public AttoParseException(final int line, final int pos) {
        super(messagePrefix(line, pos));
        this.line = Integer.valueOf(line);
        this.pos = Integer.valueOf(pos);
    }

    public AttoParseException(final String message, final Throwable throwable, final int line, final int pos) {
        super(messagePrefix(line, pos) + " " + message, throwable);
        this.line = Integer.valueOf(line);
        this.pos = Integer.valueOf(pos);
    }

    public AttoParseException(final String message, final int line, final int pos) {
        super(messagePrefix(line, pos) + " " + message);
        this.line = Integer.valueOf(line);
        this.pos = Integer.valueOf(pos);
    }

    public AttoParseException(final Throwable throwable, final int line, final int pos) {
        super(messagePrefix(line, pos), throwable);
        this.line = Integer.valueOf(line);
        this.pos = Integer.valueOf(pos);
    }

    
    private static String messagePrefix(final int line, final int pos) {
        return "(Line = " + line + ", Position = " + pos + ")"; 
    }
    
    
    
    public Integer getLine() {
        return this.line;
    }

    public Integer getPos() {
        return this.pos;
    }

    
}
