package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import DAOs.ProdutoDAO;
import beans.Produto;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ExcluirProduto extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	Produto pr = new Produto();
	ProdutoDAO prdao = new ProdutoDAO();
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExcluirProduto frame = new ExcluirProduto();
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
	ImageIcon img1 = new ImageIcon(this.getClass().getResource("/tri.png").getFile());
	private JTable table;
	private JTextField textCodigo;
	public ExcluirProduto() {
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(65, 167, 486, 232);
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
				"Codigo", "Tipo", "Pre\u00E7o", "Quantidade"
			}
		));
		scrollPane.setViewportView(table);
		
		JLabel lblExcluirProduto = new JLabel("Excluir Produto");
		lblExcluirProduto.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblExcluirProduto.setBounds(225, 43, 149, 59);
		contentPane.add(lblExcluirProduto);
		
		JLabel lblTrigo = new JLabel("trigo");
		lblTrigo.setIcon(img1);
		lblTrigo.setBounds(447, 23, 116, 104);
		contentPane.add(lblTrigo);
		
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
							pr.setId(Integer.parseInt(textCodigo.getText()));
							
							// chamada do método de exclusão na classe Dao passando como parâmetro o código do cliente para ser excluído
							prdao.deletarProduto(pr);
							
						
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
		btnExcluir.setBounds(267, 410, 89, 23);
		contentPane.add(btnExcluir);
		
		JLabel lblFundo = new JLabel("Fundo");
		lblFundo.setIcon(img);
		lblFundo.setBounds(0, 0, 624, 441);
		contentPane.add(lblFundo);
		
		textCodigo = new JTextField();
		textCodigo.setVisible(false);
		textCodigo.setText("Codigo");
		textCodigo.setBounds(45, 91, 86, 20);
		contentPane.add(textCodigo);
		textCodigo.setColumns(10);
	}
	public void setCamposFromTabela() {
		textCodigo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
	}
	public void atualizarTabela() {
		try {
			cliente = prdao.buscarTodos();
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
