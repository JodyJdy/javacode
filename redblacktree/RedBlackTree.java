/**
 * @author jdy
 * @title: RedBlackTree
 * @description:
 * @data 2023/9/7 16:26
 */
import static utl.CompareUtil.*;
public class RedBlackTree <Key extends Comparable<Key>,Val extends NodeValue<Key>>{
    private TreeNode<Key,Val> root;


    public void insert(Val v) {
        TreeNode<Key, Val> node = new TreeNode<>(ColorEnum.RED,v);
        // case 1 : 第一个节点插入
        if (root == null) {
            root = node;
            return;
        }
        // 找到要插入的节点
        TreeNode<Key, Val> insertNode = searchNodeForInsert(v.getKey(), root);

        node.setParent(insertNode);
        if (insertLeft(v, insertNode)) {
            insertNode.setLeft(node);
        } else{
            insertNode.setRight(node);
        }
        // case2  父节点是根节点，染色即可
        if (insertNode.equals(root)) {
            root.setColor(ColorEnum.BLACK);
            return;
        }
        color(node);
    }

    public void color(TreeNode<Key, Val> cur) {
        if (root.equals(cur)) {
            return;
        }
        TreeNode<Key, Val> parent = cur.getParent();
        // case3 当前节点 N 的父节点 P 和叔节点 U 均为红色
        if (parent.getColor() == ColorEnum.RED && parent.getParent().getSiblings().getColor() == ColorEnum.RED) {
            parent.setColor(ColorEnum.BLACK);
            parent.getSiblings().setColor(ColorEnum.BLACK);
            parent.getParent().setColor(ColorEnum.RED);
            color(parent.getParent());
            return;
        }
        //case 4 当前节点 N 与父节点 P 的方向相反
        if(cur.getDirection() != parent.getDirection()){
            if (cur.getDirection() == Direction.LEFT) {
                rotateRight(parent);
            } else{
                rotateLeft(parent);
            }
            color(parent);
            return;
        }
        // case 5  当前节点 N 与父节点 P 的方向相同
        if (cur.getDirection() == parent.getDirection()) {
            TreeNode<Key,Val> grandPa = parent.getParent();
            if (cur.getDirection() == Direction.LEFT) {
                rotateRight(grandPa);
            } else{
                rotateLeft(grandPa);
            }
            parent.setColor(ColorEnum.BLACK);
            grandPa.setColor(ColorEnum.RED);
        }
    }

    public boolean insertLeft(Val v, TreeNode<Key, Val> node) {
        return lt(v.getKey(),node.getVal().getKey());
    }


    public TreeNode<Key, Val> searchNodeForInsert(Key k, TreeNode<Key,Val> from) {
        Key compare = from.getVal().getKey();
        if (eq(k, compare)) {
            return from;
        }
        if(lt(k,compare)){
            if (from.getLeft() != null) {
                return searchNodeForInsert(k, from.getLeft());
            }
        } else{
            if (from.getRight() != null) {
                return searchNodeForInsert(k, from.getRight());
            }
        }
        return from;

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
