package com.coe.oom.ent.user;

import lombok.Data;

import java.util.Date;

/**
 * @program: oom
 * @description: 用户ent
 * @author: 邓太阳
 * @create: 2019-05-14 15:09
 **/
@Data
public class UserEnt {
    private Long id;    //用户ID
    private String userCode;    //用户账号
    private String custCode;    //归属的客户代码
    private String userName;    //用户姓名
    private String userPwd;    //用户密码
    private String userPhone;    //用户电话
    private String userEmail;    //用户邮件
    private String dictIsEmp;    //是否是公司员工: 是 否
    private String dictSex;    //性别: 男 女
    private String dictPosStatus;    //状态: 在职 离职 休假
    private Long siteId;    //所属站点id
    private String siteCode;    //所属站点code
    private String dictDept;    //所属部门
    private String dictPosition;    //所属职位
    private String dictIsEnableRight;    //是否启用权限
    private String createOperator;    //创建人
    private Date createTime;    //创建时间
    private String updateOperator;    //最后修改人
    private Date updateTime;    //最后修改时间
    private Long expireTime;    //逾期时间
    private String loginSystem;    //登录系统
    private Date lastLoginTime;    //最新登录时间
    private String lastLoginSystem;    //最新登录系统：管理端、客户端、扫描枪 APP
}
