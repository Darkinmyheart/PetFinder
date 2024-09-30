package vn.finder.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "spons")
@JsonIgnoreProperties({"users", "shelters"})
public class Spons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spons_id")
    private Long id;

    @Column(name = "spons_gift", nullable = false)
    private Double sponsGift;

    @Column(name = "spons_message", columnDefinition = "LONGTEXT")
    private String sponsMessage;

    @Column(name = "spons_date", nullable = false)
    private Date sponsDate;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private Users users;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelters shelters;

    public Spons(Long id, Double sponsGift, String sponsMessage, Date sponsDate, Users users, Shelters shelters) {
        this.id = id;
        this.sponsGift = sponsGift;
        this.sponsMessage = sponsMessage;
        this.sponsDate = sponsDate;
        this.users = users;
        this.shelters = shelters;
    }

    public Spons() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSponsGift() {
        return sponsGift;
    }

    public void setSponsGift(Double sponsGift) {
        this.sponsGift = sponsGift;
    }

    public String getSponsMessage() {
        return sponsMessage;
    }

    public void setSponsMessage(String sponsMessage) {
        this.sponsMessage = sponsMessage;
    }

    public Date getSponsDate() {
        return sponsDate;
    }

    public void setSponsDate(Date sponsDate) {
        this.sponsDate = sponsDate;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Shelters getShelters() {
        return shelters;
    }

    public void setShelters(Shelters shelters) {
        this.shelters = shelters;
    }

    @Override
    public String toString() {
        return "Spons{" +
                "id=" + id +
                ", sponsGift=" + sponsGift +
                ", sponsMessage='" + sponsMessage + '\'' +
                ", sponsDate=" + sponsDate +
                ", users=" + users +
                ", shelters=" + shelters +
                '}';
    }
}
