package cn.unclejoke.spider.service;

import cn.unclejoke.spider.mapper.BrandMapper;
import cn.unclejoke.spider.mapper.PictureMapper;
import cn.unclejoke.spider.mapper.ProductMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.unclejoke.spider.model.Brand;
import cn.unclejoke.spider.model.Picture;
import cn.unclejoke.spider.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 持久化数据服务类
 *
 * @author V
 * @date 2020-8-18 09:23:21
 */
@Service("persistentService")
public class PersistentService {

    private static final Logger log = LoggerFactory.getLogger(PersistentService.class);

    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PictureMapper pictureMapper;

    public void saveOrUpdateBrand(Brand brand){
        Wrapper<Brand> queryWrapper = Wrappers.<Brand>lambdaQuery().eq(Brand::getBrandId, brand.getBrandId());
        List<Brand> selectList = brandMapper.selectList(queryWrapper);
        if (!selectList.isEmpty()) {
            brand.setUpdateTime(new Date());
            Wrapper<Brand> updateWrapper = Wrappers.<Brand>lambdaUpdate().eq(Brand::getBrandId, brand.getBrandId());
            brandMapper.update(brand, updateWrapper);
            log.debug("更新: {}", brand.toString());
        } else {
            brand.setCreateTime(new Date());
            brandMapper.insert(brand);
            log.debug("新增: {}", brand.toString());
        }
    }

    public void saveOrUpdateProduct(Product product){
        Wrapper<Product> queryWrapper = Wrappers.<Product>lambdaQuery().eq(Product::getCgtId, product.getCgtId());
        List<Product> selectList = productMapper.selectList(queryWrapper);
        if (!selectList.isEmpty()) {
            product.setUpdateTime(new Date());
            Wrapper<Product> updateWrapper = Wrappers.<Product>lambdaUpdate().eq(Product::getCgtId, product.getCgtId());
            productMapper.update(product, updateWrapper);
            log.debug("更新: {}", product.toString());
        } else {
            product.setCreateTime(new Date());
            productMapper.insert(product);
            log.debug("新增: {}", product.toString());
        }
    }

    public void saveOrUpdatePicture(Picture picture){
        Wrapper<Picture> queryWrapper = Wrappers.<Picture>lambdaQuery().eq(Picture::getImgUrl, picture.getImgUrl());
        List<Picture> selectList = pictureMapper.selectList(queryWrapper);
        if (!selectList.isEmpty()) {
            picture.setUpdateTime(new Date());
            Wrapper<Picture> updateWrapper = Wrappers.<Picture>lambdaUpdate().eq(Picture::getImgUrl, picture.getImgUrl());
            pictureMapper.update(picture, updateWrapper);
            log.debug("更新: {}", picture.toString());
        } else {
            picture.setCreateTime(new Date());
            pictureMapper.insert(picture);
            log.debug("新增: {}", picture.toString());
        }
    }

}
