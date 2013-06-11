package ii.olma;

import java.util.ArrayList;
import java.util.HashMap;
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
        startingRule.incrementCount();
        startingRule.incrementCount(); // not yet sure if this is needed
        startingRule.append(new SequenceElement(firstChar));
        startingRule.setRuleName("0");
        rules.add(startingRule); // the main rule (eg the starting symbol)

        digrams = new HashMap<Digram, Rule>();

        availableNumbers= new boolean[MAXRULES];
        for(int i =1; i<MAXRULES; ++i) availableNumbers[i] = true;
        availableNumbers[0] = false;
    }

    public int ruleCount(){
        return rules.size();
    }

    private String getNewRuleName(){
//        int i = GENERATOR.nextInt(MAXRULES);
//        while(!availableNumbers[i])
//            i = GENERATOR.nextInt(MAXRULES);

        int i = 1;
        while(!availableNumbers[i]) ++i;
        return String.valueOf(i);
    }


    public void addLetter(char c) {
        System.err.println("Processing:  " + c);
        final SequenceElement newLastElement = new SequenceElement(c);
        final SequenceElement oldLastElement = startingRule.getLastElement();
        final Digram d = new Digram(oldLastElement, newLastElement);
        startingRule.append(newLastElement);
        ensureDigramUniqueness(d, startingRule, oldLastElement);
    }

    private void ensureDigramUniqueness(Digram d, Rule r, SequenceElement digramStart) {
        System.err.println("Digram: " + d.first().toString() + "  " + d.second().toString());
        if (digrams.containsKey(d)) {
            final Rule correspondingRule = digrams.get(d); //the rule with the other occurence
            final SequenceElement correspondingStart = null;

            if(correspondingRule.getLength() == 2){
                System.err.println("Replacing digram with a full rule");
                //the other occurence is a full rule
                final Digram newDigram = r.replaceDigramWithRule(d.first(), correspondingRule);
                digrams.remove(d);


                //now we have a new digram
                ensureDigramUniqueness(newDigram, r, null);

                //also the count changes
                if(!d.first().isTerminal)
                    ensureRuleUtility(d.first().getCorrespondingRule(), d.first());


                /// ALSO delete digrams!!!!!!!!!!!!!!!

            }
            else{
                System.err.println("Creating a new rule");
                final Rule newRule = new Rule();
                newRule.append(new SequenceElement(d.first()));
                newRule.append(new SequenceElement(d.second()));
                rules.add(newRule);
                newRule.setRuleName(getNewRuleName());

                r.replaceDigramWithRule(digramStart, newRule);
                correspondingRule.replaceDigramWithRule(d.first(), newRule);
                correspondingRule.deleteDigram(d);

                System.err.println("\t\t" + d.first().toString() + " " + d.second().toString());

                // add the 3 digrams and make two replacements


                // ensure utility of the first symbol
                //ensureRuleUtility(node.getCorrespondingRule(), node);
            }
        } else {
            digrams.put(d, new DigramPointer(r, digramStart));
        }
    }

    private void ensureRuleUtility(Rule r, SequenceElement s){
        if(r.shouldBeDeleted()){
            final Rule anchor = s.getCorrespondingRule();
            final SequenceElement prev = anchor.unwindRule(s, r);
            final SequenceElement next = s.next;
            final Digram d = new Digram(prev, next);
            ensureDigramUniqueness(d, anchor, null);
            rules.remove(r);
        }
    }

    public void printRules(){
        for(Rule r: rules){
            r.printContents();
        }
    }

}
