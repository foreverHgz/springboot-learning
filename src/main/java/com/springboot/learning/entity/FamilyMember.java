package com.springboot.learning.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author forever_hgz
 */
@Data
public class FamilyMember implements Serializable {
    private long memberId;

    private String memberName;

    private int age;

    private String phoneNumber;

    private String hobby;

    private Date createDate;

    private String createBy;

    private Date updateDate;

    private String updateBy;
}
