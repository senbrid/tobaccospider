package cn.unclejoke.spider;

import cn.unclejoke.spider.service.CrawlerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 程序启动类
 *
 * @author V
 * @data 2020-8-14 10:44:17
 */
public class AppMain {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-config.xml");
        CrawlerService crawlerService = (CrawlerService) context.getBean("crawlerService");
        crawlerService.run();
    }
}
