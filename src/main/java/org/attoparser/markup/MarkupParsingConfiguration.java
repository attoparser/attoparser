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

import java.io.Serializable;






/**
 * <p>
 *   Models a series of markup parsing parameterizations that can be applied during document parsing
 *   by {@link AbstractDetailedMarkupAttoHandler} (and its subclasses).
 * </p>
 * <p>
 *   For example, a this parameterizations can be used for checking the well-formedness
 *   (from an XML/XHTML standpoint) of a document.
 * </p>
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class MarkupParsingConfiguration implements Serializable {

    public static enum ElementBalancing { REQUIRE_BALANCED, AUTO_CLOSE, NO_BALANCING } 
    
    private static final long serialVersionUID = 5191449744126332916L;
    
    
    private ElementBalancing elementBalancing = ElementBalancing.NO_BALANCING;
    private boolean requireNoUnbalancedCloseElements = false;
    private boolean requireWellFormedProlog = false;
    private boolean requireUniqueRootElement = false;
    private boolean requireWellFormedAttributeValues = false;
    private boolean requireUniqueAttributesInElement = false;
    private boolean requireNoProlog = false;
    


    

    /**
     * <p>
     *   Creates a {@link MarkupParsingConfiguration} instance enforcing no restrictions at all.
     * </p>
     * <p>
     *   This is the setup:
     * </p>
     * <ul>
     *   <li><tt>elementBalancing = NO_BALANCING</tt></li>
     *   <li><tt>requireNoUnbalancedCloseElements = false</tt></li>
     *   <li><tt>requireWellFormedProlog = false</tt></li>
     *   <li><tt>requireUniqueRootElement = false</tt></li>
     *   <li><tt>requireWellFormedAttributeValues = false</tt></li>
     *   <li><tt>requireUniqueAttributesInElement = false</tt></li>
     *   <li><tt>requireNoProlog = false</tt></li>
     * </ul>
     * 
     * @return the new instance.
     */
    public static MarkupParsingConfiguration noRestrictions() {
        return new MarkupParsingConfiguration();
    }
    
    

    
    
    public MarkupParsingConfiguration() {
        super();
    }
    

    
    public ElementBalancing getElementBalancing() {
        return this.elementBalancing;
    }


    public void setElementBalancing(final ElementBalancing elementBalancing) {
        this.elementBalancing = elementBalancing;
    }
    
    
    public boolean getRequireNoUnbalancedCloseElements() {
        return this.requireNoUnbalancedCloseElements;
    }


    public void setRequireNoUnbalancedCloseElements(final boolean requireNoUnbalancedCloseElements) {
        this.requireNoUnbalancedCloseElements = requireNoUnbalancedCloseElements;
    }


    public boolean getRequireWellFormedProlog() {
        return this.requireWellFormedProlog;
    }


    public void setRequireWellFormedProlog(final boolean requireWellFormedProlog) {
        this.requireWellFormedProlog = requireWellFormedProlog;
    }


    public boolean getRequireWellFormedAttributeValues() {
        return this.requireWellFormedAttributeValues;
    }


    public void setRequireWellFormedAttributeValues(
            final boolean requireWellFormedAttributeValues) {
        this.requireWellFormedAttributeValues = requireWellFormedAttributeValues;
    }


    public boolean getRequireUniqueAttributesInElement() {
        return this.requireUniqueAttributesInElement;
    }


    public void setRequireUniqueAttributesInElement(final boolean requireUniqueAttributesInElement) {
        this.requireUniqueAttributesInElement = requireUniqueAttributesInElement;
    }


    public boolean getRequireUniqueRootElement() {
        return this.requireUniqueRootElement;
    }


    public void setRequireUniqueRootElement(final boolean requireUniqueRootElement) {
        this.requireUniqueRootElement = requireUniqueRootElement;
    }


    public boolean getRequireNoProlog() {
        return this.requireNoProlog;
    }


    public void setRequireNoProlog(final boolean requireNoProlog) {
        this.requireNoProlog = requireNoProlog;
    }
    
        
}