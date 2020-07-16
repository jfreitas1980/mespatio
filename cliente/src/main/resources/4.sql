BEGIN for f in (select 'DROP TABLE '||table_name||' cascade constraints purge' cmd from user_tables where table_name <> 'LOGMESSAGE') loop execute immediate f.cmd; end loop; END;


select * from carga where id_meta_carga = 37

SELECT * FROM PRODUTO WHERE IDPRODUTO IN (285,292)