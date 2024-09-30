package vn.finder.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "animal_info")
@JsonIgnoreProperties({"animals"})
public class AnimalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_info_id")
    private Long id;

    @Column(name = "animal_info_leg", nullable = false, length = 20)
    private String animalInfoLeg;

    @Column(name = "animal_info_color", nullable = false)
    private String animalInfoColor;

    @Column(name = "animal_info_characteristics", nullable = false, columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String animalInfoCharacteristics;

    @Column(name = "animal_info_health", nullable = false, columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String animalInfoHealth;

    @Column(name = "animal_info_harmony", nullable = false, columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String animalInfoHarmony;

    @Column(name = "animal_info_description", nullable = false, columnDefinition = "LONGTEXT")
    private String animalInfoDescription;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animals animals;

    public AnimalInfo(Long id, String animalInfoLeg, String animalInfoColor, String animalInfoCharacteristics, String animalInfoHealth, String animalInfoHarmony, String animalInfoDescription, Animals animals) {
        this.id = id;
        this.animalInfoLeg = animalInfoLeg;
        this.animalInfoColor = animalInfoColor;
        this.animalInfoCharacteristics = animalInfoCharacteristics;
        this.animalInfoHealth = animalInfoHealth;
        this.animalInfoHarmony = animalInfoHarmony;
        this.animalInfoDescription = animalInfoDescription;
        this.animals = animals;
    }

    public AnimalInfo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnimalInfoLeg() {
        return animalInfoLeg;
    }

    public void setAnimalInfoLeg(String animalInfoLeg) {
        this.animalInfoLeg = animalInfoLeg;
    }

    public String getAnimalInfoColor() {
        return animalInfoColor;
    }

    public void setAnimalInfoColor(String animalInfoColor) {
        this.animalInfoColor = animalInfoColor;
    }

    public String getAnimalInfoCharacteristics() {
        return animalInfoCharacteristics;
    }

    public void setAnimalInfoCharacteristics(String animalInfoCharacteristics) {
        this.animalInfoCharacteristics = animalInfoCharacteristics;
    }

    public String getAnimalInfoHealth() {
        return animalInfoHealth;
    }

    public void setAnimalInfoHealth(String animalInfoHealth) {
        this.animalInfoHealth = animalInfoHealth;
    }

    public String getAnimalInfoHarmony() {
        return animalInfoHarmony;
    }

    public void setAnimalInfoHarmony(String animalInfoHarmony) {
        this.animalInfoHarmony = animalInfoHarmony;
    }

    public String getAnimalInfoDescription() {
        return animalInfoDescription;
    }

    public void setAnimalInfoDescription(String animalInfoDescription) {
        this.animalInfoDescription = animalInfoDescription;
    }

    public Animals getAnimals() {
        return animals;
    }

    public void setAnimals(Animals animals) {
        this.animals = animals;
    }

    @Override
    public String toString() {
        return "AnimalInfo{" +
                "id=" + id +
                ", animalInfoLeg='" + animalInfoLeg + '\'' +
                ", animalInfoColor='" + animalInfoColor + '\'' +
                ", animalInfoCharacteristics='" + animalInfoCharacteristics + '\'' +
                ", animalInfoHealth='" + animalInfoHealth + '\'' +
                ", animalInfoHarmony='" + animalInfoHarmony + '\'' +
                ", animalInfoDescription='" + animalInfoDescription + '\'' +
                ", animals=" + animals +
                '}';
    }
}
