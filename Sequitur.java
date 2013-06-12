package ii.olma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: pdr
 * Date: 6/7/13
 * Time: 5:49 PM
 */
public class Sequitur {

    private final ArrayList<Rule> rules;
    private final Rule startingRule;
    private final HashMap<Digram, DigramPointer> digrams;
    private boolean availableNumbers[];
    private static final int MAXRULES = 10000000;
    private static final Random GENERATOR = new Random();

    public Sequitur(char firstChar) {
        rules = new ArrayList<Rule>();
        startingRule = new Rule();
        startingRule.incrementCount(2);
        startingRule.append(new SequenceElement(firstChar));
        startingRule.setRuleName("0");
        rules.add(startingRule); // the main rule (eg the starting symbol)

        digrams = new HashMap<Digram, DigramPointer>(123123);

        availableNumbers = new boolean[MAXRULES];
        for (int i = 1; i < MAXRULES; ++i) availableNumbers[i] = true;
        availableNumbers[0] = false;
    }

    public int ruleCount() {
        return rules.size();
    }

    private String getNewRuleName() {
//        int i = GENERATOR.nextInt(MAXRULES);
//        while(!availableNumbers[i])
//            i = GENERATOR.nextInt(MAXRULES);

        int i = 1;
        while (!availableNumbers[i]) ++i;
        availableNumbers[i] = false;
        return String.valueOf(i);
    }


    public void addLetter(char c) {
        System.err.println("\nProcessing:  " + c);
        printDigrams();
        final SequenceElement newLastElement = new SequenceElement(c);
        final SequenceElement oldLastElement = startingRule.getLastElement();
        final Digram d = new Digram(oldLastElement, newLastElement);
        startingRule.append(newLastElement);
        ensureDigramUniqueness(d, startingRule, oldLastElement);
        printDigrams();
        printRules();

    }

    private void ensureDigramUniqueness(Digram d, Rule r, SequenceElement digramStart) {
        System.err.println("Digram: " + d.first().toString() + "  " + d.second().toString());
        if (digrams.containsKey(d)) {
            final Rule correspondingRule = digrams.get(d).correspondingRule; //the rule with the other occurence
            final SequenceElement correspondingStart = digrams.get(d).correspondingElement;
            System.err.println("Found the same: " + correspondingStart.toString() + " " + correspondingStart.next.toString() + " in rule " + correspondingRule.getRuleName());

            if ((correspondingStart.next == digramStart && correspondingStart.equals(digramStart) && digramStart.equals(digramStart.next)) ||
                    (correspondingStart == digramStart.next && correspondingStart.equals(digramStart) && correspondingStart.equals(correspondingStart.next))) {
                System.err.println("Triple detected!");
                return;
            }

            //count usage changes
            if (!d.first().isTerminal) {
                d.first().getCorrespondingRule().decrementCount();
            }
            if (!d.second().isTerminal) {
                d.second().getCorrespondingRule().decrementCount();
            }

            if (correspondingRule.getLength() == 2) {
                System.err.println("Replacing digram with a full rule");
                //the other occurence is a full rule

                removeDestroyedDigrams(digramStart);

                final SequenceElement replacementStart = r.replaceDigramWithRule(d.first(), correspondingRule);

                Digram newDigramLeft, newDigramRight;

                //take care of usage counts
                correspondingRule.incrementCount(1);


                if (replacementStart.prev != null) {
                    //triple edge case
                    // if (!(replacementStart.prev.equals(replacementStart) && replacementStart.prev.prev != null && replacementStart.prev.prev.equals(replacementStart))) {
                    newDigramLeft = new Digram(replacementStart.prev, replacementStart);
                    ensureDigramUniqueness(newDigramLeft, r, replacementStart.prev);
                    //}
                }

                if (replacementStart.next != null) {
                    //  if (!(replacementStart.next.equals(replacementStart) && replacementStart.next.next != null && replacementStart.next.next.equals(replacementStart))) {
                    newDigramRight = new Digram(replacementStart, replacementStart.next);
                    ensureDigramUniqueness(newDigramRight, r, replacementStart);
                    //  }
                }
               ensureUtility(correspondingRule, d);

            } else {

                System.err.println("Creating a new rule");
                final Rule newRule = new Rule();
                newRule.append(new SequenceElement(d.first()));
                newRule.append(new SequenceElement(d.second()));
                rules.add(newRule);
                newRule.setRuleName(getNewRuleName());
                newRule.incrementCount(2);

                //remove digrams that will disappear
                removeDestroyedDigrams(digramStart);
                removeDestroyedDigrams(correspondingStart);

                //also remove the digram XY as it changes location
                printDigrams();
                digrams.remove(d);
                printDigrams();

                final Digram digramInNewRule = new Digram(newRule.getFirstElement(), newRule.getFirstElement().next);
                digrams.put(digramInNewRule, new DigramPointer(newRule, newRule.getFirstElement()));
                printDigrams();

                //edge case - two digrams to replace are in the same rule and next to each other
                boolean digramsNextToEachOther = false;

                if (r == correspondingRule) {
                    digramsNextToEachOther = checkForward(digramStart, correspondingStart) || checkForward(correspondingStart, digramStart);
                }

                SequenceElement replacementStart1 = r.replaceDigramWithRule(digramStart, newRule);

                if (!digramsNextToEachOther) {
                    if (replacementStart1.next != null)
                        digrams.put(new Digram(replacementStart1, replacementStart1.next), new DigramPointer(r, replacementStart1));
                    if (replacementStart1.prev != null)
                        digrams.put(new Digram(replacementStart1.prev, replacementStart1), new DigramPointer(r, replacementStart1.prev));
                }


                SequenceElement replacementStart2 = correspondingRule.replaceDigramWithRule(correspondingStart, newRule);
                correspondingRule.deleteDigram(d);

                if (!digramsNextToEachOther) {
                    if (replacementStart2.next != null)
                        digrams.put(new Digram(replacementStart2, replacementStart2.next), new DigramPointer(r, replacementStart2));
                    if (replacementStart2.prev != null)
                        digrams.put(new Digram(replacementStart2.prev, replacementStart2), new DigramPointer(r, replacementStart2.prev));
                }

                if (digramsNextToEachOther) {
                    SequenceElement left, right;
                    if (replacementStart1.next != null && replacementStart1.next.equals(replacementStart2)) {
                        left = replacementStart1;
                        right = replacementStart2;
                    } else {
                        left = replacementStart2;
                        right = replacementStart1;
                    }
                    digrams.put(new Digram(left, right), new DigramPointer(r, left));
                    if (left.prev != null)
                        digrams.put(new Digram(left.prev, left), new DigramPointer(r, left.prev));
                    if (right.next != null)
                        digrams.put(new Digram(right, right.next), new DigramPointer(r, right));
                }


                System.err.println("\t\t" + d.first().toString() + " " + d.second().toString());


                // ensure utility of the first symbol
                ensureUtility(newRule, digramInNewRule);
                //ensureRuleUtility(node.getCorrespondingRule(), node);
            }

        } else {
            digrams.put(d, new DigramPointer(r, digramStart));
        }
    }

