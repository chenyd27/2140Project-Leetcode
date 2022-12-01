package com.leetcode.service;

import com.alibaba.fastjson.JSON;
import com.leetcode.pojo.Discuss;
import com.leetcode.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LeetcodeService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    public Boolean parseContent() throws Exception{
        List<Discuss> discusses = new HtmlParseUtil().parseLeetcode();
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10m");
        for(Discuss discuss : discusses){
            bulkRequest.add(new IndexRequest("leetcode_search")
                    .source(JSON.toJSONString(discuss),XContentType.JSON));
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulkResponse.hasFailures();
    }
    // 获取数据，实现搜索功能
    public List<Map<String,Object>> searchPage(String keywords, int pageNo, int pageSize) throws Exception{
        if(pageNo <= 1){
            pageNo = 0;
        }
        // 条件搜索
        SearchRequest searchRequest = new SearchRequest("leetcode_search");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 匹配
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("content",keywords);
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("content","*" + keywords + "*");
        if(keywords != ""){
            sourceBuilder.query(matchQueryBuilder);
        }else{
            sourceBuilder.query(matchAllQueryBuilder);
            //sourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.ASC));
        }
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 分页
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);
        // 执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        // 解析结果
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for(SearchHit documentFields : response.getHits().getHits()){
            Map<String,Object> map = documentFields.getSourceAsMap();
            map.put("score",documentFields.getScore());
            list.add(map);
        }
        return list;
    }
    // 获取数据，实现搜索功能
    public int getPage(String keywords) throws Exception{
        // 条件搜索
        SearchRequest searchRequest = new SearchRequest("leetcode_search");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 匹配
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("content",keywords);
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("content","*" + keywords + "*");
        if(keywords != ""){
            sourceBuilder.query(matchQueryBuilder);
        }else{
            sourceBuilder.query(matchAllQueryBuilder);
            //sourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.ASC));
        }
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        sourceBuilder.from(0);
        sourceBuilder.size(2450);
        // 执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(response.getHits().getHits().length);
        // 解析结果
        return response.getHits().getHits().length;
    }
}
