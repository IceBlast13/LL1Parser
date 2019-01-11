package application;

import model.ICGrammar;
import model.Production;

import java.util.*;

public class LL1Parser {

    /**analyze the given sequence and return a list with the used productions indices, using the given parsing
     * table;
     * */
    public static List<Integer> analyzeSequence(List<String> sequence, int[][] parsingTable, ICGrammar icGrammar)
            throws Exception {
        if(parsingTable == null){
            /*if the grammar is not LL1, then return null*/
            throw new Exception("the given grammar is not LL1");
        }
        List<Integer> productionOrderList = new ArrayList<>();

        List<Production> productionList = icGrammar.getProductionRules();
        String epsilon = icGrammar.getEpsilon();
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push(icGrammar.getStartSymbol());

        int sequenceIndex = 0;
        while(!stack.peek().equals("$")){
            //System.out.print(stack + "\t");
            //System.out.println(sequence.subList(sequenceIndex, sequence.size()));
            if(sequence.get(sequenceIndex).equals("$")){
                if(parsingTable[icGrammar.getNonTerminalIndex(stack.peek())][icGrammar.getTerminals().size()] != -1){
                    //System.out.print(stack.peek() + "\t");
                    productionOrderList.add(parsingTable[icGrammar.getNonTerminalIndex(stack.peek())][icGrammar.getTerminals().size()]);
                    stack.pop();
                    //System.out.print(stack.peek() + "\n");
                    continue;
                }
            }
            if(stack.peek().equals(sequence.get(sequenceIndex))){
                stack.pop();
                sequenceIndex++;
            }
            else if(icGrammar.isTerminal(stack.peek())){
                throw new Exception("unsuitable terminal on the top of the stack");
            }
            else if(parsingTable[icGrammar.getNonTerminalIndex(stack.peek())]
                    [icGrammar.getTerminalIndex(sequence.get(sequenceIndex))] == -1){
                throw new Exception("the string is not suitable");
            }

            else {
                /*get the production index:*/
                int productionIndex = parsingTable[icGrammar.getNonTerminalIndex(stack.peek())]
                        [icGrammar.getTerminalIndex(sequence.get(sequenceIndex))];
                productionOrderList.add(productionIndex);
                stack.pop();
                Production production = productionList.get(productionIndex);
                List<String> rightElements = production.getRightElements();
                /*push the right elements of the production into the stack, in reverse order:*/
                for(int elementIndex = rightElements.size() - 1;elementIndex > -1;elementIndex--){
                    if(!rightElements.get(elementIndex).equals(epsilon))stack.push(rightElements.get(elementIndex));
                }
            }
        }

        return productionOrderList;
    }

    /**analyze the given sequence and return a list with the used productions indices;*/
    public static List<Integer> analyzeSequence(List<String> sequence, ICGrammar icGrammar)
            throws Exception {
        int[][] parsingTable = computeParsingTable(icGrammar);
        if(parsingTable == null){
            /*if the grammar is not LL1, then return null*/
            throw new Exception("the given grammar is not LL1");
        }
        List<Integer> productionOrderList = new ArrayList<>();

        List<Production> productionList = icGrammar.getProductionRules();
        String epsilon = icGrammar.getEpsilon();
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push(icGrammar.getStartSymbol());

        int sequenceIndex = 0;
        while(!stack.peek().equals("$")){
            //System.out.print(stack + "\t");
            //System.out.println(sequence.subList(sequenceIndex, sequence.size()));
            if(sequence.get(sequenceIndex).equals("$")){
                if(parsingTable[icGrammar.getNonTerminalIndex(stack.peek())][icGrammar.getTerminals().size()] != -1){
                    //System.out.print(stack.peek() + "\t");
                    productionOrderList.add(parsingTable[icGrammar.getNonTerminalIndex(stack.peek())][icGrammar.getTerminals().size()]);
                    stack.pop();
                    //System.out.print(stack.peek() + "\n");
                    continue;
                }
            }
            if(stack.peek().equals(sequence.get(sequenceIndex))){
                stack.pop();
                sequenceIndex++;
            }
            else if(icGrammar.isTerminal(stack.peek())){
                throw new Exception("unsuitable terminal on the top of the stack");
            }
            else if(parsingTable[icGrammar.getNonTerminalIndex(stack.peek())]
                    [icGrammar.getTerminalIndex(sequence.get(sequenceIndex))] == -1){
                throw new Exception("the string is not suitable");
            }

            else {
                /*get the production index:*/
                int productionIndex = parsingTable[icGrammar.getNonTerminalIndex(stack.peek())]
                        [icGrammar.getTerminalIndex(sequence.get(sequenceIndex))];
                productionOrderList.add(productionIndex);
                stack.pop();
                Production production = productionList.get(productionIndex);
                List<String> rightElements = production.getRightElements();
                /*push the right elements of the production into the stack, in reverse order:*/
                for(int elementIndex = rightElements.size() - 1;elementIndex > -1;elementIndex--){
                    if(!rightElements.get(elementIndex).equals(epsilon))stack.push(rightElements.get(elementIndex));
                }
            }
        }

        return productionOrderList;
    }

