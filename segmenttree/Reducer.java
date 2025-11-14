package segmenttree;

/**
 */
@FunctionalInterface
public interface Reducer<T> {
    T reduce(T a,T b);
    /**
     * 常用reduce实现
     */
    Reducer<Integer> INT_SUM = Integer::sum;
    Reducer<Integer> INT_MAX = Integer::max;
    Reducer<Integer> INT_MIN = Integer::min;
    Reducer<Integer> INT_MUL = (a,b)->a*b;
    Reducer<Long> LONG_SUM = Long::sum;
    Reducer<Long> LONG_MAX = Long::max;
    Reducer<Long> LONG_MIN = Long::min;
    Reducer<Long> LONG_MUL = (a,b)->a*b;
    Reducer<Double> DOUBLE_SUM = Double::sum;
    Reducer<Double> DOUBLE_MAX = Double::max;
    Reducer<Double> DOUBLE_MIN = Double::min;
    Reducer<Double> DOUBLE_MUL = (a,b)->a*b;


}
