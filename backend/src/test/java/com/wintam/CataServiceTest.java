package com.wintam;

import com.wintam.model.User;
import com.wintam.service.CataServiceSpring;
import com.wintam.service.KarmaServiceSpring;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wintam.repository.UserRepository;
import com.wintam.repository.CataRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CataServiceTest {

    @Mock
    private CataRepository cataRepository;

    @Mock
    private KarmaServiceSpring karmaService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CataServiceSpring cataService;

    @InjectMocks
    private KarmaServiceSpring karmaServiceSpring;

    @Test
    void codigoDebeSerCincoDigitos() {
        String codigo = cataService.getCode();
        assertEquals(5, codigo.length());
        assertTrue(codigo.matches("\\d{5}"));
    }

    @Test
    void recompensaAsistenciaSubeKarma() {
        User user = new User();
        user.setKarma(50);
        karmaServiceSpring.rewardAttendance(user);
        assertEquals(65, user.getKarma());
    }

    @Test
    void penalizacionAsistenteRestaKarma() {
        User user = new User();
        user.setKarma(50);
        karmaServiceSpring.penalizeAttendee(user);
        assertEquals(40, user.getKarma());
    }

}
