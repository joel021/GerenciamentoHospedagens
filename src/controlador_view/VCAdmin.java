/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador_view;

import controlador_view.dialog.VCAgendamentoAdmin;
import controlador_view.dialog.VCEspacoLocador;
import controlador_view.dialog.VCEspacoInfoAdmin;
import controlador_view.dialog.VCAgendamentoInfo;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import modelo.Admin;
import modelo.Espaco;
import modelo.Lugar;
import modelo.Mensagem;
import modelo.Principal;
import modelo.Usuario;

/**
 *
 * @author Joel
 */
public class VCAdmin extends VCLocadores {
    // vou precisar dessas listas para poder fazer as operações!
    private ArrayList<Espaco> espacos;
    private ArrayList<Mensagem> mensagens;
    private HashMap<Lugar, Espaco> agendamentos;
    private ArrayList<Espaco> rPesquisa;
    private static Admin contexto;
    private static ControladorMain m;
    private ArrayList<? extends Usuario> contatos;
    private int id_para, id_mensagem;
    private VCAgendamentoInfo vcEspaco;
    private VCEspacoLocador vcEspacoLocador;
    private VCAgendamentoAdmin vcAgendamentoAdmin;
    private VCEspacoInfoAdmin vcEspacoInfoAdmin;
    
    boolean l_mensagens, l_espacos, l_contatos, l_agendamentos, l_total;
    /**
     * Creates new form VCLocador
     * @param contexto
     * @param m
     */
    public VCAdmin(Admin contexto, ControladorMain m) {
        VCAdmin.contexto = contexto;
        VCAdmin.m        = m;
        initComponents();
        
        carregarConteudos();
        
        // não há pesquisa!
        list_resultado_p.setVisible(false);
        
        setTitle("Interfacie Administrador: "+contexto.getNome());
    }

