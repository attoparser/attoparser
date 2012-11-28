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
package org.attoparser.util;

import java.lang.reflect.Array;
import java.util.Arrays;







/**
 * <p>
 *   A segmented array, which can improve seek performance by dividing elements
 *   into several different segments.
 * </p>
 * <p>
 *   This class is <b>not thread-safe</b>.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class SegmentedArray<T,K> {

    public static final int DEFAULT_SEGMENT_SIZE = 15;
    
    private final Class<T> componentType;
    private final IValueHandler<? super T,K> valueHandler;
    private final int numSegments;
    private final T[][] segments;
    private final int[] segmentSizes;
    private final int maxSegmentSize;
    


    
    
    public SegmentedArray(
            final Class<T> componentType, final IValueHandler<? super T,K> valueHandler, final int numSegments) {
        this(componentType, valueHandler, numSegments, -1);
    }
    
    
    @SuppressWarnings("unchecked")
    public SegmentedArray(
            final Class<T> componentType, final IValueHandler<? super T,K> valueHandler,
            final int numSegments, final int maxSegmentSize) {
        
        super();
        
        if (componentType == null) {
            throw new IllegalArgumentException("Component type cannot be null");
        }
        if (valueHandler == null) {
            throw new IllegalArgumentException("Value Handler cannot be null");
        }
        if (numSegments <= 0) {
            throw new IllegalArgumentException("Number of segments must be > 0");
        }
        if (maxSegmentSize == 0) {
            throw new IllegalArgumentException("Maximum segment size must be > 0, or -1 for unlimited");
        }
        
        this.componentType = componentType;
        this.valueHandler = valueHandler;
        
        this.numSegments = numSegments;
        this.maxSegmentSize = maxSegmentSize;
        
        final Class<T[]> arrayClass = (Class<T[]>) Array.newInstance(componentType, 0).getClass();
        this.segments = (T[][]) Array.newInstance(arrayClass, this.numSegments);
        this.segmentSizes = new int[this.numSegments];
        Arrays.fill(this.segmentSizes, 0);
        
    }
    

    
    
    
    
    public T searchByKey(final K key) {

        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        final int segment = this.valueHandler.getSegmentByKey(key);
        final int index = segment % this.numSegments;
        
        final T[] values = this.segments[index];
        
        if (values == null) {
            return null;
        }
        
        final int valuesSize = this.segmentSizes[index];
        for (int i = 0; i < valuesSize; i++) {
            if (this.valueHandler.matchesByKey(values[i], key)) {
                return values[i];
            }
        }
        return null;
        
    }

    
    public T searchByText(final String text) {

        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }

        final int segment = this.valueHandler.getSegmentByText(text);
        final int index = segment % this.numSegments;
        
        final T[] values = this.segments[index];
        
        if (values == null) {
            return null;
        }
        
        final int valuesSize = this.segmentSizes[index];
        for (int i = 0; i < valuesSize; i++) {
            if (this.valueHandler.matchesByText(values[i], text)) {
                return values[i];
            }
        }
        return null;
        
    }
    
    
    public T searchByText(final char[] text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        return searchByText(text, 0, text.length);
    }
    
    
    public T searchByText(final char[] textBuffer, final int textOffset, final int textLen) {

        if (textBuffer == null) {
            throw new IllegalArgumentException("Text buffer cannot be null");
        }

        final int segment = this.valueHandler.getSegmentByText(textBuffer, textOffset, textLen);
        final int index = segment % this.numSegments;
        
        final T[] values = this.segments[index];
        
        if (values == null) {
            return null;
        }
        
        final int valuesSize = this.segmentSizes[index];
        for (int i = 0; i < valuesSize; i++) {
            if (this.valueHandler.matchesByText(values[i], textBuffer, textOffset, textLen)) {
                return values[i];
            }
        }
        return null;
        
    }
    
    

    
    @SuppressWarnings("unchecked")
    public boolean registerValue(final T value) {

        final int segment = this.valueHandler.getSegment(value);
        final int index = segment % this.numSegments;
        
        final int valuesSize = this.segmentSizes[index];

        if (this.maxSegmentSize != -1 && valuesSize >= this.maxSegmentSize) {
            // We've reached the maximum size for this segment!
            return false;
        }

        if (valuesSize == 0) {
            // No values until now: create segment
            
            final int initialSegmentSize =
                    (this.maxSegmentSize < 0? DEFAULT_SEGMENT_SIZE : Math.min(DEFAULT_SEGMENT_SIZE, this.maxSegmentSize));
            this.segments[index] = (T[]) Array.newInstance(this.componentType, initialSegmentSize);
            this.segments[index][0] = value;
            this.segmentSizes[index]++;
            
            return true;
            
        }
        
        final int valuesArrayLen = this.segments[index].length;
        if (valuesSize < valuesArrayLen) {
            // There's still room at the array for our new value
            
            this.segments[index][valuesSize] = value;
            this.segmentSizes[index]++;
            
            return true;
            
        }
        
        // Value does not fit. We need to resize array
        
        final int newSize =
                (this.maxSegmentSize < 0? 
                        (valuesArrayLen + DEFAULT_SEGMENT_SIZE) : 
                        Math.min((valuesArrayLen + DEFAULT_SEGMENT_SIZE), this.maxSegmentSize));
        final T[] newValues = (T[]) Array.newInstance(this.componentType, newSize);
        System.arraycopy(this.segments[index], 0, newValues, 0, valuesSize);
        newValues[valuesSize] = value;
        this.segments[index] = newValues;
        this.segmentSizes[index]++;
        
        return true;
        
    }

    
    
    
    

    @Override
    public String toString() {
        final StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < this.numSegments; i++) {
            strBuilder.append("[" + i + "] ");
            if (this.segments[i] != null) {
                final T[] segment = this.segments[i];
                final int segmentSize = this.segmentSizes[i];
                if (segment[0] instanceof char[]) {
                    strBuilder.append((char[])segment[0]);
                } else {
                    strBuilder.append(segment[0]);
                }
                for (int j = 1; j < segmentSize; j++) {
                    strBuilder.append(",");
                    if (segment[j] instanceof char[]) {
                        strBuilder.append((char[])segment[j]);
                    } else {
                        strBuilder.append(segment[j]);
                    }
                }
            }
            strBuilder.append('\n');
        }
        return strBuilder.toString();
    }

    
    


    public static interface IValueHandler<X,K> {
        
        public K getKey(final X value);
        
        public int getSegment(final X value);
        public int getSegmentByKey(final K key);
        public int getSegmentByText(final String text);
        public int getSegmentByText(final char[] textBuffer, final int textOffset, final int textLen);
        
        public boolean matchesByKey(final X value, final K key);
        public boolean matchesByText(final X value, final String text);
        public boolean matchesByText(final X value, final char[] textBuffer, final int textOffset, final int textLen);
        
    }
    
    
}