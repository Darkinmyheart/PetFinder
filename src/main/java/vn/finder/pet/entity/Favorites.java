package vn.finder.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "favorites")
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorites_id")
    private Long id;

    @Column(name = "favorites_date", nullable = false)
    private Date likeDate;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private Users users;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "animals_id", nullable = false)
    private Animals animals;

    public Favorites(Long id, Date likeDate, Users users, Animals animals) {
        this.id = id;
        this.likeDate = likeDate;
        this.users = users;
        this.animals = animals;
    }

    public Favorites() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(Date likeDate) {
        this.likeDate = likeDate;
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
