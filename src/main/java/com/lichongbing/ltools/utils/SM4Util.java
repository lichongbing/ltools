package com.lichongbing.ltools.utils;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 3:36 下午
 * @description: TODO
 */
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class SM4Util {

    private static final String PADDING_MODE = "SM4/ECB/PKCS7Padding";

    private static final String FILE_MODE_READ = "r";

    private static final String FILE_MODE_READ_WRITE = "rw";

    private static final String PBK_SHA1 = "PBKDF2WithHmacSHA1";

    private static final String ALGORITHM_SM4 = "SM4";

    private static final int KEY_DEFAULT_SIZE = 128;

    private static final int ENCRYPT_BUFFER_SIZE = 1024;

    private static final int DECRYPT_BUFFER_SIZE = 1040;

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 生成ecb暗号
     * @param algorithmName
     * @param mode
     * @param key
     * @return
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key keySpec = new SecretKeySpec(key, ALGORITHM_SM4);
        cipher.init(mode, keySpec);
        return cipher;
    }


    /**
     * 自动生成密钥
     * @return
     */
    public static byte[] generateKey() throws Exception {
        return generateKey(KEY_DEFAULT_SIZE);
    }

    public static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_SM4, BouncyCastleProvider.PROVIDER_NAME);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        kg.init(keySize, random);

        return kg.generateKey().getEncoded();
    }


    public static String createKey(){
        try {
            return ByteUtils.toHexString(generateKey());
        } catch (Exception e) {
            throw new RuntimeException("生成密钥失败");
        }
    }


    /**
     * 文件加密
     *
     * @param sourceFilePath  源文件文件路径
     * @param encryptFilePath 加密后文件路径
     * @param seed            种子
     * @throws Exception 异常
     */
    public static void encryptFile(String sourceFilePath, String encryptFilePath, String seed) {
        try {
            sm4Cipher(Cipher.ENCRYPT_MODE, sourceFilePath, encryptFilePath, seed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件解密
     *
     * @param encryptFilePath 加密文件路径
     * @param targetFilePath  解密后文件路径
     * @param seed            种子
     * @throws Exception 异常
     */
    public static void decryptFile(String encryptFilePath, String targetFilePath, String seed) {
        try {
            sm4Cipher(Cipher.DECRYPT_MODE, encryptFilePath, targetFilePath, seed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件加解密过程
     *
     * @param cipherMode     加解密选项
     * @param sourceFilePath 源文件地址
     * @param targetFilePath 目标文件地址
     * @param seed           密钥种子
     * @throws Exception 出现异常
     */
    private static void sm4Cipher(int cipherMode, String sourceFilePath,
                                  String targetFilePath, String seed) {
        FileChannel sourcefc = null;
        FileChannel targetfc = null;

        try {
            byte[] rawKey = getRawKey(seed);
            Cipher mCipher = generateEcbCipher(cipherMode, rawKey);
            File sourceFile = new File(sourceFilePath);
            File targetFile = new File(targetFilePath);

            sourcefc = new RandomAccessFile(sourceFile, FILE_MODE_READ).getChannel();
            targetfc = new RandomAccessFile(targetFile, FILE_MODE_READ_WRITE).getChannel();

            int bufferSize = Cipher.ENCRYPT_MODE == cipherMode ? ENCRYPT_BUFFER_SIZE : DECRYPT_BUFFER_SIZE;
            ByteBuffer byteData = ByteBuffer.allocate(bufferSize);
            while (sourcefc.read(byteData) != -1) {
                // 通过通道读写交叉进行。
                // 将缓冲区准备为数据传出状态
                byteData.flip();

                byte[] byteList = new byte[byteData.remaining()];
                byteData.get(byteList, 0, byteList.length);
                //此处，若不使用数组加密解密会失败，因为当byteData达不到1024个时，加密方式不同对空白字节的处理也不相同，从而导致成功与失败。
                byte[] bytes = mCipher.doFinal(byteList);
                targetfc.write(ByteBuffer.wrap(bytes));
                byteData.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sourcefc != null) {
                    sourcefc.close();
                }
                if (targetfc != null) {
                    targetfc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 大字符串分段加密
     *
     * @param content 需要加密的内容
     * @param seed    种子
     * @return 加密后内容
     * @throws Exception 异常
     */
    public static String dataEncrypt(String content, String seed) {
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        Cipher mCipher = null;
        try {
            byte[] rawKey = getRawKey(seed);
            mCipher = generateEcbCipher(Cipher.ENCRYPT_MODE, rawKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        try {
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > ENCRYPT_BUFFER_SIZE) {
                    cache = mCipher.doFinal(data, offSet, ENCRYPT_BUFFER_SIZE);
                } else {
                    cache = mCipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * ENCRYPT_BUFFER_SIZE;
            }
        } catch (Exception e) {
            throw new RuntimeException("加密字符串【" + content + "】异常");
        }
        byte[] decryptedData = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(decryptedData);
    }

    /**
     * 大字符串分段解密
     *
     * @param content 密文
     * @param seed    种子
     * @return 解密后内容
     * @throws Exception 异常
     */
    public static String dataDecrypt(String content, String seed) {
        byte[] data = Base64.getDecoder().decode(content);
        Cipher mCipher = null;
        try {
            byte[] rawKey = getRawKey(seed);
            mCipher = generateEcbCipher(Cipher.DECRYPT_MODE, rawKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        try {
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > DECRYPT_BUFFER_SIZE) {
                    cache = mCipher.doFinal(data, offSet, DECRYPT_BUFFER_SIZE);
                } else {
                    cache = mCipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * DECRYPT_BUFFER_SIZE;
            }
        } catch (Exception e) {
            throw new RuntimeException("解密字符串【" + content + "】异常");
        }
        byte[] decryptedData = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * 字符串加密
     *
     * @param data 字符串
     * @param seed 种子
     * @return 加密后内容
     * @throws Exception 异常
     */
    public static String sm4Encrypt(String data, String seed) {
        try {
            byte[] rawKey = getRawKey(seed);
            Cipher mCipher = generateEcbCipher(Cipher.ENCRYPT_MODE, rawKey);
            byte[] bytes = mCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("加密字符串【" + data + "】异常");
        }
    }

    /**
     * 字符串解密
     *
     * @param data 加密字符串
     * @param seed 种子
     * @return 解密后内容
     * @throws Exception 异常
     */
    public static String sm4Decrypt(String data, String seed) {
        try {
            byte[] rawKey = getRawKey(seed);
            Cipher mCipher = generateEcbCipher(Cipher.DECRYPT_MODE, rawKey);
            byte[] bytes = mCipher.doFinal(Base64.getDecoder().decode(data));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串【" + data + "】异常");
        }
    }

    /**
     * 使用一个安全的随机数来产生一个密匙,密匙加密使用的
     *
     * @param seed 种子
     * @return 随机数组
     * @throws NoSuchAlgorithmException 模式错误
     */
    public static byte[] getRawKey(String seed) throws NoSuchAlgorithmException, InvalidKeySpecException {

        int count = 1000;
        int keyLen = KEY_DEFAULT_SIZE;
        int saltLen = keyLen / 8;
        //SecureRandom random = new SecureRandom();
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[saltLen];
        random.setSeed(seed.getBytes());
        random.nextBytes(salt);
        KeySpec keySpec = new PBEKeySpec(seed.toCharArray(), salt, count, keyLen);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBK_SHA1);
        return secretKeyFactory.generateSecret(keySpec).getEncoded();
    }

    /**
     * 生成ecb模式密码工具
     *
     * @param mode 模式
     * @param key  密钥
     * @return 密码工具
     * @throws Exception 异常
     */
    private static Cipher generateEcbCipher(int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(PADDING_MODE, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_SM4);
        cipher.init(mode, sm4Key);
        return cipher;
    }
}

