package com.dhruv;

import java.time.Duration;
import java.time.LocalDateTime;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@PropertySource("classpath:application.properties")
public class InStockCheckService {

    private static final Logger logger = LoggerFactory.getLogger(InStockCheckService.class);
    boolean hasEnteredPincodeBefore = false;

    @Value("${pincode}")
    private String pincode;

    @Value("${xpath-pincode-list-click}")
    private String clickablePincodeXpath;

    @Value("${xpath-add-to-cart-box}")
    private String addToCartXpath;

    @Value("${xpath-pincode-box-top}")
    private String pincodeBoxAtTopXpath;

    @Value("${xpath-add-to-cart-box-in-stock}")
    private String addToCartXpath2;

    @Autowired
    private WebDriver driver;

    @PostConstruct
    void printValues() {
        logger.info("pincode: {}", pincode);
        logger.info("clickablePincodeXpath: {}", addToCartXpath);
        logger.info("addToCartBox: {}", addToCartXpath);
        logger.info("pincodeAtTop: {}", pincodeBoxAtTopXpath);
    }

    public void check(InStockCheck inStockCheck) {

        driver.get(inStockCheck.getUrl());
        inStockCheck.setTimeChecked(LocalDateTime.now());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        if (!hasEnteredPincodeBefore) {
            enterPinCode(wait);
        } else if (!isPinCodeFilled(wait)) {
            enterPinCode(wait);
        }

        var addToCartBox = getAddToCart(wait);
        var disabledAttribute = addToCartBox.getAttribute("disabled");
        var title = addToCartBox.getAttribute("title");
        var outerHtml = addToCartBox.getAttribute("outerHTML");
        logger.info("disabledAttribute : {}", disabledAttribute);
        logger.info("title: {}", title);
        logger.info("outerHtml: {}", outerHtml);
        if (isDisabled(outerHtml)) {
            inStockCheck.setStatus(Status.OutOfStock);
        } else {
            inStockCheck.setStatus(Status.InStock);
        }

    }

    private WebElement getAddToCart(WebDriverWait wait) {
        try {
            var addToCartBoxOutOfStockCase = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(addToCartXpath)));
            return addToCartBoxOutOfStockCase;
        } catch (TimeoutException tex) {
            return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(addToCartXpath2)));
        }
    }

    public void enterPinCode(WebDriverWait wait) {
        var searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("search")));
        logger.info("looking for serach box");
        // null check is useless btw
        logger.info("Search Box Found");
        searchBox.sendKeys(pincode + Keys.ENTER + Keys.ENTER);
        var pinCodeBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(clickablePincodeXpath)));
        logger.info("pincode box clicked");
        pinCodeBox.click();
        hasEnteredPincodeBefore = true;

    }

    public boolean isPinCodeFilled(WebDriverWait wait) {
        var pagePincode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(pincodeBoxAtTopXpath)));
        String pincodeTest = pagePincode.getText();
        logger.info("pincode at top : {}", pincodeTest);
        if (pincodeTest.contains(pincode)) {
            return true;
        }
        return false;
    }

    private boolean isDisabled(String outerHtml) {
        if (getDisableValue(outerHtml).equals("true")) {
            return true;
        }
        return false;
    }

    private String getDisableValue(String outerHtml) {
        StringBuilder builder = new StringBuilder();
        int indexOfDisabled = outerHtml.indexOf("disabled=");
        if (indexOfDisabled != -1) {
            boolean canAdd = false;
            for (int idx = indexOfDisabled; idx <= indexOfDisabled + 30; idx++) {
                if (canAdd) {
                    if (outerHtml.charAt(idx) == '"') {
                        break;
                    } else {
                        builder.append(outerHtml.charAt(idx));
                    }
                } else if (!canAdd && outerHtml.charAt(idx) == '"') {
                    canAdd = true;
                }
            }
        }
        logger.info("disabled value: {}", builder.toString());
        return builder.toString();

    }
}
