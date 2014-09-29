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
package org.attoparser.markup.xml;

import org.attoparser.AttoParseException;
import org.attoparser.IAttoHandleResult;
import org.attoparser.markup.AbstractMarkupAttoHandler;






/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public abstract class AbstractXmlAttoHandler extends AbstractMarkupAttoHandler
        implements IDetailedXmlElementHandling, IDetailedXmlAttributeSequenceHandling {


    



    public AbstractXmlAttoHandler() {
        super();
    }


    

    /*
     * ----------------------------------------------------------------------
     * NOTE no implementation is needed at this level of:
     *   handleDocumentStart(long, int, int, MarkupParsingConfiguration)
     *   handleDocumentEnd(long, long, int, int, MarkupParsingConfiguration)
     *   prepareForElement(char[],int,int,int,int)
     * ----------------------------------------------------------------------
     */



    @Override
    public final IAttoHandleResult handleStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col)
            throws AttoParseException {
        return handleXmlStandaloneElementStart(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final IAttoHandleResult handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        return handleXmlStandaloneElementEnd(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final IAttoHandleResult handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        return handleXmlOpenElementStart(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final IAttoHandleResult handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        return handleXmlOpenElementEnd(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final IAttoHandleResult handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        return handleXmlCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final IAttoHandleResult handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        return handleXmlCloseElementEnd(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final IAttoHandleResult handleAutoCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Auto closing elements should not be happening in well-formed XML, that that's up to the handler to decide
        // by providing an adequate MarkupParsingConfiguration instance during construction.
        return handleXmlAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }

    
    @Override
    public final IAttoHandleResult handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Auto closing elements should not be happening in well-formed XML, that that's up to the handler to decide
        // by providing an adequate MarkupParsingConfiguration instance during construction.
        return handleXmlAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final IAttoHandleResult handleUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Parsing of unmatched close elements should not be happening in well-formed XML, that that's up to the handler to decide
        // by providing an adequate MarkupParsingConfiguration instance during construction.
        return handleXmlUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }

    @Override
    public final IAttoHandleResult handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Parsing of unmatched close elements should not be happening in well-formed XML, that that's up to the handler to decide
        // by providing an adequate MarkupParsingConfiguration instance during construction.
        return handleXmlUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
    }


    @Override
    public final IAttoHandleResult handleAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen, final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen, final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {
        return handleXmlAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                valueLine, valueCol);
    }

    @Override
    public final IAttoHandleResult handleInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        return handleXmlInnerWhiteSpace(buffer, offset, len, line, col);
    }







    /*
     * ************************************************************
     * *                                                          *
     * * IMPLEMENTATION OF IDetailedXmlElementHandling INTERFACE  *
     * *                                                          *
     * ************************************************************
     */



    /*
     * -------------------------------------------------
     * STANDALONE ELEMENTS: <img ... />, <img ... >, etc
     * -------------------------------------------------
     */

    public IAttoHandleResult handleXmlStandaloneElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    public IAttoHandleResult handleXmlStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    /*
     * -------------------------
     * OPEN ELEMENTS: <div ... >
     * -------------------------
     */

    public IAttoHandleResult handleXmlOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    public IAttoHandleResult handleXmlOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    /*
     * ----------------------
     * CLOSE ELEMENTS: </div>
     * ----------------------
     */

    public IAttoHandleResult handleXmlCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    public IAttoHandleResult handleXmlCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    /*
     * ------------------------------------------
     * AUTO CLOSE ELEMENTS (because of balancing)
     * ------------------------------------------
     */

    public IAttoHandleResult handleXmlAutoCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    public IAttoHandleResult handleXmlAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    /*
     * ------------------------------------------------
     * UNMATCHED CLOSE ELEMENTS: </div> without a <div>
     * ------------------------------------------------
     */

    public IAttoHandleResult handleXmlUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    public IAttoHandleResult handleXmlUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }






    /*
     * **************************************************************
     * *                                                            *
     * * IMPLEMENTATION OF IXmlAttributeSequenceHandling INTERFACE  *
     * *                                                            *
     * **************************************************************
     */


    public IAttoHandleResult handleXmlAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }




    public IAttoHandleResult handleXmlInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }



}