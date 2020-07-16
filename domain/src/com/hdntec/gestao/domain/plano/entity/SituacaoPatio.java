package com.hdntec.gestao.domain.plano.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.util.PropertiesUtil;


/**
 * Representa a situa??o da opera??o em um determinado instante. Essa situa??o ?
 * composta basicamente pelas propriedades de tr?s componentes:
 * <ul>
 * <li>a lista de pilhas do p?tio;
 * <li>a planta.
 * </ul>
 * 
 * As situa??es de p?tio s?o geradas por atividades.
 * g
 * Uma situa??o pode ser realizada ou planejada.
 * 
 * @author andre
 * 
 */
@Entity
public class SituacaoPatio extends StatusEntity<SituacaoPatio> {

	// private FilaDeNavios filaDeNavios;
	/** Indica se os itens da situacaoPatio podem ser editados */
	private boolean statusEdicao = Boolean.FALSE;

	/** Indica de esta situacao de patio ? uma situacao persistida ou nao */
	private Boolean situacaoPatioPersistida;

	/** Serializa??o do objeto */
	private static final long serialVersionUID = 2770638426388456672L;

	/** identificador de situacao de patio */
	private Long idSituacaoPatio;

	/** se a atividade ? realizada */
	private Boolean ehRealizado;

	/** as propriedades da lista de pilhas dessa situaça de p?tio */
   @Transient
	private List<Pilha> listaDePilhasNosPatios;

	/** as propriedades da {@link Planta} dessa situa??o de p?tio */
	private Planta planta;

	/** o indice da exibicao da situacao no slider */
	private Integer indiceSituacaoPatio;

	/** A Atividade que deu origem a esta situacao */
	private Atividade atividade;

	/** Plano de situacao de patio na qual a situacao pertence */
	private PlanoEmpilhamentoRecuperacao planoEmpilhamento;

