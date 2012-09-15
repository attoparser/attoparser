package org.attoparser;

import org.attoparser.config.IAttoConfig;
import org.attoparser.content.IAttoContentHandler;
import org.attoparser.exception.AttoParseException;

public interface IAttoParser {
    
    
    public void parse(final String document, final IAttoContentHandler handler) 
            throws AttoParseException;
    
    public void parse(final char[] document, final IAttoContentHandler handler) 
            throws AttoParseException;

    public void parse(final String document, final int offset, final int len, final IAttoContentHandler handler) 
            throws AttoParseException;
    
    public void parse(final char[] document, final int offset, final int len, final IAttoContentHandler handler) 
            throws AttoParseException;
    
    public void parse(final String document, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException;
    
    public void parse(final char[] document, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException;

    public void parse(
            final String document, final int offset, final int len, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException;
    
    public void parse(
            final char[] document, final int offset, final int len, final IAttoContentHandler handler, final IAttoConfig config) 
            throws AttoParseException;
    
}
