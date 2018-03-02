package com.utmz.centreon.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User{
	
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private String login;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String ipCentreon;
	@Column(nullable = false)
	private String loginCentreon;
	@Column(nullable = false)
	private String passwordCentreon;
	@Column(nullable = false)
	private String tokenPhoneId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIpCentreon() {
		return ipCentreon;
	}
	public void setIpCentreon(String ipCentreon) {
		this.ipCentreon = ipCentreon;
	}
	public String getLoginCentreon() {
		return loginCentreon;
	}
	public void setLoginCentreon(String loginCentreon) {
		this.loginCentreon = loginCentreon;
	}
	public String getPasswordCentreon() {
		return passwordCentreon;
	}
	public void setPasswordCentreon(String passwordCentreon) {
		this.passwordCentreon = passwordCentreon;
	}
	public String getTokenPhoneId() {
		return tokenPhoneId;
	}
	public void setTokenPhoneId(String tokenPhoneId) {
		this.tokenPhoneId = tokenPhoneId;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(Long id, String login, String password, String ipCentreon, String loginCentreon,
			String passwordCentreon, String tokenPhoneId) {
		super();
		this.id = id;
		this.login = login;
		this.password = password;
		this.ipCentreon = ipCentreon;
		this.loginCentreon = loginCentreon;
		this.passwordCentreon = passwordCentreon;
		this.tokenPhoneId = tokenPhoneId;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password + ", ipCentreon=" + ipCentreon
				+ ", loginCentreon=" + loginCentreon + ", passwordCentreon=" + passwordCentreon + ", tokenPhoneId="
				+ tokenPhoneId + "]";
	}


	
	
	
	
	
}