	/**
	 * Contrutor padrao.
	 */
	public SituacaoPatio() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "situacaopatio_seq")
	@SequenceGenerator(name = "situacaopatio_seq", sequenceName = "seqsituacaopatio")
	public Long getIdSituacaoPatio() {
		return idSituacaoPatio;
	}

	@Column
	public Boolean getEhRealizado() {
		return ehRealizado;
	}

	@Transient
	public List<Pilha> getListaDePilhasNosPatios(Date data) {
		if (listaDePilhasNosPatios == null) {
			listaDePilhasNosPatios = new ArrayList<Pilha>();
		}
		List<Patio> patios = getPlanta().getListaPatios(data);

		for (Patio patio : patios) {
			recuperarPilhaDaSitucao(patio);
		}

		return listaDePilhasNosPatios;
	}

	private void recuperarPilhaDaSitucao(Patio patio) {
		List<Baliza> balizas = patio.getListaDeBalizas(this.getDtInicio());
		for (Baliza baliza : balizas) {
			Pilha pilha = baliza.retornaStatusHorario(this.getDtInicio());
			if (pilha != null && pilha.getListaDeBalizas() != null && pilha.getListaDeBalizas().size() > 0 && !listaDePilhasNosPatios.contains(pilha)) {				
				listaDePilhasNosPatios.add(pilha);
			}
		}
	}

	public List<Pilha> recuperarPilhasPatio(Patio patio) {
        List<Baliza> balizas = patio.getListaDeBalizas(this.getDtInicio());
        List<Pilha> result = new ArrayList<Pilha>(); 
        for (Baliza baliza : balizas) {
            Pilha pilha = baliza.retornaStatusHorario(this.getDtInicio());
            if (pilha != null && pilha.getListaDeBalizas() != null && pilha.getListaDeBalizas().size() > 0 && !result.contains(pilha)) {
                result.add(pilha);
            }
        }
        return result;
    }
	
	@OneToOne
	@ForeignKey(name = "fk_sitPatio_planta")
	@JoinColumn(name = "id_Planta")
	public Planta getPlanta() {
		return planta;
	}

	public void setEhRealizado(Boolean ehRealizado) {
		this.ehRealizado = ehRealizado;
	}

	public void setIdSituacaoPatio(Long idSituacaoPatio) {
		this.idSituacaoPatio = idSituacaoPatio;
	}

	/*
	 * public void setListaDePilhasNosPatios(List<Pilha> listaDePilhasNosPatios)
	 * { this.listaDePilhasNosPatios = listaDePilhasNosPatios; }
	 */
	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public void setPlanoEmpilhamento(
			PlanoEmpilhamentoRecuperacao planoEmpilhamento) {
		this.planoEmpilhamento = planoEmpilhamento;
	}

	@ManyToOne
	@ForeignKey(name = "fk_plano_situacao")
	@JoinColumn(name = "idplanoEmpRec", nullable = true, insertable = true, updatable = true)
	public PlanoEmpilhamentoRecuperacao getPlanoEmpilhamento() {
		return planoEmpilhamento;
	}

	@Override
	public String toString() {
		return "Situação de Patio em "
				+ new SimpleDateFormat(PropertiesUtil
						.buscarPropriedade("formato.campo.datahora"))
						.format(getDtInicio());
	}

	public String obterDataHoraFormatada() {
		return new SimpleDateFormat(PropertiesUtil
				.buscarPropriedade("formato.campo.datahora"))
				.format(getDtInicio());
	}

	public String obterDataFormatada() {
		return new SimpleDateFormat(PropertiesUtil
				.buscarPropriedade("formato.campo.data")).format(getDtInicio());
	}

	public String obterHoraFormatada() {
		return new SimpleDateFormat(PropertiesUtil
				.buscarPropriedade("formato.campo.hora")).format(getDtInicio());
	}

	@Transient
	public Integer getIndiceSituacaoPatio() {
		return indiceSituacaoPatio;
	}

	public void setIndiceSituacaoPatio(Integer indiceSituacaoPatio) {
		this.indiceSituacaoPatio = indiceSituacaoPatio;
	}

	@OneToOne
	//@Cascade(CascadeType.ALL)
	@ForeignKey(name = "fk_sitPatio_atividade")
	@JoinColumn(name = "idAtividade")
	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	/**
	 * Metodo que verifica se exista na lista de pilha do patio alguma pilha de
	 * emergencia que pode ser novimentada para o patio ou para o carregamento.
	 * 
	 * @return
	 */
	public boolean verificarExistenciaPilhaEmergenciaParaMovimentacao(Date data) {
		boolean existePilha = false;
		for (Pilha pilhaPatio : getListaDePilhasNosPatios(data)) {
			/*
			 * if (pilhaPatio.getEmergenciaOrigem() != null) { if
			 * (pilhaPatio.getEmergenciaOrigem().getPossibilitaRetornoPatio()) {
			 * existePilha = true; break; } }
			 */
		}
		return existePilha;
	}

	
	/**
	 * Metodo que retorna uma lista com todas as pilha de PSM ou pellet feed que
	 * podem ser movimentadas para o patio ou para o carregamento
	 * 
	 * @return
	 */
	public List<Pilha> obterListaPilhasPSMPelletFeedParaMovimentacao(
			TipoDeProdutoEnum tipoProduto,Patio patio) {
	    this.recuperarPilhaDaSitucao(patio);
	    
	    List<Pilha> listaPilhaDisponivel = new ArrayList<Pilha>();
	    List<Pilha> lista = recuperarPilhasPatio(patio);
		for (Pilha pilhaPatio : lista) {
			if (pilhaPatio.verificarTipoProdutoPilha(tipoProduto)) {
				listaPilhaDisponivel.add(pilhaPatio);
			}
		}
		return listaPilhaDisponivel;
	}


	/**
     * Metodo que retorna uma lista com todas as pilha de PSM ou pellet feed que
     * podem ser movimentadas para o patio ou para o carregamento
     * 
     * @return
     */
    public List<Pilha> obterListaPilhasEmergenciaMovimentacao(Patio patio) {
        this.recuperarPilhaDaSitucao(patio);
        
        List<Pilha> listaPilhaDisponivel = new ArrayList<Pilha>();
        List<Pilha> lista = recuperarPilhasPatio(patio);
        for (Pilha pilhaPatio : lista) {
            if (pilhaPatio.verificarTipoPilha(EnumTipoBaliza.EMERGENCIA_P5)) {
                listaPilhaDisponivel.add(pilhaPatio);
            }
            if (pilhaPatio.verificarTipoPilha(EnumTipoBaliza.EMERGENCIA_TP17)) {
                listaPilhaDisponivel.add(pilhaPatio);
            }
            if (pilhaPatio.verificarTipoPilha(EnumTipoBaliza.EMERGENCIA_TP009)) {
                listaPilhaDisponivel.add(pilhaPatio);
            }
        }
        return listaPilhaDisponivel;
    }

	
	@Transient
	public boolean isStatusEdicao() {
		return statusEdicao;
	}

	public void setStatusEdicao(boolean statusEdicao) {
		this.statusEdicao = statusEdicao;
	}


	@Transient
	public Boolean getSituacaoPatioPersistida() {
		return situacaoPatioPersistida;
	}

	public void setSituacaoPatioPersistida(Boolean situacaoPatioPersistida) {
		this.situacaoPatioPersistida = situacaoPatioPersistida;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SituacaoPatio copiarStatus() {
		SituacaoPatio newStatus = null;
		newStatus = super.copiarStatus();
		newStatus.setIdSituacaoPatio(null);
		return newStatus;
	}
}
