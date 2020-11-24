import java.math.BigInteger;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class Test {
	
	public static String data = "ciao";
	
	private static String token = ";";

	public static void main(String[] args) {
		rsaEncrypt();
	}
	
	private static void rsaEncrypt() {
		
		Random rng = new Random();
		
		BigInteger firstPrime = BigInteger.probablePrime(Long.BYTES, rng);
		BigInteger secondPrime = BigInteger.probablePrime(Long.BYTES, rng);
		while (true) {
			if (firstPrime.compareTo(secondPrime) != 0) {
				if (secondPrime.isProbablePrime(1)) {
					if (firstPrime.isProbablePrime(1)) {
						long min = 30000;
						if (secondPrime.multiply(firstPrime).compareTo(BigInteger.valueOf(min)) > 0) {
							break;
						}
					}else {
						firstPrime = BigInteger.probablePrime(Long.BYTES, rng);
					}
				} else {
					secondPrime = BigInteger.probablePrime(Long.BYTES, rng);
				}
			}else {
				firstPrime = BigInteger.probablePrime(Long.BYTES, rng);
				secondPrime = BigInteger.probablePrime(Long.BYTES, rng);
			}
		}
		
		System.out.println((secondPrime.multiply(firstPrime)).compareTo(new BigInteger("1000000"))>=0);
		
		
		//privata = d,n
		//pubblica = e,n
		
		BigInteger n = firstPrime.multiply(secondPrime);
		
		
		BigInteger z = secondPrime.subtract(BigInteger.ONE).multiply((firstPrime.subtract(BigInteger.ONE)));

		
		BigInteger e = coPrime(z);
		
		BigInteger d;
		
		int i = 2;
		   
		while(( e.multiply(BigInteger.valueOf(i)).mod(z).compareTo(BigInteger.ONE))!=0) {
			i = i + ThreadLocalRandom.current().nextInt(1, 2);
			
		}
		
		d = BigInteger.valueOf(i);


		System.out.println("prime-->" + firstPrime + " secondPrime-->" + secondPrime + " n-->" + n + " z-->" + z
				+ " e-->" + e + " d-->" + d);

		String c = encrypt(data, e, n);

		System.out.println("ENCRYPTED --> " + c);

		decrypt(c, d, n);
	}
	
	private static String encrypt(String data,BigInteger e,BigInteger n) {	
		
		Vector<BigInteger> intVector = new Vector<BigInteger>();

		StringBuilder intList = new StringBuilder();

		char[] charRawVector = new char[data.length()];

		data.getChars(0, data.length(), charRawVector, 0);		
		
		for (char character : charRawVector) {
			int i = (int) character;
			if (i != 13) {
				BigInteger c = new BigInteger(Integer.toString(i)).modPow(e,n);
				intVector.add(new BigInteger(Integer.toString(i)));
				intList.append(c);
				intList.append(";");
			} else {
				intList.append("\n");
			}
		}
		
		
		System.out.println("ENCRYPT --> " + intVector);
		
		
		return intList.toString();
	}
	
	private static void decrypt(String data, BigInteger d, BigInteger n) {

		StringTokenizer dama = new StringTokenizer(data, token);

		Vector<BigInteger> intVector = new Vector<BigInteger>();

		StringBuilder stringList = new StringBuilder();

		while (dama.hasMoreTokens()) {
			data = dama.nextToken();
			if (!data.isEmpty()) {
				int i = Integer.parseInt(data);
				BigInteger m = new BigInteger(Integer.toString(i)).modPow(d, n);
				intVector.add(m);
				stringList.append((char)m.longValueExact());
			} else {
				stringList.append("\n");
			}
		}
		
		System.out.println("DECRYPTED --> " + intVector);

		System.out.println("CHAR : DECRYPTED --> " + stringList);

	}
	
	private static BigInteger coPrime(BigInteger firstPrime) {
		BigInteger coPrime;
		Random rng = new Random();
		while ((coPrime = BigInteger.probablePrime(Long.BYTES, rng)).compareTo(firstPrime)>0) {
		}
		return coPrime;
		
	}

}
