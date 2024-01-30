package uno.crayon.engineer.myimpl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = uno.crayon.engineer.myimpl.MyHashMap.class)
class MyHashMapTest {
    @Test
    void test0() {
        MyHashMap map = new MyHashMap();
        for (int i = 0; i < 100; i++) {
            map.put("刘华强" + i, "你这瓜保熟吗？" + i);
        }
        System.out.println(map.size());
        for (int i = 0; i < 100; i++) {
            System.out.println(map.get("刘华强" + i));
        }
    }

    @Test
    void test1() {
        MyHashMap map = new MyHashMap();
        map.put("刘华强1","哥们，你这瓜保熟吗？");
        map.put("刘华强1","你这瓜熟我肯定要啊！");
        System.out.println(map.get("刘华强1"));
    }
}