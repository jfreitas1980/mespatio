package com.hdntec.gestao.services.controladores;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;


public interface IControladorExecutarPerdaPellet
{
   public void empilharPerdaPellet(Atividade atividade,Date horaEmpilhamento, List<AtividadeCampanha> atividadesCampanhas, 
            HashMap<AtividadeCampanha, Double> mapaPerdaPelletFeedPorCampanha);
}
