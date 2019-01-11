package model;

import java.util.List;

public class Production {

    private String leftElement;
    private List<String> rightElements;

    public Production(){
    }

    public Production(String leftElement, List<String> rightElements){
        this.leftElement = leftElement;
        this.rightElements = rightElements;
    }

    public String getLeftElement() {
        return leftElement;
    }

    public void setLeftElement(String leftElement) {
        this.leftElement = leftElement;
    }

    public List<String> getRightElements() {
        return rightElements;
    }

    public void setRightElements(List<String> rightElements) {
        this.rightElements = rightElements;
    }

    public boolean equals(Production other) {
        if(!this.leftElement.equals(other.getLeftElement()))return false;
        List<String> otherRightElements = other.getRightElements();
        if(this.rightElements.size() != otherRightElements.size())return false;
        for(int i = 0;i < this.rightElements.size();i++){
            if(!this.rightElements.get(i).equals(otherRightElements.get(i)))return false;
        }
        return true;
    }
}
