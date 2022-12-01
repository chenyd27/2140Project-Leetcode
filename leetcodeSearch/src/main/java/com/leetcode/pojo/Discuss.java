package com.leetcode.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discuss {
    private String url;
    private String content;
    private String title;
    private String difficulty;
    private String id;
}
