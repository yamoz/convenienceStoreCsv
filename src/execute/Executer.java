package execute;

import java.util.InputMismatchException;
import java.util.Scanner;

import streamData.IOStream;

public class Executer {

	public static void main(String[] args) {
		IOStream grabber = new IOStream();
		Scanner sc = new Scanner(System.in);
		int userInput = 0;
		while ( userInput != -999 ) {
			System.out.println("資料庫更新日期: " + grabber.showTableCreateDate() + " (輸入-999離開程式)" );
			System.out.println("想幹嘛?");
			System.out.println("請輸入代號:");
			System.out.println("1.刷新資料庫 2.匯出資料庫為csv 3.依地址找附近的超商 4.以縣市查看各大超商家數");
			System.out.println("5.增加新的超商資訊 6.手動更新超商資料 7.依據id刪除超商資料 8.印出全部超商資訊");
			System.out.println("9.依據id檢視超商資料");
			try {
				userInput = sc.nextInt();
			} catch ( InputMismatchException e ) {
				System.out.println("輸入有誤: 請重新輸入。");
				sc.next(); // 用於移到下一個scan token，避免一直停在error token上，造成無窮迴圈。
				continue;
			}
			switch (userInput) {
			case 1:
				grabber.BatchInsertFromUrl("https://quality.data.gov.tw/dq_download_csv.php?nid=32086&md5_url=70d75bd612378b4626495a70b7f1777c");
				System.out.println();
				break;
			case 2:
				grabber.selectAllStoreToCsv();
				System.out.println();
				break;
			case 3:
				grabber.selectByAddressExecuter();
				System.out.println();
				break;
			case 4:
				grabber.selectByCityExecuter();
				System.out.println();
				break;
			case 5:
				grabber.createStoreExecuter();
				System.out.println();
				break;
			case 6:
				grabber.updateStoreExecuter();
				System.out.println();
				break;
			case 7:
				grabber.deleteStoreExecuter();
				System.out.println();
				break;
			case 8:
				grabber.getAllStoresExecuter();
				System.out.println();
				break;
			case 9:
				grabber.getStoreByIdExecuter();
				System.out.println();
				break;
			case -999:
				System.out.println("程式結束");
				break;
			default:
				System.out.println("輸入有誤: 請重新輸入。");
				System.out.println();
			}
		}
		sc.close();
	}

}
