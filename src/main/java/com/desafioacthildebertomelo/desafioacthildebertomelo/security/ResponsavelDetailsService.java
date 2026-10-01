package com.desafioacthildebertomelo.desafioacthildebertomelo.security;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ResponsavelRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ResponsavelDetailsService implements UserDetailsService {

    private final ResponsavelRepository responsavelRepository;

    public ResponsavelDetailsService(ResponsavelRepository responsavelRepository) {
        this.responsavelRepository = responsavelRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Responsavel responsavel = responsavelRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Responsável não encontrado: " + email));

        // Aqui assumimos que o campo 'cargo' pode ser usado como role
        return User.builder()
                .username(responsavel.getEmail())
                .password(responsavel.getSenha()) // precisa estar criptografada com BCrypt
                .roles(responsavel.getCargo())    // exemplo: "ADMIN", "USER"
                .build();
    }
}
