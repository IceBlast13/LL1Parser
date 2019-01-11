package application;

import model.ICGrammar;
import reader.ICGrammarReader;

import java.util.*;

public class Main {

    public static void main(String[] args){
        ICGrammar icGrammar = ICGrammarReader.readICGrammar("inputPart1.txt");
        System.out.print("Enter the input sequence: ");
        Scanner scanner = new Scanner(System.in);
        String inputSequence = scanner.nextLine();
        List<String> parsedSequence = parseSequence(inputSequence);
        //System.out.println(parsedSequence);
        try {
            List<Integer> productionOrder = LL1Parser.analyzeSequence(parsedSequence, icGrammar);
            System.out.println("The production order list is: " + productionOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> parseSequence(String inputSequence){
        List<String> outputSequence = new ArrayList<>();
        String[] parsedSequence = inputSequence.split(" ");
        for(int i = 0;i < parsedSequence.length;i++){
            outputSequence.add(parsedSequence[i]);
        }
        return outputSequence;
    }

}
