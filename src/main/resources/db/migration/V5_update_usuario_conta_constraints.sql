-- Atualização da tabela 'usuario' para adicionar a coluna 'conta_id' e definir o relacionamento
ALTER TABLE usuario
ADD COLUMN conta_id BIGINT UNIQUE,
ADD CONSTRAINT fk_usuario_conta FOREIGN KEY (conta_id) REFERENCES conta(id) ON DELETE CASCADE;

-- Atualização da tabela 'conta' para melhorar o relacionamento bidirecional e garantir exclusão em cascata
ALTER TABLE conta
DROP FOREIGN KEY fk_conta_usuario;

ALTER TABLE conta
MODIFY usuario_id BIGINT UNIQUE,
ADD CONSTRAINT fk_conta_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE;

-- Atualização para garantir que ambas as tabelas compartilhem o mesmo ID (Mapeamento de chave compartilhada)
UPDATE conta
SET usuario_id = id;

UPDATE usuario
SET conta_id = id;

-- Assegurando que o 'usuario_id' em 'conta' e 'conta_id' em 'usuario' sejam iguais para garantir a relação
ALTER TABLE conta
ADD CONSTRAINT fk_conta_maps_id FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE;
