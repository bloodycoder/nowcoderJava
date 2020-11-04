package com.picard.community.community.util;

public class RedisKeyUtil {
    public static final String SPLIT = ":";
    public static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
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
}
