package vn.finder.pet.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.finder.pet.common.RandomCommon;
import vn.finder.pet.dao.UsersDAO;
import vn.finder.pet.entity.Users;

@Service
public class TwoFactorAuthPasswordsService {
    private MailService mailService;
    private HttpSession session;
    private UsersDAO usersDAO;

    @Autowired
    public TwoFactorAuthPasswordsService(HttpSession session, MailService mailService, UsersDAO usersDAO) {
        this.session = session;
        this.mailService = mailService;
        this.usersDAO = usersDAO;
    }

    /**
     * Gửi passwords dùng 1 lần đến email
     * @param email email nhận passwords
     */
    public void sendOneTimePasswords(String email) {
        String oneTimePasswords = RandomCommon.randomString(6).toString();
        session.setAttribute("one-time-passwords",oneTimePasswords);
        mailService.sendmail(mailService.getSend(),
                "Password",
                "Password ngẫu nhiên là:" + oneTimePasswords,
                email);
    }

    /**
     * Xác thực mật khẩu dùng một lần
     * @param otp mật khẩu dùng một lần
     * @return true nếu mật khẩu đúng, ngược lại false
     */
    public boolean validateOneTimePassword(String otp) {
        return otp.equals(session.getAttribute("one-time-passwords"));
    }

    /**
     * Dùng để kiểm tra pattern Password
     *
     * @param password password cần kiểm tra
     * @return true nếu đúng định dạng, false nếu sai định dạng
     */
    public boolean validatePatternPassword(String password) {
        // Kiểm tra độ dài của mật khẩu
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        // Biểu thức chính quy cho từng điều kiện
        String uppercasePattern = ".*[A-Z].*";
        String specialCharacterPattern = ".*[!@#$%^&*()_+=-].*";
        String digitPattern = ".*\\d.*";
        String lowercasePattern = ".*[a-z].*";

        int validConditions = 0;

        // Kiểm tra từng điều kiện
        if (password.matches(uppercasePattern)) {
            validConditions++;
        }
        if (password.matches(specialCharacterPattern)) {
            validConditions++;
        }
        if (password.matches(digitPattern)) {
            validConditions++;
        }
        if (password.matches(lowercasePattern)) {
            validConditions++;
        }

        // Kiểm tra nếu mật khẩu thỏa mãn ít nhất 3 trong 4 điều kiện
        return validConditions >= 3;
    }


    /**
     * Thực hiện kiểm tra password
     *
     * @param password mật khẩu người muốn đổi
     * @return true nếu khác password cũ, false nếu giống password cũ
     */
    public boolean checkTwoFactorAuthPassword(String password) {
        if (validatePatternPassword(password)) {
            session.removeAttribute("one-time-passwords");
            return true;
        }
        return false;
    }

    @Transactional
    public Users updatePassword(String email, String password){
        Users users = this.usersDAO.findById(email).get();
        if (users != null){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String passwordEncoder = bCryptPasswordEncoder.encode(password);
            users.setPassword("{bcrypt}"+passwordEncoder);
            return this.usersDAO.save(users);
        }
        return null;
    }
}