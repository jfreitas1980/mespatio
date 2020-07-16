package com.hdntec.gestao.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.richfaces.component.UIDataTable;

import com.hdntec.gestao.domain.plano.dao.SituacaoPatioDAO;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.dao.PlantaDAO;
import com.hdntec.gestao.domain.planta.entity.MetaPier;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;
import com.hdntec.gestao.web.util.SessionUtil;


public class PlantaController {
    /**Atributos da Planta*/
    private UIDataTable dataTablePlanta;
   // private SituacaoPatio situacaoPatio;
    private Planta plantaPesquisa = new Planta();
   // private SituacaoPatioDAO situacaoDAO = new SituacaoPatioDAO();
    private PlantaDAO plantaDAO = new PlantaDAO();
    private List<Planta> plantaList = new ArrayList<Planta>();
    //private List<SituacaoPatio> situacaoPatioList = new ArrayList<SituacaoPatio>();
    //private PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao;
    private static boolean isResearch = false;

    private Object objParaExcluir;
    
    /**Atributos do Pier*/
    private UIDataTable dataTablePier;
    private MetaPier pier;
    
    /**Atributos do Patio*/
    private UIDataTable dataTablePatio;
    private Patio patio;
    
    /**Atributos da Correia*/
    private UIDataTable dataTableCorreia;
    private Correia correia;
    
    /**Atributos da Usina*/
    private UIDataTable dataTableUsina;
    private Usina usina;
    
    
    /********************************
     * Metodos da Planta
     ********************************/
    private void initEntity() {
        isResearch = false;
       // situacaoPatio = new SituacaoPatio();
        plantaPesquisa = new Planta();
    }

    public String buscaPlantaPorFiltro() {
        isResearch = true;
        return null;
    }
    
    public String limpaFiltro() {
        initEntity();
        return null;
    }
    
    public void selectItem() {
        plantaPesquisa = (Planta)dataTablePlanta.getRowData();
    }
    
    public void selectPier() {
        objParaExcluir = dataTablePier.getRowData();
    }
    
    public void selectPatio() {
        objParaExcluir = dataTablePatio.getRowData();
    }
    
    public void selectCorreia() {
        objParaExcluir = dataTableCorreia.getRowData();
    }
    
    public void selectUsina() {
        objParaExcluir = dataTableUsina.getRowData();
    }    
    
    public String excluiObjeto() {
        if (objParaExcluir instanceof Pier) {
            removePier();
        }
        else if (objParaExcluir instanceof Patio) {
            removePatio();
        }
        else if (objParaExcluir instanceof Correia) {
            removeCorreia();
        }
        else if (objParaExcluir instanceof Usina) {
            removeUsina();
        }
        return null;
    }
    
    public String alteraPlanta() {
        plantaPesquisa = (Planta)dataTablePlanta.getRowData();
       // situacaoPatio = new SituacaoPatio();
       // situacaoPatio.setPlanta(plantaPesquisa);
        //situacaoPatio = situacaoDAO.buscarListaDeObjetos(situacaoPatio);
        return "atualizarPlanta";
    }
    
    public String novaPlanta() {
        initEntity();
        //planoEmpilhamentoRecuperacao = novoPlanoEmpilhamentoRecuperacao();
        //situacaoPatio = novaSituacaoPatio();
        return "novaPlanta";
    }
    
    public String gravaPlanta() {
        //situacaoPatio.limpaID();
        //planoEmpilhamentoRecuperacao.addSituacaoPatio(situacaoPatio);
        //situacaoPatio.setPlanoEmpilhamento(planoEmpilhamentoRecuperacao);
        try {
          /*  planoEmpilhamentoRecuperacao = 
                planoService.salvarPlanoDeEmpilhamentoERecuperacao(
                        planoEmpilhamentoRecuperacao);*/            
            SessionUtil.addSucessMessage("sucess");
            return "cancelarPlanta";
        } catch (Exception e) {
            e.printStackTrace();
            SessionUtil.addErrorMessage("error");
        }
        return null;
    }
    
    public String excluiPlanta() {        
        try {
     //       PlanoEmpilhamentoRecuperacao plano = recuperarPlanoEmpilhamento(situacaoPatio);
            //planoService.removerPlanoDeEmpilhamentoERecuperacao(plano);
            SessionUtil.addSucessMessage("sucess");
        } catch (Exception e) {
            e.printStackTrace();
            SessionUtil.addErrorMessage("error");
        } finally {
            initEntity();
        }
        return null;
    }

