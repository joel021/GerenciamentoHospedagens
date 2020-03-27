/*
 * este é o CRUD de locador
 */
package modelo;

import controlador_view.VCLocadores;
import modelo.execoes.ValoresNegativos;
import java.text.NumberFormat;
import java.util.ArrayList;
import modelo.requisicao.EspacoBanco;
import modelo.requisicao.MensagemBanco;
import modelo.requisicao.RequisicaoEspaco;

/**
 *
 * @author joel
 */

// este é o modelo de Locador, a view é ViewLoacador
public class Locador extends Usuario implements RequisicaoEspaco {
    VCLocadores controlador;
    
    public Locador(){
        
    }
    
    public void adicionarEspaco(VCLocadores controlador, String diaria, String titulo, String lugares, String endereco){
        this.controlador = controlador;
        double d    = 0;
        int n       = 0;
        // tratamento dos dados
        try{
            d = Double.parseDouble(diaria);
            n = Integer.parseInt(lugares);
            if(d < 0 || n < 0)
                throw new ValoresNegativos();
            
        }catch(NumberFormatException | ValoresNegativos er){
            rAdicionarE(false, -1);
            er.printStackTrace();
            return;
        }
        
        NumberFormat f = NumberFormat.getIntegerInstance();
        f.setMaximumIntegerDigits(4);
        n = Integer.parseInt(f.format(n));
            
        Espaco espaco = new Espaco();
        espaco.setTipo(titulo);
        espaco.setnLugares(n);
        espaco.setEndereco(endereco);
        espaco.setLocador(this);
        espaco.setDiaria(d);
        
        EspacoBanco espacoBanco = new EspacoBanco();
        espacoBanco.adicionar(this, espaco);
    }
    
    // resultadoRU da requisição de adicionar espaco
   @Override
    public void rAdicionarE(boolean adicionou, int id){
        
        // queremos pegar a nova lista de espaços adicionados para enviar para o controlador
        EspacoBanco espacoBanco = new EspacoBanco();
        espacoBanco.getEspacosWhere(this);
        
        if(!adicionou)
            controlador.setMensagem("Espaco não pôde ser adicionado! ");
    }
    // pega espacos desse locador no banco de dados
    public void getEspacos(VCLocadores controlador){
        this.controlador = controlador;
        
        EspacoBanco eb = new EspacoBanco();
        eb.getEspacosWhere(this);
    }
    
    // resultado da requisição de buscar espaços
    @Override
    public void rGetEspacosWhere(ArrayList<Espaco> espacos){
        // manda pro controlador
        double total = 0.0;
        // regra para mandar o resultado do lucro
        if(!espacos.isEmpty()){
            for(Espaco e: espacos){
                
                if(!e.getLugares().isEmpty()){
                    for(Lugar l: e.getLugares()){
                        if(l.getStatus().equals(Principal.CHECKIN)){
                            total += e.getDiaria()*Principal.PROPORCAO_EMBOLSO;
                        }
                    }
                }
                
            }
        }
        
        controlador.setLucro(""+total);
        controlador.setEspacosLocador(espacos);
    }

    public void removerEspaco(VCLocadores controlador, int id){
        this.controlador    = controlador;
        new Thread(){
            @Override
            public void run(){
                EspacoBanco e       = new EspacoBanco();
                Espaco espaco       = e.getEspacoWhere(id);
                
                // somente se não haver ninguém nesse espaço
                if (espaco.getLugares().isEmpty()){
                    // não pode remover agora, tem de esparar o cliente sair do imóvel, avisa ao dono do espaco
                    
                    // estou numa thread secundária, posso chamar mensagem diretamente
                    MensagemBanco mb = new MensagemBanco();
                    mb.adicionarMensagem(Locador.this.getId(), "Aviso de AirBn", "Não responda, mas não foi possível remover o espaço, exite alguém nele ainda, aguarde o cliente terminar sua hospedagem");
                    rAtualizarE(false);
                    
                }else{
                    // se haver lugares em aguardo, pode-se avisar aos usuários que não vai haver mais hospedagens
                    ArrayList<Lugar> lugares = espaco.getEmAguardo();
                    if(lugares != null){
                        // nesse caso, temos que avisar ao cliente que não será possível confirmar, esse método não tem nada haver com os Aceitar() e Recusar() de admin
                        // faz-se o trabalho completo aqui mesmo
                        
                        for(Lugar l: lugares){
                            MensagemBanco mb = new MensagemBanco();
                            mb.adicionarMensagem(l.getLocatario().getId(), "Aviso de AirBn", "Não foi possível confirmar seu pedido de hospedagem porque esse espaço está em processo de remoção.");
                        }
                    }
                    
                    // finalmente podemos remover
                    e = new EspacoBanco();
                    e.remover(Locador.this, id); // a partir daí, o remover() faz o resto
                }
            }
        }.start();
    }
    
    //
    public void atualizarEspaco(VCLocadores controlador, int id, String diaria, String titulo, String lugares, String endereco){
        this.controlador    = controlador;
        EspacoBanco eb      = new EspacoBanco();
        
        double d    = 0;
        int n       = 0;
        // tratamento dos dados
        try{
            d = Double.parseDouble(diaria);
            n = Integer.parseInt(lugares);
            if(d < 0 || n < 0)
                throw new ValoresNegativos();
            
        }catch(NumberFormatException | ValoresNegativos er){
            rAtualizarE(false);
            controlador.setMensagem("Não foi possível atualizar o espaco, informações inválidas");
            er.printStackTrace();
            return;
        }
        
        eb.atualizarEspaco(this, id, d, titulo, n, endereco);
    }
    
    @Override
    public void rAtualizarE(boolean atualizou){
        if(atualizou){
            // atualiza
            getEspacos(controlador);
            controlador.setMensagem("Atualizado com sucesso!");
        }else{
            controlador.setMensagem("Não foi possível atualizar o espaco, contate o admin!");
        }
    }
    
}
