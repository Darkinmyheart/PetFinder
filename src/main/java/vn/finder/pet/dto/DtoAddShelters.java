package vn.finder.pet.dto;


public class DtoAddShelters {

    private String location;
    private String img;
    private String addressemail;
    private String linkfacebook;
    private String linkintagram;
    private String namne;
    private String phonenumber;
    private String listmission;
    private String opentime;
    private String listpolicy;
    private String shortdescription;
    private String latitude;
    private String longitude;

    public DtoAddShelters() {

    }

    public DtoAddShelters( String location, String img, String addressemail, String linkfacebook, String linkintagram, String namne, String phonenumber, String listmission, String opentime, String listpolicy, String shortdescription, String latitude, String longitude) {

        this.location = location;
        this.img = img;
        this.addressemail = addressemail;
        this.linkfacebook = linkfacebook;
        this.linkintagram = linkintagram;
        this.namne = namne;
        this.phonenumber = phonenumber;
        this.listmission = listmission;
        this.opentime = opentime;
        this.listpolicy = listpolicy;
        this.shortdescription = shortdescription;
        this.latitude = latitude;
        this.longitude = longitude;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddressemail() {
        return addressemail;
    }

    public void setAddressemail(String addressemail) {
        this.addressemail = addressemail;
    }

    public String getLinkfacebook() {
        return linkfacebook;
    }

    public void setLinkfacebook(String linkfacebook) {
        this.linkfacebook = linkfacebook;
    }

    public String getLinkintagram() {
        return linkintagram;
    }

    public void setLinkintagram(String linkintagram) {
        this.linkintagram = linkintagram;
    }

    public String getNamne() {
        return namne;
    }

    public void setNamne(String namne) {
        this.namne = namne;
    }

    public String getphonenumber() {
        return phonenumber;
    }

    public void setphonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getListmission() {
        return listmission;
    }

    public void setListmission(String listmission) {
        this.listmission = listmission;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getListpolicy() {
        return listpolicy;
    }

    public void setListpolicy(String listpolicy) {
        this.listpolicy = listpolicy;
    }

    public String getShortdescription() {
        return shortdescription;
    }

    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "DtoAddShelters{" +
                "username='"     + '\'' +
                ", location='" + location + '\'' +
                ", img='" + img + '\'' +
                ", addressemail='" + addressemail + '\'' +
                ", linkfacebook='" + linkfacebook + '\'' +
                ", linkintagram='" + linkintagram + '\'' +
                ", namne='" + namne + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", listmission='" + listmission + '\'' +
                ", opentime='" + opentime + '\'' +
                ", listpolicy='" + listpolicy + '\'' +
                ", shortdescription='" + shortdescription + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
