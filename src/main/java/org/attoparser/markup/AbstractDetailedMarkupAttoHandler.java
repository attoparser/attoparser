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
public abstract class AbstractDetailedMarkupAttoHandler 
        extends AbstractBasicMarkupAttoHandler
        implements IDetailedElementHandling, IDetailedDocTypeHandling {

    
    private final WellFormedWrapper wrapper;
    
    
    protected AbstractDetailedMarkupAttoHandler(final boolean requireWellFormed) {
        super();
        this.wrapper = new WellFormedWrapper(this, requireWellFormed);
    }


    
    @Override
    public final void handleDocumentStart(final long startTimeNanos)
            throws AttoParseException {
        super.handleDocumentStart(startTimeNanos);
        this.wrapper.handleDocumentStart(startTimeNanos);
    }


    @Override
    public final void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos)
            throws AttoParseException {
        super.handleDocumentEnd(endTimeNanos, totalTimeNanos);
        this.wrapper.handleDocumentEnd(endTimeNanos, totalTimeNanos);
    }
    

    
    @Override
    public final void handleXmlDeclaration(
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
        
        super.handleXmlDeclaration(buffer, keywordOffset, keywordLen, keywordLine,
                keywordCol, versionOffset, versionLen, versionLine, versionCol,
                encodingOffset, encodingLen, encodingLine, encodingCol,
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                outerOffset, outerLen, line, col);
        
        this.wrapper.handleXmlDeclaration(
                buffer, 
                keywordOffset, keywordLen, keywordLine, keywordCol, 
                versionOffset, versionLen, versionLine, versionCol, 
                encodingOffset, encodingLen, encodingLine, encodingCol, 
                standaloneOffset, standaloneLen, standaloneLine, standaloneCol, 
                outerOffset, outerLen, line, col);
        
    }



    @Override
    public final void handleDocType(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        
        super.handleDocType(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        DocTypeMarkupParsingUtil.parseDetailedDocType(
                buffer, outerOffset, outerLen, line, col, this.wrapper);
        
    }

    
    
    @Override
    public final void handleStandaloneElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleStandaloneElement(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        ElementMarkupParsingUtil.parseDetailedStandaloneElement(buffer, outerOffset, outerLen, line, col, this.wrapper);
        
    }

    
    @Override
    public final void handleOpenElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen, 
            final int outerOffset, final int outerLen, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleOpenElement(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        ElementMarkupParsingUtil.parseDetailedOpenElement(buffer, outerOffset, outerLen, line, col, this.wrapper);
        
    }

    
    @Override
    public final void handleCloseElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {

        super.handleCloseElement(buffer, contentOffset, contentLen, outerOffset, outerLen, line, col);
        ElementMarkupParsingUtil.parseDetailedCloseElement(buffer, outerOffset, outerLen, line, col, this.wrapper);

    }

    
    
    
    
    

    @SuppressWarnings("unused")
    public void handleDocumentStart(final long startTimeNanos, final boolean requireWellFormed)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    @SuppressWarnings("unused")
    public void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos, final boolean requireWellFormed)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    

    
    @SuppressWarnings("unused")
    public void handleDetailedXmlDeclaration(
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
        // Nothing to be done here, meant to be overridden if required
    }
    

    
    
    public void handleStandaloneElementStart(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleStandaloneElementName(
            final char[] buffer, 
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleStandaloneElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    

    public void handleOpenElementStart(
            final char[] buffer,
            final int offset,
            final int len, final int line,
            final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleOpenElementName(
            final char[] buffer,
            final int offset,
            final int len, final int line,
            final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleOpenElementEnd(
            final char[] buffer,
            final int offset,
            final int len, final int line,
            final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
    public void handleCloseElementStart(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleCloseElementName(
            final char[] buffer, 
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    public void handleCloseElementEnd(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
    public void handleAttribute(
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
    }


    
    public void handleAttributeSeparator(
            final char[] buffer,
            final int offset, final int len,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }



    public void handleDocType(
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
        // Nothing to be done here, meant to be overridden if required
    }

    


    
    
    
    static final class WellFormedWrapper
            implements IDetailedElementHandling, IDetailedDocTypeHandling {
        
        private final AbstractDetailedMarkupAttoHandler handler;
        
        private static final int DEFAULT_STACK_SIZE = 10;

        
        private final boolean requireWellFormed;
        
        private char[][] elementStack;
        private int elementStackSize;
        
        private boolean xmlDeclarationRead = false;
        private boolean docTypeRead = false;
        private boolean elementRead = false;
        
        
        
        WellFormedWrapper(final AbstractDetailedMarkupAttoHandler handler, final boolean requireWellFormed) {
            super();
            this.handler = handler;
            this.requireWellFormed = requireWellFormed;
            if (this.requireWellFormed) {
                this.elementStack = new char[DEFAULT_STACK_SIZE][];
                this.elementStackSize = 0;
            }
        }

        
        public void handleDocumentStart(final long startTimeNanos)
                throws AttoParseException {
            this.handler.handleDocumentStart(startTimeNanos, this.requireWellFormed);
        }


        public void handleDocumentEnd(final long endTimeNanos, final long totalTimeNanos)
                throws AttoParseException {
            
            if (this.requireWellFormed) {
                if (this.elementStackSize > 0) {
                    final char[] popped = popFromStack();
                    throw new AttoParseException(
                        "Malformed markup: element " +
                        "\"" + new String(popped, 0, popped.length) + "\"" +
                        " is never closed (no closing tag at the end of document)");
                }
            }
            
            this.handler.handleDocumentEnd(endTimeNanos, totalTimeNanos, this.requireWellFormed);
            
        }

        
        public final void handleXmlDeclaration(
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
            
            this.handler.handleDetailedXmlDeclaration(buffer, keywordOffset, keywordLen, keywordLine,
                    keywordCol, versionOffset, versionLen, versionLine, versionCol,
                    encodingOffset, encodingLen, encodingLine, encodingCol,
                    standaloneOffset, standaloneLen, standaloneLine, standaloneCol,
                    outerOffset, outerLen, line, col);
            
            if (this.requireWellFormed) {
                if (this.xmlDeclarationRead) {
                    throw new AttoParseException(
                            "Malformed markup: Only one XML Declaration can appear in document");
                }
                if (this.docTypeRead) {
                    throw new AttoParseException(
                            "Malformed markup: XML Declaration must appear before DOCTYPE");
                }
                if (this.elementRead) {
                    throw new AttoParseException(
                            "Malformed markup: XML Declaration must appear before any " +
                            "elements in document");
                }
            }
            
            this.xmlDeclarationRead = true;
            
        }
        

        public void handleStandaloneElementStart(
                final char[] buffer, 
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            this.handler.handleStandaloneElementStart(buffer, offset, len, line, col);
        }

        public void handleStandaloneElementName(
                final char[] buffer, 
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            this.handler.handleStandaloneElementName(buffer, offset, len, line, col);
        }

        public void handleStandaloneElementEnd(
                final char[] buffer,
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            this.handler.handleStandaloneElementEnd(buffer, offset, len, line, col);
            this.elementRead = true;
        }

        
        public void handleOpenElementStart(
                final char[] buffer,
                final int offset,
                final int len, final int line,
                final int col)
                throws AttoParseException {
            this.handler.handleOpenElementStart(buffer, offset, len, line, col);
        }

        public void handleOpenElementName(
                final char[] buffer,
                final int offset,
                final int len, final int line,
                final int col)
                throws AttoParseException {
            this.handler.handleOpenElementName(buffer, offset, len, line, col);
            if (this.requireWellFormed) {
                addToStack(buffer, offset, len);
            }
        }

        public void handleOpenElementEnd(
                final char[] buffer,
                final int offset,
                final int len, final int line,
                final int col)
                throws AttoParseException {
            this.handler.handleOpenElementEnd(buffer, offset, len, line, col);
            this.elementRead = true;
        }

        
        public void handleCloseElementStart(
                final char[] buffer,
                final int offset, final int len, 
                final int line, final int col)
                throws AttoParseException {
            this.handler.handleCloseElementStart(buffer, offset, len, line, col);
        }

        public void handleCloseElementName(
                final char[] buffer, 
                final int offset, final int len, 
                final int line, final int col)
                throws AttoParseException {
            if (this.requireWellFormed) {
                checkStackForElement(buffer, offset, len, line, col);
            }
            this.handler.handleCloseElementName(buffer, offset, len, line, col);
        }

        public void handleCloseElementEnd(
                final char[] buffer,
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            this.handler.handleCloseElementEnd(buffer, offset, len, line, col);
            this.elementRead = true;
        }

        
        public void handleAttribute(
                final char[] buffer,
                final int nameOffset, final int nameLen,
                final int nameLine, final int nameCol,
                final int operatorOffset, final int operatorLen,
                final int operatorLine, final int operatorCol,
                final int valueContentOffset, final int valueContentLen,
                final int valueOuterOffset, final int valueOuterLen,
                final int valueLine, final int valueCol)
                throws AttoParseException {
            this.handler.handleAttribute(
                    buffer, 
                    nameOffset, nameLen, nameLine, nameCol, 
                    operatorOffset, operatorLen, operatorLine, operatorCol, 
                    valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);
        }


        
        public void handleAttributeSeparator(
                final char[] buffer,
                final int offset, final int len,
                final int line, final int col)
                throws AttoParseException {
            this.handler.handleAttributeSeparator(buffer, offset, len, line, col);
        }



        public void handleDocType(
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
            
            this.handler.handleDocType(
                    buffer, 
                    keywordOffset, keywordLen, keywordLine, keywordCol, 
                    elementNameOffset, elementNameLen, elementNameLine, elementNameCol, 
                    typeOffset, typeLen, typeLine, typeCol, 
                    publicIdOffset, publicIdLen, publicIdLine, publicIdCol, 
                    systemIdOffset, systemIdLen, systemIdLine, systemIdCol, 
                    internalSubsetOffset, internalSubsetLen, internalSubsetLine, internalSubsetCol, 
                    outerOffset, outerLen, outerLine, outerCol);
            
            if (this.requireWellFormed) {
                if (this.docTypeRead) {
                    throw new AttoParseException(
                            "Malformed markup: Only one DOCTYPE clause can appear in document");
                }
                if (this.elementRead) {
                    throw new AttoParseException(
                            "Malformed markup: XML Declaration must appear before any " +
                            "elements in document");
                }
            }
            
            this.docTypeRead = true;
            
        }

        
        
        
        
        private void checkStackForElement(
                final char[] buffer, final int offset, final int len, final int line, final int col) 
                throws AttoParseException {
            
            final char[] popped = popFromStack();
            if (popped == null) {
                throw new AttoParseException(
                        "Malformed markup: closing element " +
                        "\"" + new String(buffer, offset, len) + "\"" +
                        " is never open", line, col);
            }

            boolean matches = true;
            
            if (len != popped.length) {
                matches = false;
            }
            
            final int maxi = offset + len;
            for (int i = offset; matches && i < maxi; i++) {
                if (buffer[i] != popped[i - offset]) {
                    matches = false;
                }
            }

            if (!matches) {
                throw new AttoParseException(
                        "Malformed markup: element " +
                        "\"" + new String(popped, 0, popped.length) + "\"" +
                        " is never closed", line, col);
            }
            
        }
        
        
        
        private void addToStack(
                final char[] buffer, final int offset, final int len) {
            
            if (this.elementStackSize == this.elementStack.length) {
                growStack();
            }
            
            this.elementStack[this.elementStackSize] = new char[len];
            System.arraycopy(buffer, offset, this.elementStack[this.elementStackSize], 0, len);
            
            this.elementStackSize++;
            
        }

        
        private char[] popFromStack() {
            
            if (this.elementStackSize == 0) {
                return null;
            }
            
            this.elementStackSize--;
            
            final char[] popped = this.elementStack[this.elementStackSize];
            this.elementStack[this.elementStackSize] = null;

            return popped;
            
        }
        
        
        private void growStack() {
            
            final int newStackSize = this.elementStack.length * 2;
            final char[][] newStack = new char[newStackSize][];
            
            for (int i = 0; i < this.elementStack.length; i++) {
                newStack[i] = this.elementStack[i];
            }
            
            this.elementStack = newStack;
            
        }
        
        
    }
    
    
    
    
}