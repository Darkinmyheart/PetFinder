package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.SponsDAO;
import vn.finder.pet.entity.Spons;
import vn.finder.pet.entity.Users;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class SponsService {
    private SponsDAO sponsDAO;
    private UsersService usersService;

    @Autowired
    public SponsService(SponsDAO sponsDAO, UsersService usersService) {
        this.sponsDAO = sponsDAO;
        this.usersService = usersService;
    }

    @Transactional
    public Boolean save(Spons spons){
        try {
            this.sponsDAO.save(spons);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public Spons findById(Long id){
        return this.sponsDAO.findById(id).get();
    }

    public List<Spons> findAll(){
        return this.sponsDAO.findAll();
    }

    public Page<Spons> findAllToPage(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return this.sponsDAO.findAll(pageable);
    }

    public Page<Spons> findAllByUser(int page, int size, String userName){
        Pageable pageable = PageRequest.of(page, size);
        return this.sponsDAO.findAllByUser(pageable, userName);
    }

    public Page<Spons> findByStatus(String email, int page, int sizePage){
        Pageable pageable = PageRequest.of(page, sizePage);
        Users users = this.usersService.findById(email).get();
        return this.sponsDAO.findByStatus(pageable, users.getShelters().getId());
    }

    public Double findCountGiftInMonth(Integer month, Integer year){
        return this.sponsDAO.findCountGiftInMonth(month, year);
    }

    public Double findCountGiftInYear(Integer year){
        return this.sponsDAO.findCountGiftInYear(year);
    }

    public Integer findCountUserDonateByWebsite(Long shelterId){
        return this.sponsDAO.findCountUserDonateByWebsite(shelterId);
    }

    public static String formatCurrency(Double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }
}
