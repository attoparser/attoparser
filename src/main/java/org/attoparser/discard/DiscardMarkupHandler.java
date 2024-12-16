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
package org.attoparser.discard;


import org.attoparser.AbstractMarkupHandler;

/**
 * <p>
 *   Implementation of {@link org.attoparser.IMarkupHandler} that simply discards all events.
 * </p>
 * <p>
 *   This handler is normally used in scenarios in which some events are directed to one handler and some others
 *   to a second one, like block/node selection operations performed by means of the
 *   {@link org.attoparser.select.BlockSelectorMarkupHandler} or
 *   {@link org.attoparser.select.NodeSelectorMarkupHandler} handlers.
 * </p>
 * <p>
 *   Note that, unlike most other implementations of {@link org.attoparser.IMarkupHandler}, this class is
 *   completely stateless and <strong>thread-safe</strong>, and thus objects of this class can be safely reused
 *   among several parsing operations if needed.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class DiscardMarkupHandler extends AbstractMarkupHandler {


    /**
     * <p>
     *   Create a new instance of this handler.
     * </p>
     */
    public DiscardMarkupHandler() {
        super();
    }


    // No events handled. All of them are discarded (that's what the parent class does)

}