/*
 * =============================================================================
 *
 *   Copyright (c) 2011-2014, The THYMELEAF team (http://www.thymeleaf.org)
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
package org.attoparser.select;

import org.attoparser.IMarkupHandler;
import org.attoparser.ParseException;

/**
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public class AllowingSelectedSelectorEventHandler implements ISelectedSelectorEventHandler {


    public AllowingSelectedSelectorEventHandler() {
        super();
    }


    public void handleSelectedXmlDeclaration(
            final String[] selectors, final boolean[] selectorMatches,
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
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleXmlDeclaration(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol,
                encodingOffset, encodingLen, encodingLine, encodingCol,
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen, line, col);
    }

    public void handleSelectedDocTypeClause(
            final String[] selectors, final boolean[] selectorMatches,
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
            final int outerLine, final int outerCol,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleDocType(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                typeOffset, typeLen, typeLine, typeCol,
                publicIdOffset, publicIdLen, publicIdLine, publicIdCol,
                systemIdOffset, systemIdLen, systemIdLine, systemIdCol,
                internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol,
                outerOffset, outerLen, outerLine, outerCol);
    }

    public void handleSelectedCDATASection(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
    }

    public void handleSelectedText(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleText(buffer, offset, len, line, col);
    }

    public void handleSelectedComment(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleComment(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
    }

    public void handleSelectedAttribute(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);
    }

    public void handleSelectedStandaloneElementStart(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);
    }

    public void handleSelectedStandaloneElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);
    }

    public void handleSelectedOpenElementStart(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);
    }

    public void handleSelectedOpenElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);
    }

    public void handleSelectedCloseElementStart(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }

    public void handleSelectedCloseElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);
    }

    public void handleSelectedAutoCloseElementStart(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }

    public void handleSelectedAutoCloseElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col);
    }

    public void handleSelectedUnmatchedCloseElementStart(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);
    }

    public void handleSelectedUnmatchedCloseElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
    }

    public void handleSelectedElementInnerWhiteSpace(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleInnerWhiteSpace(buffer, offset, len, line, col);
    }

    public void handleSelectedProcessingInstruction(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int targetOffset, final int targetLen,
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        handler.handleProcessingInstruction(
                buffer,
                targetOffset, targetLen, targetLine, targetCol,
                contentOffset, contentLen, contentLine, contentCol,
                outerOffset, outerLen, line, col);
    }


}
