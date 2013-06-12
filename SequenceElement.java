package ii.olma;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pdr
 * Date: 6/7/13
 * Time: 7:13 PM
 */
public class SequenceElement {
    public boolean isTerminal;
    public Rule correspondingRule;
    private boolean isGuard;
    private char value;

    public boolean isGuard(){
        return isGuard;
    }

    public SequenceElement next;
    public SequenceElement prev;

    public static HashMap<SequenceElement, SequenceElement> digrams =
            new HashMap<SequenceElement, SequenceElement>(Constants.LARGE_PRIME);

    public SequenceElement(char value) {
        this.isTerminal = true;
        this.value = value;

    }

    public SequenceElement(Rule r) {
        this.correspondingRule = r;
        this.value = 0;
        this.isTerminal = false;
    }

    public SequenceElement(boolean isGuard, Rule r) {
        if (isGuard == true)
            this.isGuard = true;
        this.correspondingRule = r;
        this.next = this;
        this.prev = this;
        this.value = 0;
    }

    public SequenceElement(SequenceElement e) {
        this.isTerminal = e.isTerminal;
        this.correspondingRule = e.correspondingRule;
        this.value = e.value;
    }

    public boolean startsRuleOfLengthTwo() {
        return this.prev.isGuard && this.next != null && this.next.next.isGuard;
    }

    public void deleteFromSequence() {
        if (!isTerminal)
            correspondingRule.decrementCount();
    }

    public Rule getCorrespondingRule() {
        return prev.correspondingRule;
    }

    //make link between two symbols (this introduces a bigram)
    public static void makeLink(SequenceElement left, SequenceElement right) {
        assert (left != null);
        assert (right != null);

        if (left.next != null)
            left.deleteDigramFromSequence();

        left.next = right;
        right.prev = left;
    }

    public void deleteDigramFromSequence() {
        if (isGuard) return;

        if (digrams.get(this) == this)
            digrams.remove(this);
    }

    public void append(SequenceElement s) {
        makeLink(s, next);
        makeLink(this, s);
    }

    public boolean isDigramAlreadyPresent() {
        if (isGuard)
            return false;
        if (!digrams.containsKey(this)) {
            digrams.put(this, this);
            return false;
        }

        final SequenceElement correspondingDigram = digrams.get(this);
        //account for triple case
        if (correspondingDigram == this)
            return true;

        ensureDigramUniqueness(correspondingDigram);
        return true;
    }

    private void ensureDigramUniqueness(SequenceElement matchingDigram) {
        //remember to decrease counts of the two symbols

        if (matchingDigram.startsRuleOfLengthTwo()) {
            //reuse that rule
            final Rule ruleToReuse = matchingDigram.getCorrespondingRule();
            System.err.println("Reusing rule: " + ruleToReuse.toString());
            replaceWithRule(ruleToReuse);
            ensureRuleUtility(ruleToReuse);

        } else {
            //create a new rule
            final Rule r = new Rule();
            final SequenceElement s1 = new SequenceElement(this);
            final SequenceElement s2 = new SequenceElement(next);
            System.err.println("Creating new rule from " + s1.toString() + " and " + s2.toString());

            r.initiateSequence(s1, s2);

            digrams.put(s1,s1);

            matchingDigram.replaceWithRule(r);
            replaceWithRule(r);

            ensureRuleUtility(r);
        }
    }

    private void ensureRuleUtility(Rule recentlyUsedRule) {
        //update the usages and check if needs to be expanded
        final SequenceElement left = recentlyUsedRule.getFirstSequenceElement();
        final SequenceElement right = recentlyUsedRule.getLastSequenceElement();

        System.err.println("Ensuring utility of: " + left.toString() + " and " + right.toString());

        assert(left!=null);
        assert(right!=null);

        if (!right.isTerminal && !right.isGuard) {
            right.correspondingRule.decrementCount();
        }

        if (!left.isTerminal && !left.isGuard) {
            final Rule anchor = left.correspondingRule;
            anchor.decrementCount();
            if (anchor.shouldBeDeleted()) {
                left.unwind();
            }
        }
    }

    private void unwind(){
        makeLink(prev, correspondingRule.getFirstSequenceElement());
        makeLink(correspondingRule.getLastSequenceElement(), next);
    }

    private void removeDigrams() {
        makeLink(prev, next);
        if (!isGuard) {
            deleteDigramFromSequence();
            if (!isTerminal)
                correspondingRule.decrementCount();
        }

    }

    private void replaceWithRule(Rule r) {
        final SequenceElement toAdd = new SequenceElement(r);
        //two digrams to delete
        removeDigrams();
        next.removeDigrams();
        prev.append(toAdd);
        r.incrementCount(1);
        if (!prev.isDigramAlreadyPresent())
            prev.next.isDigramAlreadyPresent();

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

    public void printRules() {
//        for (Rule r : rules) {
//            r.printContents();
//        }
    }

    public static void printDigrams() {
        System.err.print("Digrams: ");
        for (Map.Entry<SequenceElement, SequenceElement> e : digrams.entrySet()) {
            System.err.print(e.getKey().toString() + "-" + e.getKey().next.toString() + "  ");
        }
        System.err.println();
    }
}
