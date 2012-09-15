package org.attoparser;

import org.attoparser.config.DefaultAttoConfig;
import org.attoparser.config.IAttoConfig;
import org.attoparser.content.IAttoContentHandler;
import org.attoparser.exception.AttoParseException;


public abstract class AbstractAttoParser implements IAttoParser {

    
    private static final DefaultAttoConfig DEFAULT_ATTO_CONFIG = new DefaultAttoConfig();

    
    
    public AbstractAttoParser() {
        super();
    }
    

    
    public final void parse(final String document, final IAttoContentHandler handler) 
            throws AttoParseException {
        parse(document, handler, DEFAULT_ATTO_CONFIG);
    }
    
    public final void parse(final char[] document, final IAttoContentHandler handler) 
            throws AttoParseException {
        parse(document, handler, DEFAULT_ATTO_CONFIG);
    }

    public final void parse(final String document, final int offset, final int len, final IAttoContentHandler handler) 
            throws AttoParseException {
        parse(document, offset, len, handler, DEFAULT_ATTO_CONFIG);
    }
    
    public final void parse(final char[] document, final int offset, final int len, final IAttoContentHandler handler) 
            throws AttoParseException {
        parse(document, offset, len, handler, DEFAULT_ATTO_CONFIG);
    }
    
    public final void parse(final String document, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException  {
        parse(document.toCharArray(), handler, config);
    }
    
    public final void parse(final char[] document, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException {
        parse(document, 0, document.length, handler, config);
    }

    public final void parse(
            final String document, final int offset, final int len, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException {
        // We avoid copying the entire String if we only need some chars
        final char[] doc = new char[len];
        document.getChars(offset, (offset + len), doc, 0);
        parse(doc, 0, len, handler, config);
    }
    
    
}