    /********************************
     * Metodos do Pier
     ********************************/
    public String novoPier() {
        pier = new MetaPier();
        //pierClone = new Pier();
        Object obj = SessionUtil.getSessionMapValue("pierController");
        if (obj instanceof PierController) { 
            PierController pierController = (PierController)obj;
            pierController.setPier(pier);
        }
        return "atualizarPier";
    }
    
    public String alteraPier() {
        pier = (MetaPier)dataTablePier.getRowData();
        Object obj = SessionUtil.getSessionMapValue("pierController");
        if (obj instanceof PierController) { 
            PierController pierController = (PierController)obj;
            pierController.setPier(pier);
            pierController.setPierSelecionado(null);
            pierController.setPierAnterior(null);            
        }
        return "atualizarPier";
    }

    public String removePier() {
        Pier pierExclusao = (Pier)objParaExcluir;
        //this.situacaoPatio.getPlanta().getListaPiers().remove(pierExclusao);
        return null;
    }

    /********************************
     * Metodos do Patio
     ********************************/
    public String novoPatio() {
        patio = new Patio();
        //patioClone = new Patio();
        Object obj = SessionUtil.getSessionMapValue("patioController");
        if (obj instanceof PatioController) { 
            PatioController patioController = (PatioController)obj;
            patioController.setPatio(patio);
        }
        return "atualizarPatio";
    }
    
    public String alteraPatio() {
        patio = (Patio)dataTablePatio.getRowData();
        //clonarPatio();
        Object obj = SessionUtil.getSessionMapValue("patioController");
        if (obj instanceof PatioController) { 
            PatioController patioController = (PatioController)obj;
            patioController.setPatio(patio);
        }
        return "atualizarPatio";
    }

    public String removePatio() {
        Patio patioExclusao = (Patio)objParaExcluir;
        if (permiteExclusaoPatio(patioExclusao)) {
            //this.situacaoPatio.getPlanta().getListaPatios().remove(patioExclusao);
            SessionUtil.addSucessMessage("sucess");
        }
        else {
            SessionUtil.addErrorMessage("patioReferenciado");
        }
        return null;
    }
    
    
    private boolean permiteExclusaoPatio(Patio patioExclusao) {
        boolean result = true;
        if (patioExclusao instanceof Patio) {
            //verificar na lista de correias da planta se existe o patio
            /*for(Correia correia : this.situacaoPatio.getPlanta().getListaCorreias()) {
                if ( patioExclusao.equals(correia.getPatioInferior()) ||
                        patioExclusao.equals(correia.getPatioSuperior()) ) {
                    result = false;
                    break;
                }
            }
            if ( result ) {
                //verificar na lista de usinas se existe o patio
                for (Usina usina : this.situacaoPatio.getPlanta().getListaUsinas()) {
                    if (patioExclusao.equals(usina.getPatioExpurgoPellet())) {
                        result = false;
                        break;
                    }
                }
            }
    */    }
        return result; 
    }
    

    /********************************
     * Metodos da Correia
     ********************************/
    public String novaCorreia() {
        correia = new Correia();

        Object obj = SessionUtil.getSessionMapValue("correiaController");
        if (obj instanceof CorreiaController) { 
            CorreiaController correiaController = (CorreiaController)obj;
            correiaController.setCorreia(correia);
        }
        return "atualizarCorreia";
    }
    
    public String alteraCorreia() {
        correia = (Correia)dataTableCorreia.getRowData();

        Object obj = SessionUtil.getSessionMapValue("correiaController");
        if (obj instanceof CorreiaController) { 
            CorreiaController correiaController = (CorreiaController)obj;
            correiaController.setCorreia(correia);
        }
        return "atualizarCorreia";
    }

    public String removeCorreia() {
        Correia correiaExclusao = (Correia)objParaExcluir;
        //this.situacaoPatio.getPlanta().getListaCorreias().remove(correiaExclusao);
        return null;
    }
    


