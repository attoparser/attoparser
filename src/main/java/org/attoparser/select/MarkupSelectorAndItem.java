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

/*
 *
 * @author Daniel Fernandez
 * @since 2.0.0
 */
final class MarkupSelectorAndItem implements IMarkupSelectorItem {

    final IMarkupSelectorItem left;
    final IMarkupSelectorItem right;


    MarkupSelectorAndItem(final IMarkupSelectorItem left, final IMarkupSelectorItem right) {
        super();
        if ((right.anyLevel() && !left.anyLevel()) || (!right.anyLevel() && left.anyLevel())) {
            throw new IllegalArgumentException("Left and right items must have the same value for ''anyLevel': " + left.toString() + " && " + right.toString());
        }
        this.left = left;
        this.right = right;
    }

    public boolean anyLevel() {
        // left and right should be equal
        return this.left.anyLevel();
    }


    public boolean matchesText(
            final int markupBlockIndex, final MarkupSelectorFilter.MarkupBlockMatchingCounter markupBlockMatchingCounter) {
        return this.left.matchesText(markupBlockIndex, markupBlockMatchingCounter) &&
                this.right.matchesText(markupBlockIndex, markupBlockMatchingCounter);
    }

    public boolean matchesComment(
            final int markupBlockIndex, final MarkupSelectorFilter.MarkupBlockMatchingCounter markupBlockMatchingCounter) {
        return this.left.matchesComment(markupBlockIndex, markupBlockMatchingCounter) &&
                this.right.matchesComment(markupBlockIndex, markupBlockMatchingCounter);
    }

    public boolean matchesCDATASection(
            final int markupBlockIndex, final MarkupSelectorFilter.MarkupBlockMatchingCounter markupBlockMatchingCounter) {
        return this.left.matchesCDATASection(markupBlockIndex, markupBlockMatchingCounter) &&
                this.right.matchesCDATASection(markupBlockIndex, markupBlockMatchingCounter);
    }

    public boolean matchesDocTypeClause(
            final int markupBlockIndex, final MarkupSelectorFilter.MarkupBlockMatchingCounter markupBlockMatchingCounter) {
        return this.left.matchesDocTypeClause(markupBlockIndex, markupBlockMatchingCounter) &&
                this.right.matchesDocTypeClause(markupBlockIndex, markupBlockMatchingCounter);
    }

    public boolean matchesXmlDeclaration(
            final int markupBlockIndex, final MarkupSelectorFilter.MarkupBlockMatchingCounter markupBlockMatchingCounter) {
        return this.left.matchesXmlDeclaration(markupBlockIndex, markupBlockMatchingCounter) &&
                this.right.matchesXmlDeclaration(markupBlockIndex, markupBlockMatchingCounter);
    }

    public boolean matchesProcessingInstruction(
            final int markupBlockIndex, final MarkupSelectorFilter.MarkupBlockMatchingCounter markupBlockMatchingCounter) {
        return this.left.matchesProcessingInstruction(markupBlockIndex, markupBlockMatchingCounter) &&
                this.right.matchesProcessingInstruction(markupBlockIndex, markupBlockMatchingCounter);
    }

    public boolean matchesElement(final int markupBlockIndex, final SelectorElementBuffer elementBuffer, final MarkupSelectorFilter.MarkupBlockMatchingCounter markupBlockMatchingCounter) {
        return this.left.matchesElement(markupBlockIndex, elementBuffer, markupBlockMatchingCounter) &&
                this.right.matchesElement(markupBlockIndex, elementBuffer, markupBlockMatchingCounter);
    }

    public String toString() {
        return "(" + this.left.toString() + " && " + this.right + ")";
    }

}
