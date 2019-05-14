package com.coe.oom.core.base.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 此基类  目前只对自定义实体有效
 * @author lqg
 *
 */
public class BaseEntity implements Serializable {

	    private Long id;

	    private String userNameCreate;

	    private Long userIdCreate;

	    private Date createTime;

	    private Long userIdUpdate;

	    private String userNameUpdate;

	    private Date updateTime;
	    
	    private String remark;
	    
		public String getUserNameCreate() {
			return userNameCreate;
		}

		public void setUserNameCreate(String userNameCreate) {
			this.userNameCreate = userNameCreate;
		}

		public Long getUserIdCreate() {
			return userIdCreate;
		}

		public void setUserIdCreate(Long userIdCreate) {
			this.userIdCreate = userIdCreate;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public Long getUserIdUpdate() {
			return userIdUpdate;
		}

		public void setUserIdUpdate(Long userIdUpdate) {
			this.userIdUpdate = userIdUpdate;
		}

		public String getUserNameUpdate() {
			return userNameUpdate;
		}

		public void setUserNameUpdate(String userNameUpdate) {
			this.userNameUpdate = userNameUpdate;
		}

		public Date getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
	    
	    
}
