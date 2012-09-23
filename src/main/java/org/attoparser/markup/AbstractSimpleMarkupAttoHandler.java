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
package org.attoparser.markup;

import org.attoparser.AttoParseException;







/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractSimpleMarkupAttoHandler 
        extends AbstractMarkupBreakDownAttoHandler {


    
    protected AbstractSimpleMarkupAttoHandler() {
        super();
    }

    @Override
    public void standaloneElementStart(char[] buffer, int offset, int len,
            int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.standaloneElementStart(buffer, offset, len, line, col);
    }

    @Override
    public void standaloneElementName(char[] buffer, int offset, int len,
            int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.standaloneElementName(buffer, offset, len, line, col);
    }

    @Override
    public void standaloneElementEnd(char[] buffer, int offset, int len,
            int line, int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.standaloneElementEnd(buffer, offset, len, line, col);
    }

    @Override
    public void openElementStart(char[] buffer, int offset, int len, int line,
            int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.openElementStart(buffer, offset, len, line, col);
    }

    @Override
    public void openElementName(char[] buffer, int offset, int len, int line,
            int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.openElementName(buffer, offset, len, line, col);
    }

    @Override
    public void openElementEnd(char[] buffer, int offset, int len, int line,
            int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.openElementEnd(buffer, offset, len, line, col);
    }

    @Override
    public void closeElementStart(char[] buffer, int offset, int len, int line,
            int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.closeElementStart(buffer, offset, len, line, col);
    }

    @Override
    public void closeElementName(char[] buffer, int offset, int len, int line,
            int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.closeElementName(buffer, offset, len, line, col);
    }

    @Override
    public void closeElementEnd(char[] buffer, int offset, int len, int line,
            int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.closeElementEnd(buffer, offset, len, line, col);
    }

    @Override
    public void elementAttribute(char[] buffer, int nameOffset, int nameLen,
            int nameLine, int nameCol, int operatorOffset, int operatorLen,
            int operatorLine, int operatorCol, int valueContentOffset,
            int valueContentLen, int valueOuterOffset, int valueOuterLen,
            int valueLine, int valueCol) throws AttoParseException {
        // TODO Auto-generated method stub
        super.elementAttribute(buffer, nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                valueLine, valueCol);
    }

    @Override
    public void elementWhitespace(char[] buffer, int offset, int len, int line,
            int col) throws AttoParseException {
        // TODO Auto-generated method stub
        super.elementWhitespace(buffer, offset, len, line, col);
    }

    @Override
    public void docType(char[] buffer, int keywordOffset, int keywordLen,
            int keywordLine, int keywordCol, int elementNameOffset,
            int elementNameLen, int elementNameLine, int elementNameCol,
            int typeOffset, int typeLen, int typeLine, int typeCol,
            int publicIdOffset, int publicIdLen, int publicIdLine,
            int publicIdCol, int systemIdOffset, int systemIdLen,
            int systemIdLine, int systemIdCol, int outerOffset, int outerLen,
            int outerLine, int outerCol) throws AttoParseException {
        // TODO Auto-generated method stub
        super.docType(buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                typeOffset, typeLen, typeLine, typeCol, publicIdOffset, publicIdLen,
                publicIdLine, publicIdCol, systemIdOffset, systemIdLen, systemIdLine,
                systemIdCol, outerOffset, outerLen, outerLine, outerCol);
    }





    
}