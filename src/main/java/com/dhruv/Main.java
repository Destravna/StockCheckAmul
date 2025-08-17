package com.dhruv;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        var ctx = new AnnotationConfigApplicationContext(WebDriverConfig.class);
        InStockCheckService service = ctx.getBean(InStockCheckService.class);
        List<InStockCheck> inStockChecks = new ArrayList<>();
        Utils.urlCollector(UrlStore.class).forEach(url -> {
            logger.info("url : {}", url);
            InStockCheck inStockCheck = new InStockCheck(url);
            service.check(inStockCheck);
            // logger.info(inStockCheck.toString());
            inStockChecks.add(inStockCheck);
        });

        inStockChecks.forEach(obj->logger.info(obj.toString()));
        ctx.close();
    
    }
}
