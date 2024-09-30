package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.FavoritesDAO;
import vn.finder.pet.entity.Animals;
import vn.finder.pet.entity.Favorites;
import vn.finder.pet.entity.Users;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class FavoritesService {
    private FavoritesDAO favoritesDAO;
    private AnimalsService animalsService;
    private UsersService usersService;

    @Autowired
    public FavoritesService(FavoritesDAO favoritesDAO, AnimalsService animalsService, UsersService usersService) {
        this.favoritesDAO = favoritesDAO;
        this.animalsService = animalsService;
        this.usersService = usersService;
    }

    @Transactional
    public Boolean removeOne(Long id) {
        Optional<Favorites> favoriteOptional = this.favoritesDAO.findById(id);
        if (favoriteOptional.isPresent()) {
            this.favoritesDAO.delete(favoriteOptional.get());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean removeAll(Users users) {
        for(int i = 0; i < users.getListFavorites().size(); i++){
            this.favoritesDAO.delete(this.favoritesDAO.findById(users.getListFavorites().get(i).getId()).get());
        }
        return false;
    }

    @Transactional
    public Favorites save(Long id, Users users){
        Animals animals = this.animalsService.findById(id);
        Favorites favorites = new Favorites(null, Date.valueOf(LocalDate.now()), users ,animals);
        return this.favoritesDAO.save(favorites);
    }

    public Page<Animals> findAll(int page, int size){
        return this.animalsService.filterFavorite(page, size);
    }

    public Page<Favorites> findByUser(int page, int size, String userName){
        Pageable pageable = PageRequest.of(page, size);
        return this.favoritesDAO.findByUser(pageable, userName);
    }

    public Integer findByMonthAndShelter(int date, int year, String userName){
        return this.favoritesDAO.findByMonthAndShelter(date, year, userName);
    }

}
