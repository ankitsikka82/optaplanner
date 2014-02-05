/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.benchmark.impl.ranking;

import java.io.Serializable;
import java.util.Comparator;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.comparator.NaturalScoreComparator;

/**
 * Like {@link NaturalScoreComparator}, but is able to compare {@link Score}s of different types or nulls.
 */
public class ResilientScoreComparator implements Comparator<Score>, Serializable {

    public int compare(Score a, Score b) {
        if (a == null) {
            return b == null ? 0 : -1;
        } else if (b == null) {
            return 1;
        }
        if (!a.isCompatibleArithmeticArgument(b)) {
            Number[] aNumbers = a.toLevelNumbers();
            Number[] bNumbers = b.toLevelNumbers();
            for (int i = 0; i < aNumbers.length || i < bNumbers.length; i++) {
                Number aToken = i < aNumbers.length ? aNumbers[i] : 0;
                Number bToken = i < bNumbers.length ? bNumbers[i] : 0;
                int comparison;
                if (aToken.getClass().equals(bToken.getClass())) {
                    comparison = ((Comparable) aToken).compareTo(bToken);
                } else {
                    comparison = Double.valueOf(aToken.doubleValue()).compareTo(bToken.doubleValue());
                }
                if (comparison != 0) {
                    return comparison;
                }
            }
            return 0;
        }
        return a.compareTo(b);
    }

}
