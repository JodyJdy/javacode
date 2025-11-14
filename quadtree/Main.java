package quadtree;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        QuadTree quadTree = new QuadTree(new Rectangle(new Point(0, 0), 100, 100), 4);

        quadTree.insert(new Point(10, 10));
        quadTree.insert(new Point(20, 20));
        quadTree.insert(new Point(30, 30));
        quadTree.insert(new Point(-140, -40));
        quadTree.insert(new Point(-40, -40));

        List<Point> query = quadTree.query(new Rectangle(new Point(0, 0), 50, 50));
        System.out.println(query);

    }
}
