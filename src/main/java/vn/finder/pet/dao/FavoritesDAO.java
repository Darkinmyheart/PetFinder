package vn.finder.pet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Favorites;
import vn.finder.pet.entity.Users;

@RepositoryRestResource(path = "favorites")
public interface FavoritesDAO extends JpaRepository<Favorites, Long> {
    Page<Favorites> findAll(Pageable pageable);

    @Query("SELECT COUNT(f.id) "
            + "FROM Animals a "
            + "INNER JOIN a.listFavorites f "
            + "INNER JOIN a.shelters s "
            + "WHERE MONTH(f.likeDate) = :date AND YEAR(f.likeDate) = :year AND s.users.userName LIKE %:userName%")
    Integer findByMonthAndShelter(@Param("date") int date, @Param("year") int year, @Param("userName") String userName);

    @Query("SELECT f FROM Favorites f WHERE f.users.userName LIKE :userName")
    Page<Favorites> findByUser(Pageable pageable, @Param("userName") String userName);
}
