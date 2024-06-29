package com.example.whatflower.ui.picture;

public class PictureBean {
    String imgUrl;
    String similarity;
    String wjUrl;
    String result;
    String detail;
    public PictureBean() {}

    public PictureBean(String imgUrl, String result, String similarity, String wjUrl, String detail){
        this.imgUrl = imgUrl;
        this.result = result;
        this.similarity = similarity;
        this.wjUrl = wjUrl;
        this.detail = detail;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public String getWjUrl() {
        return wjUrl;
    }

    public void setWjUrl(String wjUrl) {
        this.wjUrl = wjUrl;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public  String getDetail(){return detail; }
    public void setDetail(String detail){this.detail = detail;}
}