package ii.olma;

/**
 * Created with IntelliJ IDEA.
 * User: pdr
 * Date: 6/7/13
 * Time: 7:13 PM
 */
public class SequenceElement {
    public boolean isTerminal;
    private Rule correspondingRule;
    private char value;

    public SequenceElement next;
    public SequenceElement prev;

    public boolean isRuleOfLengthTwo(){
        return this.prev == null && this.next != null && this.next.next == null;
    }

    public SequenceElement(char value) {
        this.isTerminal = true;
        this.value = value;
    }

    public SequenceElement(Rule r) {
        this.correspondingRule = r;
        this.value = 0;
        this.isTerminal = false;
    }

    public void deleteFromSequence() {
        if (!isTerminal)
            correspondingRule.decrementCount();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        final SequenceElement se = (SequenceElement) o;
        if (isTerminal != se.isTerminal)
            return false;
        if (isTerminal)
            return value == se.value;
        else
            return correspondingRule == se.correspondingRule;
    }

    @Override
    public int hashCode() {
        if (value != 0) {
            return 103919 * value;
        }
        return correspondingRule.hashCode();
    }
}
