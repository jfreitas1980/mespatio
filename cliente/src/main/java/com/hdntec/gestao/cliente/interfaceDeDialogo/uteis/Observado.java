package com.hdntec.gestao.cliente.interfaceDeDialogo.uteis;

import java.io.Serializable;

public interface Observado extends Serializable
{
   public void addObservador(Observador observador);

   public void removeObservador(Observador observador);

   public void notificaObservador();
}
