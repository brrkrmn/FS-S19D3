package com.workintech.s19d2.service;

import com.workintech.s19d2.dto.RegisterResponse;
import com.workintech.s19d2.dto.RegistrationMember;
import com.workintech.s19d2.entity.Member;
import com.workintech.s19d2.entity.Role;
import com.workintech.s19d2.repository.MemberRepository;
import com.workintech.s19d2.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {
    private MemberRepository memberRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member register(String email, String password) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with given email already exists");
        }

        String encoded = passwordEncoder.encode(password);

        Role adminRole = roleRepository.findByAuthority("ADMIN").get();
        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);

        Member member = new Member();
        member.setEmail(email);
        member.setPassword(encoded);
        member.setRoles(roles);

        return memberRepository.save(member);
    }
}
