package com.picard.community.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    //替换符号
    private static final String REPLACEMENT="***";
    private TrieNode rootNode = new TrieNode();
    @PostConstruct
    public void init(){
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                ){
            String keyword;
            while((keyword = reader.readLine()) != null){
                //添加到前缀树
                this.addKeyword(keyword);
            }
        }catch(IOException e){
            logger.error("加载敏感词失败:"+e.getMessage());
        }

    }
    //将一个敏感词添加到前缀树
    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;
        for(int i=0;i<keyword.length();i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                //初始化
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            tempNode = subNode;
            //设置结束
            if(i==keyword.length()-1){
                tempNode.setKeywordEnd(true);
            }
        }
    }
    /*
    * 待过滤的文本
    * @return 过滤后的文本
    * */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //指针1
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;
        //结果
        StringBuilder sb = new StringBuilder();
        while(position<text.length()){
            char c = text.charAt(position);
            //跳过符号
            if(isSymbol(c)){
                //指针1处于根节点，将符号计入结果
                if(tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                position = ++begin;
                //指针向量
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){
                //发现敏感词
                sb.append(REPLACEMENT);
                begin = ++position;
            }else{
                //检查下一个字符
                position++;
            }
        }
        //最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }
    //判断是否为符号
    private boolean isSymbol(Character c){
        // 0x2E80~0x9FFF是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c<0x2E80 || c>0x9FFF);
    }
    private class TrieNode{
        //标志结束
        private boolean isKeywordEnd = false;
        // 子节点
        private Map<Character,TrieNode> subnodes = new HashMap<>();
        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
        //
        public void addSubNode(Character c,TrieNode node){
            subnodes.put(c,node);
        }
        public TrieNode getSubNode(Character c){
            return subnodes.get(c);
        }
    }
}
