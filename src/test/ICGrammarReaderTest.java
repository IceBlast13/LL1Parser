package test;

import model.ICGrammar;
import model.Production;
import reader.ICGrammarReader;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ICGrammarReaderTest {

    @org.junit.jupiter.api.Test
    void readICGrammar() {
        ICGrammar icResult = ICGrammarReader.readICGrammar("inputTest.txt");
        ICGrammar icExpected = new ICGrammar(Arrays.asList("a", "b", "epsilon"), Arrays.asList("S", "A"),
                Arrays.asList(
                        new Production("S", Arrays.asList("a", "b", "A")),
                        new Production("S", Arrays.asList("epsilon")),
                        new Production("A", Arrays.asList("S", "a", "a")),
                        new Production("A", Arrays.asList("b"))
                )
        );
        assertTrue(icResult.equals(icExpected));
    }



}