/**
 * @author jdy
 * @title: Main
 * @description:
 * @data 2023/9/7 13:06
 */
public class Main {
    public static void main(String[] args) {
        TreeNode<String, Person> root = new TreeNode<>(new Person("root"));
        TreeNode<String,Person> n = new TreeNode<>(new Person("n"));
        TreeNode<String,Person> l = new TreeNode<>(new Person("l"));
        TreeNode<String,Person> s = new TreeNode<>(new Person("s"));
        TreeNode<String,Person> m = new TreeNode<>(new Person("m"));
        TreeNode<String,Person> r = new TreeNode<>(new Person("r"));

        root.setLeft(n);
        n.setParent(root);
        n.setLeft(l);
        l.setParent(n);
        n.setRight(s);
        s.setParent(n);
        s.setLeft(m);
        s.setRight(r);
        m.setParent(s);
        r.setParent(s);
        RedBlackTree<String , Person> redBlackTree = new RedBlackTree<String , Person>();
        redBlackTree.rotateLeft(root.getLeft());
        show(root);

    }
    public static <K extends Comparable<K>,V extends NodeValue<K>> void  show(TreeNode<K,V>node){
        if (node == null) {
            return;
        }
        System.out.println(node.getVal());
        show(node.getLeft());
        show(node.getRight());
    }



    public static class Person  implements NodeValue<String>{
        @Override
        public String  getKey() {
            return key;
        }
        String key;

        public Person(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "key='" + key + '\'' +
                    '}';
        }
    }
}
