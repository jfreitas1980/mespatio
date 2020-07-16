package com.hdntec.gestao.integracao.pilhas;

import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.planta.dao.BalizaDAO;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * Controlador das operações do subsistema de pilha.
 * 
 * @author andre
 * 
 */
@Stateless(name = "bs/ControladorPilha", mappedName = "bs/ControladorPilha")
public class ControladorPilhas implements IControladorPilha {

    @Override
    public List<Baliza> buscarListaDeBalizas() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void salvaListaBalizas(List<Baliza> listaDeBalizas) throws ErroSistemicoException {
        BalizaDAO balizaDAO = new BalizaDAO();
        balizaDAO.salvaListaBalizas(listaDeBalizas);
    }

    @Override
    public Baliza salvaBaliza(Baliza baliza) throws ErroSistemicoException {
            BalizaDAO balizaDAO = new BalizaDAO();
            return balizaDAO.salvaBaliza(baliza);
    }
}