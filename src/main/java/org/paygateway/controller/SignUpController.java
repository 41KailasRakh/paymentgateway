package org.paygateway.controller;

import org.paygateway.config.JwtProvider;
import org.paygateway.exceptions.UserException;
import org.paygateway.model.User;
import org.paygateway.pojo.AuthResponse;
import org.paygateway.pojo.LoginRequest;
import org.paygateway.repository.UserRepository;
import org.paygateway.repositoryimpl.CartServiceImpl;
import org.paygateway.repositoryimpl.CustomUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/app/auth")
public class SignUpController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CartServiceImpl cartService;
    @Autowired
    private CustomUserServiceImpl customUserService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody User user) throws UserException {

        String email = user.getEmail();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        if(userRepository.findByEmail(email).isPresent()) {
            throw new UserException("User Already Exists with email " + email);
        }
        String password = passwordEncoder.encode(user.getPassword());
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPassword(password);
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);
        createdUser.setRole("USER");
        User savedUser = userRepository.save(createdUser);
        cartService.createCart(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(new AuthResponse("Sign In To get JWT", "Signup Success"), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest) {
        System.out.println("REQUEST COME");
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not found"));
        String jwtToken = jwtProvider.generateToken(authentication, user.getRole());
        return new ResponseEntity<>(new AuthResponse(jwtToken, "Signin Successful"), HttpStatus.ACCEPTED);

    }

    private Authentication authenticate(String userName, String password){
        UserDetails userDetails = customUserService.loadUserByUsername(userName);
        if (userDetails == null) throw new BadCredentialsException("Invalid username");

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password ");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
