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
package org.attoparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.attoparser.html.HtmlNames;
import org.attoparser.util.TextUtil;


/**
 *
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
final class MarkupEventProcessor implements IMarkupEventAttributeProcessor {


    private static final int DEFAULT_STACK_LEN = 10;
    private static final int DEFAULT_ATTRIBUTE_NAMES_LEN = 3;

    private final IMarkupAttoHandler handler;

    private final boolean useStack;

    private final boolean autoClose;
    private final boolean requireBalancedElements;
    private final boolean requireNoUnmatchedCloseElements;

    private final MarkupParsingConfiguration.PrologParsingConfiguration prologParsingConfiguration;
    private final MarkupParsingConfiguration.UniqueRootElementPresence uniqueRootElementPresence;

    private final boolean caseSensitive;

    private final boolean requireWellFormedAttributeValues;
    private final boolean requireUniqueAttributesInElement;

    private final boolean validateProlog;
    private final boolean prologPresenceForbidden;
    private final boolean xmlDeclarationPresenceForbidden;
    private final boolean doctypePresenceForbidden;

    // Will be used as an element name cache in order to avoid creating a new
    // char[] object each time an element is pushed into the stack or an attribute
    // is processed to check its uniqueness.
    private final StructureNamesRepository structureNamesRepository;

    private char[][] elementStack;
    private int elementStackSize;

    private boolean validPrologXmlDeclarationRead = false;
    private boolean validPrologDocTypeRead = false;
    private boolean elementRead = false;
    private char[] rootElementName = null;
    private char[][] currentElementAttributeNames = null;
    private int currentElementAttributeNamesSize = 0;


    private boolean closeElementIsMatched = true;


    MarkupEventProcessor(final IMarkupAttoHandler handler, final MarkupParsingConfiguration markupParsingConfiguration) {

        super();

        this.handler = handler;

        this.caseSensitive = markupParsingConfiguration.isCaseSensitive();

        this.useStack = (!MarkupParsingConfiguration.ElementBalancing.NO_BALANCING.equals(markupParsingConfiguration.getElementBalancing()) ||
                         markupParsingConfiguration.getRequireUniqueAttributesInElement() ||
                         !MarkupParsingConfiguration.UniqueRootElementPresence.NOT_VALIDATED.equals(markupParsingConfiguration.getUniqueRootElementPresence()));

        this.autoClose =
                (MarkupParsingConfiguration.ElementBalancing.AUTO_CLOSE.equals(markupParsingConfiguration.getElementBalancing()) ||
                        MarkupParsingConfiguration.ElementBalancing.AUTO_CLOSE_REQUIRE_NO_UNMATCHED_CLOSE.equals(markupParsingConfiguration.getElementBalancing()));
        this.requireBalancedElements =
                MarkupParsingConfiguration.ElementBalancing.REQUIRE_BALANCED.equals(markupParsingConfiguration.getElementBalancing());
        this.requireNoUnmatchedCloseElements =
                (this.requireBalancedElements ||
                        MarkupParsingConfiguration.ElementBalancing.AUTO_CLOSE_REQUIRE_NO_UNMATCHED_CLOSE.equals(markupParsingConfiguration.getElementBalancing()) ||
                        MarkupParsingConfiguration.ElementBalancing.REQUIRE_NO_UNMATCHED_CLOSE.equals(markupParsingConfiguration.getElementBalancing()));

        this.prologParsingConfiguration = markupParsingConfiguration.getPrologParsingConfiguration();

        this.prologParsingConfiguration.validateConfiguration();

        this.uniqueRootElementPresence = markupParsingConfiguration.getUniqueRootElementPresence();
        this.requireWellFormedAttributeValues = markupParsingConfiguration.getRequireXmlWellFormedAttributeValues();
        this.requireUniqueAttributesInElement = markupParsingConfiguration.getRequireUniqueAttributesInElement();

        this.validateProlog = this.prologParsingConfiguration.isValidateProlog();
        this.prologPresenceForbidden = this.prologParsingConfiguration.getPrologPresence().isForbidden();
        this.xmlDeclarationPresenceForbidden = this.prologParsingConfiguration.getXmlDeclarationPresence().isRequired();
        this.doctypePresenceForbidden = this.prologParsingConfiguration.getDoctypePresence().isRequired();

        if (this.useStack) {

            this.elementStack = new char[DEFAULT_STACK_LEN][];
            this.elementStackSize = 0;

            this.structureNamesRepository = new StructureNamesRepository();

        } else {

            this.elementStack = null;
            this.elementStackSize = 0;
            this.structureNamesRepository = null;

        }

    }




    IAttoHandleResult processDocumentStart(final long startTimeNanos, final int line, final int col)
            throws AttoParseException {
        return this.handler.handleDocumentStart(startTimeNanos, line, col);
    }



    IAttoHandleResult processDocumentEnd(final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws AttoParseException {

        if (this.requireBalancedElements && this.elementStackSize > 0) {
            final char[] popped = popFromStack();
            throw new AttoParseException(
                "Malformed markup: element " +
                "\"" + new String(popped, 0, popped.length) + "\"" +
                " is never closed (no closing tag at the end of document)");
        }

        if (!this.elementRead && (
                (this.validPrologDocTypeRead && this.uniqueRootElementPresence.isDependsOnPrologDoctype()) ||
                this.uniqueRootElementPresence.isRequiredAlways())) {
            throw new AttoParseException(
                    "Malformed markup: no root element present");
        }

        if (this.useStack) {
            cleanStack(line, col);
        }

        return this.handler.handleDocumentEnd(endTimeNanos, totalTimeNanos, line, col);

    }



    IAttoHandleResult processCDATASection(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {
        return this.handler.handleCDATASection(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
    }




    IAttoHandleResult processComment(
            final char[] buffer,
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {
        return this.handler.handleComment(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
    }




    IAttoHandleResult processText(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        return this.handler.handleText(buffer, offset, len, line, col);
    }




    IAttoHandleResult processProcessingInstruction(
            final char[] buffer,
            final int targetOffset, final int targetLen,
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {
        return this.handler.handleProcessingInstruction(
                buffer,
                targetOffset, targetLen, targetLine, targetCol,
                contentOffset, contentLen, contentLine, contentCol,
                outerOffset, outerLen, line, col);
    }




    IAttoHandleResult processXmlDeclaration(
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

        if (this.validateProlog && (this.prologPresenceForbidden || this.xmlDeclarationPresenceForbidden)) {
            throw new AttoParseException(
                    "An XML Declaration has been found, but it wasn't allowed",
                    line, col);
        }

        if (this.validateProlog) {

            if (this.validPrologXmlDeclarationRead) {
                throw new AttoParseException(
                        "Malformed markup: Only one XML Declaration can appear in document",
                        line, col);
            }
            if (this.validPrologDocTypeRead) {
                throw new AttoParseException(
                        "Malformed markup: XML Declaration must appear before DOCTYPE",
                        line, col);
            }
            if (this.elementRead) {
                throw new AttoParseException(
                        "Malformed markup: XML Declaration must appear before any " +
                        "elements in document",
                        line, col);
            }

        }

        final IAttoHandleResult result =
            this.handler.handleXmlDeclaration(buffer, keywordOffset, keywordLen, keywordLine,
                    keywordCol, versionOffset, versionLen, versionLine, versionCol,
                    encodingOffset, encodingLen, encodingLine, encodingCol,
                    standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                    outerOffset, outerLen, line, col);

        if (this.validateProlog) {
            this.validPrologXmlDeclarationRead = true;
        }

        return result;

    }


    IAttoHandleResult processStandaloneElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col)
            throws AttoParseException {

        if (this.useStack) {

            if (this.elementStackSize == 0) {
                checkValidRootElement(buffer, nameOffset, nameLen, line, col);
            }

            if (this.requireUniqueAttributesInElement) {
                this.currentElementAttributeNames = null;
                this.currentElementAttributeNamesSize = 0;
            }

            // This is a standalone element, no need to put into stack

        }

        /*
         * Prepare for element (maybe perform stack operations)
         */
        final IElementPreparationResult preparationResult =
                this.handler.prepareForElement(buffer, nameOffset, nameLen, line, col);
        if (preparationResult != null) {

            if (this.useStack) {

                if (preparationResult.getUnstackElements() != null) {
                    // We should first rearrange the stack
                    unstack(preparationResult.getUnstackElements(), preparationResult.getUnstackLimits(), line, col);
                }

                if (preparationResult.getShouldStack() != null && preparationResult.getShouldStack().booleanValue()) {
                    // We will NOT push if null, because pushing is NOT the default behaviour for standalone elements
                    pushToStack(buffer, nameOffset, nameLen);
                }

            }

        }

        return this.handler.handleStandaloneElementStart(buffer, nameOffset, nameLen, minimized, line, col);

    }

    IAttoHandleResult processStandaloneElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final boolean minimized,
            final int line, final int col)
            throws AttoParseException {

        final IAttoHandleResult result =
            this.handler.handleStandaloneElementEnd(buffer, nameOffset, nameLen, minimized, line, col);
        this.elementRead = true;
        return result;

    }


    IAttoHandleResult processOpenElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {

        if (this.useStack) {

            if (this.elementStackSize == 0) {
                checkValidRootElement(buffer, nameOffset, nameLen, line, col);
            }

            if (this.requireUniqueAttributesInElement) {
                this.currentElementAttributeNames = null;
                this.currentElementAttributeNamesSize = 0;
            }

        }

        /*
         * Prepare for element (maybe perform stack operations)
         */
        final IElementPreparationResult preparationResult =
                this.handler.prepareForElement(buffer, nameOffset, nameLen, line, col);
        if (preparationResult != null) {

            if (this.useStack) {

                if (preparationResult.getUnstackElements() != null) {
                    // We should first rearrange the stack
                    unstack(preparationResult.getUnstackElements(), preparationResult.getUnstackLimits(), line, col);
                }

                if (preparationResult.getShouldStack() == null || preparationResult.getShouldStack().booleanValue()) {
                    // We will push if null, because it is the default behaviour for open elements
                    pushToStack(buffer, nameOffset, nameLen);
                }

            }

        } else if (this.useStack) {

            // Pushing into the stack is the default behaviour for open elements
            pushToStack(buffer, nameOffset, nameLen);

        }

        /*
         * Actually perform the handling of the open element start
         */
        return this.handler.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);

    }

    IAttoHandleResult processOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {

        final IAttoHandleResult result =
            this.handler.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);

        this.elementRead = true;

        return result;

    }


    IAttoHandleResult processCloseElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {

        if (this.useStack) {

            this.closeElementIsMatched =
                    checkStackForElement(buffer, nameOffset, nameLen, line, col);

            if (this.requireUniqueAttributesInElement) {
                this.currentElementAttributeNames = null;
                this.currentElementAttributeNamesSize = 0;
            }

            if (this.closeElementIsMatched) {
                return this.handler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);
            } else {
                return this.handler.handleUnmatchedCloseElementStart(buffer, nameOffset, nameLen, line, col);
            }

        } else {

            return this.handler.handleCloseElementStart(buffer, nameOffset, nameLen, line, col);

        }


    }

    IAttoHandleResult processCloseElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col)
            throws AttoParseException {

        final IAttoHandleResult result;
        if (!this.useStack || this.closeElementIsMatched) {
            result = this.handler.handleCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        } else {
            result = this.handler.handleUnmatchedCloseElementEnd(buffer, nameOffset, nameLen, line, col);
        }

        this.elementRead = true;

        return result;

    }


    public IAttoHandleResult processAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol)
            throws AttoParseException {

        if (this.useStack && this.requireUniqueAttributesInElement) {

            // Check attribute name is unique in this element
            if (this.currentElementAttributeNames == null) {
                // we only create this structure if there is at least one attribute
                this.currentElementAttributeNames = new char[DEFAULT_ATTRIBUTE_NAMES_LEN][];
            }
            for (int i = 0; i < this.currentElementAttributeNamesSize; i++) {

                if (TextUtil.equals(
                        this.caseSensitive,
                        this.currentElementAttributeNames[i], 0, this.currentElementAttributeNames[i].length,
                        buffer, nameOffset, nameLen)) {

                    throw new AttoParseException(
                            "Malformed markup: Attribute \"" + new String(buffer, nameOffset, nameLen) + "\" " +
                            "appears more than once in element",
                            nameLine, nameCol);

                }

            }
            if (this.currentElementAttributeNamesSize == this.currentElementAttributeNames.length) {
                // we need to grow the array!
                final char[][] newCurrentElementAttributeNames = new char[this.currentElementAttributeNames.length + DEFAULT_ATTRIBUTE_NAMES_LEN][];
                System.arraycopy(this.currentElementAttributeNames, 0, newCurrentElementAttributeNames, 0, this.currentElementAttributeNames.length);
                this.currentElementAttributeNames = newCurrentElementAttributeNames;
            }

            this.currentElementAttributeNames[this.currentElementAttributeNamesSize] =
                    this.structureNamesRepository.getStructureName(buffer, nameOffset, nameLen);

            this.currentElementAttributeNamesSize++;

        }


        if (this.requireWellFormedAttributeValues) {

            // Check there is an operator
            if (operatorLen == 0)  {
                throw new AttoParseException(
                        "Malformed markup: Attribute \"" + new String(buffer, nameOffset, nameLen) + "\" " +
                        "must include an equals (=) sign and a value surrounded by quotes",
                        operatorLine, operatorCol);
            }


            // Check attribute is surrounded by commas (double or single)
            if (valueOuterLen == 0 || valueOuterLen == valueContentLen)  {
                throw new AttoParseException(
                        "Malformed markup: Value for attribute \"" + new String(buffer, nameOffset, nameLen) + "\" " +
                        "must be surrounded by quotes",
                        valueLine, valueCol);
            }

        }

        return this.handler.handleAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);

    }



    public IAttoHandleResult processInnerWhiteSpace(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {

        return this.handler.handleInnerWhiteSpace(buffer, offset, len, line, col);

    }



    IAttoHandleResult processDocType(
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

        if (this.validateProlog) {

            if (this.prologPresenceForbidden || this.doctypePresenceForbidden) {
                throw new AttoParseException(
                        "A DOCTYPE clause has been found, but it wasn't allowed",
                        outerLine, outerCol);
            }

            if (this.validPrologDocTypeRead) {
                throw new AttoParseException(
                        "Malformed markup: Only one DOCTYPE clause can appear in document",
                        outerLine, outerCol);
            }

            if (this.elementRead) {
                throw new AttoParseException(
                        "Malformed markup: DOCTYPE must appear before any " +
                        "elements in document",
                        outerLine, outerCol);
            }

            if (this.prologParsingConfiguration.isRequireDoctypeKeywordsUpperCase()) {

                if (keywordLen > 0) {
                    final int maxi = keywordOffset + keywordLen;
                    for (int i = keywordOffset; i < maxi; i++) {
                        if (Character.isLowerCase(buffer[i])) {
                            throw new AttoParseException(
                                    "Malformed markup: DOCTYPE requires upper-case " +
                                    "keywords (\"" + new String(buffer, keywordOffset, keywordLen) + "\" was found)",
                                    outerLine, outerCol);
                        }
                    }
                }

                if (typeLen > 0) {
                    final int maxi = typeOffset + typeLen;
                    for (int i = typeOffset; i < maxi; i++) {
                        if (Character.isLowerCase(buffer[i])) {
                            throw new AttoParseException(
                                    "Malformed markup: DOCTYPE requires upper-case " +
                                    "keywords (\"" + new String(buffer, typeOffset, typeLen) + "\" was found)",
                                    outerLine, outerCol);
                        }
                    }
                }

            }

        }

        if (this.useStack) {

            this.rootElementName =
                    this.structureNamesRepository.getStructureName(buffer, elementNameOffset, elementNameLen);

        }

        final IAttoHandleResult result =
            this.handler.handleDocType(
                    buffer,
                    keywordOffset, keywordLen, keywordLine, keywordCol,
                    elementNameOffset, elementNameLen, elementNameLine, elementNameCol,
                    typeOffset, typeLen, typeLine, typeCol,
                    publicIdOffset, publicIdLen, publicIdLine, publicIdCol,
                    systemIdOffset, systemIdLen, systemIdLine, systemIdCol,
                    internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol,
                    outerOffset, outerLen, outerLine, outerCol);

        if (this.validateProlog) {
            this.validPrologDocTypeRead = true;
        }

        return result;

    }





    private void checkValidRootElement(
            final char[] buffer, final int offset, final int len, final int line, final int col)
            throws AttoParseException {

        if (!this.validateProlog) {

            if (this.elementRead && this.uniqueRootElementPresence.isRequiredAlways()) {
                // We are not validating the prolog, but anyway we required only one element root
                // and it seems there are several.
                throw new AttoParseException(
                        "Malformed markup: Only one root element is allowed",
                        line, col);
            }

            // Nothing else to check.
            return;

        }

        // We don't need to check the possibility of having parsed forbidden XML Decl or DOCTYPE
        // because this has already been checked when the corresponding events were triggered.

        if (this.validPrologDocTypeRead) {

            if (this.elementRead) {
                // If we have a DOCTYPE, we will have a root element name and therefore we will
                // only allow one root element. But it seems there are several.
                throw new AttoParseException(
                        "Malformed markup: Only one root element (with name \"" + new String(this.rootElementName) + "\" is allowed",
                        line, col);
            }

            if (!TextUtil.equals(this.caseSensitive, this.rootElementName, 0, this.rootElementName.length, buffer, offset, len)) {
                throw new AttoParseException(
                    "Malformed markup: Root element should be \"" + new String(this.rootElementName) + "\", " +
                    "but \"" + new String(buffer, offset, len) + "\" has been found",
                    line, col);
            }

        }

    }



    private boolean checkStackForElement(
            final char[] buffer, final int offset, final int len, final int line, final int col)
            throws AttoParseException {

        int peekDelta = 0;
        char[] peek = peekFromStack(peekDelta);

        while (peek != null) {

            if (TextUtil.equals(this.caseSensitive, peek, 0, peek.length, buffer, offset, len)) {

                // We found the corresponding opening element, so we execute all pending auto-close events
                // (if needed) and return true (meaning the close element has a matching open element).

                for (int i = 0; i < peekDelta; i++) {
                    peek = popFromStack();
                    if (this.autoClose) {
                        this.handler.handleAutoCloseElementStart(peek, 0, peek.length, line, col);
                        this.handler.handleAutoCloseElementEnd(peek, 0, peek.length, line, col);
                    }
                }
                popFromStack();

                return true;

            }

            // does not match...

            if (this.requireBalancedElements) {
                throw new AttoParseException(
                        "Malformed markup: element " +
                        "\"" + new String(peek, 0, peek.length) + "\"" +
                        " is never closed", line, col);
            }

            peek = peekFromStack(++peekDelta);

        }

        // closing element at the root level
        if (this.requireNoUnmatchedCloseElements) {
            throw new AttoParseException(
                    "Malformed markup: closing element " +
                    "\"" + new String(buffer, offset, len) + "\"" +
                    " is never open", line, col);
        }

        // Return false because the close element has no matching open element
        return false;

    }




    private void cleanStack(final int line, final int col)
            throws AttoParseException {

        if (this.elementStackSize > 0) {

            // When we arrive here we know that "requireBalancedElements" is
            // false. If it were true, an exception would have been raised before.

            char[] popped = popFromStack();

            while (popped != null) {

                if (this.autoClose) {
                    this.handler.handleAutoCloseElementStart(popped, 0, popped.length, line, col);
                    this.handler.handleAutoCloseElementEnd(popped, 0, popped.length, line, col);
                }

                popped = popFromStack();

            }

        }

    }



    private void unstack(
            final char[][] unstackElements, final char[][] unstackLimits, final int line, final int col)
            throws AttoParseException {

        int peekDelta = 0;
        int unstackCount = 0;
        char[] peek = peekFromStack(peekDelta);

        while (peek != null) {

            if (unstackLimits != null) {
                // First check whether we found a limit
                for (final char[] unstackLimit : unstackLimits) {
                    if (TextUtil.equals(this.caseSensitive, unstackLimit, peek)) {
                        // Just found a limit, we should stop computing unstacking here
                        peek = null; // This will make us exit the loop
                        break;
                    }
                }
            }

            if (peek != null) {

                // Check whether this is an element we must close
                for (final char[] unstackElement : unstackElements) {
                    if (TextUtil.equals(this.caseSensitive, unstackElement, peek)) {
                        // This is an element we must unstack, so we should mark unstackCount
                        unstackCount = peekDelta + 1;
                        break;
                    }
                }

                // Feed the loop
                peek = peekFromStack(++peekDelta);

            }

        }


        for (int i = 0; i < unstackCount; i++) {

            peek = popFromStack();

            if (this.requireBalancedElements) {
                throw new AttoParseException(
                        "Malformed markup: element " +
                                "\"" + new String(peek, 0, peek.length) + "\"" +
                                " is not closed where it should be", line, col);
            }

            if (this.autoClose) {
                this.handler.handleAutoCloseElementStart(peek, 0, peek.length, line, col);
                this.handler.handleAutoCloseElementEnd(peek, 0, peek.length, line, col);
            }

        }

    }



    private void pushToStack(
            final char[] buffer, final int offset, final int len) {

        if (this.elementStackSize == this.elementStack.length) {
            growStack();
        }


        this.elementStack[this.elementStackSize] =
                this.structureNamesRepository.getStructureName(buffer, offset, len);

        this.elementStackSize++;

    }


    private char[] peekFromStack(final int delta) {
        if (this.elementStackSize <= delta) {
            return null;
        }
        return this.elementStack[(this.elementStackSize - 1) - delta];
    }


    private char[] popFromStack() {
        if (this.elementStackSize == 0) {
            return null;
        }
        final char[] popped = this.elementStack[this.elementStackSize - 1];
        this.elementStack[this.elementStackSize - 1] = null;
        this.elementStackSize--;
        return popped;
    }


    private void growStack() {

        final int newStackLen = this.elementStack.length + DEFAULT_STACK_LEN;
        final char[][] newStack = new char[newStackLen][];
        System.arraycopy(this.elementStack, 0, newStack, 0, this.elementStack.length);
        this.elementStack = newStack;

    }







    /*
     *     This class is <strong>NOT thread-safe</strong>. Should only be used inside a specific handler
     *     instance/thread and only during a single execution.
     */
    static final class StructureNamesRepository {

        private final List<char[]> repository;


        StructureNamesRepository() {
            this.repository = new ArrayList<char[]>(5);
        }


        char[] getStructureName(final char[] text, final int offset, final int len) {

            final int index = binarySearch(this.repository, text, offset, len);

            if (index != -1) {
                return this.repository.get(index);
            }


            /*
             * NOT FOUND. We need to store the text
             */
            return storeStructureName(text, offset, len);

        }


        private char[] storeStructureName(final char[] text, final int offset, final int len) {

            final int index = binarySearch(this.repository, text, offset, len);
            if (index != -1) {
                // It was already added while we were waiting for the lock!
                return this.repository.get(index);
            }

            // We rely on the static structure name cache, just in case it is a standard HTML structure name.
            // Note the StandardNamesRepository will create the new char[] if not found, so no need to null-check.
            final char[] structureName = StandardNamesRepository.getStructureName(text, offset, len);

            this.repository.add(structureName);
            Collections.sort(this.repository, CharArrayComparator.INSTANCE);

            return structureName;

        }


        private static int binarySearch(final List<char[]> values,
                                        final char[] text, final int offset, final int len) {

            int low = 0;
            int high = values.size() - 1;

            while (low <= high) {

                final int mid = (low + high) >>> 1;
                final char[] midVal = values.get(mid);

                final int cmp = TextUtil.compareTo(true, midVal, 0, midVal.length, text, offset, len);

                if (cmp < 0) {
                    low = mid + 1;
                } else if (cmp > 0) {
                    high = mid - 1;
                } else {
                    // Found!!
                    return mid;
                }

            }

            return -1;  // Not Found!!

        }


        private static class CharArrayComparator implements Comparator<char[]> {

            private static CharArrayComparator INSTANCE = new CharArrayComparator();

            public int compare(final char[] o1, final char[] o2) {
                return TextUtil.compareTo(true, o1, o2);
            }
        }

    }




    /*
     *     This class is IMMUTABLE, and therefore thread-safe. Will be used in a static manner by all
     *     threads which require the use of a repository of standard names (HTML names, in this case).
     */
    static final class StandardNamesRepository {


        private static final char[][] REPOSITORY;


        static {

            final List<String> names = new ArrayList<String>();
            // Add all the standard HTML element (tag) names
            names.addAll(HtmlNames.ALL_STANDARD_ELEMENT_NAMES);
            // We know all standard element names are lowercase, so let's cache them uppercase too
            for (final String name : HtmlNames.ALL_STANDARD_ELEMENT_NAMES) {
                names.add(name.toUpperCase());
            }
            // Add all the standard HTML attribute names
            names.addAll(HtmlNames.ALL_STANDARD_ATTRIBUTE_NAMES);
            // We know all standard attribute names are lowercase, so let's cache them uppercase too
            for (final String name : HtmlNames.ALL_STANDARD_ATTRIBUTE_NAMES) {
                names.add(name.toUpperCase());
            }
            Collections.sort(names);

            REPOSITORY = new char[names.size()][];

            for (int i = 0; i < names.size(); i++) {
                final String name = names.get(i);
                REPOSITORY[i] = name.toCharArray();
            }

        }


        static char[] getStructureName(final char[] text, final int offset, final int len) {

            final int index = binarySearch(REPOSITORY, text, offset, len);

            if (index == -1) {
                final char[] structureName = new char[len];
                System.arraycopy(text, offset, structureName, 0, len);
                return structureName;
            }

            return REPOSITORY[index];

        }


        static int binarySearch(final char[][] values,
                                final char[] text, final int offset, final int len) {

            int low = 0;
            int high = values.length - 1;

            while (low <= high) {

                final int mid = (low + high) >>> 1;
                final char[] midVal = values[mid];

                final int cmp = TextUtil.compareTo(true, midVal, 0, midVal.length, text, offset, len);

                if (cmp < 0) {
                    low = mid + 1;
                } else if (cmp > 0) {
                    high = mid - 1;
                } else {
                    // Found!!
                    return mid;
                }

            }

            return -1;  // Not Found!!

        }

    }



}