package com.leetcode.utils;

import com.leetcode.pojo.Discuss;
import com.leetcode.pojo.Topic;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
@Component
public class HtmlParseUtil {
    public List<Topic> getQuestion() throws Exception{
        List<Topic> questions = new ArrayList<>();
        for (int j = 1;j <= 1;j++){
            String address = "https://leetcode.com/problemset/all/?page=" + j;
            System.setProperty("webdriver.chrome.driver","chromedriver");
            WebDriver driver = new ChromeDriver();
            driver.get(address);
            Thread.sleep(1000);
            WebElement page = driver.findElement(By.tagName("div"));
            System.out.println("=======");
            String res = page.getText().toLowerCase(Locale.ROOT);
            String[] split = res.split("\n");
            int index = 0;
            for(;index < split.length;index++){
                if(split[index].equals("frequency")) break;
            }
            index += 1;
            for(int i = 0;i < 50;i++){
                String[] topic = split[index].split("\\. ");
                index += 2;
                String difficuly = split[index];
                String id = topic[0];
                if(id.length() < 4){
                    if(id.length() == 1) id = "000" + id;
                    else if(id.length() == 2) id = "00" + id;
                    else if(id.length() == 3) id = "0" + id;
                }
                String title = topic[1];
                String tmp = title.replace(' ','-');
                index++;
                String url = "https://leetcode.com/problems/" + tmp;
                Topic question = new Topic();
                question.setId(id);
                question.setUrl(url);
                question.setDifficulty(difficuly);
                question.setName(title);
                questions.add(question);
            }
            driver.quit();
        }
        return questions;
    }
    public List<Discuss> parseLeetcode() throws Exception{
       List<Discuss> discusses = new ArrayList<>();
       List<Topic> questions = getQuestion();
       for(Topic question : questions){
           Discuss discuss = new Discuss();
           discuss.setTitle(question.getName());
           discuss.setId(question.getId());
           discuss.setUrl(question.getUrl());
           discuss.setDifficulty(question.getDifficulty());
           String url = question.getUrl() + "/solution/";
           System.setProperty("webdriver.chrome.driver","chromedriver");
           WebDriver driver = new ChromeDriver();
           driver.get(url);
           Thread.sleep(2000);
           WebElement page = driver.findElement(By.tagName("body"));
           // ????????????
           String content = page.getText();
           ParseLeetcodeContent.loadStopWords();
           content = ParseLeetcodeContent.parseContent(content);
           //System.out.println(content);
           driver.quit();
           discuss.setContent(content);
           discusses.add(discuss);
       }
       return discusses;
    }

}
