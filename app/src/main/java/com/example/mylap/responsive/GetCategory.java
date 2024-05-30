package com.example.mylap.responsive;

import com.example.mylap.models.Category;

import java.util.List;

public class GetCategory {
    private List<Category> data;
    private Number status;

    public GetCategory(List<Category> data, Number status) {
        this.data = data;
        this.status = status;
    }

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

    public Number getStatus() {
        return status;
    }

    public void setStatus(Number status) {
        this.status = status;
    }
}
