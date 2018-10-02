package DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import beans.Produto;
import beans.Venda;
import utils.Conexao;

public class VendaDAO {
	public void CadastrarVenda(Venda ve) throws SQLException, Exception{
		try{
			String sql = "INSERT INTO venda (id_produto, id_cliente, quantidade, valor) VALUES (?,?,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, ve.getId_produto());
			sqlPrep.setInt(2, ve.getId_cliente());
			sqlPrep.setInt(3, ve.getQuantidade());
			sqlPrep.setString(4, ve.getValor());
			sqlPrep.execute();
			
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
			
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
	}

	public void AlterarVenda(Produto pro) throws Exception {
		try{
			String sql = "UPDATE produto SET quantidade=? WHERE id=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, pro.getQuantidade());
			sqlPrep.setInt(2, pro.getIdTest());
			sqlPrep.execute();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public List<Object> buscarTodos() throws SQLException, Exception{
		List<Object> venda = new ArrayList<Object>();
		try {
			String sql = "SELECT * FROM venda";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);
			
			while (rs.next())
			{
				Object[] linha = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)};
				venda.add(linha);
			}
			state.close();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return venda;
	}
//	public List<Object> buscar(Cliente cl) throws SQLException, Exception{
//		List<Object> bCliente = new ArrayList<Object>();
//		try {
//			String sql = "SELECT * FROM cliente WHERE id=?";
//			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
//			sqlPrep.setInt(1, cl.getIdTest());
//			//java.sql.Statement state = Conexao.conectar().createStatement();
//			ResultSet rs = sqlPrep.executeQuery();
//			
//			while (rs.next())
//			{
//				Object[] linha = {rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)};
//				bCliente.add(linha);
//			}
//			sqlPrep.close();
//			
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(null, e.getMessage());
//		}
//		return bCliente;
//	}
//	
	
	public int RetornarProximoCodigoVenda() throws Exception {
		try{
			String sql ="SELECT MAX(id)+1 AS id FROM venda ";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			ResultSet rs = sqlPrep.executeQuery();
			if (rs.next()){
				return rs.getInt("id");
			}else{
				return 1;
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 1;
		}
	}
}
