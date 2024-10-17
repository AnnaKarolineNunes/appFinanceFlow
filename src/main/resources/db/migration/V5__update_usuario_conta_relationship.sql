-- Adicionar restrição NOT NULL à coluna conta_id na tabela usuario para garantir que todo usuário esteja vinculado a uma conta
ALTER TABLE usuario
MODIFY conta_id BIGINT NOT NULL;

-- Remover a constraint de chave estrangeira existente e adicionar uma nova para garantir o relacionamento 1:1 entre usuario e conta
ALTER TABLE usuario
DROP FOREIGN KEY fk_usuario_conta;

ALTER TABLE usuario
ADD CONSTRAINT fk_usuario_conta
FOREIGN KEY (conta_id)
REFERENCES conta(id)
ON DELETE CASCADE;

-- Atualizar tabela conta para adicionar a referência ao usuário de forma única
ALTER TABLE conta
ADD CONSTRAINT fk_conta_usuario UNIQUE (id);
