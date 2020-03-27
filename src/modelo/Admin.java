/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;
import controlador_view.VCAdmin;
import controlador_view.VCLocadores;
import java.util.ArrayList;
import java.util.TreeMap;
import modelo.requisicao.EspacoBanco;
import modelo.requisicao.LugarBanco;
import modelo.requisicao.RequisicaoLugar;

/**
 *
 * @author aluno
 */
// uma vez admin, sempre admin, admin não tem filho, ninguém mexe no que ele pode mexer.
public final class Admin extends Locador implements RequisicaoLugar {
    
    public void getEspacos(VCAdmin controlador){
        this.controlador = controlador;
        
        EspacoBanco eb = new EspacoBanco();
        eb.getEspacosWhere(this);
    }
    
    public void aceitarReserva(VCLocadores controlador, int id_lugar, int id_locatario){
        this.controlador = controlador;
        new Thread(){
            @Override
            public void run(){
                
                // setar no banco de lugares
                LugarBanco lB = new LugarBanco();
                lB.atualizarLugar(id_lugar, id_locatario, Principal.RESERVADO);
                                
                // mas quer-se avisar a ele que foi confirmada
                lB = new LugarBanco();
                Lugar lugar = lB.getLugarWhere(id_lugar);
                
                // pode acontecer de o admin conseguir chamar a função checkout ou do usuário conseguir clicar duas vezes consecutivas muito rápido. resulta em NullPointerException se isso acontecer
                if(lugar.getLocatario() != null)
                    enviarMensagem(controlador, lugar.getLocatario().getId(), "Pedido de reserva", "Seu pedido de reserva foi confirmado.");
                
                // atualiza
                getAgendamentos(controlador);
            }
        }.start();
        
        
    }
    
    public void recusarReserva(VCLocadores controlador, int id_lugar){
        this.controlador = controlador;
        new Thread(){
            @Override
            public void run(){
                // setar no banco de lugares
                LugarBanco lB = new LugarBanco();
                lB.atualizarLugar(id_lugar, 0, Principal.LIVRE);
                                
                // mas quer-se avisar a ele que não foi confirmada
                lB          = new LugarBanco();
                Lugar lugar = lB.getLugarWhere(id_lugar);
                
                if(lugar.getLocatario() != null)
                    enviarMensagem(controlador, lugar.getLocatario().getId(), "Pedido de reserva", "Seu pedido de reserva não pode ser confirmado.");
                
                //atualiza
                getAgendamentos(controlador);
            }
        }.start();
    }

    // pega todos os lugares onde tem locatario definido
    public void getTotalComercializado(VCAdmin controlador) {
        new Thread(){
            @Override
            public void run(){
                LugarBanco lb = new LugarBanco();
                ArrayList<Lugar> lugares = lb.getCheckins();
                
                double total = 0;
                
                for(Lugar lugar: lugares){
                    total += lugar.getDiaria()*lugar.getDias()*(1 - Principal.PROPORCAO_EMBOLSO);
                }
                
                controlador.setTotalComercializado(total+"");
            }
        }.start();
    }

    
}
