package net.wyun.wm.domain.autoshow;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * @author Xuecheng
 * 
 */
@Entity
@Table(name = "auto_q_ans")
public class Answer {
	
	public Answer() {this.createt = new Date(); }

	
	private Integer    id; //             smallint unsigned     NOT NULL auto_increment primary key,
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Integer getId() { return id; }
	
	@SuppressWarnings("unused")
	public void setId(Integer id) { this.id = id; }
	
	String surveyee_id = "";
	Integer q_id;               //  int               NOT NULL,
	String  available_option;   // varchar(350)      not null DEFAULT '',   ## json string
	
	private Date    createt;          //         datetime          not NULL,
	@Column(name = "create_t", nullable = false)
	public Date getCreatet() {
		return createt;
	}

	public void setCreatet(Date createt) {
		this.createt = createt;
	}

	public String getSurveyee_id() {
		return surveyee_id;
	}

	public void setSurveyee_id(String surveyee_id) {
		this.surveyee_id = surveyee_id;
	}

	public Integer getQ_id() {
		return q_id;
	}

	public void setQ_id(Integer q_id) {
		this.q_id = q_id;
	}

	public String getAvailable_option() {
		return available_option;
	}

	public void setAvailable_option(String available_option) {
		this.available_option = available_option;
	}

	

}
