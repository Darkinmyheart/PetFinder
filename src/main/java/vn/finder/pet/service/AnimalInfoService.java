package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.AnimalInfoDAO;
import vn.finder.pet.entity.AnimalInfo;

@Service
public class AnimalInfoService {
    private AnimalInfoDAO animalInfoDAO;

    @Autowired
    public AnimalInfoService(AnimalInfoDAO animalInfoDAO) {
        this.animalInfoDAO = animalInfoDAO;
    }

    @Transactional
    public Boolean save(AnimalInfo animalInfo){
        try {
            this.animalInfoDAO.save(animalInfo);
            return true;
        } catch (Exception e){

        }
        return false;
    }
}
