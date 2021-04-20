package com.hy.enums;

/**
 * @desc 是否 枚举
 */
public enum CateGoryType {

    one(1, "一级分类"),
    two(2, "二级分类"),
    three(3, "三级分类");



    public final Integer type;
    public final String value;

    CateGoryType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
