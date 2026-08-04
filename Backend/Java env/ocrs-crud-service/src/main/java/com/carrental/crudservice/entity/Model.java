package com.carrental.crudservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "model")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Integer modelId;

    @Column(name = "brand_id", nullable = false)
    private Integer brandId;

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity;

    public Model() {
    }

    public Model(Integer modelId, Integer brandId, String modelName, Integer seatingCapacity) {
        this.modelId = modelId;
        this.brandId = brandId;
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

    public static ModelBuilder builder() {
        return new ModelBuilder();
    }

    public static class ModelBuilder {
        private Integer modelId;
        private Integer brandId;
        private String modelName;
        private Integer seatingCapacity;

        public ModelBuilder modelId(Integer modelId) {
            this.modelId = modelId;
            return this;
        }

        public ModelBuilder brandId(Integer brandId) {
            this.brandId = brandId;
            return this;
        }

        public ModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public ModelBuilder seatingCapacity(Integer seatingCapacity) {
            this.seatingCapacity = seatingCapacity;
            return this;
        }

        public Model build() {
            return new Model(modelId, brandId, modelName, seatingCapacity);
        }
    }
}
