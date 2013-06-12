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

    private static final int MAXRULES = 10000000;
    private static boolean takenNumbers[] = new boolean[MAXRULES];


    private SequenceElement guard; // this is where we attach the elements

    private DoublyLinkedList ruleContents;

    public Rule() {
        ruleContents = new DoublyLinkedList(this);
        usageCount = 0;
        ruleName = getNewRuleName();
        guard = new SequenceElement(true, this);
    }

    private static String getNewRuleName() {
        int i = 0;
        while (takenNumbers[i]) ++i;
        takenNumbers[i] = true;
        return String.valueOf(i);
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void initiateSequence(SequenceElement s1, SequenceElement s2){
        guard.next = s1;
        s1.prev = guard;
        s1.next = s2;
        s2.prev = s1;
        s2.next = guard;
        guard.prev = s2;
    }

    public SequenceElement getFirstSequenceElement(){
        return guard.next;
    }

    public SequenceElement getLastSequenceElement(){
        return guard.prev;
    }


    public SequenceElement replaceDigramWithRule(SequenceElement startingNode, Rule r) {
        System.err.println("\tReplacing in " + ruleName + " for " + startingNode.toString() + " " + startingNode.next.toString() + " with " + r.getRuleName());
        final SequenceElement newNode = new SequenceElement(r);
        //r.incrementCount(); //done in main loop

        SequenceElement s = startingNode;
        s.deleteFromSequence();
        s = startingNode.next;
        s.deleteFromSequence();

        ruleContents.insertBefore(startingNode, newNode);
        ruleContents.deleteNode(startingNode);
        ruleContents.deleteNode(newNode.next);

        return newNode;

    }

    public void deleteDigram(Digram d) {
        System.err.println("\tDeleting digram " + d.first().toString() + " " + d.second().toString() + " in " + ruleName);
        final SequenceElement beforeDigram = d.first().prev;
        final SequenceElement afterDigram = d.second().next;
        System.err.println("\tPrev: " + (beforeDigram == null ? "NULL" : beforeDigram.toString()) +
                " Next: " + (afterDigram == null ? "NULL" : afterDigram.toString()));
        ruleContents.deleteNode(d.first());
        ruleContents.deleteNode(d.second());
    }

    public SequenceElement unwindRule(SequenceElement s, Rule r) {
        final SequenceElement temp = new SequenceElement('c');
        final DoublyLinkedList replacement = r.getContents();
        ruleContents.insertBefore(s, temp);
        temp.next = replacement.getFirst();
        s.prev = replacement.getLast();
        ruleContents.deleteNode(temp);
        ruleContents.deleteNode(s);

        return replacement.getLast();
    }

    public SequenceElement expand(Rule r, boolean isFirst) {
        System.err.print("Expanding rule: ");
        r.printContents();
        final DoublyLinkedList replacement = r.getContents();

        if (isFirst) {
            replacement.append(getLastElement());
            ruleContents = replacement;
            return getLastElement().prev;
        } else {
            ruleContents.getLast().prev.next = replacement.getFirst();
            replacement.getFirst().prev = ruleContents.getLast().prev;

            ruleContents.incrementSize(replacement.getLength() - 1);
            ruleContents.setLastElement(replacement.getLast());
            return null;
        }
    }

    public SequenceElement replaceFront(Rule r){
        final SequenceElement last = getLastElement();
        ruleContents = r.getContents();
        append(last);
        return getLastElement().prev;
    }

    public DoublyLinkedList getContents() {
        return ruleContents;
    }

    public SequenceElement getLastElement() {
        return ruleContents.getLast();
    }

    public SequenceElement getFirstElement() {
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


    public void incrementCount(int i) {
        usageCount += i;
    }

    public void decrementCount() {
        --usageCount;
    }

    public boolean shouldBeDeleted() {
        if(usageCount < 2){
            takenNumbers[Integer.parseInt(ruleName)] = false;
            return true;
        }
        return false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(ruleName);
        sb.append(" -> ");
        SequenceElement s = guard.next;
        while(s != null)
            sb.append(s.toString() + " ");
        return sb.toString();
                
    }

    public void printContents() {
        System.err.print(ruleName + " -> ");
        ruleContents.printContents();
    }

}
