package cn.unclejoke.spider.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("t_spider_picture")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Picture {

    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 图片地址
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 产品ID
     */
    @TableField("product_id")
    private String productId;

    /**
     * 产品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

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
