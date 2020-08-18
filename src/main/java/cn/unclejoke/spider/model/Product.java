package cn.unclejoke.spider.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("t_spider_product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 香烟ID
     */
    @TableField("cgt_id")
    private String cgtId;

    /**
     * 香烟名称
     */
    @TableField("cgt_name")
    private String cgtName;

    /**
     * 品牌ID
     */
    @TableField("brand_id")
    private String brandId;

    /**
     * 品牌名称
     */
    @TableField("brand_name")
    private String brandName;

    /**
     * 卷烟盒码
     */
    @TableField("cgt_min_code")
    private String cgtMinCode;

    /**
     * 卷烟条码
     */
    @TableField("tob_bar_code")
    private String tobBarCode;

    /**
     * 烟支长度（mm）
     */
    @TableField("cgt_length")
    private String cgtLength;

    /**
     * 焦油量（mg）
     */
    @TableField("cgt_tarcontent")
    private String cgtTarcontent;

    /**
     * 烟气烟碱量（mg）
     */
    @TableField("cgt_nicotinic")
    private String cgtNicotinic;

    /**
     * 烟气一氧化碳量（mg）
     */
    @TableField("cgt_co")
    private String cgtCo;

    /**
     * 上市时间
     */
    @TableField("cgt_marketdate")
    private String cgtMarketdate;

    /**
     * 条批价
     */
    @TableField("whole_sale_prices")
    private String wholeSalePrices;

    /**
     * 零售价（条）
     */
    @TableField("retail_price")
    private String retailPrice;

    /**
     * 零售价（包）
     */
    @TableField("retail_price_pack")
    private String retailPricePack;

    /**
     * 产品类型
     */
    @TableField("cgt_type_name")
    private String cgtTypeName;

    /**
     * 粗细类型
     */
    @TableField("show_name")
    private String showName;

    /**
     * 包装形式名称
     */
    @TableField("cgt_packtype_name")
    private String cgtPacktypeName;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

}
