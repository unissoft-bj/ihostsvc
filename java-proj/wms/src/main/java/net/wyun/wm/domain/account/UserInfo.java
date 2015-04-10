/**
 * 
 */
package net.wyun.wm.domain.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.wyun.wm.domain.Gender;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Xuecheng
 *
 */
@Entity
@Table(name = "user_info")
public class UserInfo {

	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", columnDefinition = "CHAR(32)")
	@Id
	private String id; // VARCHAR(36) primary key NOT NULL,

	private String first_name; // varchar(20) DEFAULT NULL, # 名字
	private String last_name; // varchar(20) DEFAULT NULL, # 姓
	private String email; // varchar(64) NOT NULL DEFAULT '',
	private int byear; // smallint DEFAULT NULL, # 生日，年 // move to user_info. It
						// can be obtained in different ways.
	private int bmonth; // smallint DEFAULT NULL, # 生日，月
	private int bday; // smallint DEFAULT NULL, # 生日，日
	private Gender gender; // varchar(8) NOT NULL DEFAULT 'M', # 性别 //user info.
	private String occupation; // varchar(30) NOT NULL DEFAULT '', # 职业 // user
								// info
	private String company; // varchar(64) DEFAULT NULL, # 工作单位 //user info
	private String title; // varchar(32) DEFAULT NULL, # 职务 // user info.
	private String cid; // varchar(30) DEFAULT '000000', # 证件号, cid + ctype
	private String ctype; // varchar(10) DEFAULT NULL, # 证件类别 //remove.
	private String address; // varchar(128) DEFAULT NULL, # 地址 // to memo
	private String location; // varchar(32) DEFAULT NULL, # 所在区域 //to memo

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getByear() {
		return byear;
	}

	public void setByear(int byear) {
		this.byear = byear;
	}

	public int getBmonth() {
		return bmonth;
	}

	public void setBmonth(int bmonth) {
		this.bmonth = bmonth;
	}

	public int getBday() {
		return bday;
	}

	public void setBday(int bday) {
		this.bday = bday;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
