package com.example.projectservicetwo.dto;

public class ModelRequestDTO {

    private Integer brandId;
    private String modelName;
    private Integer seatingCapacity;

    public ModelRequestDTO() {
    }

    public ModelRequestDTO(Integer brandId, String modelName, Integer seatingCapacity) {
        this.brandId = brandId;
        this.modelName = modelName;
        this.seatingCapacity = seatingCapacity;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }
}