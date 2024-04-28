package com.springboot.learning.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.springboot.learning.entity.FamilyMember;
import com.springboot.learning.mapper.FamilyMemberMapper;

@Service
public class FamilyMemberService {
    @Resource
    private FamilyMemberMapper familyMemberMapper;

    @Resource
    private RedisService redisService;

    public List<FamilyMember> findFamilyMemberByName(String memberName) {
        return redisService.findFamilyMemberByName(memberName);
    }

    public FamilyMember findFamilyMemberById(Long memberId) {
        return redisService.findFamilyMemberById(memberId);
    }

    public List<FamilyMember> findAllMembers() {
        return redisService.findAllMembers();
    }

    public void insertFamilyMember(FamilyMember familyMember) {
        int num = familyMemberMapper.insertFamilyMember(familyMember);
        if (num > 0) {
            redisService.invalidAllCache();
        }
    }

    public void updateFamilyMember(FamilyMember familyMember) {
        int num = familyMemberMapper.updateFamilyMember(familyMember);
        if (num > 0) {
            redisService.invalidAllCache();
            redisService.invalidFamilyMemberCache(familyMember.getMemberId());
            FamilyMember temp = findFamilyMemberById(familyMember.getMemberId());
            redisService.invalidFamilyMemberCache(temp.getMemberName());
        }
    }
}
