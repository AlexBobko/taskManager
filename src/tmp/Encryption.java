package tmp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import utils.org.mindrot.jbcrypt.BCrypt;

public abstract class Encryption {

	public Encryption() {
		
	}
	public static String hashPassword(String password) {
		final String salt = "fgd#f[ds";
		String hashed_password = BCrypt.hashpw(password.concat(salt), BCrypt.gensalt());
		return(hashed_password);
		//проверка пароля
//		if (BCrypt.checkpw(pass, hashedT))
//			System.out.println("It matches");
//		else
//			System.out.println("It does not match");
		
}
	
	
	
	public static String hashStringMD5 (String pass)
	{
		final String soul = "hUsdLLs;dds";
		pass = pass.concat(soul);
		MessageDigest messageDigest = null;
		byte[] digest = new byte[0];
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(pass.getBytes());
			digest = messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			// тут можно обработать ошибку
			// возникает она если в передаваемый алгоритм в getInstance(,,,) не
			// существует
			e.printStackTrace();
		}
		BigInteger bigInt = new BigInteger(1, digest);
		String md5Hex = bigInt.toString(16);
		while (md5Hex.length() < 32) {
			md5Hex = "0" + md5Hex;
		}
		return md5Hex;
	}

}
