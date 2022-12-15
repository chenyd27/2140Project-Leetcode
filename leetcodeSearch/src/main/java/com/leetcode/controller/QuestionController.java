package com.leetcode.controller;

import com.leetcode.mapper.QuestionMapper;
import com.leetcode.pojo.QueryInfo;
import com.leetcode.pojo.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class QuestionController {
    @Autowired
    QuestionMapper questionMapper;
    // Wrapping parameters for paging and fuzzy queries into entities
    @RequestMapping("/allquestion")
    public Map<String,Object> getUserList(QueryInfo queryInfo){
        int count = questionMapper.getQuestionCounts("%" + queryInfo.getQuery() + "%");
        int pageStart = (queryInfo.getPageNum() - 1) * queryInfo.getPageSize();
        List<Question> questionList = questionMapper.getAllQuestion("%" + queryInfo.getQuery() + "%",pageStart, queryInfo.getPageSize());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("count",count);
        hashMap.put("questionList",questionList);
        return hashMap;
    }
}
