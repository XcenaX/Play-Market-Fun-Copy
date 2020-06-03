package kz.sirius.myapplication.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MyApp implements Serializable {

    private String title;
    private String text;
    private String img_url;
    private int size;
    private String short_description;
    private List<String> previews;
    private String creator;
    private int downloads;
    private String version;
    private String lastUpdate;
    private boolean isDownloaded;
    private int id;
    private int categoryId;

    public MyApp(String title, String text, String short_description, int size) {
        this.title = title;
        this.text = text;
        this.short_description = short_description;
        this.size = size;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }



    public void setSize(int size) {
        this.size = size;
    }

    public void setShortDescription(String short_description) {
        this.short_description = short_description;
    }

    public void setPreviews(List<String> previews) {
        this.previews = previews;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getShortDescription() {
        return short_description;
    }

    public int getSize() {
        return size;
    }

    public List<String> getPreviews() {
        return previews;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }


}
