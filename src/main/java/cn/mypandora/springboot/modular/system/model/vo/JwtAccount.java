package cn.mypandora.springboot.modular.system.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * JwtAccount
 *
 * @author hankaibo
 * @date 2019/6/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAccount implements Serializable {
    private static final long serialVersionUID = 7206625063123589636L;

    private String tokenId;
    private String appId;
    private String issuer;
    private Date issuedAt;
    private String audience;
    private String roles;
    private String resources;
    private String host;
}
