package util.planilha;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.hdntec.gestao.exceptions.LeituraPlanilhaException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;

import jxl.Workbook;
import jxl.read.biff.BiffException;
/**
 *
 * @author Bruno Gomes
 */
public class CflexStockyardLeitorPlanilha {

    private Workbook wb;
    /** lista de parametros para as mensagens de erros */
    private List<String> listaParametros;
    // jfilechosser abre janela para escolher o arquivo excel que deseja que seja lido
    private JFileChooser fileChose = new JFileChooser();
    //arquivo excel de onde se deseja ler
    File planilha;

    /**
     * Constroi um leitor de planilha excel e abre janela para selecionar a planilha que se deseja ler
     * @throws jxl.read.biff.BiffException
     * @throws java.io.IOException
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.OperacaoCanceladaPeloUsuarioException
     */
    public CflexStockyardLeitorPlanilha() throws BiffException, IOException, OperacaoCanceladaPeloUsuarioException{
        int valorRetorno = fileChose.showOpenDialog(null);
        if(valorRetorno == JFileChooser.APPROVE_OPTION){
             planilha = fileChose.getSelectedFile();
             this.wb = Workbook.getWorkbook(planilha);
        }else{
            throw new OperacaoCanceladaPeloUsuarioException();
        }
    }

    /**
     * Constroi um leitor de planilha excel e abre janela para selecionar a planilha que se deseja ler
     * @param fileFilter - filtro da extensao dos arquivos que irao aparecer como opcao selecionavel
     * @throws java.io.IOException
     * @throws jxl.read.biff.BiffException
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.OperacaoCanceladaPeloUsuarioException
     */
    public CflexStockyardLeitorPlanilha(FileFilter fileFilter ) throws IOException, BiffException, OperacaoCanceladaPeloUsuarioException
    {//adiciona o filtro para a janela de opcao
        fileChose.setFileFilter(fileFilter);

        int valorRetorno = fileChose.showOpenDialog(null);
        if (valorRetorno == JFileChooser.APPROVE_OPTION) {
            planilha = fileChose.getSelectedFile();
            this.wb = Workbook.getWorkbook(planilha);
        } else {
            throw new OperacaoCanceladaPeloUsuarioException();
        }
    }

    /**
     * Constroi um leitor de planilha excel
     * @param pathName - Caminho completo da planilha de importacao
     * @throws java.io.IOException
     * @throws jxl.read.biff.BiffException
     */
    public CflexStockyardLeitorPlanilha(String pathName) throws IOException, BiffException{
        if(pathName!=null)
           this.planilha = new File(pathName);
        else
           throw new IOException("Especifique um caminho para a planilha de amostras de embarque");
        this.wb = Workbook.getWorkbook(planilha);

    }

    /**
     * retorna o caminho completo da planilha utilizada para leitura
     * @return
     */
    public String getFileCaminhoCompleto(){
        return planilha.getAbsolutePath();
    }

    /**
     * Retorna o número de colunas da planilha
     * @param sheetIndex - indice da folha da planilha
     * @return
     */
    public int getNumeroDeLinhas(int sheetIndex){
        return wb.getSheet(sheetIndex).getRows();
    }
    /**
	 * Retorna o número de colunas da planilha
	 * @param sheetIndex - indíce da planilha
	 * @return
	 */
	public int getNumeroDeColunas(int sheetIndex){
		return wb.getSheet(sheetIndex).getColumns();
	}
    /**
	 * Verifica se determinada célula é vazia
	 * @param linha - número da linha na tabela excel
	 * @param coluna - número da coluna na tabela excel
	 * @param ordemPagina - indice da planilha (folha / sheet)<br>
     * <b>Importante:</b> A primeira Linha e Coluna da planilha tem indice 0(Zero).
	 * @return
	 * <br>
	 * <b>Importante:</b> A numeração das linhas e colunas iniciam em 0.
	 */
	public boolean isCelulaVazia(int linha, int coluna, int ordemPagina) {
		int numeroDeLinhas = wb.getSheet(ordemPagina).getRows();
		int numeroDeColunas = wb.getSheet(ordemPagina).getColumns();
		if (linha < numeroDeLinhas && coluna < numeroDeColunas){
			return (wb.getSheet(ordemPagina).getCell(
					PlanilhaUtil.processaLinhasEColunas(linha, coluna)).getContents()
					.equalsIgnoreCase(""));
		}else{
			return true;
		}
	}
    /**
     * Verifica se determinada célula é vazia
     * @param linha - numero da linha da celula avaliada
     * @param coluna - numero da coluna da celula avaliada
     * @param nomePagina - nome da planilha (folha / sheet<br>
     * <b>Importante:</b> A primeira Linha e Coluna da planilha tem indice 0(Zero).
     * @return
     */
    public boolean isCelulaVazia(int linha, int coluna, String nomePagina) {
		int numeroDeLinhas = wb.getSheet(nomePagina).getRows();
		int numeroDeColunas = wb.getSheet(nomePagina).getColumns();
		if (linha < numeroDeLinhas && coluna < numeroDeColunas){
			return (wb.getSheet(nomePagina).getCell(
					PlanilhaUtil.processaLinhasEColunas(linha, coluna)).getContents()
					.equalsIgnoreCase(""));
		}else{
			return true;
		}
	}

