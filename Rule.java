package ii.olma;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: pdr
 * Date: 6/7/13
 * Time: 6:54 PM
 */
public class Rule {

    private String ruleName;
    private String ruleValue;
    private int usageCount;
    public int indexInRulesArray;

    private LinkedList<SequenceElement> ruleContents;


    public Rule(){
        ruleContents = new LinkedList<SequenceElement>();
    }

    public void replaceDigram(int index, Rule r){
        ruleContents.iterator();
    }

    public SequenceElement getLastElement(){
        return ruleContents.getLast();
    }

    public void append(SequenceElement e){
        ruleContents.addLast(e);
    }

    public int getLength(){
        return ruleContents.size();
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }


    public void incrementCount(){
        ++usageCount;
    }

    public void decrementCount(){
        --usageCount;
    }

    public boolean shouldBeDeleted(){
        return usageCount < 2;
    }
}
