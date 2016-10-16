/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.perguntados.model;

import br.com.coelce.perguntados.util.Base64Encoder;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author dunkelheit
 */
@Entity
public class Usuario extends Identified {

   
    private String login;
    private String senha;
    private String confirmPassword;
//    @ManyToMany
//    private List<Permissoes> permissoes;

   

//    private Permissao permiss;
//    public Permissao getPermiss() {
//        return permiss;
//    }
//
//    public void setPermiss(Permissao permiss) {
//        this.permiss = permiss;
//    }
    
    
    @Enumerated(EnumType.STRING)
    private Perfil perfil; 

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = hashPassword(senha);
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = hashPassword(confirmPassword);
    }
    
//    public List<Permissoes> getPermissoes() {
//        return permissoes;
//    }
//    
//    public void setPermissoes(List<Permissoes> permissoes) {
//        this.permissoes = permissoes;
//    }

    public String hashPassword(String rawPassword) {
        MessageDigest md;
        byte[] stringBytes;
        try {
            md = MessageDigest.getInstance("SHA-256");
            stringBytes = rawPassword.getBytes("UTF8");

            byte[] stringCriptBytes = md.digest(stringBytes);
            char[] encoded = Base64Encoder.encode(stringCriptBytes);
            return String.valueOf(encoded);
        } catch (NoSuchAlgorithmException nsae) {
            throw new SecurityException("The Requested encoding algorithm was not found in this execution platform.", nsae);
        } catch (UnsupportedEncodingException uee) {
            throw new SecurityException("UTF8 is not supported in this execution platform.", uee);
        }
    }
}
