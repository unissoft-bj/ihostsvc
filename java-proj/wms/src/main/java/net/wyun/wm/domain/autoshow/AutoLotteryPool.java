/**
 * 
 */
package net.wyun.wm.domain.autoshow;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Xuecheng
 *
 */
@Entity
@Table(name = "lottery_pool")
public class AutoLotteryPool {
	
	private Long    id; //             smallint unsigned     NOT NULL auto_increment primary key,
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() { return id; }
	
	@SuppressWarnings("unused")
	public void setId(Long id) { this.id = id; }
	
	//   id               int               NOT NULL AUTO_INCREMENT,
	private String   surveyee_id; //      varchar(36)       NOT NULL default '',               ## without surveyee_id, then the phone number is added in gui
	private String   phone; //            varchar(10)       NOT NULL,
	private boolean  disabled;  //       boolean           NOT NULL default 0,
	private boolean  selected; //         boolean           not null default 0,
	private boolean  used; //             boolean           not null default 0,
	private Date     create_t;  //         datetime          not NULL,
	private String   show_location;      //city name where show is performed

	public String getSurveyee_id() {
		return surveyee_id;
	}

	public void setSurveyee_id(String surveyee_id) {
		this.surveyee_id = surveyee_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public Date getCreate_t() {
		return create_t;
	}

	public void setCreate_t(Date create_t) {
		this.create_t = create_t;
	}
	
	

}
