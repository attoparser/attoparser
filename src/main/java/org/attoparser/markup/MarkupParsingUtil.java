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




/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class MarkupParsingUtil {


    

    
    private MarkupParsingUtil() {
        super();
    }

    
    

    
    
    

    
    
    
    
    static int findNextStructureEndAvoidQuotes(
            final char[] text, final int offset, final int maxi, 
            final MarkupParsingLocator locator) {
        
        boolean inQuotes = false;

        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (!inQuotes && c == '>') {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }
    
    static int findNextStructureEndDontAvoidQuotes(
            final char[] text, final int offset, final int maxi, 
            final MarkupParsingLocator locator) {
        
        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (c == '>') {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }
    
    static int findNextStructureStart(
            final char[] text, final int offset, final int maxi, 
            final MarkupParsingLocator locator) {
        
        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (c == '<') {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }

    
    static int findNextWhitespaceCharWildcard(
            final char[] text, final int offset, final int maxi, 
            final boolean avoidQuotes, final MarkupParsingLocator locator) {
        
        boolean inQuotes = false;

        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (avoidQuotes && c == '"') {
                inQuotes = !inQuotes;
            } else if (!inQuotes && (c == ' ' || c == '\n' || Character.isWhitespace(c))) {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }

    
    static int findNextNonWhitespaceCharWildcard(
            final char[] text, final int offset, final int maxi, 
            final MarkupParsingLocator locator) {
        
        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            final boolean isWhitespace = (c == ' ' || c == '\n' || Character.isWhitespace(c));
            
            if (!isWhitespace) {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }

    
    static int findNextOperatorCharWildcard(
            final char[] text, final int offset, final int maxi,  
            final MarkupParsingLocator locator) {
        
        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (c == '=' || (c == ' ' || c == '\n' || Character.isWhitespace(c))) {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }

    
    static int findNextNonOperatorCharWildcard(
            final char[] text, final int offset, final int maxi, 
            final MarkupParsingLocator locator) {
        
        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (c != '=' && !(c == ' ' || c == '\n' || Character.isWhitespace(c))) {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }

    
    static int findNextAnyCharAvoidQuotesWildcard(
            final char[] text, final int offset, final int maxi,  
            final MarkupParsingLocator locator) {
        
        boolean inQuotes = false;

        for (int i = offset; i < maxi; i++) {
            
            final char c = text[i];
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (!inQuotes) {
                return i;
            }

            MarkupParsingLocator.countChar(locator, c);
            
        }
            
        return -1;
        
    }

    
    
    
}
