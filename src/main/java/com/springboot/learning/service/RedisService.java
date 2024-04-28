package com.springboot.learning.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.springboot.learning.entity.FamilyMember;
import com.springboot.learning.mapper.FamilyMemberMapper;

@Service
public class RedisService {
    @Resource
    private FamilyMemberMapper familyMemberMapper;

    @Cacheable(value = "RedisService", key = "'findFamilyMemberByName'+#memberName")
    public List<FamilyMember> findFamilyMemberByName(String memberName) {
        return familyMemberMapper.findFamilyMemberByName(memberName);
    }

    @Cacheable(value = "RedisService", key = "'findFamilyMemberById'+#memberId")
    public FamilyMember findFamilyMemberById(Long memberId) {
        return familyMemberMapper.findFamilyMemberById(memberId);
    }

    @Cacheable(value = "RedisService", key = "'findAllMembers'")
    public List<FamilyMember> findAllMembers() {
        return familyMemberMapper.findAllMembers();
    }

    @CacheEvict(value = "RedisService", key = "'findFamilyMemberByName'+#memberName")
    public void invalidFamilyMemberCache(String memberName) {
    }

    @CacheEvict(value = "RedisService", key = "'findFamilyMemberById'+#memberId")
    public void invalidFamilyMemberCache(Long memberId) {
    }

    @CacheEvict(value = "RedisService", key = "'findAllMembers'")
    public void invalidAllCache() {
    }
}
