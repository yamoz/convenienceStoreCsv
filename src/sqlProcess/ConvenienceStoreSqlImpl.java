package sqlProcess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import objectToSql.StoreSqlObjBean;

public class ConvenienceStoreSqlImpl implements ConvenienceStoreSql_interface {
	
	public boolean truncateStore() {
		String sql = "truncate table dbo.convenience_store";
		ConnTool connFactory = new ConnTool();
		Connection conn = null;
		try {
			conn = connFactory.getConnection();
			PreparedStatement prepareStmt = conn.prepareStatement(sql);
			prepareStmt.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean createStore(StoreSqlObjBean store) {
		String sql = "INSERT INTO [dbo].[convenience_store]\r\n"
				+ "           ([公司統一編號]\r\n"
				+ "           ,[公司名稱]\r\n"
				+ "           ,[分公司統一編號]\r\n"
				+ "           ,[分公司名稱]\r\n"
				+ "           ,[分公司地址]\r\n"
				+ "           ,[分公司狀態]\r\n"
				+ "           ,[分公司核准設立日期]\r\n"
				+ "           ,[分公司最後核准變更日期])\r\n"
				+ "     VALUES\r\n"
				+ "           (?,?,?,?,?,?,?,?)";
		ConnTool connFactory = new ConnTool();
		Connection conn = null;
		try {
			conn = connFactory.getConnection();
			PreparedStatement prepareStmt = conn.prepareStatement(sql);
			if ( store.getCompanyUniNum() != null ) {
				prepareStmt.setInt(1, store.getCompanyUniNum());
			} else {
				prepareStmt.setNull(1, Types.INTEGER );
			}
			if ( store.getCompanyName() != null ) {
				prepareStmt.setString(2, store.getCompanyName());
			} else {
				prepareStmt.setNull(2, Types.NVARCHAR );
			}
			if ( store.getSubCompanyUniNum() != null ) {
				prepareStmt.setInt(3, store.getSubCompanyUniNum());
			} else {
				prepareStmt.setNull(3, Types.INTEGER);
			}
			if ( store.getSubCompanyName() != null ) {
				prepareStmt.setString(4, store.getSubCompanyName());
			} else {
				prepareStmt.setNull(4, Types.NVARCHAR );
			}
			if ( store.getSubCompanyAddress() != null ) {
				prepareStmt.setString(5, store.getSubCompanyAddress());
			} else {
				prepareStmt.setNull(5, Types.NVARCHAR );
			}
			if ( store.getSubCompanyStatus() != null ) {
				prepareStmt.setInt(6, store.getSubCompanyStatus());
			} else {
				prepareStmt.setNull(6, Types.INTEGER);
			}
			if ( store.getSubCompanyPermmitDate() != null ) {
				prepareStmt.setString(7, store.getSubCompanyPermmitDate());
			} else {
				prepareStmt.setNull(7, Types.NVARCHAR );
			}
			if ( store.getSubCompanyLastPermmitChangeDate() != null ) {
				prepareStmt.setString(8, store.getSubCompanyLastPermmitChangeDate());
			} else {
				prepareStmt.setNull(8, Types.NVARCHAR );
			}
			int CreateCount = prepareStmt.executeUpdate();
			if (CreateCount > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				closeConnection(conn);
			} catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public List<StoreSqlObjBean> getAllStores() {
		ConnTool connFactory = new ConnTool();
		Connection conn = null;
		String sql = "select * from practice.dbo.convenience_store";
		List<StoreSqlObjBean> storeList = new ArrayList<StoreSqlObjBean>();
		try {
			conn = connFactory.getConnection();
			PreparedStatement prepareStmt = conn.prepareStatement(sql);
			ResultSet resultSet = prepareStmt.executeQuery();
			while ( resultSet.next() ) {
				StoreSqlObjBean temp = new StoreSqlObjBean();
				temp.setId(resultSet.getInt("id"));
				temp.setCompanyUniNum(resultSet.getInt("公司統一編號"));
				temp.setCompanyName(resultSet.getString("公司名稱"));
				temp.setSubCompanyUniNum(resultSet.getInt("分公司統一編號"));
				temp.setSubCompanyName(resultSet.getString("分公司名稱"));
				temp.setSubCompanyAddress(resultSet.getString("分公司地址"));
				temp.setSubCompanyStatus(resultSet.getInt("分公司狀態"));
				temp.setSubCompanyPermmitDate(resultSet.getString("分公司核准設立日期"));
				temp.setSubCompanyLastPermmitChangeDate(resultSet.getString("分公司最後核准變更日期"));
				storeList.add(temp);
			}
			return storeList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				closeConnection(conn);
			} catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public StoreSqlObjBean getStoreById(int id) {
		ConnTool connFactory = new ConnTool();
		StoreSqlObjBean storeData = new StoreSqlObjBean();
		Connection conn = null;
		String sql = "select * from convenience_store where id = ?";
		try {
			conn = connFactory.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			storeData.setId(rs.getInt("id"));
			storeData.setCompanyName(rs.getString("公司名稱"));
			storeData.setCompanyUniNum(rs.getInt("公司統一編號"));
			storeData.setSubCompanyAddress(rs.getString("分公司地址"));
			storeData.setSubCompanyLastPermmitChangeDate(rs.getString("分公司最後核准變更日期"));
			storeData.setSubCompanyName(rs.getString("分公司名稱"));
			storeData.setSubCompanyPermmitDate(rs.getString("分公司核准設立日期"));
			storeData.setSubCompanyStatus(rs.getInt("分公司狀態"));
			storeData.setSubCompanyUniNum(rs.getInt("分公司統一編號"));
			return storeData;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public int updateStore(StoreSqlObjBean store) {
		String sqlInsert = "INSERT INTO [dbo].[convenience_store]\r\n"
				+ "           ([公司統一編號]\r\n"
				+ "           ,[公司名稱]\r\n"
				+ "           ,[分公司統一編號]\r\n"
				+ "           ,[分公司名稱]\r\n"
				+ "           ,[分公司地址]\r\n"
				+ "           ,[分公司狀態]\r\n"
				+ "           ,[分公司核准設立日期]\r\n"
				+ "           ,[分公司最後核准變更日期])\r\n"
				+ "     VALUES\r\n"
				+ "           (<公司統一編號, int,>\r\n"
				+ "           ,<公司名稱, nvarchar(max),>\r\n"
				+ "           ,<分公司統一編號, int,>\r\n"
				+ "           ,<分公司名稱, nvarchar(max),>\r\n"
				+ "           ,<分公司地址, nvarchar(max),>\r\n"
				+ "           ,<分公司狀態, int,>\r\n"
				+ "           ,<分公司核准設立日期, varchar(max),>\r\n"
				+ "           ,<分公司最後核准變更日期, varchar(max),>)";
		// sql update 語句。
		String sqlUpdate = "UPDATE [dbo].[convenience_store]\r\n"
				+ "   SET [公司統一編號] = ?"
				+ "      ,[公司名稱] = ?"
				+ "      ,[分公司統一編號] = ?"
				+ "      ,[分公司名稱] = ?"
				+ "      ,[分公司地址] = ?"
				+ "      ,[分公司狀態] = ?"
				+ "      ,[分公司核准設立日期] = ?"
				+ "      ,[分公司最後核准變更日期] = ?"
				+ " WHERE id = ?";
		// new出ConnectionUtil(用於建立連線物件的工廠class)
		ConnTool connFactory = new ConnTool();
		Connection conn = null; // Connection說明詳見ConnectionUtil class檔案
		try {
			conn = connFactory.getConnection(); // 使用建立連線物件的工廠物件，回傳一個確定可用的連線物件(開啟和資料庫的連線)
			PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
			pstmt.setInt(1, store.getCompanyUniNum());
			pstmt.setString(2, store.getCompanyName());
			pstmt.setInt(3, store.getCompanyUniNum());
			pstmt.setString(4, store.getSubCompanyName());
			pstmt.setString(5, store.getSubCompanyAddress());
			pstmt.setInt(6, store.getSubCompanyStatus());
			pstmt.setString(7, store.getSubCompanyPermmitDate());
			pstmt.setString(8, store.getSubCompanyLastPermmitChangeDate());
			pstmt.setInt(9, store.getId());
			int count = pstmt.executeUpdate();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 最後關閉連線(釋放資源有限的 sql I/O通道)
			try {
				if (conn != null) { // 如果連線物件不為null(還存在連線物件)
					if (!conn.isClosed()) { // 如果連線物件是關閉的相反(還開著)
						conn.close(); // 關掉它
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public int deleteStore(int id) {
		String sql = "delete from convenience_store where id = ?";
		ConnTool connFactory = new ConnTool();
		Connection conn = null;
		try {
			conn = connFactory.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean dropAndCreateTable() {
		ConnTool connFactory = new ConnTool();
		String sqlDrop = "drop table if exists convenience_store";
		String sqlCreate = "create table convenience_store (\r\n"
				+ "	id int identity(1,1) primary key,\r\n"
				+ "	公司統一編號 int,\r\n"
				+ "	公司名稱 nvarchar(max),\r\n"
				+ "	分公司統一編號 int,\r\n"
				+ "	分公司名稱 nvarchar(max),\r\n"
				+ "	分公司地址 nvarchar(max),\r\n"
				+ "	分公司狀態 int,\r\n"
				+ "	分公司核准設立日期 varchar(max),\r\n"
				+ "	分公司最後核准變更日期 varchar(max),\r\n"
				+ ")";
		Connection conn = null;
		try {
			conn = connFactory.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sqlDrop);
			stmt.executeUpdate(sqlCreate);
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				conn.setAutoCommit(true);
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	// finally用 關閉連線
	public void closeConnection ( Connection conn ) throws SQLException {
		if ( conn != null ) {
			if ( !conn.isClosed() ) {
				conn.close();
			}
		}
	}
	
	// 依地址查詢超商
	public List<String[]> selectByAddress( String city, String district, String road ) {
		
		ConnTool connFactory = new ConnTool();
		Connection conn = null;
		String sql = "select 公司名稱, 分公司地址 from convenience_store where 分公司地址 like ? order by 分公司地址, 公司名稱";
		city = city.trim();
		district = district.trim();
		road = road.trim();
		String sqlR = "%" + city + "%" + district + "%" + road + "%";

		List<String[]> storeData = new ArrayList<String[]>();
		try {
			conn = connFactory.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sqlR);
			ResultSet rs = pstmt.executeQuery();
			while ( rs.next() ) {
				String[] temp = new String[2];
				temp[0] = rs.getString(1);
				temp[1] = rs.getString(2);
				storeData.add(temp);
			}
			return storeData;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeConnection(conn);
			} catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	// 以縣市查看各大超商家數
	public List<String[]> selectByCity( int cityNo ) {
		ConnTool connFactory = new ConnTool();
		Connection conn = null;
		String sql = "select 公司名稱, count(*) as'家數' from convenience_store where 分公司地址 like ? group by 公司名稱";
		List<String[]> storesByCity = new ArrayList<String[]>();
		try {
			conn = connFactory.getConnection();
			PreparedStatement prepareStmt = conn.prepareStatement(sql);
			
			if ( cityNo > 0 && cityNo < 23 ) {
				if ( cityNo == 1 ) {
					prepareStmt.setString(1, "%臺北%");
				} else if ( cityNo == 2 ) {
					prepareStmt.setString(1, "%新北%");
				} 
			}
			// 建立一個
			ResultSet resultSet = prepareStmt.executeQuery();
			while ( resultSet.next() ) {
				String[] storeData = new String[2];
				storeData[0] = resultSet.getString("公司名稱");
				storeData[1] = resultSet.getString("家數");
				storesByCity.add(storeData);
			}
			return storesByCity;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				closeConnection(conn);
			} catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
