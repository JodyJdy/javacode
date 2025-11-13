import java.util.HashMap;
import java.util.Map;

/**
 *Trie 树节点
 */
public class TrieNode<Char , Word> {
    /**
     * 子节点, key是字符
     */
    final Map<Char, TrieNode<Char, Word>> children = new HashMap<>();
    /**
     * 当前节点是否是单词的结束
     */
    Boolean isWordEnd = false;
}
