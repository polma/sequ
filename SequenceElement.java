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

    public boolean isRuleOfLengthTwo() {
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

    public SequenceElement(SequenceElement e) {
        this.isTerminal = e.isTerminal;
        this.correspondingRule = e.correspondingRule;
        this.value = e.value;
    }

    public void deleteFromSequence() {
        if (!isTerminal)
            correspondingRule.decrementCount();
    }

    public Rule getCorrespondingRule() {
        return correspondingRule;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        final SequenceElement se = (SequenceElement) o;
        return (getNumericalRepresentation() == se.getNumericalRepresentation()) &&
                (next.getNumericalRepresentation() == se.next.getNumericalRepresentation());
    }

    private int getNumericalRepresentation() {
        if (isTerminal)
            return (int) value;
        else
            return Constants.CHARMAX + Integer.parseInt(correspondingRule.getRuleName());
    }

    @Override
    public int hashCode() {
        return (Constants.PRIME1 * getNumericalRepresentation() +
                Constants.PRIME2 * next.getNumericalRepresentation()) % Constants.LARGE_PRIME;
    }

    public String toString() {
        if (isTerminal)
            return Character.toString(value);
        else {
            return correspondingRule.getRuleName();
        }
    }
}
