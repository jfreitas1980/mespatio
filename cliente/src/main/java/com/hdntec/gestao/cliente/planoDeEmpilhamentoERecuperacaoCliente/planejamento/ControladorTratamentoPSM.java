/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.planejamento;

import com.hdntec.gestao.domain.plano.entity.Atividade;

/**
 *
 * @author guilherme
 */
public class ControladorTratamentoPSM {

    // atividade a qual este controlador pertence.
    private Atividade atividade;

/*    // Itens de controle vazios para novos produtos.
    private List<ItemDeControle> itensDeControleVazios = new ArrayList<ItemDeControle>();

    public ControladorTratamentoPSM(Atividade atividade) {
        this.atividade = atividade;
    }
    
    public SituacaoPatio executar(SituacaoPatio situacaoDePatio, Date inicioExecucao, Date fimExecucao) {
        // Lista de lugares que representam as balizas com o PSM que serah tratado, ...
        List<LugarEmpilhamentoRecuperacao> lugaresTratamento = new ArrayList<LugarEmpilhamentoRecuperacao>();

        // ... o lugar para onde serah movido o Pellet Screening resultante do tratamento ...
        LugarEmpilhamentoRecuperacao lugarDestinoPellet = new LugarEmpilhamentoRecuperacao();

        // ... e o lugar para onde serah movido a pelota resultante.
        LugarEmpilhamentoRecuperacao lugarDestinoPelotas = new LugarEmpilhamentoRecuperacao();

        // ... e o lugar para onde serah movido a pelota resultante.
        LugarEmpilhamentoRecuperacao lugarDestinoLixo = new LugarEmpilhamentoRecuperacao();

        // Total que passou pelo tratamento.
        Double quantidadeTotalTratada = 0.0d;

        // Separa todos os lugares da lista nas devidas lista de onde/para.
        for (LugarEmpilhamentoRecuperacao lugarRecuperacaoEmpilhamento : this.atividade.getListaDeLugaresDeEmpilhamentoRecuperacao()) {
            // Representa o lugar de destino do pellet screening.
            if (lugarRecuperacaoEmpilhamento.getTipoDeProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_PSM)) {
                lugarDestinoPellet = lugarRecuperacaoEmpilhamento;
            // Representa as balizas que tiveram seu PSM tratado.
            } else if (lugarRecuperacaoEmpilhamento.getTipoDeProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PR)) {
                lugaresTratamento.add(lugarRecuperacaoEmpilhamento);
                quantidadeTotalTratada += lugarRecuperacaoEmpilhamento.getQuantidade();
            // Representa o lugar de destino das pelotas.
            } else if (lugarRecuperacaoEmpilhamento.getTipoDeProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA_PSM)) {
                lugarDestinoPelotas = lugarRecuperacaoEmpilhamento;
            } else {
                lugarDestinoLixo = lugarRecuperacaoEmpilhamento;
            }
        }

        // Monta a lista com Itens de Controle vazios para serem usados posteriormente na montagem de novos produtos.
        setItensDeControleVazios(Blendagem.criaListaDeItensDeControleComValoresVazios(lugaresTratamento.get(0).getListaDeBalizas().get(0).getProduto().getQualidade().getListaDeItensDeControle()));

        // Faz a retirada do produto que jah foi tratado ...
        this.alteraQuantidadeBalizasTratadas(lugaresTratamento, situacaoDePatio);

        // ... e distribui ele entre os pellet screening ...
        this.movimentaPelletScreening(lugarDestinoPellet, situacaoDePatio, quantidadeTotalTratada*(lugarDestinoPellet.getTipoDeProduto().getPorcentagemResultadoPSM()));

         ... as pelotas...
         
         this.movimentaPelotas(lugarDestinoPelotas, situacaoDePatio, quantidadeTotalTratada*(lugarDestinoPelotas.getTipoDeProduto().getPorcentagemResultadoPSM()));

          e o lixo.
          
         this.movimentaLixo(lugarDestinoLixo, situacaoDePatio, quantidadeTotalTratada*(lugarDestinoLixo.getTipoDeProduto().getPorcentagemResultadoPSM()));

        return situacaoDePatio;
    }

    *//**
     * Metodo que altera a quantidade das balizas movimentadas e retorna a lista dos
     * produtos que serao movimentados.
     *
     * @param lugarRecuperacao
     * @param situacaoPatio
     * @return listaProdutoMovimentado
     *//*
    private void alteraQuantidadeBalizasTratadas(List<LugarEmpilhamentoRecuperacao> lugarRecuperacao, SituacaoPatio situacaoPatio) {
        CloneHelper clone = new CloneHelper();
        //Recupera o patio onde estao as balizas que serao movimentadas.
        Patio patioMovimentado = lugarRecuperacao.get(0).getListaDeBalizas().get(0).getPatio();

        // Busca patio equivalente na situacao de patio atual.
        Patio patioDaSituacao = this.buscaPatioEquivalente(patioMovimentado, situacaoPatio);

        //Recupera as balizas do patio da situacao de patio atual.
        List<Baliza> balizasDoPatioDaSituacao = patioDaSituacao.getListaDeBalizas();

        // Lista das pilhas que estao sendo alteradas
        List<Pilha> listaPilhasDoPatio = new ArrayList<Pilha>();

        // Itera sobre a lista de balizas do patio da situacao ...
        for (Baliza balizaDoPatio : balizasDoPatioDaSituacao) {

            // ... e verifica se a baliza tera seu produto movimentado
            for (LugarEmpilhamentoRecuperacao lugarEmpilhamentoRecuperacao : lugarRecuperacao) {
                Baliza baliza = lugarEmpilhamentoRecuperacao.getListaDeBalizas().get(0);

                // Se a baliza do patio sofreu alteracao...
                if (balizaDoPatio.getNumero().equals(baliza.getNumero())) {
                    // ... Verifica se a pilha a que ela pertence já estah na lista de pilhas modificadas
                    if (!listaPilhasDoPatio.contains(balizaDoPatio.getPilha())) {
                        listaPilhasDoPatio.add(balizaDoPatio.getPilha());
                    }

                    // Clona o produto que esta na lista e altera o ID fake que ele contem.
                    Produto produtoClonado = new Produto();
                    produtoClonado.geraClone(balizaDoPatio.getProduto(), clone);
                    balizaDoPatio.setProduto(produtoClonado);

                    // Seta a quantidade do produto que sera movida.
                    baliza.getProduto().setQuantidade(lugarEmpilhamentoRecuperacao.getQuantidade());

                    // Subtrai o produto movimentado do total de produto na baliza
                    balizaDoPatio.getProduto().setQuantidade(balizaDoPatio.getProduto().getQuantidade() - lugarEmpilhamentoRecuperacao.getQuantidade());
               }
            }
        }

        // Itera sobre os patios que sofreram alteracao
        for (Pilha pilha : listaPilhasDoPatio) {
            int balizasZeradas = 0;
            // Verifica se a baliza da pilha esta sem quantidade ...
            for (Baliza balizaDaPilha : pilha.getListaDeBalizas()) {
                if (balizaDaPilha.getProduto().getQuantidade() == 0) {
                    balizaDaPilha.setProduto(null);
                    balizasZeradas ++;
                }
            }

            // ... Remonta as pilhas a partir das balizas.
            if (balizasZeradas != 0) {
                atualizaPilhaDaSituacaoPatio(pilha, situacaoPatio);
            }
        }
    }

    *//**
     * Movimenta o Pellet-PSM resultante do tratamento do PSM para sua baliza de destino.
     * 
     * @param lugarDestinoPellet
     * @param situacaoPatio
     * @param qtdTotalPellet
     *//*
    private void movimentaPelletScreening(LugarEmpilhamentoRecuperacao lugarDestinoPellet, SituacaoPatio situacaoPatio, Double qtdTotalPellet) {

        // Recupera a baliza que recebera o produto da movimentacao
        Baliza balizaInicialPellet = lugarDestinoPellet.getListaDeBalizas().get(0);

        // Nome da nova pilha
        String nomeDaPilha = lugarDestinoPellet.getNomeDoLugarEmpRec();

        //Recupera o patio onde estao as balizas que serao movimentadas.
        Patio patioMovimentado = balizaInicialPellet.getPatio();

        // Busca patio equivalente na situacao de patio atual.
        Patio patioDaSituacao = this.buscaPatioEquivalente(patioMovimentado, situacaoPatio);

        //Recupera as balizas do patio da situacao de patio atual.
        List<Baliza> balizasDoPatioDaSituacao = patioDaSituacao.getListaDeBalizas();
        Collections.sort(balizasDoPatioDaSituacao, new ComparadorBalizas());

        Pilha pilhaParaOndeProdutoEhMovimentado = null;

        for (Baliza baliza : balizasDoPatioDaSituacao) {
            if (baliza.getNumero().equals(balizaInicialPellet.getNumero())) {
                // Cria nova pilha para movimentar o produto ...
                if (baliza.getPilha() == null) {
                    pilhaParaOndeProdutoEhMovimentado = new Pilha();
                    pilhaParaOndeProdutoEhMovimentado.setListaDeBalizas(new ArrayList<Baliza>());
                    pilhaParaOndeProdutoEhMovimentado.setNomePilha(nomeDaPilha);
                    situacaoPatio.getListaDePilhasNosPatios().add(pilhaParaOndeProdutoEhMovimentado);
                } else {
                    // ... ou pega a pilha já existente para acrescentar produto.
                    pilhaParaOndeProdutoEhMovimentado = baliza.getPilha();
                }
            }
        }

        // Verifica qual o sentido do empilhamento da movimentacao
        if (lugarDestinoPellet.getSentido().equals(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL)) {

            // Itera sobre todas as balizas do patio em busca da baliza de destino da movimentacao
            for (int i=0;i<balizasDoPatioDaSituacao.size();i++) {
                Baliza baliza=balizasDoPatioDaSituacao.get(i);
                if (baliza.getNumero().equals(balizaInicialPellet.getNumero())) {

                    Double limiteLivreNaBaliza = 0.0d;

                    // Se a baliza que receberá o produto da movimentacao jah possuir produto ...
                    if (baliza.getProduto() != null) {
                        // ... verifica o quanto ela possui de capacidade livre para receber.
                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();

                    } else {
                    	// Se a baliza nao possuir produto, cria um com o tipo de produto do destino
                        Produto p = new Produto();
                        p.setTipoProduto(lugarDestinoPellet.getTipoDeProduto());
                        Qualidade qualidade = new Qualidade();
                        qualidade.setEhReal(Boolean.TRUE);
                        qualidade.setListaDeAmostras(new ArrayList<Amostra>());
                        qualidade.setListaDeItensDeControle(getItensDeControleVazios());
                        p.setQualidade(qualidade);
                        baliza.setProduto(p);
                        baliza.getProduto().setQuantidade(0.0d);

                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();
                    }

                    // Se o limite livre na baliza for maior do que a quantidade a movimentar ...
                    if (limiteLivreNaBaliza > qtdTotalPellet) {
                        // ... entao o limite livre da baliza serah igual ao total a movimentar.
                        limiteLivreNaBaliza = qtdTotalPellet;
                    }

                    // Se o total a movimentar for maior que ZERO ...
                    if (qtdTotalPellet > 0) {
                        // ... associa a baliza a pilha ...
                        if (baliza.getPilha() == null) {
                            baliza.setPilha(pilhaParaOndeProdutoEhMovimentado);
                        }

                        // ... e soma a quantidade que deve ser preenchida nessa baliza.
                        baliza.getProduto().setQuantidade(baliza.getProduto().getQuantidade() + limiteLivreNaBaliza);

                        baliza.setHorarioInicioFormacao(situacaoPatio.getDtInicio());
                        baliza.setHorarioFimFormacao(situacaoPatio.getDtInicio());
                        
                        // Verifica se a pilha já possui a baliza que estah recebendo o produto da movimentacao.
                        if (!baliza.getPilha().getListaDeBalizas().contains(baliza)) {
                            baliza.getPilha().getListaDeBalizas().add(baliza);
                        }

                        // subtrai o que foi movimentado do que falta para movimentar.
                        qtdTotalPellet = qtdTotalPellet - limiteLivreNaBaliza;
                        // Se ainda tiver produto para movimentar, marca a proxima baliza a ser empilhada
                        if (qtdTotalPellet > 0) {
                            balizaInicialPellet = balizasDoPatioDaSituacao.get(i + 1);
                        }
                    } else {
                        break;
                    }
                }
            }
        } else {
            for(int i=balizasDoPatioDaSituacao.size()-1;i>=0;i--) {
                Baliza baliza=balizasDoPatioDaSituacao.get(i);
                if (baliza.getNumero().equals(balizaInicialPellet.getNumero())) {

                    Double limiteLivreNaBaliza = 0.0d;

                    // Se a baliza que receberá o produto da movimentacao jah possuir produto ...
                    if (baliza.getProduto() != null) {
                        // ... verifica o quanto ela possui de capacidade livre para receber.
                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();

                    } else {
                    	// Se a baliza nao possuir produto, cria um com o tipo de produto do destino                    	
                        Produto p = new Produto();
                        p.setTipoProduto(lugarDestinoPellet.getTipoDeProduto());
                        Qualidade qualidade = new Qualidade();
                        qualidade.setEhReal(Boolean.TRUE);
                        qualidade.setListaDeAmostras(new ArrayList<Amostra>());
                        qualidade.setListaDeItensDeControle(getItensDeControleVazios());
                        p.setQualidade(qualidade);
                        baliza.setProduto(p);
                        baliza.getProduto().setQuantidade(0.0d);

                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();
                    }

                    // Se o limite livre na baliza for maior do que a quantidade a movimentar ...
                    if (limiteLivreNaBaliza > qtdTotalPellet) {
                        // ... entao o limite livre da baliza serah igual ao total a movimentar.
                        limiteLivreNaBaliza = qtdTotalPellet;
                    }

                    // Se o total a movimentar for maior que ZERO ...
                    if (qtdTotalPellet > 0) {
                        // ... associa a baliza a pilha ...
                        if (baliza.getPilha() == null) {
                            baliza.setPilha(pilhaParaOndeProdutoEhMovimentado);
                        }

                        // ... e soma a quantidade que deve ser preenchida nessa baliza.
                        baliza.getProduto().setQuantidade(baliza.getProduto().getQuantidade() + limiteLivreNaBaliza);

                        baliza.setHorarioInicioFormacao(situacaoPatio.getDtInicio());
                        baliza.setHorarioFimFormacao(situacaoPatio.getDtInicio());
                        
                        // Verifica se a pilha já possui a baliza que estah recebendo o produto da movimentacao.
                        if (!baliza.getPilha().getListaDeBalizas().contains(baliza)) {
                            baliza.getPilha().getListaDeBalizas().add(baliza);
                        }

                        // subtrai o que foi movimentado do que falta para movimentar.
                        qtdTotalPellet = qtdTotalPellet - limiteLivreNaBaliza;
                        // Se ainda tiver produto para movimentar, marca a proxima baliza a ser empilhada
                        if (qtdTotalPellet > 0) {
                            balizaInicialPellet = balizasDoPatioDaSituacao.get(i - 1);
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    *//**
     * Movimenta a Pelota-PSM resultante do tratamento do PSM para sua baliza de destino.
     * 
     * @param lugarDestinoPelotas
     * @param situacaoPatio
     * @param qtdTotalPelotas
     *//*
    private void movimentaPelotas(LugarEmpilhamentoRecuperacao lugarDestinoPelotas, SituacaoPatio situacaoPatio, Double qtdTotalPelotas) {

        // Recupera a baliza que recebera o produto da movimentacao
        Baliza balizaInicialPellet = lugarDestinoPelotas.getListaDeBalizas().get(0);

        // Nome da nova pilha
        String nomeDaPilha = lugarDestinoPelotas.getNomeDoLugarEmpRec();

        //Recupera o patio onde estao as balizas que serao movimentadas.
        Patio patioMovimentado = balizaInicialPellet.getPatio();

        // Busca patio equivalente na situacao de patio atual.
        Patio patioDaSituacao = this.buscaPatioEquivalente(patioMovimentado, situacaoPatio);

        //Recupera as balizas do patio da situacao de patio atual.
        List<Baliza> balizasDoPatioDaSituacao = patioDaSituacao.getListaDeBalizas();
        Collections.sort(balizasDoPatioDaSituacao, new ComparadorBalizas());

        Pilha pilhaParaOndeProdutoEhMovimentado = null;

        for (Baliza baliza : balizasDoPatioDaSituacao) {
            if (baliza.getNumero().equals(balizaInicialPellet.getNumero())) {
                // Cria nova pilha para movimentar o produto ...
                if (baliza.getPilha() == null) {
                    pilhaParaOndeProdutoEhMovimentado = new Pilha();
                    pilhaParaOndeProdutoEhMovimentado.setListaDeBalizas(new ArrayList<Baliza>());
                    pilhaParaOndeProdutoEhMovimentado.setNomePilha(nomeDaPilha);
                    situacaoPatio.getListaDePilhasNosPatios().add(pilhaParaOndeProdutoEhMovimentado);
                } else {
                    // ... ou clona a pilha da situacao de patio anterior.
                    pilhaParaOndeProdutoEhMovimentado = baliza.getPilha();
                }
            }
        }

        // Verifica qual o sentido do empilhamento da movimentacao
        if (lugarDestinoPelotas.getSentido().equals(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL)) {

            // Itera sobre todas as balizas do patio em busca da baliza de destino da movimentacao
            for (int i=0;i<balizasDoPatioDaSituacao.size();i++) {
                Baliza baliza=balizasDoPatioDaSituacao.get(i);
                if (baliza.getNumero().equals(balizaInicialPellet.getNumero())) {

                    Double limiteLivreNaBaliza = 0.0d;

                    // Se a baliza que receberá o produto da movimentacao jah possuir produto ...
                    if (baliza.getProduto() != null) {
                        // ... verifica o quanto ela possui de capacidade livre para receber.
                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();

                    } else {
                    	// Se a baliza nao possuir produto, cria um com o tipo de produto do destino
                        Produto p = new Produto();
                        p.setTipoProduto(lugarDestinoPelotas.getTipoDeProduto());
                        Qualidade qualidade = new Qualidade();
                        qualidade.setEhReal(Boolean.TRUE);
                        qualidade.setListaDeAmostras(new ArrayList<Amostra>());
                        qualidade.setListaDeItensDeControle(getItensDeControleVazios());
                        p.setQualidade(qualidade);
                        baliza.setProduto(p);
                        baliza.getProduto().setQuantidade(0.0d);

                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();
                    }

                    // Se o limite livre na baliza for maior do que a quantidade a movimentar ...
                    if (limiteLivreNaBaliza > qtdTotalPelotas) {
                        // ... entao o limite livre da baliza serah igual ao total a movimentar.
                        limiteLivreNaBaliza = qtdTotalPelotas;
                    }

                    // Se o total a movimentar for maior que ZERO ...
                    if (qtdTotalPelotas > 0) {
                        // ... associa a baliza a pilha ...
                        if (baliza.getPilha() == null) {
                            baliza.setPilha(pilhaParaOndeProdutoEhMovimentado);
                        }

                        // ... e soma a quantidade que deve ser preenchida nessa baliza.
                        baliza.getProduto().setQuantidade(baliza.getProduto().getQuantidade() + limiteLivreNaBaliza);

                        baliza.setHorarioInicioFormacao(situacaoPatio.getDtInicio());
                        baliza.setHorarioFimFormacao(situacaoPatio.getDtInicio());
                        
                        // Verifica se a pilha já possui a baliza que estah recebendo o produto da movimentacao.
                        if (!baliza.getPilha().getListaDeBalizas().contains(baliza)) {
                            baliza.getPilha().getListaDeBalizas().add(baliza);
                        }

                        // subtrai o que foi movimentado do que falta para movimentar.
                        qtdTotalPelotas = qtdTotalPelotas - limiteLivreNaBaliza;
                        // Se ainda tiver produto para movimentar, marca a proxima baliza a ser empilhada
                        if (qtdTotalPelotas > 0) {
                            balizaInicialPellet = balizasDoPatioDaSituacao.get(i + 1);
                        }
                    } else {
                        break;
                    }
                }
            }
        } else {
            for(int i=balizasDoPatioDaSituacao.size()-1;i>=0;i--) {
                Baliza baliza=balizasDoPatioDaSituacao.get(i);
                if (baliza.getNumero().equals(balizaInicialPellet.getNumero())) {

                    Double limiteLivreNaBaliza = 0.0d;

                    // Se a baliza que receberá o produto da movimentacao jah possuir produto ...
                    if (baliza.getProduto() != null) {
                        // ... verifica o quanto ela possui de capacidade livre para receber.
                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();

                    } else {
                    	// Se a baliza nao possuir produto, cria um com o tipo de produto do destino
                        Produto p = new Produto();
                        p.setTipoProduto(lugarDestinoPelotas.getTipoDeProduto());
                        Qualidade qualidade = new Qualidade();
                        qualidade.setEhReal(Boolean.TRUE);
                        qualidade.setListaDeAmostras(new ArrayList<Amostra>());
                        qualidade.setListaDeItensDeControle(getItensDeControleVazios());
                        p.setQualidade(qualidade);
                        baliza.setProduto(p);
                        baliza.getProduto().setQuantidade(0.0d);

                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();
                    }

                    // Se o limite livre na baliza for maior do que a quantidade a movimentar ...
                    if (limiteLivreNaBaliza > qtdTotalPelotas) {
                        // ... entao o limite livre da baliza serah igual ao total a movimentar.
                        limiteLivreNaBaliza = qtdTotalPelotas;
                    }

                    // Se o total a movimentar for maior que ZERO ...
                    if (qtdTotalPelotas > 0) {
                        // ... associa a baliza a pilha ...
                        if (baliza.getPilha() == null) {
                            baliza.setPilha(pilhaParaOndeProdutoEhMovimentado);
                        }

                        // ... e soma a quantidade que deve ser preenchida nessa baliza.
                        baliza.getProduto().setQuantidade(baliza.getProduto().getQuantidade() + limiteLivreNaBaliza);

                        baliza.setHorarioInicioFormacao(situacaoPatio.getDtInicio());
                        baliza.setHorarioFimFormacao(situacaoPatio.getDtInicio());
                        
                        // Verifica se a pilha já possui a baliza que estah recebendo o produto da movimentacao.
                        if (!baliza.getPilha().getListaDeBalizas().contains(baliza)) {
                            baliza.getPilha().getListaDeBalizas().add(baliza);
                        }

                        // subtrai o que foi movimentado do que falta para movimentar.
                        qtdTotalPelotas = qtdTotalPelotas - limiteLivreNaBaliza;
                        // Se ainda tiver produto para movimentar, marca a proxima baliza a ser empilhada
                        if (qtdTotalPelotas > 0) {
                            balizaInicialPellet = balizasDoPatioDaSituacao.get(i - 1);
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void movimentaLixo(LugarEmpilhamentoRecuperacao lugarDestinoLixo, SituacaoPatio situacaoPatio, Double qtdTotalLixo) {

        // Recupera a baliza que recebera o produto da movimentacao
        Baliza balizaInicialLixo = lugarDestinoLixo.getListaDeBalizas().get(0);

        // Nome da nova pilha
        String nomeDaPilha = lugarDestinoLixo.getNomeDoLugarEmpRec();

        //Recupera o patio onde estao as balizas que serao movimentadas.
        Patio patioMovimentado = balizaInicialLixo.getPatio();

        // Busca patio equivalente na situacao de patio atual.
        Patio patioDaSituacao = this.buscaPatioEquivalente(patioMovimentado, situacaoPatio);

        //Recupera as balizas do patio da situacao de patio atual.
        List<Baliza> balizasDoPatioDaSituacao = patioDaSituacao.getListaDeBalizas();
        Collections.sort(balizasDoPatioDaSituacao, new ComparadorBalizas());

        Pilha pilhaParaOndeProdutoEhMovimentado = null;

        for (Baliza baliza : balizasDoPatioDaSituacao) {
            if (baliza.getNumero().equals(balizaInicialLixo.getNumero())) {
                // Cria nova pilha para movimentar o produto ...
                if (baliza.getPilha() == null) {
                    pilhaParaOndeProdutoEhMovimentado = new Pilha();
                    pilhaParaOndeProdutoEhMovimentado.setListaDeBalizas(new ArrayList<Baliza>());
                    pilhaParaOndeProdutoEhMovimentado.setNomePilha(nomeDaPilha);
                    situacaoPatio.getListaDePilhasNosPatios().add(pilhaParaOndeProdutoEhMovimentado);
                } else {
                    // ... ou clona a pilha da situacao de patio anterior.
                    pilhaParaOndeProdutoEhMovimentado = baliza.getPilha();
                }
            }
        }

        // Verifica qual o sentido do empilhamento da movimentacao
        if (lugarDestinoLixo.getSentido().equals(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL)) {

            // Itera sobre todas as balizas do patio em busca da baliza de destino da movimentacao
            for (int i=0;i<balizasDoPatioDaSituacao.size();i++) {
                Baliza baliza=balizasDoPatioDaSituacao.get(i);
                if (baliza.getNumero().equals(balizaInicialLixo.getNumero())) {

                    Double limiteLivreNaBaliza = 0.0d;

                    // Se a baliza que receberá o produto da movimentacao jah possuir produto ...
                    if (baliza.getProduto() != null) {
                        // ... verifica o quanto ela possui de capacidade livre para receber.
                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();

                    } else {
                    	// Se a baliza nao possuir produto, cria um com o tipo de produto do destino
                        Produto p = new Produto();
                        p.setTipoProduto(lugarDestinoLixo.getTipoDeProduto());
                        Qualidade qualidade = new Qualidade();
                        qualidade.setEhReal(Boolean.TRUE);
                        qualidade.setListaDeAmostras(new ArrayList<Amostra>());
                        qualidade.setListaDeItensDeControle(getItensDeControleVazios());
                        p.setQualidade(qualidade);
                        baliza.setProduto(p);
                        baliza.getProduto().setQuantidade(0.0d);

                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();
                    }

                    // Se o limite livre na baliza for maior do que a quantidade a movimentar ...
                    if (limiteLivreNaBaliza > qtdTotalLixo) {
                        // ... entao o limite livre da baliza serah igual ao total a movimentar.
                        limiteLivreNaBaliza = qtdTotalLixo;
                    }

                    // Se o total a movimentar for maior que ZERO ...
                    if (qtdTotalLixo > 0) {
                        // ... associa a baliza a pilha ...
                        if (baliza.getPilha() == null) {
                            baliza.setPilha(pilhaParaOndeProdutoEhMovimentado);
                        }

                        // ... e soma a quantidade que deve ser preenchida nessa baliza.
                        baliza.getProduto().setQuantidade(baliza.getProduto().getQuantidade() + limiteLivreNaBaliza);

                        baliza.setHorarioInicioFormacao(situacaoPatio.getDtInicio());
                        baliza.setHorarioFimFormacao(situacaoPatio.getDtInicio());
                        
                        // Verifica se a pilha já possui a baliza que estah recebendo o produto da movimentacao.
                        if (!baliza.getPilha().getListaDeBalizas().contains(baliza)) {
                            baliza.getPilha().getListaDeBalizas().add(baliza);
                        }

                        // subtrai o que foi movimentado do que falta para movimentar.
                        qtdTotalLixo = qtdTotalLixo - limiteLivreNaBaliza;
                        // Se ainda tiver produto para movimentar, marca a proxima baliza a ser empilhada
                        if (qtdTotalLixo > 0) {
                            balizaInicialLixo = balizasDoPatioDaSituacao.get(i + 1);
                        }
                    } else {
                        break;
                    }
                }
            }
        } else {
            for(int i=balizasDoPatioDaSituacao.size()-1;i>=0;i--) {
                Baliza baliza=balizasDoPatioDaSituacao.get(i);
                if (baliza.getNumero().equals(balizaInicialLixo.getNumero())) {

                    Double limiteLivreNaBaliza = 0.0d;

                    // Se a baliza que receberá o produto da movimentacao jah possuir produto ...
                    if (baliza.getProduto() != null) {
                        // ... verifica o quanto ela possui de capacidade livre para receber.
                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();

                    } else {
                    	// Se a baliza nao possuir produto, cria um com o tipo de produto do destino
                        Produto p = new Produto();
                        p.setTipoProduto(lugarDestinoLixo.getTipoDeProduto());
                        Qualidade qualidade = new Qualidade();
                        qualidade.setEhReal(Boolean.TRUE);
                        qualidade.setListaDeAmostras(new ArrayList<Amostra>());
                        qualidade.setListaDeItensDeControle(getItensDeControleVazios());
                        p.setQualidade(qualidade);
                        baliza.setProduto(p);
                        baliza.getProduto().setQuantidade(0.0d);

                        limiteLivreNaBaliza = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();
                    }

                    // Se o limite livre na baliza for maior do que a quantidade a movimentar ...
                    if (limiteLivreNaBaliza > qtdTotalLixo) {
                        // ... entao o limite livre da baliza serah igual ao total a movimentar.
                        limiteLivreNaBaliza = qtdTotalLixo;
                    }

                    // Se o total a movimentar for maior que ZERO ...
                    if (qtdTotalLixo > 0) {
                        // ... associa a baliza a pilha ...
                        if (baliza.getPilha() == null) {
                            baliza.setPilha(pilhaParaOndeProdutoEhMovimentado);
                        }

                        // ... e soma a quantidade que deve ser preenchida nessa baliza.
                        baliza.getProduto().setQuantidade(baliza.getProduto().getQuantidade() + limiteLivreNaBaliza);

                        baliza.setHorarioInicioFormacao(situacaoPatio.getDtInicio());
                        baliza.setHorarioFimFormacao(situacaoPatio.getDtInicio());
                        
                        // Verifica se a pilha já possui a baliza que estah recebendo o produto da movimentacao.
                        if (!baliza.getPilha().getListaDeBalizas().contains(baliza)) {
                            baliza.getPilha().getListaDeBalizas().add(baliza);
                        }

                        // subtrai o que foi movimentado do que falta para movimentar.
                        qtdTotalLixo = qtdTotalLixo - limiteLivreNaBaliza;
                        // Se ainda tiver produto para movimentar, marca a proxima baliza a ser empilhada
                        if (qtdTotalLixo > 0) {
                            balizaInicialLixo = balizasDoPatioDaSituacao.get(i - 1);
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    *//**
     * Retorna o patio equivalente na situacao de patio clonada.
     *
     * @param patioAtual
     * @param situacaoPatio
     * @return patioDaSituacao
     *//*
    private Patio buscaPatioEquivalente(Patio patioAtual, SituacaoPatio situacaoPatio) {
        // Busca o patio que serah alterado na situacao de patio
        Patio patioDaSituacao = new Patio();
        for (Patio patio : situacaoPatio.getPlanta().getListaPatios()) {
            if (patio.getNumero().equals(patioAtual.getNumero())) {
                patioDaSituacao = patio;
            }
        }

        return patioDaSituacao;
    }

    *//**
     * Atualiza a pilha de onde foi recolhido produto.
     * @param listaBalizasRecuperacao
     *//*
    private void atualizaPilhaDaSituacaoPatio(Pilha pilhaUtilizada, SituacaoPatio situacaoPatio) {
        // Pilha atual existente na situacao de patio.
        Pilha pilhaAtual = pilhaUtilizada;
        List<Baliza> listaBalizas = pilhaAtual.getListaDeBalizas();
        Collections.sort(listaBalizas, new ComparadorBalizas());
        int indexPilhaNova = 1;

        // lista de balizas auxiliar para criar novas pilhas.
        List<Baliza> balizasPilhaNova = new ArrayList<Baliza>();
        int posicaoBaliza = -1;
        StringBuffer value = new StringBuffer();
        for (int i = 0; i < listaBalizas.size(); i++) {
            Baliza baliza = listaBalizas.get(i);
            if (baliza.getProduto() == null && !balizasPilhaNova.isEmpty()) {
                baliza.setPilha(null);
                value = new StringBuffer();
                value.append(pilhaAtual.getNomePilha()).append("_").append(indexPilhaNova);
                criaNovaPilha(balizasPilhaNova, pilhaAtual, value.toString(), situacaoPatio);
                indexPilhaNova++;
                balizasPilhaNova = new ArrayList<Baliza>();
                posicaoBaliza = baliza.getNumero();

            } else {
                if (posicaoBaliza == -1) {
                    posicaoBaliza = baliza.getNumero();
                    baliza.setPilha(null);
                    if (baliza.getProduto() != null) {
                        balizasPilhaNova.add(baliza);
                    }
                } else {
                    if (baliza.getNumero() == posicaoBaliza) {
                        baliza.setPilha(null);
                        if (baliza.getProduto() != null) {
                            balizasPilhaNova.add(baliza);
                        }
                    } else {
                    	value = new StringBuffer();
                        value.append(pilhaAtual.getNomePilha()).append("_").append(indexPilhaNova);
                        criaNovaPilha(balizasPilhaNova, pilhaAtual, value.toString(), situacaoPatio);
                        indexPilhaNova++;
                        balizasPilhaNova = new ArrayList<Baliza>();
                        if (baliza.getProduto() != null) {
                            balizasPilhaNova.add(baliza);
                        }
                        baliza.setPilha(null);
                        posicaoBaliza = baliza.getNumero();
                    }
                }
            }

            posicaoBaliza++;
        }
        if (!balizasPilhaNova.isEmpty()) {
        	value = new StringBuffer();
            value.append(pilhaAtual.getNomePilha()).append("_").append(indexPilhaNova);
        	criaNovaPilha(balizasPilhaNova, pilhaAtual, value.toString(), situacaoPatio);
        }
        Cliente cliente = pilhaAtual.getCliente();
        if(cliente!=null)
           cliente.getListaDePilhas().remove(pilhaAtual);
        situacaoPatio.getListaDePilhasNosPatios().remove(pilhaAtual);
        value = null;
    }

    private void criaNovaPilha(List<Baliza> listaDeBalizas, Pilha pilhaAtual, String nomePilha, SituacaoPatio situacaoPatio) {
        Pilha pilha = new Pilha();
        pilha.setCliente(pilhaAtual.getCliente());
        pilha.setNomePilha(nomePilha);
        pilha.setListaDeBalizas(listaDeBalizas);

        for (Baliza balizaPilhaNova : listaDeBalizas) {
            balizaPilhaNova.setPilha(pilha);
        }

        Cliente cliente = pilhaAtual.getCliente();
        if(cliente!=null)
           cliente.getListaDePilhas().add(pilha);

        situacaoPatio.getListaDePilhasNosPatios().add(pilha);
    }

    *//**
     * Retorna a lista de Itens de Controle clonados e vazios para criacao de um
     * novo produto.
     * 
     * @return the itensDeControleVazios
     *//*
    public List<ItemDeControle> getItensDeControleVazios() {
        List<ItemDeControle> itensDecontroleClonados = new ArrayList<ItemDeControle>();
        CloneHelper clone = new CloneHelper();
        for (ItemDeControle itemDeControle : itensDeControleVazios) {
        	ItemDeControle itemDeControleClonado = new ItemDeControleQualidade();
        	//itemDeControleClonado.geraClone(itemDeControle, clone);
            itensDecontroleClonados.add(itemDeControleClonado);
        }
        return itensDecontroleClonados;
    }

    *//**
     * @param itensDeControleVazios the itensDeControleVazios to set
     *//*
    public void setItensDeControleVazios(List<ItemDeControle> itensDeControleVazios) {
        this.itensDeControleVazios = itensDeControleVazios;
    }
*/
}
