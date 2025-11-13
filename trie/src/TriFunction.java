/**
 *用于从序列中获取第i个元素
 * @param <Array> 数组
 * @param <Index> 数组下标
 * @param <Value>  数组值
 */
@FunctionalInterface
public interface TriFunction<Array, Index, Value> {
    Value apply(Array a, Index i);
}
