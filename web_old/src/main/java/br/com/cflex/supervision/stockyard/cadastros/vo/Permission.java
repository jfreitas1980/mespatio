
package br.com.cflex.supervision.stockyard.cadastros.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

/**
 * <P><B>Description :</B><BR>
 * The Permission class includes attributes that describe what are the characteristics of a specific permission to access a system 
 * </P>
 * <P>
 * @author <a href="mailto:contato@improvess.com">improvess</a>
 * @since 28/04/2009
 * @version $Revision: 1.0 $
 */

@XmlType(name = "permission", propOrder = {
    "description",
    "id",
    "name"
})

public class Permission implements Serializable {

    private static final long serialVersionUID = -631473041725593152L;

    private Long id;

    private String name;
    
    private String description;

    public Permission() {
        super();
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

        if (object instanceof Permission) {
            Permission permission = (Permission) object;
            if (this.name != null) {
                returnValue = this.name.equalsIgnoreCase(permission.name);
            }
        }
        return returnValue;
    }

}
