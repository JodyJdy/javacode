public class Main {
    public static void main(String[] args) {

        //测试插入字符串的场景
        TrieTree<Character, String> trie = new TrieTree<>(
                String::charAt,
                String::length
        );

        trie.insert("car");
        trie.insert("word");
        trie.insert("tar");
        System.out.println(trie.delete("car"));
        System.out.println(trie.search("car"));
        System.out.println(trie.search("word"));
        System.out.println(trie.startsWith("wo"));

        //测试插入int列表的场景
        TrieTree<Integer, Integer[]> trie2 = new TrieTree<>(
                (a, i) -> a[i],
                a -> a.length
        );
        trie2.insert(new Integer[]{1, 2, 3});
        trie2.insert(new Integer[]{4, 5, 6, 7});
        System.out.println(trie2.search(new Integer[]{1, 2, 3}));
    }
}