    private void ensureUtility(Rule recentlyUsedRule, Digram toRemove) {


        final SequenceElement left = recentlyUsedRule.getFirstElement();
        final SequenceElement right = recentlyUsedRule.getLastElement();
        assert (left.next == right);

        SequenceElement expansionEnd = left;
        final Rule leftRule = left.getCorrespondingRule();
        if (!left.isTerminal && leftRule.shouldBeDeleted()) {

            System.err.print("Found a rule to expand: ");
            leftRule.printContents();
            System.err.print("Expanding in: ");
            recentlyUsedRule.printContents();
            printRules();
            printDigrams();

            //delete the digram formed by the left non terminal and the second element
            digrams.remove(toRemove);

            //also delete (change the location) of the digrams from expansion
            SequenceElement startingElement = leftRule.getFirstElement().next;
            while(startingElement != null){
                digrams.put(new Digram(startingElement.prev, startingElement), new DigramPointer(recentlyUsedRule, startingElement.prev));
                startingElement = startingElement.next;
            }
            printDigrams();

            expansionEnd = recentlyUsedRule.replaceFront(leftRule);

            rules.remove(leftRule);
            availableNumbers[Integer.parseInt(leftRule.getRuleName())] = true;
            assert (expansionEnd != null);

            System.err.println("Expansion end: " + expansionEnd.toString() + " " + expansionEnd.next);
            printRules();
            ensureDigramUniqueness(new Digram(expansionEnd, expansionEnd.next), recentlyUsedRule, expansionEnd);
        }

        else{

        }
//        if (!right.isTerminal && right.getCorrespondingRule().shouldBeDeleted()) {
//            recentlyUsedRule.expand(right.getCorrespondingRule(), false);
//        //    rules.remove(right.getCorrespondingRule());
//        //    availableNumbers[Integer.parseInt(right.getCorrespondingRule().getRuleName())] = true;
//        }


    }

    private void ensureUtilityOld(Rule recentlyUsedRule) {
        final SequenceElement left = recentlyUsedRule.getFirstElement();
        final SequenceElement right = recentlyUsedRule.getLastElement();
        assert (left.next == right);

        SequenceElement expansionEnd = left;
        if (!left.isTerminal && left.getCorrespondingRule().shouldBeDeleted()) {
            expansionEnd = left.getCorrespondingRule().unwindRule(left, recentlyUsedRule);
        }
        if (!right.isTerminal && right.getCorrespondingRule().shouldBeDeleted()) {
            right.getCorrespondingRule().unwindRule(right, recentlyUsedRule);
        }

        ensureDigramUniqueness(new Digram(expansionEnd, expansionEnd.next), recentlyUsedRule, expansionEnd);
    }

    private boolean checkForward(SequenceElement digramStart, SequenceElement correspondingStart) {
        return digramStart.next.next != null && digramStart.next.next.equals(correspondingStart) &&
                digramStart.next.next.next != null && digramStart.next.next.next.equals(correspondingStart.next);
    }

    private void removeDestroyedDigrams(SequenceElement digramStart) {
        Digram toRemove;
        if (digramStart.prev != null) {
            toRemove = new Digram(digramStart.prev, digramStart);
            digrams.remove(toRemove);
        }
        if (digramStart.next.next != null) {
            toRemove = new Digram(digramStart.next, digramStart.next.next);
            digrams.remove(toRemove);
        }
    }

    private void ensureRuleUtility(Rule ruleWithOccurence, SequenceElement s) {
        if (ruleWithOccurence.shouldBeDeleted()) {
            final Rule anchor = s.getCorrespondingRule();
            final SequenceElement prev = anchor.unwindRule(s, ruleWithOccurence);
            final SequenceElement next = s.next;
            final Digram d = new Digram(prev, next);
            ensureDigramUniqueness(d, anchor, null);

            rules.remove(ruleWithOccurence);
            availableNumbers[Integer.parseInt(ruleWithOccurence.getRuleName())] = true;
        }
    }

    public void printRules() {
        for (Rule r : rules) {
            r.printContents();
        }
    }

    public void printDigrams() {
        System.err.print("Digrams: ");
        for (Map.Entry<Digram, DigramPointer> e : digrams.entrySet()) {
            System.err.print(e.getKey().first().toString() + "-" + e.getKey().second().toString() + "  ");
        }
        System.err.println();
    }

}
