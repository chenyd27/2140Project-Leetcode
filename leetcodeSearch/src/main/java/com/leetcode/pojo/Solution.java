package com.leetcode.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solution {
    private String title;
    private String content;
    private int id;
    private String author;
    private Question question;
    private String url;
}
