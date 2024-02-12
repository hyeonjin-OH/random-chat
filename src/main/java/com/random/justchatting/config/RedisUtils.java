package com.random.justchatting.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String ,Object> redisTemplate;

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    /**
     * @desc: Sorted Set 삭제.
     */
    public Mono<Long> delete(String key){
        return reactiveRedisTemplate.delete(key);
    }

    /**
     * @desc: Sorted Set 조회
     */
    public Mono<String> getValue(String key){
        return reactiveRedisTemplate.opsForValue().get(key);
    }

    /**
     * @desc: RedisTemplate에 SortedSet 초기화.
     */
    public ReactiveZSetOperations opsForZSet(){
        return reactiveRedisTemplate.opsForZSet();
    }

    /**
     * @desc: Sorted Set 자료형 사이즈
     */
    public Mono<Long> zCard(String str){
        ReactiveZSetOperations z = reactiveRedisTemplate.opsForZSet();
        return z.size(str);
    }

    /**
     * @desc: Sorted Set 자료형 start ~ end 까지 조회.
     */
    public Flux<String> zRange(String key, Long start, Long end){
        return opsForZSet().range(key, Range.closed(start, end));
    }

    /**
     * @desc: Sorted Set 자료형 Value의 현재위치 조회.
     */
    public Mono<Long> getzRank(String key, String value){
        return opsForZSet().rank(key, value);
    }

}
