package com.hdntec.gestao.integracao.planta;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.CarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Manutencao;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * Interface de acesso às operações do subsistema de planta.
 * 
 * @author andre
 * 
 */
@Local
public interface IControladorPlanta {

    /**
     * Busca a lista de plantas cadastradas
     *
     * @return List<Planta> - Lista de objetos {@link Planta}
     */
    public List<Planta> buscarPlantas() throws ErroSistemicoException;

    /**
     * Salva as informações da planta
     *
     * @param planta -
     *           Objeto a ser persistido
     */
    public void salvaPlanta(Planta planta) throws ErroSistemicoException;

    /**
     * Busca todas as maquinas cadastradas no sistema
     * @return - lista com todos as máquinas cadastradas
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public List<MaquinaDoPatio> buscarListaMaquinas() throws ErroSistemicoException;

    /**
     * Busca todas as maquinas carregadoras de navios cadastradas no sistema
     * @return - lista com todos as carregadora de navios cadastradas
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public List<CarregadoraDeNavios> buscarListaCarregadorasNavio() throws ErroSistemicoException;

    /**
     * Salva lista de manutencoes.
     * @param listaManutencoes
     * @return
     * @throws ErroSistemicoException
     */
    public List<Manutencao> salvarOuAtualizarListaManutencao(List<Manutencao> listaManutencoes) throws ErroSistemicoException;

    public MaquinaDoPatio salvaMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException;

    public void removeManutencao(Manutencao manutencao) throws ErroSistemicoException;

    public MaquinaDoPatio buscarMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException;

    public List<MaquinaDoPatio> buscaListaDeMaquinasDoPatio() throws ErroSistemicoException;

    public Correia buscarCorreia(Correia correia) throws ErroSistemicoException;

    public Correia salvaCorreia(Correia correia) throws ErroSistemicoException;

    /**
     * Salva o Pier.
     * @param pier
     * @return Pier
     * @throws ErroSistemicoException
     */
    public Pier salvaPier(Pier pier) throws ErroSistemicoException;


    /**
     * 
     * @param maquina
     * @param dataInicial
     * @param dataFinal
     * @return
     */
    public List<MaquinaDoPatio> buscaListaMaquinaDoPatioPorManutencao(
    		MaquinaDoPatio maquina, Date dataInicial, Date dataFinal);
    
    /**
     * 
     * @param correia
     * @param dataInicial
     * @param dataFinal
     * @return
     */
    public List<Correia> buscaListaCorreiaPorManutencao(Correia correia,
 		   Date dataInicial, Date dataFinal);
    
    public List<MetaMaquinaDoPatio> buscarMaquinaDoPatio(MetaMaquinaDoPatio maquina) throws ErroSistemicoException;

}
