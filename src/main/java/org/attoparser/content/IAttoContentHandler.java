package org.attoparser.content;

import org.attoparser.exception.AttoParseException;


public interface IAttoContentHandler {

    public void startDocument()
            throws AttoParseException;
    
    public void endDocument()
            throws AttoParseException;
    
    public void startElement(char[] elementName, final int offset, final int len, final boolean isMinimized,
            final int line, final int pos)
            throws AttoParseException;
    
    public void endElement(char[] elementName, final int offset, final int len,
            final int line, final int pos)
            throws AttoParseException;

    public void attribute(
            char[] attributeName, final int nameOffset, final int nameLen,
            char[] attributeValue, final int valueOffset, final int valueLen,
            final int line, final int pos)
            throws AttoParseException;
    
    public void text(final char[] text, final int offset, final int len, 
            final int line, final int pos)
            throws AttoParseException;
    
    public void comment(final char[] comment, final int offset, final int len, 
            final int line, final int pos)
            throws AttoParseException;
    
    public void cdata(final char[] cdata, final int offset, final int len, 
            final int line, final int pos) 
            throws AttoParseException;
    
}
