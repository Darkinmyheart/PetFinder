package vn.finder.pet.oauth2User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.UsersDAO;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.UsersService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UsersService userService; // Service để truy cập cơ sở dữ liệu người dùng

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAuthorities().toString());

        String email = oAuth2User.getAttribute("email");
        System.out.println(email);
        Optional<Users> user = userService.findById(email);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (user.isPresent()) {
            authorities.add(new SimpleGrantedAuthority(user.get().getAuthorities().getAuthority()));
        }else{
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        }
        return new CustomOAuth2User(oAuth2User,authorities);
    }
}
