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
package org.attoparser.trace;

import java.util.Arrays;

import org.attoparser.AttoParseException;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.2
 *
 */
public final class MarkupParsingTraceEvent {

    public static enum Type {

        DOCUMENT_START("DS"), DOCUMENT_END("DE"),

        STANDALONE_ELEMENT_START("SES"), STANDALONE_ELEMENT_END("SEE"),
        NON_MINIMIZED_STANDALONE_ELEMENT_START("NSES"), NON_MINIMIZED_STANDALONE_ELEMENT_END("NSEE"),

        OPEN_ELEMENT_START("OES"), OPEN_ELEMENT_END("OEE"),

        CLOSE_ELEMENT_START("CES"), CLOSE_ELEMENT_END("CEE"),
        AUTO_CLOSE_ELEMENT_START("ACES"), AUTO_CLOSE_ELEMENT_END("ACEE"),
        UNMATCHED_CLOSE_ELEMENT_START("UCES"), UNMATCHED_CLOSE_ELEMENT_END("UCEE"),

        ATTRIBUTE("A"), INNER_WHITESPACE("IWS"),

        TEXT("T"), COMMENT("C"), CDATA_SECTION("CD"), XML_DECLARATION("XD"), DOC_TYPE("DT"), PROCESSING_INSTRUCTION("P");


        private String stringRepresentation;

        private Type(final String stringRepresentation) {
            this.stringRepresentation = stringRepresentation;
        }

        public String toString() {
            return this.stringRepresentation;
        }

    }


    private final Type type;
    private final String[] content;
    private final int[] lines;
    private final int[] cols;


    
    
    

    public static MarkupParsingTraceEvent forDocumentStart(
            final long startTimeNanos, final int line, final int col)
            throws AttoParseException {
        return new MarkupParsingTraceEvent(Type.DOCUMENT_START, new int[] {line}, new int[] {col}, String.valueOf(startTimeNanos));
    }

