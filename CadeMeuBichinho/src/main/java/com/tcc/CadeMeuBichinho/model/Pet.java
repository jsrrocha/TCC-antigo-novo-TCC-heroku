package com.tcc.CadeMeuBichinho.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table; 
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore; 

@Entity
@Table(name = "Pet")
public class Pet {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	private String name;
	private Specie specie;
	private Sex sex;
	private FurColor furColor;
	private LifeStage lifeStage;
	@Lob
	private byte[] photo;
	private String photoName;
	private Date date; 
        @Size(min = 0, max = 500)
	private String description;
	private Double latitude;
	private Double longitude;
	private Long phone;
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Specie getSpecie() {
		return specie;
	}

	public void setSpecie(Specie specie) {
		this.specie = specie;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public FurColor getFurColor() {
		return furColor;
	}

	public void setFurColor(FurColor furColor) {
		this.furColor = furColor;
	}
	
	public LifeStage getLifeStage() {
		return lifeStage;
	}

	public void setLifeStage(LifeStage lifeStage) {
		this.lifeStage = lifeStage;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
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

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
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


	public enum Specie {
		DOG, CAT;

		@Override
		public String toString() {
			switch (this) {
			case DOG:
				return "Cachorro";
			case CAT:
				return "Gato";
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	public enum Sex {
		MALE, FEMALE;

		@Override
		public String toString() {
			switch (this) {
			case MALE:
				return "Macho";
			case FEMALE:
				return "Fêmea";
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	public enum FurColor {
		WHITE, DARK, WHITEANDDARK, CARAMEL, GRAY, STRIPED, WITHPOLKADOTS, ORANGE;

		@Override
		public String toString() {
			switch (this) {
			case WHITE:
				return "Branco";
			case DARK:
				return "Preto";
			case WHITEANDDARK:
				return "Preto e branco";
			case CARAMEL:
				return "Marrom/Caramelo";
			case GRAY:
				return "Cinza";
			case STRIPED:
				return "Malhado/Listrado";
			case WITHPOLKADOTS:
				return "Com bolinhas/manchinhas";
			case ORANGE:
				return "Laranja";
			default:
				throw new IllegalArgumentException();
			}
		}
	}


	public enum LifeStage {
		PUPPY, ADULT, OLD;

		@Override
		public String toString() {
			switch (this) {
			case PUPPY:
				return "Filhote";
			case ADULT:
				return "Adulto";
			case OLD:
				return "Idoso";
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	public enum RemovalReason {
		FIND, LEAVESYSTEM;
		@Override
		public String toString() {
			switch (this) {
			case FIND:
				return "Já está com os donos";
			case LEAVESYSTEM:
				return "Retirar do sistema";
			default:
				throw new IllegalArgumentException();
			}
		}
	}
}
