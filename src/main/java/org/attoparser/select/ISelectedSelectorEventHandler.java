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
public interface ISelectedSelectorEventHandler {


    /*
     * ------------------------
     * XML Declaration events
     * ------------------------
     */

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
            throws ParseException;


    /*
     * ---------------------
     * DOCTYPE Clause events
     * ---------------------
     */

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
            throws ParseException;


    /*
     * --------------------
     * CDATA Section events
     * --------------------
     */

    public void handleSelectedCDATASection(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;


    /*
     * -----------
     * Text events
     * -----------
     */

    public void handleSelectedText(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;


    /*
     * --------------
     * Comment events
     * --------------
     */

    public void handleSelectedComment(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;


    /*
     * ----------------
     * Element handling
     * ----------------
     */

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
            throws ParseException;

    public void handleSelectedStandaloneElementStart(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;

    public void handleSelectedStandaloneElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;

    public void handleSelectedOpenElementStart(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;

    public void handleSelectedOpenElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;

    public void handleSelectedCloseElementStart(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;

    public void handleSelectedCloseElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;

    public void handleSelectedAutoCloseElementStart(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;

    public void handleSelectedAutoCloseElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;

    public void handleSelectedUnmatchedCloseElementStart(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;

    public void handleSelectedUnmatchedCloseElementEnd(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;

    public void handleSelectedElementInnerWhiteSpace(
            final String[] selectors, final boolean[] selectorMatches,
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col,
            final IMarkupHandler handler)
            throws ParseException;


    /*
     * -------------------------------
     * Processing Instruction handling
     * -------------------------------
     */

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
            throws ParseException;


}
