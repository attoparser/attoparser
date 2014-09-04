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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.attoparser.markup.html.elements.HtmlElements;


/**
 * <p>
 *     Repository for markup structure names (element names, attribute names).
 * </p>
 * <p>
 *     The aim of this class is to reduce the amount of char[] object created by this handler to the minimum, by
 *     pre-caching the names of all standard HTML element names and many of the most common HTML attribute names.
 * </p>
 * <p>
 *     Of course this pre-caching will be only effective when parsing HTML markup, but creating a real cache able
 *     to contain non-standard name would require some locking logic, not desirable at this low level of the
 *     application architecture.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class MarkupStructureNameRepository {


    private static final char[][] REPOSITORY;


    static {

        final List<String> names = new ArrayList<String>();
        names.addAll(HtmlElements.ALL_STANDARD_ELEMENT_NAMES);
        names.addAll(HtmlElements.ALL_STANDARD_ATTRIBUTE_NAMES);
        Collections.sort(names);

        REPOSITORY = new char[names.size()][];

        for (int i = 0; i < names.size(); i++) {
            final String name = names.get(i);
            REPOSITORY[i] = name.toCharArray();
        }

    }


    public static char[] getStructureName(final char[] text, final int offset, final int len) {

        final int index = binarySearch(REPOSITORY, text, offset, len);

        if (index == -1) {
            final char[] structureName = new char[len];
            System.arraycopy(text, offset, structureName, 0, len);
            return structureName;
        }

        return REPOSITORY[index];

    }



    private static int binarySearch(final char[][] values,
                            final char[] text, final int offset, final int len) {

        int low = 0;
        int high = values.length - 1;

        while (low <= high) {

            final int mid = (low + high) >>> 1;
            final char[] midVal = values[mid];

            final int cmp = compare(midVal, text, offset, len);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                // Found!!
                return mid;
            }

        }

        return -1;  // Not Found!!

    }



    private static int compare(final char[] ncr, final char[] text, final int offset, final int len) {
        final int maxCommon = Math.min(ncr.length, len);
        int i;
        for (i = 0; i < maxCommon; i++) {
            final char tc = text[offset + i];
            if (ncr[i] < tc) {
                return -1;
            } else if (ncr[i] > tc) {
                return 1;
            }
        }
        if (ncr.length > i) {
            return 1;
        }
        if (len > i) {
            return -1;
        }
        return 0;
    }


}