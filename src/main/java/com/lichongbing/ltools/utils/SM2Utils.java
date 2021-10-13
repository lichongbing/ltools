package com.lichongbing.ltools.utils;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class SM2Utils {

    //生成随机秘钥对
    public static Map<String, String> generateKeyPair(){
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();
        Map<String, String> map = new HashMap<String, String>();
        map.put("publicKey", Util.byteToHex(publicKey.getEncoded()));
        map.put("privateKey", Util.byteToHex(privateKey.toByteArray()));

        return map;
    }

    //数据加密
    public static String encrypt(byte[] publicKey, byte[] data) throws IOException
    {
        if (publicKey == null || publicKey.length == 0)
        {
            return null;
        }

        if (data == null || data.length == 0)
        {
            return null;
        }

        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);

        Cipher cipher = new Cipher();
        SM2 sm2 = SM2.Instance();
        ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);

        ECPoint c1 = cipher.Init_enc(sm2, userKey);
        cipher.Encrypt(source);
        byte[] c3 = new byte[32];
        cipher.Dofinal(c3);

//		////System.out.println("C1 " + Util.byteToHex(c1.getEncoded()));
//		////System.out.println("C2 " + Util.byteToHex(source));
//		////System.out.println("C3 " + Util.byteToHex(c3));
        //C1 C2 C3拼装成加密字串
        return Util.byteToHex(c1.getEncoded()) + Util.byteToHex(source) + Util.byteToHex(c3);

    }

    //数据解密
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) throws IOException
    {
        if (privateKey == null || privateKey.length == 0)
        {
            return null;
        }

        if (encryptedData == null || encryptedData.length == 0)
        {
            return null;
        }
        //加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
        String data = Util.byteToHex(encryptedData);
        /***分解加密字串
         * （C1 = C1标志位2位 + C1实体部分128位 = 130）
         * （C3 = C3实体部分64位  = 64）
         * （C2 = encryptedData.length * 2 - C1长度  - C2长度）
         */
        byte[] c1Bytes = Util.hexToByte(data.substring(0,130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = Util.hexToByte(data.substring(130,130 + 2 * c2Len));
        byte[] c3 = Util.hexToByte(data.substring(130 + 2 * c2Len,194 + 2 * c2Len));

        SM2 sm2 = SM2.Instance();
        BigInteger userD = new BigInteger(1, privateKey);

        //通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.Init_dec(userD, c1);
        cipher.Decrypt(c2);
        cipher.Dofinal(c3);

        //返回解密结果
        return c2;
    }

    public static void main(String[] args) throws Exception
    {
        String[][] arr = {
                /**
                 * 测试地址
                */
                { "local.url", "jdbc:oracle:thin:@192.168.3.88:1521:asiayak" },
                { "local.username", "WXGZH" },
                { "local.password", "asiayak" },

                { "sg186.url", "jdbc:oracle:thin:@192.168.3.99:1521:asiayak" },
                { "sg186.username", "EPM_VIEW1" },
                { "sg186.password", "asiayak" },

                { "zscd.url", "jdbc:oracle:thin:@192.168.3.99:1521:asiayak" },
                { "zscd.username", "sgmpuser" },
                { "zscd.password", "asiayak" },

                /**
                 * 安评地址
                */
//				{ "local.url", "jdbc:oracle:thin:@192.168.3.142:1521:orcl" },
//				{ "local.username", "WXGZH" },
//				{ "local.password", "85317979.N18a" },
//
//				{ "sg186.url", "jdbc:oracle:thin:@192.168.3.142:1521:orcl" },
//				{ "sg186.username", "epm_sc" },
//				{ "sg186.password", "85317979.N18a" },
//
//				{ "zscd.url", "jdbc:oracle:thin:@192.168.3.142:1521:orcl" },
//				{ "zscd.username", "sgmpuser" },
//				{ "zscd.password", "85317979.N18a" },

        };
        for (int i=0;i<arr.length;i++) {
            //私钥     217777cdea0afc2925c71445f3be6a9a61d2de2bb015f609d18ee86a247cab0a
//			String key=SM2Utils.encrypt(
//					Util.hexToByte("04225322497cf85cffaff3d749de214f9f46c2f99879bc60f744d25e471e3abbe91fb7780bab6205edaabc2d5abf725ff77f31d84220f69e26ecd0a98309e8ab31")
//					, arr[i][1].getBytes());
            String key=Util.signStr(
                    arr[i][1]);

            //System.out.println(arr[i][0]+"="+key);
//			String key = AESEncryptor.aesEncrypt2(arr[i][1], AESEncryptor.key);
//			////System.out.println(arr[i][0]+"="+key);
//			////System.out.println(arr[i][0]+"解密"+SM2Utils.decrypt(
//					Util.hexToByte("217777cdea0afc2925c71445f3be6a9a61d2de2bb015f609d18ee86a247cab0a")
//					, Util.hexToByte(arr[i][1].getBytes())));
            ////System.out.println(arr[i][0]+"解密"+Util.decryptStr(key));
            //(cipherText)
        }
        //生成密钥对
//		generateKeyPair();
//		JSONObject j = new JSONObject();
//		j.put("CONS_NO", "12344567890");
//		j.put("CONS_NAME", "答案的");
//
//		JSONArray ja = new JSONArray();
//		JSONObject j1 = new JSONObject();
//		j1.put("METER_NO", "5345116463121451321");
//		ja.add(j1);
//		JSONObject j2 = new JSONObject();
//		j2.put("METER_NO", "5348979779797979789");
//		ja.add(j2);
//
//		j.put("METER_LIST", ja);
//
//		j.put("TIME", DateTimeUtil.getDateTime());
//
//		String plainText = j.toJSONString();
//		byte[] sourceData = plainText.getBytes();
//
//		Map<String, String> map = generateKeyPair();
//		String pubk = map.get("publicKey");
//		String prik = map.get("privateKey");
//		//System.out.println("公钥："+pubk);
//		//System.out.println("私钥："+prik);
//		////System.out.println("====================================");
//
//		////System.out.println("加密: ");
//		String cipherText = SM2Utils.encrypt(Util.hexToByte(pubk), sourceData);
//		////System.out.println(cipherText);
//		////System.out.println("====================================");
//
//		////System.out.println("解密: ");
//		plainText = new String(SM2Utils.decrypt(Util.hexToByte(prik), Util.hexToByte(cipherText)));
//		////System.out.println(plainText);
//
//		JSONObject jj = JSONObject.parseObject(plainText);
//		////System.out.println(DateTimeUtil.getDate2StrYYmmDDHHmmss(jj.getDate("TIME")));

    }
}
