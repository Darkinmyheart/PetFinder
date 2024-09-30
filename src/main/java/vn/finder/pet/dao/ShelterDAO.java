package vn.finder.pet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Animals;
import vn.finder.pet.entity.Shelters;

import java.util.List;

@RepositoryRestResource(path = "shelters")
public interface ShelterDAO extends JpaRepository<Shelters, Long> {
    List<Shelters> findByShelterStatus(String status);

    @Query("SELECT s FROM Shelters s WHERE s.shelterStatus NOT LIKE %?1%")
    Page<Shelters> findSheltersByStatusNotContaining(Pageable pageable, @Param("status") String status);

    @Query("SELECT s FROM Shelters s WHERE s.shelterStatus LIKE %?1%")
    Page<Shelters> findSheltersByStatusContaining(Pageable pageable, @Param("status") String status);

    @Query("SELECT s FROM Shelters s WHERE s.shelterName LIKE %:shelterName% AND s.shelterAddress LIKE %:shelterAddress% AND s.shelterStatus LIKE %:status%")
    Page<Shelters> findByShelterNameAndShelterAddress(Pageable pageable, @Param("shelterName") String shelterName, @Param("shelterAddress") String shelterAddress, @Param("status") String status);

    @Query("SELECT a FROM Shelters s INNER JOIN s.listAnimals a WHERE s.users.userName LIKE :userName ORDER BY a.animalDate DESC")
    Page<Animals> findAllHistoryActiveOfShelter(Pageable pageable, @Param("userName")  String userName);

    @Query("SELECT s FROM Shelters s WHERE s.shelterStatus LIKE %:status% ORDER BY s.shelterName ASC")
    List<Shelters> findByShelterStatusOrderByShelterNameAsc(@Param("status")String status);

    @Query("SELECT s FROM Shelters s WHERE s.shelterStatus LIKE %:status% ORDER BY s.shelterName DESC")
    List<Shelters> findByShelterStatusOrderByShelterNameDesc(@Param("status")String status);

    @Query("SELECT s FROM Shelters s WHERE s.shelterStatus LIKE %:status% ORDER BY s.shelterDate ASC")
    List<Shelters> findByShelterStatusOrderByShelterDateAsc(@Param("status")String status);

    @Query("SELECT s FROM Shelters s WHERE s.shelterStatus LIKE %:status% ORDER BY s.shelterDate DESC")
    List<Shelters> findByShelterStatusOrderByShelterDateDesc(@Param("status")String status);


}
