
package util.planilha;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Implementa filtro para tipo de arquivo desejado na janela do jfileChooser
 * @author Bruno Gomes
 */
public class FiltroPlanilha extends FileFilter{

    private String descricao = "Arquivo excel *.xls";
    
    @Override
    public boolean accept(File file) {
        if(file.isDirectory())
        {//se o arquivo for um diretorio retornar verdadeiro
            return true;
        }
        String extensao = PlanilhaUtil.getExtension(file);
        if(extensao != null){
            // se a extensao for xls retornar true,
            if(extensao.equals(PlanilhaUtil.xls))
            {//...caso desejar outras extensoes basta acresentar mais um argumento neste condicional ...if(...|| extensao.equals(OUTRA_EXTENSAO))
                return true;
            }else
            {// do contrario retorna false
                return false;
            }
        }
        //se nenhuma das condicoes anteriores for verdadeira retorna false
        return false;
    }

    @Override
    public String getDescription() {
        return descricao;
    }

}
