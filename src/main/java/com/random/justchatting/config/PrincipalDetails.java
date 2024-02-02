package com.random.justchatting.config;

import com.random.justchatting.domain.login.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Setter
@Getter
@Slf4j
@NoArgsConstructor
@Component
public class PrincipalDetails implements UserDetails {
    private User user;

    public PrincipalDetails(User user){this.user = user;}

    @Override
    public String getUsername(){return user.getUuId();}

    @Override
    public String getPassword(){return user.getApiKey();}

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
