package com.raqamiEcommerce.EcommerceShop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String roleName;
    public Role(String name) {
        this.roleName=name;
    }
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users=new HashSet<>();
}
