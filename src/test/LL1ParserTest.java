package test;

import application.LL1Parser;
import model.ICGrammar;
import model.Production;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LL1ParserTest {

    @Test
    void computeFirstSets() {
        /*test 1:*/
        ICGrammar icGrammar = new ICGrammar(
                Arrays.asList("(", ")", "int", "*", "epsilon", "+"),
                Arrays.asList("program", "expr", "term", "expr_tail", "factor", "term_tail"),
                Arrays.asList(
                        new Production(
                                "program",
                                Arrays.asList("expr")
                        ),
                        new Production(
                                "expr",
                                Arrays.asList("term", "expr_tail")
                        ),
                        new Production(
                                "expr_tail",
                                Arrays.asList("+", "term", "expr_tail")
                        ),
                        new Production(
                                "expr_tail",
                                Arrays.asList("epsilon")
                        ),
                        new Production(
                                "term",
                                Arrays.asList("factor", "term_tail")
                        ),
                        new Production(
                                "term_tail",
                                Arrays.asList("*", "factor", "term_tail")
                        ),
                        new Production(
                                "term_tail",
                                Arrays.asList("epsilon")
                        ),
                        new Production(
                                "factor",
                                Arrays.asList("(", "expr", ")")
                        ),
                        new Production(
                                "factor",
                                Arrays.asList("int")
                        )
                )
        );
        List<HashSet<String>> computedResult1 = LL1Parser.computeFirstSets(icGrammar);
        List<HashSet<String>> expectedResult1 = Arrays.asList(
                new HashSet<>(Arrays.asList("(", "int")),
                new HashSet<>(Arrays.asList("(", "int")),
                new HashSet<>(Arrays.asList("(", "int")),
                new HashSet<>(Arrays.asList("epsilon", "+")),
                new HashSet<>(Arrays.asList("(", "int")),
                new HashSet<>(Arrays.asList("epsilon", "*"))
        );
        Assertions.assertTrue(computedResult1.equals(expectedResult1));

        /*test 2:*/
        ICGrammar icGrammar2 = new ICGrammar(
                Arrays.asList("+", "epsilon", "*", "id", "(", ")"),
                Arrays.asList("E", "T", "E2", "F", "T2"),
                Arrays.asList(
                        new Production(
                                "E",
                                Arrays.asList("T", "E2")
                        ),
                        new Production(
                                "E2",
                                Arrays.asList("+", "T", "E2")
                        ),
                        new Production(
                                "E2",
                                Arrays.asList("epsilon")
                        ),
                        new Production(
                                "T",
                                Arrays.asList("F", "T2")
                        ),
                        new Production(
                                "T2",
                                Arrays.asList("*", "F", "T2")
                        ),
                        new Production(
                                "T2",
                                Arrays.asList("epsilon")
                        ),
                        new Production(
                                "F",
                                Arrays.asList("(", "E", ")")
                        ),
                        new Production(
                                "F",
                                Arrays.asList("id")
                        )
                )
        );
        List<HashSet<String>> expectedResult2 = Arrays.asList(
                new HashSet<>(Arrays.asList("(", "id")),
                new HashSet<>(Arrays.asList("(", "id")),
                new HashSet<>(Arrays.asList("epsilon", "+")),
                new HashSet<>(Arrays.asList("(", "id")),
                new HashSet<>(Arrays.asList("epsilon", "*"))
        );
        List<HashSet<String>> computedResult2 = LL1Parser.computeFirstSets(icGrammar2);
        Assertions.assertTrue(expectedResult2.equals(computedResult2));

        /*Test 3:*/
        ICGrammar testIcGrammar3 = new ICGrammar();
        testIcGrammar3.setStartSymbol("E");
        testIcGrammar3.setTerminals(Arrays.asList("+", "epsilon", "*", "id", "(", ")"));
        testIcGrammar3.setNonTerminals(Arrays.asList("E", "T", "E2", "T2", "F"));
        testIcGrammar3.setProductionRules(Arrays.asList(
                new Production("E", Arrays.asList("T", "E2")),
                new Production("E2", Arrays.asList("+", "T", "E2")),
                new Production("E2", Arrays.asList("epsilon")),
                new Production("T", Arrays.asList("F", "T2")),
                new Production("T2", Arrays.asList("*", "F", "T2")),
                new Production("T2", Arrays.asList("epsilon")),
                new Production("F", Arrays.asList("(", "E", ")")),
                new Production("F", Arrays.asList("id"))
        ));
        List<String> nonTerminals3 = testIcGrammar3.getNonTerminals();
        List<String> terminals3 = testIcGrammar3.getTerminals();
        List<HashSet<String>> computedFirstSets3 = LL1Parser.computeFirstSets(testIcGrammar3);
        /*
        for(int nonTerminalIndex = 0;nonTerminalIndex < nonTerminals3.size();nonTerminalIndex++){
            System.out.printf("%s\t", nonTerminals3.get(nonTerminalIndex));
            for(String symbol : computedFirstSets3.get(nonTerminalIndex)){
                System.out.printf("%s, ", symbol);
            }
            System.out.print("\n");
        }
        */
        List<HashSet<String>> expectedFirstSets3 = Arrays.asList(
                new HashSet<>(Arrays.asList("id", "(")),
                new HashSet<>(Arrays.asList("(", "id")),
                new HashSet<>(Arrays.asList("+", "epsilon")),
                new HashSet<>(Arrays.asList("epsilon", "*")),
                new HashSet<>(Arrays.asList("id", "("))
        );
        Assertions.assertTrue(computedFirstSets3.equals(expectedFirstSets3));
    }

    @Test
    void computeFollowSets(){
        /*test 1:*/
        ICGrammar testICGrammar1 = new ICGrammar();
        testICGrammar1.setStartSymbol("program");
        testICGrammar1.setTerminals(
                Arrays.asList("(", ")", "int", "*", "epsilon", "+")
        );
        testICGrammar1.setNonTerminals(
                Arrays.asList("program", "expr", "term", "expr_tail", "factor", "term_tail")
        );
        testICGrammar1.setProductionRules(
                Arrays.asList(
                        new Production(
                                "program",
                                Arrays.asList("expr")
                        ),
                        new Production(
                                "expr",
                                Arrays.asList("term", "expr_tail")
                        ),
                        new Production(
                                "expr_tail",
                                Arrays.asList("+", "term", "expr_tail")
                        ),
                        new Production(
                                "expr_tail",
                                Arrays.asList("epsilon")
                        ),
                        new Production(
                                "term",
                                Arrays.asList("factor", "term_tail")
                        ),
                        new Production(
                                "term_tail",
                                Arrays.asList("*", "factor", "term_tail")
                        ),
                        new Production(
                                "term_tail",
                                Arrays.asList("epsilon")
                        ),
                        new Production(
                                "factor",
                                Arrays.asList("(", "expr", ")")
                        ),
                        new Production(
                                "factor",
                                Arrays.asList("int")
                        )
                )
        );
        List<HashSet<String>> firstSets1 = LL1Parser.computeFirstSets(testICGrammar1);
        List<HashSet<String>> computedFollowSets1 = LL1Parser.computeFollowSets(testICGrammar1, firstSets1);
        List<HashSet<String>> expectedFollowSets1 = Arrays.asList(
                new HashSet<>(Arrays.asList("$")),
                new HashSet<>(Arrays.asList("$", ")")),
                new HashSet<>(Arrays.asList("+", "$", ")")),
                new HashSet<>(Arrays.asList("$", ")")),
                new HashSet<>(Arrays.asList("*", "+", "$", ")")),
                new HashSet<>(Arrays.asList("+", "$", ")"))
        );
        Assertions.assertTrue(expectedFollowSets1.equals(computedFollowSets1));

        /*test 2:*/
        ICGrammar testICGrammar2 = new ICGrammar();
        testICGrammar2.setStartSymbol("E");
        testICGrammar2.setTerminals(Arrays.asList("+", "epsilon", "*", "id", "(", ")"));
        testICGrammar2.setNonTerminals(Arrays.asList("E", "T", "E2", "T2", "F"));
        testICGrammar2.setProductionRules(Arrays.asList(
                new Production("E", Arrays.asList("T", "E2")),
                new Production("E2", Arrays.asList("+", "T", "E2")),
                new Production("E2", Arrays.asList("epsilon")),
                new Production("T", Arrays.asList("F", "T2")),
                new Production("T2", Arrays.asList("*", "F", "T2")),
                new Production("T2", Arrays.asList("epsilon")),
                new Production("F", Arrays.asList("(", "E", ")")),
                new Production("F", Arrays.asList("id"))
        ));
        List<HashSet<String>> firstSet2 = LL1Parser.computeFirstSets(testICGrammar2);
        List<HashSet<String>> followSet2 = LL1Parser.computeFollowSets(testICGrammar2, firstSet2);

        List<HashSet<String>> expectedFollowSet2 = Arrays.asList(
                new HashSet<>(Arrays.asList(")", "$")),
                new HashSet<>(Arrays.asList("+", ")", "$")),
                new HashSet<>(Arrays.asList("$", ")")),
                new HashSet<>(Arrays.asList(")", "$", "+")),
                new HashSet<>(Arrays.asList("*", "$", ")", "+"))
        );
        Assertions.assertTrue(expectedFollowSet2.equals(followSet2));
        /*
        List<String> nonTerminals = testICGrammar2.getNonTerminals();
        for(int i = 0;i < nonTerminals.size();i++){
            System.out.printf("%s\t", nonTerminals.get(i));
            for(String x : followSet2.get(i)){
                System.out.printf("%s, ", x);
            }
            System.out.print("\n");
        }
        */
    }

    @Test
    void computeParsingTableTest(){
        /*test 1:*/
        System.out.print("Test 1: ");
        ICGrammar testICGrammar1 = new ICGrammar();
        testICGrammar1.setStartSymbol("program");
        testICGrammar1.setTerminals(
                Arrays.asList("(", ")", "int", "*", "epsilon", "+")
        );
        testICGrammar1.setNonTerminals(
                Arrays.asList("program", "expr", "term", "expr_tail", "factor", "term_tail")
        );
        testICGrammar1.setProductionRules(
                Arrays.asList(
                        new Production(
                                "program",
                                Arrays.asList("expr")
                        ),
                        new Production(
                                "expr",
                                Arrays.asList("term", "expr_tail")
                        ),
                        new Production(
                                "expr_tail",
                                Arrays.asList("+", "term", "expr_tail")
                        ),
                        new Production(
                                "expr_tail",
                                Arrays.asList("epsilon")
                        ),
                        new Production(
                                "term",
                                Arrays.asList("factor", "term_tail")
                        ),
                        new Production(
                                "term_tail",
                                Arrays.asList("*", "factor", "term_tail")
                        ),
                        new Production(
                                "term_tail",
                                Arrays.asList("epsilon")
                        ),
                        new Production(
                                "factor",
                                Arrays.asList("(", "expr", ")")
                        ),
                        new Production(
                                "factor",
                                Arrays.asList("int")
                        )
                )
        );
        int [][] computedParsingTable1 = LL1Parser.computeParsingTable(testICGrammar1);
        int [][] expectedParsingTable1 = new int[testICGrammar1.getNonTerminals().size()][testICGrammar1.getTerminals().size() + 1];
        for(int row = 0;row < testICGrammar1.getNonTerminals().size();row++){
            for(int col = 0;col <= testICGrammar1.getTerminals().size();col++)expectedParsingTable1[row][col] = -1;
        }
        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("program")][testICGrammar1.getTerminalIndex("(")] = 0;
        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("program")][testICGrammar1.getTerminalIndex("int")] = 0;

        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("expr")][testICGrammar1.getTerminalIndex("(")] = 1;
        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("expr")][testICGrammar1.getTerminalIndex("int")] = 1;

        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("expr_tail")][testICGrammar1.getTerminalIndex("+")] = 2;
        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("expr_tail")][testICGrammar1.getTerminalIndex(")")] = 3;
        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("expr_tail")][testICGrammar1.getTerminals().size()] = 3;

        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("term")][testICGrammar1.getTerminalIndex("(")] = 4;
        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("term")][testICGrammar1.getTerminalIndex("int")] = 4;

        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("term_tail")][testICGrammar1.getTerminalIndex("*")] = 5;
        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("term_tail")][testICGrammar1.getTerminalIndex("+")] = 6;
        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("term_tail")][testICGrammar1.getTerminalIndex(")")] = 6;
        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("term_tail")][testICGrammar1.getTerminals().size()] = 6;

        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("factor")][testICGrammar1.getTerminalIndex("(")] = 7;
        expectedParsingTable1[testICGrammar1.getNonTerminalIndex("factor")][testICGrammar1.getTerminalIndex("int")] = 8;

        for(int row = 0;row < testICGrammar1.getNonTerminals().size();row++){
            for(int col = 0;col <= testICGrammar1.getTerminals().size();col++){
                Assertions.assertTrue(computedParsingTable1[row][col] == expectedParsingTable1[row][col]);
            }
        }
        System.out.print("OK\n");
        /*test 2:*/
        System.out.print("Test 2: ");
        ICGrammar testICGrammar2 = new ICGrammar();
        testICGrammar2.setStartSymbol("E");
        testICGrammar2.setTerminals(Arrays.asList("+", "epsilon", "*", "id", "(", ")"));
        testICGrammar2.setNonTerminals(Arrays.asList("E", "T", "E2", "T2", "F"));
        testICGrammar2.setProductionRules(Arrays.asList(
                new Production("E", Arrays.asList("T", "E2")),
                new Production("E2", Arrays.asList("+", "T", "E2")),
                new Production("E2", Arrays.asList("epsilon")),
                new Production("T", Arrays.asList("F", "T2")),
                new Production("T2", Arrays.asList("*", "F", "T2")),
                new Production("T2", Arrays.asList("epsilon")),
                new Production("F", Arrays.asList("(", "E", ")")),
                new Production("F", Arrays.asList("id"))
        ));
        int[][] computedParsingTable2 = LL1Parser.computeParsingTable(testICGrammar2);
        int[][] expectedParsingTable2 = new int[testICGrammar2.getNonTerminals().size()][testICGrammar2.getTerminals().size()+1];

        for(int row = 0;row < testICGrammar2.getNonTerminals().size();row++){
            for(int col = 0;col <= testICGrammar2.getTerminals().size();col++){expectedParsingTable2[row][col] = -1;}
        }

        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("E")][testICGrammar2.getTerminalIndex("id")] = 0;
        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("E")][testICGrammar2.getTerminalIndex("(")] = 0;

        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("E2")][testICGrammar2.getTerminalIndex("+")] = 1;
        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("E2")][testICGrammar2.getTerminalIndex(")")] = 2;
        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("E2")][testICGrammar2.getTerminals().size()] = 2;

        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("T")][testICGrammar2.getTerminalIndex("id")] = 3;
        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("T")][testICGrammar2.getTerminalIndex("(")] = 3;

        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("T2")][testICGrammar2.getTerminalIndex("+")] = 5;
        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("T2")][testICGrammar2.getTerminalIndex("*")] = 4;
        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("T2")][testICGrammar2.getTerminalIndex(")")] = 5;
        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("T2")][testICGrammar2.getTerminals().size()] = 5;

        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("F")][testICGrammar2.getTerminalIndex("id")] = 7;
        expectedParsingTable2[testICGrammar2.getNonTerminalIndex("F")][testICGrammar2.getTerminalIndex("(")] = 6;

        for(int row = 0;row < testICGrammar2.getNonTerminals().size();row++){
            for(int col = 0;col <= testICGrammar2.getTerminals().size();col++){
                Assertions.assertTrue(computedParsingTable2[row][col] == expectedParsingTable2[row][col]);
            }
        }
        //printParsingTable(computedParsingTable2, testICGrammar2);
        System.out.print("OK\n");

    }

    @Test
    void analyzeSequenceTest(){
        /*test 1:*/
        try{
            ICGrammar testICGrammar1 = new ICGrammar();
            testICGrammar1.setStartSymbol("E");
            testICGrammar1.setTerminals(Arrays.asList("+", "epsilon", "*", "id", "(", ")"));
            testICGrammar1.setNonTerminals(Arrays.asList("E", "T", "E2", "T2", "F"));
            testICGrammar1.setProductionRules(Arrays.asList(
                    new Production("E", Arrays.asList("T", "E2")),
                    new Production("E2", Arrays.asList("+", "T", "E2")),
                    new Production("E2", Arrays.asList("epsilon")),
                    new Production("T", Arrays.asList("F", "T2")),
                    new Production("T2", Arrays.asList("*", "F", "T2")),
                    new Production("T2", Arrays.asList("epsilon")),
                    new Production("F", Arrays.asList("(", "E", ")")),
                    new Production("F", Arrays.asList("id"))
            ));
            int[][] computedParsingTable1 = LL1Parser.computeParsingTable(testICGrammar1);
            List<String> testSequence1 = Arrays.asList("id", "+", "id", "*", "id", "$");
            List<Integer> computedProductionOrderList1 = LL1Parser.analyzeSequence(testSequence1, computedParsingTable1, testICGrammar1);
            //System.out.println(computedProductionOrderList1);
            List<Integer> expectedProductionOrderList1 = Arrays.asList(1, 4, 8, 6, 2, 4, 8, 5, 8, 6, 3);
            Assertions.assertTrue(computedProductionOrderList1.size() == expectedProductionOrderList1.size());
            for(int i = 0;i < computedProductionOrderList1.size();i++){
                Assertions.assertTrue(computedProductionOrderList1.get(i) + 1 == expectedProductionOrderList1.get(i));
            }
        }
        catch (Exception e){
            System.out.println("Error in test 1:" + e.getMessage());
        }
        /*test 2:*/
        try{
            ICGrammar testICGrammar2 = new ICGrammar();
            testICGrammar2.setStartSymbol("program");
            testICGrammar2.setTerminals(Arrays.asList("+", "epsilon", "*", "(", ")", "int"));
            testICGrammar2.setNonTerminals(Arrays.asList("program", "expr", "term", "expr_tail", "factor", "term_tail"));
            testICGrammar2.setProductionRules(Arrays.asList(
                    new Production("program", Arrays.asList("expr")),
                    new Production("expr", Arrays.asList("term", "expr_tail")),
                    new Production("expr_tail", Arrays.asList("+", "term", "expr_tail")),
                    new Production("expr_tail", Arrays.asList("epsilon")),
                    new Production("term", Arrays.asList("factor", "term_tail")),
                    new Production("term_tail", Arrays.asList("*", "factor", "term_tail")),
                    new Production("term_tail", Arrays.asList("epsilon")),
                    new Production("factor", Arrays.asList("(", "expr", ")")),
                    new Production("factor", Arrays.asList("int"))
            ));
            //testICGrammar2.setEpsilon("epsilon");
            List<String> inputSequence2 = Arrays.asList("int", "*", "(", "int", "+", "int", ")", "*", "int", "$");
            List<Integer> computedProductionOrderList2 = LL1Parser.analyzeSequence(inputSequence2, testICGrammar2);
            List<Integer> expectedProductionOrderList2 = Arrays.asList(
                    1, 2, 5, 9, 6, 8, 2, 5, 9, 7, 3, 5, 9, 7, 4, 6, 9, 7, 4
            );
            Assertions.assertTrue(computedProductionOrderList2.size() == expectedProductionOrderList2.size());
            for(int i = 0;i < computedProductionOrderList2.size();i++){
                Assertions.assertTrue(computedProductionOrderList2.get(i)+1 == expectedProductionOrderList2.get(i));
            }
        }catch (Exception e){
            System.out.println("Error in test 2:" + e.getMessage());
        }
    }

    public void printParsingTable(int[][] parsingTable, ICGrammar icGrammar){
        List<String> nonTerminals = icGrammar.getNonTerminals();
        List<String> terminals = icGrammar.getTerminals();
        for(int nonTerminalIndex = 0;nonTerminalIndex < nonTerminals.size();nonTerminalIndex++){
            String currentNonTerminal = nonTerminals.get(nonTerminalIndex);
            System.out.printf("%s\t\t", currentNonTerminal);
            for(int terminalIndex = 0; terminalIndex < terminals.size()+1;terminalIndex++){
                if(terminalIndex == terminals.size()){
                    System.out.printf("($, %d), ", parsingTable[nonTerminalIndex][terminalIndex] + 1);
                }
                else{
                    String currentTerminal = terminals.get(terminalIndex);
                    if(currentTerminal.equals("epsilon"))continue;
                    System.out.printf("(%s, %d), ", currentTerminal, parsingTable[nonTerminalIndex][terminalIndex] + 1);
                }
            }
            System.out.print("\n");
        }
    }

}