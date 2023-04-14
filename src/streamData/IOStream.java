package streamData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import execute.barObjBean;
import objectToSql.StoreSqlObjBean;
import sqlProcess.ConnTool;
import sqlProcess.ConvenienceStoreSqlImpl;

public class IOStream {
	public int BatchInsertFromUrl( String inputUrlStr ) {
		try {
//			URL url = new URL("https://quality.data.gov.tw/dq_download_csv.php?nid=32086&md5_url=70d75bd612378b4626495a70b7f1777c");
			URL url = new URL(inputUrlStr);
			InputStream openStream = url.openStream();
			InputStreamReader ISreader = new InputStreamReader(openStream);
			BufferedReader buffReader = new BufferedReader(ISreader);
			String data = "";
			ConvenienceStoreSqlImpl StoreSql = new ConvenienceStoreSqlImpl();
			StoreSql.dropAndCreateTable();
			buffReader.readLine(); // 讀掉第一行表頭
			int sqlCount = 1;
			Set<Integer> errorList = new HashSet<>();
			while ( ( data = buffReader.readLine() ) != null ) { // && sqlCount < 9300
				StoreSqlObjBean convenienceStore = new StoreSqlObjBean();
				// 識別讀入的資料型別
				// 識別null
				String[] split = data.split(",");
				int arrLen = split.length;
				if ( arrLen != 8 ) {
					errorList.add(sqlCount); // 記錄錯誤筆數(以欄位格數不符判斷)
				}
				for ( int i = 0; i < arrLen; i++ ) {
					String trimedStr = split[i].trim();
					// set store_obj to correct value
					switch ( i ) {
						case 0: // 統編 轉成int
							if ( trimedStr.equals("") ) {
								convenienceStore.setCompanyUniNum(null);
							} else {
								try {
									convenienceStore.setCompanyUniNum(Integer.valueOf(trimedStr));
								} catch ( NumberFormatException e ) {
									; // ignore error
								}
							}
							break;
						case 1: // 主公司名
							if ( trimedStr.equals("") ) {
								convenienceStore.setCompanyName(null);
							} else {
								convenienceStore.setCompanyName(trimedStr);
							}
							break;
						case 2: // 分公司統編
							if ( trimedStr.equals("") ) {
								convenienceStore.setSubCompanyUniNum(null);
							} else {
								try {
									convenienceStore.setSubCompanyUniNum(Integer.valueOf(trimedStr));
								} catch ( NumberFormatException e ) {
									; // ignore error
								}
							}
							break;
						case 3: // 分公司名
							if ( trimedStr.equals("") ) {
								convenienceStore.setSubCompanyName(null);
							} else {
								convenienceStore.setSubCompanyName(trimedStr);
							}
							break;
						case 4: // 分公司地址
							if ( trimedStr.equals("") ) {
								convenienceStore.setSubCompanyAddress(null);
							} else {
								convenienceStore.setSubCompanyAddress(trimedStr);
							}
							break;
						case 5: // 分公司狀態
							if ( trimedStr.equals("") ) {
								convenienceStore.setSubCompanyStatus(null);
							} else {
								try {
									convenienceStore.setSubCompanyStatus(Integer.valueOf(trimedStr));
								} catch ( NumberFormatException e ) {
									; // ignore error
								}
							}
							break;
						case 6: // 分公司核准設立日期
							if ( trimedStr.equals("") ) {
								convenienceStore.setSubCompanyPermmitDate(null);
							} else {
								convenienceStore.setSubCompanyPermmitDate(trimedStr);
							}
							break;
						case 7: // 分公司最後核准變更日期
							if ( trimedStr.equals("") ) {
								convenienceStore.setSubCompanyLastPermmitChangeDate(null);
							} else {
								convenienceStore.setSubCompanyLastPermmitChangeDate(trimedStr);
							}
							break;
					}
				}
				// insert into sql by storeObj
				if ( StoreSql.createStore(convenienceStore) ) {
					System.out.printf("成功新增%d筆。\n", sqlCount);
					sqlCount++;
				}
			}
			for ( int err:errorList ) {
				System.out.printf("第%d次輸入可能有誤。\n", err);
			}
			return sqlCount;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1; // if error occur and interrupted
	}
	// 選取全部資料表至csv
	public void selectAllStoreToCsv() {
		// file I/O
		Scanner sc = new Scanner(System.in);
		System.out.println("請輸入檔名:");
		String fileRouteName = sc.nextLine();
//		sc.close();
		File storeCsv = new File(fileRouteName);
		try {
			if ( !storeCsv.exists() ) {
				storeCsv.getParentFile().mkdirs();
				storeCsv.createNewFile();
			} else {
				storeCsv.delete();
				storeCsv.getParentFile().mkdirs();
				storeCsv.createNewFile();
				System.out.println( "正在重建並複寫: " + fileRouteName );
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try(FileWriter fWriter = new FileWriter(storeCsv, true);
//				BufferedWriter buffWriter = new BufferedWriter(fWriter) ) {
		try (FileOutputStream fos = new FileOutputStream(storeCsv);
				OutputStreamWriter buffWriter = new OutputStreamWriter(fos, StandardCharsets.UTF_8)){
			// preview and export to csv
			ConvenienceStoreSqlImpl selector = new ConvenienceStoreSqlImpl();
			List<StoreSqlObjBean> storeList = selector.getAllStores();
			for ( StoreSqlObjBean store:storeList ) {
//			System.out.printf("%d,", store.getId());
				System.out.printf("%d,",store.getId());
				if ( store.getCompanyUniNum() == null ) {
					buffWriter.write(",");
					buffWriter.flush();
					System.out.print(",");
				} else {
					buffWriter.write(store.getCompanyUniNum() + ",");
					buffWriter.flush();
					System.out.printf("%d,", store.getCompanyUniNum());
				}
				if ( store.getCompanyName() == null ) {
					buffWriter.write(",");
					buffWriter.flush();
					System.out.print(",");
				} else {
					buffWriter.write(store.getCompanyName() + ",");
					buffWriter.flush();
					System.out.printf("%s,", store.getCompanyName());
				}
				if ( store.getSubCompanyUniNum() == null ) {
					buffWriter.write(",");
					buffWriter.flush();
					System.out.print(",");
				} else {
					buffWriter.write(store.getSubCompanyUniNum() + ",");
					buffWriter.flush();
					System.out.printf("%d,", store.getSubCompanyUniNum());
				}
				if ( store.getSubCompanyName() == null ) {
					buffWriter.write(",");
					buffWriter.flush();
					System.out.print(",");
				} else {
					buffWriter.write(store.getSubCompanyName() + ",");
					buffWriter.flush();
					System.out.printf("%s,", store.getSubCompanyName());
				}
				if ( store.getSubCompanyAddress() == null ) {
					buffWriter.write(",");
					buffWriter.flush();
					System.out.print(",");
				} else {
					buffWriter.write(store.getSubCompanyAddress() + ",");
					buffWriter.flush();
					System.out.printf("%s,", store.getSubCompanyAddress());
				}
				if ( store.getSubCompanyStatus() == null ) {
					buffWriter.write(",");
					buffWriter.flush();
					System.out.print(",");
				} else {
					buffWriter.write(store.getSubCompanyStatus() + ",");
					buffWriter.flush();
					System.out.printf("%d,", store.getSubCompanyStatus());
				}
				if ( store.getSubCompanyPermmitDate() == null ) {
					buffWriter.write(",");
					buffWriter.flush();
					System.out.print(",");
				} else {
					buffWriter.write(store.getSubCompanyPermmitDate() + ",");
					buffWriter.flush();
					System.out.printf("%s,", store.getSubCompanyPermmitDate());
				}
				if ( store.getSubCompanyLastPermmitChangeDate() == null ) {
					buffWriter.write("");
					buffWriter.flush();
					System.out.print("");
				} else {
					buffWriter.write(store.getSubCompanyLastPermmitChangeDate());
					buffWriter.flush();
					System.out.printf("%s", store.getSubCompanyLastPermmitChangeDate());
				}
				buffWriter.write("\n");
				buffWriter.flush();
				System.out.print("\n");
			}
			System.out.println("檔案已寫入至: " + fileRouteName );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		sc.next(); // 用於移到下一個scan token，避免一直停在error token上，造成無窮迴圈。???放這裡?
	}
	
	// 顯示table最後創建日期
	public String showTableCreateDate() {
		ConnTool connFactory = new ConnTool();
		Connection conn = null;
		String sql = "SELECT modify_date FROM sys.tables where name = 'convenience_store'";
		try {
			conn = connFactory.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					if (!conn.isClosed()) {
						conn.close();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void selectByAddressExecuter() {
		String city = ""; 
		String district = "";
		String road = "";
		Scanner sc = new Scanner(System.in);
		System.out.println("可依順序(ex:城市->路名->巷弄)輸入3個地址搜尋關鍵字，或依指示輸入。");
		System.out.println("請輸入縣市:");
		city = sc.nextLine();
		System.out.println("請輸入街區:");
		district = sc.nextLine();
		System.out.println("請輸入路名:");
		road = sc.nextLine();
		ConvenienceStoreSqlImpl StoreSqlImpl = new ConvenienceStoreSqlImpl();
		List<String[]> store = new ArrayList<String[]>();
		store = StoreSqlImpl.selectByAddress(city, district, road);
		if ( store == null ) {
			System.out.println("沒找到。");
		} else {
			for ( String[] info:store ) {
				System.out.printf("%s: \t%s",info[0].substring(0, 6), info[1]);
				System.out.println();
			}
		}
//		sc.close(); // close會跳: java.util.NoSuchElementException
	}
	
	public void selectByCityExecuter(){
		int input = -1;
		int largestNum = 0;
		List<String[]> storesByCity = new ArrayList<String[]>();
		List<barObjBean> storeNumList = new ArrayList<>();
		Scanner sc = new Scanner(System.in);
		ConvenienceStoreSqlImpl StoreSqlImpl = new ConvenienceStoreSqlImpl();
		System.out.println("請輸入要查詢的縣市編號:");
		System.out.println("1.臺北市 2.新北市");
		try {
			input = sc.nextInt();
			storesByCity = StoreSqlImpl.selectByCity(input);
			for ( String[] cityInfo:storesByCity ) {
//				barObjBean storeNumObj = new barObjBean();
				System.out.println(cityInfo[0] + ": " + cityInfo[1]);
				if ( largestNum < Integer.valueOf(cityInfo[1]) ) {
					largestNum = Integer.valueOf(cityInfo[1]);
				}
			}
			System.out.println();
			// 長條圖用List
			// find largest number and print by scale of the largest
			// sort store number list asc?
			for ( String[] cityInfo:storesByCity ) { // 依原順序(未排序)印出長條圖
				float temp = ( Float.valueOf(cityInfo[1]) / largestNum ) * 40;
				int stars = Math.round(temp);
//				System.out.println(stars);
				System.out.print( cityInfo[0].substring(0, 2) + " " );
				for ( int i = 0; i < stars; i++ ) {
					System.out.print("*");
				}
				System.out.println();
			}
		} catch ( InputMismatchException e ) {
			System.out.println("輸入有誤。");
		}
		
	}
	
	public void deleteStoreExecuter() {
		int input = -1;
		Scanner sc = new Scanner(System.in);
		System.out.println("請輸入要刪除資料的id: ");
		try {
			input = sc.nextInt();
			ConvenienceStoreSqlImpl StoreSql = new ConvenienceStoreSqlImpl();
			System.out.println("已刪除" + StoreSql.deleteStore(input) + "列。");
		} catch ( Exception e ) {
			System.out.println("輸入有誤。");
		}
	}
	
	public void updateStoreExecuter() {
		ConvenienceStoreSqlImpl StoreSql = new ConvenienceStoreSqlImpl();
		StoreSqlObjBean storeData = new StoreSqlObjBean();
		Scanner sc = new Scanner(System.in);
		String input = "";
		System.out.println("請輸入要更改條目之id編號:");
		storeData.setId(Integer.valueOf(sc.nextLine()));
		System.out.println("請輸入[總公司統一編號]:");
		input = sc.nextLine();
		if ( input.trim() == "" ) { // 若無輸入或為空白字元，使用該欄資料庫原值。
			
		}
		storeData.setCompanyUniNum(Integer.valueOf(input));
		System.out.println("請輸入[公司名稱]:");
		storeData.setCompanyName(sc.nextLine());
		System.out.println("請輸入[分公司統一編號]:");
		storeData.setSubCompanyUniNum(Integer.valueOf(sc.nextLine()));
		System.out.println("請輸入[分公司名稱]:");
		storeData.setSubCompanyName(sc.nextLine());
		System.out.println("請輸入[分公司地址]:");
		storeData.setSubCompanyAddress(sc.nextLine());
		System.out.println("請輸入[分公司狀態]:");
		storeData.setSubCompanyStatus(Integer.valueOf(sc.nextLine()));
		System.out.println("請輸入[分公司核准設立日期]:");
		storeData.setSubCompanyPermmitDate(sc.nextLine());
		System.out.println("請輸入[分公司最後核准變更日期]:");
		storeData.setSubCompanyLastPermmitChangeDate(sc.nextLine());
		int count = StoreSql.updateStore(storeData);
		System.out.println("已更動" + count + "筆資料。");
	}
	
	public void getStoreByIdExecuter() {
		ConvenienceStoreSqlImpl StoreSql = new ConvenienceStoreSqlImpl();
		StoreSqlObjBean storeData = new StoreSqlObjBean();
		Scanner sc = new Scanner(System.in);
		System.out.println("請輸入要查詢的id:");
		String input = sc.nextLine();
		storeData = StoreSql.getStoreById(Integer.valueOf(input)); // input exception未修正
//		System.out.println(storeData.getId());
		System.out.println("公司統一編號: " + storeData.getCompanyUniNum());
		System.out.println("公司名稱: " + storeData.getCompanyName());
		System.out.println("分公司統一編號: " + storeData.getCompanyUniNum());
		System.out.println("分公司名稱: " + storeData.getSubCompanyName());
		System.out.println("分公司地址: " + storeData.getSubCompanyAddress());
		System.out.println("分公司狀態: " + storeData.getSubCompanyStatus());
		System.out.println("分公司核准設立日期: " + storeData.getSubCompanyPermmitDate());
		System.out.println("分公司最後核准變更日期: " + storeData.getSubCompanyLastPermmitChangeDate());
	}
	
	public void getAllStoresExecuter() {
		ConvenienceStoreSqlImpl StoreSql = new ConvenienceStoreSqlImpl();
		List<StoreSqlObjBean> storeData = new ArrayList<>();
		storeData = StoreSql.getAllStores();
		for ( StoreSqlObjBean store:storeData ) {
			System.out.println("id: "+ store.getId());
			System.out.println("公司統一編號: " + store.getCompanyUniNum());
			System.out.println("公司名稱: " + store.getCompanyName());
			System.out.println("分公司統一編號: " + store.getCompanyUniNum());
			System.out.println("分公司名稱: " + store.getSubCompanyName());
			System.out.println("分公司地址: " + store.getSubCompanyAddress());
			System.out.println("分公司狀態: " + store.getSubCompanyStatus());
			System.out.println("分公司核准設立日期: " + store.getSubCompanyPermmitDate());
			System.out.println("分公司最後核准變更日期: " + store.getSubCompanyLastPermmitChangeDate());
			System.out.println();
		}
	}
	
	public void createStoreExecuter() {
		ConvenienceStoreSqlImpl StoreSql = new ConvenienceStoreSqlImpl();
		StoreSqlObjBean storeData = new StoreSqlObjBean();
		Scanner sc = new Scanner(System.in);
		String input = "";
		System.out.println("請輸入新增條目相關資訊。");
		System.out.println("請輸入[總公司統一編號]:");
		input = sc.nextLine();
		if ( input.trim() == "" ) { // 若無輸入或為空白字元，使用該欄資料庫原值。
			
		}
		storeData.setCompanyUniNum(Integer.valueOf(input));
		System.out.println("請輸入[公司名稱]:");
		storeData.setCompanyName(sc.nextLine().trim());
		System.out.println("請輸入[分公司統一編號]:");
		storeData.setSubCompanyUniNum(Integer.valueOf(sc.nextLine().trim()));
		System.out.println("請輸入[分公司名稱]:");
		storeData.setSubCompanyName(sc.nextLine().trim());
		System.out.println("請輸入[分公司地址]:");
		storeData.setSubCompanyAddress(sc.nextLine().trim());
		System.out.println("請輸入[分公司狀態]:");
		storeData.setSubCompanyStatus(Integer.valueOf(sc.nextLine().trim()));
		System.out.println("請輸入[分公司核准設立日期]:");
		storeData.setSubCompanyPermmitDate(sc.nextLine().trim());
		System.out.println("請輸入[分公司最後核准變更日期]:");
		storeData.setSubCompanyLastPermmitChangeDate(sc.nextLine().trim());
		if (StoreSql.createStore(storeData) ) {
			System.out.println("已新增該筆資料。");
		}
	}
}
