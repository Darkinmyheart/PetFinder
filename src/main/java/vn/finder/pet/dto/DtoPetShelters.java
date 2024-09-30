package vn.finder.pet.dto;


import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

public class DtoPetShelters{
    private String username;
    private String breed_type;
    private String breed_name;
    private String animalName;
    private String animalSize;
    private String animalAge;
    private boolean animalGender;
    private Date animalDate;
    private String animalAvatar;
    private List<String> listAvatar;
    private String animal_info_harmony;
    private String animal_info_health;
    private String animal_info_leg;
    private String animal_info_description;
    private String animal_info_color;
    private String animal_info_characteristics;

    public DtoPetShelters(){

    }

    @Override
    public String toString() {
        return "DtoPetShelters{" +
                "username='" + username + '\'' +
                ", breed_type='" + breed_type + '\'' +
                ", breed_name='" + breed_name + '\'' +
                ", animalName='" + animalName + '\'' +
                ", animalSize='" + animalSize + '\'' +
                ", animalAge='" + animalAge + '\'' +
                ", animalGender=" + animalGender +
                ", animalDate=" + animalDate +
                ", animalAvatar='" + animalAvatar + '\'' +
                ", listAvatar='" + listAvatar + '\'' +
                ", animal_info_harmony='" + animal_info_harmony + '\'' +
                ", animal_info_health='" + animal_info_health + '\'' +
                ", animal_info_leg='" + animal_info_leg + '\'' +
                ", animal_info_description=" + animal_info_description +
                ", animal_info_color='" + animal_info_color + '\'' +
                ", animal_info_characteristics='" + animal_info_characteristics + '\'' +
                '}';
    }

    public DtoPetShelters(String username, String breed_type, String breed_name, String animalName, String animalSize, String animalAge, boolean animalGender, Date animalDate, String animalAvatar, List<String> listAvatar, String animal_info_harmony, String animal_info_health, String animal_info_leg, String animal_info_description, String animal_info_color, String animal_info_characteristics) {
        this.username = username;
        this.breed_type = breed_type;
        this.breed_name = breed_name;
        this.animalName = animalName;
        this.animalSize = animalSize;
        this.animalAge = animalAge;
        this.animalGender = animalGender;
        this.animalDate = animalDate;
        this.animalAvatar = animalAvatar;
        this.listAvatar = listAvatar;
        this.animal_info_harmony = animal_info_harmony;
        this.animal_info_health = animal_info_health;
        this.animal_info_leg = animal_info_leg;
        this.animal_info_description = animal_info_description;
        this.animal_info_color = animal_info_color;
        this.animal_info_characteristics = animal_info_characteristics;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBreed_type() {
        return breed_type;
    }

    public void setBreed_type(String breed_type) {
        this.breed_type = breed_type;
    }

    public String getBreed_name() {
        return breed_name;
    }

    public void setBreed_name(String breed_name) {
        this.breed_name = breed_name;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getAnimalSize() {
        return animalSize;
    }

    public void setAnimalSize(String animalSize) {
        this.animalSize = animalSize;
    }

    public String getAnimalAge() {
        return animalAge;
    }

    public void setAnimalAge(String animalAge) {
        this.animalAge = animalAge;
    }

    public boolean isAnimalGender() {
        return animalGender;
    }

    public void setAnimalGender(boolean animalGender) {
        this.animalGender = animalGender;
    }

    public Date getAnimalDate() {
        return animalDate;
    }

    public void setAnimalDate(Date animalDate) {
        this.animalDate = animalDate;
    }

    public String getAnimalAvatar() {
        return animalAvatar;
    }

    public void setAnimalAvatar(String animalAvatar) {
        this.animalAvatar = animalAvatar;
    }

    public List<String> getListAvatar() {
        return listAvatar;
    }

    public void setListAvatar(List<String> listAvatar) {
        this.listAvatar = listAvatar;
    }

    public String getAnimal_info_harmony() {
        return animal_info_harmony;
    }

    public void setAnimal_info_harmony(String animal_info_harmony) {
        this.animal_info_harmony = animal_info_harmony;
    }

    public String getAnimal_info_health() {
        return animal_info_health;
    }

    public void setAnimal_info_health(String animal_info_health) {
        this.animal_info_health = animal_info_health;
    }

    public String getAnimal_info_leg() {
        return animal_info_leg;
    }

    public void setAnimal_info_leg(String animal_info_leg) {
        this.animal_info_leg = animal_info_leg;
    }

    public String getAnimal_info_description() {
        return animal_info_description;
    }

    public void setAnimal_info_description(String animal_info_description) {
        this.animal_info_description = animal_info_description;
    }

    public String getAnimal_info_color() {
        return animal_info_color;
    }

    public void setAnimal_info_color(String animal_info_color) {
        this.animal_info_color = animal_info_color;
    }

    public String getAnimal_info_characteristics() {
        return animal_info_characteristics;
    }

    public void setAnimal_info_characteristics(String animal_info_characteristics) {
        this.animal_info_characteristics = animal_info_characteristics;
    }
}
