/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador_view;

import controlador_view.dialog.VCarregando;
import modelo.Entrar;
import modelo.Usuario;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import modelo.Admin;
import modelo.Principal;
import modelo.Cadastrar;
import modelo.Espaco;
import modelo.Locador;

/**
 *
 * @author Joel
*/
public class ControladorMain {
    private VCMain m;
    private VC vEntrar, vCadastrar, vcLocador, vcLocatario, vcAdmin; // views, a gente se ver por aqui!
    private final VCarregando c;
    private ArrayList<Espaco> rPesquisa;
    private Principal contexto;
    
    public ControladorMain(VCMain m){
        this.m      = m;
        m.setLocationRelativeTo(null);
        c           = new VCarregando();
        
        contexto       = new Principal();
    }
    public void buscar(String tag){
        
        contexto.getEspacosWhere(this, tag);
    }
    // espacos de pesquisa
    public void setREspacosP(ArrayList<Espaco> espacos) {
        rPesquisa = espacos;
        DefaultListModel modelo = new DefaultListModel();
        
        m.getList_pesquisa().setVisible(true);
        if(!espacos.isEmpty()){
            for(Espaco e: rPesquisa){
                int vagas = e.getnLugares() - e.getLugares().size();
                modelo.addElement(e.getTipo()+" : "+e.getEndereco()+" : "+e.getnLugares()+" lugares : "+vagas);
            }
            
            m.getList_pesquisa().setModel(modelo);
        }else{
            m.getList_pesquisa().setVisible(false);
        }
        fecharDialog(m);
    }
    
    public void entrar(String msg){        
        if(vEntrar == null){
            //cria uma view 
            m.setEnabled(false);

            Entrar e    = new Entrar(this);
            vEntrar     = new VCEntrar(e, this);// controlador /view
            
        } 
        
        viewDefault(m, vEntrar);
        vEntrar.setMensagem(msg);
        
        
        fecharDialog(vEntrar);
    }
    
    public void cadastrar(String msg){
        // se não tiver criado a view ainda
        if(vCadastrar == null){
            
            m.setEnabled(false);

            Cadastrar mCadastrar    = new Cadastrar(this);
            vCadastrar              = new VCadastrar(mCadastrar, this);// controlador /view
            
        }
        vCadastrar.setMensagem(msg);
        
        viewDefault(m,vCadastrar);
        fecharDialog(vCadastrar);
    }
    
    // 
    public void viewDefault(VC principal, VC v){
        v.setLocationRelativeTo(null);
        // não deixar fechar o programa todo, caso clique em fechar

        v.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        v.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent event){
                v.setVisible(false);
                principal.setEnabled(true);
                principal.setVisible(true);
            }
        });
        v.setVisible(true);
    }
    
    public void carregando(VC v, String msg){
        c.setLocationRelativeTo(null);
        c.setVisible(true);
        v.setEnabled(false);
    }
    
    // iniciara interfacie crud locador
    public void crudLocador(Usuario usuario){
        // fecha views
        if(vCadastrar != null){
            vCadastrar.dispose();
        }
        if(vEntrar != null){
            vEntrar.dispose();
        }
        if(vcLocatario != null){
            vcLocatario.dispose();
        }
        if(vcAdmin != null)
            vcAdmin.dispose();
        
        // abre view, dessa vez, O controller VCLocador vai se comunicar diretamente com o modelo, Main não será mais o intermediário
        
        Locador locador = new Locador();
        
        locador.setEmail(usuario.getEmail());
        locador.setId(usuario.getId());
        locador.setNome(usuario.getNome());
        locador.setSenha(usuario.getSenha());
        locador.setUsuario(usuario.getUsuario());
        
        vcLocador = new VCLocador(locador, this);
        vcLocador.setEnabled(true);
        vcLocador.setVisible(true);
        fecharDialog(vcLocador);
    }
    
    public void crudLocatario(Usuario usuario){
        // fecha views
        if(vCadastrar != null){
            vCadastrar.dispose();
        }
        if(vEntrar != null){
            vEntrar.dispose();
        }
        if(vcLocador != null){
            vcLocador.dispose();
        }
        
        // abre view, dessa vez, O controller VCLocador vai se comunicar diretamente com o modelo, Main não será mais o intermediário
        vcLocatario = new VCLocatario(usuario, this);
        //vcLocatario.setEnabled(true);
        fecharDialog(vcLocatario);
    }
    public void crudAdmin(Usuario usuario){
        // fecha views
        if(vCadastrar != null){
            vCadastrar.dispose();
            vCadastrar.setVisible(false);
        }
        if(vEntrar != null){
            vEntrar.dispose();
            vEntrar.setVisible(false);
        }
        if(vcLocatario != null){
            vcLocatario.dispose();
            vcLocatario.setVisible(false);
        }
        if(vcLocador != null){
            vcLocador.dispose();
            vcLocador.setVisible(false);
        }
        
        // abre view, dessa vez, O controller VCLocador vai se comunicar diretamente com o modelo, Main não será mais o intermediário
        
        Admin admin = new Admin();
        
        admin.setEmail(usuario.getEmail());
        admin.setId(usuario.getId());
        admin.setNome(usuario.getNome());
        admin.setSenha(usuario.getSenha());
        admin.setUsuario(usuario.getUsuario());
        
        vcAdmin = new VCAdmin(admin, this);
        vcAdmin.setEnabled(true);
        vcAdmin.setVisible(true);
        fecharDialog(vcAdmin);
    }
    
    //view main
    public void main(){
        m.setEnabled(true);
        m.setVisible(true);
    }
    public void fecharDialog(VC view){
        c.setVisible(false);
        c.setEnabled(false);
        c.dispose();
        
        view.setEnabled(true);
        view.setVisible(true);
        
    }
}
