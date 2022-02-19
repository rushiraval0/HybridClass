package com.example.hybridclass;

public class FileMaterial {

    private String materialName;
    private String downloadUrl;

    public FileMaterial() {
    }

    public FileMaterial(String materialName, String downloadUrl) {
        this.materialName = materialName;
        this.downloadUrl = downloadUrl;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
