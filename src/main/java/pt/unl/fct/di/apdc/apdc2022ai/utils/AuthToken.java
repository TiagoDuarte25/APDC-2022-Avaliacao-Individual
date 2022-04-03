package pt.unl.fct.di.apdc.apdc2022ai.utils;

import java.util.UUID;

public class AuthToken {
	
	public static final long EXPIRATION_TIME = 1000 * 60 * 10; // 10 min
	
	public String username;
	public String tokenID;
	public long creationData;
	public long expirationData;

	public AuthToken(String username) {
		this.username = username;
		this.tokenID = UUID.randomUUID().toString();
		this.creationData = System.currentTimeMillis();
		this.expirationData = this.creationData + AuthToken.EXPIRATION_TIME;
	}
	
	
}
