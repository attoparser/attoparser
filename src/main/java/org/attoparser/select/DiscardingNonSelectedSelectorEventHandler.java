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
public class DiscardingNonSelectedSelectorEventHandler implements INonSelectedSelectorEventHandler {


    public DiscardingNonSelectedSelectorEventHandler() {
        super();
    }


    public void handleNonSelectedXmlDeclaration(
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
        // Nothing to do
    }

    public void handleNonSelectedDocTypeClause(
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
        // Nothing to do
    }

    public void handleNonSelectedCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedText(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedComment(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedAttribute(
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
        // Nothing to do
    }

    public void handleNonSelectedStandaloneElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedOpenElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedAutoCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedUnmatchedCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedElementInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

    public void handleNonSelectedProcessingInstruction(
            final char[] buffer,
            final int targetOffset, final int targetLen,
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException {
        // Nothing to do
    }

}