    /***/
    public static int[][] computeParsingTable(ICGrammar icGrammar){

        List<String> nonTerminals = icGrammar.getNonTerminals();
        List<String> terminals = icGrammar.getTerminals();
        List<Production> productions = icGrammar.getProductionRules();

        int numberOfNonTerminals = nonTerminals.size();
        int numberOfTerminals = terminals.size();
        int parsingTable[][] = new int[numberOfNonTerminals][numberOfTerminals+1];
        for(int row = 0;row < numberOfNonTerminals;row++){
            for(int col = 0;col < numberOfTerminals+1;col++){
                parsingTable[row][col] = -1;
            }
        }
        List<HashSet<String>> firstSets = computeFirstSets(icGrammar);
        List<HashSet<String>> followSets = computeFollowSets(icGrammar, firstSets);

        String epsilon = icGrammar.getEpsilon();

        for(int productionIndex = 0;productionIndex < productions.size();productionIndex++){
            Production currentProduction = productions.get(productionIndex);
            String firstRightElement = currentProduction.getRightElements().get(0);
            String leftElement = currentProduction.getLeftElement();
            int leftElementIndex = icGrammar.getNonTerminalIndex(leftElement);
            /*if the */
            if(icGrammar.isNonTerminal(firstRightElement)){
                HashSet<String> firstSet = firstSets.get(icGrammar.getNonTerminalIndex(firstRightElement));
                for(String terminal : firstSet){
                    int terminalIndex = -1;
                    if(!terminal.equals("$")){
                        terminalIndex = icGrammar.getTerminalIndex(terminal);
                    }
                    else { terminalIndex = numberOfTerminals; }
                    if(parsingTable[leftElementIndex][terminalIndex] != -1){
                        return null;
                    }
                    parsingTable[leftElementIndex][terminalIndex] = productionIndex;
                }
                if(firstSet.contains(epsilon)){
                    HashSet<String> followSet  = followSets.get(leftElementIndex);
                    for(String terminal : followSet){
                        int terminalIndex = -1;
                        if(!terminal.equals("$")){ terminalIndex = icGrammar.getTerminalIndex(terminal);}
                        else {terminalIndex = numberOfTerminals; }
                        if(parsingTable[leftElementIndex][terminalIndex] != -1){
                            return null;
                        }
                        parsingTable[leftElementIndex][terminalIndex] = productionIndex;
                    }
                }
            }
            else if(icGrammar.isTerminal(firstRightElement)){
                if(!firstRightElement.equals(epsilon)){
                    int terminalIndex = icGrammar.getTerminalIndex(firstRightElement);
                    if(parsingTable[leftElementIndex][terminalIndex] != -1){
                        return null;
                    }
                    parsingTable[leftElementIndex][terminalIndex] = productionIndex;
                }
                else{
                    HashSet<String> followSet  = followSets.get(leftElementIndex);
                    for(String terminal : followSet){
                        int terminalIndex = -1;
                        if(!terminal.equals("$")){terminalIndex = icGrammar.getTerminalIndex(terminal);}
                        else {terminalIndex = numberOfTerminals;}
                        if(parsingTable[leftElementIndex][terminalIndex] != -1){
                            return null;
                        }
                        parsingTable[leftElementIndex][terminalIndex] = productionIndex;
                    }
                }
            }
        }

        return parsingTable;
    }

