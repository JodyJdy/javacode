/**
 * @author jdy
 * @title: TreeNode
 * @description:
 * @data 2023/9/7 16:25
 */
public class TreeNode<Key extends Comparable<Key>,Val extends NodeValue<Key>> {
    private ColorEnum color;
    private Val val;
    private TreeNode<Key,Val> parent;
    private TreeNode<Key,Val> left;
    private TreeNode<Key,Val> right;


    public TreeNode(Val val) {
        this.val = val;
    }

    public TreeNode(ColorEnum color, Val val) {
        this.color = color;
        this.val = val;
    }

    /**
     * 在父节点中的方向
     */
    public Direction getDirection(){
        if (parent == null) {
            return Direction.ROOT;
        }
        if (this.equals(parent.left)) {
            return Direction.LEFT;
        }
        if (this.equals(parent.right)) {
            return Direction.RIGHT;
        }
        throw new RuntimeException("结构异常");
    }

    public ColorEnum getColor() {
        return color;
    }

    public void setColor(ColorEnum color) {
        this.color = color;
    }

    public Val getVal() {
        return val;
    }

    public void setVal(Val val) {
        this.val = val;
    }

    public TreeNode<Key, Val> getParent() {
        return parent;
    }

    public void setParent(TreeNode<Key, Val> parent) {
        this.parent = parent;
    }

    public TreeNode<Key, Val> getLeft() {
        return left;
    }

    public void setLeft(TreeNode<Key, Val> left) {
        this.left = left;
    }

    public TreeNode<Key, Val> getRight() {
        return right;
    }

    public void setRight(TreeNode<Key, Val> right) {
        this.right = right;
    }


    /**
     * 获取兄弟节点
     */
    public TreeNode<Key,Val> getSiblings(){
        switch (getDirection()) {
            case LEFT: return parent.getRight();
            case RIGHT: return parent.getLeft();
            default: return null;
        }

    }
}
