package com.example.projectservicetwo.dto;

public class BrandResponseDTO {

    private Integer brandId;
    private String bname;

    public BrandResponseDTO() {
    }

    public BrandResponseDTO(Integer brandId, String bname) {
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
}