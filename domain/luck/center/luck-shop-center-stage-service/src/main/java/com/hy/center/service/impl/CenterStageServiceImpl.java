package com.hy.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.hy.center.mapper.*;
import com.hy.center.model.*;
import com.hy.center.model.bo.AnnouncementBO;
import com.hy.center.model.bo.FinancialRecordsBO;
import com.hy.center.model.bo.ItemsBiz;
import com.hy.center.model.bo.VipInfoBO;
import com.hy.center.model.vo.AnnouncementVO;
import com.hy.center.model.vo.FinancialEarningsVO;
import com.hy.center.model.vo.ItemsVO;
import com.hy.center.service.ICenterStageService;
import com.hy.pojo.PagedGridResult;
import com.hy.pojo.ResponseJsonResult;
import com.hy.utils.DateUtil;
import com.hy.utils.JsonUtils;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.hy.constant.Constant.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/30 15:14
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/30 15:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class CenterStageServiceImpl implements ICenterStageService {

    @Autowired
    private PriceInfoMapper priceInfoMapper;
    @Autowired
    private FinancialProductsMapper financialProductsMapper;
    @Autowired
    private FinancialRecordsMapper financialRecordsMapper;
    @Autowired
    private AnnouncementMapper announcementMapper;
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private VipInfoMapper vipInfoMapper;
    @Autowired
    private ItemsMapper itemsMapper;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addItemsInformation(ItemsBiz itemsBiz) {
        Items items = Items.builder().itemName(itemsBiz.getItemName())
                .content(itemsBiz.getContent())
                .onOffStatus(itemsBiz.getOnOffStatus())
                .price(itemsBiz.getPrice())
                .url(itemsBiz.getUrl())
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        itemsMapper.insert(items);
    }

    @Override
    public ResponseJsonResult getItemsInfoPagedGridResult(Integer pageNo, Integer pageSize, String startDate, String endDate) {
        QueryWrapper<Items> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (StringUtil.isNotBlank(startDate)) {
            queryWrapper.ge("date_format(create_time,'%Y-%m-%d')", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {
            queryWrapper.le("date_format(create_time,'%Y-%m-%d')", endDate);
        }
        if (pageNo == null) {
            pageNo = PAGE_NO;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        Page<Items> page = new Page<>(pageNo, pageSize, true);
        IPage<Items> iPage = itemsMapper.selectPage(page, queryWrapper);
        List<Items> itemsList = iPage.getRecords();

        List<ItemsVO> itemsVOList = new ArrayList<>();
        itemsList.forEach(items -> {
            ItemsVO itemsVO = ItemsVO.builder()
                    .content(items.getContent())
                    .itemName(items.getItemName())
                    .onOffStatus(items.getOnOffStatus())
                    .price(items.getPrice())
                    .url(items.getUrl())
                    .createTime(DateUtil.dateToStringWithTime(items.getCreateTime()))
                    .id(String.valueOf(items.getId()))
                    .build();
            itemsVOList.add(itemsVO);
        });
        return ResponseJsonResult.ok(PagedGridResult.builder().pageNo(pageNo).rows(itemsVOList).pages(iPage.getPages()).total(iPage.getTotal()).build());

    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsBiz> getItemsInfoList() {
        QueryWrapper<Items> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<Items> itemsList;
        List<ItemsBiz> itemsBizList = new ArrayList<>();
        itemsList = itemsMapper.selectList(queryWrapper);

        for (Items items : itemsList) {
            ItemsBiz itemsBiz = ItemsBiz.builder()
                    .id(items.getId())
                    .itemName(items.getItemName())
                    .content(items.getContent())
                    .onOffStatus(items.getOnOffStatus())
                    .price(items.getPrice())
                    .url(items.getUrl())
                    .build();
            itemsBizList.add(itemsBiz);
        }
        return itemsBizList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void editItemsInformationByItemsId(ItemsBiz itemsBiz) {
        new LambdaUpdateChainWrapper<>(itemsMapper)
                .eq(Items::getId, itemsBiz.getId())
                .set(Items::getContent, itemsBiz.getContent())
                .set(Items::getItemName, itemsBiz.getItemName())
                .set(Items::getOnOffStatus, itemsBiz.getOnOffStatus())
                .set(Items::getPrice, itemsBiz.getPrice())
                .set(Items::getUrl, itemsBiz.getUrl())
                .set(Items::getUpdateTime, new Date())
                .update();
    }

    @Override
    public Items getItemsInfoByItemsId(Long itemsId) {
        QueryWrapper<Items> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", itemsId);
        return itemsMapper.selectOne(queryWrapper);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsBiz> getItemsList(String userId, Integer numberOfTasks) {

        QueryWrapper<Items> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("on_off_status", 1)
                .last("order by rand() limit " + numberOfTasks);

        String itemsListRedisKey = ITEMS_LIST + ":" + userId;
        String itemsListRedisStr = redisOperator.get(itemsListRedisKey);

        List<Items> itemsList;
        List<ItemsBiz> itemsBizList = new ArrayList<>();
        if (itemsListRedisStr == null) {
            itemsList = itemsMapper.selectList(queryWrapper);
            for (Items items : itemsList) {
                ItemsBiz itemsBiz = ItemsBiz.builder()
                        .id(items.getId())
                        .itemName(items.getItemName())
                        .content(items.getContent())
                        .onOffStatus(items.getOnOffStatus())
                        .price(items.getPrice())
                        .url(items.getUrl())
                        .build();
                itemsBizList.add(itemsBiz);
            }
            redisOperator.set(itemsListRedisKey, JsonUtils.objectToJson(itemsBizList));
        } else {
            itemsBizList = JsonUtils.jsonToList(itemsListRedisStr, ItemsBiz.class);
        }


        return itemsBizList;
    }


    @Override
    public ResponseJsonResult queryVipInfoList(String userId) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        return ResponseJsonResult.ok(getVipInfos(userId));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<VipInfo> getVipInfos(String userId) {
        QueryWrapper<VipInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return vipInfoMapper.selectList(queryWrapper);
    }

    @Override
    public void saveVipInfo(VipInfoBO vipInfoBO) {
        List<VipInfo> vipInfos = getVipInfos(null);
        Integer id;
        if (vipInfos != null) {
            id = vipInfos.get(0).getId() + 1;
        } else {
            id = 10001;
        }
        VipInfo vipInfo = VipInfo.builder()
                .id(id)
                .assets(vipInfoBO.getAssets())
                .commissionRate(vipInfoBO.getCommissionRate())
                .monthlyWithdrawalLimit(vipInfoBO.getMonthlyWithdrawalLimit())
                .name(vipInfoBO.getName())
                .numberOfDailyWithdrawals(vipInfoBO.getNumberOfDailyWithdrawals())
                .numberOfTasks(vipInfoBO.getNumberOfTasks())
                .vipLevel(vipInfoBO.getVipLevel())
                .build();
        vipInfoMapper.insert(vipInfo);
    }

    @Override
    public void editVipInfo(VipInfoBO vipInfoBO) {
        VipInfo vipInfo = getVipInfosByVipLevel(vipInfoBO.getVipLevel());
        new LambdaUpdateChainWrapper<>(vipInfoMapper)
                .eq(VipInfo::getVipLevel, vipInfoBO.getVipLevel())
                .set(VipInfo::getAssets, vipInfoBO.getAssets() != null ? vipInfoBO.getAssets() : vipInfo.getAssets())
                .set(VipInfo::getCommissionRate, vipInfoBO.getCommissionRate() != null ? vipInfoBO.getCommissionRate() : vipInfo.getCommissionRate())
                .set(VipInfo::getMonthlyWithdrawalLimit, vipInfoBO.getMonthlyWithdrawalLimit() != null ? vipInfoBO.getMonthlyWithdrawalLimit() : vipInfo.getMonthlyWithdrawalLimit())
                .set(VipInfo::getNumberOfTasks, vipInfoBO.getNumberOfTasks() != null ? vipInfoBO.getNumberOfTasks() : vipInfo.getNumberOfTasks())
                .update();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public VipInfo getVipInfosByVipLevel(Integer vipLevel) {
        QueryWrapper<VipInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vip_level", vipLevel);
        return vipInfoMapper.selectOne(queryWrapper);
    }

    //获取价格列表
    @Override
    public List<PriceInfo> getPriceInfoList() {
        QueryWrapper<PriceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return priceInfoMapper.selectList(queryWrapper);
    }

    @Override
    public void savePriceInfo(Double price) {
        List<PriceInfo> priceInfos = getPriceInfoList();
        Integer id;
        if (priceInfos != null) {
            id = priceInfos.get(0).getId() + 1;
        } else {
            id = 1001;
        }

        PriceInfo priceInfo = PriceInfo.builder()
                .id(id)
                .price(price)
                .build();
        priceInfoMapper.insert(priceInfo);
    }

    @Override
    public void editPriceInfo(Integer id, Double price) {
        PriceInfo priceInfo = PriceInfo.builder()
                .id(id)
                .price(price)
                .build();
        priceInfoMapper.updateById(priceInfo);
    }

    //查询价格信息
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryPriceInfoList(String userId) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        return ResponseJsonResult.ok(getPriceInfoList());
    }

    private ResponseJsonResult checkParams(String userId) {
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("The userId can not be empty");
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE, "Login expired, please login again !", "");
        }
        return null;
    }


    @Override
    public List<FinancialProducts> getFinancialProductsList() {
        QueryWrapper<FinancialProducts> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return financialProductsMapper.selectList(queryWrapper);
    }

    @Override
    public void saveFinancialProductsInfo(Integer cycle, Double interest, Double totalRevenue) {

        List<FinancialProducts> financialProductsList = getFinancialProductsList();
        Integer id;
        if (financialProductsList != null) {
            id = financialProductsList.get(0).getId() + 1;
        } else {
            id = 10001;
        }

        FinancialProducts financialProducts = FinancialProducts.builder()
                .id(id)
                .cycle(cycle)
                .interest(interest)
                .totalRevenue(totalRevenue)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        financialProductsMapper.insert(financialProducts);
    }

    @Override
    public void editFinancialProductsInfo(Integer id, Integer cycle, Double interest, Double totalRevenue) {

        FinancialProducts financialProducts = financialProductsMapper.selectById(id);

        new LambdaUpdateChainWrapper<>(financialProductsMapper)
                .eq(FinancialProducts::getId, id)
                .set(FinancialProducts::getCycle, cycle != null ? cycle : financialProducts.getCycle())
                .set(FinancialProducts::getInterest, interest != null ? interest : financialProducts.getInterest())
                .set(FinancialProducts::getTotalRevenue, totalRevenue != null ? totalRevenue : financialProducts.getTotalRevenue())
                .set(FinancialProducts::getUpdateTime, new Date())
                .update();
    }

    //查询产品列表
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryFinancialProductsList(String userId) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        return ResponseJsonResult.ok(getFinancialProductsList());
    }

    //通过产品ID查询产品信息
    @Transactional(propagation = Propagation.SUPPORTS)
    public FinancialProducts getFinancialProductsByProductId(Integer productId) {
        return financialProductsMapper.selectById(productId);
    }

    //查询用户购买的理财产品
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<FinancialRecords> getFinancialRecordsList(String userId) {
        QueryWrapper<FinancialRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).in("status", Arrays.asList(1, 2));
        return financialRecordsMapper.selectList(queryWrapper);
    }

    @Override
    public ResponseJsonResult myFinancialRecordsList(String userId) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        return ResponseJsonResult.ok(getFinancialRecordsList(userId));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public FinancialRecords getFinancialRecords(String userId, Integer productId) {
        QueryWrapper<FinancialRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("product_id", productId).in("status", Arrays.asList(1, 2));
        return financialRecordsMapper.selectOne(queryWrapper);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public FinancialRecords createFinancialRecordsInfo(FinancialRecordsBO financialRecordsBO) {

        Double amount = financialRecordsBO.getAmount();
        FinancialProducts financialProducts = getFinancialProductsByProductId(financialRecordsBO.getProductId());

        FinancialRecords financialRecords = FinancialRecords.builder()
                .productId(financialProducts.getId())
                .orderId(financialRecordsBO.getOrderId())
                .cycle(financialProducts.getCycle())
                .interest(financialProducts.getInterest())
                .totalRevenue(financialProducts.getTotalRevenue())
                .userId(financialRecordsBO.getUserId())
                .nicknames(financialRecordsBO.getNicknames())
                .amount(financialRecordsBO.getAmount())
                .expectedEarning(amount + amount * financialProducts.getTotalRevenue())
                .createTime(new Date())
                .updateTime(new Date())
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(financialProducts.getCycle() + 1))
                .finalSettlementTime(new Date())
                .status(1)
                .build();
        financialRecordsMapper.insert(financialRecords);
        return financialRecords;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult updateFinancialRecordsInfo(String userId, Integer productId, Double amount) {
        FinancialRecords financialRecords = getFinancialRecords(userId, productId);

        int status = 0;
        if (financialRecords.getAmount().equals(amount)) {
            status = 3;
        }
        if (financialRecords.getAmount() > amount) {
            status = 2;
        }
        if (financialRecords.getAmount() < amount) {
            return ResponseJsonResult.errorMsg("Wrong amount entered");
        }
        //计算实际收益
        Double actualEarningsAmount = financialRecords.getAmount() * financialRecords.getInterest() * financialRecords.getDays();

        new LambdaUpdateChainWrapper<>(financialRecordsMapper)
                .eq(FinancialRecords::getUserId, userId)
                .eq(FinancialRecords::getProductId, productId)
                .set(FinancialRecords::getActualEarnings, actualEarningsAmount)
                .set(FinancialRecords::getStatus, status)
                .set(FinancialRecords::getUpdateTime, new Date())
                .set(FinancialRecords::getFinalSettlementTime, new Date())
                .update();

        return ResponseJsonResult.ok();
    }

    //计算日息
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public FinancialRecords calculateDailyInterest(String userId, Integer productId) {
        FinancialRecords financialRecords = getFinancialRecords(userId, productId);
        //计算日息
        double dailyInterest;
        //实际收益
        double actualEarningsAmount;
        LocalDate now = LocalDate.now();
        if (now.isBefore(financialRecords.getStartDate())) {
            dailyInterest = 0.00;
            actualEarningsAmount = 0.00;
        } else {
            dailyInterest = financialRecords.getAmount() * financialRecords.getInterest();
            actualEarningsAmount = financialRecords.getAmount() * financialRecords.getInterest() * financialRecords.getDays();
        }

        new LambdaUpdateChainWrapper<>(financialRecordsMapper)
                .eq(FinancialRecords::getUserId, userId)
                .eq(FinancialRecords::getProductId, productId)
                .set(FinancialRecords::getDays, financialRecords.getDays() + 1)
                .set(FinancialRecords::getDailyInterest, dailyInterest)
                .set(FinancialRecords::getActualEarnings, actualEarningsAmount)
                .set(FinancialRecords::getUpdateTime, new Date())
                .set(FinancialRecords::getFinalSettlementTime, new Date())
                .update();
        financialRecords = getFinancialRecords(userId, productId);
        return financialRecords;
    }

    @Override
    public ResponseJsonResult totalFinancialEarningsInfo(String userId) {

        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;

        List<FinancialRecords> financialRecordsList = getFinancialRecordsList(userId);
        double hasEarnedInterest = 0.00;
        double purchaseAmount = 0.00;
        double expectedEarningInterest = 0.00;

        for (FinancialRecords financialRecords : financialRecordsList) {
            hasEarnedInterest += financialRecords.getActualEarnings();
            purchaseAmount += financialRecords.getAmount();
            expectedEarningInterest += financialRecords.getExpectedEarning();
        }

        //总资产= 购买金额+已经获取的利息
        double totalAssets = purchaseAmount + hasEarnedInterest;

        FinancialEarningsVO financialEarningsVO = FinancialEarningsVO.builder()
                .hasEarnedInterest(hasEarnedInterest)
                .purchaseAmount(purchaseAmount)
                .expectedEarningInterest(expectedEarningInterest)
                .totalAssets(totalAssets)
                .build();
        return ResponseJsonResult.ok(financialEarningsVO);
    }


    //获取公告列表
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Announcement> getAnnouncementInfoList() {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<AnnouncementVO> announcementVOList = new ArrayList<>();
        List<Announcement> announcementList = announcementMapper.selectList(queryWrapper);

        return announcementList;
    }

    @Override
    public Announcement getAnnouncementInfoById(String id) {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return announcementMapper.selectOne(queryWrapper);
    }

    //查询公告列表信息
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryAnnouncementInfoList() {
        return ResponseJsonResult.ok(getAnnouncementInfoList());
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addAnnouncementInfo(AnnouncementBO announcementBO) {
        Announcement announcement = Announcement.builder()
                .title(announcementBO.getTitle())
                .content(announcementBO.getContent())
                .popUp(announcementBO.getPopUp())
                .status(announcementBO.getStatus())
                .type(announcementBO.getType())
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        announcementMapper.insert(announcement);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void editAnnouncementInfo(AnnouncementBO announcementBO) {
        Announcement announcement = Announcement.builder()
                .id(announcementBO.getId())
                .title(announcementBO.getTitle())
                .content(announcementBO.getContent())
                .popUp(announcementBO.getPopUp() != null ? announcementBO.getPopUp() : 1)
                .status(announcementBO.getStatus() != null ? announcementBO.getStatus() : 1)
                .type(announcementBO.getType())
                .updateTime(new Date())
                .build();
        announcementMapper.updateById(announcement);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteAnnouncementInfo(String id) {
        Announcement announcement = Announcement.builder()
                .id(id)
                .build();
        announcementMapper.deleteById(announcement);
    }
}
