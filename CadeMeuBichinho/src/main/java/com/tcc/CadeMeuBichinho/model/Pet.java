package com.tcc.CadeMeuBichinho.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table; 

import com.fasterxml.jackson.annotation.JsonIgnore; 

@Entity
@Table(name = "Pet")
public class Pet {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	private Type type;
	private Sex sex;
	private Size size;
	private FurColor furColor;
	private LifeStages lifeStage;
	@Lob
	private byte[] photo;
	private Date date; 
	private String description;
	private Double latitude;
	private Double longitude;
	private Integer phone;
	private Boolean phoneWithWhats;
	private Boolean lostPet;
	private Boolean remove;
	private RemovalReason removalReason;
	@ManyToOne
	@JsonIgnore
	private User user;

	public Pet() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public FurColor getFurColor() {
		return furColor;
	}

	public void setFurColor(FurColor furColor) {
		this.furColor = furColor;
	}

	public LifeStages getLifeStage() {
		return lifeStage;
	}

	public void setLifeStage(LifeStages lifeStage) {
		this.lifeStage = lifeStage;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Integer getPhone() {
		return phone;
	}

	public void setPhone(Integer phone) {
		this.phone = phone;
	}

	public Boolean getPhoneWithWhats() {
		return phoneWithWhats;
	}

	public void setPhoneWithWhats(Boolean phoneWithWhats) {
		this.phoneWithWhats = phoneWithWhats;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getLostPet() {
		return lostPet;
	}

	public void setLostPet(Boolean lostPet) {
		this.lostPet = lostPet;
	}

	public Boolean getRemove() {
		return remove;
	}

	public void setRemove(Boolean remove) {
		this.remove = remove;
	}
	
	public RemovalReason getRemovalReason() {
		return removalReason;
	}

	public void setRemovalReason(RemovalReason removalReason) {
		this.removalReason = removalReason;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



	public enum Type {
		DOG,CAT
	} 
	public enum Sex {
		MALE,FEMALE 
	} 
	public enum Size {
		P,M,G,GG	
	} 

	public enum FurColor {
		WHITE,DARK,WHITEANDDARK,CARAMEL,GRAY,STRIPED,WITHPOLKADOTS,ORANGE
	} 

	public enum LifeStages {
		PUPPY,ADULT,OLD
	} 
	
	public enum RemovalReason {
		FIND,LEAVESYSTEM 
	} 
	

}
