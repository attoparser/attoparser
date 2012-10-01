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

import java.util.LinkedHashMap;
import java.util.Map;

import org.attoparser.AttoParseException;







/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractStandardMarkupAttoHandler 
        extends AbstractDetailedMarkupAttoHandler
        implements IStandardMarkupHandling {

    
    private String currentElementName;
    private Map<String,String> currentElementAttributes;
    private int currentElementLine;
    private int currentElementCol;
    
    
    
    protected AbstractStandardMarkupAttoHandler() {
        super();
    }
    

    @Override
    public final void standaloneElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {
        
        super.standaloneElementStart(buffer, offset, len, line, col);
        
        this.currentElementName = null;
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;
        
    }
    
    

    @Override
    public final void standaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {

        super.standaloneElementName(buffer, offset, len, line, col);
        
        this.currentElementName = new String(buffer, offset, len);
        
    }

    
    
    @Override
    public final void standaloneElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        
        super.standaloneElementEnd(buffer, offset, len, line, col);
        
        standaloneElement(this.currentElementName, this.currentElementAttributes, this.currentElementLine, this.currentElementCol);
        
    }

    
    
    @Override
    public final void openElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.openElementStart(buffer, offset, len, line, col);

        this.currentElementName = null;
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;
        
    }
    
    

    @Override
    public final void openElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {

        super.openElementName(buffer, offset, len, line, col);
        
        this.currentElementName = new String(buffer, offset, len);
        
    }

    
    
    @Override
    public final void openElementEnd(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.openElementEnd(buffer, offset, len, line, col);
        
        openElement(this.currentElementName, this.currentElementAttributes, this.currentElementLine, this.currentElementCol);
        
    }

    
    
    @Override
    public final void closeElementStart(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.closeElementStart(buffer, offset, len, line, col);
        
        this.currentElementName = null;
        this.currentElementAttributes = null;
        this.currentElementLine = line;
        this.currentElementCol = col;
        
    }
    
    

    @Override
    public final void closeElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {

        super.closeElementName(buffer, offset, len, line, col);
        
        this.currentElementName = new String(buffer, offset, len);
        
    }

    
    
    @Override
    public final void closeElementEnd(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {

        super.closeElementEnd(buffer, offset, len, line, col);

        closeElement(this.currentElementName, this.currentElementLine, this.currentElementCol);
        
    }

    
    
    @Override
    public final void attribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, 
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {

        super.attribute(buffer, nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                valueLine, valueCol);
        
        final String attributeName = new String(buffer, nameOffset, nameLen);
        final String attributeValue = 
                (valueContentLen <= 0?  "" : new String(buffer, valueContentOffset, valueContentLen));
        
        if (this.currentElementAttributes == null) {
            this.currentElementAttributes = new LinkedHashMap<String, String>(5, 1.0f);
        }

        this.currentElementAttributes.put(attributeName, attributeValue);
        
    }

    
    
    @Override
    public final void attributeSeparator(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.attributeSeparator(buffer, offset, len, line, col);
        
    }

    
    
    @Override
    public final void docType(
            final char[] buffer, 
            final int keywordOffset, final int keywordLen,
            final int keywordLine, final int keywordCol, 
            final int elementNameOffset, final int elementNameLen, 
            final int elementNameLine, final int elementNameCol,
            final int typeOffset, final int typeLen, 
            final int typeLine, final int typeCol,
            final int publicIdOffset, final int publicIdLen, 
            final int publicIdLine, final int publicIdCol, 
            final int systemIdOffset, final int systemIdLen,
            final int systemIdLine, final int systemIdCol, 
            final int internalSubsetOffset, final int internalSubsetLen,
            final int internalSubsetLine, final int internalSubsetCol,
            final int outerOffset, final int outerLen,
            final int outerLine, final int outerCol)
            throws AttoParseException {

        super.docType(buffer, keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                typeOffset, typeLen, typeLine, typeCol, publicIdOffset, publicIdLen,
                publicIdLine, publicIdCol, systemIdOffset, systemIdLen, systemIdLine,
                systemIdCol, internalSubsetOffset, internalSubsetLen, internalSubsetLine, 
                internalSubsetCol, outerOffset, outerLen, outerLine, outerCol);
        
        docType(
                new String(buffer, elementNameOffset, elementNameLen),
                (publicIdLen <= 0? null : new String(buffer, publicIdOffset, publicIdLen)),
                (systemIdLen <= 0? null : new String(buffer, systemIdOffset, systemIdLen)),
                (internalSubsetLen <= 0? null : new String(buffer, internalSubsetOffset, internalSubsetLen)),
                outerLine, outerCol);
        
    }


    
    @Override
    public final void comment(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        super.comment(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        
        comment(buffer, contentOffset, contentLen, line, col);
        
    }


    
    @Override
    public final void cdata(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        super.cdata(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        
        cdata(buffer, contentOffset, contentLen, line, col);
        
    }
    
    

    @Override
    public final void xmlDeclaration(
            final char[] buffer, 
            final int keywordOffset, final int keywordLen,
            final int keywordLine, final int keywordCol,
            final int versionOffset, final int versionLen,
            final int versionLine, final int versionCol,
            final int encodingOffset, final int encodingLen,
            final int encodingLine, final int encodingCol,
            final int standaloneOffset, final int standaloneLen,
            final int standaloneLine, final int standaloneCol,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        super.xmlDeclaration(
                buffer, 
                keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol, 
                encodingOffset, encodingLen, encodingLine, encodingCol, 
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol, 
                outerOffset, outerLen, line, col);
        
        final String version = new String(buffer, versionOffset, versionLen);
        final String encoding = new String(buffer, encodingOffset, encodingLen);
        final String standalone = new String(buffer, standaloneOffset, standaloneLen);
        
        xmlDeclaration(version, encoding, standalone, line, col);
        
    }


    
    @Override
    public final void processingInstruction(
            final char[] buffer, 
            final int targetOffset, final int targetLen, 
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        super.processingInstruction(
                buffer, 
                targetOffset, targetLen, targetLine, targetCol, 
                contentOffset, contentLen, contentLine, contentCol,
                outerOffset, outerLen, line, col);
        
        processingInstruction(
                new String(buffer, targetOffset, targetLen), 
                new String(buffer, contentOffset, contentLen), 
                line, col);
        
    }


    
    
    
    public void standaloneElement(
            final String elementName, final Map<String,String> attributes,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    
    public void openElement(
            final String elementName, final Map<String,String> attributes,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    
    
    public void closeElement(
            final String elementName, final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    
    public void docType(
            final String elementName, final String publicId, final String systemId, 
            final String internalSubset, final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    
    
    public void comment(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    
    
    public void cdata(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void xmlDeclaration(
            final String version, 
            final String encoding, 
            final String standalone, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void processingInstruction(
            final String target, final String content, 
            final int line, final int col) 
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
}