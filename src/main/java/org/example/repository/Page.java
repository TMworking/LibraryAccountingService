package org.example.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Page<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalRecords;
}
