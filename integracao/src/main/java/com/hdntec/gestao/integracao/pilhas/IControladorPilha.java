package com.hdntec.gestao.integracao.pilhas;

import java.util.List;

import javax.ejb.Local;

import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * Interface de acesso às operações do subsistema de pilha.
 * 
 * @author andre
 * 
 * 
 */
@Local
public interface IControladorPilha {

    /**
     * método para buscar lista de balizas
     *
     * @return
     */
    public List<Baliza> buscarListaDeBalizas();

    /**
     * método de persitência de uma lista de balizas
     *
     * @param listaDeBalizas
     * @return
     */
    public void salvaListaBalizas(List<Baliza> listaDeBalizas) throws ErroSistemicoException;

    public Baliza salvaBaliza(Baliza baliza) throws ErroSistemicoException;
}