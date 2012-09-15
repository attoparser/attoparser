package org.attoparser.exception;

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
