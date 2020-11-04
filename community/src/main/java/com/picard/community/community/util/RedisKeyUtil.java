package com.picard.community.community.util;

public class RedisKeyUtil {
    public static final String SPLIT = ":";
    public static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType,int entityId){
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_ENTITY_LIKE);
        sb.append(SPLIT);
        sb.append(entityType);
        sb.append(SPLIT);
        sb.append(entityId);
        return sb.toString();
    }
    //某个用户的赞
    // like:user:userId -> int
    public static String getUserLikeKey(int userId){
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_USER_LIKE);
        sb.append(SPLIT);
        sb.append(userId);
        return sb.toString();
    }
    //某个用户关注的实体
    //followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE+SPLIT+userId+SPLIT+entityType;
    }
    //某个实体拥有的粉丝
    // follwer:entityType:entityId - > zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId){
        return PREFIX_FOLLOWER+SPLIT+entityType+SPLIT+entityId;
    }
}
