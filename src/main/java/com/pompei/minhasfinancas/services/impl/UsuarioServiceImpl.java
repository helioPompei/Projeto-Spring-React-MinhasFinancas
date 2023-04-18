package com.pompei.minhasfinancas.services.impl;

import com.pompei.minhasfinancas.exception.ErroAutenticacao;
import com.pompei.minhasfinancas.exception.RegraNegocioException;
import com.pompei.minhasfinancas.model.entity.Usuario;
import com.pompei.minhasfinancas.repository.UsuarioRepository;
import com.pompei.minhasfinancas.services.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if(!usuario.isPresent()){
            throw new ErroAutenticacao("Usuario não encontrado com este email!");
        }
        if(!usuario.get().getSenha().equals(senha)){
            throw new ErroAutenticacao("Senha Invalida!");
        }
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = usuarioRepository.existsByEmail(email);
        if(existe){
            throw new RegraNegocioException("Já existe um usuário com este email!");
        }
    }
}
