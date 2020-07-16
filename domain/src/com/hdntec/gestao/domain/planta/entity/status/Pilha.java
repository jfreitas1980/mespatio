/**
 * Os produtos são armazenados no pátio em pilhas. Cada pilha é identificada e geralmente formada para atender um determinado cliente. O espaço ocupado por cada pilha no pátio é dado por sua baliza inicial e final.
 *
 * @author andre
 */

package com.hdntec.gestao.domain.planta.entity.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizas;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;


@MappedSuperclass
@Entity
public class Pilha extends StatusEntity<Pilha> {

	/** o serial gerado automaticamente */
	private static final long serialVersionUID = -7073546999169259079L;

	/** identificador de pilha */
	private Long idPilha;

	/** nome da pilha */
	private String nomePilha;

	/** o cliente que é dono dessa pilha */
	private Cliente cliente;

	/** a lista de balizas que possuem o mesmo produto */
	private List<Baliza> listaDeBalizas;

	/** o horário de início da formação da baliza */
	private Date horarioInicioFormacao;

	/** o horário de fim de formação da baliza */
	private Date horarioFimFormacao;

	public Pilha() {

	}

	/**
	 * Metodo que verifica se a pilha encontra-se dentro do patio recebido como
	 * parametro de acordo com o patio contido dentro da lista de balizas.
	 * 
	 * @param patio
	 * @return Boolean
	 */
	public Boolean verificarPatioDaPilha(Patio patio) {
		if (!listaDeBalizas.isEmpty()) {
			Baliza balizaPatio = new LinkedList<Baliza>(listaDeBalizas)
					.getFirst();
			return balizaPatio.getPatio().getMetaPatio()
					.equals(patio.getMetaPatio());
		} else {
			return Boolean.FALSE;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pilha_seq")
	@SequenceGenerator(name = "pilha_seq", sequenceName = "seqpilha")
	public Long getIdPilha() {
		return idPilha;
	}

	@Column(nullable = false, length = 60)
	public String getNomePilha() {
		return nomePilha;
	}

	@OneToOne
	@ForeignKey(name = "fk_pilha_cliente")
	@JoinColumn(name = "id_Cliente")
	public Cliente getCliente() {
		return cliente;
	}

	// TODO Darley SA11079 Otimizando colocando LAZY

	@ManyToMany(mappedBy = "pilhas")
	@Cascade(value={CascadeType.DELETE})           
	public List<Baliza> getListaDeBalizas() {
		if (listaDeBalizas != null) {
			Collections.sort(listaDeBalizas, Baliza.comparadorBaliza);
		} else {
			listaDeBalizas = new ArrayList<Baliza>();
		}
		return listaDeBalizas;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setIdPilha(Long idPilha) {
		this.idPilha = idPilha;
	}

	public void setListaDeBalizas(List<Baliza> listaDeBalizas) {
		this.listaDeBalizas = listaDeBalizas;
	}

	public void setNomePilha(String nomePilha) {
		this.nomePilha = nomePilha;
	}

	/**
	 * Metodo que verifica se o tipo de produto passado como parametro eh
	 * compativel com o tipo de produto da pilha
	 * 
	 * @param tipoProduto
	 * @return
	 */
	public boolean verificarTipoProdutoPilha(TipoProduto tipoProduto) {
		Baliza baliza = new LinkedList<Baliza>(this.listaDeBalizas).getFirst();
		if (baliza.getProduto().getTipoProduto().getIdTipoProduto()
				.equals(tipoProduto.getIdTipoProduto())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Metodo que verifica se o tipo de produto passado como parametro eh
	 * compativel com o tipo de produto da pilha
	 * 
	 * @param tipoProduto
	 * @return
	 */
	public boolean verificarTipoProdutoPilha(TipoDeProdutoEnum tipoProduto) {
		Baliza baliza = new LinkedList<Baliza>(this.listaDeBalizas).getFirst();
		if (baliza.getProduto().getTipoProduto().getTipoDeProduto()
				.equals(tipoProduto)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Metodo que verifica se o tipo de produto passado como parametro eh
	 * compativel com o tipo de produto da pilha
	 * 
	 * @param tipoProduto
	 * @return
	 */
	public boolean verificarTipoPilha(EnumTipoBaliza tipoBaliza) {
		Baliza baliza = new LinkedList<Baliza>(this.listaDeBalizas).getFirst();
		if (baliza.getMetaBaliza().getTipoBaliza().equals(tipoBaliza)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Metoto que retorna o total de produtos das balizas da pilha
	 * 
	 * @return
	 */
	public Double obterQuantidadeTotalProdutos() {
		Double totalProdutos = 0D;
		for (Baliza baliza : this.listaDeBalizas) {
			totalProdutos += baliza.getProduto().getQuantidade();
		}
		return totalProdutos;
	}

	/**
	 * Metoto que retorna o total de produtos das balizas da pilha
	 * 
	 * @return
	 */
	public Double obterQuantidadeTotalProdutos(int indexBalizaInicial,
			int indexBalizaFinal) {

		Double totalProdutos = 0D;
		for (int i = indexBalizaInicial; i <= indexBalizaFinal; i++) {
			Baliza baliza = this.listaDeBalizas.get(i);
			totalProdutos += baliza.getProduto().getQuantidade();
		}
		return totalProdutos;
	}

	/**
	 * 
	 * @param indexBalizaInicial
	 * @param indexBalizaFinal
	 * @return
	 */
	public Double obterQuantidadeTotalProdutosPorBaliza(int numBalizaInicial,
			int numBalizaFinal) {

		Double totalProdutos = 0D;
		for (Baliza baliza : this.listaDeBalizas) {
			if (baliza.getNumero() >= numBalizaInicial
					&& baliza.getNumero() <= numBalizaFinal) {
				totalProdutos += baliza.getProduto().getQuantidade();
			}
		}
		return totalProdutos;
	}

	@Override
	public String toString() {
		StringBuffer value = new StringBuffer();

		int numeroBalizaInicial = new LinkedList<Baliza>(this.listaDeBalizas)
				.getFirst().getNumero();
		int numeroBalizaFinal = new LinkedList<Baliza>(this.listaDeBalizas)
				.getLast().getNumero();
		value.append(this.nomePilha).append(" - da baliza ")
				.append(numeroBalizaInicial).append(" até ")
				.append(numeroBalizaFinal);
		return value.toString();
	}

	public void addBaliza(Baliza baliza) {
		Boolean adiciona = Boolean.TRUE;
		if (this.getListaDeBalizas() == null) {
			this.setListaDeBalizas(new ArrayList<Baliza>());
		}

		for (Baliza item : this.getListaDeBalizas()) {
			if (item.getMetaBaliza().equals(baliza.getMetaBaliza())
					|| baliza.getProduto() == null) {
				adiciona = Boolean.FALSE;
			}
		}

		if (adiciona) {
			this.getListaDeBalizas().add(baliza);
			baliza.addPilha(this);
		}
	}

	public void removeBaliza(Baliza baliza) {

		if (this.getListaDeBalizas() == null) {
			return;
		}

		int idx = -1;
		for (int i = 0; i < this.getListaDeBalizas().size(); i++) {
			if (this.getListaDeBalizas().get(i).getMetaBaliza()
					.equals(baliza.getMetaBaliza())) {
				idx = i;
				break;
			}
		}
		if (idx >= 0)
			this.getListaDeBalizas().remove(idx);

	}

	public void removeBaliza(List<Baliza> balizas) {
		for(Baliza b : balizas){
			this.removeBaliza(b);
		}

	
	}
	
	public void addBaliza(List<Baliza> balizas) {
		if (balizas != null) {
			for (Baliza baliza : balizas) {
				addBaliza(baliza);
			}
		}
	}

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getHorarioInicioFormacao() {
		return horarioInicioFormacao;
	}

	public void setHorarioInicioFormacao(Date horarioInicioFormacao) {
		this.horarioInicioFormacao = horarioInicioFormacao;
	}

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getHorarioFimFormacao() {
		return horarioFimFormacao;
	}

	public void setHorarioFimFormacao(Date horarioFimFormacao) {
		this.horarioFimFormacao = horarioFimFormacao;
	}

	/**
	 * criaPilha
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 17/06/2010
	 * @see
	 * @param
	 * @return Pilha
	 */
	public static Pilha criaPilha(String nomePilha, List<Baliza> balizas,
			Cliente cliente, long timeDefault, Pilha pilhaOrigem) {
		Pilha pilha = new Pilha();

		pilha.setNomePilha(nomePilha);
		pilha.setCliente(cliente);
		pilha.setIdUser(1L);
		pilha.setDtInsert(new Date(timeDefault));
		pilha.setDtInicio(new Date(timeDefault));
		if (pilhaOrigem != null) {
			pilha.setHorarioInicioFormacao(pilhaOrigem
					.getHorarioInicioFormacao());
			pilha.setHorarioFimFormacao(pilhaOrigem
					.getHorarioFimFormacao());
		} else {
			pilha.setHorarioInicioFormacao(new Date(timeDefault));
		}
		pilha.addBaliza(balizas);

		return pilha;
	}

	/**
	 * Cria uma ou mais pilhas dado uma lista de balizas. Mais de uma pilha é
	 * criada se a lista de balizas não for continua
	 */
	public static List<Pilha> criaPilhasDescontinuas(List<Baliza> balizas,
			Cliente cliente, Date dataEvento, String nomeFinalPilha,
			String prefixoNomePilha,Pilha pilhaAtual) {
		List<Pilha> listaNovasPilhas = new ArrayList<Pilha>();

		int i = 0;
		Collections.sort(balizas, Baliza.comparadorBaliza);
		// StringBuffer value = new StringBuffer();
		int contadorPilhas = 0;

		ArrayList<Baliza> balizasFormandoNovaPilha = new ArrayList<Baliza>();

		for (Baliza balizaPilha : balizas) {

			if (balizaPilha.getProduto() != null) {
				// pega o tipo de produto da baliza anterior para verificar se
				// eh o mesmo da baliza atual
				Baliza balizaAnterior = (i > 0) ? balizas.get(i - 1)
						: balizaPilha;
				int idx = balizaAnterior.getNumero() - balizaPilha.getNumero();

				// verifica se as duas balizas sao continuas
				if ((balizaAnterior.getMetaBaliza().getMetaPatio()
						.equals(balizaPilha.getMetaBaliza().getMetaPatio()))
						&& balizaAnterior
								.getMetaBaliza()
								.getTipoBaliza()
								.equals(balizaPilha.getMetaBaliza()
										.getTipoBaliza())
						&&  (idx  == 0 || idx == 1|| idx == -1) 
						&& balizaAnterior
								.getProduto()
								.getTipoProduto()
								.equals(balizaPilha.getProduto()
										.getTipoProduto())) {
					balizasFormandoNovaPilha.add(balizaPilha);
				} else {
					// cria um nova pilha e reseta a lista de balizas para a
					// proxima pilha
					if (balizasFormandoNovaPilha.size() > 0) {
						// limpando o objeto pilha da baliza
						int indiceInicioAtualizacao = balizaPilha
								.getMetaBaliza()
								.getIndiceIntervaloCorrespondente(dataEvento);
						int sizeListaStatusBaliza = balizaPilha.getMetaBaliza()
								.getSizeListaStatus();
						for (int j = indiceInicioAtualizacao; j < sizeListaStatusBaliza; j++) {
							Baliza proximoStatusBaliza = balizaPilha
									.getMetaBaliza().getListaStatus().get(j);
							// pilhaEditada.getListaDeBalizas().remove(proximoStatusBaliza);
							proximoStatusBaliza.setPilhas(null);
						}

						contadorPilhas++;
						String nomePilha = nomeFinalPilha;
						if (nomePilha == null)
							nomePilha = prefixoNomePilha
									+ "_"
									+ balizasFormandoNovaPilha.get(0)
											.getMetaBaliza().getMetaPatio()
											.getNomePatio()
									+ "_"
									+ balizasFormandoNovaPilha.get(0)
											.getNumero();
						Pilha novaPilha = Pilha.criaPilha(nomePilha,
								balizasFormandoNovaPilha, cliente,
								dataEvento.getTime(), pilhaAtual);
						listaNovasPilhas.add(novaPilha);
						balizasFormandoNovaPilha = new ArrayList<Baliza>();
						balizasFormandoNovaPilha.add(balizaPilha);
					}
				}
			}
			 i++;
		}
		// verifica se formou apenas uma pilha
		if (balizasFormandoNovaPilha.size() > 0) {
			for (Baliza balizaPilha : balizasFormandoNovaPilha) {
				// limpando o objeto pilha da baliza
				int indiceInicioAtualizacao = balizaPilha.getMetaBaliza()
						.getIndiceIntervaloCorrespondente(dataEvento);
				int sizeListaStatusBaliza = balizaPilha.getMetaBaliza()
						.getSizeListaStatus();
				for (int j = indiceInicioAtualizacao; j < sizeListaStatusBaliza; j++) {
					Baliza proximoStatusBaliza = balizaPilha.getMetaBaliza()
							.getListaStatus().get(j);
					// pilhaEditada.getListaDeBalizas().remove(proximoStatusBaliza);
					// proximoStatusBaliza.setPilhas(null);
					// proximoStatusBaliza.setProduto(null);
					//proximoStatusBaliza.setHorarioInicioFormacao(null);
					//proximoStatusBaliza.setHorarioFimFormacao(null);
				}
			}

			contadorPilhas++;
			String nomePilha = nomeFinalPilha;
			if (nomePilha == null)
				nomePilha = prefixoNomePilha
						+ "_"
						+ balizasFormandoNovaPilha.get(0).getMetaBaliza()
								.getMetaPatio().getNomePatio() + "_"
						+ balizasFormandoNovaPilha.get(0).getNumero();
			Pilha novaPilha = Pilha.criaPilha(nomePilha,
					balizasFormandoNovaPilha, cliente, dataEvento.getTime(),
					pilhaAtual);
			listaNovasPilhas.add(novaPilha);
			balizasFormandoNovaPilha = new ArrayList<Baliza>();
		}

		return listaNovasPilhas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime
				* result
				+ ((horarioFimFormacao == null) ? 0 : horarioFimFormacao
						.hashCode());
		result = prime
				* result
				+ ((horarioInicioFormacao == null) ? 0 : horarioInicioFormacao
						.hashCode());
		result = prime * result + ((idPilha == null) ? 0 : idPilha.hashCode());
		result = prime * result
				+ ((nomePilha == null) ? 0 : nomePilha.hashCode());
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
		Pilha other = (Pilha) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (horarioFimFormacao == null) {
			if (other.horarioFimFormacao != null)
				return false;
		} else if (!horarioFimFormacao.equals(other.horarioFimFormacao))
			return false;
		if (horarioInicioFormacao == null) {
			if (other.horarioInicioFormacao != null)
				return false;
		} else if (!horarioInicioFormacao.equals(other.horarioInicioFormacao))
			return false;
		if (idPilha == null) {
			if (other.idPilha != null)
				return false;
		} else if (!idPilha.equals(other.idPilha))
			return false;
		if (nomePilha == null) {
			if (other.nomePilha != null)
				return false;
		} else if (!nomePilha.equals(other.nomePilha))
			return false;
		return true;
	}

	/**
	 * decompoePilhaEditada -
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 21/07/2010
	 * @see
	 * @param
	 * @return void
	 */
	public static void decompoePilhaEditada(Pilha pilhaEditada,
			Date horarioAtividade) {
		List<Baliza> novaListaBalizas = new ArrayList<Baliza>();

		int i = 0;
		Collections.sort(pilhaEditada.getListaDeBalizas(),
				Baliza.comparadorBaliza);
		StringBuffer value = new StringBuffer();
		int contadorPilhas = 0;

		for (Baliza balizaPilha : pilhaEditada.getListaDeBalizas()) {
			// pega o tipo de produto da baliza anterior para verificar se eh o
			// mesmo da baliza atual
			Baliza balizaAnterior = (i > 0) ? pilhaEditada.getListaDeBalizas()
					.get(i - 1) : balizaPilha;

			// verifica se o produto da baliza e a pilha da baliza possui
			// objetos

			int idx = balizaAnterior.getNumero() - balizaPilha.getNumero();

			if (pilhaEditada != null && balizaPilha.getProduto() != null) {

				if (balizaAnterior.getProduto() == null
						&& balizaPilha.getProduto() != null &&  (idx  == 0 || idx == 1|| idx == -1)) {
					novaListaBalizas.add(balizaPilha);
				} else if (balizaPilha.getProduto().getTipoProduto()
						.equals(balizaAnterior.getProduto().getTipoProduto()) && (idx  == 0 || idx == 1|| idx == -1)) {
					novaListaBalizas.add(balizaPilha);
				} else {
					// cria uma nova pilha qdo encontrado elementos nulos na
					// lista de balizas da pilha
					if (!novaListaBalizas.isEmpty()) {

						contadorPilhas++;
						value = new StringBuffer();
						value.append(pilhaEditada.getNomePilha()).append("_")
								.append(contadorPilhas);
						Collections.sort(novaListaBalizas,
								new ComparadorBalizas());
						pilhaEditada.setDtFim(horarioAtividade);
						Pilha.criaPilha(value.toString(), novaListaBalizas,
								pilhaEditada.getCliente(),
								horarioAtividade.getTime(), pilhaEditada);

						novaListaBalizas = new ArrayList<Baliza>();
						novaListaBalizas.add(balizaPilha);
					}
				}
			} else if (pilhaEditada != null && balizaPilha.getProduto() == null) {
				// limpando o objeto pilha da baliza
				int indiceInicioAtualizacao = balizaPilha.getMetaBaliza()
						.getIndiceIntervaloCorrespondente(horarioAtividade);
				int sizeListaStatusBaliza = balizaPilha.getMetaBaliza()
						.getSizeListaStatus();

				for (int j = indiceInicioAtualizacao; j < sizeListaStatusBaliza; j++) {
					Baliza proximoStatusBaliza = balizaPilha.getMetaBaliza()
							.getListaStatus().get(j);
					// pilhaEditada.getListaDeBalizas().remove(proximoStatusBaliza);
					// proximoStatusBaliza.getPilhas().remove(pilhaEditada);
					// proximoStatusBaliza.setPilhas(null);
					// proximoStatusBaliza.setProduto(null);
					//proximoStatusBaliza.setHorarioInicioFormacao(null);
					//proximoStatusBaliza.setHorarioFimFormacao(null);
				}

				// verifica se a baliza anterior possui produto para a criacao
				// da nova pilha
				if (balizaAnterior.retornaStatusHorario(horarioAtividade) != null
						&& balizaAnterior.getProduto() != null) {
					// cria uma nova pilha qdo encontrado elementos nulos na
					// lista de balizas da pilha
					contadorPilhas++;
					value = new StringBuffer();
					value.append(pilhaEditada.getNomePilha()).append("_")
							.append(contadorPilhas);
					Collections.sort(novaListaBalizas, new ComparadorBalizas());
					pilhaEditada.setDtFim(horarioAtividade);
					Pilha.criaPilha(value.toString(), novaListaBalizas,
							pilhaEditada.getCliente(),
							horarioAtividade.getTime(), pilhaEditada);
					novaListaBalizas = new ArrayList<Baliza>();
				}
			}
			i++;
		}

		if (!novaListaBalizas.isEmpty()) {
			// cria uma nova pilha qdo encontrado elementos nulos na lista de
			// balizas da pilha
			contadorPilhas++;
			value = new StringBuffer();
			value.append(pilhaEditada.getNomePilha()).append("_")
					.append(contadorPilhas);
			Collections.sort(novaListaBalizas, new ComparadorBalizas());
			pilhaEditada.setListaDeBalizas(new ArrayList<Baliza>());
			pilhaEditada.addBaliza(novaListaBalizas);			
		}
	}

}
