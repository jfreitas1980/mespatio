
package com.hdntec.gestao.domain.planta.entity.status;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;


/**
 * Classe que representa uma máquina que trabalha no pátio
 * 
 * @author andre
 * 
 */
@Entity
public class MaquinaDoPatio extends StatusEntity<MaquinaDoPatio> {

    /** Serialização */
    private static final long serialVersionUID = -3760150985202823458L;

    /** id da entidade */
    private Long idMaquina;

    private MetaMaquinaDoPatio metaMaquina;

    /** estado de operação da máquina */
    private EstadoMaquinaEnum estado;

    /**
     * identifica se a máquina está apontando o braco para o pátio inferior
     * ou superior
     */
    private Boolean bracoNoPatioInferior;

    /** a taxa de operação em ton/h */
    private Double taxaDeOperacaoNominal;

    /** a posição da máquina */
    private Baliza posicao;

    /** a velocidade do deslocamento da maquina em balizas por hora (baliza/h) */
    private Double velocidadeDeslocamento;

    /** taxa de operacao temporaria (ton/h) */
    private Double taxaDeOperacaoTemporaria;

    private LugarEmpilhamentoRecuperacao lugarEmpilhamento;
    
    /** Atividade na qual a maquina esta trabalhando */
    private Atividade atividade;

    private Patio patio;

    /**
     * Construtor padrão
     */
    public MaquinaDoPatio() {
    }

    @OneToOne
    //@Cascade(value = CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "idPatio", nullable = false)
    public Patio getPatio() {
        return patio;
    }

