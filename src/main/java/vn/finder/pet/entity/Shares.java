package vn.finder.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "shares")
@JsonIgnoreProperties({"users", "animals"})
public class Shares {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shares_id")
    private Long id;

    @Column(name = "shares_date", nullable = false)
    private Date likeDate;

    @Column(name = "shares_emailSendTo", nullable = false)
    private String emailSendTo;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private Users users;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "animals_id", nullable = false)
    private Animals animals;

    public Shares(Long id, Date likeDate, Users users, Animals animals, String emailSendTo) {
        this.id = id;
        this.likeDate = likeDate;
        this.users = users;
        this.animals = animals;
        this.emailSendTo = emailSendTo;
    }

    public Shares() {

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

    public Animals getAnimals() {
        return animals;
    }

    public void setAnimals(Animals animals) {
        this.animals = animals;
    }

    public Date getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(Date likeDate) {
        this.likeDate = likeDate;
    }

    public String getEmailSendTo() {
        return emailSendTo;
    }

    public void setEmailSendTo(String emailSendTo) {
        this.emailSendTo = emailSendTo;
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", likeDate=" + likeDate +
                ", users=" + users +
                ", animals=" + animals +
                '}';
    }
}
