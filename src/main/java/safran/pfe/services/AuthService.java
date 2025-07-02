/*package safran.pfe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import safran.pfe.dto.*;
import safran.pfe.entities.Users;
import safran.pfe.entities.DTO.AuthResponse;
import safran.pfe.entities.DTO.LoginRequest;
import safran.pfe.entities.DTO.RegisterRequest;
import safran.pfe.repo.UserRepository;
import safran.pfe.config.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        Users user = Users.createUser(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                null,  // Les rôles peuvent être définis ici en fonction des besoins
                null   // Le département peut être défini ici
        );
        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return response;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = jwtUtil.generateToken((Users) auth.getPrincipal());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return response;
    }
}*/
