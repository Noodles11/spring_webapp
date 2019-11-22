package net.atos.spring_webapp.service;

import net.atos.spring_webapp.model.Permission;
import net.atos.spring_webapp.model.User;
import net.atos.spring_webapp.repository.PermissionRepository;
import net.atos.spring_webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private PermissionRepository permissionRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<User> getAllUsersOrdered(Sort.Direction direction){
        return userRepository.findAll(Sort.by(direction,"userId"));
    }

    public void registerUser(User user){
        assignPermissionToUser(user, 1);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User assignPermissionToUser(User user, int roleId){
        Optional<Permission> optionalPermission = permissionRepository.findById((byte) roleId);
        Permission role = null;
        if(optionalPermission.isPresent()){
            role = optionalPermission.get();
            user.addPermission(role);
        }
        return user;
    }

    public User getUserById(long userId) {
        Optional<User> optUser = userRepository.findById(userId);
        User user = null;
        if( optUser.isPresent() ){
            user = optUser.get();
        }
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }


}
