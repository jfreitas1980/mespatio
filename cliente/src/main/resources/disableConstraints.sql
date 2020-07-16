ALTER TABLE maquinadopatio
disable CONSTRAINT fk_maquinadopatio_atividade;
/
ALTER TABLE pilhavirtual
disable CONSTRAINT fk_virtual_baliza;
/
ALTER TABLE carga
disable CONSTRAINT fk_carga_atividade;
/
ALTER TABLE lugarempilhamentorecuperacao
disable CONSTRAINT fk_lugar_maquina;
/
ALTER TABLE atividade
disable CONSTRAINT FK_ATIVIDADE_CARGA;
/

ALTER TABLE ATIVIDADECAMPANHA
disable CONSTRAINT FK_ATIVCAMPANNHA_CAMPANHA;
/