/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador_view;

import controlador_view.dialog.VCEspacoInfo;
import controlador_view.dialog.VCAgendamentoInfo;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import modelo.Espaco;
import modelo.Lugar;
import modelo.Mensagem;
import modelo.Principal;
import modelo.Usuario;

/**
 *
 * @author Joel
 */
public class VCLocatario extends ControladorCliente {
    
    // vou precisar dessas listas para poder fazer as operações!
    private ArrayList<Mensagem> mensagens;
    private static HashMap<Lugar, Espaco> agendamentos; // um lugar pode ter vários lugares, mas um lugar só tem um lugar
    private static ArrayList<Espaco> rPesquisa;
    private static Usuario contexto;
    private static ControladorMain m;
    private ArrayList<? extends Usuario> contatos;
    private int id_para, id_mensagem;
    private VCAgendamentoInfo vcAgendamentoInfo;
    private VCEspacoInfo vcEspacoInfo;
    
    boolean l_mensagens, l_contatos, l_agendamentos;
    
    /**
     * Creates new form VCLocador
     * @param contexto
     * @param m
     */
    public VCLocatario(Usuario contexto, ControladorMain m) {
        VCLocatario.contexto = contexto;
        VCLocatario.m        = m;
        initComponents();
        
        carregarConteudos();
        id_para = 0;
        list_resultado_p.setVisible(false);
        setTitle("Interfacie Locatário: "+contexto.getNome());
    }
    // carregar conteudos que o Usuario tem acesso
    private void carregarConteudos(){
        
        setLocationRelativeTo(null);
        m.carregando(this, "");
        contexto.getMensagens(this);
        contexto.getContatos(this);
        contexto.getAgendamentos(this);
        
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
                    int i   = list_contatos.locationToIndex(e.getPoint());
                    id_para = contatos.get(i).getId();

                    text_contato.setText(contatos.get(i).getNome()+" - "+contatos.get(i).getEmail());
                    button_enviar.setEnabled(true);
                    button_responder.setEnabled(false);
                }
            }
        };
        list_contatos.addMouseListener(clickContatos);

        // adiciona click lugar de pesquisa
        MouseListener clickEspacoPesquisa = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 1){
                    // mostra view
                    int i = list_resultado_p.locationToIndex(e.getPoint());

                    if(vcEspacoInfo == null){
                        vcEspacoInfo = new VCEspacoInfo(contexto, VCLocatario.this, m);
                    }
                    vcEspacoInfo.setTitle(rPesquisa.get(i).getTipo());
                    vcEspacoInfo.setText_endereco(rPesquisa.get(i).getEndereco());
                    vcEspacoInfo.setId_espaco(rPesquisa.get(i).getId());
                    vcEspacoInfo.setText_lugares(rPesquisa.get(i).getnLugares()+"");
                    vcEspacoInfo.setText_diaria(rPesquisa.get(i).getDiaria()+"");
                    vcEspacoInfo.setLugares_livres(rPesquisa.get(i).getLugaresLivres()+"");
                    vcEspacoInfo.setText_tipo(rPesquisa.get(i).getTipo());
                    vcEspacoInfo.setVisible(true);

                }
            }
        };
        list_resultado_p.addMouseListener(clickEspacoPesquisa);

        // adiciona click lugar
        MouseListener clickEspacoPedido = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 1){
                    // mostra view
                    int i = list_agendamentos.locationToIndex(e.getPoint());

                    if(vcAgendamentoInfo == null){
                        vcAgendamentoInfo = new VCAgendamentoInfo(contexto, VCLocatario.this, m);
                    }

                    Iterator<Lugar> it =  agendamentos.keySet().iterator(); // iterator de lugares

                    for(int i2 = 0; i2 < i; i2++){
                        it.next();
                    }
                    
                    Lugar lugar = it.next();
                    vcAgendamentoInfo.setTitle(agendamentos.get(lugar).getTipo());
                    vcAgendamentoInfo.setId_lugar(lugar.getId());
                    vcAgendamentoInfo.setTipo(agendamentos.get(lugar).getTipo());
                    vcAgendamentoInfo.setDiaria(lugar.getDiaria());
                    vcAgendamentoInfo.setEndereco(agendamentos.get(lugar).getEndereco());
                    vcAgendamentoInfo.setText_status(lugar.getStatus());

                    vcAgendamentoInfo.setVisible(true);
                    setEnabled(false);
                }
            }
        };
        list_agendamentos.addMouseListener(clickEspacoPedido);
    }
    
    // mensagem <de, mesagem>
    @Override
    public void setMensagens(ArrayList<Mensagem> mensagens){
        this.mensagens          = mensagens;
        DefaultListModel modelo = new DefaultListModel();
        list_mensagens.setVisible(true);
        if(!mensagens.isEmpty()){
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
        DefaultListModel modelo = new DefaultListModel();
        list_contatos.setVisible(true);
        if(!contatos.isEmpty()){
            for(Usuario u: contatos){
                modelo.addElement(u.getNome()+" - "+u.getEmail()+" : "+u.getUsuario());
            }
            list_contatos.setModel(modelo);
        }else
            list_contatos.setVisible(false);
        l_contatos = true;
        viewCarregada();
    }
    
    // agendamentos do usuário
    @Override
    public void setAgendamentos(HashMap<Lugar, Espaco> agendamentos) {
        VCLocatario.agendamentos        = agendamentos;
        DefaultListModel modelo         = new DefaultListModel();
        double total                    = 0;
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
        
        l_agendamentos = true;
        text_total_gasto.setText(total+"");
        viewCarregada();
    }

    // verifica se as views foram carregadas
    
    private void viewCarregada(){
        if(l_mensagens && l_contatos && l_agendamentos){
            m.fecharDialog(this);
            setEnabled(true);
            this.setFocusable(true);
        }
    }
   
    // espacos de pesquisa
    @Override
    public void setREspacosP(ArrayList<Espaco> espacos) {
        rPesquisa = espacos;
        DefaultListModel modelo = new DefaultListModel();
        list_resultado_p.setVisible(true);
        if(!espacos.isEmpty()){
            for(Espaco e: rPesquisa){
                int vagas = e.getnLugares() - e.getLugares().size();
                modelo.addElement(e.getTipo()+" : "+e.getEndereco()+" : "+e.getnLugares()+" lugares : "+vagas);
            }
            
            list_resultado_p.setModel(modelo);
        }else{
            list_resultado_p.setVisible(false);
        }
        m.fecharDialog(this);
    }
    
    @Override
    public ArrayList<Espaco> getRPesquisa(){
        return rPesquisa;
    }
   
    @Override
    public void setMensagem(String msg){
        text_avisos.setText(msg);
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
        jPanel6 = new javax.swing.JPanel();
        button_sair = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        list_agendamentos = new javax.swing.JList<>();
        jLabel11 = new javax.swing.JLabel();
        text_msg = new javax.swing.JLabel();
        text_avisos = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        text_total_gasto = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(194, 194, 194));

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

        jLabel9.setText("Contatar a");

        jLabel10.setText("Titulo");

        text_mensagem.setEditable(false);
        text_mensagem.setColumns(20);
        text_mensagem.setRows(5);
        jScrollPane7.setViewportView(text_mensagem);

        text_contato.setText("contato");

        edit_titulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_tituloActionPerformed(evt);
            }
        });

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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button_responder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_enviar))
                    .addComponent(jScrollPane5)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(text_contato)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(edit_titulo))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane3)))
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

        jLabel3.setText("Espacos");

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
                        .addComponent(edit_pesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
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
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(194, 194, 194));

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

        jLabel11.setText("Lista de espacos que você pediu agendamento");

        text_msg.setText("Ola");

        text_avisos.setForeground(new java.awt.Color(204, 0, 51));

        jLabel4.setText("Total gasto: R$");

        text_total_gasto.setText("jLabel5");

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
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(text_msg, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(text_avisos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_total_gasto))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 4, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_msg)
                    .addComponent(text_avisos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(text_total_gasto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6)
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
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        // TODO add your handling code here:
         m.main();
        setEnabled(false);
        setVisible(false);
        contexto = null;
    }//GEN-LAST:event_button_sairActionPerformed

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
            java.util.logging.Logger.getLogger(VCLocatario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VCLocatario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VCLocatario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VCLocatario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VCLocatario(contexto, m).setVisible(true);
            }
        });
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_enviar;
    private javax.swing.JButton button_pesquisar;
    private javax.swing.JButton button_responder;
    private javax.swing.JButton button_sair;
    private javax.swing.JTextArea edit_mensagem;
    private javax.swing.JTextField edit_pesquisar;
    private javax.swing.JTextField edit_titulo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JList<String> list_agendamentos;
    private javax.swing.JList<String> list_contatos;
    private javax.swing.JList list_mensagens;
    private javax.swing.JList list_resultado_p;
    private javax.swing.JLabel text_avisos;
    private javax.swing.JLabel text_contato;
    private javax.swing.JTextArea text_mensagem;
    private javax.swing.JLabel text_msg;
    private javax.swing.JLabel text_total_gasto;
    // End of variables declaration//GEN-END:variables

}
