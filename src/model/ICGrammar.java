package model;

import java.util.List;

public class ICGrammar {

    private List<String> terminals;
    private List<String> nonTerminals;
    private List<Production> productionRules;
    private String startSymbol;
    private String epsilon = "epsilon";

    public ICGrammar(){
    }

    public ICGrammar(List<String> terminals, List<String> nonTerminals, List<Production> productionRules){
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.productionRules = productionRules;
    }

    public String getEpsilon(){
        return this.epsilon;
    }

    public void setEpsilon(String epsilon){
        this.epsilon = epsilon;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<String> terminals) {
        this.terminals = terminals;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(List<String> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public List<Production> getProductionRules() {
        return productionRules;
    }

    public void setProductionRules(List<Production> productionRules) {
        this.productionRules = productionRules;
    }

    public boolean equals(ICGrammar other){
        if(this.terminals.size() != other.getTerminals().size())return false;
        List<String> otherTerminals = other.getTerminals();
        for(int i = 0;i < this.terminals.size();i++){
            if(!this.terminals.get(i).equals(otherTerminals.get(i)))return false;
        }
        List<String> otherNonTerminals = other.getNonTerminals();
        if(this.nonTerminals.size() != otherNonTerminals.size())return false;
        for(int i = 0;i < this.nonTerminals.size();i++){
            if(!this.nonTerminals.get(i).equals(otherNonTerminals.get(i)))return false;
        }
        List<Production> otherProductions = other.getProductionRules();
        if(this.productionRules.size() != otherProductions.size())return false;
        for(int i = 0;i < this.productionRules.size();i++){
            if(!this.productionRules.get(i).equals(otherProductions.get(i)))return false;
        }
        return true;
    }

    public String getTerminalByIndex(int index){
        return this.terminals.get(index);
    }

    public String getNonTerminalByIndex(int index){
        return this.nonTerminals.get(index);
    }

    public int getTerminalIndex(String terminal){
        int result = -1;
        for(int i = 0;i < this.terminals.size();i++){
            if(terminal.equals(this.terminals.get(i)))return i;
        }
        return result;
    }

    public int getNonTerminalIndex(String nonTerminal){
        int result = -1;
        for(int i = 0;i < this.nonTerminals.size();i++){
            if(nonTerminal.equals(this.nonTerminals.get(i)))return i;
        }
        return result;
    }

    public boolean isTerminal(String terminal){
        for(String x : this.terminals)
            if(x.equals(terminal))return true;
        return false;
    }

    public boolean isNonTerminal(String nonTerminal){
        for(String x : this.nonTerminals)
            if(x.equals(nonTerminal))return true;
        return false;
    }

}
