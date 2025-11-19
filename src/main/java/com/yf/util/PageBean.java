package com.yf.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 */
public class PageBean {
    
    // 当前页
    private Integer pageNow = 1;
    
    // 总页数
    private Integer pageSum;
    
    // 总数量
    private Integer pageCount;
    
    // 每页显示的数量，默认5条
    private Integer pageSize = 5;
    
    // 每页显示的数据内容
    private List<Object> list = new ArrayList<Object>();
    
    public PageBean() {}
    
    /**
     * 计算总页数
     */
    public void calculatePageSum() {
        if (pageCount != null && pageSize != null && pageSize > 0) {
            pageSum = pageCount / pageSize;
            if (pageCount % pageSize != 0) {
                pageSum++;
            }
        }
    }
    
    // getter 和 setter 方法
    
    public Integer getPageNow() {
        return pageNow;
    }
    
    public void setPageNow(Integer pageNow) {
        this.pageNow = pageNow;
    }
    
    public Integer getPageSum() {
        return pageSum;
    }
    
    public void setPageSum(Integer pageSum) {
        this.pageSum = pageSum;
    }
    
    public Integer getPageCount() {
        return pageCount;
    }
    
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
        // 设置总数量后自动计算总页数
        calculatePageSum();
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        // 设置每页数量后自动计算总页数
        calculatePageSum();
    }
    
    public List<Object> getList() {
        return list;
    }
    
    public void setList(List<Object> list) {
        this.list = list;
    }
    
    /**
     * 获取起始下标
     */
    public int getStartIndex() {
        return (pageNow - 1) * pageSize;
    }
    
    /**
     * 是否有上一页
     */
    public boolean hasPrevious() {
        return pageNow > 1;
    }
    
    /**
     * 是否有下一页
     */
    public boolean hasNext() {
        return pageNow < pageSum;
    }
    
    /**
     * 获取上一页页码
     */
    public int getPreviousPage() {
        return pageNow - 1;
    }
    
    /**
     * 获取下一页页码
     */
    public int getNextPage() {
        return pageNow + 1;
    }
}