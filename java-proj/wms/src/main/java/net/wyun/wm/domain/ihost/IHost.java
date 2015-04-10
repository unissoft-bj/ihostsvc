/**
 * 
 */
package net.wyun.wm.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

/**
 * @author Xuecheng
 *
 */
@Entity
@Table(name = "ihost")
public class IHost {
	

	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name= "id", columnDefinition = "CHAR(32)")
    @Id
    private String uuidHex;
	
	public IHost(){ this.create_t = new Date(); }
  
    
    public String getUuidHex() {
		return uuidHex;
	}

	public void setUuidHex(String uuidHex) {
		this.uuidHex = uuidHex;
	}

	String name;
    String city;
    String brand;
    
    @Temporal(TemporalType.TIMESTAMP)
    Date create_t;
    
    
    Date modify_t;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Date getCreate_t() {
		return create_t;
	}

	public void setCreate_t(Date create_t) {
		this.create_t = create_t;
	}

	public Date getModify_t() {
		return modify_t;
	}

	public void setModify_t(Date modify_t) {
		this.modify_t = modify_t;
	}
    

}
