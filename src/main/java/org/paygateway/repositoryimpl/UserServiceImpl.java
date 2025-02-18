package org.paygateway.repositoryimpl;

import org.paygateway.config.JwtProvider;
import org.paygateway.exceptions.UserException;
import org.paygateway.model.User;
import org.paygateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserById(Long userId) throws UserException {
        return userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);

        return userRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found with email " + email));
    }
}
