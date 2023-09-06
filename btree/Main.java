/**
 * @author jdy
 * @title: Main
 * @description:
 * @data 2023/9/6 10:29
 */
public class Main {
    public static void main(String[] args) {

        BPlusTree<Integer,Person> tree = new BPlusTree<>(3);
        tree.insert(new Person(1));
        tree.insert(new Person(2));
        tree.insert(new Person(3));
        tree.insert(new Person(4));
        tree.insert(new Person(5));
        tree.insert(new Person(6));

        tree.delete(1);
        tree.delete(2);
        tree.delete(3);
        System.out.println("hello world");
    }

    static class Person implements NodeValue<Integer>{
        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    '}';
        }

        private Integer id;

        public Person(Integer id) {
            this.id = id;
        }

        @Override
        public Integer getKey() {
            return id;
        }

    }

}
