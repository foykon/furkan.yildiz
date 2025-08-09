package com.furkan.project.common.result;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedDataResult<T> extends DataResult<List<T>> {

    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    /**
     * @param data          Sayfadaki öğeler
     * @param currentPage   0-based current page
     * @param pageSize      page size
     * @param totalElements toplam kayıt sayısı
     * @param totalPages    toplam sayfa sayısı
     * @param success       işlem sonucu
     * @param message       mesaj/anahtar
     */
    public PagedDataResult(List<T> data,
                           int currentPage,
                           int pageSize,
                           long totalElements,
                           int totalPages,
                           boolean success,
                           String message) {
        super(data, success, message);
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public PagedDataResult(List<T> data,
                           int currentPage,
                           int pageSize,
                           long totalElements,
                           int totalPages,
                           String message) {
        this(data, currentPage, pageSize, totalElements, totalPages, true, message);
    }
}
