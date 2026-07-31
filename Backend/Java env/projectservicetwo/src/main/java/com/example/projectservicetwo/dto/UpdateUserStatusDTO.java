package com.example.projectservicetwo.dto;

public class UpdateUserStatusDTO {

    private String status;

    public UpdateUserStatusDTO() {
    }

    public UpdateUserStatusDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}