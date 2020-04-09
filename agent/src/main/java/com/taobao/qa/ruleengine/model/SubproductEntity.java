package com.taobao.qa.ruleengine.model;

public class SubproductEntity {
    private String product; // 产品线信息
    private String subproduct;// 业务线信息

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSubproduct() {
        return subproduct;
    }

    public void setSubproduct(String subproduct) {
        this.subproduct = subproduct;
    }
}
