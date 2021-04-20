package com.hy.search.mapper;

import com.hy.search.model.vo.CategoryVO;

import java.util.List;

/**
 * @Description: $- ClassificationMapperCustom -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/18 14:17
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/18 14:17
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public interface ClassificationMapperCustom {

    public List<CategoryVO> getSubCatList(Integer rootId);
}
