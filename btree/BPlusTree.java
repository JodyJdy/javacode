import java.util.Iterator;
import java.util.List;

import static utl.CompareUtil.*;

/**
 * @author jdy
 * @title: BTree
 * @description:
 * @data 2023/9/6 10:33
 */
public class BPlusTree<Key extends Comparable<Key>, Val extends NodeValue<Key>> {
    /**
     * b+ 树 度的数量
     */
    private final int m;

    public TreeNode<Key, Val> getRootNode() {
        return rootNode;
    }

    private TreeNode<Key, Val> rootNode;

    public BPlusTree(int m) {
        this.m = m;
    }


    public Val search(Key k) {
        TreeLeafNode<Key, Val> treeLeafNode = searchNode(k);
        for (Val v : treeLeafNode.getValues()) {
            if (eq(v.getKey(), k)) {
                return v;
            }
        }
        return null;
    }

    /**
     * 搜索key 所在的叶子节点
     */
    public TreeLeafNode<Key, Val> searchNode(Key k) {
        return doSearchNode(k, rootNode);
    }

    /**
     * 从 from 节点开始进行搜索
     */
    private TreeLeafNode<Key, Val> doSearchNode(Key k, TreeNode<Key, Val> from) {
        TreeIndexNode<Key, Val> indexNode;
        while (!from.isLeaf()) {
            indexNode = (TreeIndexNode<Key, Val>) from;
            List<Key> keyList = from.getKeys();
            int i = 0;
            for (; i < keyList.size(); i++) {
                if (lt(k, keyList.get(i))) {
                    from = indexNode.getSubNodes().get(i);
                    break;
                } else if(eq(k,keyList.get(i))&& i + 1 < indexNode.getSubNodes().size()){
                   from = indexNode.getSubNodes().get(i+1) ;
                   break;
                }
            }
            //未找到，那就是最后一个
            if (i == keyList.size()) {
                from = indexNode.getLastNode();
            }
        }
        return (TreeLeafNode<Key, Val>) from;
    }

    /**
     *如果key 相同的val已经存在，会进行替换
     */
    public void insert(Val val) {
        //插入根节点
        if (rootNode == null) {
            rootNode = new TreeLeafNode<>(NodeType.ROOT_LEAF);
            //进行转型，方便处理
            TreeLeafNode<Key, Val> tempRoot = (TreeLeafNode<Key, Val>) rootNode;
            tempRoot.addKey(val.getKey());
            tempRoot.addVal(val);
            return;
        }
        //根据key找到指定的位置
        Key k = val.getKey();
        TreeLeafNode<Key, Val> insertedNode = searchNode(k);
        int index =insertedNode.getKeys().indexOf(val.getKey());
        //如果key相同，进行替换，这样就不会有重复的key出现
        if(index >= 0){
            insertedNode.getValues().set(index, val);
            return;
        }
        doInsertNode(val, insertedNode);
    }

    /**
     * 向叶子节点添加值
     */
    private void doInsertNode(Val val, TreeLeafNode<Key, Val> t) {
        List<Key> keys = t.getKeys();
        List<Val> values = t.getValues();
        int insertIndex = -1;
        for (int i = 0; i < keys.size(); i++) {
            if (le(val.getKey(), keys.get(i))) {
                insertIndex = i;
                break;
            }
        }
        if (insertIndex == -1) {
            keys.add(val.getKey());
            values.add(val);
        } else {
            keys.add(insertIndex, val.getKey());
            values.add(insertIndex, val);
        }
        // 如果满了，进行分裂
        if (isFull(t)) {
            split(t);
        }
    }

    /**
     * 插入数据后，导致页分裂时
     * 向父节点中插入一个key
     */
    private void insertParentIndexNode(TreeIndexNode<Key, Val> parentNode, TreeNode<Key, Val> left, TreeNode<Key, Val> right, Key k, TreeNode<Key, Val> splitNode) {
        //获取被分裂的节点的下标
        int index = parentNode.getSubNodes().indexOf(splitNode);
        //新插入两个节点，替换原有的 splitNode
        parentNode.getSubNodes().set(index, right);
        parentNode.getSubNodes().add(index, left);

        //插入key
        int insertIndex = -1;
        for (int i = 0; i < parentNode.getKeys().size(); i++) {
            if (le(k, parentNode.getKeys().get(i))) {
                insertIndex = i;
                break;
            }
        }
        if (insertIndex == -1) {
            parentNode.addKey(k);
        } else {
            parentNode.getKeys().add(insertIndex, k);
        }
        //判断 parentNode 需不需要 分裂
        if (isFull(parentNode)) {
            split(parentNode);
        }
    }

    /**
     * 插入索引节点
     */
    private void doInsertIndexNode(TreeIndexNode<Key, Val> t, Key k) {

    }


