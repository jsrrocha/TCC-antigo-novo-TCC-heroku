package com.tcc.CadeMeuBichinho.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Comment")
public class Comment {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	@ManyToOne
	private Pet pet;
	@ManyToOne
	@JsonIgnore
	private User userReceived;
	@ManyToOne
	@JsonIgnore
	private User userSend;
	private String comment;
	private Boolean notificationActive;

	public Comment() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public User getUserReceived() {
		return userReceived;
	}

	public void setUserReceived(User userReceived) {
		this.userReceived = userReceived;
	}

	public User getUserSend() {
		return userSend;
	}

	public void setUserSend(User userSend) {
		this.userSend = userSend;
	}

	public Boolean getNotificationActive() {
		return notificationActive;
	}

	public void setNotificationActive(Boolean notificationActive) {
		this.notificationActive = notificationActive;
	}
}