    /********************************
     * Metodos da Usina
     ********************************/
    public String novaUsina() {
        usina = new Usina();

        Object obj = SessionUtil.getSessionMapValue("usinaController");
        if (obj instanceof UsinaController) { 
            UsinaController usinaController = (UsinaController)obj;
            usinaController.setUsina(usina);
        }
        return "atualizarUsina";
    }
    
    public String alteraUsina() {
        usina = (Usina)dataTableUsina.getRowData();
        try {
            Object obj = SessionUtil.getSessionMapValue("usinaController");
            if (obj instanceof UsinaController) { 
                UsinaController usinaController = (UsinaController)obj;
                usinaController.setUsina(usina);
            }       
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "atualizarUsina";
    }

    public String removeUsina() {
        Usina usinaExclusao = (Usina)objParaExcluir;
        //this.situacaoPatio.getPlanta().getListaUsinas().remove(usinaExclusao);
        return null;
    }
    
    /**********************************
     * Setters e Getters
     **********************************/
    
    public UIDataTable getDataTablePlanta() {
        return dataTablePlanta;
    }

    public void setDataTablePlanta(UIDataTable dataTablePlanta) {
        this.dataTablePlanta = dataTablePlanta;
    }

    public static boolean isResearch() {
        return isResearch;
    }

    public static void setResearch(boolean isResearch) {
        PlantaController.isResearch = isResearch;
    }
    
    public Planta getPlantaPesquisa() {
        return plantaPesquisa;
    }

    public void setPlantaPesquisa(Planta plantaPesquisa) {
        this.plantaPesquisa = plantaPesquisa;
    }

   

    public List<Planta> listarPlantas() {
        try {
            plantaList = plantaDAO.buscaPorExemploPlanta(new Planta());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return plantaList;
    }

    public UIDataTable getDataTablePier() {
        return dataTablePier;
    }

    public void setDataTablePier(UIDataTable dataTablePier) {
        this.dataTablePier = dataTablePier;
    }

    public UIDataTable getDataTablePatio() {
        return dataTablePatio;
    }

    public void setDataTablePatio(UIDataTable dataTablePatio) {
        this.dataTablePatio = dataTablePatio;
    }

    public MetaPier getPier() {
        return pier;
    }

    public void setPier(MetaPier pier) {
        this.pier = pier;
    }

    public Patio getPatio() {
        return patio;
    }

    public void setPatio(Patio patio) {
        this.patio = patio;
    }

    public UIDataTable getDataTableCorreia() {
        return dataTableCorreia;
    }

    public void setDataTableCorreia(UIDataTable dataTableCorreia) {
        this.dataTableCorreia = dataTableCorreia;
    }

    public Correia getCorreia() {
        return correia;
    }

    public void setCorreia(Correia correia) {
        this.correia = correia;
    }
    
    public UIDataTable getDataTableUsina() {
        return dataTableUsina;
    }

    public void setDataTableUsina(UIDataTable dataTableUsina) {
        this.dataTableUsina = dataTableUsina;
    }

    public Usina getUsina() {
        return usina;
    }

    public void setUsina(Usina usina) {
        this.usina = usina;
    }

    private PlanoEmpilhamentoRecuperacao novoPlanoEmpilhamentoRecuperacao() {
        PlanoEmpilhamentoRecuperacao plano = new PlanoEmpilhamentoRecuperacao();
        //plano.setDataInicio(new Date());
        plano.setEhOficial(Boolean.FALSE);
        //plano.setUltimaModificacao(new Date());
        plano.setHorizonteDePlanejamento(DSSStockyardTimeUtil.obterValorDeIntervalos(new Long(3), null, null, new Long(0)));
        return plano;
    }


    private List<Planta> filtrarPlantas(String nomePlanta) {
        List<Planta> resultado = null;
        Planta plantaPesquisa = new Planta();
        plantaPesquisa.setNomePlanta(nomePlanta);
        resultado = plantaDAO.buscarListaDeObjetos(plantaPesquisa);        
        return resultado;
    }

    public List<Planta> getPlantaList() {
        if (isResearch) {
            plantaList = filtrarPlantas(plantaPesquisa.getNomePlanta());
        } else {
            plantaList = listarPlantas();
        }        
        return plantaList;
    }

    public void setPlantaList(List<Planta> plantaList) {
        this.plantaList = plantaList;
    }
   
}
