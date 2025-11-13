import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 *字典树实现
 * @param <Char> 表示插入序列的子元素
 * @param <Word> 表示插入的序列
 */
public class TrieTree<Char, Word> {
    /**
     * 从序列中获取第i个字符
     */
    private final TriFunction<Word, Integer, Char> getCharFunc;
    /**
     * 获取序列的长度
     */
    private final Function<Word, Integer> lengthFunc;
    /**
     * 根节点
     */
    private final TrieNode<Char, Word> root;

    public TrieTree(TriFunction<Word, Integer, Char> getCharFunc, Function<Word, Integer> lengthFunc) {
        this.getCharFunc = getCharFunc;
        this.lengthFunc = lengthFunc;
        root = new TrieNode<>();
    }

    public void insert(Word w) {
        int length = lengthFunc.apply(w);
        TrieNode<Char, Word> temp = root;
        for (int i = 0; i < length; i++) {
            Char ch = getCharFunc.apply(w, i);
            if (!temp.children.containsKey(ch)) {
                temp.children.put(ch, new TrieNode<>());
            }
            temp = temp.children.get(ch);
        }
        temp.isWordEnd = true;
    }

    public boolean search(Word w) {
        return search(w, null,false);
    }
    private boolean search(Word w, List<TrieNode<Char, Word>> path, boolean isPrefix){
        if (path != null) {
            path.add(root);
        }
        int length = lengthFunc.apply(w);
        TrieNode<Char, Word> temp = root;
        for (int i = 0; i < length; i++) {
            Char ch = getCharFunc.apply(w, i);
            if (!temp.children.containsKey(ch)) {
                return false;
            }
            temp = temp.children.get(ch);
            if (path != null) {
                path.add(temp);
            }
        }
        return isPrefix || temp.isWordEnd;
    }

    public boolean startsWith(Word prefix) {
        return search(prefix, null, true);
    }

    public boolean delete(Word w) {
        List<TrieNode<Char, Word>> path = new ArrayList<>();
        boolean exist = search(w, path, false);

        //删除失败
        if (!exist) {
            return false;
        }
        //从尾部删除
        for (int i = path.size() - 2; i >= 0; i--) {
            TrieNode<Char, Word> parNode = path.get(i);
            //获取 childNode在parNode的key
            Char key = getCharFunc.apply(w, i);
            parNode.children.remove(key);
            if (parNode.isWordEnd || !parNode.children.isEmpty()) {
                break;
            }
        }
        return true;
    }
}
