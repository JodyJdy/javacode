package segmenttree;

public class Main {
    public static void main(String[] args) {
        SegmentTree<Integer> segmentTree = new SegmentTree<>(new Integer[]{1,2,3,4,5},Reducer.INT_SUM);
        System.out.println(segmentTree.getRoot());
        segmentTree.update(1,5);
        System.out.println(segmentTree.getRoot());
        System.out.println(segmentTree.query(2,4));
    }

}