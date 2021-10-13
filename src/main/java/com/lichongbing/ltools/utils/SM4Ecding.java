package com.lichongbing.ltools.utils;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 3:17 下午
 * @description: TODO
 */
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.*;
import java.util.Arrays;

public class SM4Ecding {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String ENCODING = "UTF-8";
    public static final String ALGORITHM_NAME = "SM4";
    // 加密算法/分组加密模式/分组填充方式
    // PKCS5Padding-以8个字节为一组进行分组加密
    // 定义分组加密模式使用：PKCS5Padding
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    // 128-32位16进制；256-64位16进制
    public static final int DEFAULT_KEY_SIZE = 128;

    public static final String KEY = "7e27bd95ecce14bc5432ddf67b749fec";
    //用于加密phone+id消息
    public static final String privateKeyForMsg = "da01a33a4a43b111ad9ef212f26eb2f7";

    /**
     * 生成ECB暗号
     *
     * @explain ECB模式（电子密码本模式：Electronic codebook）
     * @param algorithmName
     *            算法名称
     * @param mode
     *            模式
     * @param key
     * @return
     * @throws Exception
     */
    private static javax.crypto.Cipher generateEcbCipher(String algorithmName, int mode,
                                                         byte[] key) throws Exception {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(algorithmName,
                BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }


    /**
     * 自动生成密钥
     *
     * @explain
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static byte[] generateKey() throws Exception {
        return generateKey(DEFAULT_KEY_SIZE);
    }

    /**
     * @explain
     * @param keySize
     * @return
     * @throws Exception
     */
    public static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME,
                BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * sm4加密
     *
     * @explain 加密模式：ECB 密文长度不固定，会随着被加密字符串长度的变化而变化
     * @param hexKey
     *            16进制密钥（忽略大小写）
     * @param paramStr
     *            待加密字符串
     * @return 返回16进制的加密字符串
     * @throws Exception
     */
    public static String encryptEcb(String hexKey, String paramStr)
            throws Exception {
        String cipherText = "";
        // 16进制字符串--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // String--&gt;byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 加密后的数组
        byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
        // byte[]--&gt;hexString
        cipherText = ByteUtils.toHexString(cipherArray);
        return cipherText;
    }

    /**
     * sm4加密
     *
     * @explain 加密模式：ECB 密文长度不固定，会随着被加密字符串长度的变化而变化
     * @param paramStr
     *            待加密字符串
     * @return 返回16进制的加密字符串
     * @throws Exception
     */
    public static String encryptEcb(String paramStr)
            throws Exception {
        String cipherText = "";
        // 16进制字符串--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(KEY);
        // String--&gt;byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 加密后的数组
        byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
        // byte[]--&gt;hexString
        cipherText = ByteUtils.toHexString(cipherArray);
        return cipherText;
    }

    /**
     * 加密模式之Ecb
     *
     * @explain
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data)
            throws Exception {
        javax.crypto.Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING,
                javax.crypto.Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * sm4解密
     *
     * @explain 解密模式：采用ECB
     * @param hexKey
     *            16进制密钥
     * @param cipherText
     *            16进制的加密字符串（忽略大小写）
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decryptEcb(String hexKey, String cipherText)
            throws Exception {
        // 用于接收解密后的字符串
        String decryptStr = "";
        // hexString--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // hexString--&gt;byte[]
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        // 解密
        byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
        // byte[]--&gt;String
        decryptStr = new String(srcData, ENCODING);
        return decryptStr;
    }

    /**
     * sm4解密
     *
     * @explain 解密模式：采用ECB
     * @param cipherText
     *            16进制的加密字符串（忽略大小写）
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decryptEcb(String cipherText)
            throws Exception {
        // 用于接收解密后的字符串
        String decryptStr = "";
        // hexString--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(KEY);
        // hexString--&gt;byte[]
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        // 解密
        byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
        // byte[]--&gt;String
        decryptStr = new String(srcData, ENCODING);
        return decryptStr;
    }

    /**
     * 解密
     *
     * @explain
     * @param key
     * @param cipherText
     * @return
     * @throws Exception
     */
    public static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText)
            throws Exception {
        javax.crypto.Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING,
                Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    /**
     * 校验加密前后的字符串是否为同一数据
     *
     * @explain
     * @param hexKey
     *            16进制密钥（忽略大小写）
     * @param cipherText
     *            16进制加密后的字符串
     * @param paramStr
     *            加密前的字符串
     * @return 是否为同一数据
     * @throws Exception
     */
    public static boolean verifyEcb(String hexKey, String cipherText,
                                    String paramStr) throws Exception {
        // 用于接收校验结果
        boolean flag = false;
        // hexString--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // 将16进制字符串转换成数组
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        // 解密
        byte[] decryptData = decrypt_Ecb_Padding(keyData, cipherData);
        // 将原字符串转换成byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 判断2个数组是否一致
        flag = Arrays.equals(decryptData, srcData);
        return flag;
    }

    public static void main(String[] args) {
//        try {
//
//            String signature =  SM3Utils.encrypt("" + "" + "");
//            System.out.println("Signature数字签名:"+signature);
////			System.out.println("5:"+DateTimeUtil.getTodayStr(4));
//            String bb= "";
//            System.out.println(bb);
//            String aa = encryptEcb("", bb);
//            System.out.println(aa);
//            System.out.println(decryptEcb("",""));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            String data = "7d6a677a1e0b83ad56bb97007a7fcb96d3c8d179bd02ac47e1fbc924bc0fd3fcfe637549d7333df7bca5ff2cc8cc315951d0d505b694078fea7c2369b38d296f4058059c573350666cf8ed62617b671cae3dc2523514c7ecd740cb54d9f51c25c7ed370dc388fd54eb03aa536806f35d44838392ab87378fd78d73a677da66baa153161f0c480357e637e34f6bb2fc46c852040dc253997ceadecc07b703c89bd6cd4912ed8af1ea36cca8f4d2bf00daa5854594a617a2c37c8d27d7b2540caa1ae34613d74e4286806bbcfdf493ae024199b073bfea570c4d56f385e9380b871d548607efb2bcebe2e737373cc75259649f499a7a87132f8f6bedcfc647621d6fd4776922a9a969fd37695386bbac588dc62c23b1d9dfd36076bb33f79b8202881977b188ae53f0c1b3d3cf0a2c2dd7f8d4b41179d16e85330950cd966d51881dd44a78eee0c6110ed55f8d3992241e739a8c45ce8eb8315b880624833a076165d96086763baf4f793f7e575a444e1b40b526d4264e31f702014a9afe04f5c965c15fa40323313e0c7abb92691435dcc0c9cd2b6fc4da96b1c4877698a8e804649f499a7a87132f8f6bedcfc647621d7ab18dae029a01c136d1ae995e1333ad9d431c7570b634b55233b2f9bcbb452dc84bca01e44f48d993d9922f10aab93929c543c1d6b122360f7af03281ef2f1116b1ae27d06eedd94c6974c648a2e47be3278ce210e570263922242ebe46f430ef32449b40cb589f7df9826b12bcf4adf7266b1b61cea5bf7dd17e37d58c1079119bc666ef39124341c09aa58f69f717c3db90d435f9e74fa044fa0948beb8b92dcbde32fe80f9586f203e63820267ed";

            String s = decryptEcb("c085ebc6ad0800a6e0530ab0209200a6",data);
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String encryptEcbForMsg(String paramStr)
            throws Exception {
        String cipherText = "";
        // 16进制字符串--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(privateKeyForMsg);
        // String--&gt;byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 加密后的数组
        byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
        // byte[]--&gt;hexString
        cipherText = ByteUtils.toHexString(cipherArray);
        return cipherText;
    }

    public static String decryptEcbForMsg(String cipherText)
            throws Exception {
        // 用于接收解密后的字符串
        String decryptStr = "";
        // hexString--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(privateKeyForMsg);
        // hexString--&gt;byte[]
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        // 解密
        byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
        // byte[]--&gt;String
        decryptStr = new String(srcData, ENCODING);
        return decryptStr;
    }
}

