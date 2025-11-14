package segmenttree;

public class SegmentTree<T> {
    private Reducer<T> reducer;
    private T[] tree;
    private T[] data;

    public SegmentTree(T[] data, Reducer<T> reducer) {
        this.reducer = reducer;
        //创建树
        this.data = data;
        tree = (T[]) new Object[data.length * 4];
        buildTree(data, tree, 0, 0, data.length - 1);
    }

    private void buildTree(T[] data, T[] tree, int node, int start, int end) {
        //叶子节点
        if (start == end) {
            tree[node] = data[start];
            return;
        }
        int mid = (start + end) / 2;
        //递归的创建左右子树
        buildTree(data, tree, getLeftChild(node), start, mid);
        buildTree(data, tree, getRightChild(node), mid + 1, end);
        tree[node] = reducer.reduce(tree[getLeftChild(node)], tree[getRightChild(node)]);
    }

    private int getLeftChild(int node) {
        return node * 2 + 1;
    }

    private int getRightChild(int node) {
        return node * 2 + 2;
    }

    public void update(int target, T val) {
        data[target] = val;
        doUpdate(0, val, 0, data.length - 1, target);
    }

    /**
     * node = 0, 表示的是整个data数组区间
     *
     * @param node   树的节点
     * @param val    data数组应该修改的值
     * @param start  node 对应 data数组区间的开始
     * @param end    node 对应 data数组区段的结束
     * @param target data数组修改值的下标
     */

    private void doUpdate(int node, T val, int start, int end, int target) {
        if (start == end) {
            tree[node] = val;
            return;
        }
        int mid = (start + end) / 2;

        //二分查找到target
        if (target <= mid) {
            doUpdate(getLeftChild(node), val, start, mid, target);
        } else {
            doUpdate(getRightChild(node), val, mid + 1, end, target);
        }
        //更新各节点的值
        tree[node] = reducer.reduce(tree[getLeftChild(node)], tree[getRightChild(node)]);
    }

    public T getRoot() {
        return tree[0];
    }

    public T query(int left, int right) {
        return doQuery(0, 0, data.length - 1, left, right);
    }

    public T doQuery(int node, int start, int end, int left, int right) {
        // [start,end] 区间在  [left,right] 里面
        if (left <= start && right >= end) {
            return tree[node];
        }
        if (left > end || right < start) {
            return null;
        }
        int mid = (start + end) / 2;
        T leftRange = doQuery(getLeftChild(node), start, mid, left, right);
        T rightRange = doQuery(getRightChild(node), mid + 1, end, left, right);

        if (leftRange == null) {
            return rightRange;
        }
        if (rightRange == null) {
            return leftRange;
        }
        return reducer.reduce(leftRange, rightRange);
    }
}
