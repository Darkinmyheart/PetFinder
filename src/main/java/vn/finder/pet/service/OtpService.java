package vn.finder.pet.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class OtpService {
	private static final Integer x = 1;
	private LoadingCache<String, String> otpCache;
	//phương Thức tạo Cache Tồn tại được Bao lâu
	public OtpService() {
		// Cache có Khoảng thời gian x và giá trị thời gian sau đó Tạo 1 Cache mới chứa Key và value nếu Key hết hạn thì giá trị sẽ trả về Null
		otpCache = CacheBuilder.newBuilder().expireAfterAccess(x, TimeUnit.MINUTES)
				.build(new CacheLoader<String, String>() {
					@Override
					public String load(String key) {
						return "";
					}
				});
	}
	//Mã Random
	public String RandomMa() {
		Random random = new Random();
		int otp = 0;
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			otp = random.nextInt(10);
			b.append(otp);
		}
		return b.toString();
	}
	//Lấy Mã Otp
	public String GetOtp(String otp) {
		try {
			return otpCache.get(otp);
		} catch (ExecutionException e) {
			return null;
		}
	}

	//Kiểm Tra giá Key tồn tại chưa
	public String checkKey(String key) {
		String cachedOTP = otpCache.getIfPresent(key);
		return cachedOTP;
	}

	//Kiểm Tra Giá Trị Của Mã otp thuộc Email nào
	public String CheckOtp(String valuse) {
		Map<String, String> cacheMap = otpCache.asMap();
		for (Map.Entry<String, String> entry : cacheMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (value.equals(value)) {
				return key;
			}
			System.out.println(key);

		}
		return null;
	}
	public String generateOTP(String key) throws ExecutionException {
		//mã Random
		String otp = RandomMa();
		//kiểm Tra Key Tồn Tại Giá trị chưa Có Thì Xoá Chưa thì thêm
		if(checkKey(key)!=null) {
			otpCache.invalidate(key);
		}
		//Đặt Key và value
		otpCache.put(key, otp);
		return otp;
	}


}
