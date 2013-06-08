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
    private final HashMap<Digram, DigramPointer> digrams;

    public Sequitur(char firstChar) {
        rules = new ArrayList<Rule>();
        startingRule = new Rule();
        startingRule.incrementCount();
        startingRule.incrementCount(); // not yet sure if this is needed
        startingRule.append(new SequenceElement(firstChar));
        rules.add(startingRule); // the main rule (eg the starting symbol)

        digrams = new HashMap<Digram, DigramPointer>();
    }


    public void addLetter(char c) {
        final SequenceElement newLastElement = new SequenceElement(c);
        final Digram d = new Digram(startingRule.getLastElement(), newLastElement);
        final DigramPointer p = new DigramPointer(startingRule, startingRule.getLength()-1);
        startingRule.append(newLastElement);
        ensureDigramUniqueness(d,p);
    }

    private void ensureDigramUniqueness(Digram d, DigramPointer p) {
        if (digrams.containsKey(d)) {
            final DigramPointer dp = digrams.get(d);
            if(dp.getRule().getLength() == 2){
                //the other occurence is a full rule

            }
        } else {
            digrams.put(d, p);
        }
    }

}
