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


/**
 * <p>
 *   Inner class used for returning results in inner try* methods in Util parsing classes.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.5.0
 *
 */
final class BestEffortParsingResult {

    static final BestEffortParsingResult PARSED_NO_RESULT = new BestEffortParsingResult(null);

    private final IAttoHandleResult handleResult;


    static BestEffortParsingResult forHandleResult(final IAttoHandleResult handleResult) {
        if (handleResult == null) {
            return PARSED_NO_RESULT;
        }
        return new BestEffortParsingResult(handleResult);
    }


    private BestEffortParsingResult(final IAttoHandleResult handleResult) {
        super();
        this.handleResult = handleResult;
    }

    public IAttoHandleResult getHandleResult() {
        return this.handleResult;
    }
}
