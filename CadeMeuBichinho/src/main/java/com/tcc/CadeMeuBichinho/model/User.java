package com.tcc.CadeMeuBichinho.model;


import java.security.NoSuchAlgorithmException;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Users")
public class User {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	private String name;
	@Column(unique = true,name = "email")
	@Email
	private String email; 
	@Size(min = 0, max = 500)
	private String password;
	private Integer phone;
	private Boolean phoneWithWhats;
	@JsonIgnore
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
	private List<Pet> pets;
	@JsonIgnore
	@OneToMany(mappedBy = "userSend",cascade = CascadeType.ALL) 
	private List<Comment> messagesSend;
	@JsonIgnore
	@OneToMany(mappedBy = "userReceived",cascade = CascadeType.ALL)
	private List<Comment> messagesReceived;
	private Boolean active;
	
	
	public User() {
	}
	

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		
		return password;
	}

	public void setPassword(String password) throws NoSuchAlgorithmException {
		this.password = password;
	}

	
	public Integer getPhone() {
		return phone;
	}
	
	public void setPhone(Integer phone) {
		this.phone = phone;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

	public Boolean getPhoneWithWhats() {
		return phoneWithWhats;
	}


	public void setPhoneWithWhats(Boolean phoneWithWhats) {
		this.phoneWithWhats = phoneWithWhats;
	}


	public List<Pet> getPets() {
		return pets;
	}


	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}


	public List<Comment> getMessagesSend() {
		return messagesSend;
	}


	public void setMessagesSend(List<Comment> messagesSend) {
		this.messagesSend = messagesSend;
	}


	public List<Comment> getMessagesReceived() {
		return messagesReceived;
	}


	public void setMessagesReceived(List<Comment> messagesReceived) {
		this.messagesReceived = messagesReceived;
	}
	
	public Boolean getActive() {
		return active;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}

}
