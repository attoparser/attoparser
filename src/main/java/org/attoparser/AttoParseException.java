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
package org.attoparser;



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
    private final Integer col;

    

    public AttoParseException() {
        super();
        this.line = null;
        this.col = null;
    }

    public AttoParseException(final String message, final Throwable throwable) {
        
        super(message(message, throwable), throwable);
        
        if (throwable != null && throwable instanceof AttoParseException) {
            this.line = ((AttoParseException)throwable).getLine();
            this.col = ((AttoParseException)throwable).getCol();
        } else {
            this.line = null;
            this.col = null;
        }
        
    }

    public AttoParseException(final String message) {
        super(message);
        this.line = null;
        this.col = null;
    }

    public AttoParseException(final Throwable throwable) {
        
        super(message(null, throwable), throwable);
        
        if (throwable != null && throwable instanceof AttoParseException) {
            this.line = ((AttoParseException)throwable).getLine();
            this.col = ((AttoParseException)throwable).getCol();
        } else {
            this.line = null;
            this.col = null;
        }
        
    }
    

    public AttoParseException(final int line, final int col) {
        super(messagePrefix(line, col));
        this.line = Integer.valueOf(line);
        this.col = Integer.valueOf(col);
    }

    public AttoParseException(final String message, final Throwable throwable, final int line, final int col) {
        super(messagePrefix(line, col) + " " + message, throwable);
        this.line = Integer.valueOf(line);
        this.col = Integer.valueOf(col);
    }

    public AttoParseException(final String message, final int line, final int col) {
        super(messagePrefix(line, col) + " " + message);
        this.line = Integer.valueOf(line);
        this.col = Integer.valueOf(col);
    }

    public AttoParseException(final Throwable throwable, final int line, final int col) {
        super(messagePrefix(line, col), throwable);
        this.line = Integer.valueOf(line);
        this.col = Integer.valueOf(col);
    }

    
    
    
    private static String messagePrefix(final int line, final int col) {
        return "(Line = " + line + ", Column = " + col + ")"; 
    }
    
    
    
    private static String message(final String message, final Throwable throwable) {
        
        if (throwable != null && throwable instanceof AttoParseException) {
            
            final AttoParseException exception = (AttoParseException)throwable;
            if (exception.getLine() != null && exception.getCol() != null) {
                return "(Line = " + exception.getLine() + ", Column = " + exception.getCol() + ")" + 
                        (message != null? (" " + message) : throwable.getMessage()); 
            }

        }
        if (message != null) {
            return message;
        }
        if (throwable != null) {
            return throwable.getMessage();
        }
        return null;
        
    }
    
    
    
    
    
    public Integer getLine() {
        return this.line;
    }

    public Integer getCol() {
        return this.col;
    }

    
}
