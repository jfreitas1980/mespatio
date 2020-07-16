package com.hdntec.gestao.integracao.navios;

import java.util.List;

import javax.ejb.Local;

import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * Interface de acesso às operações do subsistema de navios.
 * 
 * @author andre
 * 
 */
@Local
public interface IControladorNavios {

    /**
     * Metodo que busca uma fila de navios persistida
     * @return fila com todos os Navios
     */
    public FilaDeNavios buscarFilaDeNavios(FilaDeNavios filtroFilaNavios) throws ErroSistemicoException;

    /**
     * Persiste no banco de dados um objeto fila de navios
     * @param filaNavios - A fila de navios a ser persistida
     * @return filaNavios - A fila de navios persistida
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public FilaDeNavios salvarFilaDeNavios(FilaDeNavios filaNavios) throws ErroSistemicoException;

    /**
     * Busca os clientes cadastrados na base
     * @param cliente
     * @return uma lista com os clientes cadastrados
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public List<Cliente> buscarCliente(Cliente cliente) throws ErroSistemicoException;

    /**
     * Atualiza no banco de dados um objeto fila de navios
     * @param filaNavios - A fila de navios a ser persistida
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void atualizarFilaDeNavios(FilaDeNavios filaNavios) throws ErroSistemicoException;

    /**
     * Metodo que busca todos os clientes cadastrados no sistema.
     * @return
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public List<Cliente> buscaListaClientes() throws ErroSistemicoException;

    /**
     * atualiza os dados do cliente
     * @param cliente
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void atualizaCliente(Cliente cliente) throws ErroSistemicoException;

    /**
     * salva o navio no banco
     * @param navio
     * @return
     */
   public Navio salvarNavio(Navio navio) throws ErroSistemicoException;

   /**
    * remove o navio do banco
    * @param navio
    * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
    */
   public void removerNavio(Navio navio) throws ErroSistemicoException;

}