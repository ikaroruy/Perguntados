/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.perguntados.util;

/**
 *
 * @author dunkelheit
 */
public enum StringUtils {

    INSTANCE;

    public boolean isNullOrBlank(String property) {
        return property == null || property.trim().isEmpty();
    }
}
