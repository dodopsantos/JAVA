package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DAOs.VendaDAO;
import beans.Venda;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class VendaView extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	private List<Object> venda = new ArrayList<Object>();
	VendaDAO vedao = new VendaDAO();
	Venda ve = new Venda();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VendaView frame = new VendaView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	ImageIcon img = new ImageIcon(this.getClass().getResource("/background-pics-002.jpg").getFile());
	ImageIcon img1 = new ImageIcon(this.getClass().getResource("/easy.png").getFile());
	private JTable table;
	/**
	 * Create the frame.
	 */
	public VendaView() {
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
		
		JLabel lblVend = new JLabel("Vend");
		lblVend.setIcon(img1);
		lblVend.setBounds(439, 11, 125, 166);
		contentPane.add(lblVend);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(82, 182, 460, 217);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "ID_Produto", "ID_Cliente", "Quantidade", "Valor"
			}
		));
		scrollPane.setViewportView(table);
		
		JLabel lblHistricoDeVenda = new JLabel("Hist\u00F3rico de Venda");
		lblHistricoDeVenda.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblHistricoDeVenda.setBounds(196, 64, 212, 54);
		contentPane.add(lblHistricoDeVenda);
		
		JLabel lblFundo = new JLabel("Fundo");
		lblFundo.setIcon(img);
		lblFundo.setBounds(0, 0, 624, 441);
		contentPane.add(lblFundo);
	}
	public void atualizarTabela() {
		try {
			venda = vedao.buscarTodos();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
		for (int x=0; x!=venda.size(); x++)
			{
				model.addRow((Object[]) venda.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
