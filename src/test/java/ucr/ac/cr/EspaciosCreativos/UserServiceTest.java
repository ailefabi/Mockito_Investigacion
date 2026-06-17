package ucr.ac.cr.EspaciosCreativos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ucr.ac.cr.EspaciosCreativos.model.dto.UserDTO;
import ucr.ac.cr.EspaciosCreativos.model.entity.User;
import ucr.ac.cr.EspaciosCreativos.repository.UserRepository;
import ucr.ac.cr.EspaciosCreativos.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

        /**
         * @Mock crea una simulación (mock) de UserRepository.
         * Este objeto NO se conecta a una base de datos real;
         */
        @Mock
        private UserRepository userRepository;

        /**
         * @InjectMocks crea una instancia real de UserService
         * e inyecta automáticamente el mock de UserRepository
         * en el campo correspondiente (anotado con @Autowired en la clase real).
         */
        @InjectMocks
        private UserService userService;

        // Objetos de prueba reutilizables, inicializados antes de cada test
        private User user;

        /**
         * @BeforeEach se ejecuta antes de cada prueba individual.
         * Aquí preparamos un objeto User base para evitar repetir código
         * en cada metodo de test.
         */
        @BeforeEach
        void setUp() {
            user = new User(1, "Fabián Rodríguez", "fabian@ucr.ac.cr", "Clave123@", "ESTUDIANTE");
        }


    // ====================================================
    // 1. PRUEBA: Guardar un usuario nuevo (escenario exitoso)
    // ====================================================
    @Test
    void saveUser_DeberiaGuardarUsuario_CuandoNoExisteElId() {
        // Arrange (preparación):
        // Simulamos que el repositorio NO encuentra el usuario por id (Optional vacío)
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        // Simulamos que al guardar, el repositorio devuelve el mismo usuario
        when(userRepository.save(user)).thenReturn(user);

        // Act (ejecución):
        UserDTO resultado = userService.saveUser(user);

        // Assert (verificación):
        assertNotNull(resultado);
        assertEquals(user.getId(), resultado.getId());
        assertEquals(user.getEmail(), resultado.getEmail());
        assertEquals(user.getName(), resultado.getName());

        // Verificamos que los métodos del repositorio fueron invocados correctamente
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(user);
    }


    // ====================================================
    // 2. PRUEBA: Guardar un usuario que ya existe (escenario de error)
    // ====================================================
    @Test
    void saveUser_DeberiaRetornarNull_CuandoElUsuarioYaExiste() {
        // Arrange: simulamos que el repositorio SÍ encuentra un usuario con ese id
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        UserDTO resultado = userService.saveUser(user);

        // Assert: el metodo debe devolver null según la lógica de negocio
        assertNull(resultado);

        // Verificamos que jamás se intentó guardar un usuario duplicado
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, never()).save(any(User.class));
    }


    // ====================================================
    // 3. PRUEBA: Buscar todos los usuarios (escenario exitoso)
    // ====================================================
    @Test
    void findAll_DeberiaRetornarListaDeUsuarios() {
        // Arrange
        User user2 = new User(2, "Ana Pérez", "ana@ucr.ac.cr", "Clave456@", "ADMIN");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        // Act
        List<UserDTO> resultado = userService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Fabián Rodríguez", resultado.get(0).getName());
        assertEquals("Ana Pérez", resultado.get(1).getName());

        verify(userRepository, times(1)).findAll();
    }

    // ====================================================
    // 4. PRUEBA: Buscar usuario por id existente (escenario exitoso)
    // ====================================================
    @Test
    void findByIDUser_DeberiaRetornarUsuario_CuandoExiste() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        UserDTO resultado = userService.findByIDUser(1);

        // Assert
        assertNotNull(resultado);
        assertEquals("fabian@ucr.ac.cr", resultado.getEmail());

        verify(userRepository, times(1)).findById(1);
    }

}
