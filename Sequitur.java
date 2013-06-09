package ii.olma;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: pdr
 * Date: 6/7/13
 * Time: 5:49 PM
 */
public class Sequitur {

    private final ArrayList<Rule> rules;
    private final Rule startingRule;
    private final HashMap<Digram, SequenceElement> digrams;

    public Sequitur(char firstChar) {
        rules = new ArrayList<Rule>();
        startingRule = new Rule();
        startingRule.incrementCount();
        startingRule.incrementCount(); // not yet sure if this is needed
        startingRule.append(new SequenceElement(firstChar));
        rules.add(startingRule); // the main rule (eg the starting symbol)

        digrams = new HashMap<Digram, SequenceElement>();
    }


    public void addLetter(char c) {
        final SequenceElement newLastElement = new SequenceElement(c);
        final Digram d = new Digram(startingRule.getLastElement(), newLastElement);
        startingRule.append(newLastElement);
        ensureDigramUniqueness(d, startingRule);
    }

    private void ensureDigramUniqueness(Digram d, Rule r) {
        if (digrams.containsKey(d)) {
            final SequenceElement node = digrams.get(d);
            if(node.isRuleOfLengthTwo()){
                //the other occurence is a full rule
                final Digram newDigram = r.replaceDigramWithRule(d.first(), node.getCorrespondingRule());

                //now we have a new digram
                ensureDigramUniqueness(newDigram, r);

                //also the count changes
                if(!d.first().isTerminal)
                    ensureRuleUtility(d.first().getCorrespondingRule(), d.first());


                /// ALSO delete digrams!!!!!!!!!!!!!!!

            }
            else{
                final Rule newRule = new Rule();
                newRule.append(node);
                newRule.append(node.next);
                rules.add(newRule);

                // add the 3 digrams and make two replacements


                // ensure utility of the first symbol
                ensureRuleUtility(node.getCorrespondingRule(), node);
            }
        } else {
            digrams.put(d, d.first());
        }
    }

    private void ensureRuleUtility(Rule r, SequenceElement s){
        if(r.shouldBeDeleted()){
            final Rule anchor = s.getCorrespondingRule();
            final SequenceElement prev = anchor.unwindRule(s, r);
            final SequenceElement next = s.next;
            final Digram d = new Digram(prev, next);
            ensureDigramUniqueness(d, anchor);
            rules.remove(r);
        }
    }

}
