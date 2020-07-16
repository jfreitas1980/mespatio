
package com.hdntec.gestao.domain.plano.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.planta.dao.LugarEmpilhamentoRecuperacaoDAO;
import com.hdntec.gestao.domain.planta.dao.MovimentacaoNavioDAO;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe DAO para persistir a entidade de Atividade.
 * @author Ricardo Trabalho
 */
public class AtividadeDAO extends AbstractGenericDAO<Atividade> {

    private LugarEmpilhamentoRecuperacaoDAO lugarEmpilhamentoRecuperacaoDAO = new LugarEmpilhamentoRecuperacaoDAO();
    private MovimentacaoNavioDAO movNavioDAO = new MovimentacaoNavioDAO();
	/**
     * Salva objeto atividade na entidade
     * @param {@link Atividade}
     */
    public Atividade salvaAtividade(Atividade atividade) throws ErroSistemicoException {
        try {
        	Atividade atividadeSalva = null;
        		atividadeSalva = super.salvar(atividade);
        	
            if (atividade.getMovimentacaoNavio() != null) {
            	movNavioDAO.salvaMovimentacaoNavio(atividade.getMovimentacaoNavio());
            }
            if (atividade.getListaDeLugaresDeEmpilhamentoRecuperacao() != null &&
            		atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().size() > 0 ) {
            	lugarEmpilhamentoRecuperacaoDAO.salvaLugarEmpilhamentoRecuperacao(atividade.getListaDeLugaresDeEmpilhamentoRecuperacao());
            }	
           
            atividadeSalva.setUpdated(Boolean.FALSE);
            return atividadeSalva;
        } catch (HibernateException hbEx) {
        	HibernateUtil.rollbackTransaction();
        	hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto atividade na entidade
     * @param {@link Atividade}
     */
    public void alteraAtividade(Atividade atividade) throws ErroSistemicoException {
        try {
            super.atualizar(atividade);
            //            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto atividade da entidade
     * @param {@link Atividade}
     */
    public void removeAtividade(Atividade atividade) throws ErroSistemicoException {
        try {
                    	
           if (atividade.getMovimentacaoNavio() != null) {
        	  movNavioDAO.removeMovimentacaoNavio(atividade.getMovimentacaoNavio());
           }
        
           
           if (atividade.getListaDeLugaresDeEmpilhamentoRecuperacao() != null &&
        		atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().size() > 0 ) {
        	lugarEmpilhamentoRecuperacaoDAO.removeLugarEmpilhamentoRecuperacao(atividade.getListaDeLugaresDeEmpilhamentoRecuperacao());
        }	
           
           
           atividade.setMovimentacaoNavio(null);
           atividade.setListaDeLugaresDeEmpilhamentoRecuperacao(null);
        	
           Atividade atvAnterior = null;
           if (atividade.getAtividadeAnterior() != null) {
        	   atvAnterior = atividade.getAtividadeAnterior(); 
           }
           
           
        	super.deletar(atividade);
        	
        	if (atvAnterior != null) {
        		atvAnterior.setQuantidadeAtualizadaPeloMES(Boolean.FALSE);
        		atvAnterior.setUpdated(Boolean.FALSE);
        		salvaAtividade(atvAnterior);
        	}
            //            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca atividade 
     *
     * @param {@link Atividade}
     * @return link List<Atividade>}
     */
    public List<Atividade> buscaPorExemploAtividade(Atividade atividade) throws ErroSistemicoException {
        try {
            List<Atividade> listaPesquisada = super.buscarListaDeObjetos(atividade);
            //            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto atividade da entidade
     * @param {@link Atividade}
     */
    public void removeAtividade1(Atividade atividade) throws ErroSistemicoException {
        PreparedStatement preparedStatement = null;
        try {
            Session session = HibernateUtil.getSession();

            String hql[] = {
                            "delete from Lugar_Maquina where idLugarEmpilhaRecupera = ?",
                            "delete from Lugar_Baliza where idLugarEmpilhaRecupera = ?",
                            "delete from Lugar_Carga where idLugarEmpilhaRecupera = ?",
                            "delete from Lugar_Campanha where idLugarEmpilhaRecupera = ?",
                            "delete from LugarEmpilhamentoRecuperacao where idLugarEmpilhaRecupera = ?"};

            try {
                session.beginTransaction();
                for (int i = 0; i < hql.length; i++) {
                    preparedStatement = session.connection().prepareStatement(hql[i]);
                    preparedStatement.setLong(1, atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0)
                                    .getIdLugarEmpilhaRecupera());
                    preparedStatement.executeUpdate();

                }
                //preparedStatement = session.connection().prepareStatement("delete from atividade where id = ? ");
                //preparedStatement.setLong(1, atividade.getId());

                session.getTransaction().commit();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /*Query query = session.createQuery(hql);
            query.setLong("name",);
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            */

            /*   StringBuilder sSql = new StringBuilder();
               sSql.append("select distinct(t) from PlanoEmpilhamentoRecuperacao ");
               sSql.append("t join fetch t.listaSituacoesPatio as situacoes ");
               sSql.append(" where t.idUser =:param3 and  t.ehOficial = 0");
               Query q = session.createQuery(sSql.toString());
               q.setParameter("param3", idUser);
               PlanoEmpilhamentoRecuperacao result = (PlanoEmpilhamentoRecuperacao) q.uniqueResult();
            //            super.encerrarSessao();
               
               return result;

            
            
            
            
            super.deletar(atividade);*/
            //            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public Atividade existeEmpilhamentoPendente(MetaMaquinaDoPatio maquina) {
        Session session = HibernateUtil.getSession();
        StringBuilder sSql = new StringBuilder();
        PreparedStatement preparedStatement = null;
        Atividade atv = null;
        ResultSet rs = null;
        //sSql.append(" select a1.id,a1.dt_inicio,a1.dt_fim,finalizada from atividade a1 where TIPOATIVIDADE = 'ATUALIZACAO_EMPILHAMENTO' and dt_fim is null");
        //sSql.append(" and not exists (select * from  atividade where TIPOATIVIDADE = 'ATUALIZACAO_EMPILHAMENTO' and ATIVIDADEANTERIOR_ID = a1.id)");
        //sSql.append(" and a1.id in (select id_atividade from MAQUINADOPATIO where id_meta_maq in (?))");
        
        sSql.append(" select a1.id,a1.dt_inicio,a1.dt_fim,finalizada from atividade a1 where TIPOATIVIDADE = 'ATUALIZACAO_EMPILHAMENTO' ");
        sSql.append(" and not exists (select * from  atividade where TIPOATIVIDADE = 'ATUALIZACAO_EMPILHAMENTO' and ATIVIDADEANTERIOR_ID = a1.id)");
        sSql.append(" and a1.id in (select id_atividade from MAQUINADOPATIO where id_meta_maq in (?))");
        
        try {
            preparedStatement = session.connection().prepareStatement(sSql.toString());
            preparedStatement.setLong(1, maquina.getIdMaquina());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                atv = this.buscarAtividade(rs.getLong(1));
            }
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            rs = null;
            preparedStatement = null;
        }

        return atv;
    }
    
    public Atividade existeRecuperacaoPendente(MetaMaquinaDoPatio maquina) {
        Session session = HibernateUtil.getSession();
        StringBuilder sSql = new StringBuilder();
        PreparedStatement preparedStatement = null;
        Atividade atv = null;
        ResultSet rs = null;
        sSql.append(" select a1.id,a1.dt_inicio,a1.dt_fim,finalizada from atividade a1 where TIPOATIVIDADE = 'ATUALIZACAO_RECUPERACAO' ");
        sSql.append(" and not exists (select * from  atividade where TIPOATIVIDADE = 'ATUALIZACAO_RECUPERACAO' and ATIVIDADEANTERIOR_ID = a1.id)");
        sSql.append(" and a1.id in (select id_atividade from MAQUINADOPATIO where id_meta_maq in (?))");
        try {
            preparedStatement = session.connection().prepareStatement(sSql.toString());
            preparedStatement.setLong(1, maquina.getIdMaquina());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                atv = this.buscarAtividade(rs.getLong(1));
            }
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            rs = null;
            preparedStatement = null;
        }


        return atv;
    }

    public Atividade existeMovimentacaoEmergenciaPendente(MetaMaquinaDoPatio maquina) {
        Session session = HibernateUtil.getSession();
        StringBuilder sSql = new StringBuilder();
        PreparedStatement preparedStatement = null;
        Atividade atv = null;
        ResultSet rs = null;
        sSql.append(" select a1.id,a1.dt_inicio,a1.dt_fim,finalizada from atividade a1 where TIPOATIVIDADE = 'MOVIMENTAR_PILHA_EMERGENCIA' ");
        sSql.append(" and not exists (select * from  atividade where TIPOATIVIDADE = 'MOVIMENTAR_PILHA_EMERGENCIA' and ATIVIDADEANTERIOR_ID = a1.id)");
        sSql.append(" and a1.id in (select id_atividade from MAQUINADOPATIO where id_meta_maq in (?))");
        try {
            preparedStatement = session.connection().prepareStatement(sSql.toString());
            preparedStatement.setLong(1, maquina.getIdMaquina());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                atv = this.buscarAtividade(rs.getLong(1));
            }
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            rs = null;
            preparedStatement = null;
        }


        return atv;
    }

    public Atividade existeMovimentacaoPSMPendente(MetaMaquinaDoPatio maquina) {
        Session session = HibernateUtil.getSession();
        StringBuilder sSql = new StringBuilder();
        PreparedStatement preparedStatement = null;
        Atividade atv = null;
        ResultSet rs = null;
        sSql.append(" select a1.id,a1.dt_inicio,a1.dt_fim,finalizada from atividade a1 where TIPOATIVIDADE = 'MOVIMENTAR_PILHA_PSM' ");
        sSql.append(" and not exists (select * from  atividade where TIPOATIVIDADE = 'MOVIMENTAR_PILHA_PSM' and ATIVIDADEANTERIOR_ID = a1.id)");
        sSql.append(" and a1.id in (select id_atividade from MAQUINADOPATIO where id_meta_maq in (?))");
        try {
            preparedStatement = session.connection().prepareStatement(sSql.toString());
            preparedStatement.setLong(1, maquina.getIdMaquina());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                atv = this.buscarAtividade(rs.getLong(1));
            }
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            rs = null;
            preparedStatement = null;
        }


        return atv;
    }

    public Atividade existeMovimentacaoPelletPendente(MetaMaquinaDoPatio maquina) {
        Session session = HibernateUtil.getSession();
        StringBuilder sSql = new StringBuilder();
        PreparedStatement preparedStatement = null;
        Atividade atv = null;
        ResultSet rs = null;
        sSql.append(" select a1.id,a1.dt_inicio,a1.dt_fim,finalizada from atividade a1 where TIPOATIVIDADE = 'MOVIMENTAR_PILHA_PELLET_FEED' ");
        sSql.append(" and not exists (select * from  atividade where TIPOATIVIDADE = 'MOVIMENTAR_PILHA_PELLET_FEED' and ATIVIDADEANTERIOR_ID = a1.id)");
        sSql.append(" and a1.id in (select id_atividade from MAQUINADOPATIO where id_meta_maq in (?))");
        try {
            preparedStatement = session.connection().prepareStatement(sSql.toString());
            preparedStatement.setLong(1, maquina.getIdMaquina());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                atv = this.buscarAtividade(rs.getLong(1));
            }
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            rs = null;
            preparedStatement = null;
        }


        return atv;
    }


    public Atividade existeMovimentacaoRetornoPelletFeedPendente(MetaMaquinaDoPatio maquina) {
        Session session = HibernateUtil.getSession();
        StringBuilder sSql = new StringBuilder();
        PreparedStatement preparedStatement = null;
        Atividade atv = null;
        ResultSet rs = null;
        sSql.append(" select a1.id,a1.dt_inicio,a1.dt_fim,finalizada from atividade a1 where TIPOATIVIDADE = 'RETORNO_PELLET_FEED' ");
        sSql.append(" and not exists (select * from  atividade where TIPOATIVIDADE = 'RETORNO_PELLET_FEED' and ATIVIDADEANTERIOR_ID = a1.id)");
        sSql.append(" and a1.id in (select id_atividade from MAQUINADOPATIO where id_meta_maq in (?))");
        try {
            preparedStatement = session.connection().prepareStatement(sSql.toString());
            preparedStatement.setLong(1, maquina.getIdMaquina());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                atv = this.buscarAtividade(rs.getLong(1));
            }
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            rs = null;
            preparedStatement = null;
        }


        return atv;
    }

    
    
    private Atividade buscarAtividade(Long id) {
        Atividade result= null;
        Session session =  HibernateUtil.getSession();
        //Montando a consulta
        StringBuilder sSql = new StringBuilder();
        sSql.append("from "+Atividade.class.getName()); 
        sSql.append(" where id = :id ");        
        //Criando a query e passando os parametros
        Query q = session.createQuery(sSql.toString());
        
        q.setLong("id", id);
        
        //Executando a consulta
        result = (Atividade) q.uniqueResult();
//      super.encerrarSessao();
        return result;
    }

    

    /**
     * Remove o objeto atividade da entidade
     * @param {@link Atividade}
     */
    public void removePilhaPendente() throws ErroSistemicoException {
        /*PreparedStatement preparedStatement = null;
        try {
            Session session = HibernateUtil.getSession();

            String hql[] = {
                            "delete from pilha where idpilha not in (select idpilha from baliza_pilha)"};

            try {
                session.beginTransaction();
                for (int i = 0; i < hql.length; i++) {
                    preparedStatement = session.connection().prepareStatement(hql[i]);                   
                    preparedStatement.executeUpdate();

                }
                //preparedStatement = session.connection().prepareStatement("delete from atividade where id = ? ");
                //preparedStatement.setLong(1, atividade.getId());

                session.getTransaction().commit();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }*/
    }

    
}
