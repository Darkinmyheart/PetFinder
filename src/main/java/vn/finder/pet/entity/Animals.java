package vn.finder.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "animals")
@JsonIgnoreProperties({"breed", "shelters", "listFavorites", "listAdopt"})
public class Animals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animals_id")
    private Long id;

    @Column(name = "animal_avatar", nullable = false)
    private String animalAvatar;

    @Column(name = "animal_name", nullable = false, length = 50)
    private String animalName;

    @Column(name = "animal_age", nullable = false)
    private String animalAge;

    @Column(name = "animal_size", nullable = false)
    private String animalSize;

    @Column(name = "animal_gender", nullable = false)
    private Boolean animalGender;

    @Column(name = "animal_date", nullable = false)
    private Date animalDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "breed_id", nullable = false)
    private Breed breed;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelters shelters;

    @OneToOne(mappedBy = "animals", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AnimalInfo animalInfo;

    @OneToMany(mappedBy = "animals", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Avatar> listAvatar;

    @OneToMany(mappedBy = "animals", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorites> listFavorites;

    @OneToMany(mappedBy = "animals", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Adopt> listAdopt;

    public Animals(Long id, String animalAvatar, String animalName, String animalAge, String animalSize, Boolean animalGender, Date animalDate, Breed breed, Shelters shelters) {
        this.id = id;
        this.animalAvatar = animalAvatar;
        this.animalName = animalName;
        this.animalAge = animalAge;
        this.animalSize = animalSize;
        this.animalGender = animalGender;
        this.animalDate = animalDate;
        this.breed = breed;
        this.shelters = shelters;
    }

    public Animals() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnimalAvatar() {
        return animalAvatar;
    }

    public void setAnimalAvatar(String animalAvatar) {
        this.animalAvatar = animalAvatar;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getAnimalAge() {
        return animalAge;
    }

    public void setAnimalAge(String animalAge) {
        this.animalAge = animalAge;
    }

    public String getAnimalSize() {
        return animalSize;
    }

    public void setAnimalSize(String animalSize) {
        this.animalSize = animalSize;
    }

    public Boolean getAnimalGender() {
        return animalGender;
    }

    public void setAnimalGender(Boolean animalGender) {
        this.animalGender = animalGender;
    }

    public Date getAnimalDate() {
        return animalDate;
    }

    public void setAnimalDate(Date animalDate) {
        this.animalDate = animalDate;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Shelters getShelters() {
        return shelters;
    }

    public void setShelters(Shelters shelters) {
        this.shelters = shelters;
    }

    public List<Favorites> getListFavorites() {
        return listFavorites;
    }

    public void setListFavorites(List<Favorites> listFavorites) {
        this.listFavorites = listFavorites;
    }

    public List<Adopt> getListAdopt() {
        return listAdopt;
    }

    public void setListAdopt(List<Adopt> listAdopt) {
        this.listAdopt = listAdopt;
    }

    public List<Avatar> getListAvatar() {
        return listAvatar;
    }

    public void setListAvatar(List<Avatar> listAvatar) {
        this.listAvatar = listAvatar;
    }

    public AnimalInfo getAnimalInfo() {
        return animalInfo;
    }

    public void setAnimalInfo(AnimalInfo animalInfo) {
        this.animalInfo = animalInfo;
    }

    @Override
    public String toString() {
        return "Animals{" +
                "id=" + id +
                ", animalAvatar='" + animalAvatar + '\'' +
                ", animalName='" + animalName + '\'' +
                ", animalAge='" + animalAge + '\'' +
                ", animalSize='" + animalSize + '\'' +
                ", animalGender=" + animalGender +
                ", animalDate=" + animalDate +
                ", breed=" + breed +
                ", shelters=" + shelters +
                '}';
    }
}
