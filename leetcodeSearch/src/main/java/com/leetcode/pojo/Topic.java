package com.leetcode.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    private String id;
    private String name;
    private String url;
    private String difficulty;
}
