/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador_view.dialog;

import controlador_view.ControladorMain;
import controlador_view.VCLocadores;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import modelo.Admin;

/**
 *
 * @author Joel
 */
public class VCEspacoInfoAdmin extends javax.swing.JFrame {
    private int id_espaco, id_locatario;
    private static Admin contexto;
    private static VCLocadores controlador;
    private static ControladorMain m;
    
    /**
     * Creates new form VCEspacoInfoAdmin
     */
    public VCEspacoInfoAdmin(Admin contexto, VCLocadores controlador, ControladorMain m) {
        VCEspacoInfoAdmin.contexto      = contexto;
        VCEspacoInfoAdmin.controlador   = controlador;
        VCEspacoInfoAdmin.m = m;
        initComponents();
        
        
        setLocationRelativeTo(null);
        // não deixar fechar o programa todo, caso clique em fechar

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent event){
                setVisible(false);
                controlador.setEnabled(true);
                controlador.setVisible(true);
            }
        });
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        edit_tipo = new javax.swing.JTextField();
        edit_endereco = new javax.swing.JTextField();
        edit_lugares = new javax.swing.JTextField();
        edit_locador = new javax.swing.JTextField();
        edit_diaria = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        edit_n_dias = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        edit_n_lugares = new javax.swing.JTextField();
        text_lugares_livres = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        button_excluir = new javax.swing.JButton();
        button_atualizar = new javax.swing.JButton();
        button_ok = new javax.swing.JButton();
        button_reservar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(27, 27, 27));
        jLabel1.setText("Tipo");

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(27, 27, 27));
        jLabel2.setText("Endereco");

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(27, 27, 27));
        jLabel3.setText("Lugares");

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(27, 27, 27));
        jLabel4.setText("Locador");

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(27, 27, 27));
        jLabel5.setText("Diária");

        edit_tipo.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        edit_endereco.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        edit_lugares.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        edit_locador.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        edit_diaria.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(27, 27, 27));
        jLabel6.setText("Dias");

        edit_n_dias.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel7.setText("Para reservar");

        jLabel8.setText("Quantidade de lugares");

        text_lugares_livres.setText("livres");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(edit_diaria, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                            .addComponent(edit_locador, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                            .addComponent(edit_endereco)
                            .addComponent(edit_tipo)
                            .addComponent(edit_n_dias)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(edit_lugares, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_lugares_livres, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(edit_n_lugares, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(edit_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(edit_endereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(edit_lugares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_lugares_livres))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edit_locador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(edit_diaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(edit_n_lugares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(edit_n_dias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47))
        );

        button_excluir.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        button_excluir.setText("Excluir");
        button_excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_excluirActionPerformed(evt);
            }
        });

        button_atualizar.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        button_atualizar.setText("Atualizar");
        button_atualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_atualizarActionPerformed(evt);
            }
        });

        button_ok.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        button_ok.setText("ok");
        button_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_okActionPerformed(evt);
            }
        });

        button_reservar.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        button_reservar.setText("Reservar");
        button_reservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_reservarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(button_ok)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                .addComponent(button_reservar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_excluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button_atualizar)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_excluir)
                    .addComponent(button_atualizar)
                    .addComponent(button_ok)
                    .addComponent(button_reservar)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_excluirActionPerformed
        // TODO add your handling code here:
        m.carregando(controlador, "");
        dispose();
        contexto.removerEspaco(controlador, id_espaco);
    }//GEN-LAST:event_button_excluirActionPerformed

    private void button_atualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_atualizarActionPerformed
        // TODO add your handling code here:
        m.carregando(controlador, "");
        dispose();
        contexto.atualizarEspaco(controlador, id_espaco, edit_diaria.getText(), edit_tipo.getText(), edit_lugares.getText(), edit_endereco.getText());
    }//GEN-LAST:event_button_atualizarActionPerformed

    private void button_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_okActionPerformed
          controlador.setVisible(true);
          controlador.setEnabled(true);
          dispose();
    }//GEN-LAST:event_button_okActionPerformed

    private void button_reservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_reservarActionPerformed
        // TODO add your handling code here:
        m.carregando(controlador, "");
        dispose();
        //ControladorCliente controlador, int id_espaco, String q_pessoas, String q_dias
        contexto.reservar(controlador, id_espaco, edit_n_lugares.getText(), edit_n_dias.getText());
    }//GEN-LAST:event_button_reservarActionPerformed

    public void setId_espaco(int id_espaco) {
        this.id_espaco = id_espaco;
    }

    public void setId_locatario(int id_locatario) {
        this.id_locatario = id_locatario;
    }

    public void setEdit_endereco(String edit_endereco) {
        this.edit_endereco.setText(edit_endereco);
    }

    public void setEdit_locador(String edit_locador) {
        this.edit_locador.setText(edit_locador);
    }

    public void setEdit_lugares(String edit_lugares) {
        this.edit_lugares.setText(edit_lugares);
    }

    public void setEdit_diaria(String edit_diaria) {
        this.edit_diaria.setText(edit_diaria);
    }

    public void setEdit_tipo(String edit_tipo) {
        this.edit_tipo.setText(edit_tipo);
    }

    public void setText_lugares_livres(String text_lugares_livres) {
        this.text_lugares_livres.setText(text_lugares_livres+" lugares livres");
    }
    
    
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
            java.util.logging.Logger.getLogger(VCEspacoInfoAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VCEspacoInfoAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VCEspacoInfoAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VCEspacoInfoAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VCEspacoInfoAdmin(contexto, controlador, m).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_atualizar;
    private javax.swing.JButton button_excluir;
    private javax.swing.JButton button_ok;
    private javax.swing.JButton button_reservar;
    private javax.swing.JTextField edit_diaria;
    private javax.swing.JTextField edit_endereco;
    private javax.swing.JTextField edit_locador;
    private javax.swing.JTextField edit_lugares;
    private javax.swing.JTextField edit_n_dias;
    private javax.swing.JTextField edit_n_lugares;
    private javax.swing.JTextField edit_tipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel text_lugares_livres;
    // End of variables declaration//GEN-END:variables
}
