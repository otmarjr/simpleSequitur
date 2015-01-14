package simpleSequitur;

/*
 This class is part of a Java port of Craig Nevill-Manning's Sequitur algorithm.
 Copyright (C) 1997 Eibe Frank

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

public class rule {

    // Guard symbol to mark beginning
    // and end of rule.
    public guard theGuard;

    // Counter keeps track of how many
    // times the rule is used in the
    // grammar.
    public int count;

    // The total number of rules.
    public static int numRules = 0;

    // The rule's number.
    // Used for identification of
    // non-terminals.
    public int number;

    // Index used for printing.
    public int index;

    private alphabet alphabet;

    rule() {
        number = numRules;
        numRules++;
        theGuard = new guard(this);
        count = 0;
        index = 0;
    }

    rule(alphabet sigma) {
        this();
        this.alphabet = sigma;
    }

    public alphabet sigma() {
        return this.alphabet;
    }
    
    public symbol first() {
        return theGuard.n;
    }

    public symbol last() {
        return theGuard.p;
    }

    public static List<String> mergeRepeatedItemsWithPlusOperator(List<String> items) {

        if (items.stream().anyMatch(i -> i.contains(")+"))) {

            String firstRepetition = items.stream().filter(i -> i.contains(")+")).findFirst().get();

            int indexRepetition = items.indexOf(firstRepetition);

            int initialPositionPattern = firstRepetition.indexOf("(");
            int finalPositionPattern = firstRepetition.indexOf(")+");
            String repeatedPattern = firstRepetition.substring(initialPositionPattern + 1, finalPositionPattern);

            int toMergeIndex = indexRepetition + 1;

            while (toMergeIndex < items.size() && items.get(toMergeIndex).equals(repeatedPattern)) {
                toMergeIndex++;
            }

            List<String> before = items.subList(0, indexRepetition + 1);
            List<String> after = mergeRepeatedItemsWithPlusOperator(items.subList(toMergeIndex, items.size()));

            List<String> merged;
            merged = new LinkedList<>(before);
            merged.addAll(after);
            return merged;
        } else {
            return items;
        }

    }

    public static List<String> convertRepeatedSubstringsToRegex(List<String> sequence) {
        List<String> items = mergeRepeatedItemsWithPlusOperator(sequence);

        List<String> regex = new LinkedList<>();

        int n = items.size();
        int substringSize = n / 2;

        List<Integer> positionMaximumMatch = new ArrayList<>(items.size());

        for (int i = 0; i < items.size(); i++) {
            positionMaximumMatch.add(i, -1);
        }

        Map<List<String>, List<Map.Entry<Integer, Integer>>> substringOccurences;
        substringOccurences = new HashMap<>();

        Map<Integer, List<String>> positionContainerSubstrings;

        positionContainerSubstrings = new LinkedHashMap<>();

        while (positionMaximumMatch.stream()
                .anyMatch(i -> i == -1) && substringSize > 0) {

            for (int i = 0; i + substringSize < n; i++) {

                List<String> substringi;

                int j = i + substringSize;

                boolean contiguousUnmatchedJItemsAhead = true;

                for (int k = i; k < j; k++) {
                    if (positionMaximumMatch.get(k) != -1) {
                        contiguousUnmatchedJItemsAhead = false;
                    }
                }

                if (!contiguousUnmatchedJItemsAhead) {
                    continue;
                }

                substringi = items.subList(i, j);

                for (int k = j; k + substringSize <= items.size(); k++) {

                    boolean contiguousUnmatchedKItemsAhead = true;

                    for (int p = k; p < k + substringSize; p++) {
                        if (positionMaximumMatch.get(p) != -1) {
                            contiguousUnmatchedJItemsAhead = false;
                        }
                    }

                    if (!contiguousUnmatchedKItemsAhead) {
                        continue;
                    }

                    List<String> substringj;
                    substringj = items.subList(k, k + substringSize);

                    if (substringi.equals(substringj)) {
                        for (int p = i; p < j; p++) {
                            positionMaximumMatch.set(p, substringSize);
                            positionContainerSubstrings.put(p, substringi);
                        }

                        for (int p = k; p < k + substringSize; p++) {
                            positionMaximumMatch.set(p, substringSize);
                            positionContainerSubstrings.put(p, substringi);
                        }

                        Map.Entry<Integer, Integer> startEndPositions;
                        startEndPositions = new AbstractMap.SimpleEntry<>(k, k + substringSize);

                        if (substringOccurences.containsKey(substringi)) {
                            substringOccurences.get(substringi).add(startEndPositions);
                        } else {
                            List<Map.Entry<Integer, Integer>> matchIndexes;
                            matchIndexes = new LinkedList<>();

                            Map.Entry<Integer, Integer> ij;
                            ij = new AbstractMap.SimpleEntry<>(i, j);

                            matchIndexes.add(ij);
                            matchIndexes.add(startEndPositions);

                            substringOccurences.put(substringi, matchIndexes);
                        }
                    }

                }
            }

            substringSize--;
        }

        int index = 0;

        while (index < n) {
            if (positionContainerSubstrings.containsKey(index)) {
                List<String> containerSubString = positionContainerSubstrings.get(index);
                int i0 = index;

                List<Map.Entry<Integer, Integer>> occurences = substringOccurences
                        .get(containerSubString)
                        .stream()
                        .filter(ij -> ij.getKey() >= i0)
                        .collect(Collectors.toList());

                if (occurences.size() > 1) {
                    int size = containerSubString.size();
                    int offsetCausedByNonRepeatedItems = regex.size();
                    List<Map.Entry<Integer, Integer>> adjacent = occurences.stream()
                            .filter(e -> e.getKey().equals(occurences.indexOf(e) * size + offsetCausedByNonRepeatedItems))
                            .collect(Collectors.toList());

                    if (adjacent.size() > 1) {
                        boolean isPrimitiveRepetition = !containerSubString
                                .get(containerSubString.size()-1)
                                .contains("+");

                        if (isPrimitiveRepetition) {
                            regex.add("(");
                            regex.addAll(containerSubString);
                            regex.add(")+");
                        } else {
                            regex.addAll(containerSubString);
                        }
                        index = adjacent.get(adjacent.size() - 1).getValue();
                    } else {
                        regex.addAll(containerSubString);
                        index = adjacent.get(0).getValue();
                    }
                } else {
                    regex.addAll(containerSubString);
                    index = occurences.get(0).getValue();
                }
            } else {
                regex.add(items.get(index));
                index++;
            }
        }

        return regex;
    }

    public String convertRightHandSideToRegex() {
        List<String> rightHandSide = new LinkedList<>();

        for (symbol sym = this.first(); (!sym.isGuard()); sym = sym.n) {
            if (sym.isNonTerminal()) {
                nonTerminal nt = (nonTerminal) sym;
                rightHandSide.add(nt.r.convertRightHandSideToRegex());
            } else {
                String currentElement;
                currentElement = sym.getValue();
                rightHandSide.add(currentElement);
            }
        }

        List<String> protoRegex = convertRepeatedSubstringsToRegex(rightHandSide);

        StringBuilder regex = new StringBuilder();

        protoRegex.forEach(e -> regex.append(e));

        return regex.toString();
    }

    public String getRules() {

        Vector rules = new Vector(numRules);
        rule currentRule;
        rule referedTo;
        symbol sym;
        int index;
        int processedRules = 0;
        StringBuffer text = new StringBuffer();
        int charCounter = 0;

        text.append("Usage\tRule\n");
        rules.addElement(this);
        while (processedRules < rules.size()) {
            currentRule = (rule) rules.elementAt(processedRules);
            text.append(" ");
            text.append(currentRule.count);
            text.append("\tR");
            text.append(processedRules);
            text.append(" -> ");
            for (sym = currentRule.first(); (!sym.isGuard()); sym = sym.n) {
                if (sym.isNonTerminal()) {
                    referedTo = ((nonTerminal) sym).r;
                    if ((rules.size() > referedTo.index)
                            && ((rule) rules.elementAt(referedTo.index)
                            == referedTo)) {
                        index = referedTo.index;
                    } else {
                        index = rules.size();
                        referedTo.index = index;
                        rules.addElement(referedTo);
                    }
                    text.append('R');
                    text.append(index);
                } else {
                    String symValue = sym.getValue();

                    if (symValue == " ") {
                        text.append('_');
                    } else {
                        if (symValue == "\n") {
                            text.append("\\n");
                        } else {
                            text.append(symValue);
                        }
                    }
                }
                text.append(' ');
            }
            text.append('\n');
            processedRules++;
        }
        return new String(text);
    }
}
