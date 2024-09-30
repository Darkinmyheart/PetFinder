package vn.finder.pet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.AuthoritiesDAO;
import vn.finder.pet.entity.Authorities;

@Service
public class AuthoritiesService {
    @Autowired
    AuthoritiesDAO authoritiesDAO;

    public Authorities findById(Long id){
        return authoritiesDAO.findById(id).get();
    }
    public Authorities save(Authorities authorities){
        return  authoritiesDAO.save(authorities);
    }
}