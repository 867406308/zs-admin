package com.zs.security.service;

import com.zs.framework.security.service.CustomUserDetailsService;
import com.zs.modules.sys.user.domain.dto.SysUserDTO;
import com.zs.modules.sys.user.service.ISysUserService;
import com.zs.security.model.LoginUserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Objects;

@Service
public class CustomUserDetailsServiceImpl  implements CustomUserDetailsService, UserDetailsService {
    @Resource
    private ISysUserService iSysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询数据库用户是否存在
        SysUserDTO sysUserDTO = iSysUserService.selectByUserName(username);
        if(Objects.isNull(sysUserDTO)){
            throw new UsernameNotFoundException("用户不存在");
        }
        return new LoginUserInfo(sysUserDTO, new HashSet<>());
    }
}
