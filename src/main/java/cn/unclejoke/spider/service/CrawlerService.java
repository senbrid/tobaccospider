package cn.unclejoke.spider.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import cn.unclejoke.spider.model.Brand;
import cn.unclejoke.spider.model.Picture;
import cn.unclejoke.spider.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service("crawlerService")
public class CrawlerService {

    private static final Logger log = LoggerFactory.getLogger(CrawlerService.class);

    private static final String BASE = "http://www.etmoc.com";

    private static final String BASE_URL = BASE + "/Firms/";

    private static final String PATTERN = "[^(0-9.)]";

    @Autowired
    private PersistentService persistentService;

    /**
     * 爬虫启动方法
     */
    public void run() {
        Map<String, String> allBrandHref = this.retrieveAllBrand();
        for (Map.Entry<String, String> brand : allBrandHref.entrySet()) {
            String brandHref = brand.getKey();
            Map<String, String> productListByBrand = this.retrieveProductListByBrand(brandHref);
            for (Map.Entry<String, String> productHref : productListByBrand.entrySet()) {
                this.retrieveProductDetail(productHref.getKey(), productHref.getValue(), brandHref.split("=")[1],
                        brand.getValue());
            }
        }
    }

    /**
     * 检索香烟品牌(大陆品牌、港澳台品牌)
     *
     * @return
     */
    private Map<String, String> retrieveAllBrand() {
        Map<String, String> result = new HashMap<>();
        try {
            Document document = Jsoup.connect(BASE_URL + "BrandAll").get();
            Elements elements = document.getElementsByClass("left-header");
            int index = 1;
            for (Element element : elements) {
                Element h4 = element.child(0);
                if ("大陆品牌".equals(h4.ownText()) || "港澳台品牌".equals(h4.ownText())) {
                    Element next = element.nextElementSibling();
                    Elements links = next.getElementsByTag("a");
                    for (Element link : links) {
                        String linkHref = link.attr("href");
                        String linkText = link.ownText();
                        result.put(linkHref, linkText);
                        String brandId = linkHref.split("=")[1];
                        Brand brand = Brand.builder().brandId(brandId).brandName(linkText)
                                .brandSort(index++).build();
                        log.info(brand.toString());
                        persistentService.saveOrUpdateBrand(brand);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取品牌下所有产品
     *
     * @param href 短地址
     * @return
     */
    private Map<String, String> retrieveProductListByBrand(String href) {
        Map<String, String> result = new HashMap<>();
        try {
            // 通过url直接抓取网页的全部源码
            Document document = Jsoup.connect(BASE_URL + href).get();
            Elements elements = document.getElementsByClass("li-p");
            for (Element element : elements) {
                if (element == null) {
                    continue;
                }
                String cigaretteUrl = element.getElementsByClass("li-p-i").select("a").attr("href");
                String cigaretteName = element.getElementsByClass("li-p-t").select("a").text();
                result.put(cigaretteUrl, cigaretteName);
            }
            // 如果地址中带有分页参数，则说明一页，不是第只有在第一页的时候获取所有分页
            if (!href.contains("page")) {
                Elements paginationElements = document.getElementsByClass("pagination");
                if (!paginationElements.isEmpty()) {
                    Elements links = paginationElements.first().getElementsByTag("a");
                    if (!links.isEmpty()) {
                        links.remove(links.size() - 1);
                        for (Element link : links) {
                            String pageNum = link.text();
                            result.putAll(this.retrieveProductListByBrand(href + "&page=" + pageNum));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//    /**
//     * 获取产品详细信息
//     *
//     * @param href 短地址
//     * @return
//     */
//    private void retrieveProductDetailBak(String href, String productName, String brandId, String brandName) {
//        try {
//            String productId = href.split("=")[1];
//            // 通过url直接抓取网页的全部源码
//            Document document = Jsoup.connect(BASE_URL + href).get();
//            String picHref = document.getElementsByClass("proImg proBarshad").select("a").attr("href");
//            if (StringUtils.isNotBlank(picHref)) {
//                this.retrieveProductPicture(picHref, productId, productName);
//            }
//            Elements elements = document.getElementsByClass("proBars");
//            for (Element element : elements) {
//                if (element == null) {
//                    continue;
//                }
//                Elements divs = element.getElementsByTag("div");
//                for (Element div : divs) {
//                    if (div.hasClass("proBar")) {
//                        Elements childDivs = div.select("div");
//                        Map<String, Object> tempProduct = new HashMap<>();
//                        for (Element childDiv : childDivs) {
//                            String spanText = childDiv.select("span").first().ownText();
//                            if (spanText.contains("产品类型")) {
//                                tempProduct.put("cgtTypeName", childDiv.ownText());
//                            } else if (spanText.contains("焦油量")) {
//                                String s = childDiv.ownText();
//                                s = s.replaceAll(PATTERN, "");
//                                tempProduct.put("cgtTarcontent", BigDecimal.valueOf(Double.parseDouble(s)));
//                            } else if (spanText.contains("烟碱量")) {
//                                String s = childDiv.ownText();
//                                s = s.replaceAll(PATTERN, "");
//                                tempProduct.put("cgtNicotinic", BigDecimal.valueOf(Double.parseDouble(s)));
//                            } else if (spanText.contains("一氧化碳量")) {
//                                String s = childDiv.ownText();
//                                s = s.replaceAll(PATTERN, "");
//                                tempProduct.put("cgtCo", BigDecimal.valueOf(Double.parseDouble(s)));
//                            } else if (spanText.contains("包装形式")) {
//                                tempProduct.put("cgtPacktypeName", childDiv.ownText());
//                            } else if (spanText.contains("烟支规格")) {
//                                String length = childDiv.ownText();
//                                length = length.replaceAll(PATTERN, "");
//                                tempProduct.put("cgtLength", BigDecimal.valueOf(Double.parseDouble(length)));
//                                if (childDiv.select("span").size() > 1) {
//                                    tempProduct.put("showName", childDiv.select("span").get(1).ownText());
//                                }
//                            } else if (spanText.contains("小盒条码")) {
//                                tempProduct.put("cgtMinCode", childDiv.ownText());
//                            } else if (spanText.contains("条盒条码")) {
//                                tempProduct.put("tobBarCode", childDiv.ownText());
//                            } else if (spanText.contains("小盒零售价")) {
//                                String strong = childDiv.select("strong").text();
//                                tempProduct.put("retailPricePack", BigDecimal.valueOf(Double.parseDouble(strong)));
//                            } else if (spanText.contains("条盒零售价")) {
//                                String strong = childDiv.select("strong").text();
//                                tempProduct.put("retailPrice", BigDecimal.valueOf(Double.parseDouble(strong)));
//                            } else if (spanText.contains("批发价格")) {
//                                String strong = childDiv.ownText();
//                                strong = strong.replaceAll(PATTERN, "");
//                                tempProduct.put("wholeSalePrices", BigDecimal.valueOf(Double.parseDouble(strong)));
//                            } else if (spanText.contains("上市时间")) {
//                                String strong = childDiv.ownText();
//                                tempProduct.put("cgtMarketdate", strong);
//                            }
//                        }
//                        if (!tempProduct.isEmpty()) {
//                            Product product = BeanUtils.mapToBean(tempProduct, Product.class);
//                            product.setCgtId(productId);
//                            product.setCgtName(productName);
//                            product.setBrandId(brandId);
//                            product.setBrandName(brandName);
//                            productList.add(product);
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 获取产品详细信息
     *
     * @param href 短地址
     * @return
     */
    private void retrieveProductDetail(String href, String productName, String brandId, String brandName) {
        try {
            String productId = href.split("=")[1];
            // 通过url直接抓取网页的全部源码
            Document document = Jsoup.connect(BASE_URL + href).get();
            String picHref = document.getElementsByClass("proImg proBarshad").select("a").attr("href");
            if (StringUtils.isNotBlank(picHref)) {
                this.retrieveProductPicture(picHref, productId, productName);
            }
            Elements elements = document.getElementsByClass("proBars").select("span");
            Product product = Product.builder().cgtId(productId).cgtName(productName).brandId(brandId)
                    .brandName(brandName).build();
            for (Element span : elements) {
                String spanText = span.ownText();
                if (span.hasClass("lbl")) {
                    product.setShowName(span.ownText());
                } else if (spanText.contains("产品类型")) {
                    product.setCgtTypeName(span.parent().ownText());
                } else if (spanText.contains("焦油量")) {
                    product.setCgtTarcontent(span.parent().ownText());
                } else if (spanText.contains("烟碱量")) {
                    product.setCgtNicotinic(span.parent().ownText());
                } else if (spanText.contains("一氧化碳量")) {
                    product.setCgtCo(span.parent().ownText());
                } else if (spanText.contains("包装形式")) {
                    product.setCgtPacktypeName(span.parent().ownText());
                } else if (spanText.contains("烟支规格")) {
                    product.setCgtLength(span.parent().ownText());
                } else if (spanText.contains("小盒条码")) {
                    product.setCgtMinCode(span.parent().ownText());
                } else if (spanText.contains("条盒条码")) {
                    product.setTobBarCode(span.parent().ownText());
                } else if (spanText.contains("小盒零售价")) {
                    String strong = span.parent().select("strong").text();
                    product.setRetailPricePack(strong);
                } else if (spanText.contains("条盒零售价")) {
                    String strong = span.parent().select("strong").text();
                    product.setRetailPrice(strong);
                } else if (spanText.contains("批发价格")) {
                    product.setWholeSalePrices(span.parent().ownText());
                } else if (spanText.contains("上市时间")) {
                    product.setCgtMarketdate(span.parent().ownText());
                }
            }
            log.info(product.toString());
            persistentService.saveOrUpdateProduct(product);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取产品图片地址
     *
     * @param href 短地址
     * @return
     */
    private void retrieveProductPicture(String href, String productId, String productName) {
        try {
            // 通过url直接抓取网页的全部源码
            Document document = Jsoup.connect(BASE_URL + href).get();
            Element picBox = document.getElementById("picbox");
            Elements divs = picBox.select("div");
            int index = 1;
            for (Element div : divs) {
                boolean isPicDiv = div.hasAttr("data-img");
                if (isPicDiv) {
                    Picture picture = Picture.builder().imgUrl(BASE + div.attr("data-img"))
                            .productId(productId).productName(productName).sort(index++).build();
                    log.info(picture.toString());
                    persistentService.saveOrUpdatePicture(picture);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
