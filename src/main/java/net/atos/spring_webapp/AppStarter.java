package net.atos.spring_webapp;

import net.atos.spring_webapp.model.User;
import net.atos.spring_webapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//@Component
public class AppStarter implements CommandLineRunner {

    private UserRepository userRepository;
    private PermissionRepository permissionRepository;
    private PostRepository postRepository;
    private MessageRepository messageRepository;
    private CommentRepository commentRepository;

    // wstrzykiwanie przez konstruktor
    @Autowired
    public AppStarter(UserRepository userRepository, PermissionRepository permissionRepository, PostRepository postRepository, MessageRepository messageRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.postRepository = postRepository;
        this.messageRepository = messageRepository;
        this.commentRepository = commentRepository;
    }

    private void getAllUsers(){
        List<User> users = userRepository.findAll();
        printUsers(users);
    }

    private void getAllUsersSortedByEmail(){
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "email"));
        printUsers(users);
    }

    private void getAllUsersSortedByRegisterDate(){
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "registerDate"));
        printUsers(users);
    }

    private void printUserById(long userId){
        Optional<User> isUser = userRepository.findById(userId);
        if(isUser.isPresent()) {
            User user = isUser.get();
            System.out.printf("%5d|%20s|%10s|%7b|%22s  ---->  ",
                    user.getUserId(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getEnable(),
                    user.getRegisterDate());
            user.getRoles().stream().forEach(permission -> System.out.printf("%s ", permission.getRoleName()));
            System.out.println();
        } else {
            System.out.println("User with ID: "+userId+" not found!");
        }
    }

    private void printUsers(List<User> users){
        users.stream().forEach(
                user -> System.out.printf("%5d|%20s|%10s|%7b|%22s\n",
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                user.getEnable(),
                user.getRegisterDate())
        );
    }

    private long countUsers(){
        long usersCount = userRepository.count();
        return usersCount;
    }

    private void aggregatePermissions(){
        List<Object[]> permissions = userRepository.aggregatePermissions();
        permissions.stream().forEach(objects -> System.out.printf("Permission: %s (%s)\n",objects[0],objects[1]));
    }

    private void changeUserStatus(long userId, boolean status){
        Optional<User> isUser = userRepository.findById(userId);
        if(isUser.isPresent()){
            User user = isUser.get();
            user.setEnable(status);
            userRepository.save(user);
        }
    }

    private void updateUserStatus(boolean enable, int userId){
        userRepository.updateUserStatus(enable,userId);
        if(!enable){
            userRepository.deleteUserRoles(userId);
        }
    }

    private List<User> getUsersByStatus(boolean status){
        return userRepository.findAllByEnable(status);
    }

    @Transactional(
            rollbackFor = {Exception.class},
            readOnly = false
    )
    public void addManyUsers(List<User> users) throws Exception{
        for (int i = 0; i < users.size(); i++) {
            if (i==5){
                System.out.println("ROLLBACK");
                throw new Exception();
            }
            userRepository.save(users.get(i));
        }
    }







    @Override
    public void run(String... args) throws Exception {

        List<User> users = new ArrayList<User>(
                Arrays.asList(
                        new User("x1@xxx.com","111_xxX"),
                        new User("x2@xxx.com","111_xxX"),
                        new User("x3@xxx.com","111_xxX"),
                        new User("x4@xxx.com","111_xxX"),
                        new User("x5@xxx.com","111_xxX"),
                        new User("x6@xxx.com","111_xxX")
                )
        );
        try {
            addManyUsers(users);
        } catch(Exception e) {
            System.out.println("Nie zapisano!");
        }

        System.out.println("---------- get all users ("+countUsers()+") ----------");
        getAllUsersSortedByEmail();
//        getAllUsersSortedByRegisterDate();
        System.out.println("------------- change status of user 3 to false");
        updateUserStatus(false,3);
        getAllUsersSortedByEmail();
        System.out.println("------------------ all enabled users");
        getUsersByStatus(true).stream().forEach(user -> System.out.println(user.getEmail()));

//        System.out.println("---------- PERMISSIONS ----------");
//        aggregatePermissions();
    }
}
