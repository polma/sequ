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
    }

    public void replaceDigramWithRule(ListNode startingNode, Rule r) {
        final ListNode newNode = new ListNode(new SequenceElement(r));
        r.incrementCount();

        SequenceElement s = startingNode.element;
        s.deleteFromSequence();
        s = startingNode.next.element;
        s.deleteFromSequence();

        ruleContents.insertBefore(startingNode, newNode);
        ruleContents.deleteNode(startingNode);
        ruleContents.deleteNode(newNode.next);


        // maintain rule utility!


        // it also introduces new bigrams!

    }

    public SequenceElement getLastElement() {
        return ruleContents.getLast().element;
    }

    public void append(SequenceElement e) {
        ruleContents.append(new ListNode(e));
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
}
