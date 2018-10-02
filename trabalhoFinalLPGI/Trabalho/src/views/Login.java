package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Login extends JFrame {

	private JPanel contentPane;
	
	MainView MV = new MainView();

	/**
	 * Launch the application.
	 */
	ImageIcon img = new ImageIcon(this.getClass().getResource("/bella.png").getFile());
	ImageIcon img1 = new ImageIcon(this.getClass().getResource("/fundo-de-design.jpg").getFile());
	private JTextField tfLogin;
	private JPasswordField tfSenha;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogo = new JLabel("Logo");
		lblLogo.setIcon(img);
		lblLogo.setBounds(259, 40, 116, 79);
		contentPane.add(lblLogo);
		
		tfLogin = new JTextField();
		tfLogin.setBounds(248, 203, 145, 20);
		contentPane.add(tfLogin);
		tfLogin.setColumns(10);
		
		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblLogin.setBounds(192, 206, 46, 14);
		contentPane.add(lblLogin);
		
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblSenha.setBounds(192, 237, 46, 14);
		contentPane.add(lblSenha);
		
		JButton btnAcessar = new JButton("Acessar");
		btnAcessar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tfLogin.getText().equals("admin") && tfSenha.getText().equals("admin")) {
					MV.setVisible(true);
					setVisible(false);
				}else {
					JOptionPane.showMessageDialog(null, "Login ou senha Incorretos!");
				}
			}
		});
		btnAcessar.setBounds(274, 265, 89, 23);
		contentPane.add(btnAcessar);
		
		tfSenha = new JPasswordField();
		tfSenha.setBounds(248, 234, 145, 20);
		contentPane.add(tfSenha);
		
		JLabel lblFundo = new JLabel("Fundo");
		lblFundo.setIcon(img1);
		lblFundo.setBounds(0, 0, 624, 441);
		contentPane.add(lblFundo);
	}
}
