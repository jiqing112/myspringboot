package cn.mypandora.springboot.core.shiro.realm;

import cn.mypandora.springboot.core.shiro.matcher.JwtMatcher;
import cn.mypandora.springboot.core.shiro.matcher.PasswordMatcher;
import cn.mypandora.springboot.core.shiro.token.JwtToken;
import cn.mypandora.springboot.core.shiro.token.PasswordToken;
import cn.mypandora.springboot.modular.system.service.UserService;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * BaseRealm
 * 需要注入自己的接口，判断用户认证结果、授权结果。
 *
 * @author hankaibo
 * @date 2019/1/12
 */
@Component
public class RealmManager {

    /**
     * 注入接口，以供登录难时调用数据库获取用户进行比对。
     */
    private UserService userService;

    private PasswordMatcher passwordMatcher;
    private JwtMatcher jwtMatcher;

    @Autowired
    public RealmManager(UserService userService, PasswordMatcher passwordMatcher, JwtMatcher jwtMatcher) {
        this.userService = userService;
        this.passwordMatcher = passwordMatcher;
        this.jwtMatcher = jwtMatcher;
    }

    public List<Realm> initGetRealm() {
        List<Realm> realmList = new LinkedList<>();
        // ----- password
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setUserService(userService);
        passwordRealm.setCredentialsMatcher(passwordMatcher);
        passwordRealm.setAuthenticationTokenClass(PasswordToken.class);
        realmList.add(passwordRealm);
        // ----- jwt
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCredentialsMatcher(jwtMatcher);
        jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(jwtRealm);
        return Collections.unmodifiableList(realmList);
    }
}