    public void setPatio(Patio patio) {
        this.patio = patio;
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

    @Transient
    public String getNomeMaquina() {
        return getMetaMaquina().getNomeMaquina();
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public EstadoMaquinaEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoMaquinaEnum estado) {
        this.estado = estado;
    }

    @Column(nullable = false, precision = 2)
    public Double getTaxaDeOperacaoNominal() {
        return taxaDeOperacaoNominal;
    }

    public void setTaxaDeOperacaoNominal(Double taxaDeOperacaoNominal) {
        this.taxaDeOperacaoNominal = taxaDeOperacaoNominal;
    }

    @Transient
    public Date getProximaManutencao() {
        return null;
    }

    public void setProximaManutencao(Date proximaManutencao) {

    }

    @OneToOne
    /*@Cascade(value = {
    	      CascadeType.SAVE_UPDATE})*/
    @ForeignKey(name = "fk_maq_baliza")
    @JoinColumn(name = "id_Baliza", nullable = true)
    public Baliza getPosicao() {
        return posicao;
    }

    public void setPosicao(Baliza posicao) {
        this.posicao = posicao;
    }

    @Column(nullable = false)
    public Double getVelocidadeDeslocamento() {
        return velocidadeDeslocamento;
    }

    public void setVelocidadeDeslocamento(Double velocidadeDeslocamento) {
        this.velocidadeDeslocamento = velocidadeDeslocamento;
    }

    public void setTaxaDeOperacaoTemporaria(Double taxaDeOperacaoTemporaria) {
        this.taxaDeOperacaoTemporaria = taxaDeOperacaoTemporaria;
    }

    @Transient
    public Double getTaxaDeOperacaoTemporaria() {
        return this.taxaDeOperacaoTemporaria;
    }

    @Transient
    public String getTagPimsEstado() {
        return getMetaMaquina().getTagPimsEstado();
    }

    @Transient
    public String getTagPimsPosicionamento() {
        return getMetaMaquina().getTagPimsPosicionamento();

    }

    @Transient
    public String getTagPimsAnguloAlturaLanca() {
        return getMetaMaquina().getTagPimsAnguloAlturaLanca();

    }

    public void setTagPimsAnguloAlturaLanca(String tagPimsAnguloAlturaLanca) {

    }

    @Transient
    public String getTagPimsAnguloLaterialLanca() {
        return getMetaMaquina().getTagPimsAnguloLaterialLanca();
    }

    @Override
    public String toString() {
        return getMetaMaquina().getNomeMaquina();// + " - " + this.getEstado() + " - Baliza : " + this.getPosicao().getNumero() + " - Pátio: " +  this.getPosicao().getPatio().getNomePatio(); 
    }

    @ManyToOne    
    @ForeignKey(name = "fk_maquinadopatio_atividade")
    @JoinColumn(name = "id_atividade", nullable = true)    
    public Atividade getAtividade() {        
    	return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    @Transient
    public String getTagPimsBalanca() {
        return getMetaMaquina().getTagPimsBalanca();
    }

    /**
     * As pás-carregadeiras possuem um pátio no qual estão localizadas. Já
     * as outras máquinas de pátio se localizam sobre as correias. Para estas
     * máquinas, o pátio de localização vai depender se o braço da mesma
     * está no pátio inferior ou superior.
     * 
     * @return o pátio de localização da máquina
     */
    public Patio obterPatioDeLocalizacao() {
        Patio patioDeLocalizacao = null;

        /*	
        if (getTipoDaMaquina() == TipoMaquinaEnum.PA_CARREGADEIRA) {
            patioDeLocalizacao = this.patio;
        } else {
            if (bracoNoPatioInferior) {
                patioDeLocalizacao = this.correia.getPatioInferior();
            } else {
                patioDeLocalizacao = this.correia.getPatioSuperior();
            }
        }
         */
        return patioDeLocalizacao;
    }

    /**
     * As pás-carregadeiras possuem um pátio no qual estão localizadas. Já
     * as outras máquinas de pátio se localizam sobre as correias. Para estas
     * máquinas, o pátio de localização vai depender se o braço da mesma
     * está no pátio inferior ou superior.
     * 
     * @return o pátio de localização da máquina
     */
    public Patio obterPatioDeLocalizacaoCorreia() {
        Patio patioDeLocalizacao = null;
        /*
         * if (this.bracoNoPatioInferior != null && !this.bracoNoPatioInferior)
         * { patioDeLocalizacao = this.correia.getPatioSuperior(); } else { if
         * (this.correia.getPatioInferior() != null) { patioDeLocalizacao =
         * this.correia.getPatioInferior(); this.setBracoNoPatioInferior(true);
         * } else if (this.correia.getPatioSuperior() != null) {
         * patioDeLocalizacao = this.correia.getPatioSuperior();
         * this.setBracoNoPatioInferior(false); } }
         */
        // this.setPatio(patioDeLocalizacao);
        return patioDeLocalizacao;
    }

    @Transient
    public Double quantidadePorTempo(Double horasDuracaoAtividade) {
        return this.getTaxaDeOperacaoNominal() * horasDuracaoAtividade;
    }

    @Transient
    public TipoMaquinaEnum getTipoDaMaquina() {
        // TODO Auto-generated method stub
        return getMetaMaquina().getTipoDaMaquina();
    }

    @ManyToOne
    //@Cascade(value = {
       // CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "ID_META_MAQ", nullable = false, insertable = true)
    @ForeignKey(name = "fk_meta_maquina")
    public MetaMaquinaDoPatio getMetaMaquina() {
        return metaMaquina;
    }

    public void setMetaMaquina(MetaMaquinaDoPatio metaMaquina) {
        this.metaMaquina = metaMaquina;
    }

    @Transient
    public Boolean getGiraLanca() {
        return getMetaMaquina().getGiraLanca();
    }

    public void setGiraLanca(Boolean giraLanca) {

    }

    @Column
    public Boolean getBracoNoPatioInferior() {
        return bracoNoPatioInferior;
    }

    public void setBracoNoPatioInferior(Boolean bracoNoPatioInferior) {
        this.bracoNoPatioInferior = bracoNoPatioInferior;
    }

    @Transient
    public Correia getCorreia() {
        // TODO Auto-generated method stub
        if (getMetaMaquina().getMetaCorreia() != null)
            return getMetaMaquina().getMetaCorreia().retornaStatusHorario(this.getDtInicio());
        else
            return null;
    }
    
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LUGAR_EMPILHAMENTO", nullable = true)
    @ForeignKey(name = "fk_maquina_lugar")    
	public LugarEmpilhamentoRecuperacao getLugarEmpilhamento() {
		return lugarEmpilhamento;
	}

	public void setLugarEmpilhamento(LugarEmpilhamentoRecuperacao lugarEmpilhamento) {
		this.lugarEmpilhamento = lugarEmpilhamento;
	}
*/
}
