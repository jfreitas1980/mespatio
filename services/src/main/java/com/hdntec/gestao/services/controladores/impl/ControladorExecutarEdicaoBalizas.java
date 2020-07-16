package com.hdntec.gestao.services.controladores.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.vo.atividades.EdicaoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.services.controladores.IControladorExecutarEdicaoBalizas;


public class ControladorExecutarEdicaoBalizas implements IControladorExecutarEdicaoBalizas 
{
	private static ControladorExecutarEdicaoBalizas instance = null;

	/**
	 * Construtor privado.
	 */
	public ControladorExecutarEdicaoBalizas() {
	}

	/**
	 * Retorna a instancia singleton da fAbrica.
	 * 
	 * @return TransLogicDAOFactory
	 */
	public static ControladorExecutarEdicaoBalizas getInstance() {
		if (instance == null) {
			instance = new ControladorExecutarEdicaoBalizas();
		}
		return instance;
	}

    
    public Atividade editarBalizas(EdicaoVO edicaoVO) throws AtividadeException
   {
      List<Pilha> pilhasDeletadas = new ArrayList<Pilha>();	
      Atividade atividade = new Atividade();
      atividade.setTipoAtividade(TipoAtividadeEnum.ATIVIDADE_EDICAO_BALIZAS);      
      atividade.setDtInicio(edicaoVO.getDataInicio());
      atividade.setDtFim(edicaoVO.getDataInicio());
      atividade.setDtInsert(new Date(System.currentTimeMillis()));
      atividade.setIdUser(1L);

      LugarEmpilhamentoRecuperacao lugarEmpRecDestino = new LugarEmpilhamentoRecuperacao();
      
      try
      {
    	  lugarEmpRecDestino.setOrdem(0);
    	  lugarEmpRecDestino.setDtInicio(edicaoVO.getDataInicio());
          lugarEmpRecDestino.setDtFim(edicaoVO.getDataInicio());
          lugarEmpRecDestino.setDtInsert(new Date(System.currentTimeMillis()));
          lugarEmpRecDestino.setIdUser(1L);
          lugarEmpRecDestino.setNomeDoLugarEmpRec(edicaoVO.getNomePilha());
          lugarEmpRecDestino.setQuantidade(0D);
          
          
          Pilha origem = edicaoVO.getListaBalizas().get(0).retornaStatusHorario(edicaoVO.getDataInicio());

          Pilha pilha = Pilha.criaPilha(edicaoVO.getNomePilha(), edicaoVO.getListaBalizas(), null, edicaoVO.getDataInicio().getTime(), origem);
          //Pilha.criaPilhasDescontinuas(pilha.getListaDeBalizas(), null, edicaoVO.getDataInicio(), pilha.getNomePilha(), "");
          Pilha.decompoePilhaEditada(pilha, edicaoVO.getDataInicio());
         // Pilha.criaPilhasDescontinuas(pilha.getListaDeBalizas(), null, edicaoVO.getDataInicio(), pilha.getNomePilha(), "");          
          lugarEmpRecDestino.addPilhaEditada(pilha);
         // lugarEmpRecDestino.addPilhaEditada(pilha);          
          lugarEmpRecDestino.addBaliza(edicaoVO.getListaBalizas());
          atividade.addLugarEmpilhamento(lugarEmpRecDestino);
                  
      } catch (Exception e)
      {
         e.printStackTrace();
      }
      return atividade;

   }
    
    public Atividade unificarBalizas(EdicaoVO edicaoVO) throws AtividadeException
    {
       Atividade atividade = new Atividade();
       atividade.setTipoAtividade(TipoAtividadeEnum.ATIVIDADE_EDICAO_BALIZAS);      
       atividade.setDtInicio(edicaoVO.getDataInicio());
       atividade.setDtFim(edicaoVO.getDataInicio());
       atividade.setDtInsert(new Date(System.currentTimeMillis()));
       atividade.setIdUser(1L);

       LugarEmpilhamentoRecuperacao lugarEmpRecDestino = new LugarEmpilhamentoRecuperacao();
       
       try
       {
           lugarEmpRecDestino.setDtInicio(edicaoVO.getDataInicio());
           lugarEmpRecDestino.setDtFim(edicaoVO.getDataInicio());
           lugarEmpRecDestino.setDtInsert(new Date(System.currentTimeMillis()));
           lugarEmpRecDestino.setIdUser(1L);
           lugarEmpRecDestino.setNomeDoLugarEmpRec(edicaoVO.getNomePilha());
           lugarEmpRecDestino.setQuantidade(0D);
           
           Pilha.criaPilha(edicaoVO.getNomePilha(), edicaoVO.getListaBalizas(), null, edicaoVO.getDataInicio().getTime(), null);// (edicaoVO.getListaBalizas(), null, edicaoVO.getDataInicio(), edicaoVO.getNomePilha(), null);
           
          
           lugarEmpRecDestino.addBaliza(edicaoVO.getListaBalizas());
           atividade.addLugarEmpilhamento(lugarEmpRecDestino);
                   
       } catch (Exception e)
       {
          e.printStackTrace();
       }
       return atividade;

    }
    
}
