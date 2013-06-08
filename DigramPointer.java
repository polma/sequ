package ii.olma;

/**
 * Created with IntelliJ IDEA.
 * User: pdr
 * Date: 6/7/13
 * Time: 8:16 PM
 */
public class DigramPointer {

    private Rule rule;
    private int digramIndex;

    public DigramPointer(Rule rule, int digramIndex) {
        this.rule = rule;
        this.digramIndex = digramIndex;
    }

    public int getDigramIndex() {
        return digramIndex;
    }

    public void setDigramIndex(int digramIndex) {
        this.digramIndex = digramIndex;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
