package com.picard.community.community;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testStrings(){
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey,1);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        redisTemplate.opsForValue().increment(redisKey);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        redisTemplate.opsForValue().decrement(redisKey);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
    }
    @Test
    public void testHashes(){
        String redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey,"id",1);
        redisTemplate.opsForHash().put(redisKey,"username","zhangsan");
        System.out.println(redisTemplate.opsForHash().get(redisKey,"id"));
    }
    @Test
    public void testLists(){
        String redisKey = "test:ids";
        redisTemplate.opsForList().leftPush(redisKey,101);
        redisTemplate.opsForList().leftPush(redisKey,102);
        redisTemplate.opsForList().leftPush(redisKey,103);
        System.out.println(redisTemplate.opsForList().size(redisKey));
    }
    @Test
    public void testSets(){
        String redisKey = "test:teachers";
        redisTemplate.opsForSet().add(redisKey,"刘备","关于");
        System.out.println(redisTemplate.opsForSet().size(redisKey));
    }
    @Test
    public void testBoundOprations(){
        String redisKey = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }
    //编程事务
    @Test
    public void testTransaction(){
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String redisKey = "test:tx";
                redisOperations.multi();
                redisOperations.opsForSet().add(redisKey,"zhangsan");
                redisOperations.opsForSet().add(redisKey,"lisi");
                redisOperations.opsForSet().add(redisKey,"wangwu");
                System.out.println(redisOperations.opsForSet().members(redisKey));
                return redisOperations.exec();
            }
        });
        System.out.println(obj);
    }
    // 统计20万个重复数据的独立总数
    @Test
    public void testHyperLogLog(){
        String redisKey = "test:hll:01";
        for(int i=1;i<=100000;i++){
            redisTemplate.opsForHyperLogLog().add(redisKey,i);
        }
        for(int i=1;i<=100000;i++){
            int r = (int)(Math.random()*100000+1);
            redisTemplate.opsForHyperLogLog().add(redisKey,r);
        }
        System.out.println(redisTemplate.opsForHyperLogLog().size(redisKey));
    }
    // 将三组数据合并，再合并后的重复数据的独立总数.
    @Test
    public void testHyperLogLogUnion(){
        String redisKey = "test:hll:02";
        for(int i=1;i<=10000;i++){
            redisTemplate.opsForHyperLogLog().add(redisKey,i);
        }
    }
    //统计一组数据的布尔值
    @Test
    public void testBitMap(){
        //就是一个位数组
        String redisKey = "test:bm:01";
        redisTemplate.opsForValue().setBit(redisKey,1,true);
        redisTemplate.opsForValue().setBit(redisKey,4,true);
        redisTemplate.opsForValue().setBit(redisKey,7,true);
        //
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,2));
        //统计
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(redisKey.getBytes());
            }
        });
        System.out.println(obj);
    }
    //统计3组数据的bool值，并对三组数据做or运算
    @Test
    public void testBitmapOperation(){
        String redisKey = "test:bm:02";
        redisTemplate.opsForValue().setBit(redisKey,0,true);
        redisTemplate.opsForValue().setBit(redisKey,1,true);
        redisTemplate.opsForValue().setBit(redisKey,2,true);
        String redisKey2 = "test:bm:03";
        redisTemplate.opsForValue().setBit(redisKey2,2,true);
        redisTemplate.opsForValue().setBit(redisKey2,3,true);
        redisTemplate.opsForValue().setBit(redisKey2,4,true);
        String redisKey3 = "test:bm:04";
        redisTemplate.opsForValue().setBit(redisKey3,4,true);
        redisTemplate.opsForValue().setBit(redisKey3,5,true);
        redisTemplate.opsForValue().setBit(redisKey3,6,true);
        String ansKey = "test:bm:or";
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.bitOp(RedisStringCommands.BitOperation.OR,ansKey.getBytes(),redisKey.getBytes(),redisKey2.getBytes(),redisKey3.getBytes());
                return connection.bitCount(ansKey.getBytes());
            }
        });
        System.out.println(obj);
        System.out.println(redisTemplate.opsForValue().getBit(ansKey,0));
        System.out.println(redisTemplate.opsForValue().getBit(ansKey,1));
        System.out.println(redisTemplate.opsForValue().getBit(ansKey,2));
        System.out.println(redisTemplate.opsForValue().getBit(ansKey,3));
        System.out.println(redisTemplate.opsForValue().getBit(ansKey,4));
        System.out.println(redisTemplate.opsForValue().getBit(ansKey,5));
        System.out.println(redisTemplate.opsForValue().getBit(ansKey,6));
    }
}
