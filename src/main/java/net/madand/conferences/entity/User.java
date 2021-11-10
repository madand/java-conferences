package net.madand.conferences.entity;

import net.madand.conferences.auth.Role;

import java.io.Serializable;
import java.time.Instant;

public class User implements IDable, Serializable {
    private static final long serialVersionUID = 7877556371955180148L;

    private int id;
    private Instant createdAt;
    private Instant updatedAt;
    private String email;
    private String realName;
    private String passwordHash;
    private Role role;

    public User() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