    public static MarkupParsingTraceEvent forDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, final int line, final int col)
            throws AttoParseException {
        return new MarkupParsingTraceEvent(Type.DOCUMENT_END, new int[] {line}, new int[] {col}, String.valueOf(endTimeNanos), String.valueOf(totalTimeNanos));
    }

    public static MarkupParsingTraceEvent forStandaloneElementStart(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.STANDALONE_ELEMENT_START, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forStandaloneElementEnd(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.STANDALONE_ELEMENT_END, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forNonMinimizedStandaloneElementStart(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.NON_MINIMIZED_STANDALONE_ELEMENT_START, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forNonMinimizedStandaloneElementEnd(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.NON_MINIMIZED_STANDALONE_ELEMENT_END, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forOpenElementStart(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.OPEN_ELEMENT_START, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forOpenElementEnd(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.OPEN_ELEMENT_END, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forCloseElementStart(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.CLOSE_ELEMENT_START, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forCloseElementEnd(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.CLOSE_ELEMENT_END, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forAutoCloseElementStart(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.AUTO_CLOSE_ELEMENT_START, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forAutoCloseElementEnd(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.AUTO_CLOSE_ELEMENT_END, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forUnmatchedCloseElementStart(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.UNMATCHED_CLOSE_ELEMENT_START, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forUnmatchedCloseElementEnd(
            final String elementName,
            final int line, final int col)
            throws AttoParseException {
        if (elementName == null || elementName.trim().equals("")) {
            throw new IllegalArgumentException("Element name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(Type.UNMATCHED_CLOSE_ELEMENT_END, new int[] {line}, new int[] {col}, elementName);
    }

    public static MarkupParsingTraceEvent forAttribute(
            final String name,
            final int nameLine, final int nameCol,
            final String operator,
            final int operatorLine, final int operatorCol,
            final String outerValue,
            final int valueLine, final int valueCol)
            throws AttoParseException {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Attribute name cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(
                Type.ATTRIBUTE, new int[] {nameLine, operatorLine, valueLine}, new int[] {nameCol, operatorCol, valueCol}, name, operator, outerValue);
    }

    public static MarkupParsingTraceEvent forText(
            final String content, final int line, final int col)
            throws AttoParseException {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        return new MarkupParsingTraceEvent(Type.TEXT, new int[] {line}, new int[] {col}, content);
    }

    public static MarkupParsingTraceEvent forComment(
            final String content,
            final int line, final int col)
            throws AttoParseException {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        return new MarkupParsingTraceEvent(Type.COMMENT, new int[] {line}, new int[] {col}, content);
    }

    public static MarkupParsingTraceEvent forCDATASection(
            final String content,
            final int line, final int col)
            throws AttoParseException {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        return new MarkupParsingTraceEvent(Type.CDATA_SECTION, new int[] {line}, new int[] {col}, content);
    }

    public static MarkupParsingTraceEvent forXmlDeclaration(
            final String keyword,
            final int keywordLine, final int keywordCol,
            final String version,
            final int versionLine, final int versionCol,
            final String encoding,
            final int encodingLine, final int encodingCol,
            final String standalone,
            final int standaloneLine, final int standaloneCol)
            throws AttoParseException {
        if (keyword == null || keyword.trim().equals("")) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(
                Type.XML_DECLARATION,
                new int[] {keywordLine, versionLine, encodingLine, standaloneLine},
                new int[] {keywordCol, versionCol, encodingCol, standaloneCol},
                keyword, version, encoding, standalone);
    }

    public static MarkupParsingTraceEvent forInnerWhiteSpace(
            final String content,
            final int line, final int col)
            throws AttoParseException {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        return new MarkupParsingTraceEvent(Type.INNER_WHITESPACE, new int[] {line}, new int[] {col}, content);
    }

    public static MarkupParsingTraceEvent forDocType(
            final String keyword,
            final int keywordLine, final int keywordCol,
            final String elementName,
            final int elementNameLine, final int elementNameCol,
            final String type,
            final int typeLine, final int typeCol,
            final String publicId,
            final int publicIdLine, final int publicIdCol,
            final String systemId,
            final int systemIdLine, final int systemIdCol,
            final String internalSubset,
            final int internalSubsetLine, final int internalSubsetCol)
            throws AttoParseException {
        if (keyword == null || keyword.trim().equals("")) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }
        return new MarkupParsingTraceEvent(
                Type.DOC_TYPE,
                new int[] {keywordLine, elementNameLine, typeLine, publicIdLine, systemIdLine, internalSubsetLine},
                new int[] {keywordCol, elementNameCol, typeCol, publicIdCol, systemIdCol, internalSubsetCol},
                keyword, elementName, type, publicId, systemId, internalSubset);
    }


    public static MarkupParsingTraceEvent forProcessingInstruction(
            final String target,
            final int targetLine, final int targetCol,
            final String content,
            final int contentLine, final int contentCol)
            throws AttoParseException {
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }
        return new MarkupParsingTraceEvent(
                Type.PROCESSING_INSTRUCTION,
                new int[] {targetLine, contentLine}, new int[] {targetCol, contentCol},
                target, content);
    }




    
    

    private MarkupParsingTraceEvent(final Type type, final int[] lines, final int[] cols, final String... content) {

        super();

        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        this.type = type;
        this.content = content;
        this.lines = lines;
        this.cols = cols;

    }


    public Type getType() {
        return this.type;
    }

    public boolean hasContent() {
        return this.content != null;
    }

    public String[] getContent() {
        return this.content;
    }

    public int[] getLines() {
        return this.lines;
    }

    public int[] getCols() {
        return this.cols;
    }




    @Override
    public String toString() {

        final StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(this.type);

        if (this.content != null && this.lines != null & this.lines.length == this.content.length) {

            for (int i = 0; i < this.content.length; i++) {
                strBuilder.append('(');
                if (this.content[i] != null) {
                    strBuilder.append(this.content[i]);
                }
                strBuilder.append(')');
                strBuilder.append('{');
                strBuilder.append(String.valueOf(this.lines[i]));
                strBuilder.append(',');
                strBuilder.append(String.valueOf(this.cols[i]));
                strBuilder.append('}');
            }
            
            return strBuilder.toString();
            
        }

        
        if (this.content != null) {
            for (final String contentItem : this.content) {
                strBuilder.append('(');
                if (contentItem != null) {
                    strBuilder.append(contentItem);
                }
                strBuilder.append(')');
            }
        }

        strBuilder.append('{');
        strBuilder.append(String.valueOf(this.lines[0]));
        strBuilder.append(',');
        strBuilder.append(String.valueOf(this.cols[0]));
        strBuilder.append('}');

        return strBuilder.toString();

    }





    boolean matchesTypeAndContent(final MarkupParsingTraceEvent event) {
        if (this == event) {
            return true;
        }
        if (event == null) {
            return false;
        }
        if (this.type == null) {
            if (event.type != null) {
                return false;
            }
        } else if (!this.type.equals(event.type)) {
            return false;
        }
        if (this.content == null) {
            if (event.content != null) {
                return false;
            }
        } else if (!Arrays.equals(this.content, event.content)) {
            return false;
        }
        return true;
    }




    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + Arrays.hashCode(content);
        result = 31 * result + Arrays.hashCode(lines);
        result = 31 * result + Arrays.hashCode(cols);
        return result;
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final MarkupParsingTraceEvent that = (MarkupParsingTraceEvent) o;

        if (!Arrays.equals(cols, that.cols)) {
            return false;
        }
        if (!Arrays.equals(content, that.content)) {
            return false;
        }
        if (!Arrays.equals(lines, that.lines)) {
            return false;
        }
        if (type != that.type) {
            return false;
        }

        return true;

    }


}