/**
 * @author jdy
 * @title: Main
 * @description:
 * @data 2023/9/7 13:06
 */
public class Main {
    public static void main(String[] args) {
        RedBlackTree<String ,Person> tree = new RedBlackTree<>();

        tree.insert(new Person("1"));
        tree.insert(new Person("2"));
        tree.insert(new Person("3"));
        tree.insert(new Person("4"));
        tree.insert(new Person("5"));


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
