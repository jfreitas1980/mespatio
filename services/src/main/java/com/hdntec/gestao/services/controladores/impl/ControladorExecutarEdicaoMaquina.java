
package com.hdntec.gestao.services.controladores.impl;

import java.util.Date;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.vo.atividades.EdicaoMaquinaVO;
import com.hdntec.gestao.domain.vo.atividades.MovimentacaoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.services.controladores.IControladorExecutarEdicaoMaquinas;

/**
 * <P><B>Description :</B><BR>
 * General ControladorExecutarEdicaoMaquina
 * </P>
 * <P>
 * <B>
 * SAs : <BR>
 * 8148
 * </B>
 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
 * @since 28/10/2010
 * @version $Revision: 1.1 $
 */
public class ControladorExecutarEdicaoMaquina implements IControladorExecutarEdicaoMaquinas {

    private static ControladorExecutarEdicaoMaquina instance = null;

    /**
     * Construtor privado.
     */
    public ControladorExecutarEdicaoMaquina() {
    }

    /**
     * Retorna a instancia singleton da fAbrica.
     * 
     * @return ControladorExecutarEdicaoMaquina
     */
    public static ControladorExecutarEdicaoMaquina getInstance() {
        if (instance == null) {
            instance = new ControladorExecutarEdicaoMaquina();
        }
        return instance;
    }


	@Override
	public Atividade editarMaquinas(EdicaoMaquinaVO edicaoVO)
			throws AtividadeException {
		// TODO Auto-generated method stub
		Atividade atividade = new Atividade();
	      atividade.setTipoAtividade(TipoAtividadeEnum.ATIVIDADE_EDICAO_MAQUINAS);      
	      atividade.setDtInicio(edicaoVO.getDataInicio());
	      atividade.setDtFim(edicaoVO.getDataFim());
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
	          lugarEmpRecDestino.setNomeDoLugarEmpRec("MOVIMENTAR MAQUINA");
	          lugarEmpRecDestino.setQuantidade(0D);
	          
	          Baliza novoStatusBaliza = edicaoVO.getPosicao().clonarStatus(edicaoVO.getDataInicio());
	   	      MaquinaDoPatio novaStatusMaquina = edicaoVO.getMaquina().clonarStatus(edicaoVO.getDataInicio());
	   	      
	   	      novaStatusMaquina.setEstado(edicaoVO.getEstado());
	   	      novaStatusMaquina.setPosicao(novoStatusBaliza);
	   	      novaStatusMaquina.setPatio(novoStatusBaliza.getPatio());
	   	      novaStatusMaquina.setTaxaDeOperacaoNominal(edicaoVO.getTaxaDeOperacaoNominal());
	   	      novaStatusMaquina.setVelocidadeDeslocamento(edicaoVO.getVelocidadeDeslocamento());
	   	      novaStatusMaquina.setGiraLanca(edicaoVO.getGirarLanca());   	      
	   	      lugarEmpRecDestino.addMaquinaDoPatio(novaStatusMaquina);          
	          
	   	      lugarEmpRecDestino.addBaliza(novoStatusBaliza);
	          atividade.addLugarEmpilhamento(lugarEmpRecDestino);
	                  
	      } catch (Exception e)
	      {
	         e.printStackTrace();
	      }
	      return atividade;

	}


    
        /**
     * validarManutencaoInterdicao
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 25/01/2011
     * @see
     * @param 
     * @return void
     */
    private void validarManutencaoInterdicao(MovimentacaoVO movimentacaoVO) throws AtividadeException {
        Boolean interditada = Boolean.FALSE;
        StringBuffer strInterditadas = new StringBuffer();
        Date dataInterdicao = movimentacaoVO.getDataInicio();
        if (movimentacaoVO.getDataFim() != null ) {
            dataInterdicao = movimentacaoVO.getDataFim(); 
        }   
        
        for (MetaBaliza novaBaliza : movimentacaoVO.getListaBalizasDestino()) {            
            if (novaBaliza.balizaInterditado(dataInterdicao)) {
                interditada = Boolean.TRUE;
                strInterditadas.append(novaBaliza.getNomeBaliza()).append("\n");                       
            }
        }
        
        // Maquina 
        for (MetaMaquinaDoPatio novaBaliza : movimentacaoVO.getListaMaquinas()) {            
            if (novaBaliza.maquinaInterditado(dataInterdicao)) {
                interditada = Boolean.TRUE;
                strInterditadas.append(novaBaliza.getNomeMaquina()).append("\n");                       
            }
        }

        // Correias Máquinas 
        for (MetaMaquinaDoPatio novaBaliza : movimentacaoVO.getListaMaquinas()) {            
            if (!novaBaliza.getTipoDaMaquina().equals(TipoMaquinaEnum.PA_CARREGADEIRA)) {
                if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                    interditada = Boolean.TRUE;
                    strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                }
            }    
        }
        
        
        //Correias Usinas  
        for (MetaUsina novaBaliza : movimentacaoVO.getListaUsinas()) {                           
                if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                    interditada = Boolean.TRUE;
                    strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                }
         }
        

        //Correias Filtragens  
        for (MetaFiltragem novaBaliza : movimentacaoVO.getListaFiltragens()) {                           
                if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                    interditada = Boolean.TRUE;
                    strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                }
         }

        
        
        if (interditada) {
            throw new AtividadeException("Existem itens sob interdição/manutenção! \n" + strInterditadas.toString() );
        }
    }

    	    
}