     /**
    * Faz leitura de uma celula com número double do woorbook definido para este leitor dado linha,coluna, e ordem da página na planilha
    * @param linha - número da linha na tabela excel
	* @param coluna - número da coluna na tabela excel
	* @param ordemPagina - indice da planilha (folha / sheet)<br>
    * <b>Importante:</b> A primeira Linha e Coluna da planilha tem indice 0(Zero).
    * @return Valor da célula
    * @throws LeituraPlanilhaException - caso a célula não contenha nenhum valor.
    * @throws Exception
    * <br>
    * <b>Importante:</b> A numeração das linhas e colunas iniciam em 0.
    */
   public Double leCelulaDouble(int linha, int coluna, int ordemPagina)throws LeituraPlanilhaException, Exception {
      Double dadoCelula = null;
      listaParametros = new ArrayList<String>();
      if (isCelulaVazia(linha, coluna, ordemPagina)){
          listaParametros.add(PlanilhaUtil.processaLinhasEColunas(linha, coluna) );
          throw new LeituraPlanilhaException("exception.celula.planilha.vazia" , listaParametros);
      }else{
         try{
            dadoCelula = Double.parseDouble(wb.getSheet(ordemPagina).getCell(PlanilhaUtil.processaLinhasEColunas(linha, coluna)).getContents().trim());
         }catch(NumberFormatException nfe){
             listaParametros.add(PlanilhaUtil.processaLinhasEColunas(linha, coluna) );
            throw new LeituraPlanilhaException("exception.valor.celula.planilha.invalido", listaParametros);
         }
         return dadoCelula;
      }
   }

   /**
    * Faz leitura de uma celula do woorbook definido para este leitor dado linha,coluna, e ordem da página na planilha
    * @param linha - número da linha na tabela excel
	* @param coluna - número da coluna na tabela excel
	* @param ordemPagina - indice da planilha (folha / sheet) <br>
    * <b>Importante:</b> A primeira Linha e Coluna da planilha tem indice 0(Zero).
    * @return
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
    * @throws java.lang.Exception
    */
   public String leCelula(int linha, int coluna, int ordemPagina)throws LeituraPlanilhaException, Exception {
       listaParametros = new ArrayList<String>();
       if (isCelulaVazia(linha, coluna, ordemPagina)){
            listaParametros.add(PlanilhaUtil.processaLinhasEColunas(linha, coluna) );
			throw new LeituraPlanilhaException("exception.celula.planilha.vazia" , listaParametros);
		}else{
			return (wb.getSheet(ordemPagina).getCell(PlanilhaUtil.processaLinhasEColunas(linha, coluna)).getContents().trim());
		}
	}

    /**
    * Faz leitura de uma celula do woorbook definido para este leitor dado linha,coluna, e nome da página na planilha
    * @param linha - número da linha na tabela excel
    * @param coluna - número da coluna na tabela excel
	* @param ordemPagina - nome da planilha (folha / sheet)<br>
     * <b>Importante:</b> A primeira Linha e Coluna da planilha tem indice 0(Zero).
    * @return
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
    * @throws java.lang.Exception
    */
   public String leCelula(int linha, int coluna, String nomePagina)throws LeituraPlanilhaException, Exception {
       listaParametros = new ArrayList<String>();
       if (isCelulaVazia(linha, coluna, nomePagina)){
            listaParametros.add(PlanilhaUtil.processaLinhasEColunas(linha, coluna) );
			throw new LeituraPlanilhaException("exception.celula.planilha.vazia" , listaParametros);
		}else{
			return (wb.getSheet(nomePagina).getCell(PlanilhaUtil.processaLinhasEColunas(linha, coluna)).getContents().trim());
		}
	}

   /**
    * retorna o numero de sheets (abas) que a planilha
    * @return
    */
   public int numeroDeAbas(){
       return wb.getNumberOfSheets();
   }

   public String[] getSheetNames(){
       return this.wb.getSheetNames() ;
   }

}
