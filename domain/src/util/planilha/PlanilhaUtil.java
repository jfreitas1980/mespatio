package util.planilha;

import java.io.File;

/**
 *
 * @author Bruno Gomes
 */
public class PlanilhaUtil {

    private static final String[] LETRAS = { "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };

    public static final int COLUNA_A = 0;
    public static final int COLUNA_B = 1;
    public static final int COLUNA_C = 2;
    public static final int COLUNA_D = 3;
    public static final int COLUNA_E = 4;
    public static final int COLUNA_F = 5;
    public static final int COLUNA_G = 6;
    public static final int COLUNA_H = 7;
    public static final int COLUNA_I = 8;
    public static final int COLUNA_J = 9;
    public static final int COLUNA_K = 10;
    public static final int COLUNA_L = 11;
    public static final int COLUNA_M = 12;
    public static final int COLUNA_N = 13;
    public static final int COLUNA_O = 14;
    public static final int COLUNA_P = 15;
    public static final int COLUNA_Q = 16;
    public static final int COLUNA_R = 17;
    public static final int COLUNA_S = 18;
    public static final int COLUNA_T = 19;
    public static final int COLUNA_U = 20;
    public static final int COLUNA_V = 21;
    public static final int COLUNA_W = 22;
    public static final int COLUNA_X = 23;
    public static final int COLUNA_Y = 24;
    public static final int COLUNA_Z = 25;

    //tipo de arquivos (extensão)
    public static final String xls = "xls";

    /**
     * Retorna a extensão de um arquivo
     * @param file - O arquivo que se deseja verificar a extensao
     * @return - A extensao do arquivo
     */
    public static String getExtension(File file){
        String ext = null;
        String fileName = file.getName();
        int i = fileName.lastIndexOf("."); //verifica o indice onde começa a extensao do arquivo pelo ponto (.)
        //verifica se o indice é valido sendo maior que Zero e menor que o tamanho da String do nome do arquivo
        if(i > 0 && i < fileName.length() - 1)
        { //.. em caso positivo retorna a extensão do arquivo
            ext = fileName.substring(i+1).toLowerCase();
        }
        
        return ext;
    }

	/**
	 * Retorna em formato XY (X - letra definindo coluna,Y- numero definindo linha) dado a linha e coluna em formato numerico
	 * @param linha
	 * @param coluna
	 * @return
	 */
	public static String processaLinhasEColunas(int linha, int coluna) {
		String caracteresColuna = "";
		int resto;
		while (coluna >= 0) {
			resto = coluna % 26;
			caracteresColuna = LETRAS[resto] + caracteresColuna;
			coluna = (coluna / 26) - 1;
		}
//		System.out.println("Retorno de processa linhas -->" + caracteresColuna + (linha + 1) );
		return caracteresColuna + (linha + 1);
	}

}
