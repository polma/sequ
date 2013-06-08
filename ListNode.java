package ii.olma;

/**
 * Created with IntelliJ IDEA.
 * User: pdr
 * Date: 6/8/13
 * Time: 10:22 AM
 */
public class ListNode {

    public ListNode(SequenceElement e){
        this.element = e;
    }

    public boolean isRuleOfLengthTwo(){
        return this.prev == null && this.next != null && this.next.next == null;
    }

    public SequenceElement element;
    public ListNode next;
    public ListNode prev;
}
