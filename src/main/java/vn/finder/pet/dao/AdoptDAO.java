package vn.finder.pet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Adopt;
import vn.finder.pet.entity.Animals;
import vn.finder.pet.entity.Users;

import java.util.List;

@RepositoryRestResource(path = "adopt")
public interface AdoptDAO extends JpaRepository<Adopt, Long> {
    @Query("SELECT COUNT(ad.id) "
            + "FROM Animals a "
            + "INNER JOIN a.listAdopt ad "
            + "INNER JOIN a.shelters s "
            + "WHERE MONTH(ad.adoptDate) = :date AND YEAR(ad.adoptDate) = :year AND s.users.userName LIKE %:userName%")
    Integer findByMonthAndShelter(@Param("date") int date, @Param("year") int year, @Param("userName") String userName);

    @Query("SELECT a FROM Adopt a WHERE a.adopt_status NOT LIKE :status")
    List<Adopt> findAllNotContains(String status);

    Adopt findByAnimalsAndUsers(Animals animals, Users users);

    @Query("SELECT a FROM Adopt a INNER JOIN a.animals am INNER JOIN am.shelters s WHERE s.id = :id")
    Page<Adopt> findAllAdoptOfShelter(@Param(value = "id") Long id, Pageable pageable);

    @Query("SELECT a FROM Adopt a WHERE a.adopt_status LIKE %:adopt_status% AND a.users.userName = :users")
    Page<Adopt> findByUsersAndAdoptStatus(@Param(value = "users") String users, @Param(value = "adopt_status") String adopt_status, Pageable pageable);
}
