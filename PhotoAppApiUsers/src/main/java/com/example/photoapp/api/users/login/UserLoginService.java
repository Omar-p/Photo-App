package com.example.photoapp.api.users.login;

import com.example.photoapp.api.users.AppUser;
import com.example.photoapp.api.users.User;
import com.example.photoapp.api.users.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
class UserLoginService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserLoginService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public AppUser findAppUserByEmail(String email) {
    return userRepository.findAppUserByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username)
        .map(UserAdapter::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public String getEmailFromPrincipal(Object principal) {
    return ((UserAdapter) principal).getEmail();
  }

  private static class UserAdapter extends User implements UserDetails {
    private UserAdapter(User user) {
      super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return Set.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
      return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
      return true;
    }

    @Override
    public boolean isAccountNonLocked() {
      return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    @Override
    public boolean isEnabled() {
      return true;
    }
  }
}
