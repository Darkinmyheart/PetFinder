package vn.finder.pet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.AdopterProfile;

@RepositoryRestResource(path = "adopter-profile")
public interface AdopterProfileDAO extends JpaRepository<AdopterProfile, Long> {

}
