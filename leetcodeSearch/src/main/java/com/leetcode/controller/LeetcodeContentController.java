package com.leetcode.controller;

import com.alibaba.fastjson.JSON;
import com.leetcode.pojo.QueryInfo;
import com.leetcode.service.LeetcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LeetcodeContentController {
    @Autowired
    LeetcodeService leetcodeService;
    @GetMapping("/parseleetcode")
    public Boolean parse() throws Exception{
        return leetcodeService.parseContent();
    }
    @GetMapping("/search")
    public String search(QueryInfo queryInfo) throws Exception{
        int pageStart = (queryInfo.getPageNum() - 1) * queryInfo.getPageSize();
        List<Map<String,Object>> list = leetcodeService.searchPage(queryInfo.getQuery(), pageStart, queryInfo.getPageSize());
        int size = leetcodeService.getPage(queryInfo.getQuery());
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("size",size);
        String res = JSON.toJSONString(map);
        return res;
    }
}
