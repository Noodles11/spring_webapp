package net.atos.spring_webapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Byte permissionId;
    @Column(name = "role_name")
    private String roleName;

    public Permission(String roleName) {
        this.roleName = roleName;
    }
}
