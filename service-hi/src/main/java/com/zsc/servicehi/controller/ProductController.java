package com.zsc.servicehi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 商品服务
     */
    @GetMapping("/service")
    public String productService() {
        logger.info("Product Service Is Called...");
        return "Product Service Is Called...";
    }
}
