package uno.crayon.engineer.myimpl;

/**
 * @author
 * 0. 底层存储结构：Node 类
 * 1. hash 寻址算法：直接利用 % 取余
 * 2. hashcode 方法设计
 * 3. 冲突：寻址法、拉链法、双散列法
 * 3. 扩容迁移：
 */
public class MyHashMap<K, V> {

    private final int DEFAULT_CAPACITY = 16;
    private final float LOAD_FACTOR = 1f;
    private final int MAX_CAPACITY = Integer.MAX_VALUE - 8;
    private int size;
    private float threshold;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        size = 0;
        threshold = buckets.length * LOAD_FACTOR;
    }

    /**
     *
     * @param
     * @param
     * @return
     * / 节点存储类
     */
    class Node<K, V> {
        private K key;
        private V value;

        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public V get(K key) {
        int i = storeIndex(key, buckets.length);
        Node<K, V> node = buckets[i];
        if (node == null) return null;
        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    /**
     * @return true 新插入值
     * @return false 更新值
     */
    public boolean put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        return putVal(key, value, buckets);
    }

    /**
     * @return true 新插入值
     * @return false 更新值
     */
    private boolean putVal(K key, V value, Node<K, V>[] buckets) {

        int index = storeIndex(key, buckets.length);
        Node<K, V> node = buckets[index];
        if (node == null) {
            buckets[index] = new Node<K, V>(key, value);
            size++;
            return true;
        } else {
            Node<K, V> pre = null;
            while (node != null) {
                // key 相同，覆盖 value
                if(node.key.hashCode() == key.hashCode()
                        && (node.key == key || node.key.equals(key))) {
                    node.value = value;
                    return false;
                }
                pre = node;
                node = node.next;
            }
            // 桶位置+链表没有相同的值，插入尾部
            pre.next = new Node<K, V>(key, value);
            size++;
            return true;
        }
    }

    public int size() {
        return size;
    }

    /**
     * 两倍原数组大小，重新 hash 存值
     * 1. 哈希冲突，直接头插法插入
     */
    private void resize() {
        int doubleLength = buckets.length * 2;
        int newLength = Math.min(doubleLength, MAX_CAPACITY);
        Node<K, V>[] newBuckets = new Node[newLength];

        // 重新计算 size
        size = 0;

        // 遍历原数组桶
        for (int i = 0; i < buckets.length; i++) {
            Node<K, V> oldNode = buckets[i];
            // 没有数据，下一个位置
            if (oldNode == null) continue;

            // 计算新的存储位置
            int newIndex = storeIndex(oldNode.key, newLength);

            // 迁移原bucket位置上的所有数据
            while(oldNode != null) {
                putVal(oldNode.key, oldNode.value, newBuckets);
                oldNode = oldNode.next;
            }
        }
        buckets = newBuckets;
        newBuckets = null;
        // 第一次实现的时候漏掉重新计算 threshold
        // 导致resize之后的每一次插入重复调用resize且新数组长度指数级增加
        threshold = buckets.length * LOAD_FACTOR;
    }

    /**
     * @return key 在哈希桶中的位置
     */
    private int storeIndex(K key,int length) {
        int hash = disturbHash(key);
        return Math.abs(hash % length);
    }
    /**
     * hash码扰动算法
     */
    static int disturbHash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public static void main(String[] args) {
            MyHashMap map = new MyHashMap<>();
            for (int i = 0; i < 100; i++) {
                map.put("刘华强" + i, "你这瓜保熟吗？" + i);
            }
            System.out.println(map.size());
            for (int i = 0; i < 100; i++) {
                System.out.println(map.get("刘华强" + i));
            }
        }

}
