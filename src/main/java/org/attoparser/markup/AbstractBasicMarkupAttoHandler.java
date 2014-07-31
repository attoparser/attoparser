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

import org.attoparser.AbstractAttoHandler;
import org.attoparser.AttoParseException;





/**
 * <p>
 *   Base abstract implementations for markup-specialized attohandlers that need differentiation
 *   among the different types of markup structures.
 * </p>
 * <p>
 *   This implementation differentiates among:
 * </p>
 * <ul>
 *   <li><b>Tags (a.k.a. <i>elements</i>)</b>: <tt>&lt;body&gt;</tt>, <tt>&lt;img/&gt;</tt>, 
 *       <tt>&lt;div class="content"&gt;</tt>, etc. (note that elemens are returned as a whole,
 *       without differentiating their arguments)</li>
 *   <li><b>Comments</b>: <tt>&lt;!-- this is a comment --&gt;</tt></li>
 *   <li><b>CDATA sections</b>: <tt>&lt;![CDATA[ ... ]]&gt;</tt></li>
 *   <li><b>DOCTYPE clauses</b>: <tt>&lt;!DOCTYPE html&gt;</tt></li>
 *   <li><b>XML Declarations</b>: <tt>&lt;?xml version="1.0"?&gt;</tt></li>
 *   <li><b>Processing Instructions</b>: <tt>&lt;?xsl-stylesheet ...?&gt;</tt></li>
 * </ul>
 * <p>
 *   This class provides empty implementations for all event handlers, so that
 *   subclasses can override only the methods they need.
 * </p>
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractBasicMarkupAttoHandler 
        extends AbstractAttoHandler
        implements IBasicElementHandling, IBasicDocTypeHandling, ICDATASectionHandling, ICommentHandling,
                   IXmlDeclarationHandling, IProcessingInstructionHandling {


    
    protected AbstractBasicMarkupAttoHandler() {
        super();
    }



    @Override
    public final char[] handleStructure(
            final char[] buffer,
            final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {
        
        super.handleStructure(buffer, offset, len, line, col);

        final char[] tryParseElementResult =
                ElementMarkupParsingUtil.tryParseElement(buffer, offset, len, line, col, this);
        if (tryParseElementResult != ElementMarkupParsingUtil.TRY_PARSE_ELEMENT_FALSE) {
            return tryParseElementResult;
        }

        if (!CommentMarkupParsingUtil.tryParseComment(buffer, offset, len, line, col, this)) {
             if (!CdataMarkupParsingUtil.tryParseCdata(buffer, offset, len, line, col, this )) {
                 if (!DocTypeMarkupParsingUtil.tryParseDocType(buffer, offset, len, line, col, this)) {
                     if (!XmlDeclarationMarkupParsingUtil.tryParseXmlDeclaration(buffer, offset, len, line, col, this)) {
                         if (!ProcessingInstructionMarkupParsingUtil.tryParseProcessingInstruction(buffer, offset, len, line, col, this)) {
                             throw new AttoParseException(
                                     "Could not parse as markup structure: " +
                                     "\"" + new String(buffer, offset, len) + "\"",
                                     line, col);
                         }
                     }
                 }
             }
        }

        return null;

    }



    public void handleDocType(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }
    
    

    public char[] handleStandaloneElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }


    public char[] handleOpenElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen,
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    
    public char[] handleCloseElement(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
        return null;
    }

    
    public void handleComment(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }

    
    public void handleCDATASection(
            final char[] buffer, 
            final int contentOffset, final int contentLen,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    public void handleXmlDeclaration(
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


    public void handleProcessingInstruction(
            final char[] buffer, 
            final int targetOffset, final int targetLen, 
            final int targetLine, final int targetCol,
            final int contentOffset, final int contentLen,
            final int contentLine, final int contentCol,
            final int outerOffset, final int outerLen, 
            final int line, final int col)
            throws AttoParseException {
        // Nothing to be done here, meant to be overridden if required
    }


    
    
}