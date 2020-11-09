package com.picard.community.community.service;

import com.picard.community.community.dao.MessageMapper;
import com.picard.community.community.entity.Message;
import com.picard.community.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    public List<Message> findConversations(int userId,int offset,int limit){
        return messageMapper.selectConversations(userId,offset,limit);
    }
    public int findConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }
    public List<Message>findLetters(String conversationId,int offset,int limit){
        return messageMapper.selectLetters(conversationId,offset,limit);
    }
    //查询某个会话所包含的私信数量
    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }
    //查询未读私信数量
    public int findLetterUnreadCount(int userId,String conversationId){
        return messageMapper.selectLetterUnreadCount(userId,conversationId);
    }
    //发私信
    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }
    //读取消息
    public int readMessage(List<Integer>ids){
        return messageMapper.updateStatus(ids,1);
    }
    //最新通知
    public Message findLatestNotice(int userId,String topic){
        return messageMapper.selectLastestNotice(userId,topic);
    }
    public int findNoticeCount(int userId,String topic){
        return messageMapper.selectNoticeCount(userId,topic);
    }
    public int findNoticeUnreadCount(int userId,String topic){
        return messageMapper.selectNoticeUnreadCount(userId,topic);
    }
    public List<Message> findNotices(int userId,String topic,int offset,int limit){
        return messageMapper.selectNotices(userId, topic, offset, limit);
    }
}
