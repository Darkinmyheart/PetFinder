package vn.finder.pet.dto;

import java.util.List;

public class AnimalInfoRequest {
    private String location;
    private String breed;
    private String name;
    private String size;
    private List<String> age;
    private List<Boolean> gender;
    private int pageNumber;
    private int sizePage;
    private List<String> breedType;

    public AnimalInfoRequest(String location, String breed, String name, String size, List<String> age, List<Boolean> gender, int pageNumber, int sizePage, List<String> breedType) {
        this.location = location;
        this.breed = breed;
        this.name = name;
        this.size = size;
        this.age = age;
        this.gender = gender;
        this.pageNumber = pageNumber;
        this.sizePage = sizePage;
        this.breedType = breedType;
    }

    public AnimalInfoRequest() {

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<String> getAge() {
        return age;
    }

    public void setAge(List<String> age) {
        this.age = age;
    }

    public List<Boolean> getGender() {
        return gender;
    }

    public void setGender(List<Boolean> gender) {
        this.gender = gender;
    }

    public String getListAge(){
        String s = "[";
        for (int i = 0; i < this.getAge().size(); i++){
            s += this.getAge().get(i) + ", ";
        }
        s = s.trim().substring(0, s.length() - 2) + "]";
        return s;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getSizePage() {
        return sizePage;
    }

    public void setSizePage(int sizePage) {
        this.sizePage = sizePage;
    }

    public List<String> getBreedType() {
        return breedType;
    }

    public void setBreedType(List<String> breedType) {
        this.breedType = breedType;
    }

    @Override
    public String toString() {
        return "AnimalInfoRequest{" +
                "location='" + location + '\'' +
                ", breed='" + breed + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", pageNumber=" + pageNumber +
                ", sizePage=" + sizePage +
                '}';
    }
}
