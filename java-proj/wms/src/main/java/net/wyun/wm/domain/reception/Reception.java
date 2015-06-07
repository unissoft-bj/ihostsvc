package net.wyun.wm.domain.reception;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.wyun.wm.domain.Gender;
import net.wyun.wm.domain.WmsData;


/**
 * 
 * @author Xuecheng
 * 
 */
@Entity
@Table(name = "reception")
public class Reception extends WmsData{
	
	
	
	    public Reception() {this.createt = new Date(); }

		private enum CarUsageType {
	    	PRIVATE, GOV, ALL;
	    }
	    
	    private enum BuyLevel {
	    	A, B, C, D;
	    }
	    
	    private enum Status {
	    	NEW, UPDATE, CLOSED, OPEN;
	    }
		
	    @Column(name = "agent_id", nullable = false)
	    private String agentId;   //    varchar(36)           NOT NULL,                             # this is binded with the phone which records the audio, account id
	    public String getAgentId() {
			return agentId;
		}

		public void setAgentId(String agentId) {
			this.agentId = agentId;
		}

		private String agent_phone = "";   //optional, in case agent has multiple phone being used in the system
	    private String agent_mac = "";     //optional, the mac of the agent's phone

		private int    person_cnt;  //     smallint unsigned     NOT NULL default 1,
	    
	    private Gender gender = Gender.MALE; //         smallint              NOT NULL default 0,       # for example 3M2F
	    private String customer_name = ""; //  varchar(30)           NOT NULL default '',
	    private String phone = ""; //          varchar(20)           NOT NULL default '',
	    private String location = ""; //       varchar(30)           NOT NULL default '',
	    private String transport = ""; //      varchar(20)           NOT NULL default '',                 #到店方式: closed sets?
	    private String intention = ""; //      varchar(20)           NOT NULL default '',                 #来店目的：咨询新车/预约提车/其他 ；选择或者新建，可维护
	    private int visit_index = 1; //    smallint unsigned     NOT NULL default 1,                  #来店频率：初次/再次；选择或者新建，可维护
	    private String refer_source = "";   //varchar(20)           NOT NULL default "",                 #客户来源：报纸/杂志/电视/电台/网络/户外广告/车展/朋友介绍/路过；选择或者新建，可维护     
	    
	    private CarUsageType car_usage = CarUsageType.PRIVATE; //      enum('private', 'gov', 'all') NOT NULL default 'private',
	    private String car_style = ""; //      varchar(20)           NOT NULL default '',
	    private String car_model = ""; //      varchar(20)           NOT NULL default '',
	    private String car_color = ""; //    varchar(4)            NOT NULL default '',
	    private BuyLevel buy_level = BuyLevel.A; //      enum('A','B','C','D') NOT NULL default 'A',                #意向级别：A/B/C/D；选择或者新建，可维护
	    private String description = ""; //    varchar(200)          NOT NULL default '',                 #接待经过：调查问卷/试乘试驾/报价；选择或者新建，可维护
	    private String result = ""; //         varchar(100)          NOT NULL default '',                 #接待结果：信息留存/签单/提车；选择或者新建，可维护
	    private String comparison = ""; //     varchar(150)          NOT NULL default '',                 #竞品对比：输入内容，可口述录音, path to audio file or text??
	    private String memo = ""; //           varchar(150)          NOT NULL default '',                 #备注：输入内容，可口述录音 audio or text
	    
	    private Status status = Status.NEW; //         enum('new', 'update', 'delete') NOT NULL default 'new'      # 新建，更新，删除
	    private int  sibling_id; //        smallint unsigned,    NOT NULL default 0,                  # id of reception which occurs after current reception in time
	    private Date end_t; //          datetime              default NULL                         #when reception ends, agent opens the app, press the "stop recording"
        private Date modify_t;
	    
	    public Date getModify_t() {
			return modify_t;
		}

		public void setModify_t(Date modify_t) {
			this.modify_t = modify_t;
		}

		public int getSibling_id() {
			return sibling_id;
		}

		public void setSibling_id(int sibling_id) {
			this.sibling_id = sibling_id;
		}
		

		public String getAgent_phone() {
			return agent_phone;
		}
	
		public void setAgent_phone(String agent_phone) {
			this.agent_phone = agent_phone;
		}
		

	    public String getAgent_mac() {
			return agent_mac;
		}

		public void setAgent_mac(String agent_mac) {
			this.agent_mac = agent_mac;
		}
		
		public int getPerson_cnt() {
			return person_cnt;
		}

		public void setPerson_cnt(int person_cnt) {
			this.person_cnt = person_cnt;
		}

		@Enumerated(EnumType.STRING)
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

		public int getVisit_index() {
			return visit_index;
		}

		public void setVisit_index(int visit_index) {
			this.visit_index = visit_index;
		}

		public String getRefer_source() {
			return refer_source;
		}

		public void setRefer_source(String refer_source) {
			this.refer_source = refer_source;
		}

		@Enumerated(EnumType.STRING)
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

		@Enumerated(EnumType.STRING)
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

		@Enumerated(EnumType.STRING)
		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public Date getEnd_t() {
			return end_t;
		}

		public void setEnd_t(Date end_t) {
			this.end_t = end_t;
		}

}
