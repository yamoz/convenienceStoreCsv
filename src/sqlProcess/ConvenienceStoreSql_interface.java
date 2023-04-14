package sqlProcess;

import java.util.List;

import objectToSql.StoreSqlObjBean;

public interface ConvenienceStoreSql_interface {
	// create
	boolean createStore(StoreSqlObjBean store);
	
	// Read=>ALL, ByID
	List<StoreSqlObjBean> getAllStores();
	StoreSqlObjBean getStoreById( int id );
	
	// update
	int updateStore(StoreSqlObjBean store);
	
	// delete by id
	int deleteStore( int id );
}