    public static int[][] computeParsingTable(ICGrammar icGrammar, List<HashSet<String>> firstSets,
                                              List<HashSet<String>> followSets){

        List<String> nonTerminals = icGrammar.getNonTerminals();
        List<String> terminals = icGrammar.getTerminals();
        List<Production> productions = icGrammar.getProductionRules();

        String epsilon = icGrammar.getEpsilon();

        int numberOfNonTerminals = icGrammar.getNonTerminals().size();
        int numberOfTerminals = icGrammar.getTerminals().size();
        int parsingTable[][] = new int[numberOfNonTerminals][numberOfTerminals+1];
        for(int row = 0;row < numberOfNonTerminals;row++){
            for(int col = 0;col < numberOfTerminals+1;col++){
                parsingTable[row][col] = -1;
            }
        }

        for(int productionIndex = 0;productionIndex < productions.size();productionIndex++){
            String firstRightElement = productions.get(productionIndex).getRightElements().get(0);
            String leftElement = productions.get(productionIndex).getLeftElement();
            int leftElementIndex = icGrammar.getNonTerminalIndex(leftElement);
            /*if the */
            if(icGrammar.isNonTerminal(firstRightElement)){
                HashSet<String> firstSet = firstSets.get(icGrammar.getNonTerminalIndex(firstRightElement));
                for(String terminal : firstSet){
                    int terminalIndex = icGrammar.getTerminalIndex(terminal);
                    if(parsingTable[leftElementIndex][terminalIndex] != -1){
                        return null;
                    }
                    parsingTable[leftElementIndex][terminalIndex] = productionIndex;
                }
                if(firstSet.contains(epsilon)){
                    HashSet<String> followSet  = followSets.get(leftElementIndex);
                    for(String terminal : followSet){
                        int terminalIndex = icGrammar.getTerminalIndex(terminal);
                        if(parsingTable[leftElementIndex][terminalIndex] != -1){
                            return null;
                        }
                        parsingTable[leftElementIndex][terminalIndex] = productionIndex;
                    }
                    if(followSet.contains("$")){
                        if(parsingTable[leftElementIndex][numberOfTerminals] != -1){
                            return null;
                        }
                        parsingTable[leftElementIndex][numberOfNonTerminals] = productionIndex;
                    }
                }
            }
            else if(icGrammar.isTerminal(firstRightElement)){
                if(!firstRightElement.equals(epsilon)){
                    int terminalIndex = icGrammar.getTerminalIndex(firstRightElement);
                    if(parsingTable[leftElementIndex][terminalIndex] != -1){
                        return null;
                    }
                    parsingTable[leftElementIndex][terminalIndex] = productionIndex;
                }
                else{
                    HashSet<String> followSet  = followSets.get(leftElementIndex);
                    for(String terminal : followSet){
                        if(!terminal.equals("$")){
                            int terminalIndex = icGrammar.getTerminalIndex(terminal);
                            if(parsingTable[leftElementIndex][terminalIndex] != -1){
                                return null;
                            }
                            parsingTable[leftElementIndex][terminalIndex] = productionIndex;
                        }
                    }
                    if(followSet.contains("$")){
                        if(parsingTable[leftElementIndex][numberOfTerminals] != -1){
                            return null;
                        }
                        parsingTable[leftElementIndex][numberOfNonTerminals] = productionIndex;
                    }
                }
            }
        }

        return parsingTable;
    }

