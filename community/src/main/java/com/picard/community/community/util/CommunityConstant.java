package com.picard.community.community.util;

public interface CommunityConstant {
    /*
    * 激活成功
    * */
    int ACTIVATION_SUCCESS = 0;
    /*
     * 重复激活
     * */
    int ACTIVATION_REPEAT = 1;
    /*
     * 激活失败
     * */
    int ACTIVATION_FAIL = 2;
    /*
    * 默认状态的登录凭证超时
    * */
    int DEFAULT_EXPIRED_SECONDS = 3600*12;
    /*
     * 记住我的登录凭证超时
     * */
    int REMEMBERME_EXPIRED_SECONDS = 3600*12*100;
    /*
    * 实体类型：帖子
    * */
    int ENTITY_TYPE_POST=1;
    /*
    * 实体类型：评论
    * */
    int ENTITY_TYPE_COMMENT=2;
}
