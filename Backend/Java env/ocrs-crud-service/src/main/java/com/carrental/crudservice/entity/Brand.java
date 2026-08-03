package com.carrental.crudservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Integer brandId;

    @Column(nullable = false, unique = true, length = 50)
    private String bname;

    public Brand() {
    }

    public Brand(Integer brandId, String bname) {
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

    public static BrandBuilder builder() {
        return new BrandBuilder();
    }

    public static class BrandBuilder {
        private Integer brandId;
        private String bname;

        public BrandBuilder brandId(Integer brandId) {
            this.brandId = brandId;
            return this;
        }

        public BrandBuilder bname(String bname) {
            this.bname = bname;
            return this;
        }

        public Brand build() {
            return new Brand(brandId, bname);
        }
    }
}
