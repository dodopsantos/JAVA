package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import DAOs.ClienteDAO;
import DAOs.ProdutoDAO;
import DAOs.VendaDAO;
import beans.Cliente;
import beans.Produto;
import beans.Venda;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.RowFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.JTextPane;

public class MainView extends JFrame {

	private JPanel contentPane;

	private List<Object> produto = new ArrayList<Object>();
	private List<Object> cliente = new ArrayList<Object>();
	
	VendaView VV = new VendaView();
	VendaDAO vedao = new VendaDAO();
	Venda ve = new Venda();
	ExcluirCliente EC = new ExcluirCliente();
	ExcluirProduto EP = new ExcluirProduto();
	Produto pro = new Produto();
	ProdutoDAO prodao = new ProdutoDAO();
	CadastroClienteView CC = new CadastroClienteView();
	ClienteDAO cldao = new ClienteDAO();
	Cliente cl = new Cliente();
	ProdutoView PV = new ProdutoView();
	
	/**
	 * Launch the application.
	 */
	int a,b,c,d,f,e,g;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
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

	ImageIcon img = new ImageIcon(this.getClass().getResource("/bella.png").getFile());
	ImageIcon img1 = new ImageIcon(this.getClass().getResource("/fundo-de-design.jpg").getFile());
	private JTable table;
	private JTextField tfNome;
	/**
	 * @wbp.nonvisual location=-28,99
	 */
	private final JTextField textField = new JTextField();
	private JTable table_1;
	private JTextField tfNomeC;
	private JTextField tfID_1;
	private JTextField tfID_2;
	private JTextField tfQt;
	private JTextField textCode;
	private JTextField tfValor;
	public MainView() {
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent arg0) {
				atualizarTabela();
				atualizarTabelaCliente();
				limpar();
			}
		});
	
		textField.setColumns(10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 640, 480);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnCliente = new JMenu("Cliente");
		menuBar.add(mnCliente);
		
		JMenuItem mntmCadastrar = new JMenuItem("Cadastrar");
		mntmCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CC.setVisible(true);
			}
		});
		
		mnCliente.add(mntmCadastrar);
		
		JMenuItem mntmExcluir = new JMenuItem("Excluir");
		mntmExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EC.setVisible(true);
			}
		});
		mnCliente.add(mntmExcluir);
		
		JMenu mnProduto = new JMenu("Produto");
		menuBar.add(mnProduto);
		
		JMenuItem mntmCadastrar_1 = new JMenuItem("Cadastrar");
		mntmCadastrar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PV.setVisible(true);
			}
		});
		mntmCadastrar_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		mnProduto.add(mntmCadastrar_1);
		
		JMenuItem mntmExcluirp = new JMenuItem("Excluir");
		mntmExcluirp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EP.setVisible(true);
			}
		});
		mnProduto.add(mntmExcluirp);
		
		JMenu mnNewMenu = new JMenu("Venda");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmEfetuar = new JMenuItem("Estoque");
		mntmEfetuar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		mnNewMenu.add(mntmEfetuar);
		
		JMenuItem mntmHistorico = new JMenuItem("Hist\u00F3rico");
		mntmHistorico.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				VV.atualizarTabela();
				VV.setVisible(true);
			}
		});
		mnNewMenu.add(mntmHistorico);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.getContentPane().setBackground(Color.white);
		JLabel lblBela = new JLabel("bela");
		lblBela.setIcon(img);
		lblBela.setBounds(407, 11, 120, 99);
		contentPane.add(lblBela);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(358, 110, 219, 232);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				setCamposFromTabela2();
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID","Produto", "Pre\u00E7o", "Qt."
			}
		));
		
		tfNome = new JTextField();
		tfNome.setBounds(358, 354, 219, 20);
		contentPane.add(tfNome);
		tfNome.setColumns(10);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(62, 110, 219, 232);
		contentPane.add(scrollPane_1);
		
		table_1 = new JTable();
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				setCamposFromTabela1();
			}
		});
		scrollPane_1.setViewportView(table_1);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nome", "CNPJ", "Telefone"
			}
		));
		
		tfNomeC = new JTextField();
		tfNomeC.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				TableRowSorter<TableModel> filtro = null;  
				DefaultTableModel model = (DefaultTableModel) table_1.getModel();  
				filtro = new TableRowSorter<TableModel>(model);  
				table_1.setRowSorter(filtro); 
				
				if (tfNomeC.getText().length() == 0) {
					filtro.setRowFilter(null);
				} else {  
					
					filtro.setRowFilter(RowFilter.regexFilter("(?i)" + tfNomeC.getText(), 1));  
				}  
			}
			
		});
		tfNomeC.setBounds(62, 354, 219, 20);
		contentPane.add(tfNomeC);
		tfNomeC.setColumns(10);
		
		JLabel lblBellaFlor = new JLabel("Bella - Flor");
		lblBellaFlor.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblBellaFlor.setBounds(124, 50, 120, 14);
		contentPane.add(lblBellaFlor);
		
		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				atualizarTabela();
				atualizarTabelaCliente();
			}
		});
		btnAtualizar.setBounds(431, 373, 89, 23);
		contentPane.add(btnAtualizar);
		tfID_2 = new JTextField();
		tfQt = new JTextField();
		textCode = new JTextField();
		tfValor = new JTextField();
		tfID_1 = new JTextField();
		tfID_1.setEditable(false);
		tfID_1.setVisible(true);
		tfID_1.setBounds(291, 132, 50, 20);
		contentPane.add(tfID_1);
		tfID_1.setColumns(10);
		
		tfQt.setEditable(false);
		tfQt.setVisible(false);
		tfQt.setBounds(528, 79, 86, 20);
		contentPane.add(tfQt);
		tfQt.setColumns(10);
		
		
		textCode.setEditable(false);
		textCode.setBounds(574, 0, 50, 20);
		contentPane.add(textCode);
		textCode.setColumns(10);
		
		tfValor.setVisible(false);
		tfValor.setBounds(528, 31, 86, 20);
		contentPane.add(tfValor);
		tfValor.setColumns(10);
		
		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
			}});
		btnConsultar.setBounds(129, 373, 89, 23);
		contentPane.add(btnConsultar);
		
		
		tfID_2.setEditable(false);
		tfID_2.setVisible(true);
		tfID_2.setBounds(291, 163, 49, 20);
		contentPane.add(tfID_2);
		tfID_2.setColumns(10);

		
		JButton btnVender = new JButton("Vender");
		btnVender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String valor;
				String [] nom = new String[9999];
				String msg = "";
				
				valor = tfValor.getText();
				nom = valor.split("");
				
				for (int i = 0; i < nom.length; i++) {
					if (nom[i].equals("0")||nom[i].equals("1")||nom[i].equals("2")|| nom[i].equals("3")||nom[i].equals("4")||nom[i].equals("5") || nom[i].equals("6")||nom[i].equals("7")||nom[i].equals("8") || nom[i].equals("9")) {
						
						msg += nom[i];
					}
				}
				
				a = Integer.parseInt(tfID_1.getText());
				b = Integer.parseInt(tfID_2.getText());
			    c = Integer.parseInt(tfQt.getText());
				d = Integer.parseInt(textCode.getText());
				e = Integer.parseInt(msg);
				
				
				if (a >= 0 && b >= 0) {
					f = Integer.parseInt(JOptionPane.showInputDialog("Quantidade do Produto:"));
					if(f <= c ) {
						e = e*f;
						try {
							ve.setId_cliente(a);
							ve.setId_produto(b);
							ve.setQuantidade(f);
							ve.setValor(String.valueOf("R$ "+e));
							
							vedao.CadastrarVenda(ve);
							
							pro.setQuantidade(c-f);
							pro.setIdTest(b);
							vedao.AlterarVenda(pro);
							
							atualizarTabela();
							limpar();
							
							g = Integer.parseInt(JOptionPane.showInputDialog("Deseja adicionar mais produtos? \n (1 - Sim | 2 - Não)"));
							if(g == 2){
							JOptionPane.showMessageDialog(null, "Venda realizada com sucesso!");
							}else if (g == 1) {
								setCamposFromTabela1();
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else {
						JOptionPane.showMessageDialog(null, "Não há "+f+" unidades em estoque. (Qt.: "+c+" )");
					}
				}else {
					JOptionPane.showMessageDialog(null, "Seleciona o Cliente e o Produto!");
				}
			}
		});
		btnVender.setBounds(280, 194, 78, 23);
		contentPane.add(btnVender);
		
		

		
		JLabel lblIak = new JLabel("iak");
		lblIak.setBounds(0, 0, 624, 420);
		lblIak.setIcon(img1);
		contentPane.add(lblIak);
		
		
		
		
		tfNome.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				TableRowSorter<TableModel> filtro = null;  
				DefaultTableModel model = (DefaultTableModel) table.getModel();  
				filtro = new TableRowSorter<TableModel>(model);  
				table.setRowSorter(filtro); 
				
				if (tfNome.getText().length() == 0) {
					filtro.setRowFilter(null);
				} else {  
					
					filtro.setRowFilter(RowFilter.regexFilter("(?i)" + tfNome.getText(), 1));  
				}  
			}
		});
		
	}
	public void setCamposFromTabela1() {
		tfID_1.setText(String.valueOf(table_1.getValueAt(table_1.getSelectedRow(), 0)));
		cl.setIdTest(Integer.parseInt(tfID_1.getText()));
	}
	public void setCamposFromTabela2() {
		tfID_2.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
		tfQt.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));
		tfValor.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
		pro.setIdTest(Integer.parseInt(tfID_2.getText()));
		
	}
	public void atualizarTabela() {
		try {
			produto = prodao.buscarTodos();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
		for (int x=0; x!=produto.size(); x++)
			{
				model.addRow((Object[]) produto.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	public void atualizarTabelaCliente() {
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
	public void limpar() {
		tfID_1.setText(null);
		tfID_2.setText(null);
		tfNome.setText(null);
		tfNomeC.setText(null);
		tfQt.setText(null);
		tfValor.setText(null);
		
		try {
			textCode.setText(String.valueOf(vedao.RetornarProximoCodigoVenda()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
