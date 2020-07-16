package com.hdntec.gestao.integracao.planta;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.planta.dao.CarregadoraDeNaviosDAO;
import com.hdntec.gestao.domain.planta.dao.CorreiaDAO;
import com.hdntec.gestao.domain.planta.dao.ManutencaoDAO;
import com.hdntec.gestao.domain.planta.dao.MaquinaDoPatioDAO;
import com.hdntec.gestao.domain.planta.dao.MetaMaquinaDoPatioDAO;
import com.hdntec.gestao.domain.planta.dao.PierDAO;
import com.hdntec.gestao.domain.planta.dao.PlantaDAO;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.CarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Manutencao;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * Controlador das operações do subsistema de planta.
 * 
 * @author andre
 * 
 *         Session Bean implementation class EntidadeBS
 */
@Stateless(name = "bs/ControladorPlanta", mappedName = "bs/ControladorPlanta")
public class ControladorPlanta implements IControladorPlanta {

    @Override
    public List<Planta> buscarPlantas() throws ErroSistemicoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void salvaPlanta(Planta planta) throws ErroSistemicoException {
        PlantaDAO plantaDAO = new PlantaDAO();
        plantaDAO.salvaPlanta(planta);
    }

    @Override
    public List<CarregadoraDeNavios> buscarListaCarregadorasNavio() throws ErroSistemicoException {
        CarregadoraDeNaviosDAO carregaNavioDAO = new CarregadoraDeNaviosDAO();
        return carregaNavioDAO.buscaListaCarregadoraDeNavios();
    }

    @Override
    public List<MaquinaDoPatio> buscarListaMaquinas() throws ErroSistemicoException {
        MaquinaDoPatioDAO mDAO = new MaquinaDoPatioDAO();
        return mDAO.buscaListaDeMaquinasDoPatio();
    }

    @Override
    public List<Manutencao> salvarOuAtualizarListaManutencao(List<Manutencao> listaManutencoes) throws ErroSistemicoException {
        ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
        return manutencaoDAO.salvaOuAtualizarListaManutencao(listaManutencoes);
    }

    @Override
    public void removeManutencao(Manutencao manutencao) throws ErroSistemicoException {
        ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
        manutencaoDAO.removeManutencao(manutencao);

    }

    @Override
    public MaquinaDoPatio salvaMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException {
        MaquinaDoPatioDAO maquinaDoPatioDAO = new MaquinaDoPatioDAO();
        return maquinaDoPatioDAO.salvaMaquinaDoPatio(maquina);
    }

    @Override
    public MaquinaDoPatio buscarMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException {
        MaquinaDoPatioDAO maquinaDoPatioDAO = new MaquinaDoPatioDAO();
        return maquinaDoPatioDAO.buscarMaquinaDoPatio(maquina);
    }

    @Override
    public List<MetaMaquinaDoPatio> buscarMaquinaDoPatio(MetaMaquinaDoPatio maquina) throws ErroSistemicoException {
        MetaMaquinaDoPatioDAO maquinaDoPatioDAO = new MetaMaquinaDoPatioDAO();
        return maquinaDoPatioDAO.buscarListaDeObjetos(maquina);
    }

    
    @Override
    public Correia buscarCorreia(Correia correia) throws ErroSistemicoException {
        CorreiaDAO correiaDAO = new CorreiaDAO();
        return correiaDAO.buscarCorreia(correia);
    }

    @Override
    public Correia salvaCorreia(Correia correia) throws ErroSistemicoException {
        CorreiaDAO correiaDAO = new CorreiaDAO();
        return correiaDAO.salvaCorreia(correia);
    }

    @Override
    public List<MaquinaDoPatio> buscaListaDeMaquinasDoPatio() throws ErroSistemicoException {
    	MaquinaDoPatioDAO maquinaDoPatioDAO = new MaquinaDoPatioDAO();
        return maquinaDoPatioDAO.buscaListaDeMaquinasDoPatio();
    }

    @Override
    public Pier salvaPier(Pier pier) throws ErroSistemicoException {
      PierDAO pierDAO = new PierDAO();
      return pierDAO.salvaPier(pier);
    }

    @Override
    public List<MaquinaDoPatio> buscaListaMaquinaDoPatioPorManutencao(
    		MaquinaDoPatio maquina, Date dataInicial, Date dataFinal) {
    	MaquinaDoPatioDAO maquinaDoPatioDAO = new MaquinaDoPatioDAO();
        return maquinaDoPatioDAO.buscaListaMaquinaPorManutencao(maquina, 
        		dataInicial, dataFinal);
    }
    
    @Override
    public List<Correia> buscaListaCorreiaPorManutencao(Correia correia,
    		Date dataInicial, Date dataFinal) {
    	CorreiaDAO correiaDAO = new CorreiaDAO();
    	return correiaDAO.buscaListaCorreiaPorManutencao(correia, dataInicial, 
    			dataFinal);

    }
}
