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
    private String query; // 查询信息
    private int pageNum = 1; // 当前页书
    private int pageSize = 1; // 每页最大数
}
