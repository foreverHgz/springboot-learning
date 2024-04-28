package com.springboot.learning.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.springboot.learning.entity.FamilyMember;

@Mapper
public interface FamilyMemberMapper {
    @Select("SELECT member_id as memberId, member_name as memberName, age as age, phone_number as phoneNumber, hobby as hobby, create_date as createDate, create_by as createBy, update_date as updateDate, update_by as updateBy FROM mydatabase.family_member WHERE member_name = #{memberName}")
    List<FamilyMember> findFamilyMemberByName(@Param("memberName") String memberName);

    @Select("SELECT member_id as memberId, member_name as memberName, age as age, phone_number as phoneNumber, hobby as hobby, create_date as createDate, create_by as createBy, update_date as updateDate, update_by as updateBy FROM mydatabase.family_member WHERE member_id = #{memberId} limit 1")
    FamilyMember findFamilyMemberById(@Param("memberId") Long memberId);

    List<FamilyMember> findAllMembers();

    int insertFamilyMember(@Param("familyMember") FamilyMember familyMember);

    int updateFamilyMember(@Param("familyMember") FamilyMember familyMember);
}
