package com.hy.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.enums.YesOrNo;
import com.hy.pojo.ResponseJsonResult;
import com.hy.search.mapper.CarouselMapper;
import com.hy.search.model.Carousel;
import com.hy.search.service.ICarouselService;
import com.hy.utils.JsonUtils;
import com.hy.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 轮播图(Carousel)表服务实现类
 *
 * @author 寒夜
 * @since 2020-11-18 13:38:30
 */
@RestController
public class CarouselServiceImpl implements ICarouselService {

    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private CarouselMapper carouselMapper;

    private List<Carousel> queryAll(Integer isShow) {
        QueryWrapper<Carousel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_show", isShow);
        return carouselMapper.selectList(queryWrapper);
    }

    @Override
    public ResponseJsonResult carousel() {
        List<Carousel> list;
        String carousel_str = redisOperator.get("carousel:list");
        if (StringUtils.isBlank(carousel_str)) {
            list = queryAll(YesOrNo.YES.type);
            redisOperator.set("carousel:list", JsonUtils.objectToJson(list));
        }else {
            list = JsonUtils.jsonToList(carousel_str,Carousel.class);
        }
        return ResponseJsonResult.ok(list);
    }
}