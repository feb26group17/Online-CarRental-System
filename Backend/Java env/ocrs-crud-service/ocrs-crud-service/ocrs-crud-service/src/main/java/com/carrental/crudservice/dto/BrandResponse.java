package com.carrental.crudservice.dto;

public class BrandResponse {
    private Integer brandId;
    private String bname;

    public BrandResponse() {
    }

    public BrandResponse(Integer brandId, String bname) {
        this.brandId = brandId;
        this.bname = bname;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public static BrandResponseBuilder builder() {
        return new BrandResponseBuilder();
    }

    public static class BrandResponseBuilder {
        private Integer brandId;
        private String bname;

        public BrandResponseBuilder brandId(Integer brandId) {
            this.brandId = brandId;
            return this;
        }

        public BrandResponseBuilder bname(String bname) {
            this.bname = bname;
            return this;
        }

        public BrandResponse build() {
            return new BrandResponse(brandId, bname);
        }
    }
}