    private void carregarConteudos(){
        setLocationRelativeTo(null);
        m.carregando(this, "");
        contexto.getMensagens(this);
        contexto.getContatos(this);
        contexto.getAgendamentos(this);
        contexto.getEspacos(this);
        contexto.getTotalComercializado(this);
        
        // adiciona click mensagem
        MouseListener clickMensagem = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 1){
                    int i           = list_mensagens.locationToIndex(e.getPoint());                    
                    //text_mensagem.setText("De "+mensagens.get(i).getDe().getEmail()+", para "+mensagens.get(i).getPara().getEmail()+" \nMensagem: \n"+mensagens.get(i).getMensagem());
                    
                    text_mensagem.setText(mensagens.get(i).getMensagem());
                    
                    if(mensagens.get(i).getDe().getId() == contexto.getId())
                        text_mensagem.setForeground(Color.DARK_GRAY);
                    else
                       text_mensagem.setForeground(Color.BLUE); 
                    
                    button_enviar.setEnabled(false);
                    button_responder.setEnabled(true);
                    id_mensagem = mensagens.get(i).getId();
                }
            }
        };
        list_mensagens.addMouseListener(clickMensagem);

        // adiciona click mensagem
        MouseListener clickContatos = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 1){
                    int i = list_contatos.locationToIndex(e.getPoint());
                    id_para = contatos.get(i).getId();

                    text_contato.setText(contatos.get(i).getNome()+" - "+contatos.get(i).getEmail());
                    button_enviar.setEnabled(true);
                    button_responder.setEnabled(false);
                }
            }
        };
        list_contatos.addMouseListener(clickContatos);

        // adiciona click lugar
        MouseListener clickEspacoPesquisa = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 1){
                    if(e.getClickCount() == 1){
                        // mostra view
                        int i = list_resultado_p.locationToIndex(e.getPoint());

                        if(vcEspacoInfoAdmin == null){
                            vcEspacoInfoAdmin = new VCEspacoInfoAdmin(contexto, VCAdmin.this, m);
                        }

                        vcEspacoInfoAdmin.setTitle(rPesquisa.get(i).getTipo());
                        vcEspacoInfoAdmin.setEdit_tipo(rPesquisa.get(i).getTipo());
                        vcEspacoInfoAdmin.setEdit_diaria(rPesquisa.get(i).getDiaria()+"");
                        vcEspacoInfoAdmin.setEdit_endereco(rPesquisa.get(i).getEndereco());
                        vcEspacoInfoAdmin.setId_espaco(rPesquisa.get(i).getId());
                        vcEspacoInfoAdmin.setEdit_lugares(rPesquisa.get(i).getnLugares()+"");
                        vcEspacoInfoAdmin.setEdit_locador(rPesquisa.get(i).getLocador().getEmail());
                        vcEspacoInfoAdmin.setId_espaco(rPesquisa.get(i).getId());
                        vcEspacoInfoAdmin.setText_lugares_livres(rPesquisa.get(i).getLugaresLivres()+"");
                        vcEspacoInfoAdmin.setVisible(true);

                    }
                }
            }
        };
        list_resultado_p.addMouseListener(clickEspacoPesquisa);

        // adiciona click espacos dele
        MouseListener clickEspaco = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 1){
                    int i           = list_mensagens.locationToIndex(e.getPoint());

                    // se for nulo, cria nova view
                    if(vcEspacoLocador == null){
                        vcEspacoLocador = new VCEspacoLocador(contexto, VCAdmin.this, m);
                    }
                    // configura
                    vcEspacoLocador.setEdit_diaria(espacos.get(i).getDiaria()+"");
                    vcEspacoLocador.setEdit_endereco(espacos.get(i).getEndereco()+"");
                    vcEspacoLocador.setEdit_lugares(espacos.get(i).getnLugares()+"");
                    vcEspacoLocador.setEdit_lugares_ocupados(espacos.get(i).getLugares().size()+"");
                    vcEspacoLocador.setIdEspaco(espacos.get(i).getId());
                    vcEspacoLocador.setTextComissao(espacos.get(i).getComissao()+"");
                    vcEspacoLocador.setEnabled(true);
                }
            }
        };
        list_espacos.addMouseListener(clickEspaco);

        // agendamentos, o admin pode aceitar ou recusar
        MouseListener clickAgendamentos = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 1){
                    // mostra view
                    int i = list_agendamentos.locationToIndex(e.getPoint());

                    if(vcAgendamentoAdmin == null){
                        vcAgendamentoAdmin = new VCAgendamentoAdmin(contexto, VCAdmin.this, m);
                    }

                    Iterator<Lugar> it =  agendamentos.keySet().iterator(); // iterator de lugares

                    for(int i2 = 0; i2 < i; i2++){
                        it.next();
                    }
                    Lugar lugar = it.next();
                    vcAgendamentoAdmin.setTitle(agendamentos.get(lugar).getTipo());
                    vcAgendamentoAdmin.setText_diaria(lugar.getDiaria()+"");
                    vcAgendamentoAdmin.setText_tipo(agendamentos.get(lugar).getTipo());
                    vcAgendamentoAdmin.setText_endereco(agendamentos.get(lugar).getEndereco());
                    vcAgendamentoAdmin.setText_status(lugar.getStatus());
                    vcAgendamentoAdmin.setId_espaco(agendamentos.get(lugar).getId());
                    vcAgendamentoAdmin.setText_usuario(lugar.getLocatario().getNome()+" - "+lugar.getLocatario().getEmail());
                    vcAgendamentoAdmin.setIdUsuario(lugar.getLocatario().getId());
                    vcAgendamentoAdmin.setText_dias(lugar.getDias()+"");
                    
                    if(lugar.getLocatario().getId() != contexto.getId()){
                        vcAgendamentoAdmin.getButton_checkin().setEnabled(false);
                        vcAgendamentoAdmin.getButton_checkout().setEnabled(false);
                    }else{
                        vcAgendamentoAdmin.getButton_checkin().setEnabled(true);
                        vcAgendamentoAdmin.getButton_checkout().setEnabled(false);
                    }
                    vcAgendamentoAdmin.setVisible(true);
                    setEnabled(false);
                }
            }
        };
        list_agendamentos.addMouseListener(clickAgendamentos);
    }
    // chamada pelo modelo para encher a lista de espacos
    @Override
    public void setEspacosLocador(ArrayList<Espaco> espacos){
        this.espacos = espacos;
        DefaultListModel modelo;
        if(!espacos.isEmpty()){
            
            modelo          = new DefaultListModel();
            for(Espaco espaco: espacos){
                modelo.addElement(espaco.getTipo()+" : "+espaco.getEndereco()+" : R$"+espaco.getDiaria()+" : "+espaco.getLugares()+" lugares : "+espaco.getnLugares()+", "+espaco.getLugares().size()+" ocupados");
            }

            list_espacos.setModel(modelo);
            list_espacos.setVisible(true);
        }else
            list_espacos.setVisible(false);
        
        l_espacos = true;
        viewCarregada();
    }
    // mensagem <de, mesagem>
    @Override
    public void setMensagens(ArrayList<Mensagem> mensagens){
        this.mensagens = mensagens;
        
        list_mensagens.setVisible(true);
        if(!mensagens.isEmpty()){
            DefaultListModel modelo = new DefaultListModel();
            for(Mensagem m: mensagens){
                modelo.addElement(m.getTitulo()+" para "+m.getPara().getEmail());
            }
            list_mensagens.setModel(modelo);
        }else
            list_mensagens.setVisible(false);
        l_mensagens = true;
        viewCarregada();
    }
    
    @Override
    public void setContatos(ArrayList <? extends Usuario> contatos){
        this.contatos = contatos;
        
        list_contatos.setVisible(true);
        
        // tomei cuidado de sempre enviar pelo menos uma referencia de lista vazia em todos os métodos que enchem lista
        if(!contatos.isEmpty()){
            DefaultListModel modelo = new DefaultListModel();
            for(Usuario u: contatos){
                modelo.addElement(u.getNome()+" - "+u.getEmail()+" : "+u.getUsuario());
            }
            list_contatos.setModel(modelo);
        }else
            list_contatos.setVisible(false);
        l_contatos = true;
        viewCarregada();
    }
    
    // verifica se as views foram carregadas
    
    private void viewCarregada(){
        if(l_mensagens && l_espacos && l_contatos && l_agendamentos){
            m.fecharDialog(this);
            setEnabled(true);
            this.setFocusable(true);
        } else{
            m.carregando(this, "");
        }
    }
    // agendamentos do usuário
    @Override
    public void setAgendamentos(HashMap<Lugar, Espaco> agendamentos) {
        this.agendamentos = agendamentos;
        
        DefaultListModel modelo = new DefaultListModel();
        double total            = 0;
        if(!agendamentos.isEmpty()){
            
            Iterator<Lugar> i2 = agendamentos.keySet().iterator(); // iterator de lugares
            
            while(i2.hasNext()){
                Lugar lugar = i2.next();
                modelo.addElement(agendamentos.get(lugar).getTipo()+" : "+lugar.getStatus()+" : R$"+lugar.getDiaria()+" : "+lugar.getDias()+" dias : "+" "+agendamentos.get(lugar).getEndereco());
                
                if(lugar.getStatus().equals(Principal.CHECKIN))
                    total += lugar.getDiaria()*lugar.getDias();
            }
            list_agendamentos.setModel(modelo);
            list_agendamentos.setVisible(true);
            
        }else
            list_agendamentos.setVisible(false);
        text_total_gasto.setText(total+"");
        l_agendamentos = true;
        viewCarregada();
    }

    // pesquisa de um lugar
    @Override
    public void setREspacosP(ArrayList<Espaco> espacos) {
        rPesquisa = espacos;
        if(!espacos.isEmpty()){
            DefaultListModel modelo = new DefaultListModel();
            for(Espaco e: espacos){
                int vagas = e.getnLugares() - e.getLugares().size();
                modelo.addElement(e.getTipo()+" : "+e.getEndereco()+" : "+e.getnLugares()+" : "+vagas+" vagas");
            }
            list_resultado_p.setModel(modelo);
            list_resultado_p.setVisible(true);
        }else
            list_resultado_p.setVisible(false);
        
        m.fecharDialog(this);
    }
    @Override
    public ArrayList<Espaco> getRPesquisa(){
        return rPesquisa;
    }
    
    public void setTotalComercializado(String total){
        l_total = true;
        text_total.setText(total);
        viewCarregada();
    }
    
    @Override
    public void setMensagem(String msg){
        text_aviso.setText(msg);
    }
    
    @Override
    public void setLucro(String lucro){
        text_lucro.setText(lucro);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list_espacos = new javax.swing.JList<>();
        text_espacos = new javax.swing.JLabel();
        text_lucro = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        list_mensagens = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        edit_mensagem = new javax.swing.JTextArea();
        button_enviar = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        list_contatos = new javax.swing.JList<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        text_mensagem = new javax.swing.JTextArea();
        text_contato = new javax.swing.JLabel();
        edit_titulo = new javax.swing.JTextField();
        button_responder = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        edit_pesquisar = new javax.swing.JTextField();
        button_pesquisar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_resultado_p = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        edit_tipo = new javax.swing.JTextField();
        edit_endereco = new javax.swing.JTextField();
        edit_lugares = new javax.swing.JTextField();
        edit_diaria = new javax.swing.JTextField();
        button_adicionar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        button_sair = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        list_agendamentos = new javax.swing.JList<>();
        jLabel11 = new javax.swing.JLabel();
        text_msg = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        text_total = new javax.swing.JLabel();
        text_aviso = new javax.swing.JLabel();
        text_total_gasto = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(194, 194, 194));

        list_espacos.setBackground(new java.awt.Color(216, 216, 216));
        list_espacos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(list_espacos);

        text_espacos.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        text_espacos.setText("Seus espacos(você é o dono)");

        jLabel13.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel13.setText("Lucro sobre os espacos R$");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(text_espacos)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_lucro)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(text_espacos, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_lucro)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(194, 194, 194));

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel1.setText("Mensagens");

        list_mensagens.setBackground(new java.awt.Color(216, 216, 216));
        list_mensagens.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(list_mensagens);

        edit_mensagem.setColumns(20);
        edit_mensagem.setRows(5);
        jScrollPane4.setViewportView(edit_mensagem);

        button_enviar.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        button_enviar.setText("Enviar");
        button_enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_enviarActionPerformed(evt);
            }
        });

        list_contatos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(list_contatos);

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel9.setText("Contatar a");

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel10.setText("Titulo");

        text_mensagem.setEditable(false);
        text_mensagem.setColumns(20);
        text_mensagem.setRows(5);
        jScrollPane7.setViewportView(text_mensagem);

        text_contato.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        text_contato.setText("contato");

        edit_titulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_tituloActionPerformed(evt);
            }
        });

        button_responder.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        button_responder.setText("Responder");
        button_responder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_responderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(edit_titulo))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(text_contato)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(button_responder)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button_enviar))
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(text_contato))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(edit_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_enviar)
                    .addComponent(button_responder))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(194, 194, 194));

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel3.setText("Espacos");

        button_pesquisar.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        button_pesquisar.setText("Pesquisar");
        button_pesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_pesquisarActionPerformed(evt);
            }
        });

        list_resultado_p.setBackground(new java.awt.Color(216, 216, 216));
        list_resultado_p.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(list_resultado_p);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edit_pesquisar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_pesquisar)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(edit_pesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_pesquisar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(194, 194, 194));

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel4.setText("Adicionar Espaco");

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel5.setText("Tipo");

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel6.setText("Endereco");

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel7.setText("Lugares");

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel8.setText("Diária");

        button_adicionar.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        button_adicionar.setText("Adicionar Espaco");
        button_adicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_adicionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edit_tipo)
                            .addComponent(edit_endereco)
                            .addComponent(edit_lugares)
                            .addComponent(edit_diaria)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button_adicionar)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(edit_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(edit_endereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(edit_lugares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(edit_diaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_adicionar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(194, 194, 194));
        jPanel6.setMaximumSize(new java.awt.Dimension(377, 425));

        button_sair.setText("sair");
        button_sair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_sairActionPerformed(evt);
            }
        });

        list_agendamentos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(list_agendamentos);

        jLabel11.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel11.setText("Total gasto R$: ");

        text_msg.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        text_msg.setText("Ola");

        jLabel12.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel12.setText("Total comercializado R$ ");

        text_total.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        text_total.setText("jLabel13");

        text_aviso.setForeground(new java.awt.Color(204, 0, 0));

        text_total_gasto.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        text_total_gasto.setText("jLabel14");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button_sair))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(text_msg, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(text_aviso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(text_total_gasto)
                            .addComponent(text_total, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_msg)
                    .addComponent(text_aviso))
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(text_total))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(text_total_gasto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_sair)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void edit_tituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_tituloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edit_tituloActionPerformed

    private void button_sairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_sairActionPerformed
        // TODO add your handling code here:
        m.main();
        setEnabled(false);
        setVisible(false);
        contexto = null;
    }//GEN-LAST:event_button_sairActionPerformed

    private void button_pesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_pesquisarActionPerformed
        // TODO add your handling code here:
        m.carregando(this, "");
        contexto.buscar(this, edit_pesquisar.getText());
    }//GEN-LAST:event_button_pesquisarActionPerformed

    private void button_responderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_responderActionPerformed
        // TODO add your handling code here:
        m.carregando(this, "Enviar mensagem");
            // o usuáro está em lista de contatos
        contexto.responderMensagem(this, id_mensagem, edit_mensagem.getText());
    }//GEN-LAST:event_button_responderActionPerformed

    private void button_adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_adicionarActionPerformed
        // TODO add your handling code here:
        m.carregando(this, "Adicionar espaco");
        contexto.adicionarEspaco(this, edit_diaria.getText(), edit_tipo.getText(), edit_lugares.getText(), edit_endereco.getText());
    }//GEN-LAST:event_button_adicionarActionPerformed

    private void button_enviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_enviarActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        if(id_para == 0){
            text_contato.setText("selecione um destinatario");
        }else{
            m.carregando(this, "Enviar mensagem");
            // o usuáro está em lista de contatos
            contexto.enviarMensagem(this, id_para, edit_titulo.getText(), edit_mensagem.getText());
        }
    }//GEN-LAST:event_button_enviarActionPerformed
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VCLocador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VCLocador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VCLocador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VCLocador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VCLocador(contexto, m).setVisible(true);
            }
        });
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_adicionar;
    private javax.swing.JButton button_enviar;
    private javax.swing.JButton button_pesquisar;
    private javax.swing.JButton button_responder;
    private javax.swing.JButton button_sair;
    private javax.swing.JTextField edit_diaria;
    private javax.swing.JTextField edit_endereco;
    private javax.swing.JTextField edit_lugares;
    private javax.swing.JTextArea edit_mensagem;
    private javax.swing.JTextField edit_pesquisar;
    private javax.swing.JTextField edit_tipo;
    private javax.swing.JTextField edit_titulo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JList<String> list_agendamentos;
    private javax.swing.JList<String> list_contatos;
    private javax.swing.JList<String> list_espacos;
    private javax.swing.JList list_mensagens;
    private javax.swing.JList list_resultado_p;
    private javax.swing.JLabel text_aviso;
    private javax.swing.JLabel text_contato;
    private javax.swing.JLabel text_espacos;
    private javax.swing.JLabel text_lucro;
    private javax.swing.JTextArea text_mensagem;
    private javax.swing.JLabel text_msg;
    private javax.swing.JLabel text_total;
    private javax.swing.JLabel text_total_gasto;
    // End of variables declaration//GEN-END:variables

}
