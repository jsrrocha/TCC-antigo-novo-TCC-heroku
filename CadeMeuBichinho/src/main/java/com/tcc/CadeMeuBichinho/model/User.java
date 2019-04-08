package com.tcc.CadeMeuBichinho.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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

@Entity
@Table(name = "Users")
public class User {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	private String name;
	@Column(unique = true,name = "email")
	@Email
	private String email; //user desativado com email?
	private String password;
	private Integer phone;
	private Boolean phoneWithWhats;
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
	private List<Pet> pets;
	@OneToMany(mappedBy = "userSend",cascade = CascadeType.ALL) 
	private List<Comment> messagesSend;
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
		String codedPassword = encryptPasswordInSha(password);
		this.password = codedPassword;
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
	
    public String encryptPasswordInSha(String password) throws NoSuchAlgorithmException {
    	MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.update(password.getBytes());
		byte[] bytes = digest.digest();
		
		String codedPassword = Base64.getEncoder().encodeToString(bytes);
		System.out.println("Senha SHA1 set: " + codedPassword);
    	
    	return codedPassword;
    }


	public Boolean getActive() {
		return active;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}

}
