/**
 * @author jdy
 * @title: Main
 * @description:
 * @data 2023/9/8 10:33
 */
public class Main {
    public static void main(String[] args) {
        AvlTree<Integer,Person> tree = new AvlTree<>();
        for (int i = 1; i < 1000; i++) {
            tree.insert(new Person(i));
        }
        for (int i = 1; i < 500; i++) {
            tree.delete(i);
        }
        System.out.println(tree.testBalance(tree.getRoot()));
        show(tree.getRoot());
    }
    public static <K extends Comparable<K>,V extends NodeValue<K>> void  show(TreeNode<K,V>node){
        if (node == null) {
            return;
        }
        show(node.getLeft());
        System.out.println(node.getVal());
        show(node.getRight());
    }


    public static class Person implements NodeValue<Integer>{
        public Person(Integer id) {
            this.id = id;
        }

        private Integer id;

        @Override
        public Integer getKey() {
            return id;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    '}';
        }
    }
}
