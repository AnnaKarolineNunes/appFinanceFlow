package com.nunes.financeFlow.models.user;

public record RegisterDTO(String nome, String email, String senha, UserRole role) {
}