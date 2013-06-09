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
        ensureDigramUniqueness(d);
    }

    private void ensureDigramUniqueness(Digram d) {
        if (digrams.containsKey(d)) {
            final SequenceElement node = digrams.get(d);
            if(node.isRuleOfLengthTwo()){
                //the other occurence is a full rule
                startingRule.replaceDigramWithRule(startingRule.getLastElement().);
            }
        } else {
            digrams.put(d, );
        }
    }

}
