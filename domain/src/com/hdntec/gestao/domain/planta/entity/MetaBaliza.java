
package com.hdntec.gestao.domain.planta.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.AbstractMetaEntity;
import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Interdicao;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.Amostra;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.vo.atividades.AtividadeRecuperarEmpilharVO;
import com.hdntec.gestao.domain.vo.atividades.EmpilhamentoEmergenciaVO;
import com.hdntec.gestao.domain.vo.atividades.MovimentacaoVO;


/**
 * É o espaço físico que foi padronizado como medida mínima de um pátio para a operação de empilhamento e recuperação.
 * <p>
 * Persite. - Tamanho: - Volume: mais de um centena de registros. - Período de Persistência: cadastro de planta. - Freq. Update: algumas vezes por dia. -Confiabilidade: deve ser confiável.
 * 
 * @author andre
 * 
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
                "ID_PATIO", "numero", "nomeBaliza"}))
public class MetaBaliza extends AbstractMetaEntity<Baliza> {

    /** Serialização do objeto */
    private static final long serialVersionUID = -1449375616083250262L;

    /** identificador de baliza */
    private Long idBaliza;

    /** Nome da Baliza */
    private String nomeBaliza;

    /** a largura da baliza, em metros */
    private Double largura;

    /** o número da marcação da baliza no pátio */
    private Integer numero;

    /** a capacidade máxima da baliza, em toneladas */
    private Double capacidadeMaxima;

    /**
     * tipo da baliza podendo ser do tipo normal que aceita pelota e pallet,
     * do tipo especial que se localiza no inicio dos patios, ou do tipo de emergencia
     * para sobras da usina.
     */
    private EnumTipoBaliza tipoBaliza;

    /** o pátio ao qual esta baliza pertence */
    private MetaPatio metaPatio;

    /** Lista com as manutencoes agendadas */
    private List<Interdicao> listaInterdicao;

    
    public MetaBaliza() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "meta_bal_seq")
    @SequenceGenerator(name = "meta_bal_seq", sequenceName = "seq_meta_bal")
    public Long getIdBaliza() {
        return idBaliza;
    }

    @Column(nullable = false, length = 60)
    public String getNomeBaliza() {
        return nomeBaliza;
    }

    @Column(nullable = false)
    public Double getLargura() {
        return largura;
    }

    @Column(nullable = false)
    public Integer getNumero() {
        return numero;
    }

    @Column(nullable = false)
    public Double getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    @ManyToOne
    @JoinColumn(name = "ID_PATIO", nullable = false, insertable = true)
    @ForeignKey(name = "fk_meta_patio")
    public MetaPatio getMetaPatio() {
        return metaPatio;
    }

    public void setCapacidadeMaxima(Double capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public void setIdBaliza(Long idBaliza) {
        this.idBaliza = idBaliza;
    }

    public void setLargura(Double largura) {
        this.largura = largura;
    }

    public void setNomeBaliza(String nomeBaliza) {
        this.nomeBaliza = nomeBaliza;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setMetaPatio(MetaPatio patio) {
        this.metaPatio = patio;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public EnumTipoBaliza getTipoBaliza() {
        return tipoBaliza;
    }

    public void setTipoBaliza(EnumTipoBaliza tipoBaliza) {
        this.tipoBaliza = tipoBaliza;
    }

    @Override
    public String toString() {
        return nomeBaliza;
    }

    @Override
    @OneToMany(fetch = FetchType.LAZY,mappedBy="metaBaliza")
    @Fetch(FetchMode.SELECT)
    //@Cascade(value={CascadeType.ALL,CascadeType.SAVE_UPDATE})
    //@OrderBy("dtInicio asc")
    public List<Baliza> getListaStatus() {
        return super.getListaStatus();
    }

    /*
    public static void main(String args[])
    {      
       try {
       Baliza pilha1 = new Baliza();
       pilha1.setNomeBaliza("teste1");
       
       Baliza pilha2 = new Baliza();
       pilha2.setNomeBaliza("teste2");
       
       Baliza pilha3 = new Baliza();
       pilha3.setNomeBaliza("teste3");
       
       
       Date date1 = new Date();
       date1.setYear(2010);
       date1.setMonth(1);
       date1.setHours(10);
       date1.setMinutes(30);
       
       Date date2 = new Date();
       date2.setYear(2010);
       date2.setMonth(1);
       date2.setHours(11);
       date2.setMinutes(30);
       
       Date date3 = new Date();
       date3.setYear(2010);
       date3.setMonth(1);
       date3.setHours(13);
       date3.setMinutes(30);
       
       MetaBaliza mp = new MetaBaliza();
       mp.incluirNovoStatus(pilha1, date1);
       mp.incluirNovoStatus(pilha3, date3);
       mp.incluirNovoStatus(pilha2, date2);      
       
       Date date4 = new Date();
       date4.setYear(2010);
       date4.setMonth(1);
       date4.setHours(9);
       date4.setMinutes(30);
       
       Date date5 = new Date();
       date5.setYear(2010);
       date5.setMonth(1);
       date5.setHours(12);
       date5.setMinutes(30);
       
       Date date6 = new Date();
       date6.setYear(2010);
       date6.setMonth(1);
       date6.setHours(14);
       date6.setMinutes(30);
         
       Baliza pilha4 = mp.retornaStatusHorario(date1);
       pilha4 = mp.retornaStatusHorario(date5);
       pilha4 = mp.retornaStatusHorario(date2);
       
       mp.removeStatusAPartirDeHorario(date3);
       } catch(Exception e) {
     	  e.printStackTrace();
       }
       
    } 
    */

    @Override
    public void incluirNovoStatus(Baliza novoStatus, Date horaStatus) {
        super.incluirNovoStatus(novoStatus, horaStatus);
        novoStatus.setMetaBaliza(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((capacidadeMaxima == null) ? 0 : capacidadeMaxima.hashCode());
        result = prime * result + ((idBaliza == null) ? 0 : idBaliza.hashCode());
        result = prime * result + ((largura == null) ? 0 : largura.hashCode());
        result = prime * result + ((metaPatio == null) ? 0 : metaPatio.hashCode());
        result = prime * result + ((nomeBaliza == null) ? 0 : nomeBaliza.hashCode());
        result = prime * result + ((numero == null) ? 0 : numero.hashCode());
        result = prime * result + ((tipoBaliza == null) ? 0 : tipoBaliza.hashCode());
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
        MetaBaliza other = (MetaBaliza) obj;
        if (capacidadeMaxima == null) {
            if (other.capacidadeMaxima != null)
                return false;
        } else if (!capacidadeMaxima.equals(other.capacidadeMaxima))
            return false;
        if (idBaliza == null) {
            if (other.idBaliza != null)
                return false;
        } else if (!idBaliza.equals(other.idBaliza))
            return false;
        if (largura == null) {
            if (other.largura != null)
                return false;
        } else if (!largura.equals(other.largura))
            return false;
        if (metaPatio == null) {
            if (other.metaPatio != null)
                return false;
        } else if (!metaPatio.equals(other.metaPatio))
            return false;
        if (nomeBaliza == null) {
            if (other.nomeBaliza != null)
                return false;
        } else if (!nomeBaliza.equals(other.nomeBaliza))
            return false;
        if (numero == null) {
            if (other.numero != null)
                return false;
        } else if (!numero.equals(other.numero))
            return false;
        if (tipoBaliza == null) {
            if (other.tipoBaliza != null)
                return false;
        } else if (!tipoBaliza.equals(other.tipoBaliza))
            return false;
        return true;
    }

    @Override
    public Baliza clonarStatus(Date horario) {
        Baliza result = null;
        result = super.clonarStatus(horario);
        result.setIdBaliza(null);        
        result.setPilhas(null);
        if (result.getProduto() != null)
        {
        	Produto p = result.getProduto().copiarStatus();
        	result.setProduto(p);
        }
        return result;
    }
    
    /**
     * gerarBalizas
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 17/06/2010
     * @see
     * @param 
     * @return List<Baliza>
     */
    private static List<Baliza> gerarAtividadeBalizas(AtividadeRecuperarEmpilharVO recuperacaoVO,List<MetaBaliza> metaBalizas) {
        List<Baliza> balizasDestino = new ArrayList<Baliza>();
        for (MetaBaliza metaBaliza : metaBalizas) {
        	Baliza novaBaliza = null;
        	if (recuperacaoVO.getDataFim() != null ) {
        		novaBaliza = metaBaliza.clonarStatus(recuperacaoVO.getDataFim());
        		 novaBaliza.setEstado(EstadoMaquinaEnum.OCIOSA);
                 novaBaliza.setHorarioFimFormacao(recuperacaoVO.getDataFim());
        	} else {
            	novaBaliza = metaBaliza.clonarStatus(recuperacaoVO.getDataInicio());
            	novaBaliza.setEstado(EstadoMaquinaEnum.OPERACAO);
            }
            
            // seta o inicio de formação da baliza
            if (novaBaliza.getHorarioInicioFormacao() == null) {
                novaBaliza.setHorarioInicioFormacao(recuperacaoVO.getDataInicio());    
            }
            Produto produtoEmpilhado = null;
            /*if (novaBaliza.getProduto() != null) { 
            	produtoEmpilhado= novaBaliza.getProduto().copiarStatus();             	            	
            	//produtoEmpilhado.getQualidade().setDtInsert(new Date(System.currentTimeMillis()));
            	//Produto produtoEmpilhado = Produto.criaProduto(recuperacaoVO.getTipoProduto(), recuperacaoVO.getDataInicio().getTime());            	            
            } else {*/
              if (novaBaliza.getProduto() == null) {	
                produtoEmpilhado = Produto.criaProduto(recuperacaoVO.getTipoProduto(), recuperacaoVO.getDataInicio().getTime());
                Qualidade novaQualidade = new Qualidade();
                novaQualidade.setListaDeAmostras(new ArrayList<Amostra>());
                if (recuperacaoVO.getCampanhas() != null && recuperacaoVO.getCampanhas().size() > 0) {
                    novaQualidade.setListaDeItensDeControle(Blendagem.criaListaDeItensDeControleComValoresVazios(recuperacaoVO.getCampanhas().get(0).getQualidadeEstimada().getListaDeItensDeControle()));              
                }
                
                
                for (ItemDeControle ic : novaQualidade.getListaDeItensDeControle())
                {
                	ic.setDtInicio(recuperacaoVO.getDataInicio());
                	ic.setDtFim(recuperacaoVO.getDataFim());
                	ic.setDtInsert(new Date(System.currentTimeMillis()));
                }
                novaQualidade.setEhReal(false);
                novaQualidade.setDtInicio(recuperacaoVO.getDataInicio());
                novaQualidade.setDtFim(recuperacaoVO.getDataFim());
                novaQualidade.setDtInsert(new Date(System.currentTimeMillis()));
                novaQualidade.setIdUser(1L);
                produtoEmpilhado.setQualidade(novaQualidade);
                novaQualidade.setProduto(produtoEmpilhado);
                novaBaliza.setProduto(produtoEmpilhado);
              }            
            balizasDestino.add(novaBaliza);
        }                 
       return balizasDestino;
    }

    public static List<Baliza> gerarAtividadeRecuperarBalizas(AtividadeRecuperarEmpilharVO recuperacaoVO) {
        
        List<Baliza> balizasDestino = MetaBaliza.gerarAtividadeBalizas(recuperacaoVO,recuperacaoVO.getListaBalizas());        
        
        
        return balizasDestino;
    }

    public static List<Baliza> gerarAtividadeEmpilharBalizas(AtividadeRecuperarEmpilharVO recuperacaoVO) {        
        List<Baliza> balizasDestino = MetaBaliza.gerarAtividadeBalizas(recuperacaoVO,recuperacaoVO.getListaBalizas());
        for (Baliza novaBaliza : balizasDestino) {            
        	novaBaliza.getProduto().setTipoProduto(recuperacaoVO.getTipoProduto());        	        
        }
        return balizasDestino;
    }

    public static List<Baliza> gerarAtividadeMovimentacaoRecuperacaoBalizas(MovimentacaoVO recuperacaoVO) {
        
        List<Baliza> balizasDestino = MetaBaliza.gerarAtividadeBalizas(recuperacaoVO,recuperacaoVO.getListaBalizas());        
        
        
        return balizasDestino;
    }
    
    public static List<Baliza> gerarAtividadeMovimentacaoEmpilhamentoBalizas(MovimentacaoVO recuperacaoVO) {
        
        List<Baliza> balizasDestino = MetaBaliza.gerarAtividadeBalizas(recuperacaoVO,recuperacaoVO.getListaBalizasDestino());        
        
        
        return balizasDestino;
    }
    
    public static HashMap<MetaPatio, List<Baliza>> gerarAtividadeEmpilhamentoEmergencia(EmpilhamentoEmergenciaVO empilhamentoEmergenciaVO) 
    {
    	HashMap<MetaPatio, List<Baliza>> mapaNovasBalizas = new HashMap<MetaPatio, List<Baliza>>();
    	Set<MetaPatio> listaPatios = empilhamentoEmergenciaVO.getMapaBalizasPorPatio().keySet();
        for (MetaPatio metaPatio : listaPatios)
        {
        	List<MetaBaliza> listaBalizasPatio = empilhamentoEmergenciaVO.getMapaBalizasPorPatio().get(metaPatio);
        	List<Baliza> balizasDestino = MetaBaliza.gerarAtividadeBalizas(empilhamentoEmergenciaVO,listaBalizasPatio);        
        	mapaNovasBalizas.put(metaPatio, balizasDestino);
        }
        
        return mapaNovasBalizas;
    }

    public void setListaInterdicao(List<Interdicao> listaInterdicao) {
        this.listaInterdicao = listaInterdicao;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {
                    javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST})
    @Fetch(FetchMode.SELECT)
    @Cascade(value = {
                    CascadeType.SAVE_UPDATE,CascadeType.DELETE,CascadeType.DELETE_ORPHAN})
    @ForeignKey(name = "fk_baliza_interdicao", inverseName = "fk_interdicao_baliza")
    @JoinTable(name = "Baliza_Interdicao", joinColumns = @JoinColumn(name = "idBaliza"), inverseJoinColumns = @JoinColumn(name = "idInterdicao"))
    public List<Interdicao> getListaInterdicao() {
        return listaInterdicao;
    }

    /**
     * addInterdicao
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 08/12/2010
     * @see
     * @param 
     * @return void
     */
    public void addInterdicao(Interdicao interdicao) {
        if (getListaInterdicao() == null) {
            setListaInterdicao(new ArrayList<Interdicao>());
        }

        if (!getListaInterdicao().contains(interdicao)) {
            getListaInterdicao().add(interdicao);
     
        }
    }

    /**
     * addInterdicao
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 08/12/2010
     * @see
     * @param 
     * @return void
     */
    public void addInterdicao(List<Interdicao> interdicoes) {
        if (interdicoes != null) {
            for (Interdicao interdicao : interdicoes) {
                addInterdicao(interdicao);
            }
        }
    }
    
    @Transient
    public Boolean balizaInterditado(Date horaExecucao)
    {       
       Boolean result = Boolean.FALSE;
       Collections.sort(getListaInterdicao(),Interdicao.comparadorInterdicao);
       List<Interdicao> campanhas =  getListaInterdicao();
       
       for (Interdicao inter : campanhas ) {           
           if (inter.getDataFinal().getTime() >= horaExecucao.getTime() &&
                           inter.getDataInicial().getTime() <= horaExecucao.getTime()) {                                      
               result =  Boolean.TRUE;
               break;
           }
       }       
       return result;
    }
    
    @Transient
    public Boolean balizaInterditado(List<Interdicao> lstInterdicao,Date horaExecucao)
    {       
       Boolean result = Boolean.FALSE;
       Collections.sort(lstInterdicao,Interdicao.comparadorInterdicao);
       for (Interdicao inter : lstInterdicao ) {           
           if (inter.getDataFinal().getTime() >= horaExecucao.getTime() &&
                           inter.getDataInicial().getTime() <= horaExecucao.getTime()) {                                      
               result =  Boolean.TRUE;
               break;
           }
       }       
       return result;
    }
    
}
