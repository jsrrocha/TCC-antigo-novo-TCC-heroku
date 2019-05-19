package com.tcc.CadeMeuBichinho.model.auth;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oauthRefreshToken")
public class OauthRefreshToken {

	@Id
	public String tokenId;
	public byte[] authentication;
	public byte[] token;
	
	public OauthRefreshToken() {
		
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public byte[] getAuthentication() {
		return authentication;
	}
	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}
	public byte[] getToken() {
		return token;
	}
	public void setToken(byte[] token) {
		this.token = token; 
	}
	
	
}