    /**This method returns the firstSets of a given context-free grammar;*/
    public static List<HashSet<String>> computeFirstSets(ICGrammar icGrammar){
        List<String> terminals = icGrammar.getTerminals();
        List<String> nonTerminals = icGrammar.getNonTerminals();
        List<Production> productions = icGrammar.getProductionRules();
        String epsilon = icGrammar.getEpsilon();
        List<HashSet<String>> firstSets = new ArrayList<HashSet<String>>();
        for(int i = 0;i < nonTerminals.size();i++){
            firstSets.add(new HashSet<>());
        }
        /**first, compute the grade for each non-terminal, where the grade is the set of unresolved non-terminals that
         * appear on the first position in a specific production;
         * */
        List<HashSet<String>> grades = new ArrayList<HashSet<String>>();
        for(int i = 0;i < icGrammar.getNonTerminals().size();i++){
            grades.add(new HashSet<>());
        }

        for(int i = 0;i < productions.size();i++){
            Production production = productions.get(i);
            /*update the corresponding set only if the first production element is a non-terminal:*/
            if(icGrammar.isNonTerminal(production.getRightElements().get(0))){
                int leftNonTerminalIndex = icGrammar.getNonTerminalIndex(production.getLeftElement());
                HashSet<String> oldSet = grades.get(leftNonTerminalIndex);
                oldSet.add(production.getRightElements().get(0));
                grades.set(leftNonTerminalIndex, oldSet);
            }
            else if(icGrammar.isTerminal(production.getRightElements().get(0))){
                /*update the corresponding firstSet element:*/
                int leftNonTerminalIndex = icGrammar.getNonTerminalIndex(production.getLeftElement());
                HashSet<String> oldSet = firstSets.get(leftNonTerminalIndex);
                oldSet.add(production.getRightElements().get(0));
                firstSets.set(leftNonTerminalIndex, oldSet);
            }
        }

        /*then, add all the nonTerminals with empty grade to the queue:
         */
        Queue<String> queue = new LinkedList<>();
        for(int i = 0;i < nonTerminals.size();i++){
            if(grades.get(i).size() == 0){
                queue.add(nonTerminals.get(i));
            }
        }

        /**this is some kind of topological-sort, in that we always compute the first entry for the "free"
         * non-terminals, i.e. the non-terminals with the non-terminals on the first position from the right side
         * in all the productions
         * resolved
         * */
        boolean []isVisited = new boolean[nonTerminals.size()];
        for(int i = 0;i < nonTerminals.size();i++)isVisited[i] = false;
        while(!queue.isEmpty()){
            String x = queue.remove();
            isVisited[icGrammar.getNonTerminalIndex(x)] = true;
            /*erase this String from each set:*/
            for(int i = 0;i < grades.size();i++){
                HashSet<String> oldSet = grades.get(i);
                if(!oldSet.contains(x))continue;
                oldSet.remove(x);
                grades.set(i, oldSet);
                /*add the set of the free element to the set that contains it on the 1st position:*/
                Set<String> oldFirstSet = firstSets.get(i);
                Set<String> freeElements = firstSets.get(icGrammar.getNonTerminalIndex(x));
                oldFirstSet.addAll(freeElements);
                firstSets.set(i, (HashSet<String>) oldFirstSet);
            }
            for(int i = 0;i < nonTerminals.size();i++){
                if(grades.get(i).size() == 0 && isVisited[i] == false){
                    /*push the new "free" non-terminal in the queue:*/
                    queue.add(nonTerminals.get(i));
                }
            }
        }

        return firstSets;
    }

