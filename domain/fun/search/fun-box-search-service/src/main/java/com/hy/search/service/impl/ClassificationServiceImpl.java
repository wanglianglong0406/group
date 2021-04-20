package com.hy.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.enums.CateGoryType;
import com.hy.pojo.ResponseJsonResult;
import com.hy.search.mapper.ClassificationMapper;
import com.hy.search.mapper.ClassificationMapperCustom;
import com.hy.search.model.Classification;
import com.hy.search.model.vo.CategoryVO;
import com.hy.search.service.IClassificationService;
import com.hy.utils.JsonUtils;
import com.hy.utils.RedisOperator;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页分类(Classification)表服务实现类
 *
 * @author 寒夜
 * @since 2020-11-18 13:38:12
 */
@RestController
@Slf4j
public class ClassificationServiceImpl implements IClassificationService {
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private ClassificationMapperCustom classificationMapperCustom;
    @Autowired
    private ClassificationMapper classificationMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Classification> queryALLRootLevelCate() {
        QueryWrapper<Classification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", CateGoryType.one.type);
        return classificationMapper.selectList(queryWrapper);
    }


    @Override
    public ResponseJsonResult root() {
        List<Classification> list;
        String cats_str = redisOperator.get("cats");
        if (StringUtils.isBlank(cats_str)) {
            list = queryALLRootLevelCate();
            redisOperator.set("cats", JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(cats_str, Classification.class);
        }
        return ResponseJsonResult.ok(list);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CategoryVO> getSubCatList(Integer rootId) {
        return classificationMapperCustom.getSubCatList(rootId);
    }


    @Override
    public ResponseJsonResult sub(@ApiParam(name = "rootId", value = "一级分类id", required = true) Integer rootId) {

        if (rootId == null) {
            return ResponseJsonResult.errorMsg("分类不存在");
        }

        List<CategoryVO> list;
        String subCat_str = redisOperator.get("subCat:" + rootId);
        if (StringUtils.isBlank(subCat_str)) {
            list = getSubCatList(rootId);
            redisOperator.set("subCat:" + rootId, JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(subCat_str, CategoryVO.class);
        }

        return ResponseJsonResult.ok(list);
    }
}