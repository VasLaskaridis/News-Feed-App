package com.example.periptero.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonModel {

    private String status;

    private String totalResults;

    private List<ArticleModel> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<ArticleModel> getArticles() { return articles; }

    public void setArticles(List<ArticleModel> articles) {
        this.articles = articles;
    }
}
