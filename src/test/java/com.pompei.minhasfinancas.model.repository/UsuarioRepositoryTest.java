package com.pompei.minhasfinancas.model.repository;

import com.pompei.minhasfinancas.model.entity.Usuario;
import com.pompei.minhasfinancas.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        // Cenário
        Usuario usuario = criarUsuarioDeTeste();
        entityManager.persist(usuario);
        // Ação - Execução
        boolean result = usuarioRepository.existsByEmail("usuario@email.com");
        // Verificação
        Assertions.assertTrue(result);
    }
    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail(){
        // Cenário

        // Ação - Execução
        boolean result = usuarioRepository.existsByEmail("usuario@email.com");
        // Verificação
        Assertions.assertFalse(result);
    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados(){
        // Cenário
        Usuario usuario = criarUsuarioDeTeste();
        // Ação - Execução
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        // Verificação
        Assertions.assertNotNull(usuarioSalvo.getId());
    }

    @Test
    public void deveBuscarUmUsuarioPorEmail(){
        // Cenário
        Usuario usuario = criarUsuarioDeTeste();
        entityManager.persist(usuario);
        // Ação - Execução
        Optional<Usuario> result = usuarioRepository.findByEmail(usuario.getEmail());
        // Verificação
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase(){
        // Cenário

        // Ação - Execução
        Optional<Usuario> result = usuarioRepository.findByEmail("usuario@email.com");
        // Verificação
        Assertions.assertFalse(result.isPresent());
    }

    public static Usuario criarUsuarioDeTeste(){
        Usuario usuario = Usuario.builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha")
                .build();
        return usuario;
    }


}
