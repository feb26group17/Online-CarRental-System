package com.carrental.crudservice.dto;

public class ModelResponse {
    private Integer modelId;
    private Integer brandId;
    private String brandName;
    private String modelName;
    private Integer seatingCapacity;

    public ModelResponse() {
    }

    public ModelResponse(Integer modelId, Integer brandId, String brandName, String modelName, Integer seatingCapacity) {
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

    public static ModelResponseBuilder builder() {
        return new ModelResponseBuilder();
    }

    public static class ModelResponseBuilder {
        private Integer modelId;
        private Integer brandId;
        private String brandName;
        private String modelName;
        private Integer seatingCapacity;

        public ModelResponseBuilder modelId(Integer modelId) {
            this.modelId = modelId;
            return this;
        }

        public ModelResponseBuilder brandId(Integer brandId) {
            this.brandId = brandId;
            return this;
        }

        public ModelResponseBuilder brandName(String brandName) {
            this.brandName = brandName;
            return this;
        }

        public ModelResponseBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public ModelResponseBuilder seatingCapacity(Integer seatingCapacity) {
            this.seatingCapacity = seatingCapacity;
            return this;
        }

        public ModelResponse build() {
            return new ModelResponse(modelId, brandId, brandName, modelName, seatingCapacity);
        }
    }
}
