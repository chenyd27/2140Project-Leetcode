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

import java.util.*;
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
            bulkRequest.add(new IndexRequest("leetcode_search_plus")
                    .source(JSON.toJSONString(discuss),XContentType.JSON));
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulkResponse.hasFailures();
    }
    public List<Map<String,Object>> searchPage(String keywords, int pageNo, int pageSize) throws Exception{
        if(pageNo <= 1){
            pageNo = 0;
        }
        // Condition Search
        SearchRequest searchRequest = new SearchRequest("leetcode_search");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // Matching
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("content",keywords);
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        HashMap<String,Map<String,Object>> tmp = new HashMap<>();
        List<Map<String,Object>> ans = new ArrayList<>();
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);
        if(keywords != ""){
            String[] terms = keywords.split(" ");
            for(String term : terms){
                MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("content",term);
                sourceBuilder.query(matchQuery);
                searchRequest.source(sourceBuilder);
                SearchResponse resp = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
                for(SearchHit documentFields : resp.getHits().getHits()){
                    Map<String,Object> map1 = tmp.getOrDefault(documentFields.getId(),documentFields.getSourceAsMap());
                    map1.put(term,(double)documentFields.getScore());
                    tmp.put(documentFields.getId(),map1);
                }
            }
            for(Map<String,Object> objectMap : tmp.values()){
                double score = 1.0;
                for(String term : terms){
                    score *= (double) objectMap.getOrDefault(term,0.01);
                }
                objectMap.put("score",score);
                ans.add(objectMap);
            }
            Collections.sort(ans,(s1,s2)->{
                return (double)s1.get("score") - (double) s2.get("score") >= 0 ? -1 : 1;
            });
            // sourceBuilder.query(matchQueryBuilder);
        }else{
            sourceBuilder.query(matchAllQueryBuilder);
            sourceBuilder.from(pageNo);
            sourceBuilder.size(pageSize);
            // 执行搜索
            searchRequest.source(sourceBuilder);
            SearchResponse response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
            for(SearchHit documentFields : response.getHits().getHits()){
                Map<String,Object> map = documentFields.getSourceAsMap();
                map.put("score",documentFields.getScore());
                ans.add(map);
            }
            //sourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.ASC));
        }
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        System.out.println(ans.size());
        return ans;
    }
    // Get data and implement search function
    public int getPage(String keywords) throws Exception{
        // Condition Search
        SearchRequest searchRequest = new SearchRequest("leetcode_search");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // Matching
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
        // execute a search
        searchRequest.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(response.getHits().getHits().length);
        // Parsing results
        return response.getHits().getHits().length;
    }
}
