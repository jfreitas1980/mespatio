package com.hdntec.gestao.integracao.navios;

import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.navios.dao.ClienteDAO;
import com.hdntec.gestao.domain.navios.dao.FilaDeNaviosDAO;
import com.hdntec.gestao.domain.navios.dao.MetaNavioDAO;
import com.hdntec.gestao.domain.navios.dao.NavioDAO;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * Controlador das operações do subsistema de navios.
 * 
 * @author andre
 * 
 */
/**
 * Session Bean implementation class EntidadeBS
 */
@Stateless(name = "bs/ControladorNavios", mappedName = "bs/ControladorNavios")
public class ControladorNavios implements IControladorNavios {

    @Override
    public FilaDeNavios buscarFilaDeNavios(FilaDeNavios filtroFilaNavios) throws ErroSistemicoException {
        FilaDeNaviosDAO filaDAO = new FilaDeNaviosDAO();
        FilaDeNavios filaPesquisada = filaDAO.buscarFilaDeNavios(filtroFilaNavios);
        return filaPesquisada;
    }

    @Override
    public FilaDeNavios salvarFilaDeNavios(FilaDeNavios filaNavios) throws ErroSistemicoException {
        FilaDeNaviosDAO filaDAO = new FilaDeNaviosDAO();
        return filaDAO.salvaFilaDeNavios(filaNavios);
    }

    @Override
    public List<Cliente> buscarCliente(Cliente cliente) throws ErroSistemicoException {
        ClienteDAO clienteDAO = new ClienteDAO();
        return clienteDAO.buscaPorExemploCliente(cliente);
    }

    @Override
    public List<Cliente> buscaListaClientes() throws ErroSistemicoException {
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> listaClientesPesquisados = clienteDAO.buscarClientesCadastrados();
        return listaClientesPesquisados;
    }

    @Override
    public void atualizarFilaDeNavios(FilaDeNavios filaNavios) throws ErroSistemicoException {
        FilaDeNaviosDAO filaDAO = new FilaDeNaviosDAO();
        filaDAO.atualizar(filaNavios);
    }

    @Override
    public Navio salvarNavio(Navio navio) throws ErroSistemicoException {
        NavioDAO navioDAO = new NavioDAO();
        return navioDAO.salvaNavio(navio);
    }

    @Override
    public void removerNavio(Navio navio) throws ErroSistemicoException
    {
        MetaNavioDAO navioDAO = new MetaNavioDAO();
        navioDAO.removeMetaNavio(navio.getMetaNavio());
    }

    @Override
    public void atualizaCliente(Cliente cliente) throws ErroSistemicoException {
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.alteraCliente(cliente);
    }
}
