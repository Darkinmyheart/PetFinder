package vn.finder.pet.dto;

public class ShelterDTO {

    private Long id;
    private String shelterAvatar;
    private String shelterName;
    private String shelterEmail;
    private String shelterAddress;
    private String shelterPhone;
    private String shelterDate;
    private String shelterStatus;
    private String shelterInfoMission;
    private String shelterInfoPolicy;
    private String shelterInfoOperatingTime;
    private String shelterInfoFacebook;
    private String shelterInfoInstagram;
    private String shelterDescription;
    private String shelterLatitude;
    private String shelterLongitude;
    private String userName;

    public ShelterDTO() {
    }

    public ShelterDTO(Long id, String shelterAvatar, String shelterName, String shelterEmail, String shelterAddress, String shelterPhone, String shelterDate, String shelterStatus, String shelterInfoMission, String shelterInfoPolicy, String shelterInfoOperatingTime, String shelterInfoFacebook, String shelterInfoInstagram, String shelterDescription, String shelterLatitude, String shelterLongitude, String username) {
        this.id = id;
        this.shelterAvatar = shelterAvatar;
        this.shelterName = shelterName;
        this.shelterEmail = shelterEmail;
        this.shelterAddress = shelterAddress;
        this.shelterPhone = shelterPhone;
        this.shelterDate = shelterDate;
        this.shelterStatus = shelterStatus;
        this.shelterInfoMission = shelterInfoMission;
        this.shelterInfoPolicy = shelterInfoPolicy;
        this.shelterInfoOperatingTime = shelterInfoOperatingTime;
        this.shelterInfoFacebook = shelterInfoFacebook;
        this.shelterInfoInstagram = shelterInfoInstagram;
        this.shelterDescription = shelterDescription;
        this.shelterLatitude = shelterLatitude;
        this.shelterLongitude = shelterLongitude;
        this.userName = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShelterAvatar() {
        return shelterAvatar;
    }

    public void setShelterAvatar(String shelterAvatar) {
        this.shelterAvatar = shelterAvatar;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getShelterEmail() {
        return shelterEmail;
    }

    public void setShelterEmail(String shelterEmail) {
        this.shelterEmail = shelterEmail;
    }

    public String getShelterAddress() {
        return shelterAddress;
    }

    public void setShelterAddress(String shelterAddress) {
        this.shelterAddress = shelterAddress;
    }

    public String getShelterPhone() {
        return shelterPhone;
    }

    public void setShelterPhone(String shelterPhone) {
        this.shelterPhone = shelterPhone;
    }

    public String getShelterDate() {
        return shelterDate;
    }

    public void setShelterDate(String shelterDate) {
        this.shelterDate = shelterDate;
    }

    public String getShelterStatus() {
        return shelterStatus;
    }

    public void setShelterStatus(String shelterStatus) {
        this.shelterStatus = shelterStatus;
    }

    public String getShelterInfoMission() {
        return shelterInfoMission;
    }

    public void setShelterInfoMission(String shelterInfoMission) {
        this.shelterInfoMission = shelterInfoMission;
    }

    public String getShelterInfoPolicy() {
        return shelterInfoPolicy;
    }

    public void setShelterInfoPolicy(String shelterInfoPolicy) {
        this.shelterInfoPolicy = shelterInfoPolicy;
    }

    public String getShelterInfoOperatingTime() {
        return shelterInfoOperatingTime;
    }

    public void setShelterInfoOperatingTime(String shelterInfoOperatingTime) {
        this.shelterInfoOperatingTime = shelterInfoOperatingTime;
    }

    public String getShelterInfoFacebook() {
        return shelterInfoFacebook;
    }

    public void setShelterInfoFacebook(String shelterInfoFacebook) {
        this.shelterInfoFacebook = shelterInfoFacebook;
    }

    public String getShelterInfoInstagram() {
        return shelterInfoInstagram;
    }

    public void setShelterInfoInstagram(String shelterInfoInstagram) {
        this.shelterInfoInstagram = shelterInfoInstagram;
    }

    public String getShelterDescription() {
        return shelterDescription;
    }

    public void setShelterDescription(String shelterDescription) {
        this.shelterDescription = shelterDescription;
    }

    public String getShelterLatitude() {
        return shelterLatitude;
    }

    public void setShelterLatitude(String shelterLatitude) {
        this.shelterLatitude = shelterLatitude;
    }

    public String getShelterLongitude() {
        return shelterLongitude;
    }

    public void setShelterLongitude(String shelterLongitude) {
        this.shelterLongitude = shelterLongitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ShellterDTO{" +
                "id=" + id +
                ", shelterAvatar='" + shelterAvatar + '\'' +
                ", shelterName='" + shelterName + '\'' +
                ", shelterEmail='" + shelterEmail + '\'' +
                ", shelterAddress='" + shelterAddress + '\'' +
                ", shelterPhone='" + shelterPhone + '\'' +
                ", shelterDate='" + shelterDate + '\'' +
                ", shelterStatus='" + shelterStatus + '\'' +
                ", shelterInfoMission='" + shelterInfoMission + '\'' +
                ", shelterInfoPolicy='" + shelterInfoPolicy + '\'' +
                ", shelterInfoOperatingTime='" + shelterInfoOperatingTime + '\'' +
                ", shelterInfoFacebook='" + shelterInfoFacebook + '\'' +
                ", shelterInfoInstagram='" + shelterInfoInstagram + '\'' +
                ", shelterDescription='" + shelterDescription + '\'' +
                ", shelterLatitude='" + shelterLatitude + '\'' +
                ", shelterLongitude='" + shelterLongitude + '\'' +
                ", username='" + userName + '\'' +
                '}';
    }
}
