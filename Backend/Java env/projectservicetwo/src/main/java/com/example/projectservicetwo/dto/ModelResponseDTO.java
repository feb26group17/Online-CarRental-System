package com.example.projectservicetwo.dto;

public class ModelResponseDTO {

    private Integer modelId;
    private Integer brandId;
    private String brandName;
    private String modelName;
    private Integer seatingCapacity;

    public ModelResponseDTO() {
    }

    public ModelResponseDTO(Integer modelId, Integer brandId, String brandName, String modelName, Integer seatingCapacity) {
        this.modelId = modelId;
        this.brandId = brandId;
        this.brandName = brandName;
        this.modelName = modelName;
        this.seatingCapacity = seatingCapacity;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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