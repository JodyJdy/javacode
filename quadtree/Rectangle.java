package quadtree;

/**
 * 二维矩形
 */
public class Rectangle {
    /**
     * 矩形中心点
     */
    final Point center;
    /**
     * 矩形宽高的一半，  为了方便计算
     */
    final double halfWidth;
    final double halfHeight;

    public Rectangle(Point center, double halfWidth, double halfHeight) {
        this.center = center;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    /**
     * 矩形包含点p
     */
    public boolean contains(Point p) {
        return p.x >= center.x - halfWidth && p.x <= center.x + halfWidth &&
                p.y >= center.y - halfHeight && p.y <= center.y + halfHeight;
    }

    /**
     * 矩形是否相交
     */
    public boolean intersects(Rectangle r) {
        return !(center.x - halfWidth > r.center.x + r.halfWidth || center.x + halfWidth < r.center.x - r.halfWidth ||
                center.y - halfHeight > r.center.y + r.halfHeight || center.y + halfHeight < r.center.y - r.halfHeight);
    }
}
