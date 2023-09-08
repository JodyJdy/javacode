/**
 * j
 * @author jdy
 * @title: AvlTree
 * @description:
 * @data 2023/9/8 9:38
 */
import static utl.CompareUtil.*;
public class AvlTree<Key extends Comparable<Key>, Val extends NodeValue<Key>> {

    private TreeNode<Key,Val> root;

    public void insert(Val v) {
        //第一次插入
        if (root == null) {
            root = new TreeNode<>(v);
            return;
        }
        TreeNode<Key, Val> node = search(v.getKey(), root);
        //key已经存在，原地替换
        if (node != null) {
            node.setVal(v);
            return;
        }
        TreeNode<Key, Val> newNode = new TreeNode<>(v);
        //查找待插入的节点
        TreeNode<Key, Val> nodeForInsert = searchNodeForInsert(v.getKey(), root);
        //设置父节点
        newNode.setParent(nodeForInsert);
        if (lt(v.getKey(), nodeForInsert.getKey())) {
            nodeForInsert.setLeft(newNode);
        } else{
            nodeForInsert.setRight(newNode);
        }
        balance(nodeForInsert);
    }

    public void balance(TreeNode<Key,Val> node){
        //可能当前节点是平衡的，但是父节点是不平衡的，因此要迭代到root节点
        while (node != null) {
            BalanceTypeEnum type = testBalance(node);
            switch (type) {
                case LL : node = rotateRight(node);break;
                case LR : {
                    rotateLeft( node.getLeft());
                    node = rotateRight(node);
                }break;
                case RR: node = rotateLeft(node);break;
                case RL : {
                    rotateRight(node.getRight());
                    node = rotateLeft(node);
                }
                break;
            }
            //经过旋转后的node已经发生了改变，更改为改变后node的parent
            node = node.getParent();
        }
    }

    public Val search(Key key) {
        TreeNode<Key, Val> node = search(key, root);
        if (node != null) {
            return node.getVal();
        }
        return null;
    }

    private TreeNode<Key, Val> searchNodeForInsert(Key key, TreeNode<Key, Val> from) {
        if(lt(key,from.getKey())){
            if (from.getLeft() != null) {
                return searchNodeForInsert(key,from.getLeft());
            }
        }
        if (ge(key, from.getKey())) {
            if (from.getRight() != null) {
                return searchNodeForInsert(key, from.getRight());
            }
        }
        return from;
    }
    private TreeNode<Key,Val> search(Key key, TreeNode<Key, Val> from) {
        if (from == null) {
            return null;
        }
        if (key.equals(from.getKey())) {
            return from;
        }
        if (lt(key, from.getKey())) {
            return search(key, from.getLeft());
        }
        return search(key, from.getRight());
    }
    public void delete(Key key) {
        TreeNode<Key, Val> node = search(key, root);
        doDeleteNode(node);
    }

    private void doDeleteNode(TreeNode<Key, Val> node) {
        if (node == null) {
            return;
        }
        TreeNode<Key,Val> parent = node.getParent();
        // 删除的是叶子节点
        if (node.getLeft() == null && node.getRight() == null) {
            if (node.equals(root)) {
                root = null;
                return;
            }
            if(node.equals(parent.getLeft())){
                parent.setLeft(null);
            } else{
                parent.setRight(null);
            }
            balance(parent);
            return;
        }
        //只有左子树或者只有右子树， 用左子树 或者右字树替代即可
        if((node.getLeft() == null && node.getRight() != null)||(node.getLeft() != null && node.getRight() == null)){
            TreeNode<Key,Val> replace = node.getLeft() == null ? node.getRight() : node.getLeft();
            if (root.equals(node)) {
                root = replace;
                return;
            }
            if(node.equals(parent.getLeft())){
                parent.setLeft(replace);
            } else{
                parent.setRight(replace);
            }
            replace.setParent(parent);
            balance(parent);
            return;
        }
        // 既有左子树，又有右子树，从左边找一个最大的，进行交换
        if (node.getLeft() != null && node.getRight() != null) {
            TreeNode<Key, Val> leftMax = findLeftMax(node.getLeft());
            Val t = node.getVal();
            node.setVal(leftMax.getVal());
            leftMax.setVal(t);
            doDeleteNode(leftMax);
        }
    }
    public TreeNode<Key,Val> findLeftMax(TreeNode<Key,Val> left){
        while (left.getRight() != null) {
            left = left.getRight();
        }
        return left;
    }

    public TreeNode<Key, Val> rotateLeft(TreeNode<Key, Val> node) {
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
        if (newNode.getLeft() != null) {
            newNode.getLeft().setParent(node);
        }
        newNode.setLeft(node);
        //设置父节点中的子节点为 newNode
        if (node.equals(root)) {
            this.root = newNode;
        } else if(node.equals(parent.getLeft())){
            parent.setLeft(newNode);
        } else{
            parent.setRight(newNode);
        }
        //设置parent
        node.setParent(newNode);
        newNode.setParent(parent);
        return newNode;
    }

    public TreeNode<Key, Val> rotateRight(TreeNode<Key, Val> node) {
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
        if (newNode.getRight() != null) {
            newNode.getRight().setParent(node);
        }
        newNode.setRight(node);

        //设置父节点中的子节点为 newNode
        if (node.equals(root)) {
            this.root = newNode;
        } else if(node.equals(parent.getLeft())){
            parent.setLeft(newNode);
        } else{
            parent.setRight(newNode);
        }
        //设置parent
        node.setParent(newNode);
        newNode.setParent(parent);
        return newNode;
    }

    public BalanceTypeEnum testBalance(TreeNode<Key, Val> node) {
        TreeNode<Key,Val> leftNode = node.getLeft();
        TreeNode<Key,Val> rightNode = node.getRight();
        int left = maxHeight(leftNode);
        int right = maxHeight(rightNode);
        //不平衡，判断不平衡的类型
        if(Math.abs(left - right) > 1){
            // LL 或者 LR
            if (left - right > 1) {
                if (leftNode.getLeft() != null && maxHeight(leftNode.getLeft()) - right >= 1) {
                   return BalanceTypeEnum.LL;
                } else if (leftNode.getRight() != null && maxHeight(leftNode.getRight()) - right >= 1) {
                    return BalanceTypeEnum.LR;
                } else{
                    throw new RuntimeException("结构异常或有多处不平衡");
                }
            } else{
                if (rightNode.getRight() != null && maxHeight(rightNode.getRight()) - left >= 1) {
                   return BalanceTypeEnum.RR;
                } else if(rightNode.getRight() != null && maxHeight(rightNode.getLeft()) - left >= 1){
                    return BalanceTypeEnum.RL;
                } else{
                    throw new RuntimeException("结构异常或有多处不平衡");
                }
            }
        }
        return BalanceTypeEnum.BALANCE;
    }

    public int maxHeight(TreeNode<Key, Val> node) {
        if (node == null) {
            return 0;
        }
        int left = maxHeight(node.getLeft());
        int right = maxHeight(node.getRight());
        return Math.max(left,right) + 1;
    }

    public TreeNode<Key, Val> getRoot() {
        return root;
    }

    public void setRoot(TreeNode<Key, Val> root) {
        this.root = root;
    }
}
