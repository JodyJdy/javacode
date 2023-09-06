import java.util.ArrayList;
import java.util.List;

/**
 * @author jdy
 * @title: TreeLeafNode
 * @description:
 * @data 2023/9/6 10:42
 */
public class TreeLeafNode<Key extends Comparable<Key>,Val extends NodeValue<Key>> extends TreeNode<Key,Val>{
    private final List<Val> values = new ArrayList<>();

    /**
     * 叶子节点的右节点
     */
    private TreeLeafNode<Key,Val> rightNode;

    private TreeLeafNode<Key,Val> leftNode;


    public TreeLeafNode(NodeType type) {
        super(type);
    }


    public List<Val> getValues() {
        return values;
    }

    public void addVal(Val v) {
        values.add(v);
    }

    public TreeLeafNode<Key, Val> getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(TreeLeafNode<Key, Val> leftNode) {
        this.leftNode = leftNode;
    }

    public TreeLeafNode<Key, Val> getRightNode() {
        return rightNode;
    }

    public void setRightNode(TreeLeafNode<Key, Val> rightNode) {
        this.rightNode = rightNode;
    }

    @Override
    public TreeLeafNode<Key, Val> getLeftSiblings() {
        return (TreeLeafNode<Key, Val>) super.getLeftSiblings();
    }

    @Override
    public TreeLeafNode<Key, Val> getRightSiblings() {
        return (TreeLeafNode<Key, Val>) super.getRightSiblings();
    }
    public Val removeLastVal(){
        return values.remove(values.size() - 1);
    }
    public Val removeFirstVal(){
        return values.remove(0);
    }
}
