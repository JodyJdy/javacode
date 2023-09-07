/**
 * @author jdy
 * @title: RedBlackTree
 * @description:
 * @data 2023/9/7 16:26
 */
public class RedBlackTree <Key extends Comparable<Key>,Val extends NodeValue<Key>>{
    private TreeNode<Key,Val> root;


    public void insert(Val v) {

    }

    public Val search(Key k) {

        return null;
    }
    public TreeNode<Key, Val> searchNode(Key k) {
        return null;
    }

    public void delete(Key k) {

    }


    /**
     *左旋
     *        |                       |
     *        N                       S
     *       / \     l-rotate(N)     / \
     *      L   S    ==========>    N   R
     *         / \                 / \
     *        M   R               L   M
     */
    public void rotateLeft(TreeNode<Key, Val> node) {
        if (node == null) {
            throw new RuntimeException("无法旋转");
        }
        if (node.getRight() == null) {
            throw new RuntimeException("无法旋转");
        }

        //父节点
        TreeNode<Key,Val> parent = node.getParent();
        //newNode 用于替换 当前node的位置
        TreeNode<Key,Val> newNode = node.getRight();

        node.setRight(newNode.getLeft());
        newNode.setLeft(node);

        //设置父节点中的子节点为 newNode
        switch (node.getDirection()) {
            case ROOT:this.root = newNode;break;
            case LEFT:parent.setLeft(newNode);break;
            case RIGHT:parent.setRight(newNode);break;
        }
        //设置parent
        node.setParent(newNode);
        newNode.setParent(parent);
    }

    /**
     * 右旋
     * <p>
     * *         |                       |
     * *        N                       S
     * *       / \     r-rotate(N)     / \
     * *      L   S    <==========    N   R
     * *         / \                 / \
     * *        M   R               L   M
     */
    public void rotateRight(TreeNode<Key, Val> node) {
        if (node == null) {
            throw new RuntimeException("无法旋转");
        }
        if (node.getLeft() == null) {
            throw new RuntimeException("无法旋转");
        }

        //父节点
        TreeNode<Key,Val> parent = node.getParent();
        //newNode 用于替换 当前node的位置
        TreeNode<Key,Val> newNode = node.getLeft();

        node.setLeft(newNode.getRight());
        newNode.setRight(node);

        //设置父节点中的子节点为 newNode
        switch (node.getDirection()) {
            case ROOT:this.root = newNode;break;
            case LEFT:parent.setLeft(newNode);break;
            case RIGHT:parent.setRight(newNode);break;
        }
        //设置parent
        node.setParent(newNode);
        newNode.setParent(parent);
    }
}
