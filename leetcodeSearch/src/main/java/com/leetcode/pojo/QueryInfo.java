package com.leetcode.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryInfo {
    private String query; // Inquiry Information
    private int pageNum = 1; // Current Page
    private int pageSize = 1; // Maximum number per page
}
