package net.wyun.wm.domain;

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
@Table(name = "reception")
public class Reception {
	
	    private enum CarUsageType {
	    	PRIVATE, GOV, ALL;
	    }
	    
	    private enum BuyLevel {
	    	A, B, C, D;
	    }
	    
	    private enum Status {
	    	NEW, UPDATE, DELETE;
	    }
	
	    private Long    id; //             smallint unsigned     NOT NULL auto_increment primary key,
	    
	    @Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "id")
		public Long getId() { return id; }
		
		@SuppressWarnings("unused")
		private void setId(Long id) { this.id = id; }
		
	    private String agent_id;   //    varchar(36)           NOT NULL,                             # this is binded with the phone which records the audio, account id
	    private int    person_cnt;  //     smallint unsigned     NOT NULL default 1,
	    private Gender gender; //         smallint              NOT NULL default 0,       # for example 3M2F
	    private String customer_name; //  varchar(30)           NOT NULL default '',
	    private String phone; //          varchar(20)           NOT NULL default '',
	    private String location; //       varchar(30)           NOT NULL default '',
	    private String transport; //      varchar(20)           NOT NULL default '',                 #到店方式: closed sets?
	    private String intention; //      varchar(20)           NOT NULL default '',                 #来店目的：咨询新车/预约提车/其他 ；选择或者新建，可维护
	    private String visit_index; //    smallint unsigned     NOT NULL default 1,                  #来店频率：初次/再次；选择或者新建，可维护
	    private String refer_source;   //varchar(20)           NOT NULL default "",                 #客户来源：报纸/杂志/电视/电台/网络/户外广告/车展/朋友介绍/路过；选择或者新建，可维护     
	    private CarUsageType car_usage; //      enum('private', 'gov', 'all') NOT NULL default 'private',
	    private String car_style; //      varchar(20)           NOT NULL default '',
	    private String car_model; //      varchar(20)           NOT NULL default '',
	    private String car_color; //    varchar(4)            NOT NULL default '',
	    private BuyLevel buy_level; //      enum('A','B','C','D') NOT NULL default 'A',                #意向级别：A/B/C/D；选择或者新建，可维护
	    private String description; //    varchar(200)          NOT NULL default '',                 #接待经过：调查问卷/试乘试驾/报价；选择或者新建，可维护
	    private String result; //         varchar(100)          NOT NULL default '',                 #接待结果：信息留存/签单/提车；选择或者新建，可维护
	    private String comparison; //     varchar(150)          NOT NULL default '',                 #竞品对比：输入内容，可口述录音, path to audio file or text??
	    private String memo; //           varchar(150)          NOT NULL default '',                 #备注：输入内容，可口述录音 audio or text
	    private Status status; //         enum('new', 'update', 'delete') NOT NULL default 'new'      # 新建，更新，删除
	    private int sibling; //        smallint unsigned,    NOT NULL default 0,                  # id of reception which occurs after current reception in time
	    private Date start_t; //        datetime              NOT NULL,                            #when record button is pressed
	    private Date end_t; //          datetime              default NULL                         #when reception ends, agent opens the app, press the "stop recording"

		public String getAgent_id() {
			return agent_id;
		}

		public void setAgent_id(String agent_id) {
			this.agent_id = agent_id;
		}

		public int getPerson_cnt() {
			return person_cnt;
		}

		public void setPerson_cnt(int person_cnt) {
			this.person_cnt = person_cnt;
		}

		public Gender getGender() {
			return gender;
		}

		public void setGender(Gender gender) {
			this.gender = gender;
		}

		public String getCustomer_name() {
			return customer_name;
		}

		public void setCustomer_name(String customer_name) {
			this.customer_name = customer_name;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getTransport() {
			return transport;
		}

		public void setTransport(String transport) {
			this.transport = transport;
		}

		public String getIntention() {
			return intention;
		}

		public void setIntention(String intention) {
			this.intention = intention;
		}

		public String getVisit_index() {
			return visit_index;
		}

		public void setVisit_index(String visit_index) {
			this.visit_index = visit_index;
		}

		public String getRefer_source() {
			return refer_source;
		}

		public void setRefer_source(String refer_source) {
			this.refer_source = refer_source;
		}

		public CarUsageType getCar_usage() {
			return car_usage;
		}

		public void setCar_usage(CarUsageType car_usage) {
			this.car_usage = car_usage;
		}

		public String getCar_style() {
			return car_style;
		}

		public void setCar_style(String car_style) {
			this.car_style = car_style;
		}

		public String getCar_model() {
			return car_model;
		}

		public void setCar_model(String car_model) {
			this.car_model = car_model;
		}

		public String getCar_color() {
			return car_color;
		}

		public void setCar_color(String car_color) {
			this.car_color = car_color;
		}

		public BuyLevel getBuy_level() {
			return buy_level;
		}

		public void setBuy_level(BuyLevel buy_level) {
			this.buy_level = buy_level;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public String getComparison() {
			return comparison;
		}

		public void setComparison(String comparison) {
			this.comparison = comparison;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public int getSibling() {
			return sibling;
		}

		public void setSibling(int sibling) {
			this.sibling = sibling;
		}

		public Date getStart_t() {
			return start_t;
		}

		public void setStart_t(Date start_t) {
			this.start_t = start_t;
		}

		public Date getEnd_t() {
			return end_t;
		}

		public void setEnd_t(Date end_t) {
			this.end_t = end_t;
		}

}
