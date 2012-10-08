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
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class DocumentRestrictions implements Serializable {

    
    private static final long serialVersionUID = 5191449744126332916L;
    
    
    private boolean requireBalancedElements = false;
    private boolean requireWellFormedProlog = false;
    private boolean requireUniqueRootElement = false;
    private boolean requireWellFormedAttributeValues = false;
    private boolean requireUniqueAttributesInElement = false;
    


    
    
    public static DocumentRestrictions none() {
        final DocumentRestrictions spec = new DocumentRestrictions();
        spec.requireBalancedElements = false;
        spec.requireWellFormedProlog = false;
        spec.requireUniqueRootElement = false;
        spec.requireWellFormedAttributeValues = false;
        spec.requireUniqueAttributesInElement = false;
        return spec;
    }

    
    public static DocumentRestrictions wellFormed() {
        final DocumentRestrictions spec = new DocumentRestrictions();
        spec.requireBalancedElements = true;
        spec.requireWellFormedProlog = true;
        spec.requireUniqueRootElement = true;
        spec.requireWellFormedAttributeValues = true;
        spec.requireUniqueAttributesInElement = true;
        return spec;
    }
    
    

    
    
    private DocumentRestrictions() {
        super();
    }
    

    
    public boolean getRequireBalancedElements() {
        return this.requireBalancedElements;
    }


    public void setRequireBalancedElements(final boolean requireBalancedElements) {
        this.requireBalancedElements = requireBalancedElements;
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
    
        
}