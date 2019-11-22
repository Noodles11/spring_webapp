package net.atos.spring_webapp.repository;

import net.atos.spring_webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            nativeQuery = true,
            value = "select p.role_name, count(*) from user u join user_permission up on u.user_id = up.user_id join permission p on p.permission_id = up.permission_id group by p.role_name order by 2 desc"
    )
    List<Object[]> aggregatePermissions();

    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value = "UPDATE user SET enable = :enable WHERE user_id = :user_id"
    )
    void updateUserStatus(@Param("enable") boolean enable, @Param("user_id") int userId);

    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value = "delete from user_permission where user_id = ?"
    )
    void deleteUserRoles(int userId);

    @Query(
            nativeQuery = true,
            value = "select * from user where enable = ?"
    )
    List<User> findAllByEnable(boolean status);

    @Transactional()
    @Override
    User save(User user);

    User findFirstByEmail(String email);

//    @Transactional(
//            rollbackFor = {Exception.class},
//            readOnly = false
//    )
//    void addManyUsers(List<User> users) throws Exception;
}
