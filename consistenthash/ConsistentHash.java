package consistenthash;

import java.util.*;

public class ConsistentHash<K, V> {

    /**
     * 环， 存放所有虚拟节点
     */
    final TreeMap<Integer, Map<K, V>> treeMap = new TreeMap<>();

    /**
     * 存放所有真实节点
     */
    private final Map<String, Node<K, V>> realNodes = new HashMap<>();


    public ConsistentHash() {
    }

    public void addRealNode(String nodeName, int replica) {
        Node<K, V> node = new Node<>();
        for (int j = 0; j < replica; j++) {
            int hash = hashMapHash(virtualNodeKey(nodeName, j));
            HashMap<K, V> virtualNodes = new HashMap<>();
            //记录真实节点的虚拟节点
            node.virtualNodes.put(hash, virtualNodes);
            //虚拟节点加入 环
            treeMap.put(hash, virtualNodes);
        }
        realNodes.put(nodeName, node);
    }

    /**
     * 删除一个真实节点
     */
    public void removeRealNode(String nodeName) {
        Node<K, V> node = realNodes.get(nodeName);
        for (Map.Entry<Integer, Map<K, V>> entry : node.virtualNodes.entrySet()) {
            treeMap.remove(entry.getKey());
        }
        realNodes.remove(nodeName);
    }

    /**
     * 删除一个虚拟节点
     *
     * @param nodeName         真实节点名称
     * @param virtualNodeIndex 虚拟节点下标
     */
    public void removeVirtualNode(String nodeName, int virtualNodeIndex) {
        Node<K, V> node = realNodes.get(nodeName);
        int hash = hashMapHash(virtualNodeKey(nodeName, virtualNodeIndex));
        node.virtualNodes.remove(hash);
        treeMap.remove(hash);
    }

    public V get(K k) {
        int virtualNodeHash = getVirtualNodeHash(k);
        return treeMap.get(virtualNodeHash).get(k);
    }

    public V put(K k, V v) {
        int virtualNodeHash = getVirtualNodeHash(k);
        return treeMap.get(virtualNodeHash).put(k, v);
    }

    /**
     * 根据 key 获取这个key应该存放的 虚拟节点
     */
    public Integer getVirtualNodeHash(K k) {
        //计算 k 的hash
        int hash = hashMapHash(k);
        //获取 k 的hash 对应的虚拟节点
        SortedMap<Integer, Map<K, V>> greaterThanHashMap = treeMap.tailMap(hash);
        //如果treemap中有比hash大的key，就是环上面顺时针的下一个节点
        //如果没有，就需要获取第一个节点
        return !greaterThanHashMap.isEmpty() ? greaterThanHashMap.firstKey() : treeMap.firstKey();
    }


    /**
     * 生成虚拟节点对应的key    0#0,0#1
     *
     * @param nodeName 真实节点名称
     * @param index    虚拟节点下标
     * @return 节点名称
     */
    private static String virtualNodeKey(String nodeName, int index) {
        return "%s#%d".formatted(nodeName, index);
    }

    static int hashMapHash(Object key) {
        int h;
        return ((key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16));
    }

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    public static String randomGenerateChar() {
        // 随机生成长度 10~100之间
        int length = 10 + RANDOM.nextInt(91); // nextInt(11) → 0~10
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }


}
