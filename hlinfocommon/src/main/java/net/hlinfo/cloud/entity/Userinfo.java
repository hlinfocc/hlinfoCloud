package net.hlinfo.cloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Table("userinfo")
@ApiModel("管理员信息表")
public class Userinfo implements Serializable {
	@Name
	@Column("id")
	@ColDefine(notNull=false, type=ColType.VARCHAR, width=32)
	@Comment(value="主键ID")
	@ApiModelProperty("权限主键，添加的时候不用传")
	private String id;
	/**
	* 登录账号
	*/
	@Column("account")
	@ColDefine(notNull=true, type=ColType.VARCHAR, width=50)
	@Comment(value="登录账号")
	@ApiModelProperty(value="登录账号")
	private String account;
	/**
	* 用户名
	*/
	@Column("username")
	@ColDefine(notNull=true, type=ColType.VARCHAR, width=50)
	@Comment(value="用户名")
	@ApiModelProperty(value="用户名")
	private String username;
	
	@Column("password")
	@ColDefine(notNull=true, type=ColType.VARCHAR, width=64)
	@Comment(value="密码")
	@ApiModelProperty(value="密码")
	@JsonIgnore
	private String password;
	
	@Column("salt")
	@ColDefine(notNull=true, type=ColType.VARCHAR, width=10)
	@Comment(value="加密盐")
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	private String salt;
	
	@Column("email")
	@ColDefine(type=ColType.VARCHAR, width=50)
	@Comment(value="邮件")
	@ApiModelProperty(value="邮件")
	private String email;
	
	@Column("remark")
	@ColDefine(type=ColType.VARCHAR, width=500)
	@Comment(value="备注")
	@ApiModelProperty(value="备注")
	private String remark;
	
	/**
	* 状态 0 禁用 1 启用
	*/
	@Column("state")
	@ColDefine(type=ColType.INT, width=3)
	@Comment(value="状态 0 禁用 1 启用")
	@ApiModelProperty(value="状态 0 禁用 1 启用")
	private int state;
	
	/**
	* 上一次登陆时间
	*/
	@Column("logint")
	@ColDefine(type=ColType.VARCHAR, width=25)
	@Comment(value="上一次登陆时间")
	@ApiModelProperty(value="上一次登陆时间")
	private String loginutime;
	/**
	* 上一次登陆ip
	*/
	@Column("loginuip")
	@ColDefine(type=ColType.VARCHAR, width=50)
	@Comment(value="上一次登陆ip")
	@ApiModelProperty(value="上一次登陆ip")
	private String loginuip;

	/**
	 * 创建时间
	 */
	@Column("createtime")
	@ColDefine(notNull=true, type=ColType.DATETIME, width=25)
	@Comment(value="创建时间")
	@ApiModelProperty(hidden = true)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createtime;
	/**
	 * 上一次更新时间
	 */
	@Column("updatetime")
	@ColDefine(notNull=true, type=ColType.DATETIME, width=25)
	@Comment(value="上一次更新时间")
	@ApiModelProperty(hidden = true)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatetime;

	/**
	 * 是否删除
	 */
	@Column("isdelete")
	@ColDefine(notNull=true, type=ColType.INT, width=2)
	@Comment(value="是否删除： 0没有删除 1 删除")
	@ApiModelProperty(hidden = true)
	private int isdelete;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getLoginutime() {
		return loginutime;
	}

	public void setLoginutime(String loginutime) {
		this.loginutime = loginutime;
	}

	public String getLoginuip() {
		return loginuip;
	}

	public void setLoginuip(String loginuip) {
		this.loginuip = loginuip;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public int getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
}
