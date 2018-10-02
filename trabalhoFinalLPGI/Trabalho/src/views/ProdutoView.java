package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import DAOs.ProdutoDAO;
import beans.Produto;
import java.awt.Font;
//import views.ClienteView;

public class ProdutoView extends JFrame {
private static final long serialVersionUID = 1L;
private JPanel contentPane;
private JTextField textNome;
private JTextField textCpf;
private JTextField textTelefone;
private JTextField textPNome;
private JTextField textPCodigo;
private JTable table;

private List<Object> cliente = new ArrayList<Object>();


Produto pr = new Produto();
ProdutoDAO prdao = new ProdutoDAO();

long time = System.currentTimeMillis();
Timestamp timestamp = new Timestamp(time);



private JTextField textCodigo;

public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
		public void run() {
			try {
				ProdutoView frame = new ProdutoView();
				frame.setVisible(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	});
}

public ProdutoView() {
	addWindowListener(new WindowAdapter() {
		public void windowOpened(WindowEvent arg0) {
			atualizarTabela();
			limpar();
		}
	});
	ImageIcon img = new ImageIcon(this.getClass().getResource("/background-pics-002.jpg").getFile());
	ImageIcon img1 = new ImageIcon(this.getClass().getResource("/tri.png").getFile());
	setTitle("Cadastro Clientes");
	setResizable(false);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
	setBounds(100, 100, 640, 480);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	contentPane.setLayout(null);
	
	JLabel lblProduto = new JLabel("Produto:");
	lblProduto.setHorizontalAlignment(SwingConstants.RIGHT);
	lblProduto.setBounds(20, 183, 59, 20);
	contentPane.add(lblProduto);
	
	textNome = new JTextField();
	textNome.setColumns(10);
	textNome.setBounds(89, 184, 147, 20);
	contentPane.add(textNome);
	
	textCpf = new JTextField();
	textCpf.setColumns(10);
	textCpf.setBounds(89, 245, 147, 20);
	contentPane.add(textCpf);
	
	JLabel lblPreo = new JLabel("Pre\u00E7o:");
	lblPreo.setHorizontalAlignment(SwingConstants.RIGHT);
	lblPreo.setBounds(20, 245, 59, 20);
	contentPane.add(lblPreo);
	
	JButton alterar = new JButton("Alterar");
	alterar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (table.getSelectedRow() != -1){
				try {
					  
					//atribuição dos valores dos campos para o objeto cliente
					pr.setId(Integer.parseInt(textCodigo.getText()));
					pr.setTipo(textNome.getText());
					pr.setPreco(textCpf.getText());
					pr.setQuantidade(Integer.parseInt(textTelefone.getText()));
					// chamada do método de alteração na classe Dao
					prdao.AlterarProduto(pr);
					
	
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				atualizarTabela();
			}
			
			else{
				JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
			}
		}
	});
	alterar.setBackground(SystemColor.controlHighlight);
	alterar.setBounds(336, 374, 110, 21);
	contentPane.add(alterar);
	
	JButton cadastrar = new JButton("Cadastrar");
	cadastrar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			try {
				//atribuição dos valores dos campos para o objeto cliente
				pr.setTipo(textNome.getText());
				pr.setPreco("R$ "+textCpf.getText());
				pr.setQuantidade(Integer.parseInt(textTelefone.getText()));
				
				// chamada do método de cadastro na classe Dao
				prdao.CadastrarProduto(pr);
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
			
			atualizarTabela();
			limpar();

		}
	});
	cadastrar.setBackground(SystemColor.controlHighlight);
	cadastrar.setBounds(89, 341, 147, 21);
	contentPane.add(cadastrar);
	
	JLabel lblQuantidade = new JLabel("Quantidade:");
	lblQuantidade.setHorizontalAlignment(SwingConstants.RIGHT);
	lblQuantidade.setBounds(10, 214, 73, 20);
	contentPane.add(lblQuantidade);
	
	JButton limpar = new JButton("Limpar");
	limpar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			limpar();
		}
	});
	limpar.setBackground(SystemColor.controlHighlight);
	limpar.setBounds(89, 374, 147, 21);
	contentPane.add(limpar);
	
	textTelefone = new JTextField();
	textTelefone.setColumns(10);
	textTelefone.setBounds(89, 215, 147, 20);
	contentPane.add(textTelefone);
	
	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(336, 152, 228, 178);
	contentPane.add(scrollPane);
	
	table = new JTable();
	table.addMouseListener(new MouseAdapter() {
		public void mousePressed(MouseEvent arg0) {
			setCamposFromTabela();
		}
	});
	scrollPane.setViewportView(table);
	table.setModel(new DefaultTableModel(
		new Object[][] {
		},
		new String[] {
			"Codigo", "Tipo", "Preco", "Qts"
		}
	));
	
	JLabel lblCodigo = new JLabel("ID:");
	lblCodigo.setHorizontalAlignment(SwingConstants.RIGHT);
	lblCodigo.setBounds(20, 152, 59, 20);
	contentPane.add(lblCodigo);
	
	textCodigo = new JTextField();
	textCodigo.setEditable(false);
	textCodigo.setColumns(10);
	textCodigo.setBounds(89, 153, 147, 20);
	contentPane.add(textCodigo);
	
	textPNome = new JTextField();
	textPNome.setBounds(336, 341, 228, 20);
	contentPane.add(textPNome);
	textPNome.addCaretListener(new CaretListener() {
		public void caretUpdate(CaretEvent arg0) {
			
			//atualizar a tabela apenas com valores correspondentes aos digitados no campo de busca por nome
			TableRowSorter<TableModel> filtro = null;  
			DefaultTableModel model = (DefaultTableModel) table.getModel();  
			filtro = new TableRowSorter<TableModel>(model);  
			table.setRowSorter(filtro); 
			
			if (textPNome.getText().length() == 0) {
				filtro.setRowFilter(null);
			} else {  
				filtro.setRowFilter(RowFilter.regexFilter("(?i)" + textPNome.getText(), 1));  
			}  
		
		}
	});
	textPNome.setColumns(10);
	
	JLabel lblProduto_1 = new JLabel("Produto");
	lblProduto_1.setFont(new Font("Times New Roman", Font.PLAIN, 30));
	lblProduto_1.setBounds(259, 24, 124, 48);
	contentPane.add(lblProduto_1);
	
	JLabel lblInformaes = new JLabel("Informa\u00E7\u00F5es:");
	lblInformaes.setFont(new Font("Tahoma", Font.PLAIN, 13));
	lblInformaes.setBounds(117, 114, 91, 14);
	contentPane.add(lblInformaes);
	
	JLabel label = new JLabel("..");
	label.setBounds(393, 24, 124, 105);
	label.setIcon(img1);
	contentPane.add(label);
	
	JLabel lblFoto = new JLabel("foto");
	lblFoto.setBounds(0, 0, 634, 451);
	lblFoto.setIcon(img);
	contentPane.add(lblFoto);
}

public void setCamposFromTabela() {
	textCodigo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
	textNome.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
	textCpf.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
	textTelefone.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));
}

public void limpar() {
	textCpf.setText(null);
	textNome.setText(null);
	textTelefone.setText(null);
	try {
		textCodigo.setText(String.valueOf(prdao.RetornarProximoCodigoCliente()));
	} catch (Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage());
	}
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
