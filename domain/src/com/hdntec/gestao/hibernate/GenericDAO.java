package com.hdntec.gestao.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;

public interface GenericDAO<T> {
    
    /**
     * Método genérico para salvar registro de qualquer tipo na base de dados
     */
    public T salvar(final T clazz) throws HibernateException;

    /**
     * Método genérico para salvar registro de qualquer tipo na base de dados
     */
    public List<T> salvar(final List<T> listaObjetos) throws HibernateException;

    /**
     * Método genérico para buscar registro de qualquer tipo na base de dados
     *
     * @param example
     * @return
     */
    public List<T> buscarListaDeObjetos(final T objeto) throws HibernateException;

    /**
     * Método genérico para buscar registro de qualquer tipo na base de dados
     *
     * @param example
     * @return
     */
    public T buscarPorObjeto(final T objeto) throws HibernateException;

    /**
     * Método genérico para excluir registro de qualquer tipo na base de dados
     */
    public void deletar(final T clazz) throws HibernateException;


    /**
     * Remove uma lista de objetos deixando-os na mesma transação
     * @param listaObjetos
     * @throws org.hibernate.HibernateException
     */
    public void remover(List<T> listaObjetos) throws HibernateException;

    /**
     * Método genérico para atualizar dados de qualquer tipo na base de dados
     *
     * @param clazz
     * @throws HibernateException
     */
    public void atualizar(T clazz) throws HibernateException;

    /**
     * Método genérico para encerrar uma sessao aberta
     * (Usado apos a busca de objetos)
     *
     * @throws HibernateException
     */
    public void encerrarSessao() throws HibernateException;

    /**
     * Metodo generico para buscar uma lista de objetos por intervalo de datas
     */
    public List<T> buscarListaDeObjetosPorDatas(T objeto, String nomeCampo, Date dataInicial, Date dataFinal);

    /**
     * Atualiza uma lista de objetos
     * @param listaObjetos
     * @throws org.hibernate.HibernateException
     */
    public void atualizar(final List<T> listaObjetos) throws HibernateException;

    public List<T> findAll(Class<T> clazz) throws HibernateException ;
    
    public T getObjeto(Class<T> clazz, Serializable id) throws HibernateException;
    
    /**
     * Método que busca uma lista de objetos com o valor do "nomecampo" diferente do "condicao"
     * @param objeto
     * @param nomeCampo
     * @param condicao
     * @return
     */
    public List<T> buscarListaDeObjetosPorCondicaoDiferenteDe(T objeto, String nomeCampo, String condicao);

    public List<T> findAllDistinctyOff(Class<T> clazz) throws HibernateException ;
}
