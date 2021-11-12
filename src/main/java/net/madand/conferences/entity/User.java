package net.madand.conferences.entity;

import net.madand.conferences.auth.Role;
import net.madand.conferences.security.PasswordHelper;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 7877556371955180148L;

    private int id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String email;
    private String realName;
    private String passwordHash;
    private Role role;

    public User() {
    }

    public static User makeInstance(String email, String realName, String password, Role role) {
        User user = new User();

        user.setEmail(email);
        user.setRealName(realName);
        user.setPassword(password);
        user.setRole(role);

        return user;
    }

    /**
     * Set the passwordHash from the given string.
     *
     * @param newPassword the new password in clear text.
     */
    public void setPassword(String newPassword) {
        passwordHash = PasswordHelper.hash(newPassword);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", realName='" + realName + '\'' +
                ", role=" + role +
                '}';
    }
}
