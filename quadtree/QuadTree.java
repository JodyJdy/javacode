package quadtree;

import java.util.ArrayList;
import java.util.List;

/**
 * (x,y)是四叉树的中心点位置
 * <p>
 * ┌─────────────┐
 * │ NW    | NE  │
 * │       |  *  │
 * ├───  (x,y)───┤>
 * │ SW    | SE  │
 * │  o    |     │
 * └─────────────┘
 */
public class QuadTree {

    // 阈值，超过则分裂
    private final int CAPACITY;
    private final Rectangle boundary;
    private List<Point> points = new ArrayList<>();
    /**
     * 是否已经分裂
     */
    private boolean divided = false;

    private QuadTree nw;
    private QuadTree ne;
    private QuadTree sw;
    private QuadTree se;

    public QuadTree(Rectangle boundary, int capacity) {
        this.boundary = boundary;
        this.CAPACITY = capacity;
    }

    public boolean insert(Point p) {
        /**
         *不在矩形边界内
         */
        if (!boundary.contains(p)) {
            return false;
        }
        //已经插入过
        if (points.contains(p)) {
            return true;
        }

        if (points.size() < CAPACITY) {
            points.add(p);
            return true;
        }
        //进行分裂
        if (!divided) {
            doDivide();
        }
        //尝试插入到子节点
        return nw.insert(p) || ne.insert(p) || sw.insert(p) || se.insert(p);
    }

    public void doDivide() {
        double x = boundary.center.x;
        double y = boundary.center.y;
        double hw = boundary.halfWidth / 2;
        double hh = boundary.halfHeight / 2;

        //计算四个象限的边界
        Rectangle nwRect = new Rectangle(new Point(x - hw, y + hh), hw, hh);
        Rectangle neRect = new Rectangle(new Point(x + hw, y + hh), hw, hh);
        Rectangle swRect = new Rectangle(new Point(x - hw, y - hh), hw, hh);
        Rectangle seRect = new Rectangle(new Point(x + hw, y - hh), hw, hh);

        nw = new QuadTree(nwRect, CAPACITY);
        ne = new QuadTree(neRect, CAPACITY);
        sw = new QuadTree(swRect, CAPACITY);
        se = new QuadTree(seRect, CAPACITY);
        divided = true;
    }


    private void query(Rectangle range, List<Point> found) {
        if (!boundary.intersects(range)) return;

        for (Point p : points) {
            if (range.contains(p)) {
                found.add(p);
            }
        }

        if (divided) {
            nw.query(range, found);
            ne.query(range, found);
            sw.query(range, found);
            se.query(range, found);
        }
    }

    // 查询方法（直接返回结果）
    public List<Point> query(Rectangle range) {
        List<Point> found = new ArrayList<>();
        query(range, found);
        return found;
    }


}
