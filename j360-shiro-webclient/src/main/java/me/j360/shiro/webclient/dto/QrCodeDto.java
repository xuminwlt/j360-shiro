package me.j360.shiro.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Package: com.fotoplace.outer.dto
 * User: min_xu
 * Date: 2016/12/26 下午2:20
 * 说明：
 */

@Data
@AllArgsConstructor
public class QrCodeDto {

    private String uuid;
    private String qrcode;
    private String qrUrl;

}
