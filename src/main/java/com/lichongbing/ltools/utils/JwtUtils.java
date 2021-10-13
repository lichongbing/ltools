package com.lichongbing.ltools.utils;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 3:09 下午
 * @description: TODO
 */
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtils {

    public static final long EXPIRE = 1000 * 60 * 60 * 6;  //token 过期时间
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";  //秘钥


    /**
     * 生成token字符串的方法
     *
     * @param id
     * @param nickname
     * @return
     */
    public static String getJwtToken(String id, String nickname) {
        String JwtToken = Jwts.builder().setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")

                .setSubject("bazhong-yichuangshouli")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))

                .claim("id", id)
                .claim("nickname", nickname)

                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();

        return JwtToken;
    }


    /**
     * 判断token是否存在与有效
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * 判断token 是否存在与有效
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request){

        try {
            String token = request.getHeader("token");
            if (StringUtils.isEmpty(token)){
                return false;
            }

            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


        return true;
    }




    public static String getUserIdByJwtToken(HttpServletRequest request){
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)){
            return "";
        }

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();

        return (String) claims.get("id");
    }




}

