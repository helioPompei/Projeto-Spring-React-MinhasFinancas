package com.pompei.minhasfinancas.service;

import com.pompei.minhasfinancas.exception.ErroAutenticacao;
import com.pompei.minhasfinancas.exception.RegraNegocioException;
import com.pompei.minhasfinancas.model.entity.Usuario;
import com.pompei.minhasfinancas.repository.UsuarioRepository;
import com.pompei.minhasfinancas.services.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl usuarioService;

    @MockBean
    UsuarioRepository usuarioRepository;

    @Test
    public void deveSalvarUmUsuario(){
        // Cenário
        Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder()
                .id(1l)
                .nome("nome")
                .email("email@email.com")
                .senha("senha")
                .build();
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        // Ação
        Usuario usuarioSalvo = usuarioService.salvarUsuario(new Usuario());

        // Verificacao
        Assertions.assertNotNull(usuarioSalvo);
        Assertions.assertEquals(usuarioSalvo.getId(), 1l);
        Assertions.assertEquals(usuarioSalvo.getNome(), "nome");
        Assertions.assertEquals(usuarioSalvo.getEmail(), "email@email.com");
        Assertions.assertEquals(usuarioSalvo.getSenha(), "senha");
    }

    @Test
    public void naoDeveSalvarUmUsuarioComEmailJaCadastrado(){
        // Cenário
        String email = "email@email.com";
        Usuario usuario = Usuario.builder()
                .email(email)
                .build();
        Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);

        // Ação
        Assertions.assertThrows(RegraNegocioException.class,
                () -> usuarioService.salvarUsuario(usuario));

        // Verificacao
        Mockito.verify(usuarioRepository,Mockito.never()).save(usuario);
    }

    @Test
    public void deveValidarEmail(){
        // Cenario
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);
        // Ação
        usuarioService.validarEmail("email@email.com");
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        // Cenario
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(true);
        // Ação
        Assertions.assertThrows(RegraNegocioException.class,
                () -> usuarioService.validarEmail("email@email.com"));
    }

    @Test
    public void deveAutenticarUmUsuarioComSucesso(){
        // Cenario
        String email = "email@email.com";
        String senha = "senha";
        Usuario usuario = Usuario.builder()
                .email(email)
                .senha(senha)
                .id(1l)
                .build();
        Mockito.when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(usuario));
        // Acao
        Usuario result = usuarioService.autenticar(email,senha);
        // Verificacao
        Assertions.assertNotNull(result);
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado(){
        // Cenario
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Acao - verificacao
        Assertions.assertThrows(ErroAutenticacao.class,
                () -> usuarioService.autenticar("email@email.com","senha"));
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater(){
        // Cenario
        String senha = "senha";
        Usuario usuario = Usuario.builder()
                .email("email@email.com")
                .senha(senha)
                .build();
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(usuario));

        // Acao
        ErroAutenticacao exception = Assertions.assertThrows(ErroAutenticacao.class,
                () -> usuarioService.autenticar("email@email.com","outraSenha"));
        // 0verificacao
        Assertions.assertEquals(exception.getMessage(), "Senha Invalida!");
    }






}
