-- Atualiza a coluna "role" da tabela "usuario" para torná-la opcional e manter o valor padrão 'USER'
ALTER TABLE usuario
MODIFY COLUMN role VARCHAR(50) DEFAULT 'USER';
