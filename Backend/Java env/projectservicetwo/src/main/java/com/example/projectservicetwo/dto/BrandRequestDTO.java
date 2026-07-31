package com.example.projectservicetwo.dto;

public class BrandRequestDTO {

    private String bname;

    public BrandRequestDTO() {
    }

    public BrandRequestDTO(String bname) {
        this.bname = bname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }
}