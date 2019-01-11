package reader;

import model.ICGrammar;
import model.Production;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ICGrammarReader {

    public ICGrammarReader(){
    }

    public static ICGrammar readICGrammar(String filename){
        ICGrammar icGrammar = new ICGrammar();

        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            /*read the terminals, separated by space on the 1st line of the input file:*/
            List<String> terminals = Arrays.asList(br.readLine().split(" "));
            icGrammar.setTerminals(terminals);
            /*read the nonTerminals, separated by space on the 2nd line of the given input file;
            * */
            List<String> nonTerminals = new ArrayList<>();
            String[] nonTerminalsRead = br.readLine().split(" ");
            for(int i = 0;i < nonTerminalsRead.length;i++){
                nonTerminals.add(nonTerminalsRead[i]);
            }
            icGrammar.setNonTerminals(nonTerminals);
            /*read the number of production rules:*/
            int rulesNo = Integer.parseInt(br.readLine());
            List<Production> prList = new ArrayList<>();
            for(int i = 0;i < rulesNo;i++){
                /*each production rule will be read in the following form:*
                * - the left element will be on the 1st line of the Production
                * - the right elements will be separated by space on the 2nd line of the production
                 */
                prList.add(new Production(br.readLine(), Arrays.asList(br.readLine().split(" "))));
            }
            icGrammar.setProductionRules(prList);
            /*read the start symbol:
            * (the start symbol must be a string from the non-terminals set)
            * */
            icGrammar.setStartSymbol(br.readLine());
            /**
             * read the epsilon symbol symbol:
             * */
            icGrammar.setEpsilon(br.readLine());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }

        return icGrammar;
    }

}
