package utl;

/**
 * @author jdy
 * @title: utl.CompareUtil
 * @description:
 * @data 2023/9/6 11:51
 */
public class CompareUtil {
    public static<T extends Comparable<T>> boolean le(T a, T b) {
        return a.compareTo(b)<=0;
    }
    public static<T extends Comparable<T>> boolean lt(T a, T b) {
        return a.compareTo(b)<0;
    }

    public static<T extends Comparable<T>> boolean eq(T a, T b) {
        return a.compareTo(b)==0;
    }

    public static<T extends Comparable<T>> boolean gt(T a, T b) {
        return a.compareTo(b)>0;
    }
    public static<T extends Comparable<T>> boolean ge(T a, T b) {
        return a.compareTo(b)>=0;
    }
}
