package com.hdntec.gestao.services.controladores.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.navios.StatusNavioEnum;
import com.hdntec.gestao.domain.navios.dao.MetaNavioDAO;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.MovimentacaoNavio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBerco;
import com.hdntec.gestao.domain.planta.entity.status.Berco;
import com.hdntec.gestao.domain.vo.atividades.AtividadeAtracarDesAtracarNavioVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.services.controladores.IControladorFilaDeNavios;


public class ControladorFilaDeNavios implements IControladorFilaDeNavios 
{

    private static ControladorFilaDeNavios instance = null;
    private static ComparadorStatusEntity<Navio> comparadorStatus = new ComparadorStatusEntity<Navio>();
    /**
     * Construtor privado.
     */
    public ControladorFilaDeNavios() {
    }

    /**
     * Retorna a instancia singleton da f�brica.
     * 
     * @return TransLogicDAOFactory
     */
    public static ControladorFilaDeNavios getInstance() {
        if (instance == null) {
            instance = new ControladorFilaDeNavios();
        }
        return instance;
    }

    
    @Override
    public FilaDeNavios recuperaFila(Date hora) {
        // TODO Auto-generated method stub
        FilaDeNavios fila = new FilaDeNavios();
        List<Navio> navios = new ArrayList<Navio>();
        MetaNavioDAO dao = new MetaNavioDAO();
        List<MetaNavio> result = dao.findAll(MetaNavio.class);
        
        for (MetaNavio meta : result) {
            Collections.sort(meta.getListaStatus(), comparadorStatus);
            Navio navio = meta.retornaStatusHorario(hora);
            if (navio.naFila()) {
                navios.add(navio);
            }
        }
        fila.setListaDeNaviosNaFila(navios);
        dao = null;
        return fila; 
    }

    public Atividade movimentarNavio(AtividadeAtracarDesAtracarNavioVO movimentarNavioVO) throws AtividadeException {
        // TODO Auto-generated method stub
        Atividade atividade = null;
        MovimentacaoNavio movimentacaoNavio = null;
        	
            atividade = new Atividade();
            movimentacaoNavio = new MovimentacaoNavio();
            //executa valida��o data da ultima situa��o  
            //movimentarNavioVO.setDataInicio(Atividade.verificaAtualizaDataAtividade(movimentarNavioVO.getDataInicio(),dataUltimaSituacao));
            atividade.setFinalizada(Boolean.TRUE);
            atividade.setDtInicio(movimentarNavioVO.getDataInicio());
            atividade.setDtFim(movimentarNavioVO.getDataInicio());
            atividade.setDtInsert(new Date(System.currentTimeMillis()));
            atividade.setIdUser(1L);
            
            if (movimentarNavioVO.getTipoAtividade().equals(TipoAtividadeEnum.CHEGADA_DE_NAVIO)) {
                atividade.setTipoAtividade(TipoAtividadeEnum.CHEGADA_DE_NAVIO);
            } else if (movimentarNavioVO.getTipoAtividade().equals(TipoAtividadeEnum.SAIDA_DE_NAVIO)) {
                atividade.setTipoAtividade(TipoAtividadeEnum.SAIDA_DE_NAVIO);
            } else if (movimentarNavioVO.getTipoAtividade().equals(TipoAtividadeEnum.NAVIO_BARRA)) {
            	atividade.setTipoAtividade(TipoAtividadeEnum.NAVIO_BARRA);            
            } else if (movimentarNavioVO.getTipoAtividade().equals(TipoAtividadeEnum.NAVIO_EXCLUIDO)) {
                atividade.setTipoAtividade(TipoAtividadeEnum.NAVIO_EXCLUIDO);            
            }

            MetaBerco metaBerco = movimentarNavioVO.getMetaBercoDestino();
            Berco bercoDestino = null;
            if (metaBerco == null && movimentarNavioVO.getTipoAtividade().equals(TipoAtividadeEnum.NAVIO_EXCLUIDO)) {
                MetaNavio metaNavio = movimentarNavioVO.getMetaNavio();
                Navio navioAtracado = metaNavio.clonarStatus(movimentarNavioVO.getDataInicio());                
                navioAtracado.setBercoDeAtracacao(null);
                navioAtracado.setIdentificadorBerco(null);
                navioAtracado.setStatusEmbarque(StatusNavioEnum.EXCLUIDO);                                 
            } else {            
                bercoDestino = metaBerco.gerarAtividadeBerco(movimentarNavioVO);
            }    

            // gerar atividade para o pier 

            /**
               * movimentacao navio lugar de empilhament 
               */
            movimentacaoNavio.setDtInsert(new Date(System.currentTimeMillis()));
            movimentacaoNavio.setIdUser(1L);
            movimentacaoNavio.setDtInicio(movimentarNavioVO.getDataInicio());
            movimentacaoNavio.setDtFim(movimentarNavioVO.getDataInicio());

            movimentacaoNavio.setBercoDestino(bercoDestino);
            movimentacaoNavio.setNavio(movimentarNavioVO.getMetaNavio().retornaStatusHorario(movimentarNavioVO.getDataInicio()));

            atividade.addMovimentacaoNavio(movimentacaoNavio);
      
        return atividade;
    }
    
   /* public Atividade movimentarNavio(MetaNavio metaNavio) throws AtividadeException {
    MetaNavioDAO dao = new MetaNavioDAO();
    dao.salvaMetaNavio(metaNavio);
    dao = null;*/
    
}

