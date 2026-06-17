package ucr.ac.cr.EspaciosCreativos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ucr.ac.cr.EspaciosCreativos.model.dto.LoginUserDTO;
import ucr.ac.cr.EspaciosCreativos.model.dto.UserDTO;
import ucr.ac.cr.EspaciosCreativos.model.entity.User;
import ucr.ac.cr.EspaciosCreativos.service.UserService;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> findall(){
        return ResponseEntity.ok(this.userService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Validated @RequestBody User user, BindingResult result){
        if (result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()){
                errors.put(error.getField(),error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        UserDTO dto = this.userService.saveUser(user);
        if (dto == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario con el ID "+user.getId()+" ya se encuentra registrado.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name){
        if (this.userService.findByName(name).isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Lista Vacia!");
        }
        return ResponseEntity.ok(this.userService.findByName(name));
    }

    @GetMapping("/order")
    public ResponseEntity<?> findAllByOrderByName(){
        if (this.userService.findAllByOrderByNameAsc().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Lista Vacia!");
        }
        return ResponseEntity.ok(this.userService.findAllByOrderByNameAsc());
    }

    @GetMapping("/list-rol/{rol}")
    public ResponseEntity<?> buscarRoles(@PathVariable String rol) {
        return ResponseEntity.ok(this.userService.buscarPorRol(rol));
    }

    @GetMapping("/{email}/{password}")
    public ResponseEntity<?> findByEmailAndPassword(@PathVariable String email, @PathVariable String password){
        return ResponseEntity.ok(this.userService.findByEmailAndPassword(email, password));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDTO dtologin){
        User user = this.userService.login(dtologin.getEmail(),dtologin.getPassword());
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }
        return ResponseEntity.ok("Bienvenido "+user.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            this.userService.deleteUser(id);
            return ResponseEntity.ok("Usuario eliminado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@PathVariable Integer id, @RequestBody User userEdit) {
        UserDTO dto = this.userService.editUser(id, userEdit);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
        return ResponseEntity.ok(dto);
    }


}
