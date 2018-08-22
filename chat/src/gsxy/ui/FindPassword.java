package gsxy.ui;

import gsxy.bean.Account;
import gsxy.db.DBOper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FindPassword extends JFrame implements MouseListener{
    JButton reset,findpassword;
    public FindPassword(){
        JLabel lblhead =new JLabel(new ImageIcon("chat/img/qqshow2.jpg"));
        this.add(lblhead);
        reset =new JButton("重置密码");
        findpassword =new JButton("找回密码");
        reset.setBounds(60,150,130,25);
        reset.setForeground(Color.BLUE);
        findpassword.setBounds(200,150,130,25);
        findpassword.setForeground(Color.BLUE);
        lblhead.add(reset);
        lblhead.add(findpassword);

        this.setSize(420,340);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        findpassword.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource()==findpassword){
            String pwd =new DBOper().getPwd(Integer.parseInt(String.valueOf(123456789)));
            JOptionPane.showMessageDialog(null,"您的密码是："+pwd);
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
