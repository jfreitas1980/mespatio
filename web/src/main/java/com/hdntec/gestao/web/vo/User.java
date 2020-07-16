package com.hdntec.gestao.web.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * <P><B>Description :</B><BR>
 * The user class has data from a person who can access a system and how it can access through the definition of your profile
 * </P>
 * <P>
 * @author <a href="mailto:contato@improvess.com">improvess</a>
 * @since 28/04/2009
 * @version $Revision: 1.0 $
 */

@XmlType(name = "user", propOrder = {
    "email",
    "id",
    "login",
    "name",
    "nickname",
    "profile"
})
@XmlRootElement

public class User implements Serializable {

	private static final long serialVersionUID = -3928789719858753281L;

	private Long id;

	private String name;
	
	private String nickname;
	
	private String email;
	
	private String login;

	@XmlTransient
	private String password;

	@XmlTransient
	private String passwordRetype;
	
	@XmlTransient
	private String passwordCurrent;
	
	@XmlTransient
	public String getPasswordRetype() {
		return passwordRetype;
	}

	public void setPasswordRetype(String passwordRetype) {
		this.passwordRetype = passwordRetype;
	}
	
	@XmlTransient
	public String getPasswordCurrent() {
		return passwordCurrent;
	}

	public void setPasswordCurrent(String passwordCurrent) {
		this.passwordCurrent = passwordCurrent;
	}

	private Profile profile;

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
		if (name != null) {
			this.name = name.trim();
		}
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		if (nickname != null) {
			this.nickname = nickname.trim();
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email != null) {
			this.email = email.trim();
		}
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		if (login != null) {
			this.login = login.trim();
		}
	}

	@XmlTransient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public User() {
		super();
	}

	@Override
	public int hashCode() {
		int result = 0;
		if (this.name != null) {
			result = name.hashCode();
		}
		return result;
	}

	@Override
	public boolean equals(Object object) {
		boolean returnValue = false;

		if (object instanceof User) {
			User user = (User) object;
			if (this.login != null) {
				returnValue = this.login.equals(user.login);
			}
		}
		return returnValue;
	}

}
