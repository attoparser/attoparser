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



/**
 * <p>
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class IgnorableUnmatchedCloseElementWarning extends IgnorableArtifactWarning {

    public static final String CODE = "IGN-0020";
    public static final String MESSAGE = 
            "This close element should be ignored, as it does not correspond to any previously " +
            "open element.";
    
    
    protected IgnorableUnmatchedCloseElementWarning() {
        super(CODE, MESSAGE);
    }
    
}
