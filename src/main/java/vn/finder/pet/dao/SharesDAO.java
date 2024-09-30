package vn.finder.pet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Shares;

@RepositoryRestResource(path = "shares")
public interface SharesDAO extends JpaRepository<Shares, Long> {

}
