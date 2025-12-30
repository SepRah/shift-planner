package com.example.shiftplanner.application.security;

import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.staff.StaffMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftplannerUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userService.findByUsername(username);

        List<GrantedAuthority> authorities = new ArrayList<>();

        // 🔐 System roles
        user.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );

        // 🧑‍💼 Staff qualification
        StaffMember staff = user.getStaffmember();
        if (staff != null) {
            authorities.add(new SimpleGrantedAuthority(
                    "STAFF_" + staff.getStaffQualificationLevel().name()
            ));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                user.isActive(),
                true,
                true,
                true,
                authorities
        );
    }
}