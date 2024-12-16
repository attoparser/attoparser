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
package org.attoparser.select;



/**
 * <p>
 *   Class used for reporting the current selectors matching the different levels of selection specified at
 *   the handler chain by means of instances of {@link org.attoparser.select.BlockSelectorMarkupHandler} and
 *   {@link org.attoparser.select.NodeSelectorMarkupHandler} instances.
 * </p>
 * <p>
 *   Each selection level represents a selection handler (block or node), in the order that they have been setup
 *   for execution in the handler chain.
 * </p>
 * <p>
 *   The {@link #toString()} method of this class provides a visual representation of all the selection levels
 *   currently being used, as well as the selectors that are matching at each level.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public final class ParseSelection {

    private int levelCounter = 0;

    ParseSelectionLevel[] levels;


    /**
     * <p>
     *   Create a new instance of this class. Instances of this class are normally created by the parser
     *   implementation objects (e.g. {@link org.attoparser.MarkupParser}), so there is no need to ever call this
     *   constructor from the user's code.
     * </p>
     */
    public ParseSelection() {
        super();
    }

    int subscribeLevel() {
        final ParseSelectionLevel[] newLevels = new ParseSelectionLevel[this.levelCounter + 1];
        if (this.levels != null) {
            System.arraycopy(this.levels, 0, newLevels, 0, this.levelCounter);
        }
        this.levels = newLevels;
        this.levels[this.levelCounter] = new ParseSelectionLevel();
        return this.levelCounter++;
    }


    /**
     * <p>
     *   Get the amount of levels that have been registered at the handler chain. This will be the amount of
     *   {@link org.attoparser.select.BlockSelectorMarkupHandler} and
     *   {@link org.attoparser.select.NodeSelectorMarkupHandler} handler instances registered at the current
     *   handler chain being executed by the parser.
     * </p>
     *
     * @return the total amount of <em>selection levels</em>.
     */
    public int getSelectionLevels() {
        return this.levelCounter;
    }


    /**
     * <p>
     *   Returns all the selectors (not only the matching ones) active for a specific <em>selection level</em>, ie
     *   for a specific instance of {@link org.attoparser.select.BlockSelectorMarkupHandler} or
     *   {@link org.attoparser.select.NodeSelectorMarkupHandler} at the handler chain.
     * </p>
     *
     * @param level the selection level.
     * @return all the selectors active for that level (not only the matching ones).
     */
    public String[] getSelectors(final int level) {
        if (level >= this.levelCounter) {
            throw new IllegalArgumentException(
                    "Cannot return current selection: max level is " + this.levelCounter + " (specified: " + level + ")");
        }
        if (this.levels == null) {
            return null;
        }
        return this.levels[level].selectors;
    }


    /**
     * <p>
     *   Returns the matching selectors for a specific selection level, or null if no selector is currently matching
     *   at that level.
     * </p>
     * <p>
     *   This method <strong>should only be called from event handlers</strong> or other code called from these, given
     *   the information about matching selectors will only be updated before (and after) calling the events
     *   relevant for that selection.
     * </p>
     *
     * @param level the selection level.
     * @return the currently matching selectors for that level.
     */
    public String[] getCurrentSelection(final int level) {
        if (level >= this.levelCounter) {
            throw new IllegalArgumentException(
                    "Cannot return current selection: max level is " + this.levelCounter + " (specified: " + level + ")");
        }
        if (this.levels == null) {
            return null;
        }
        return this.levels[level].getCurrentSelection();
    }


    /**
     * <p>
     *   Returns whether any selectors are currently matching at the specified selection level.
     * </p>
     *
     * @param level the selection level.
     * @return true if any selectors are matching at the specified level, false if not.
     */
    public boolean isMatchingAny(final int level) {
        if (level >= this.levelCounter) {
            throw new IllegalArgumentException(
                    "Cannot return current selection: max level is " + this.levelCounter + " (specified: " + level + ")");
        }
        if (this.levels == null) {
            return false;
        }
        return this.levels[level].isSelectionActive();
    }


    /**
     * <p>
     *   Returns whether any selectors are currently matching, at any level.
     * </p>
     *
     * @return true if any selectors are matching at any level, false if not.
     */
    public boolean isMatchingAny() {
        if (this.levels == null) {
            return false;
        }
        int i = 0;
        int n = this.levelCounter;
        while (n-- != 0) {
            if (this.levels[i].isSelectionActive()) {
                return true;
            }
            i++;
        }
        return false;
    }


    @Override
    public String toString() {
        if (this.levels.length == 0) {
            return "";
        }
        final StringBuilder strBuilder = new StringBuilder(40);
        strBuilder.append(this.levels[0]);
        if (this.levels.length > 1) {
            for (int i = 1; i < this.levels.length; i++) {
                strBuilder.append(" -> ");
                strBuilder.append(this.levels[i]);
            }
        }
        return strBuilder.toString();
    }


    static final class ParseSelectionLevel {

        String[] selectors;
        boolean[] selection;

        private ParseSelectionLevel() {
            super();
        }


        String[] getCurrentSelection() {

            if (this.selection == null) {
                return null;
            }

            int size = 0;
            int i = 0;
            int n = this.selectors.length;
            while (n-- != 0) {
                if (this.selection[i]) {
                    size++;
                }
                i++;
            }

            if (size == this.selectors.length) {
                // We avoid needing the creation of a new array object
                return this.selectors;
            }

            final String[] currentSelection = new String[size];
            int j = 0;
            i = 0;
            n = this.selectors.length;
            while (n-- != 0) {
                if (this.selection[i]) {
                    currentSelection[j++] = this.selectors[i];
                }
                i++;
            }

            return currentSelection;

        }


        public boolean isSelectionActive() {
            if (this.selection == null) {
                return false;
            }
            int i = 0;
            int n = this.selectors.length;
            while (n-- != 0) {
                if (this.selection[i]) {
                    return true;
                }
                i++;
            }
            return false;
        }


        @Override
        public String toString() {

            final StringBuilder strBuilder = new StringBuilder(20);
            strBuilder.append('[');
            if (this.selection != null) {
                for (int i = 0; i < this.selectors.length; i++) {
                    if (this.selection[i]) {
                        if (strBuilder.length() > 1) {
                            strBuilder.append(',');
                        }
                        strBuilder.append(this.selectors[i]);
                    }
                }
            }
            strBuilder.append(']');

            return strBuilder.toString();

        }



    }


}
