package com.hy.search.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 二级分类VO
 *
 * @author 寒夜
 * @since 2020-11-17 21:30:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategoryVO {

    private Integer subId;
    private String subName;
    private String subType;
    private Integer subFatherId;


}
