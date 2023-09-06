import java.util.ArrayList;
import java.util.List;

/**
 * 索引节点
 * @author jdy
 * @title: TreeIndexNode
 * @description:
 * @data 2023/9/6 10:41
 */
public class TreeIndexNode<Key extends Comparable<Key>,Val extends NodeValue<Key>> extends TreeNode<Key,Val>{
    /**
     * 子节点
     */
    private final List<TreeNode<Key, Val>> subNodes = new ArrayList<>();
    public TreeIndexNode(NodeType type) {
        super(type);
    }
    public TreeNode<Key,Val> getLastNode(){
        return subNodes.get(subNodes.size() - 1);
    }

    public int getSubNodeIndex(TreeNode<Key, Val> node) {
        return subNodes.indexOf(node);
    }
    public List<TreeNode<Key,Val>> getSubNodes() {
        return subNodes;
    }
    public void addNode(TreeNode<Key, Val> node) {
        subNodes.add(node);
    }

    @Override
    public TreeIndexNode<Key, Val> getLeftSiblings() {
        return (TreeIndexNode<Key, Val>) super.getLeftSiblings();
    }
    @Override
    public TreeIndexNode<Key, Val> getRightSiblings() {
        return (TreeIndexNode<Key, Val>) super.getRightSiblings();
    }
}
