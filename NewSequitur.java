package ii.olma;

/**
 * Created with IntelliJ IDEA.
 * User: pdr
 * Date: 6/12/13
 * Time: 10:35 PM
 */
public class NewSequitur {
    private Rule startingRule;

    public NewSequitur() {
        startingRule = new Rule();
    }

    public void addLetter(char c) {
        System.err.println("\nProcessing:  " + c);
        final SequenceElement newLastElement = new SequenceElement(c);
        final SequenceElement lastElement = startingRule.getLastSequenceElement();
        lastElement.append(newLastElement);

        startingRule.getLastSequenceElement().prev.isDigramAlreadyPresent();

    }

    public void printStart() {
        System.err.print(startingRule.toString());
    }

    public String decompress(){
        return startingRule.getFullWord();
    }
}
