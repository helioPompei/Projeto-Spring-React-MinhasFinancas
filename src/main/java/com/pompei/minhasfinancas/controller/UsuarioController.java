package com.pompei.minhasfinancas.controller;

import com.pompei.minhasfinancas.dto.UsuarioDto;
import com.pompei.minhasfinancas.exception.ErroAutenticacao;
import com.pompei.minhasfinancas.exception.RegraNegocioException;
import com.pompei.minhasfinancas.model.entity.Usuario;
import com.pompei.minhasfinancas.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String index(){
        return "hello world!";
    }


    @PostMapping("/autenticar")
    public ResponseEntity autenticar( @RequestBody UsuarioDto usuarioDto) {
        try {
            Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDto.email(),usuarioDto.senha());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch(ErroAutenticacao e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity salvar( @RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = Usuario.builder()
                .nome(usuarioDto.nome())
                .email(usuarioDto.email())
                .senha(usuarioDto.senha())
                .build();
        try {
            Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch(RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
