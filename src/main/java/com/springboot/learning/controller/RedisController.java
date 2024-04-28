package com.springboot.learning.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.springboot.learning.entity.FamilyMember;
import com.springboot.learning.service.FamilyMemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/redis", tags = "测试用接口")
public class RedisController {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // @Resource
    // private FamilyMemberMapper familyMemberMapper;

    @Resource
    private FamilyMemberService familyMemberService;

    @GetMapping("/setRedisStringValue/{key}/{value}")
    @ApiOperation(value = "设置字符串缓存信息到redis", notes = "设置字符串缓存信息到redis")
    public String testSetRedisStringValue(@PathVariable(name = "key") String key,
        @PathVariable(name = "value") String value) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
        return valueOperations.get(key);
    }

    @GetMapping("/getRedisStringValue/{key}")
    @ApiOperation(value = "获取redis字符串缓存信息", notes = "获取redis字符串缓存信息")
    public String testGetRedisStringValue(@PathVariable(name = "key") String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    @GetMapping("/setRedisSetValue/{key}")
    @ApiOperation(value = "设置Set缓存信息到redis", notes = "设置Set缓存信息到redis")
    public Set<Object> testSetRedisSetValue(@PathVariable(name = "key") String key, FamilyMember familyMember) {
        SetOperations<Object, Object> setOperations = redisTemplate.opsForSet();
        setOperations.add(key, familyMember);
        return setOperations.members(key);
    }

    @GetMapping("/getRedisSetValue/{key}")
    @ApiOperation(value = "获取redis Set缓存信息", notes = "获取redis Set缓存信息")
    public Set<Object> testGetRedisSetValue(@PathVariable(name = "key") String key) {
        SetOperations<Object, Object> setOperations = redisTemplate.opsForSet();
        return setOperations.members(key);
    }

    @GetMapping("/setRedisZSetValue/{key}")
    @ApiOperation(value = "设置Set缓存信息到redis", notes = "设置Set缓存信息到redis")
    public Set<Object> testSetRedisZSetValue(@PathVariable(name = "key") String key, FamilyMember familyMember) {
        ZSetOperations<Object, Object> zsetOperations = redisTemplate.opsForZSet();
        zsetOperations.add(key, familyMember, 1.0);
        return zsetOperations.range(key, 0, -1);
    }

    @GetMapping("/getRedisZSetValue/{key}")
    @ApiOperation(value = "获取redis Set缓存信息", notes = "获取redis Set缓存信息")
    public Set<Object> testGetRedisZSetValue(@PathVariable(name = "key") String key) {
        ZSetOperations<Object, Object> zsetOperations = redisTemplate.opsForZSet();
        return zsetOperations.range(key, 0, -1);
    }

    @GetMapping("/setRedisListValue/{key}")
    @ApiOperation(value = "设置列表缓存信息到redis", notes = "设置列表缓存信息到redis")
    public List<Object> testSetRedisListValue(HttpServletRequest request, HttpServletResponse response,
        @PathVariable(name = "key") String key, FamilyMember familyMember) {
        ListOperations<Object, Object> listOperations = redisTemplate.opsForList();
        listOperations.leftPush(key, familyMember);
        Optional<Long> endIndex = Optional.ofNullable(listOperations.size(key));
        return endIndex.isPresent() ? listOperations.range(key, 0, endIndex.get() - 1) : Collections.emptyList();
    }

    @GetMapping("/getRedisListValue/{key}")
    @ApiOperation(value = "获取redis列表缓存信息", notes = "获取redis列表缓存信息")
    public List<Object> testGetRedisListValue(@PathVariable(name = "key") String key) {
        ListOperations<Object, Object> listOperations = redisTemplate.opsForList();
        Optional<Long> endIndex = Optional.ofNullable(listOperations.size(key));
        return endIndex.isPresent() ? listOperations.range(key, 0, endIndex.get() - 1) : Collections.emptyList();
    }

    @GetMapping("/setRedisHashValue/{key}")
    @ApiOperation(value = "设置哈希缓存信息到redis", notes = "设置哈希缓存信息到redis")
    public Object testSetRedisHashValue(@PathVariable(name = "key") String key, FamilyMember familyMember) {
        HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, key, familyMember);
        return hashOperations.get(key, key);
    }

    @GetMapping("/getRedisHashValue/{key}")
    @ApiOperation(value = "获取redis哈希缓存信息", notes = "获取redis哈希缓存信息")
    public Object testGetRedisHashValue(@PathVariable(name = "key") String key) {
        HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, key);
    }

    @GetMapping("/testMysqlConnection/{memberName}")
    @ApiOperation(value = "根据名称查询成员信息", notes = "根据名称查询成员信息")
    public String testMysqlConnection(@PathVariable(name = "memberName") String memberName) {
        List<FamilyMember> familyMembers = familyMemberService.findFamilyMemberByName(memberName);
        return JSON.toJSONString(familyMembers);
    }

    @GetMapping("/testQueryAllMembers")
    @ApiOperation(value = "查询所有成员信息", notes = "查询所有成员信息")
    public String testQueryAllMembers() {
        List<FamilyMember> familyMembers = familyMemberService.findAllMembers();
        return JSON.toJSONString(familyMembers);
    }

    @GetMapping("/testSaveFamilyMember")
    @ApiOperation(value = "新增成员信息", notes = "新增成员信息")
    public String testSaveFamilyMember(FamilyMember familyMember) {
        familyMemberService.insertFamilyMember(familyMember);
        return JSON.toJSONString(familyMemberService.findFamilyMemberById(familyMember.getMemberId()));
    }

    @GetMapping("/testUpdateFamilyMember")
    @ApiOperation(value = "修改成员信息", notes = "修改成员信息")
    public String testUpdateFamilyMember(FamilyMember familyMember) {
        familyMemberService.updateFamilyMember(familyMember);
        return JSON.toJSONString(familyMemberService.findFamilyMemberById(familyMember.getMemberId()));
    }
}
