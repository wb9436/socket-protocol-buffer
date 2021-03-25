package com.bomu.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class GToken {
    private static final Logger logger = LogManager.getLogger(GToken.class);
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    public static final String GTOKEN_SECRET = "!@ADEDerop)@--";
    private static final String USER_ID = "userId";
    private static final String AREA_ID = "areaId";
    private static AtomicInteger keyId = new AtomicInteger();

    /**
     * 验证信息是有效
     *
     * @param GToken
     * @param userId
     * @return
     */
    public static boolean verify(String GToken, String userId, String areaId) {
        if (GToken == null) {
            logger.info("GToken必填");
            return false;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(GTOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withClaim(USER_ID, userId).withClaim(AREA_ID, areaId).build();
            verifier.verify(GToken);
            return true;
        } catch (Exception e) {
            logger.error("校验JwtToken失败," + e.getMessage());
            return false;
        }
    }

    /**
     * 验证信息是有效
     * @param GToken
     * @return
     */
    public static boolean verify(String GToken) {
        if (GToken == null) {
            logger.info("GToken必填");
            return false;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(GTOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(GToken);
            return true;
        } catch (Exception e) {
            logger.error("校验JwtToken失败," + e.getMessage());
            return false;
        }
    }

    /**
     * 获取用户编号
     *
     * @param GToken
     * @return
     */
    public static String getAreaId(String GToken) {
        try {
            DecodedJWT jwt = JWT.decode(GToken);
            return jwt.getClaim(AREA_ID).asString();
        } catch (JWTDecodeException e) {
            logger.error("获取areaId失败," + e.getMessage());
            return "";
        }
    }

    /**
     * 获取用户编号
     *
     * @param GToken
     * @return
     */
    public static String getUserId(String GToken) {
        try {
            DecodedJWT jwt = JWT.decode(GToken);
            return jwt.getClaim(USER_ID).asString();
        } catch (JWTDecodeException e) {
            logger.error("获取userId失败," + e.getMessage());
            return "";
        }
    }

    /**
     * 创建GToken
     *
     * @param userId
     * @return
     */
    public static String create(String userId, String areaId) {
        Date expireTime = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(GTOKEN_SECRET);
        // 附带username信息
        return JWT.create()
                .withClaim(USER_ID, userId)
                .withClaim(AREA_ID, areaId)
//                .withIssuer("bomu")
//                .withIssuedAt(new Date())
//                .withExpiresAt(expireTime)
//                .withSubject("verify game user.")
//                .withKeyId(UUID.randomUUID().toString())
                .withKeyId(String.valueOf(keyId.getAndIncrement()))
                .sign(algorithm);
    }

    public static  void main(String[] args) {
        System.out.println(GToken.create("a","b"));
    }

}
