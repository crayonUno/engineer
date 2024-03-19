package uno.crayon.engineer.collections;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author
 */
public class IteratorDebug {
    protected ArrayList<Integer> arList;
    protected LinkedList<Integer> linkedList;

    private IteratorDebug() {
        arList = Lists.newArrayList(2,4,1,3,5,2);
        linkedList = Lists.newLinkedList(arList);
    }

    public static void main(String[] args) {
        ArrayList<Integer> arList;
        LinkedList<Integer> linkedList;
        arList = Lists.newArrayList(2,4,1,3,5,2);
        linkedList = Lists.newLinkedList(arList);
        for (int i : arList) {
            System.out.print(i + " ");
        }

        for (int i : linkedList) {
            System.out.print(i + " ");
        }
    }
}
