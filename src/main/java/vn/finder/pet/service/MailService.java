package vn.finder.pet.service;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailService {
	private JavaMailSender mailsender;
	private OtpService otp;

	@Value("${spring.mail.username}")
	private String send;

	@Autowired
	public MailService(JavaMailSender mailsender, OtpService otp) {
		this.mailsender = mailsender;
		this.otp = otp;
	}

	public void sendmail(String mail,String subject, String text) {
		SimpleMailMessage ml= new SimpleMailMessage();
		ml.setFrom(mail);
		ml.setSubject(subject);
		ml.setText(text);
		mailsender.send(ml);
	}

	public String getSend() {
		return send;
	}

	public void sendmail(String mail, String subject, String text,String toMail) {
		SimpleMailMessage ml= new SimpleMailMessage();
		ml.setFrom(mail);
		ml.setSubject(subject);
		ml.setText(text);
		ml.setTo(toMail);
		mailsender.send(ml);
	}

	public void MaxacNhan(String mailto) {
		SimpleMailMessage ml= new SimpleMailMessage();
		ml.setFrom(send);
		ml.setSubject("Mã Xác Nhận");
		try {
			ml.setText("Mã Xác nhận Đăng Ký: "+otp.generateOTP(mailto)+"\n"
			+"Vui lòng nhập Mã Xác Thực để Hoàn Thành Quá Trình Đăng Ký Hiệu Lực Trong Vòng 1 Phút");
		} catch (ExecutionException e) {
			e.printStackTrace();
		}	
		ml.setTo(mailto);
		System.out.println(ml.getText());
		mailsender.send(ml);
	}
	
	public void ResetPassWord(String mailto) {
		SimpleMailMessage ml= new SimpleMailMessage();
		ml.setFrom(send);
		ml.setSubject("Mật Khẩu Mới Của Bạn");
		ml.setText("Mã Xác nhận Đăng Ký: "+1+"\n"
		+"Vui Đăng Nhập Và đổi Lại Mật Khẩu Theo Đường link"+"\n"+"https://www.youtube.com/watch?v=ihY0Hjv2fK0");	
		ml.setTo(mailto);
		mailsender.send(ml);
	}

	public void agreePet(String email,String namepet){
		sendmail(send,"Finder Pet Support","Đơn Xin Nhận Nuôi: "+namepet+" Của Bạn Đã Được Chấp Nhận",email);
	}

	public void disablePet(String email,String namepet){
		sendmail(send,"Finder Pet Support","Bạn không đủ điều kiện để nhận nuôi: "+namepet,email);
	}

	public void sendMailToShelters(String email,String nameuser,String namepet){
		sendmail(send,"Thông Báo ",nameuser+":"+"Đã Yêu Cầu Nhận Nuôi: "+namepet,email);
	}

	public void sendMailToAdminAgree(String email,String name){
		sendmail(send,"Thông Báo Tới Từ Admin",":"+"Đơn Xin Chấp Thuận Từ :  "+name+  "/n"+" Đã Được Xác Nhận Là Shelters ",email);
	}

	public void sendMailToAdminDelete(String email,String name){
		sendmail(send,"Thông Báo Tới Từ Admin",":"+"Shelter của bạn :  "+name+  "/n"+" Đã bị chúng tôi tạm thời đóng cửa. Nếu có bất cứ khyếu nại nào vui lòng gửi báo cáo cho chúng tôi ",email);
	}

	public void sendMailToAdminDisable(String email,String name){
		sendmail(send,"Thông Báo Tới Từ Admin "+name,"Yêu Xin Chấp Thuận ĐÃ Bị Từ Chối Vì Không Đủ Điều Kiện Thành Shelters: ",email);
	}

	public boolean checkString(String email){
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		if(email.matches(emailRegex)){
			return true;
		}
		return false;
	}

	public void sendMailSharePet (String email,String name,String age,String Gender,String url){
		if(checkString(email) == true){
			sendmail(send,"Thông Báo PetFinder","Mình muốn chia sẻ với bạn thông tin về một con thú cưng đáng yêu Tên Là:"+name.toUpperCase()+"\n"
					+"Age: "+age.toUpperCase()+"\n"
					+"Gender:"+Gender.toUpperCase()+"\n"+
					". Bạn có thể xem chi tiết tại đường dẫn sau:"+url+"\n"+"Trân trọng,\n" +
					email,email);
		}
	}

	public void sendMailSharePetUser (String email,String name,String age,String Gender,String url,String user){
		if(checkString(email) == true){
			sendmail(send,"Thông Báo PetFinder",user+" muốn chia sẻ với bạn thông tin về một con thú cưng đáng yêu Tên Là:"+name+"\n"
					+"Age:"+age.toUpperCase()+"\n"
					+"Gender:"+Gender.toUpperCase()+"\n"+
					"Bạn có thể xem chi tiết tại đường dẫn sau:"+url+"\n"+"Trân trọng,\n" +
					email,email);
		}
	}
}
