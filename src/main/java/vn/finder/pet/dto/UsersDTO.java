package vn.finder.pet.dto;

import java.util.Date;

public class UsersDTO {

    private String userName;
    private String firstName;
    private String lastName;
    private String country;
    private String address;
    private String phone;
    private String avatar;
    private String createdDate;

    // Constructors
    public UsersDTO() {
    }

    public UsersDTO(String userName, String firstName, String lastName, String country, String address,
                   String phone, String avatar, String createdDate) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.address = address;
        this.phone = phone;
        this.avatar = avatar;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
