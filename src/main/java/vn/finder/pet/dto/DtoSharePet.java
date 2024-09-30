package vn.finder.pet.dto;

public class DtoSharePet {
    private String email;
    private String linkshare;
    private Long id;

    public DtoSharePet(String email) {
        this.email = email;
    }

    public DtoSharePet(String email, String linkshare, Long id) {
        this.email = email;
        this.linkshare = linkshare;
        this.id=id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkshare() {
        return linkshare;
    }

    public void setLinkshare(String linkshare) {
        this.linkshare = linkshare;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
