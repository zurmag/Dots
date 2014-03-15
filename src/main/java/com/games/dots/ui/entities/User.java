package com.games.dots.ui.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	public String id;
	public UserType userType;	
	public String color;
	public String avatarUrl;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)){
			return false;
		}
		
		if (userType == null) {
			if (other.userType != null)
				return false;
		} else if (!id.equals(other.userType)){
			return false;
		}
		
		
		return true;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
		
	}
	
}
