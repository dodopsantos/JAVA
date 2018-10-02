package DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import views.MainView;
import beans.Cliente;
import utils.Conexao;



public class ClienteDAO {
	public void CadastrarCliente(Cliente cl) throws SQLException, Exception{
		try{
			String sql = "INSERT INTO cliente (cnpj, nome, email, telefone, cidade, id_estado) VALUES (?,?,?,?,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, cl.getCnpj());
			sqlPrep.setString(2, cl.getNome());
			sqlPrep.setString(3, cl.getEmail());
			sqlPrep.setString(4, cl.getTelefone());
			sqlPrep.setString(5, cl.getCidade());
			sqlPrep.setString(6, cl.getId_estado());
			sqlPrep.execute();
			
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
			
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
	}

	public void AlterarCliente(Cliente cl) throws Exception {
		try{
			String sql = "UPDATE cliente SET cnpj=?,nome=?, email=?, telefone=?, cidade=? WHERE id=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, cl.getCnpj());
			sqlPrep.setString(2, cl.getNome());
			sqlPrep.setString(3, cl.getEmail());
			sqlPrep.setString(4, cl.getTelefone());
     		sqlPrep.setString(5, cl.getCidade());
     		sqlPrep.setInt(6, cl.getId());
			sqlPrep.execute();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void deletarCliente(Cliente cl) throws Exception{
		try{
			String sql = "DELETE FROM cliente WHERE id=? ";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, cl.getId());
			sqlPrep.execute();
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public List<Object> buscarTodos() throws SQLException, Exception{
		List<Object> cliente = new ArrayList<Object>();
		try {
			String sql = "SELECT * FROM cliente";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);
			
			while (rs.next())
			{
				Object[] linha = {rs.getString(1), rs.getString(3), rs.getString(2), rs.getString(5)};
				cliente.add(linha);
			}
			state.close();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return cliente;
	}
	public List<Object> buscar(Cliente cl) throws SQLException, Exception{
		List<Object> bCliente = new ArrayList<Object>();
		try {
			String sql = "SELECT * FROM cliente WHERE id=?";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, cl.getIdTest());
			//java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = sqlPrep.executeQuery();
			
			while (rs.next())
			{
				Object[] linha = {rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)};
				bCliente.add(linha);
			}
			sqlPrep.close();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return bCliente;
	}
	
	
	public int RetornarProximoCodigoCliente() throws Exception {
		try{
			String sql ="SELECT MAX(id)+1 AS id FROM cliente ";
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
