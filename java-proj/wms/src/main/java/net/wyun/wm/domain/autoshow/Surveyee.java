/**
 * 
 */
package net.wyun.wm.domain.autoshow;

import java.util.Date;

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
@Table(name = "surveyee")
public class Surveyee {
	
	public Surveyee() { createt = new Date();	}
	
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name= "id", columnDefinition = "VARCHAR(36)")
    @Id
	private String  id;      //	   id             VARCHAR(36)       primary key,
	
	private String  name; //           varchar(20)       not null DEFAULT '',
	private int     age;  //            smallint          not null DEFAULT 0,
	private Gender  gender; //         varchar(6)        not null DEFAULT 'MALE',
	private String  city;   //           varchar(36)       not null DEFAULT '',
	private String  phone; //          varchar(10)       not null DEFAULT '',
	private boolean has_car; //        boolean           not null DEFAULT 0,
	
	@Column(name = "create_t", nullable = false)
	private Date    createt; //      datetime          not NULL,
	
	
	public Date getCreatet() {
		return createt;
	}
	public void setCreatet(Date createt) {
		this.createt = createt;
	}

	private Date    modify_t; //       datetime          DEFAULT NULL,
	private String  show_location;  //  VARCHAR(36)       NOT NULL DEFAULT '',
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isHas_car() {
		return has_car;
	}
	public void setHas_car(boolean has_car) {
		this.has_car = has_car;
	}
	
	public Date getModify_t() {
		return modify_t;
	}
	public void setModify_t(Date modify_t) {
		this.modify_t = modify_t;
	}
	public String getShow_location() {
		return show_location;
	}
	public void setShow_location(String show_location) {
		this.show_location = show_location;
	}

}
