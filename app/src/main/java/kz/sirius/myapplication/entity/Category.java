package kz.sirius.myapplication.entity;

public class Category {
    private int categoryId;
    private String name;
    private String categoryUrl;

    public Category(int categoryId, String categoryUrl, String name) {
        this.categoryId = categoryId;
        this.categoryUrl = categoryUrl;
        this.name = name;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }
}
