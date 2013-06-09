package ii.olma;

/**
 * Created with IntelliJ IDEA.
 * User: pdr
 * Date: 6/8/13
 * Time: 10:22 AM
 */
public class DoublyLinkedList {
    private SequenceElement first;
    private SequenceElement last;

    private Rule correspondingRule;

    private int length;

    public DoublyLinkedList(Rule r) {
        length = 0;
        first = null;
        last = null;
        correspondingRule = r;
    }

    public SequenceElement getLast() {
        return last;
    }

    public void append(SequenceElement n) {
        if (last == null) {
            n.prev = null;
            n.next = null;
            first = n;
            last = n;
        } else {
            last.next = n;
            n.prev = last;
            n.next = null;
        }
        ++length;
    }

    public void deleteNode(SequenceElement n) {
        if (n.prev == null) {
            first = n.next;
        } else {
            n.prev.next = n.next;
        }
        if (n.next == null) {
            last = n.prev;
        } else {
            n.next.prev = n.prev;
        }
        --length;
    }

    public void insertBefore(SequenceElement node, SequenceElement newNode) {
        newNode.prev = node.prev;
        newNode.next = node;
        if (node.prev == null)
            first = newNode;
        else
            node.prev.next = newNode;
        node.prev = newNode;
    }

    public void replaceDigram() {

    }

    public int getLength() {
        return length;
    }

}
