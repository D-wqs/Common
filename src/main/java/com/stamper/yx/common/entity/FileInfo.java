package com.stamper.yx.common.entity;

import java.io.Serializable;
import java.util.Date;

public class FileInfo implements Serializable {
    private Integer id;
    private Integer sealRecordInfoId;

    /**
     * 原文件名
     */
    private String originalName;

    /**
     * 新文件名
     */
    private String fileName;

    /**
     * 文件配置地址
     */
    private String path;

    /**
     * 本次存储相对路径
     */
    private String relativePath;

    /**
     * 本地存储绝对路径
     */
    private String absolutePath;

    private Date createDate;

    private Date updateDate;

    private Date deleteDate;

    private static final long serialVersionUID = 1L;

    public Integer getSealRecordInfoId() {
        return sealRecordInfoId;
    }

    public void setSealRecordInfoId(Integer sealRecordInfoId) {
        sealRecordInfoId = sealRecordInfoId;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取原文件名
     *
     * @return original_name - 原文件名
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     * 设置原文件名
     *
     * @param originalName 原文件名
     */
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    /**
     * 获取新文件名
     *
     * @return file_name - 新文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置新文件名
     *
     * @param fileName 新文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取文件地址
     *
     * @return path - 文件地址
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置文件地址
     *
     * @param path 文件地址
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取本次存储相对路径
     *
     * @return relative_path - 本次存储相对路径
     */
    public String getRelativePath() {
        return relativePath;
    }

    /**
     * 设置本次存储相对路径
     *
     * @param relativePath 本次存储相对路径
     */
    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    /**
     * 获取本地存储绝对路径
     *
     * @return absolute_path - 本地存储绝对路径
     */
    public String getAbsolutePath() {
        return absolutePath;
    }

    /**
     * 设置本地存储绝对路径
     *
     * @param absolutePath 本地存储绝对路径
     */
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    /**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return update_date
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return delete_date
     */
    public Date getDeleteDate() {
        return deleteDate;
    }

    /**
     * @param deleteDate
     */
    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sealRecordInfoId=").append(sealRecordInfoId);
        sb.append(", id=").append(id);
        sb.append(", originalName=").append(originalName);
        sb.append(", fileName=").append(fileName);
        sb.append(", path=").append(path);
        sb.append(", relativePath=").append(relativePath);
        sb.append(", absolutePath=").append(absolutePath);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", deleteDate=").append(deleteDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}