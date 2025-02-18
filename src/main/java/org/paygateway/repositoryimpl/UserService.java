package org.paygateway.repositoryimpl;

import org.paygateway.exceptions.UserException;
import org.paygateway.model.User;

public interface UserService {
    User findUserById(Long userId) throws UserException;
    User findUserProfileByJwt(String jwt) throws UserException;
}
