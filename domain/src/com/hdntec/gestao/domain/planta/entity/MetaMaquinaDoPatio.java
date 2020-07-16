
package com.hdntec.gestao.domain.planta.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.hdntec.gestao.domain.AbstractMetaEntity;
import com.hdntec.gestao.domain.plano.dao.AtividadeDAO;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Interdicao;
import com.hdntec.gestao.domain.planta.entity.status.Manutencao;
import com.hdntec.gestao.domain.planta.entity.status.ManutencaoMaquina;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.vo.atividades.AtividadeRecuperarEmpilharVO;


/**
 * Classe que representa uma máquina que trabalha no pátio
 * 
 * @author andre
 *
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "nomeMaquina"))
public class MetaMaquinaDoPatio extends AbstractMetaEntity<MaquinaDoPatio> {

    /** Serialização */
    private static final long serialVersionUID = -3760150985202823458L;

    /** id da entidade */
    private Long idMaquina;

    /** Nome da máquina */
    private String nomeMaquina;

    /** Flag que bloqueia a inversao da lanca da maquina */
    private Boolean giraLanca;

    /** a correia na qual esta máquina se encontra */
    private MetaCorreia metaCorreia;

    private List<ManutencaoMaquina> listaManutencao;

    /** tag do PIMS estado da maquina */
    private String tagPimsEstado;

    /** tag do PIMS do posicionamento da maquina */
    private String tagPimsPosicionamento;

    /** tag do PIMS angulo lateral da lanca */
    private String tagPimsAnguloLaterialLanca;

    /** tag do PIMS Angulo de altura da lanca */
    private String tagPimsAnguloAlturaLanca;

    /** tag do PIMS Balanca da maquina */
    private String tagPimsBalanca;

    /** o tipo desta máquina */
    private TipoMaquinaEnum tipoDaMaquina;

    private MetaPatio metaPatio;

    /**
     * Construtor padrão
     */
    public MetaMaquinaDoPatio() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "maq_seq")
    @SequenceGenerator(name = "maq_seq", sequenceName = "seqmaq")
    public Long getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(Long idMaquina) {
        this.idMaquina = idMaquina;
    }

    @Column(nullable = false, length = 60)
    public String getNomeMaquina() {
        return nomeMaquina;
    }

    public void setNomeMaquina(String nomeMaquina) {
        this.nomeMaquina = nomeMaquina;
    }

    @ManyToOne
    public MetaCorreia getMetaCorreia() {
        return metaCorreia;
    }

    public void setMetaCorreia(MetaCorreia correia) {
        this.metaCorreia = correia;
    }

    @Column(nullable = true)
    public String getTagPimsEstado() {
        return tagPimsEstado;
    }

    public void setTagPimsEstado(String tagPimsEstado) {
        this.tagPimsEstado = tagPimsEstado;
    }

    @Column(nullable = true)
    public String getTagPimsPosicionamento() {
        return tagPimsPosicionamento;
    }

    public void setTagPimsPosicionamento(String tagPimsPosicionamento) {
        this.tagPimsPosicionamento = tagPimsPosicionamento;
    }

    @Column(nullable = true)
    public String getTagPimsAnguloAlturaLanca() {
        return tagPimsAnguloAlturaLanca;
    }

    public void setTagPimsAnguloAlturaLanca(String tagPimsAnguloAlturaLanca) {
        this.tagPimsAnguloAlturaLanca = tagPimsAnguloAlturaLanca;
    }

    @Column(nullable = true)
    public String getTagPimsAnguloLaterialLanca() {
        return tagPimsAnguloLaterialLanca;
    }

    public void setTagPimsAnguloLaterialLanca(String tagPimsAnguloLaterialLanca) {
        this.tagPimsAnguloLaterialLanca = tagPimsAnguloLaterialLanca;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public TipoMaquinaEnum getTipoDaMaquina() {
        return tipoDaMaquina;
    }

    public void setTipoDaMaquina(TipoMaquinaEnum tipoDaMaquina) {
        this.tipoDaMaquina = tipoDaMaquina;
    }

    @Column
    public Boolean getGiraLanca() {
        return giraLanca;
    }

    public void setGiraLanca(Boolean giraLanca) {
        this.giraLanca = giraLanca;
    }

    @Override
    public String toString() {
        //      return nomeMaquina + " - " + estado.toString();
        return nomeMaquina;
    }

    @Column(nullable = true, length = 30)
    public String getTagPimsBalanca() {
        return tagPimsBalanca;
    }

    public void setTagPimsBalanca(String tagPimsBalanca) {
        this.tagPimsBalanca = tagPimsBalanca;
    }

    @Override
    @OneToMany(fetch = FetchType.LAZY,mappedBy="metaMaquina")
    @Fetch(FetchMode.SELECT)
    //@Cascade(value = {
              //      CascadeType.ALL, CascadeType.SAVE_UPDATE})
    public List<MaquinaDoPatio> getListaStatus() {
        if (listaStatus == null) {
            listaStatus = new ArrayList<MaquinaDoPatio>();
        }
        return listaStatus;
    }

    @Override
    @Transient
    public void incluirNovoStatus(MaquinaDoPatio novoStatus, Date horaStatus) {
        super.incluirNovoStatus(novoStatus, horaStatus);
        novoStatus.setMetaMaquina(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((giraLanca == null) ? 0 : giraLanca.hashCode());
        result = prime * result + ((idMaquina == null) ? 0 : idMaquina.hashCode());
        result = prime * result + ((metaCorreia == null) ? 0 : metaCorreia.hashCode());
        result = prime * result + ((metaPatio == null) ? 0 : metaPatio.hashCode());
        result = prime * result + ((nomeMaquina == null) ? 0 : nomeMaquina.hashCode());
        result = prime * result + ((tagPimsAnguloAlturaLanca == null) ? 0 : tagPimsAnguloAlturaLanca.hashCode());
        result = prime * result + ((tagPimsAnguloLaterialLanca == null) ? 0 : tagPimsAnguloLaterialLanca.hashCode());
        result = prime * result + ((tagPimsBalanca == null) ? 0 : tagPimsBalanca.hashCode());
        result = prime * result + ((tagPimsEstado == null) ? 0 : tagPimsEstado.hashCode());
        result = prime * result + ((tagPimsPosicionamento == null) ? 0 : tagPimsPosicionamento.hashCode());
        result = prime * result + ((tipoDaMaquina == null) ? 0 : tipoDaMaquina.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        MetaMaquinaDoPatio other = (MetaMaquinaDoPatio) obj;
        if (giraLanca == null) {
            if (other.giraLanca != null)
                return false;
        } else if (!giraLanca.equals(other.giraLanca))
            return false;
        if (idMaquina == null) {
            if (other.idMaquina != null)
                return false;
        } else if (!idMaquina.equals(other.idMaquina))
            return false;
        if (metaCorreia == null) {
            if (other.metaCorreia != null)
                return false;
        } else if (!metaCorreia.equals(other.metaCorreia))
            return false;
        if (metaPatio == null) {
            if (other.metaPatio != null)
                return false;
        } else if (!metaPatio.equals(other.metaPatio))
            return false;
        if (nomeMaquina == null) {
            if (other.nomeMaquina != null)
                return false;
        } else if (!nomeMaquina.equals(other.nomeMaquina))
            return false;
        if (tagPimsAnguloAlturaLanca == null) {
            if (other.tagPimsAnguloAlturaLanca != null)
                return false;
        } else if (!tagPimsAnguloAlturaLanca.equals(other.tagPimsAnguloAlturaLanca))
            return false;
        if (tagPimsAnguloLaterialLanca == null) {
            if (other.tagPimsAnguloLaterialLanca != null)
                return false;
        } else if (!tagPimsAnguloLaterialLanca.equals(other.tagPimsAnguloLaterialLanca))
            return false;
        if (tagPimsBalanca == null) {
            if (other.tagPimsBalanca != null)
                return false;
        } else if (!tagPimsBalanca.equals(other.tagPimsBalanca))
            return false;
        if (tagPimsEstado == null) {
            if (other.tagPimsEstado != null)
                return false;
        } else if (!tagPimsEstado.equals(other.tagPimsEstado))
            return false;
        if (tagPimsPosicionamento == null) {
            if (other.tagPimsPosicionamento != null)
                return false;
        } else if (!tagPimsPosicionamento.equals(other.tagPimsPosicionamento))
            return false;
        if (tipoDaMaquina == null) {
            if (other.tipoDaMaquina != null)
                return false;
        } else if (!tipoDaMaquina.equals(other.tipoDaMaquina))
            return false;
        return true;
    }

    @Override
    public MaquinaDoPatio clonarStatus(Date horario) {
        MaquinaDoPatio maquina = super.clonarStatus(horario);
        maquina.setIdMaquina(null);
        return maquina;
    }

    private static MaquinaDoPatio montarAtividade(AtividadeRecuperarEmpilharVO recuperacaoVO,
                    List<LugarEmpilhamentoRecuperacao> lugaresAnteriores, List<Baliza> balizasDestino, Atividade atividade,
                    Boolean finalizarAtividade, MetaMaquinaDoPatio metaMaquina) {
        MaquinaDoPatio novaStatusMaquina;
        if (finalizarAtividade) {
            novaStatusMaquina = metaMaquina.clonarStatus(recuperacaoVO.getDataFim());
        } else {
            novaStatusMaquina = metaMaquina.clonarStatus(recuperacaoVO.getDataInicio());
        }

        /** verifica se a maquina está excutando alguma atividade anterior
         * caso afirmativo guarda o lugar de empilhamento em uma lista para finalizar os mesmos
         * 
         * E SE ATIVIDADE NÃO ESTÁ FINALIZADA/ MAQUINA EM OPERAÇÃO - CENÁRIO DE ATUALIZAÇÃO DE EMPILHAMENTO
         */

        if (novaStatusMaquina.getAtividade() != null && novaStatusMaquina.getEstado().equals(EstadoMaquinaEnum.OPERACAO)
                        && lugaresAnteriores != null) {
            if (atividade.getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO)) {
                atividade.setAtividadeAnterior(metaMaquina.existeEmpilhamentoPendente());
            } else if (atividade.getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO)) {
                atividade.setAtividadeAnterior(metaMaquina.existeRecuperacaoPendente());
            } else if (atividade.getTipoAtividade().equals(TipoAtividadeEnum.MOVIMENTAR_PILHA_PELLET_FEED)) {
                atividade.setAtividadeAnterior(metaMaquina.existeMovimentacaoPelletPendente());
            } else if (atividade.getTipoAtividade().equals(TipoAtividadeEnum.MOVIMENTAR_PILHA_PSM)) {
                atividade.setAtividadeAnterior(metaMaquina.existeMovimentacaoPSMPendente());
            } else if (atividade.getTipoAtividade().equals(TipoAtividadeEnum.MOVIMENTAR_PILHA_EMERGENCIA)) {
                atividade.setAtividadeAnterior(metaMaquina.existeMovimentacaoEmergenciaPendente());
            } else if (atividade.getTipoAtividade().equals(TipoAtividadeEnum.RETORNO_PELLET_FEED)) {
                atividade.setAtividadeAnterior(metaMaquina.existeMovimentacaoRetornoPelletFeedPendente());
            }
            atividade.setFinalizada(Boolean.TRUE);
            if (atividade.getAtividadeAnterior() != null) {
                lugaresAnteriores.addAll(atividade.getAtividadeAnterior().getListaDeLugaresDeEmpilhamentoRecuperacao());
            }
        }

        novaStatusMaquina.setAtividade(atividade);

        // seta o estado da maquina
        if (finalizarAtividade) {
            novaStatusMaquina.setEstado(EstadoMaquinaEnum.OCIOSA);
            novaStatusMaquina.setAtividade(null);
        } else {
            novaStatusMaquina.setEstado(EstadoMaquinaEnum.OPERACAO);
        }

        // posiciona na primeira baliza 
    	Collections.sort(balizasDestino,Baliza.comparadorBaliza);
        novaStatusMaquina.setPosicao(balizasDestino.get(0));
        novaStatusMaquina.setPatio(balizasDestino.get(0).getPatio());

        if (recuperacaoVO.getSentidoRecuperacao() != null && finalizarAtividade) {
        
        	if (recuperacaoVO.getSentidoRecuperacao().equals(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL)) {
        		Collections.reverse(balizasDestino);
        		novaStatusMaquina.setPosicao(balizasDestino.get(0));  	
            }            	
                        
        }

        return novaStatusMaquina;
    }

    /**
     * gerarAtividadeMaquina
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 23/06/2010
     * @see
     * @param 
     * @return void
     */
    public static void gerarAtividadeMaquina(AtividadeRecuperarEmpilharVO recuperacaoVO,
                    List<LugarEmpilhamentoRecuperacao> lugaresAnteriores, List<Baliza> balizasOrigem,
                    List<Baliza> balizasDestino, List<MaquinaDoPatio> maquinasDestino, List<Correia> correiasDestino,
                    Atividade atividade, Boolean finalizarAtividade) {
        /**
         *  2- aplica atividade nas maquinas 
         */
        for (MetaMaquinaDoPatio metaMaquina : recuperacaoVO.getListaMaquinas()) {
            MaquinaDoPatio novaStatusMaquina = null;
            if (metaMaquina.getTipoDaMaquina().equals(TipoMaquinaEnum.PA_CARREGADEIRA)) {
                novaStatusMaquina = montarAtividade(recuperacaoVO, lugaresAnteriores, balizasOrigem, atividade,
                                finalizarAtividade, metaMaquina);
            } else {
                novaStatusMaquina = montarAtividade(recuperacaoVO, lugaresAnteriores, balizasDestino, atividade,
                                finalizarAtividade, metaMaquina);
                //gera atividade para a correia da maquina   
                correiasDestino.addAll(MetaCorreia.gerarAtividadeCorreia(novaStatusMaquina, finalizarAtividade));
                // adicona na lista                                
            }
            maquinasDestino.add(novaStatusMaquina);
        }
    }

    public Atividade existeEmpilhamentoPendente() {

        Atividade result = null;
        AtividadeDAO dao = new AtividadeDAO();
        result = dao.existeEmpilhamentoPendente(this);
        return result;

    }

    public Atividade existeRecuperacaoPendente() {

        Atividade result = null;
        AtividadeDAO dao = new AtividadeDAO();
        result = dao.existeRecuperacaoPendente(this);
        return result;

    }

    public Atividade existeMovimentacaoEmergenciaPendente() {

        Atividade result = null;
        AtividadeDAO dao = new AtividadeDAO();
        result = dao.existeMovimentacaoEmergenciaPendente(this);
        return result;

    }

    public Atividade existeMovimentacaoPSMPendente() {

        Atividade result = null;
        AtividadeDAO dao = new AtividadeDAO();
        result = dao.existeMovimentacaoPSMPendente(this);
        return result;

    }

    public Atividade existeMovimentacaoPelletPendente() {

        Atividade result = null;
        AtividadeDAO dao = new AtividadeDAO();
        result = dao.existeMovimentacaoPelletPendente(this);
        return result;

    }

    public Atividade existeMovimentacaoRetornoPelletFeedPendente() {

        Atividade result = null;
        AtividadeDAO dao = new AtividadeDAO();
        result = dao.existeMovimentacaoRetornoPelletFeedPendente(this);
        return result;

    }

    public void addManutencao(ManutencaoMaquina interdicao) {
        if (getListaManutencao() == null) {
            setListaManutencao(new ArrayList<ManutencaoMaquina>());
        }

        if (!getListaManutencao().contains(interdicao)) {
            getListaManutencao().add(interdicao);
            interdicao.setMaquina(this);
        }
    }

    public void addManutencao(List<ManutencaoMaquina> interdicoes) {
        if (interdicoes != null) {
            for (ManutencaoMaquina interdicao : interdicoes) {
                addManutencao(interdicao);
            }
        }
    }

    @Transient
    public Boolean maquinaInterditado(Date horaExecucao) {
        Boolean result = Boolean.FALSE;
        Collections.sort(getListaManutencao(), Manutencao.comparadorManutencao);
        List<ManutencaoMaquina> campanhas = getListaManutencao();

        for (ManutencaoMaquina inter : campanhas) {
            if (inter.getDataFinal().getTime() >= horaExecucao.getTime()
                            && inter.getDataInicial().getTime() <= horaExecucao.getTime()) {
                result = Boolean.TRUE;
                break;
            }
        }
        return result;
    }

    @Transient
    public Boolean maquinaInterditado(List<ManutencaoMaquina> lstInterdicao, Date horaExecucao) {
        Boolean result = Boolean.FALSE;
        Collections.sort(lstInterdicao, Manutencao.comparadorManutencao);
        for (ManutencaoMaquina inter : lstInterdicao) {
            if (inter.getDataFinal().getTime() >= horaExecucao.getTime()
                            && inter.getDataInicial().getTime() <= horaExecucao.getTime()) {
                result = Boolean.TRUE;
                break;
            }
        }
        return result;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {
                    javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST},mappedBy="maquina")
    @Fetch(FetchMode.SELECT)
    @Cascade(value = {
                    CascadeType.SAVE_UPDATE})
    public List<ManutencaoMaquina> getListaManutencao() {
        if (listaManutencao == null) {
            listaManutencao = new ArrayList<ManutencaoMaquina>();
        }        
        return listaManutencao;
    }

    public void setListaManutencao(List<ManutencaoMaquina> listaManutencao) {
        this.listaManutencao = listaManutencao;
    }

    @Transient
    public Boolean maquinaManutencao(Date horaExecucao)
    {       
       Boolean result = Boolean.FALSE;
       Collections.sort(getListaManutencao(),ManutencaoMaquina.comparadorManutencao);
       List<ManutencaoMaquina> campanhas =  getListaManutencao();
       
       for (ManutencaoMaquina inter : campanhas ) {           
           if (inter.getDataFinal().getTime() >= horaExecucao.getTime() &&
                           inter.getDataInicial().getTime() <= horaExecucao.getTime()) {                                      
               result =  Boolean.TRUE;
               break;
           }
       }       
       return result;
    }
    
}