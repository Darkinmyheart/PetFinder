package vn.finder.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "avatar")
@JsonIgnoreProperties({"animals"})
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avatar_id")
    private Long id;

    @Column(name = "avatar_name", nullable = false)
    private String avatar_name;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "animals_id", nullable = false)
    private Animals animals;

    public Avatar(Long id, String avatar_name, Animals animals) {
        this.id = id;
        this.avatar_name = avatar_name;
        this.animals = animals;
    }

    public Avatar() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar_name() {
        return avatar_name;
    }

    public void setAvatar_name(String avatar_name) {
        this.avatar_name = avatar_name;
    }

    public Animals getAnimals() {
        return animals;
    }

    public void setAnimals(Animals animals) {
        this.animals = animals;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", avatar_name='" + avatar_name + '\'' +
                ", animals=" + animals +
                '}';
    }
}
