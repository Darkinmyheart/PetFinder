package vn.finder.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "adopter_profile")
@JsonIgnoreProperties({"users"})
public class AdopterProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(name = "profile_desired", nullable = false, length = 50)
    private String desired;

    @Column(name = "profile_experience", nullable = false)
    private Boolean experience;

    @Column(name = "profile_pets_at_home", nullable = false, length = 50)
    private String petsAtHome;

    @Column(name = "profile_outdoor", nullable = false)
    private Boolean outdoor;

    @Column(name = "profile_age_of_pet", nullable = false)
    private String ageOfPet;

    @Column(name = "profile_gender_of_pet", nullable = false)
    private Boolean genderOfPet;

    @Column(name = "profile_size_of_pet", nullable = false)
    private String sizeOfPet;

    @Column(name = "profile_breed_of_pet", nullable = false)
    private String breedOfPet;

    @Column(name = "profile_need", nullable = false)
    private String need;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "username", nullable = false)
    private Users users;

    public AdopterProfile(Long id, String desired, Boolean experience, String petsAtHome, Boolean outdoor, String ageOfPet, Boolean genderOfPet, String sizeOfPet, String breedOfPet, String need, Users users) {
        this.id = id;
        this.desired = desired;
        this.experience = experience;
        this.petsAtHome = petsAtHome;
        this.outdoor = outdoor;
        this.ageOfPet = ageOfPet;
        this.genderOfPet = genderOfPet;
        this.sizeOfPet = sizeOfPet;
        this.breedOfPet = breedOfPet;
        this.need = need;
        this.users = users;
    }

    public AdopterProfile() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesired() {
        return desired;
    }

    public void setDesired(String desired) {
        this.desired = desired;
    }

    public Boolean getExperience() {
        return experience;
    }

    public void setExperience(Boolean experience) {
        this.experience = experience;
    }

    public String getPetsAtHome() {
        return petsAtHome;
    }

    public void setPetsAtHome(String petsAtHome) {
        this.petsAtHome = petsAtHome;
    }

    public Boolean getOutdoor() {
        return outdoor;
    }

    public void setOutdoor(Boolean outdoor) {
        this.outdoor = outdoor;
    }

    public String getAgeOfPet() {
        return ageOfPet;
    }

    public void setAgeOfPet(String ageOfPet) {
        this.ageOfPet = ageOfPet;
    }

    public Boolean getGenderOfPet() {
        return genderOfPet;
    }

    public void setGenderOfPet(Boolean genderOfPet) {
        this.genderOfPet = genderOfPet;
    }

    public String getSizeOfPet() {
        return sizeOfPet;
    }

    public void setSizeOfPet(String sizeOfPet) {
        this.sizeOfPet = sizeOfPet;
    }

    public String getBreedOfPet() {
        return breedOfPet;
    }

    public void setBreedOfPet(String breedOfPet) {
        this.breedOfPet = breedOfPet;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "AdopterProfile{" +
                "id=" + id +
                ", desired='" + desired + '\'' +
                ", experience=" + experience +
                ", petsAtHome='" + petsAtHome + '\'' +
                ", outdoor=" + outdoor +
                ", ageOfPet='" + ageOfPet + '\'' +
                ", genderOfPet=" + genderOfPet +
                ", sizeOfPet='" + sizeOfPet + '\'' +
                ", breedOfPet='" + breedOfPet + '\'' +
                ", need='" + need + '\'' +
                ", users=" + users +
                '}';
    }
}
