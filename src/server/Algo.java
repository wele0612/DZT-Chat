package server;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
public class Algo {
	public static String RSA_ALGORITHM = "RSA";
    public static String UTF8 = "UTF-8";
    private static final int KEY_SIZE=1024;
    
    public static class KeyStore{
        private Object publicKey;
        private Object privateKey;
        public KeyStore(Object pub, Object pri) {
        	publicKey=pub;
        	privateKey=pri;
        }
        /**
         * RSA��ȡ˽Կ
         * @param keyStore
         * @return
         */
        public byte[] getPrivateKey(){
            return ((RSAPrivateKey)privateKey).getEncoded();
        }

        /**
         * RSA��ȡ��Կ
         * @param keyStore
         * @return
         */
        public byte[] getPublicKey(){
            return ((RSAPublicKey)publicKey).getEncoded();
        }
    }
    /**
     * SHA256
     * @param strText �ַ���
     * @return String ɢ���ַ���
     */
	public static String SHA256(final String strText)
    {
	    String strResult = null;
	    if (strText != null && strText.length() > 0)
		    {
		    try{
			    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			    // ����Ҫ���ܵ��ַ���
			    messageDigest.update(strText.getBytes());
			    byte byteBuffer[] = messageDigest.digest();
			    StringBuffer strHexString = new StringBuffer();
			    for (int i = 0; i < byteBuffer.length; i++){
				    String hex = Integer.toHexString(0xff & byteBuffer[i]);
				    if (hex.length() == 1){
				    	strHexString.append('0');
				    }
				    strHexString.append(hex);
			    }
			    // �õ����ؽY��
			    strResult = strHexString.toString();
		    }
		    catch (NoSuchAlgorithmException e){
		    	e.printStackTrace();
		    }
	    }
	    return strResult;
    }
	
	//Author of RSA method:https://zhuanlan.zhihu.com/p/126469593
	public static KeyStore createKeys() throws NoSuchAlgorithmException {
        //KeyPairGenerator�������ɹ�Կ��˽Կ�ԡ���Կ����������ʹ�� getInstance ��������
		SecureRandom r=new SecureRandom();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE,r);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        KeyStore keyStore = new KeyStore(publicKey,privateKey);
        return keyStore;
    }

    /**
     * RSA˽Կ����
     * @param data ����������
     * @param key ��Կ
     * @return byte[] ��������
     * */
    public static byte[] RSAencryptByPrivateKey(byte[] data,byte[] key) throws Exception{

        //ȡ��˽Կ
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory=KeyFactory.getInstance(RSA_ALGORITHM);
        //����˽Կ
        PrivateKey privateKey=keyFactory.generatePrivate(pkcs8KeySpec);
        //���ݼ���
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA��Կ����
     * @param data
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     */
    public static byte[] RSAencryptByPublicKey(byte[] data, byte[] key) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        //ʵ������Կ����
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        //��ʼ����Կ,���ݸ����ı�����Կ����һ���µ� X509EncodedKeySpec��
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        //���ݼ���
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA˽Կ����
     * @param data ����������
     * @param key ��Կ
     * @return byte[] ��������
     * */
    public static byte[] RSAdecryptByPrivateKey(byte[] data,byte[] key) throws Exception{
        //ȡ��˽Կ
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory=KeyFactory.getInstance(RSA_ALGORITHM);
        //����˽Կ
        PrivateKey privateKey=keyFactory.generatePrivate(pkcs8KeySpec);
        //���ݽ���
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA��Կ����
     * @param data ����������
     * @param key ��Կ
     * @return byte[] ��������
     * */
    public static byte[] RSAdecryptByPublicKey(byte[] data,byte[] key) throws Exception{

        //ʵ������Կ����
        KeyFactory keyFactory=KeyFactory.getInstance(RSA_ALGORITHM);
        //��ʼ����Կ
        //��Կ����ת��
        X509EncodedKeySpec x509KeySpec=new X509EncodedKeySpec(key);
        //������Կ
        PublicKey pubKey=keyFactory.generatePublic(x509KeySpec);
        //���ݽ���
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * ��Կ, 256λ32���ֽ�
     */
    public static final String DEFAULT_SECRET_KEY = "uBdUx82vPHkDKb284d7NkjFoNcKWBuka";
    private static final String AES = "AES";

    /**
     * ��ʼ����IV, ��ʼ����IV�ĳ��ȹ涨Ϊ128λ16���ֽ�, ��ʼ��������ԴΪ�������.
     */
    private static final byte[] KEY_VI = DEFAULT_SECRET_KEY.substring(0, 16).getBytes();

    /**
     * ���ܽ����㷨/����ģʽ/��䷽ʽ
     */
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static java.util.Base64.Encoder base64Encoder = java.util.Base64.getEncoder();
    private static java.util.Base64.Decoder base64Decoder = java.util.Base64.getDecoder();

    static {
        java.security.Security.setProperty("crypto.policy", "unlimited");
    }

    /**
     * AES����
     * @return ���ܺ��ַ���
     */
    public static String AESencode(String key, String content) {
        try {
            javax.crypto.SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(key.getBytes(), AES);
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey, new javax.crypto.spec.IvParameterSpec(KEY_VI));

            // ��ȡ�������ݵ��ֽ�����(����Ҫ����Ϊutf-8)��Ȼ��������������ĺ�Ӣ�Ļ�����ľͻ����Ϊ����
            byte[] byteEncode = content.getBytes(java.nio.charset.StandardCharsets.UTF_8);

            // �����������ĳ�ʼ����ʽ����
            byte[] byteAES = cipher.doFinal(byteEncode);

            // �����ܺ������ת��Ϊ�ַ���
            return base64Encoder.encodeToString(byteAES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES����
     */
    public static String AESdecode(String key, String content) {
        try {
            javax.crypto.SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(key.getBytes(), AES);
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey, new javax.crypto.spec.IvParameterSpec(KEY_VI));

            // �����ܲ����������ݽ�����ֽ�����
            byte[] byteContent = base64Decoder.decode(content);
            // ����
            byte[] byteDecode = cipher.doFinal(byteContent);
            return new String(byteDecode, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getRandomString(int length){
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //��Random���������
            Random random=new Random();  
            StringBuffer sb=new StringBuffer();
            for(int i=0; i<length; ++i){
              int number=random.nextInt(62);
              sb.append(str.charAt(number));
            }
            return sb.toString();
      }
}
