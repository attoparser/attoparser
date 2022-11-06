/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2022, The ATTOPARSER team (https://www.attoparser.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       https://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.attoparser;


import org.attoparser.config.ParseConfiguration;
import org.attoparser.select.ParseSelection;

/*
 * Special implementation of the IMarkupHandler interface which implements the logic required to parse HTML
 * markup. It acts as a bridge and ends up delegating most of its events to another IMarkupHandler instance,
 * but resolving first the HTML elements that are being referenced in parsing events and allowing these elements
 * to introduce their inner logic in the parsing process; like e.g. letting VOID elements declare they are
 * 'standalone' elements even if they are specified as 'open' ones in markup code.
 * 
 * @author Daniel Fernandez
 * @since 2.0.0
 */
final class HtmlMarkupHandler extends AbstractMarkupHandler {

    private static final char[] HEAD_BUFFER = "head".toCharArray();
    private static final char[] BODY_BUFFER = "body".toCharArray();

    private final IMarkupHandler next;

    private ParseStatus status = null; // Will be always set, but anyway we should initialize.
    private boolean autoOpenEnabled = false;
    private boolean autoCloseEnabled = false;
    private HtmlElement currentElement = null;

    private int markupLevel = 0;

    private boolean htmlElementHandled = false;
    private boolean headElementHandled = false;
    private boolean bodyElementHandled = false;



    HtmlMarkupHandler(final IMarkupHandler next) {
        super();
        if (next == null) {
            throw new IllegalArgumentException("Chained handler cannot be null");
        }
        this.next = next;
    }



    @Override
    public void setParseStatus(final ParseStatus status) {
        // This will be ALWAYS called, so there is no need to actually check whether this property is null when using it
        this.status = status;
        this.next.setParseStatus(status);
    }



    @Override
    public void setParseSelection(final ParseSelection selection) {
        // This will be ALWAYS called, so there is no need to actually check whether this property is null when using it
        this.next.setParseSelection(selection);
    }



    @Override
    public void setParseConfiguration(final ParseConfiguration parseConfiguration) {

        this.autoOpenEnabled =
                ParseConfiguration.ElementBalancing.AUTO_OPEN_CLOSE == parseConfiguration.getElementBalancing();
        this.autoCloseEnabled =
                (ParseConfiguration.ElementBalancing.AUTO_OPEN_CLOSE == parseConfiguration.getElementBalancing() ||
                 ParseConfiguration.ElementBalancing.AUTO_CLOSE == parseConfiguration.getElementBalancing());

        this.next.setParseConfiguration(parseConfiguration);

    }




    @Override
    public void handleDocumentStart(
            final long startTimeNanos, final int line, final int col)
            throws ParseException {

        this.next.handleDocumentStart(startTimeNanos, line, col);

    }



    @Override
    public void handleDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws ParseException {

        this.next.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);

    }



    @Override
    public void handleXmlDeclaration(
            final char[] buffer,
            final int keywordOffset, final int keywordLen, final int keywordLine, final int keywordCol,
            final int versionOffset, final int versionLen, final int versionLine, final int versionCol,
            final int encodingOffset, final int encodingLen, final int encodingLine, final int encodingCol,
            final int standaloneOffset, final int standaloneLen, final int standaloneLine, final int standaloneCol,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        this.next.handleXmlDeclaration(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                versionOffset, versionLen, versionLine, versionCol,
                encodingOffset, encodingLen, encodingLine, encodingCol,
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen,
                line, col);

    }



    @Override
    public void handleDocType(
            final char[] buffer,
            final int keywordOffset, final int keywordLen, final int keywordLine, final int keywordCol,
            final int elementNameOffset, final int elementNameLen, final int elementNameLine, final int elementNameCol,
            final int typeOffset, final int typeLen, final int typeLine, final int typeCol,
            final int publicIdOffset, final int publicIdLen, final int publicIdLine, final int publicIdCol,
            final int systemIdOffset, final int systemIdLen, final int systemIdLine, final int systemIdCol,
            final int internalSubsetOffset, final int internalSubsetLen, final int internalSubsetLine, final int internalSubsetCol,
            final int outerOffset, final int outerLen,
            final int outerLine, final int outerCol)
            throws ParseException {

        this.next.handleDocType(
                buffer,
                keywordOffset, keywordLen, keywordLine, keywordCol,
                elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                typeOffset, typeLen, typeLine, typeCol,
                publicIdOffset, publicIdLen, publicIdLine, publicIdCol,
                systemIdOffset, systemIdLen, systemIdLine, systemIdCol,
                internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol,
                outerOffset, outerLen,
                outerLine, outerCol);

    }



    @Override
    public void handleCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        this.next.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);

    }



    @Override
    public void handleComment(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        this.next.handleComment(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);

    }



    @Override
    public void handleText(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        this.next.handleText(buffer, offset, len, line, col);

    }



    @Override
    public void handleStandaloneElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col)
            throws ParseException {
        
        this.currentElement = HtmlElements.forName(buffer, nameOffset, nameLen);
        this.currentElement.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);

    }

    @Override
    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized, final int line, final int col)
            throws ParseException {
        
        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final HtmlElement element = this.currentElement;
        this.currentElement = null;

        // Hoping for better days in which tail calls might be optimized ;)
        element.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);

    }


    
    
    @Override
    public void handleOpenElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col) 
            throws ParseException {
        
        this.currentElement = HtmlElements.forName(buffer, nameOffset, nameLen);

        if (this.autoOpenEnabled) {
            if (this.markupLevel == 0 && this.currentElement == HtmlElements.HTML) {
                this.htmlElementHandled = true;
            } else if (this.markupLevel == 1 && this.htmlElementHandled && this.currentElement == HtmlElements.HEAD) {
                this.headElementHandled = true;
            } else if (this.markupLevel == 1 && this.htmlElementHandled && this.currentElement == HtmlElements.BODY) {
                if (!this.headElementHandled) {
                    // No <head> element has been handled, we should add it automatically
                    final HtmlElement headElement = HtmlElements.forName(HEAD_BUFFER, 0, HEAD_BUFFER.length);
                    headElement.handleAutoOpenElementStart(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoOpenElementEnd(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementStart(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementEnd(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    this.headElementHandled = true;
                }
                this.bodyElementHandled = true;
            }
        }

        this.currentElement.handleOpenElementStart(buffer, nameOffset, nameLen, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);

    }

    @Override
    public void handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        this.markupLevel++;

        final HtmlElement element = this.currentElement;
        this.currentElement = null;

        // Hoping for better days in which tail calls might be optimized ;)
        element.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);

    }



    @Override
    public void handleAutoOpenElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.currentElement = HtmlElements.forName(buffer, nameOffset, nameLen);

        if (this.autoOpenEnabled) {
            if (this.markupLevel == 0 && this.currentElement == HtmlElements.HTML) {
                this.htmlElementHandled = true;
            } else if (this.markupLevel == 1 && this.htmlElementHandled && this.currentElement == HtmlElements.HEAD) {
                this.headElementHandled = true;
            } else if (this.markupLevel == 1 && this.htmlElementHandled && this.currentElement == HtmlElements.BODY) {
                if (!this.headElementHandled) {
                    // No <head> element has been handled, we should add it automatically
                    final HtmlElement headElement = HtmlElements.forName(HEAD_BUFFER, 0, HEAD_BUFFER.length);
                    headElement.handleAutoOpenElementStart(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoOpenElementEnd(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementStart(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementEnd(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    this.headElementHandled = true;
                }
                this.bodyElementHandled = true;
            }
        }

        this.currentElement.handleAutoOpenElementStart(buffer, nameOffset, nameLen, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);

    }

    @Override
    public void handleAutoOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        this.markupLevel++;

        final HtmlElement element = this.currentElement;
        this.currentElement = null;

        // Hoping for better days in which tail calls might be optimized ;)
        element.handleAutoOpenElementEnd(buffer, nameOffset, nameLen, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);

    }

    
    
    
    @Override
    public void handleCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col) 
            throws ParseException {

        this.markupLevel--;

        this.currentElement = HtmlElements.forName(buffer, nameOffset, nameLen);

        if (this.autoOpenEnabled) {
            if (this.markupLevel == 0 && this.htmlElementHandled && this.currentElement == HtmlElements.HTML) {
                if (!this.headElementHandled) {
                    // No <head> element has been handled, we should add it automatically
                    final HtmlElement headElement = HtmlElements.forName(HEAD_BUFFER, 0, HEAD_BUFFER.length);
                    headElement.handleAutoOpenElementStart(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoOpenElementEnd(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementStart(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementEnd(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    this.headElementHandled = true;
                }
                if (!this.bodyElementHandled) {
                    // No <body> element has been handled, we should add it automatically
                    final HtmlElement headElement = HtmlElements.forName(BODY_BUFFER, 0, BODY_BUFFER.length);
                    headElement.handleAutoOpenElementStart(BODY_BUFFER, 0, BODY_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoOpenElementEnd(BODY_BUFFER, 0, BODY_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementStart(BODY_BUFFER, 0, BODY_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementEnd(BODY_BUFFER, 0, BODY_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    this.bodyElementHandled = true;
                }
            }
        }

        this.currentElement.handleCloseElementStart(buffer, nameOffset, nameLen, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
        
    }

    @Override
    public void handleCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final HtmlElement element = this.currentElement;
        this.currentElement = null;

        // Hoping for better days in which tail calls might be optimized ;)
        element.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);

    }

    
    
    
    @Override
    public void handleAutoCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        this.markupLevel--;

        this.currentElement = HtmlElements.forName(buffer, nameOffset, nameLen);

        if (this.autoOpenEnabled) {
            if (this.markupLevel == 0 && this.htmlElementHandled && this.currentElement == HtmlElements.HTML) {
                if (!this.headElementHandled) {
                    // No <head> element has been handled, we should add it automatically
                    final HtmlElement headElement = HtmlElements.forName(HEAD_BUFFER, 0, HEAD_BUFFER.length);
                    headElement.handleAutoOpenElementStart(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoOpenElementEnd(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementStart(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementEnd(HEAD_BUFFER, 0, HEAD_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    this.headElementHandled = true;
                }
                if (!this.bodyElementHandled) {
                    // No <body> element has been handled, we should add it automatically
                    final HtmlElement headElement = HtmlElements.forName(BODY_BUFFER, 0, BODY_BUFFER.length);
                    headElement.handleAutoOpenElementStart(BODY_BUFFER, 0, BODY_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoOpenElementEnd(BODY_BUFFER, 0, BODY_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementStart(BODY_BUFFER, 0, BODY_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    headElement.handleAutoCloseElementEnd(BODY_BUFFER, 0, BODY_BUFFER.length, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
                    this.bodyElementHandled = true;
                }
            }
        }

        this.currentElement.handleAutoCloseElementStart(buffer, nameOffset, nameLen, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
        
    }

    @Override
    public void handleAutoCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final HtmlElement element = this.currentElement;
        this.currentElement = null;

        // Hoping for better days in which tail calls might be optimized ;)
        element.handleAutoCloseElementEnd(buffer, nameOffset, nameLen, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);

    }

    
    
    
    @Override
    public void handleUnmatchedCloseElementStart(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {
        
        this.currentElement = HtmlElements.forName(buffer, nameOffset, nameLen);
        this.currentElement.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
        
    }

    @Override
    public void handleUnmatchedCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot end element: no current element");
        }

        final HtmlElement element = this.currentElement;
        this.currentElement = null;

        // Hoping for better days in which tail calls might be optimized ;)
        element.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);

    }
    
    
    
    
    @Override
    public void handleAttribute(
            final char[] buffer, 
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol, 
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol, 
            final int valueContentOffset, final int valueContentLen, 
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot handle attribute: no current element");
        }
        
        this.currentElement.handleAttribute(buffer, nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen,
                valueLine, valueCol, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
        
    }




    @Override
    public void handleInnerWhiteSpace(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws ParseException {

        if (this.currentElement == null) {
            throw new IllegalStateException("Cannot handle attribute: no current element");
        }
        
        this.currentElement.handleInnerWhiteSpace(buffer, offset, len, line, col, this.next, this.status, this.autoOpenEnabled, this.autoCloseEnabled);
        
    }




    @Override
    public void handleProcessingInstruction(
            final char[] buffer,
            final int targetOffset, final int targetLen, final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen, final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws ParseException {

        this.next.handleProcessingInstruction(
                buffer,
                targetOffset, targetLen, targetLine, targetCol,
                contentOffset, contentLen, contentLine, contentCol,
                outerOffset, outerLen,
                line, col);

    }


}