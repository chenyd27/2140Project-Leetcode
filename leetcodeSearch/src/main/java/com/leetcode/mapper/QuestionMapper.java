package com.leetcode.mapper;

import com.leetcode.pojo.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface QuestionMapper {
    public List<Question> getAllQuestion(@Param("name")String name, @Param("pageStart") int pageStart, @Param("pageSize") int pageSize);
    public int getQuestionCounts(@Param("name")String name); // 获取题目个数

}
