package ii.olma;

/**
 * Created with IntelliJ IDEA.
 * User: pdr
 * Date: 6/7/13
 * Time: 11:18 PM
 */
public class Digram {
    private SequenceElement e1;
    private SequenceElement e2;

    public Digram(SequenceElement e1, SequenceElement e2){
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        final Digram d = (Digram) o;
        return e1.equals(d.e1) && e2.equals(d.e2);
    }

    @Override
    public int hashCode() {
        return 101641 * e1.hashCode() + 92863 * e2.hashCode();
    }


}
