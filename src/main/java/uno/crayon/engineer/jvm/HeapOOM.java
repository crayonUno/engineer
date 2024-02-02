package uno.crayon.engineer.jvm;

import uno.crayon.engineer.myimpl.MyHashMap;
import uno.crayon.engineer.myimpl.myiocfactory.MyPhoneBean;

import java.util.ArrayList;
import java.util.List;

/**
 * VM参数： -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOOM {
    static class OOMObject {
    }

    public static void main(String[] args) {
        List<MyPhoneBean> list = new ArrayList<>();
        while (true) {
            list.add(new MyPhoneBean());
        }
    }
}

