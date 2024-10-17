CREATE TABLE receita (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255),
    nome VARCHAR(255),
    valor DECIMAL(10, 2) NOT NULL,
    data DATE NOT NULL,
    tipo_receita VARCHAR(50) NOT NULL,
    id_usuario BIGINT NOT NULL,
    conta_id BIGINT NOT NULL,
    CONSTRAINT fk_receita_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_receita_conta FOREIGN KEY (conta_id) REFERENCES conta(id) ON DELETE CASCADE
);
