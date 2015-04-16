/* 
 * Copyright (c) 2013 Matrix wifi
 */
package net.wyun.wm.domain.account;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.wyun.wm.domain.MacAccount;
import net.wyun.wm.domain.Permission;
import net.wyun.wm.domain.role.Role;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "account")
@JsonIgnoreProperties({"id", "password", "factor", "sent_to_server", "enabled", "macAccounts"})
public class Account implements UserDetails {
	public static final Account ACCOUNT = new Account("anonymous");
	
    public Account() { }
	
	public Account(String phone) { 
		this.phone = phone; 
		this.create_t = new Date();
	}
	
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name= "account_id", columnDefinition = "VARCHAR(36)")
    @Id
	private String id;         //      VARCHAR(36)     primary key,                       #   id int unsigned NOT NULL auto_increment primary key, the same user can have differennt account at different ihost
   
	private String phone;      //            varchar(30)     DEFAULT NULL,	                    #	常用电话号码 , mobile phone number for receiving sms., one account --> one phone number
    private String password = "";  //         varchar(20)     DEFAULT ''                         #   password use for secure login, optional, try mac/password login first. For internal users
    private int point = 0;  //            int not null    DEFAULT '0',	                    #	userid下的积分 ?? better name
    private int factor = 1000; //           int not null    DEFAULT '1000',	                #	points转integral的因子，1000代表1 // put it together with integral in a separate table
    private String originator;   //       varchar(36)     DEFAULT ''                         #   id of the agnet who creates this account
    private String hint = "";   //             varchar(30)     NOT NULL,                          #   required for recover or change account, for example change phone number
    
    @ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="user_info_id", unique= true, nullable=true, insertable=true, updatable=true)
    private UserInfo userInfo; //    VARCHAR(36)     NOT NULL DEFAULT '',               #   foreign key to user_info table.
    
    private boolean sent_to_server = false; //   boolean         NOT NULL DEFAULT 0,                #   ?? flag for syn with iserver
    private boolean enabled = true; //          boolean         NOT NULL default 0,                #   for Admin usage
    private Date create_t; //         datetime        DEFAULT NULL,	                    #	记录时间
    private Date modify_t; //         datetime        DEFAULT NULL,	                    #	记录更新时间
	
   
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "point")
	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	@Column(name = "factor")
	public int getFactor() {
		return factor;
	}

	public void setFactor(int factor) {
		this.factor = factor;
	}

	@Column(name = "originator")
	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	@Column(name= "hint")
	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@Column(name="sent_to_server")
	public boolean isSent_to_server() {
		return sent_to_server;
	}

	public void setSent_to_server(boolean sent_to_server) {
		this.sent_to_server = sent_to_server;
	}

	@Column(name="create_t")
	public Date getCreate_t() {
		return create_t;
	}

	public void setCreate_t(Date create_t) {
		this.create_t = create_t;
	}

	@Column(name="modify_t")
	public Date getModify_t() {
		return modify_t;
	}

	public void setModify_t(Date modify_t) {
		this.modify_t = modify_t;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public static Account getAccount() {
		return ACCOUNT;
	}
	
	//mac <-- --> account mapping 
	@OneToMany(mappedBy = "account")
	private Set<MacAccount> macAccounts = new HashSet<MacAccount>();
	
	public Set<MacAccount> getMacAccounts() {
		return macAccounts;
	}

	public void setMacAccounts(Set<MacAccount> macAccounts) {
		this.macAccounts = macAccounts;
	}

	
	//=============  below is for userdetails =======================//
	@Transient
	public boolean isAccountNonExpired() { return true; }

	@Transient
	public boolean isAccountNonLocked() { return true; }


	@Transient
	public boolean isCredentialsNonExpired() { return true; }

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "account_role",
		joinColumns = { @JoinColumn(name = "account_id") },
		inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<Role>();
	public Set<Role> getRoles() { return roles; }
	
	public void setRoles(Set<Role> roles) { this.roles = roles; }
	
	@Transient
	public void addRole(Role r){
		roles.add(r);
	}
	
	
	@Transient
	public Set<Permission> getPermissions() {
		Set<Permission> perms = new HashSet<Permission>();
		for (Role role : roles) { perms.addAll(role.getPermissions()); }
		return perms;
	}


	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.addAll(getRoles());
		authorities.addAll(getPermissions());
		return authorities;
	}
	
	/**
	 * <p>
	 * Returns the phone of the account.
	 * </p>
	 */
	public String toString() { return phone; }

	@Transient
	@Override
	public String getUsername() {
		return this.phone;
	}
}
