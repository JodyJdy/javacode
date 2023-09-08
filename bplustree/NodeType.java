/**
 * @author jdy
 * @title: NodeType
 * @description:
 * @data 2023/9/6 10:30
 */
public enum NodeType {

    ROOT_LEAF("根节点 & 叶子节点",0),
    LEAF("叶子节点",1),
    INDEX("索引节点", 2),
    ROOT_INDEX("根节点 & 索引节点", 3),

    ;

    NodeType(String type, int value) {
    }
}
