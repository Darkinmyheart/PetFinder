package vn.finder.pet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Users;

import java.util.List;

@RepositoryRestResource(path = "users")
public interface UsersDAO extends JpaRepository<Users, String> {
    public Page<Users> findAll(Pageable pageable);

    @Query("SELECT u FROM Users u WHERE (:search IS NULL OR :search = '' OR u.firstName LIKE %:search% OR u.lastName LIKE %:search%) ORDER BY u.firstName ASC")
    List<Users> findAllByOrderByFirstNameAsc(@Param("search") String search);

    @Query("SELECT u FROM Users u WHERE (:search IS NULL OR :search = '' OR u.firstName LIKE %:search% OR u.lastName LIKE %:search%) ORDER BY u.firstName DESC")
    List<Users> findAllByOrderByFirstNameDesc(@Param("search") String search);

    @Query("SELECT u FROM Users u WHERE (:search IS NULL OR :search = '' OR u.firstName LIKE %:search% OR u.lastName LIKE %:search%) ORDER BY u.createdDate ASC")
    List<Users> findAllByOrderByCreatedDateAsc(@Param("search") String search);

    @Query("SELECT u FROM Users u WHERE (:search IS NULL OR :search = '' OR u.firstName LIKE %:search% OR u.lastName LIKE %:search%) ORDER BY u.createdDate DESC")
    List<Users> findAllByOrderByCreatedDateDesc(@Param("search") String search);

}
