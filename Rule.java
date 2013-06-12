package ii.olma;

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

    private DoublyLinkedList ruleContents;

    public Rule() {
        ruleContents = new DoublyLinkedList(this);
        usageCount = 0;
    }

    public int getUsageCount(){
        return usageCount;
    }


    public SequenceElement replaceDigramWithRule(SequenceElement startingNode, Rule r) {
        System.err.println("\tReplacing in "+ ruleName + " for " + startingNode.toString() + " " + startingNode.next.toString() + " with " + r.getRuleName());
        final SequenceElement newNode = new SequenceElement(r);
        r.incrementCount();

        SequenceElement s = startingNode;
        s.deleteFromSequence();
        s = startingNode.next;
        s.deleteFromSequence();

        ruleContents.insertBefore(startingNode, newNode);
        ruleContents.deleteNode(startingNode);
        ruleContents.deleteNode(newNode.next);

        return newNode;
        // maintain rule utility!

    }

    public void deleteDigram(Digram d){
        System.err.println("\tDeleting digram " + d.first().toString() + " " + d.second().toString() + " in " + ruleName);
        final SequenceElement beforeDigram = d.first().prev;
        final SequenceElement afterDigram = d.second().next;
        System.err.println("\tPrev: " + (beforeDigram == null ? "NULL"  : beforeDigram.toString()) +
                " Next: " + (afterDigram == null ? "NULL" : afterDigram.toString()));
        ruleContents.deleteNode(d.first());
        ruleContents.deleteNode(d.second());
    }

    public SequenceElement unwindRule(SequenceElement s, Rule r){
        final SequenceElement temp = new SequenceElement('c');
        final DoublyLinkedList replacement = r.getContents();
        ruleContents.insertBefore(s, temp);
        temp.next = replacement.getFirst();
        s.prev = replacement.getLast();
        ruleContents.deleteNode(temp);
        ruleContents.deleteNode(s);

        return replacement.getLast();
    }

    public DoublyLinkedList getContents(){
        return ruleContents;
    }

    public SequenceElement getLastElement() {
        return ruleContents.getLast();
    }

    public SequenceElement getFirstElement(){
        return ruleContents.getFirst();
    }

    public void append(SequenceElement e) {
        ruleContents.append(e);
    }

    public int getLength() {
        return ruleContents.getLength();
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


    public void incrementCount() {
        ++usageCount;
    }

    public void decrementCount() {
        --usageCount;
    }

    public boolean shouldBeDeleted() {
        return usageCount < 2;
    }

    public void printContents(){
        System.err.print(ruleName + " -> ");
        ruleContents.printContents();
    }
}
