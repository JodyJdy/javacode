/**
 * 使用 Val 表示节点值，K来表示 节点排序字段
 * @author jdy
 * @title: NodeValue
 * @description:
 * @data 2023/9/6 11:11
 */
public interface NodeValue<K extends Comparable<K>>{
    /**
     * 获取可排序的key
     */
    K getKey();
}
