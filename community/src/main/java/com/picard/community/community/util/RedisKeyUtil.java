package com.picard.community.community.util;

public class RedisKeyUtil {
    public static final String SPLIT = ":";
    public static final String PREFIX_ENTITY_LIKE = "like:entity";
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
}
