package ucr.ac.cr.EspaciosCreativos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ucr.ac.cr.EspaciosCreativos.model.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository <User, Integer> {

    List<User> findByName(String name);
    List<User> findAllByOrderByNameAsc();
    User findByEmailAndPassword(String email, String password);
    /*
    User findByEmail(String email);
    List<User> findByRol(String rol);
     */

    //JPQL
    @Query("SELECT u FROM User u WHERE u.rol = :rol")
    List<User> buscarPorRol(@Param("rol") String rol);

    @Query("SELECT u FROM User u WHERE u.email=:email AND u.password=:password")
    User loginDTO(@Param("email") String email, @Param("password") String password);



}