    private void splitLeafNode(TreeLeafNode<Key, Val> leafNode) {
        //创建两个新的叶子节点
        TreeLeafNode<Key, Val> left = new TreeLeafNode<>(NodeType.LEAF);
        TreeLeafNode<Key, Val> right = new TreeLeafNode<>(NodeType.LEAF);
        //维护双向链表关系
        if (leafNode.getLeftNode() != null) {
            leafNode.getLeftNode().setRightNode(left);
        }
        //设置叶子节点的右节点
        left.setRightNode(right);
        right.setRightNode(leafNode.getRightNode());
        //设置叶子节点的左节点
        right.setLeftNode(left);
        left.setLeftNode(leafNode.getLeftNode());
        //拷贝数据，左节点，包含前 m/2个记录
        for (int i = 0; i < m / 2; i++) {
            left.addKey(leafNode.getKeys().get(i));
            left.addVal(leafNode.getValues().get(i));
        }
        for (int i = m / 2; i < m; i++) {
            right.addKey(leafNode.getKeys().get(i));
            right.addVal(leafNode.getValues().get(i));
        }
        //向父节点插入索引节点，父节点一定是 TreeIndexNode类型的
        //t 是root节点，需要重新创建root节点
        if (leafNode.getParentNode() == null) {
            this.rootNode = new TreeIndexNode<>(NodeType.ROOT_INDEX);
            leafNode.setParentNode((TreeIndexNode<Key, Val>) this.rootNode);
            ((TreeIndexNode<Key, Val>) this.rootNode).addNode(leafNode);
        }
        //设置 left，right的父节点
        left.setParentNode(leafNode.getParentNode());
        right.setParentNode(leafNode.getParentNode());
        //父节点中添加key
        insertParentIndexNode(leafNode.getParentNode(), left, right, leafNode.getKeys().get(m / 2), leafNode);
    }

    private void splitIndexNode(TreeIndexNode<Key, Val> indexNode) {
        //创建两个新的叶子节点
        TreeIndexNode<Key, Val> left = new TreeIndexNode<>(NodeType.INDEX);
        TreeIndexNode<Key, Val> right = new TreeIndexNode<>(NodeType.INDEX);
        //设置叶子节点的右节点
        //拷贝数据，左节点，包含前 m/2个记录
        for (int i = 0; i < m / 2; i++) {
            //拷贝key
            left.addKey(indexNode.getKeys().get(i));
        }
        final Iterator<TreeNode<Key, Val>> each = indexNode.getSubNodes().iterator();
        Key mid = indexNode.getKeys().get(m / 2);
        while (each.hasNext()) {
            TreeNode<Key, Val> cur = each.next();
            if (lt(cur.getLastKey(), mid)) {
                left.addNode(cur);
                cur.setParentNode(left);
                each.remove();
            }
        }
        //这里与 splitNode不一样， 第m/2 个key 插入到父节点中
        for (int i = m / 2 + 1; i < m; i++) {
            right.addKey(indexNode.getKeys().get(i));
        }
        //剩下的全都加入到right里面
        right.getSubNodes().addAll(indexNode.getSubNodes());
        right.getSubNodes().forEach(s -> s.setParentNode(right));

        //向父节点插入索引节点，父节点一定是 TreeIndexNode类型的
        //t 是root节点，需要重新创建root节点
        if (indexNode.getParentNode() == null) {
            this.rootNode = new TreeIndexNode<>(NodeType.ROOT_INDEX);
            indexNode.setParentNode((TreeIndexNode<Key, Val>) this.rootNode);
            ((TreeIndexNode<Key, Val>) this.rootNode).addNode(indexNode);
        }
        //设置 left，right的父节点
        left.setParentNode(indexNode.getParentNode());
        right.setParentNode(indexNode.getParentNode());
        //父节点中添加key
        insertParentIndexNode(indexNode.getParentNode(), left, right, indexNode.getKeys().get(m / 2), indexNode);
    }

    private void split(TreeNode<Key, Val> t) {
        //叶子节点分裂
        if (t instanceof TreeLeafNode) {
            splitLeafNode((TreeLeafNode<Key, Val>) t);
            return;
        }
        //索引节点分裂
        splitIndexNode((TreeIndexNode<Key, Val>) t);
    }



    public boolean isFull(TreeNode<Key, Val> t) {
        return t.getKeys().size() >= m;
    }


    public boolean isEmpty(TreeNode<Key, Val> t) {
        return t.getKeys().size() <= m /2 - 1;
    }

    /**
     * 节点是否富裕
     */
    public boolean isRich(TreeNode<Key, Val> t) {
        return t.getKeys().size() > m/2;
    }

