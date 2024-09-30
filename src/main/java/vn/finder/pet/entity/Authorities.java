package vn.finder.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "authorities",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "authority"})})
@JsonIgnoreProperties({"users"})
public class Authorities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "username", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Users users;

    @Column(name = "authority", nullable = false, length = 18)
    private String authority;

    public Authorities() {
    }

    public Authorities(Users users, String authority) {
        this.users = users;
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "Authorities{" +
                "user=" + users +
                ", authority='" + authority + '\'' +
                '}';
    }
}