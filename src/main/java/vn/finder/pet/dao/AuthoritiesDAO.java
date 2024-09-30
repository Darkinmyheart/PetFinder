package vn.finder.pet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Authorities;

@RepositoryRestResource(path = "authorities")
public interface AuthoritiesDAO extends JpaRepository<Authorities, Long> {

}
