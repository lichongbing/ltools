package com.lichongbing.ltools.utils;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 3:39 下午
 * @description: TODO
 */
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SM4Utils {

    public static final String SECRETKEY="JeF8U9wHFOMfs2Y8";

    private static final String IV = "UISwD9fW6cFh9SNS";

    private static boolean hexString = false;

    public SM4Utils()
    {
    }

    public static String encryptDataECB(String plainText)
    {
        try
        {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            if (hexString)
            {
                keyBytes = Util.hexStringToBytes(SECRETKEY);
            }
            else
            {
                keyBytes = SECRETKEY.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes("GBK"));
            String cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0)
            {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String decryptDataECB(String cipherText)
    {
        try
        {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            if (hexString)
            {
                keyBytes = Util.hexStringToBytes(SECRETKEY);
            }
            else
            {
                keyBytes = SECRETKEY.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, new BASE64Decoder().decodeBuffer(cipherText));
            return new String(decrypted, "GBK");
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String encryptDataCBC(String plainText)
    {
        try
        {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString)
            {
                keyBytes = Util.hexStringToBytes(SECRETKEY);
                ivBytes = Util.hexStringToBytes(IV);
            }
            else
            {
                keyBytes = SECRETKEY.getBytes();
                ivBytes = IV.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes("GBK"));
            String cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0)
            {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String decryptDataCBC(String cipherText)
    {
        try
        {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString)
            {
                keyBytes = Util.hexStringToBytes(SECRETKEY);
                ivBytes = Util.hexStringToBytes(IV);
            }
            else
            {
                keyBytes = SECRETKEY.getBytes();
                ivBytes = IV.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, new BASE64Decoder().decodeBuffer(cipherText));
            return new String(decrypted, "GBK");
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static void main(String[] args) throws IOException
    {
        String plainText = "1234rdegtfdedededededededededede5678";
        //SM4   ECB加密
        String cipherText = encryptDataECB(plainText);
        System.out.println(cipherText);
        //SM4   ECB解密
        plainText = decryptDataECB(cipherText);
        System.out.println(plainText);
        //SM4   CBC加密
        cipherText = encryptDataCBC(plainText);
        System.out.println(cipherText);
        //SM4   CBC解密
        plainText = decryptDataCBC(cipherText);
        System.out.println(plainText);
    }
}

