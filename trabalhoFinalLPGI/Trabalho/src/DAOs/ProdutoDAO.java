package DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import beans.Produto;
import utils.Conexao;

public class ProdutoDAO {
	public void CadastrarProduto(Produto pro) throws SQLException, Exception{
		try{
			String sql = "INSERT INTO produto (tipo, preco, quantidade) VALUES (?,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, pro.getTipo());
			sqlPrep.setString(2, pro.getPreco());
			sqlPrep.setInt(3, pro.getQuantidade());
			sqlPrep.execute();
			
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
			
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
	}

	public void AlterarProduto(Produto pro) throws Exception {
		try{
			String sql = "UPDATE produto SET tipo=?, preco=?, quantidade=? WHERE id=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, pro.getTipo());
			sqlPrep.setString(2, pro.getPreco());
			sqlPrep.setInt(3, pro.getQuantidade());
			sqlPrep.setInt(4, pro.getId());
			sqlPrep.execute();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void deletarProduto(Produto pro) throws Exception{
		try{
			String sql = "DELETE FROM produto WHERE id=? ";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, pro.getId());
			sqlPrep.execute();
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public List<Object> buscarTodos() throws SQLException, Exception{
		List<Object> produto = new ArrayList<Object>();
		try {
			String sql = "SELECT * FROM produto";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);
			
			while (rs.next())
			{
				Object[] linha = {rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4)};
				produto.add(linha);
			}
			state.close();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return produto;
	}
	
	public int RetornarProximoCodigoCliente() throws Exception {
		try{
			String sql ="SELECT MAX(id)+1 AS id FROM produto ";
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
