package vn.finder.pet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.AnimalInfo;

@RepositoryRestResource(path = "animal-info")
public interface AnimalInfoDAO extends JpaRepository<AnimalInfo, Long> {

}
