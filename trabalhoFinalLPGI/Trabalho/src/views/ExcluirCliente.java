package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DAOs.ClienteDAO;
import beans.Cliente;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ExcluirCliente extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	ClienteDAO cldao = new ClienteDAO();
	Cliente cl = new Cliente();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExcluirCliente frame = new ExcluirCliente();
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
	private List<Object> cliente = new ArrayList<Object>();
	ImageIcon img = new ImageIcon(this.getClass().getResource("/background-pics-002.jpg").getFile());
	ImageIcon img1 = new ImageIcon(this.getClass().getResource("/cli3.png").getFile());
	private JTable table;
	private JTextField textCodigo;
	
	public ExcluirCliente() {
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent arg0) {
				atualizarTabela();
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCadastro = new JLabel("Cadastro");
		lblCadastro.setIcon(img1);
		lblCadastro.setBounds(474, 27, 105, 107);
		contentPane.add(lblCadastro);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(75, 145, 475, 240);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				setCamposFromTabela();
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nome", "CNPJ", "Telefone"
			}
		));
		scrollPane.setViewportView(table);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1){
					Object[] options3 = {"Excluir", "Cancelar"};
					if(JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir o registro:\n>   " 
							+ table.getValueAt(table.getSelectedRow(), 0) + "   -   "
							+ table.getValueAt(table.getSelectedRow(), 1), null,
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options3, options3[0]) == 0){
						try {
						
							//atribuição do valor do campo código para o objeto cliente
							cl.setId(Integer.parseInt(textCodigo.getText()));
							
							// chamada do método de exclusão na classe Dao passando como parâmetro o código do cliente para ser excluído
							cldao.deletarCliente(cl);
							
						
							atualizarTabela();
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
				}
			
			}
		});
		btnExcluir.setBounds(277, 394, 89, 23);
		contentPane.add(btnExcluir);
		
		JLabel lblExcluirCliente = new JLabel("Excluir Cliente");
		lblExcluirCliente.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblExcluirCliente.setBounds(251, 70, 178, 14);
		contentPane.add(lblExcluirCliente);
		
		JLabel lblFundo = new JLabel("Fundo");
		lblFundo.setIcon(img);
		lblFundo.setBounds(0, 0, 624, 441);
		contentPane.add(lblFundo);
		
		textCodigo = new JTextField();
		textCodigo.setVisible(false);
		textCodigo.setBounds(10, 70, 86, 20);
		contentPane.add(textCodigo);
		textCodigo.setColumns(10);
	}
	public void setCamposFromTabela() {
		textCodigo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
	}
	public void atualizarTabela() {
		try {
			cliente = cldao.buscarTodos();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
		for (int x=0; x!=cliente.size(); x++)
			{
				model.addRow((Object[]) cliente.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

}