    /**This method returns the follow sets of a given context-free grammar, knowing
     *  the given first set:*/
    public static List<HashSet<String>> computeFollowSets(ICGrammar icGrammar, List<HashSet<String>> firstSets){
        try{
            List<String> terminals = icGrammar.getTerminals();
            List<String> nonTerminals = icGrammar.getNonTerminals();
            List<Production> productions = icGrammar.getProductionRules();
            List<HashSet<String>> followSets = new ArrayList<>();
            String epsilon = icGrammar.getEpsilon();
            for(int i = 0;i < nonTerminals.size();i++){
                followSets.add(new HashSet<>());
            }
            int indexOfStartSymbol = icGrammar.getNonTerminalIndex(icGrammar.getStartSymbol());
            /*place the input right end-marker $ in followSets(indexOfStartSymbol):*/
            updateSets("$", indexOfStartSymbol, followSets);
            /**/
            boolean changeMade = true;
            while(changeMade){
                changeMade = false;
                for(int productionIndex = 0;productionIndex < productions.size();productionIndex++){
                    Production currentProduction = productions.get(productionIndex);
                    for(int rightIndex = 0;rightIndex < currentProduction.getRightElements().size();rightIndex++){
                        String currentRightResult = currentProduction.getRightElements().get(rightIndex);
                        if(icGrammar.isNonTerminal(currentRightResult)){
                            int currentNonTerminalIndex = icGrammar.getNonTerminalIndex(currentRightResult);
                            /*simply add the following non-terminal's first set to the current non-terminal's follow
                            * set:*/
                            if(rightIndex + 1 < currentProduction.getRightElements().size() &&
                                    icGrammar.isNonTerminal(currentProduction.getRightElements().get(rightIndex+1))
                                    ){
                                int nextNonTerminalIndex = icGrammar.getNonTerminalIndex(
                                        currentProduction.getRightElements().get(rightIndex+1));
                                HashSet<String> nextFirstSet = firstSets.get(nextNonTerminalIndex);
                                changeMade = changeMade || updateSets(
                                        nextFirstSet,
                                        currentNonTerminalIndex,
                                        followSets
                                );
                            }
                            /*if the following element is a terminal, simply add it to the current
                            * non-terminal's follow set:
                            * */
                            if(rightIndex + 1 < currentProduction.getRightElements().size() &&
                                    icGrammar.isTerminal(currentProduction.getRightElements().get(rightIndex+1))
                                    ){
                                //System.out.println(currentRightResult + " " + currentProduction.getRightElements().get(rightIndex+1));
                                changeMade = changeMade || updateSets(
                                        currentProduction.getRightElements().get(rightIndex+1),
                                        currentNonTerminalIndex,
                                        followSets);
                            }
                            /*if the following element is a non-terminal and its corresponding first set contains
                            * epsilon, then add the left element's follow set to the current non-terminal's follow
                            * set:
                            * */
                            if(rightIndex + 1 < currentProduction.getRightElements().size() &&
                                    icGrammar.isNonTerminal(currentProduction.getRightElements().get(rightIndex+1)) &&
                                    firstSets
                                            .get(
                                                    icGrammar
                                                            .getNonTerminalIndex(
                                                                    currentProduction
                                                                            .getRightElements()
                                                                            .get(rightIndex+1)
                                                            )
                                            )
                                            .contains(epsilon)
                                    ){
                                HashSet<String> leftFollowSet = followSets
                                        .get(icGrammar.getNonTerminalIndex(currentProduction.getLeftElement()));
                                changeMade = updateSets(
                                        leftFollowSet,
                                        currentNonTerminalIndex,
                                        followSets
                                ) || changeMade;
                            }
                            /*if the current non-terminal is the last right element, then add the left element's
                            * follow set to the current right element follow set:
                            * */
                            if(rightIndex + 1 == currentProduction.getRightElements().size()){
                                HashSet<String> leftFollowSet = followSets
                                        .get(icGrammar.getNonTerminalIndex(currentProduction.getLeftElement()));
                                changeMade = updateSets(
                                        leftFollowSet,
                                        currentNonTerminalIndex,
                                        followSets
                                ) || changeMade;
                            }
                        }
                    }
                }
            }
            /*remove epsilon from each set:*/
            for(int nonTerminalIndex = 0;nonTerminalIndex < nonTerminals.size();nonTerminalIndex++){
                HashSet<String> oldSet = followSets.get(nonTerminalIndex);
                oldSet.remove(epsilon);
                followSets.set(nonTerminalIndex, oldSet);
            }
            return followSets;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**return true if newItem doesn't exist in the corresponding set;*/
    public static boolean updateSets(String newItem, int index, List<HashSet<String>> sets){
        HashSet<String> oldSet = sets.get(index);
        if(oldSet.contains(newItem))return false;
        oldSet.add(newItem);
        sets.set(index, oldSet);
        return true;
    }

    /**return true if newSet contains elements that do not exist in the corresponding set;*/
    public static boolean updateSets(HashSet<String> newSet, int index, List<HashSet<String>> sets){
        HashSet<String> oldSet = sets.get(index);
        if(oldSet.containsAll(newSet))return false;
        oldSet.addAll(newSet);
        sets.set(index, oldSet);
        return true;
    }

}
