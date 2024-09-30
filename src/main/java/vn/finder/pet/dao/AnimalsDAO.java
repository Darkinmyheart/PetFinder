package vn.finder.pet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Adopt;
import vn.finder.pet.entity.Animals;

import java.util.List;

@RepositoryRestResource(path = "animals")
public interface AnimalsDAO extends JpaRepository<Animals, Long> {
    @Query("SELECT a FROM Animals a WHERE a.breed.breed_type IN :breedType AND a.breed.breed_name LIKE %:breedName% AND a.shelters.shelterAddress LIKE %:shelterAddress% AND a.animalAge IN :ages AND a.animalGender IN :genders AND a.animalSize LIKE %:size% AND a.animalName LIKE %:name% AND a.id NOT IN :adopt ORDER BY a.animalName DESC")
    Page<Animals> findByBreedNameContains(@Param("breedType") List<String> breedType, @Param("breedName") String breedName, @Param("shelterAddress") String shelterAddress, @Param("ages") List<String> ages, @Param("genders") List<Boolean> genders, @Param("size") String size, @Param("name") String name, @Param("adopt") List<Long> adopt, Pageable pageable);

    @Query("SELECT a FROM Animals a ORDER BY a.animalName DESC")
    Page<Animals> filterName(Pageable pageable);

    @Query("SELECT a FROM Animals a ORDER BY a.animalDate DESC")
    Page<Animals> filterDate(Pageable pageable);

    @Query("SELECT a " +
            "FROM Animals a " +
            "LEFT JOIN a.listFavorites f " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(f.id) DESC")
    Page<Animals> filterFavorite(Pageable pageable);

    @Query("SELECT COUNT(a.id) FROM Animals a LEFT JOIN a.listAdopt ad WHERE ad.id IS NULL AND a.shelters.id = :id")
    Integer findCountAnimalsAvailable(@Param("id") Long id);

    @Query("SELECT COUNT(a.id) FROM Animals a INNER JOIN a.listAdopt ad WHERE ad.adopt_status LIKE '%Awaiting%' AND a.shelters.id = :id")
    Integer findCountAwaitingAnimals(@Param("id") Long id);

    @Query("SELECT COUNT(a.id) FROM Animals a JOIN a.listAdopt ad WHERE ad.adopt_status LIKE '%Adopted%' AND a.shelters.id = :id")
    Integer findCountAnimalsAdopted(@Param("id") Long id);

    @Query("SELECT COUNT(a.id) FROM Animals a WHERE a.shelters.id = :id")
    Integer findCountAnimalsInShelter(@Param("id") Long id);

    @Query("SELECT COUNT(a.id) FROM Animals a INNER JOIN a.listAdopt ad WHERE a.shelters.id = :id")
    Integer findCountAdopt(@Param("id") Long id);

    @Query("SELECT ad FROM Animals a INNER JOIN a.listAdopt ad WHERE ad.adopt_status LIKE %:status% AND a.shelters.id = :id ORDER BY ad.adoptDate DESC")
    Page<Adopt> findByStatus(@Param("status") String status, Pageable pageable, @Param("id") Long id);

    @Query("SELECT COUNT(a.id) FROM Animals a INNER JOIN a.listFavorites f WHERE a.shelters.id = :id")
    Integer findCountFavorite(@Param("id") Long id);

    @Query("SELECT a FROM Animals a WHERE a.shelters.id = :id")
    Page<Animals> findAllPet(Pageable pageable, @Param("id") Long id);

    @Query(value = "SELECT a FROM Animals a ORDER BY function('RAND')")
    Page<Animals> findRandom(Pageable pageable);

    @Query("SELECT a FROM Animals a INNER JOIN a.shelters s " +
            "ORDER BY CASE WHEN s.shelterAddress LIKE %:shelterAddress% THEN 1 ELSE 2 END, " +
            "s.shelterAddress")
    Page<Animals> findByShelterAddressOrderByCustom(@Param("shelterAddress") String shelterAddress, Pageable pageable);

    @Query("SELECT a FROM Animals a JOIN a.breed b WHERE b.breed_type LIKE %:breedType% " +
            "ORDER BY CASE WHEN b.breed_name LIKE %:breedName% THEN 1 ELSE 2 END, b.breed_type")
    Page<Animals> findByBreedOrderByCustom(@Param("breedName") String breedName, @Param("breedType") String breedType, Pageable pageable);

    @Query("SELECT a FROM Animals a " +
            "WHERE a.breed.breed_name LIKE %:breedName% " +
            "AND a.breed.breed_type LIKE %:breedType% " +
            "AND a.animalName LIKE %:animalName% " +
            "AND a.animalGender = :animalGender " +
            "AND a.animalSize LIKE %:animalSize% " +
            "AND a.animalAge LIKE %:animalAge%")
    Animals findAnimalsByCriteria(@Param("breedName") String breedName,
                                        @Param("breedType") String breedType,
                                        @Param("animalName") String animalName,
                                        @Param("animalGender") Boolean animalGender,
                                        @Param("animalSize") String animalSize,
                                        @Param("animalAge") String animalAge);
}
