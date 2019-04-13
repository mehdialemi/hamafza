package ir.co.realtime.disaster.auth.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated
    private PrivilegeType privilegeType;

    @ManyToMany(mappedBy = "privileges")
    private Collection <Role> roles;

    public Privilege() {
        super();
    }

    public Privilege(final PrivilegeType privilegeType) {
        super();
        this.privilegeType = privilegeType;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public PrivilegeType getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(PrivilegeType privilegeType) {
        this.privilegeType = privilegeType;
    }

    public Collection <Role> getRoles() {
        return roles;
    }

    public void setRoles(final Collection <Role> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((privilegeType == null) ? 0 : privilegeType.hashCode());
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
        Privilege other = (Privilege) obj;
        if (privilegeType == null) {
            if (other.privilegeType != null)
                return false;
        } else if (!privilegeType.equals(other.privilegeType))
            return false;
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Privilege [privilegeType=").append(privilegeType).append("]").append("[id=").append(id).append("]");
        return builder.toString();
    }
}