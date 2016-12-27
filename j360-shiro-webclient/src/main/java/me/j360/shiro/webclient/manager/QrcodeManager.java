package me.j360.shiro.webclient.manager;

import me.j360.shiro.webclient.core.JedisTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QrcodeManager {

    // 有效期
    private static final int EXPIRED_QRCODE = 5*60;

    private static final int STATE_EXPIRED = -1;
    private static final int STATE_EMPTY = 0;
    private static final int STATE_BANDING = 1;
    private static final int STATE_ALLOW = 2;
    private static final int STATE_REFUSE = 3;

    private static final String PARAM_UID = "PARAM_UID";
    private static final String PARAM_USERNAME = "PARAM_USERNAME";
    private static final String PARAM_DIGEST = "PARAM_DIGEST";
    private static final String PARAM_STATE = "PARAM_STATE";


    @Autowired
    private JedisTemplate jedisTemplate;

    /**
     * 生成唯一标识
     * @param uuid
     * @return
     */
    public QrCodeDto createQrcode(String uuid) {

        String qrcode = Identities.uuid2();

        String url = "";

        return new QrCodeDto(uuid,qrcode,url);
    }

    /**
     * 用户绑定唯一标识
     */
    public boolean bindingQrcode(Long uid, String qrcode) {

        jedisTemplate.set(qrcode, String.valueOf(uid));
        jedisTemplate.setExpire(qrcode, EXPIRED_QRCODE);// 设置过期时间

        return true;

    }

    /**
     * 检查绑定帐号
     */
    public QrState checkState(String uuid, String qrcode) {
        // 校验二维码有效性
        String temp_qrcode = jedisTemplate.get(uuid);
        if (StringUtils.isNotEmpty(temp_qrcode) && StringUtils.equals(temp_qrcode, qrcode)) {
            // 检测二维码状态
            String state = jedisTemplate.hget(qrcode, PARAM_STATE);
            if (StringUtils.isNotEmpty(state)) {
                return new QrState(Integer.parseInt(state));
            }

            // 未绑定
            return new QrState(STATE_EMPTY);
        }
        // 二维码已失效
        return new QrState(STATE_EXPIRED);
    }

    /**
     * 移动端登录确认
     */
    public boolean confirmLogin(Long uid, String qrcode, boolean confirm) {
        int state = confirm ? STATE_ALLOW : STATE_REFUSE;
        jedisTemplate.hset(qrcode, PARAM_STATE, String.valueOf(state));
        return true;
    }

    /**
     * 获取登录用户 uid
     */
    public String getLoginUid(String qrcode) {
        String uid = jedisTemplate.hget(qrcode, PARAM_UID);
        return StringUtils.isNotEmpty(uid)?uid:"";
    }



    /**
     * 清除缓存
     */
    public void clearQrcode(String qrcode) {
        jedisTemplate.del(qrcode);
    }

}
