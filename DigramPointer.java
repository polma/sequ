package ii.olma;

/**
 * Created with IntelliJ IDEA.
 * User: pdr
 * Date: 6/11/13
 * Time: 1:36 PM
 */
public class DigramPointer {
    public Rule correspondingRule;
    public SequenceElement correspondingElement;

    public DigramPointer(Rule r, SequenceElement s){
        this.correspondingElement = s;
        this.correspondingRule = r;
    }
}
