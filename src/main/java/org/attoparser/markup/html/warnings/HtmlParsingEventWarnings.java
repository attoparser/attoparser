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
package org.attoparser.markup.html.warnings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * <p>
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class HtmlParsingEventWarnings {

    
    
    // STY-0010
    public static final IHtmlParsingEventWarning NON_MINIMIZED_STANDALONE_ELEMENT = new NonMinimizedStandaloneElementWarning();
    // IGN-0020
    public static final IHtmlParsingEventWarning IGNORABLE_UNMATCHED_CLOSE_ELEMENT = new IgnorableUnmatchedCloseElementWarning();
    // IGN-0030
    public static final IHtmlParsingEventWarning IGNORABLE_CLOSE_STANDALONE_ELEMENT = new IgnorableCloseStandaloneElementWarning();
    
    
    
    
    
    /*
     * --------------------
     *   LISTS
     * --------------------
     */
    
    public static final HtmlParsingEventWarnings WARNINGS_NONE = new HtmlParsingEventWarnings();
    public static final HtmlParsingEventWarnings WARNINGS_NON_MINIMIZED_STANDALONE_ELEMENT = new HtmlParsingEventWarnings(NON_MINIMIZED_STANDALONE_ELEMENT);
    public static final HtmlParsingEventWarnings WARNINGS_IGNORABLE_UNMATCHED_CLOSE_ELEMENT = new HtmlParsingEventWarnings(IGNORABLE_UNMATCHED_CLOSE_ELEMENT);
    public static final HtmlParsingEventWarnings WARNINGS_IGNORABLE_CLOSE_STANDALONE_ELEMENT = new HtmlParsingEventWarnings(IGNORABLE_CLOSE_STANDALONE_ELEMENT);
    

    

    
    
    /*
     * --------------------
     *   FACTORY
     * --------------------
     */
    
    public static HtmlParsingEventWarnings create(final List<IHtmlParsingEventWarning> warnings) {
        return new HtmlParsingEventWarnings(Collections.unmodifiableList(new ArrayList<IHtmlParsingEventWarning>(warnings)));
    }

    
    
    
    
    /*
     * ------------------------------------------------------------
     *   INSTANCE PROPERTIES, CONSTRUCTORS AND METHODS
     * ------------------------------------------------------------
     * 
     */
    
    private final List<IHtmlParsingEventWarning> warnings;
    

    
    
    @SuppressWarnings({"unchecked","cast"})
    private HtmlParsingEventWarnings() {
        this((List<IHtmlParsingEventWarning>)Collections.EMPTY_LIST);
    }

    private HtmlParsingEventWarnings(IHtmlParsingEventWarning warning) {
        this(Collections.singletonList(warning));
    }
    
    private HtmlParsingEventWarnings(final List<IHtmlParsingEventWarning> warnings) {
        super();
        this.warnings = warnings;
    }

    
    

    public List<IHtmlParsingEventWarning> getWarnings() {
        return this.warnings;
    }
    
    
    public boolean isEmpty() {
        return this.warnings.isEmpty();
    }
    
    
}
