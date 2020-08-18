package cn.unclejoke.spider.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("t_spider_brand")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Brand {

    @TableId(type = IdType.AUTO)
    private String id;

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
     * 排序
     */
    @TableField("brand_sort")
    private Integer brandSort;

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
