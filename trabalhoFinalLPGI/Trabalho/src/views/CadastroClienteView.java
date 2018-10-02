package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DAOs.CidadeDAO;
import DAOs.ClienteDAO;
import DAOs.EnderecoDAO;
import beans.Cidade;
import beans.Cliente;
import beans.Endereco;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JScrollPane;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class CadastroClienteView extends JFrame {

	private JPanel contentPane;
	private JTextField tfCNPJ;
	private JTextField tfNome;
	private JTextField tfEmail;
	private JTextField tfTelefone;
	private JTextField tfCidade;
	private JTextField tfRua;
	private JTextField tfNumero;
	
	private List<Object> cliente = new ArrayList<Object>();

	Cliente cl = new Cliente();
	ClienteDAO cldao = new ClienteDAO();
	Cidade cd = new Cidade();
	CidadeDAO cddao = new CidadeDAO();
	Endereco en = new Endereco();
	EnderecoDAO endao = new EnderecoDAO();
	
	private JTextField textId;
	private JTable table;
	private JTable table_1;
	private JTextField tfID;
	private JTextField tfBusca;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadastroClienteView frame = new CadastroClienteView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CadastroClienteView() {
		addWindowListener(new WindowAdapter() {
		public void windowOpened(WindowEvent arg0) {
			atualizarTabela();
			limpar();
		}
	});
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		ImageIcon img = new ImageIcon(this.getClass().getResource("/background-pics-002.jpg").getFile());
		ImageIcon img1 = new ImageIcon(this.getClass().getResource("/cli3.png").getFile());
		contentPane.setLayout(null);
		
		JLabel lblCadastro = new JLabel("Cadastro");
		lblCadastro.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblCadastro.setBounds(254, 11, 148, 66);
		contentPane.add(lblCadastro);
		
		JLabel lblCnpj = new JLabel("CNPJ: ");
		lblCnpj.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCnpj.setBounds(49, 137, 46, 14);
		contentPane.add(lblCnpj);
		
		tfCNPJ = new JTextField();
		tfCNPJ.setBounds(105, 135, 148, 20);
		contentPane.add(tfCNPJ);
		tfCNPJ.setColumns(10);
		
		tfNome = new JTextField();
		tfNome.setBounds(105, 166, 148, 20);
		contentPane.add(tfNome);
		tfNome.setColumns(10);
		
		tfEmail = new JTextField();
		tfEmail.setBounds(105, 197, 148, 20);
		contentPane.add(tfEmail);
		tfEmail.setColumns(10);
		
		tfTelefone = new JTextField();
		tfTelefone.setBounds(105, 226, 148, 20);
		contentPane.add(tfTelefone);
		tfTelefone.setColumns(10);
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNome.setBounds(49, 169, 46, 14);
		contentPane.add(lblNome);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblEmail.setBounds(49, 200, 46, 14);
		contentPane.add(lblEmail);
		
		JLabel lblTel = new JLabel("Tel.:");
		lblTel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTel.setBounds(49, 229, 46, 14);
		contentPane.add(lblTel);
		
		JLabel lblCidade = new JLabel("Estado:");
		lblCidade.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCidade.setBounds(49, 259, 46, 14);
		contentPane.add(lblCidade);
		
		JComboBox comboBox = new JComboBox();
		
		comboBox.setModel(new DefaultComboBoxModel(new String[] {" -- Escolha", "Acre", "Alagoas", "Amap\u00E1", "Amazonas", "Bahia", "Cear\u00E1", "Distrito Federal", "Esp\u00EDrito Santo", "Goi\u00E1s", "Maranh\u00E3o", "Mato Grosso", "Mato Grosso do Sul", "Minas Gerais", "Par\u00E1", "Para\u00EDba", "Paran\u00E1", "Pernambuco", "Piau\u00ED", "Rio de janeiro", "Rio Grande do Norte", "Rio Grande do Sul", "Rond\u00F4nia", "Roraima", "Santa Catarina", "S\u00E3o Paulo", "Sergipe", "Tocantins"}));
		comboBox.setBounds(105, 257, 148, 20);
		contentPane.add(comboBox);
		
		tfCidade = new JTextField();
		tfCidade.setBounds(105, 283, 148, 20);
		contentPane.add(tfCidade);
		tfCidade.setColumns(10);
		
		JLabel lblCidade_1 = new JLabel("Cidade:");
		lblCidade_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCidade_1.setBounds(49, 286, 46, 14);
		contentPane.add(lblCidade_1);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					//atribuição dos valores dos campos para o objeto cliente
					cl.setCnpj(tfCNPJ.getText());
					cl.setNome(tfNome.getText());
					cl.setEmail(tfEmail.getText());
					cl.setTelefone(tfTelefone.getText());
					cl.setCidade(tfCidade.getText());
					en.setNumero(Integer.parseInt(tfNumero.getText()));
					en.setRua(tfRua.getText());
					if(comboBox.getSelectedIndex() == 0) {
						cl.setId_estado(null);
					}else if (comboBox.getSelectedIndex() == 1) {
						cl.setId_estado("Acre");
					}else if (comboBox.getSelectedIndex() == 2) {
						cl.setId_estado("Alagoas");
					}else if (comboBox.getSelectedIndex() == 3) {
						cl.setId_estado("Amazonas");
					}else if (comboBox.getSelectedIndex() == 4) {
						cl.setId_estado("Amapá");
					}else if (comboBox.getSelectedIndex() == 5) {
						cl.setId_estado("Bahia");
					}else if (comboBox.getSelectedIndex() == 6) {
						cl.setId_estado("Ceará");
					}else if (comboBox.getSelectedIndex() == 7) {
						cl.setId_estado("Distrito Federal");
					}else if (comboBox.getSelectedIndex() == 8) {
						cl.setId_estado("Espírito Santo");
					}else if (comboBox.getSelectedIndex() == 9) {
						cl.setId_estado("Goiás");
					}else if (comboBox.getSelectedIndex() == 10) {
						cl.setId_estado("Maranhão");
					}else if (comboBox.getSelectedIndex() == 11) {
						cl.setId_estado("Minas Gerais");
					}else if (comboBox.getSelectedIndex() == 12) {
						cl.setId_estado("Mato Grosso do Sul");
					}else if (comboBox.getSelectedIndex() == 13) {
						cl.setId_estado("Mato Grosso");
					}else if (comboBox.getSelectedIndex() == 14) {
						cl.setId_estado("Pará");
					}else if (comboBox.getSelectedIndex() == 15) {
						cl.setId_estado("Paraíba");
					}else if (comboBox.getSelectedIndex() == 16) {
						cl.setId_estado("Pernambuco");
					}else if (comboBox.getSelectedIndex() == 17) {
						cl.setId_estado("Piauí");
					}else if (comboBox.getSelectedIndex() == 18) {
						cl.setId_estado("Paraná");
					}else if (comboBox.getSelectedIndex() == 19) {
						cl.setId_estado("Rio de Janeiro");
					}else if (comboBox.getSelectedIndex() == 20) {
						cl.setId_estado("Rio Grande do Norte");
					}else if (comboBox.getSelectedIndex() == 21) {
						cl.setId_estado("Rondônia");
					}else if (comboBox.getSelectedIndex() == 22) {
						cl.setId_estado("Roraima");
					}else if (comboBox.getSelectedIndex() == 23) {
						cl.setId_estado("Rio Grande do Sul");
					}else if (comboBox.getSelectedIndex() == 24) {
						cl.setId_estado("Santa Catarina");
					}else if (comboBox.getSelectedIndex() == 25) {
						cl.setId_estado("Sergipe");
					}else if (comboBox.getSelectedIndex() == 26) {
						cl.setId_estado("São Paulo");
					}else if (comboBox.getSelectedIndex() == 27) {
						cl.setId_estado("Tocantins");
					}
					
					// chamada do método de cadastro na classe Dao
					
					cldao.CadastrarCliente(cl);
					endao.CadastrarCliente(en);
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				atualizarTabela();
				limpar();

			}
		});
		btnCadastrar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCadastrar.setBounds(126, 386, 109, 23);
		contentPane.add(btnCadastrar);
		
		tfNumero = new JTextField();
		tfNumero.setBounds(105, 314, 148, 20);
		contentPane.add(tfNumero);
		tfNumero.setColumns(10);
		
		tfRua = new JTextField();
		tfRua.setBounds(105, 345, 148, 20);
		contentPane.add(tfRua);
		tfRua.setColumns(10);
		
		JLabel lblNum = new JLabel("Num.:");
		lblNum.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNum.setBounds(49, 316, 46, 14);
		contentPane.add(lblNum);
		
		JLabel lblRua = new JLabel("Rua:");
		lblRua.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRua.setBounds(49, 347, 46, 14);
		contentPane.add(lblRua);
		
		JLabel lblInformaes = new JLabel("Informa\u00E7\u00F5es");
		lblInformaes.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblInformaes.setBounds(133, 93, 89, 14);
		contentPane.add(lblInformaes);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(295, 137, 301, 228);
		contentPane.add(scrollPane);

		table_1 = new JTable();
		table_1.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent arg0) {
				setCamposFromTabela();
			}
		});
		scrollPane.setViewportView(table_1);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nome", "CNPJ", "Telefone"
			}
		));
		
		JLabel lblCliente = new JLabel("Cliente");
		lblCliente.setBounds(476, 11, 103, 96);
		lblCliente.setIcon(img1);
		contentPane.add(lblCliente);
		
		JButton btnAlterar = new JButton("Alterar");
		btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table_1.getSelectedRow() != -1){
					try {
						  
						//atribuição dos valores dos campos para o objeto cliente
						cl.setId(Integer.parseInt(tfID.getText()));
						cl.setCnpj(tfCNPJ.getText());
						cl.setNome(tfNome.getText());
						cl.setEmail(tfEmail.getText());
						cl.setTelefone(tfTelefone.getText());
						cl.setCidade(tfCidade.getText());
//						en.setNumero(Integer.parseInt(tfNumero.getText()));
//					    en.setRua(tfRua.getText());
						
						
						// chamada do método de alteração na classe Dao
						cldao.AlterarCliente(cl);
						
		
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
		btnAlterar.setBounds(295, 387, 89, 23);
		contentPane.add(btnAlterar);
		
		tfID = new JTextField();
		tfID.setEditable(false);
		tfID.setBounds(49, 106, 46, 20);
		contentPane.add(tfID);
		tfID.setColumns(10);
		
		tfBusca = new JTextField();
		tfBusca.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				//atualizar a tabela apenas com valores correspondentes aos digitados no campo de busca por nome
				TableRowSorter<TableModel> filtro = null;  
				DefaultTableModel model = (DefaultTableModel) table_1.getModel();  
				filtro = new TableRowSorter<TableModel>(model);  
				table_1.setRowSorter(filtro); 
				
				if (tfBusca.getText().length() == 0) {
					filtro.setRowFilter(null);
				} else {  
					filtro.setRowFilter(RowFilter.regexFilter("(?i)" + tfBusca.getText(), 1));  
				}  
			}
		});
		tfBusca.setBounds(295, 366, 301, 20);
		contentPane.add(tfBusca);
		tfBusca.setColumns(10);
		
		JLabel lblImagem = new JLabel("imagem");
		lblImagem.setForeground(Color.WHITE);
		lblImagem.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblImagem.setBounds(0, 0, 624, 441);
		lblImagem.setIcon(img);
		contentPane.add(lblImagem);
		
	}
	public void setCamposFromTabela() {
		tfID.setText(String.valueOf(table_1.getValueAt(table_1.getSelectedRow(), 0)));
		tfCNPJ.setText(String.valueOf(table_1.getValueAt(table_1.getSelectedRow(), 2)));
		tfNome.setText(String.valueOf(table_1.getValueAt(table_1.getSelectedRow(), 1)));
//		tfEmail.setText(String.valueOf(table_1.getValueAt(table_1.getSelectedRow(), 2)));
		tfTelefone.setText(String.valueOf(table_1.getValueAt(table_1.getSelectedRow(),3)));
//		tfCidade.setText(String.valueOf(table_1.getValueAt(table_1.getSelectedRow(),5)));
//		tfNumero.setText(String.valueOf(table_1.getValueAt(table_1.getSelectedRow(),6)));
//		tfRua.setText(String.valueOf(table_1.getValueAt(table_1.getSelectedRow(),7)));
	}
	public void limpar() {
		tfCNPJ.setText(null);
		tfCidade.setText(null);
		tfEmail.setText(null);
		tfNome.setText(null);
		tfNumero.setText(null);
		tfRua.setText(null);
		tfTelefone.setText(null);
		try {
			tfID.setText(String.valueOf(cldao.RetornarProximoCodigoCliente()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	public void atualizarTabela() {
		try {
			cliente = cldao.buscarTodos();
			DefaultTableModel model = (DefaultTableModel) table_1.getModel();
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
