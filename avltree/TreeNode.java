import java.util.ArrayList;
import java.util.List;

/**
 * @author jdy
 * @title: TreeNode
 * @description:
 * @data 2023/9/6 10:36
 */
public  class TreeNode<Key extends Comparable<Key>, Val extends NodeValue<Key>> {
    public TreeNode<Key, Val> getRight() {
        return right;
    }

    public TreeNode(Val val) {
        this.val = val;
    }

    public void setVal(Val val) {
        this.val = val;
    }

    private  Val val;
    /**
     * 左节点
     */
    private TreeNode<Key,Val> left;
    /**
     * 右节点
     */
    private TreeNode<Key,Val> right;

    public void setRight(TreeNode<Key, Val> right) {
        this.right = right;
    }

    public TreeNode<Key, Val> getParent() {
        return parent;
    }

    public void setParent(TreeNode<Key, Val> parent) {
        this.parent = parent;
    }

    /**
     * 父节点
     */
    private TreeNode<Key,Val> parent;


    public TreeNode<Key, Val> getLeft() {
        return left;
    }

    public Val getVal() {
        return val;
    }

    public void setLeft(TreeNode<Key, Val> left) {
        this.left = left;
    }

    public Key getKey(){
        return val.getKey();
    }
}