    public void delete(Key key) {
        TreeLeafNode<Key,Val> node = searchNode(key);
        if (node == null) {
            throw new RuntimeException("key不存在:" + key);
        }
        doLeafNodeDelete(node, key);
    }
    private void doLeafNodeDelete(TreeLeafNode<Key, Val> node, Key key) {
        //删除节点
        int index = node.getKeys().indexOf(key);
        node.getKeys().remove(index);
        node.getValues().remove(index);
        //如果是root节点直接删除返回
        if (node.getParentNode() == null) {
            if (node.getKeys().isEmpty()) {
                rootNode = null;
            }
            return;
        }
        //不用调整，直接结束
        if (!isEmpty(node)) {
            return;
        }
        //获取当前节点在父节点中的顺序
        int nodeIndex = node.getParentNode().getSubNodeIndex(node);
        //左兄弟有富裕，借一个key
        if (node.getLeftSiblings() != null) {
            TreeLeafNode<Key,Val> leftSiblings = node.getLeftSiblings();
            if(isRich(leftSiblings)){
                Key lastKey = leftSiblings.removeLastKey();
                node.getKeys().add(0,lastKey);
                node.getValues().add(0,leftSiblings.removeLastVal());
                //调整key 为 借的兄弟节点的key
                //左兄弟的最后一个key（借的哪一个）替换为父节点上面的key
                node.getParentNode().getKeys().set(nodeIndex - 1, lastKey);
                return;
            }
        }
        //右兄弟有富裕，借一个key
        if (node.getRightSiblings() != null) {
            TreeLeafNode<Key,Val> rightSiblings = node.getRightSiblings();
            if (isRich(rightSiblings)) {
                node.getKeys().add(0,rightSiblings.removeFirstKey());
                node.getValues().add(0,rightSiblings.removeFirstVal());
                //右兄弟的第一个key（借过之后的第一个）替换为父节点上面的key
                node.getParentNode().getKeys().set(nodeIndex, rightSiblings.getKeys().get(0));
                return;
            }
        }
        //无富裕，尝试合并
        if (node.getLeftSiblings() != null) {
            TreeLeafNode<Key,Val> leftSiblings = node.getLeftSiblings();
                //合并
                //从父节点中删除node
                node.getParentNode().getSubNodes().remove(node);
                leftSiblings.getKeys().addAll(node.getKeys());
                leftSiblings.getValues().addAll(node.getValues());
                //删除父节点中多余的key
                node.getParentNode().getKeys().remove(nodeIndex - 1);
            if (!isEmpty(node.getParentNode())) {
                return;
            }
            //处理父节点
            doIndexNodeDelete(node.getParentNode());
            return;
        }
        if (node.getRightSiblings() != null) {
            //合并
            TreeLeafNode<Key,Val> rightSiblings = node.getRightSiblings();
            node.getParentNode().getSubNodes().remove(node);
            for (Key k : node.getKeys()) {
                rightSiblings.getKeys().add(0,k);
            }
            for (Val v : node.getValues()) {
                rightSiblings.getValues().add(0,v);
            }
            //删除父节点中多余的key
            node.getParentNode().getKeys().remove(nodeIndex);
            if (!isEmpty(node.getParentNode())) {
                return;
            }
            //处理父节点
            doIndexNodeDelete(node.getParentNode());
            return;
        }
    }
    private void doIndexNodeDelete(TreeIndexNode<Key, Val> node) {
        int nodeIndex = node.getParentNode().getSubNodeIndex(node);
        if (node.getLeftSiblings() != null) {
            TreeIndexNode<Key,Val> leftSiblings = node.getLeftSiblings();
            if (isRich(leftSiblings)) {
                Key t = node.getParentNode().getKeys().get(nodeIndex - 1);
                Key t2 = leftSiblings.removeLastKey();
                //兄弟节点key上移动
                node.getParentNode().getKeys().set(nodeIndex - 1, t2);
                //父节点key下一移动，移动到左兄弟结束位置
                leftSiblings.getKeys().add(t);
                return;
            }
        }
        if (node.getRightSiblings() != null) {
            TreeIndexNode<Key,Val> rightSiblings = node.getRightSiblings();
            if (isRich(rightSiblings)) {
                Key t = node.getParentNode().getKeys().get(nodeIndex);
                Key t2 = rightSiblings.removeFirstKey();
                //兄弟节点key上移动
                node.getParentNode().getKeys().set(nodeIndex, t2);
                //父节点key下移动，移动到右兄弟开始位置
                rightSiblings.getKeys().add(0,t);
                return;
            }
        }
        if (node.getLeftSiblings() != null) {
           //合并
            TreeIndexNode<Key,Val> leftSiblings = node.getLeftSiblings();
            //删除父节点中的key
            Key t = node.getParentNode().getKeys().remove(nodeIndex - 1);
            leftSiblings.getKeys().add(t);
            leftSiblings.getKeys().addAll(node.getKeys());
            //删除node节点，合并成 leftSibling了
            node.getParentNode().getSubNodes().remove(node);
            //root情况特殊处理
            if (node.getParentNode() == rootNode && rootNode.getKeys().isEmpty()) {
                rootNode = leftSiblings;
                leftSiblings.setParentNode(null);
                return;
            }
            if(!isRich(leftSiblings)){
                doIndexNodeDelete(leftSiblings);
                return;
            }
        }
        if (node.getRightSiblings() != null){
           //合并
            TreeIndexNode<Key,Val> rightSiblings = node.getRightSiblings();
            Key t = node.getParentNode().getKeys().get(nodeIndex);
            node.getKeys().add(t);
            node.getKeys().addAll(rightSiblings.getKeys());
            node.getParentNode().getSubNodes().remove(rightSiblings);
            if (node.getParentNode() == rootNode && rootNode.getKeys().isEmpty()) {
                rootNode = node;
                node.setParentNode(null);
                return;
            }
            if(!isRich(node)){
                doIndexNodeDelete(node);
            }

        }
    }


}
