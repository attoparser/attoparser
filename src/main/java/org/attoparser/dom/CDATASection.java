/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2025 Attoparser (https://www.attoparser.org)
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
package org.attoparser.dom;


/**
 * <p>
 *   CDATA Section node in a DOM tree.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public class CDATASection 
        extends Text {
    
    private static final long serialVersionUID = -131121996532074777L;

    public CDATASection(final String content) {
        super(content);
    }
    
    @Override
    public CDATASection cloneNode(final INestableNode parent) {
        final CDATASection cdataSection = new CDATASection(getContent());
        cdataSection.setLine(getLine());
        cdataSection.setCol(getCol());
        cdataSection.setParent(parent);
        return cdataSection;
    }
    
}
