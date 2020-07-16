package com.hdntec.gestao.web.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * <P>
 * <B>Description :</B><BR>
 * The profile class includes a set of permissions that define the degree of
 * access of a user to a system characterized by a name and description
 * </P>
 * <P>
 * 
 * @author <a href="mailto:contato@improvess.com">improvess</a>
 * @since 28/04/2009
 * @version $Revision: 1.0 $
 */

@XmlType(name = "profile", propOrder = { "description", "id", "name",
		"permissions" })
public class Profile implements Serializable {

	private static final long serialVersionUID = -7888979055396003833L;

	private Long id;

	private String name;

	private String description;

	@XmlElement(nillable = true)
	private List<Permission> permissions;

	public Profile() {
		super();
		setPermissions(new ArrayList<Permission>());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null) {
			this.name = name.trim();
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description != null) {
			this.description = description.trim();
		}
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	@XmlTransient
	public void setPermissions(List<Permission> permission) {
		this.permissions = permission;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

		if (object instanceof Profile) {
			Profile profile = (Profile) object;
			if (this.name != null) {
				returnValue = this.name.equalsIgnoreCase(profile.name);
			}
		}
		return returnValue;
	}

}
