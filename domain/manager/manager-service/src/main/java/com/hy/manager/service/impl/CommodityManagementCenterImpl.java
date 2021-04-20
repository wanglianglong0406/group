package com.hy.manager.service.impl;

import com.hy.center.model.Items;
import com.hy.center.model.bo.ItemsBiz;
import com.hy.center.service.ICenterStageService;
import com.hy.manager.model.bo.ItemsBO;
import com.hy.manager.service.ICommodityManagementCenterService;
import com.hy.manager.util.CSV;
import com.hy.manager.util.ReadCsvUtil;
import com.hy.pojo.ResponseJsonResult;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.hy.constant.Constant.LOGIN_EXPIRED_CODE;
import static com.hy.constant.Constant.REDIS_USER_TOKEN;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/30 14:49
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/30 14:49
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class CommodityManagementCenterImpl implements ICommodityManagementCenterService {

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private ICenterStageService iCenterStageService;

    //查询商品信息
    @Override
    public ResponseJsonResult queryItemsInfoList(String userId,Integer pageNo, Integer pageSize, String startDate, String endDate) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        return ResponseJsonResult.ok(iCenterStageService.getItemsInfoPagedGridResult(pageNo, pageSize, startDate, endDate));
    }

    //编辑商品信息
    @Override
    public ResponseJsonResult editItemsInformation(ItemsBO itemsBO, String userId) {

        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;

        Long itemsId = itemsBO.getId();
        Items items = iCenterStageService.getItemsInfoByItemsId(itemsId);
        if(items==null){
            return ResponseJsonResult.errorMsg("This ID does not exist !");
        }
        assert items != null;

        ItemsBiz itemsBiz = ItemsBiz.builder()
                .id(itemsBO.getId())
                .content(itemsBO.getContent() != null ? itemsBO.getContent() : items.getContent())
                .itemName(itemsBO.getItemName() != null ? itemsBO.getItemName() : items.getItemName())
                .price(itemsBO.getPrice() != null ? itemsBO.getPrice() : items.getPrice())
                .onOffStatus(itemsBO.getOnOffStatus() != null ? itemsBO.getOnOffStatus() : items.getOnOffStatus())
                .url(itemsBO.getUrl() != null ? itemsBO.getUrl() : items.getUrl())
                .build();
        iCenterStageService.editItemsInformationByItemsId(itemsBiz);
        return ResponseJsonResult.ok();
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


    //导入商品信息
    @Override
    public ResponseJsonResult importItemsCSV(String userId,MultipartFile file) throws IOException {

        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;

        byte[] bate = file.getBytes();
        List<String> dataList = ReadCsvUtil.getResource(bate);

        try {
            if (!dataList.isEmpty()) {
                dataList.remove(0);
                for (int i = 0; i < dataList.size(); i++) {
                    String data = dataList.get(i);
                    String[] source = data.split(",");

                    if(StringUtil.isEmpty(source[0]) || StringUtil.isEmpty(source[1]) || StringUtil.isEmpty(source[2]) || StringUtil.isEmpty(source[3])){
                        return ResponseJsonResult.errorMsg("Template parameter column cannot be empty !");
                    }
                    if (source[0].length() > 128) {
                        return ResponseJsonResult.errorMsg("Product name cannot be larger than 128 bytes !");
                    }
                    if (source[1].length() > 512) {
                        return ResponseJsonResult.errorMsg("Item description cannot be greater than 512 bytes !");
                    }
                    if (!isNumeric(source[2])) {
                        return ResponseJsonResult.errorMsg("Price must be a positive integer or double decimal !");
                    }
                    if (source[3].length() > 128) {
                        return ResponseJsonResult.errorMsg("Product image link cannot be larger than 128 bytes !");
                    }
                    ItemsBiz itemsBiz = ItemsBiz.builder()
                            .itemName(source[0])
                            .content(source[1])
                            .price(Double.parseDouble(source[2]))
                            .onOffStatus(1)
                            .url(source[3])
                            .build();
                    System.out.println(itemsBiz.toString());
                    iCenterStageService.addItemsInformation(itemsBiz);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseJsonResult.errorMsg("Failed to import product information !");
        }
        return ResponseJsonResult.build(200, "Import product information successfully !", "");
    }

    public final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0.00-9.00]*$");
        else
            return false;
    }

    //商品模板下载
    @Override
    public ResponseJsonResult downloadedItemsCSVTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {

        CSV.exportCsv(response, request);

        return ResponseJsonResult.build(200, "Product information import template downloaded successfully !", "");
    }


    public static void main(String[] args) throws IOException {

        HttpServletResponse response = null;
        HttpServletRequest request = null;
        CommodityManagementCenterImpl commodityManagementCenter = new CommodityManagementCenterImpl();
        //commodityManagementCenter.importItemsCSV(request, (MultipartFile) new File("D:/download.csv"));

        boolean a = isNumeric("300.01");
        System.err.println(a);
    }

}
