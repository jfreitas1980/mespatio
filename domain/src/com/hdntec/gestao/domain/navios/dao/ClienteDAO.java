package com.hdntec.gestao.domain.navios.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistencia da entidade Cliente
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 */
public class ClienteDAO extends AbstractGenericDAO<Cliente> {

    /**
     * Salva entidade cliente
     * @param {@link Cliente}
     */
    public Cliente salvaCliente(Cliente cliente) throws ErroSistemicoException {
        try {
            Cliente clienteSalvo = super.salvar(cliente);
//            super.encerrarSessao();
            return clienteSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera entidade cliente
     * @param {@link Cliente}
     */
    public void alteraCliente(Cliente cliente) throws ErroSistemicoException {
        try {
            super.atualizar(cliente);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o cliente da entidade
     * @param {@link Cliente}
     */
    public void removeCliente(Cliente cliente) throws ErroSistemicoException {
        try {
            super.deletar(cliente);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca clientes da entidade cliente por exemplo
     *
     * @param {@link Cliente}
     * @return link List<Cliente>}
     */
    public List<Cliente> buscaPorExemploCliente(Cliente cliente) throws ErroSistemicoException {
        try{
            List<Cliente> listaCliente = super.buscarListaDeObjetos(cliente);
            acessaListasCliente(listaCliente);
//            super.encerrarSessao();
            return listaCliente;
        }catch(HibernateException hbex){
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }

    /**
     * Metodo que busca no banco de dados todos os cliente cadastrados
     * @param listaCliente
     */
    public List<Cliente> buscarClientesCadastrados() throws ErroSistemicoException {
        try{
            List<Cliente> listaCliente = super.buscarListaDeObjetos(new Cliente());
            acessaListasCliente(listaCliente);
//            super.encerrarSessao();
            return listaCliente;
        }catch(HibernateException hbex){
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }

    /**
     * Metodo auxiliar que acessa as sublistas do objeto cliente para
     * retorna-las carregadas para a interface do cliente
     * @param listaCliente
     */
    private void acessaListasCliente(List<Cliente> listaCliente){
       /* for(Cliente cliente : listaCliente){
            if(cliente.getListaDeNaviosDoCliente() != null){
                cliente.getListaDeNaviosDoCliente().size();
                for(Navio navio : cliente.getListaDeNaviosDoCliente()){
                    if(navio.getListaDeCargasDoNavio() != null){
                        navio.getListaDeCargasDoNavio().size();
                        for(Carga carga : navio.getListaDeCargasDoNavio()){
                            if(carga.getProduto() != null){
                                carga.getProduto().getQualidade().getListaDeItensDeControle().size();
                            }
                            if (carga.getOrientacaoDeEmbarque() != null) {
                                carga.getOrientacaoDeEmbarque().getListaItemDeControle().size();
                            }
                        }
                    }
                }                
            }
            if(cliente.getListaDePilhas() != null){
                cliente.getListaDePilhas().size();
                for(Pilha pilha : cliente.getListaDePilhas()){
                    if(pilha.getListaDeBalizas() != null){
                        pilha.getListaDeBalizas().size();
                        for (Baliza baliza :  pilha.getListaDeBalizas()) {
                            if (baliza.getProduto().getQualidade() != null) {
                                baliza.getProduto().getQualidade().getListaDeItensDeControle().size();
                            }
                            baliza.getPatio().getListaDeBalizas().size();
                            baliza.getPatio().getListaDeMaquinasDoPatio().size();
                        }
                    }
                }
            }
        }*/
    }
}
