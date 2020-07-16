package com.hdntec.gestao.services.controladores.impl;

import java.util.List;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.planta.dao.AtividadeCampanhaDAO;
import com.hdntec.gestao.domain.planta.dao.CampanhaDAO;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.services.controladores.IControladorUsinas;


public class ControladorUsinas implements IControladorUsinas 
{

    private static ControladorUsinas instance = null;
    private static ComparadorStatusEntity<Navio> comparadorStatus = new ComparadorStatusEntity<Navio>();
    /**
     * Construtor privado.
     */
    public ControladorUsinas() {
    }

    /**
     * Retorna a instancia singleton da f�brica.
     * 
     * @return TransLogicDAOFactory
     */
    public static ControladorUsinas getInstance() {
        if (instance == null) {
            instance = new ControladorUsinas();
        }
        return instance;
    }

    
    @Override
    public void removerCampanha(Campanha campanha) throws ErroSistemicoException {
        // TODO Auto-generated method stub
        AtividadeCampanhaDAO dao = new AtividadeCampanhaDAO();                                
        List<AtividadeCampanha> atividadeCampanhas = dao.buscarPorCampanha(campanha);
        if (atividadeCampanhas != null && atividadeCampanhas.size() > 0) {
            throw new ErroSistemicoException("Existe atividade que utiliza esse campanha, não é possivel excluir !");
        } else {
            MetaUsina usina = campanha.getMetaUsina();
            usina.getListaCampanhas().remove(campanha);
            CampanhaDAO daoC = new CampanhaDAO();
            campanha.setMetaUsina(null);
            daoC.removeCampanha(campanha);   
            daoC = null;
        }
    }

    @Override
    public void salvarCampanha(Campanha campanha) throws ErroSistemicoException {
        // TODO Auto-generated method stub
        CampanhaDAO dao = new CampanhaDAO();
        dao.salvaCampanha(campanha);        
        dao = null;        
    }
}

