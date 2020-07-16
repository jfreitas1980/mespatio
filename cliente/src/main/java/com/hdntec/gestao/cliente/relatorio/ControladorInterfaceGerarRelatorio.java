package com.hdntec.gestao.cliente.relatorio;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;

import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.domain.relatorio.comparadores.ComparadorInterfaceRelatorio;
import com.hdntec.gestao.domain.relatorio.enums.EspecieRelatorioEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * 
 * <P><B>Description :</B><BR>
 * General ControladorInterfaceGerarRelatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 26/06/2009
 * @version $Revision: 1.1 $
 */
public class ControladorInterfaceGerarRelatorio
{

   private ControladorInterfaceGestaoRelatorio controladorInterfaceGestao;

   private List<PadraoRelatorio> padraoRelatorioLista;

   /**
    *
    * @param controladorInterfaceGestao
    */
   public ControladorInterfaceGerarRelatorio(ControladorInterfaceGestaoRelatorio controlador) throws ErroSistemicoException
   {
	controladorInterfaceGestao = controlador;
	popularListaPadraoRelatorio();
   }

   /**
    *
    * popularListaPadraoRelatorio
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 24/06/2009
    * @see
    * @throws ErroSistemicoException
    * @return Returns the void.
    */
   public void popularListaPadraoRelatorio() throws ErroSistemicoException
   {
	   padraoRelatorioLista = controladorInterfaceGestao.getControladorGestaoRelatorio().buscarPadroesRelatorio();

   }

   /**
    * Get padraoRelatorioLista
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 24/06/2009
    */
   public List<PadraoRelatorio> getPadraoRelatorioLista()
   {
	if (padraoRelatorioLista == null)
	{
	   padraoRelatorioLista = new ArrayList<PadraoRelatorio>();
	}
	return padraoRelatorioLista;
   }

   /**
    *
    * popularComboBoxModelo
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 24/06/2009
    * @see
    * @param combo
    * @return Returns the void.
    */
   public void popularComboBoxModelo(JComboBox combo)
   {
	combo.removeAllItems();
	Collections.sort(getPadraoRelatorioLista(), new ComparadorInterfaceRelatorio());
	for (PadraoRelatorio padrao : getPadraoRelatorioLista())
	{
	   combo.addItem(padrao);
	}
   }

   public void gerarRelatorio(Relatorio relatorio, 
		   EspecieRelatorioEnum especieRelatorio) throws IOException, ErroSistemicoException
   {
	try
	{
	   relatorio.setNomeRelatorio(geraNomeRelatorio(especieRelatorio));
	   controladorInterfaceGestao.gerarRelatorio(relatorio,especieRelatorio);
	   
	   if (EspecieRelatorioEnum.RELATORIO_DSP.equals(especieRelatorio)) {
		   controladorInterfaceGestao.getControladorGestaoRelatorio().salvarRelatorio(relatorio);
		   controladorInterfaceGestao.abrirRelatorio(relatorio);
		   controladorInterfaceGestao.popularListaRelatorios();
	   }
	   else {
		   controladorInterfaceGestao.abrirRelatorio(relatorio);
	   }
	}
	catch (Exception e)
	{
		if (e instanceof ErroSistemicoException)
			throw (ErroSistemicoException)e;
		else
			throw new ErroSistemicoException(e);
	}
   }
   

   private String geraNomeRelatorio(EspecieRelatorioEnum especieRelatorio)
   {
	   String nome = "rel";
	   if (EspecieRelatorioEnum.INDICADOR_QUALIDADE.equals(especieRelatorio)) {
		   nome = PropertiesUtil.getMessage("relatorio.configurar.padrao.file.name.tatico");
	   }
	   else if (EspecieRelatorioEnum.RELATORIO_DSP.equals(especieRelatorio)) {
		   nome = PropertiesUtil.getMessage("relatorio.configurar.padrao.file.name");
	   }
	   else if (EspecieRelatorioEnum.DESLOCAMENTO_MAQUINA.equals(especieRelatorio)) {
		   nome = PropertiesUtil.getMessage("relatorio.configurar.padrao.file.name.relOperacao");
	   }
	   else if (EspecieRelatorioEnum.PLANO_RECUPERACAO.equals(especieRelatorio)) {
		   nome = PropertiesUtil.getMessage("relatorio.configurar.padrao.file.name.planoRecuperacao");
	   }
	   else if (EspecieRelatorioEnum.ATENDIMENTO_CARGA.equals(especieRelatorio)) {
		   nome = PropertiesUtil.getMessage("relatorio.configurar.padrao.file.name.atendimentoCarga");
	   }
	   String dataHora = "";
	   SimpleDateFormat df = new SimpleDateFormat(PropertiesUtil.getMessage("relatorio.configurar.padrao.file.name.dateformat"));
	   dataHora = df.format(new Date(System.currentTimeMillis()));
	   nome += dataHora;
	   return nome;
   }
}
