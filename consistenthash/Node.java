package consistenthash;

import java.util.HashMap;
import java.util.Map;

/**
 * 真实节点
 */
public class Node<K,V> {
    /**
     * 虚拟节点 环上面的位置  k -> HashMap
     */
    final Map<Integer,Map<K,V>> virtualNodes = new HashMap<>();

}
