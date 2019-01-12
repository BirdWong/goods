package cn.jijiking51.goods.admin.Order.commons;

/**
 * @author h4795
 * @date 2019/1/4 0004
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import cn.jijiking51.goods.book.domain.Book;
import cn.jijiking51.goods.order.domain.Order;
import cn.jijiking51.goods.order.domain.OrderItem;
import cn.jijiking51.goods.user.domain.User;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import javax.servlet.http.HttpServletResponse;

public class OrderExcel {
	//各列标题名
	static String[] titleString = {"订单","用户名","下单时间","总金额","状态","地址","书籍id","书籍名","书籍单价格","单类总金额","购买数量"};
	//各列的宽度
	static int[] titleSize = new int[] {11*1024,  3*1024,   6*1024,   2*1024,   3*1024,  15*1024,   11*1024,   10*1024,3*1024,3*1024,3*1024};
	//文字设置
	HSSFFont fontStyleStatic;
	//样式设置
	HSSFCellStyle cellStyleStatic;


	/**
	 * 初始化
	 * @param wb
	 * @param sheet
	 */
	void initExcle(HSSFWorkbook wb,HSSFSheet sheet) {

		/*
		 * 设置标题
		 * 	创建行
		 * 	取得列
		 * 	设置内容
		 * 	设置文字大小 并调用setStyle设置样式
		 * 	合并单元格
		 * 	for循环设置列标题
		 */
		sheet.setZoom(9,10);
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("图书商城订单详情");
		setStyle(wb, (short)20, cell);

		MergedRegion(wb,sheet, 0, 0, 0, titleString.length-1);


		row = sheet.createRow(1);


		for(int i = 0; i < titleString.length  ;i++) {
			sheet.setColumnWidth(i, titleSize[i]);
			cell = row.createCell(i);
			cell.setCellValue(titleString[i]);
			setStyle(wb, (short)12, cell);
		}
	}

	/**
	 * 生成WorkBook对象
	 * @param name
	 * @return
	 */
	HSSFWorkbook getWb(String name) {
		HSSFWorkbook wb = new HSSFWorkbook();
		//添加Worksheet（不添加sheet时生成的xls文件打开时会报错）

		return wb;
	}

	/**
	 * 生成Sheet对象
	 * @param wb
	 * @param name
	 * @return
	 */
	HSSFSheet getSheet(HSSFWorkbook wb,String name) {
		@SuppressWarnings("unused")
		HSSFSheet sheet = wb.createSheet(name);
		return sheet;
	}

	/**
	 * 输出excel方法
	 * @param wb
	 * @param filePath
	 */
	void saveExcel(HSSFWorkbook wb,String filePath) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);
			wb.write(fileOutputStream);
			System.out.println("成功");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * 创建Font字体对象方法
	 * @param wb
	 * @return
	 */
	HSSFFont getFont(HSSFWorkbook wb) {

		if(fontStyleStatic == null) {
			fontStyleStatic = wb.createFont();


		}

		return fontStyleStatic;
	}

	/**
	 * 创建Style样式对象的方法
	 * @param wb
	 * @return
	 */
	HSSFCellStyle getcellStyle(HSSFWorkbook wb) {

		if( cellStyleStatic == null) {
			cellStyleStatic = wb.createCellStyle();

		}

		return cellStyleStatic;
	}

	/**
	 * 设置字体等样式
	 * @param wb
	 * @param size
	 * @param cell
	 */
	void setStyle(HSSFWorkbook wb, short size,Cell cell) {
		HSSFCellStyle cellStyle = getcellStyle(wb);
		HSSFFont fontStyle = getFont(wb);

		fontStyle.setFontName("宋体");
		fontStyle.setFontHeightInPoints(size);

		cellStyle.setFont(fontStyle);
		//水平垂直居中
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		// 边框设置
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cell.setCellStyle(cellStyle);
	}



	/**
	 * 合并单元格，并且添加边框
	 * @param sheet
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	void MergedRegion(HSSFWorkbook wb,HSSFSheet sheet,int x1,int x2,int y1,int y2) {
		// 起始行, 终止行, 起始列, 终止列
		CellRangeAddress cra =new CellRangeAddress(x1,x2, y1,y2);
		sheet.addMergedRegion(cra);

		// 使用RegionUtil类为合并后的单元格添加边框
		// 下边框
		RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet);
		// 左边框
		RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet);
		// 有边框
		RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
		// 上边框
		RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);

		//合并单元格后设置样式
		HSSFRow row = sheet.getRow(x1);
		HSSFCell cell = row.getCell(y1);
		setStyle(wb, (short)12, cell);



	}


	/**
	 * 获得所有订单
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	ArrayList<Order> getOrderList() throws ClassNotFoundException, SQLException {
		Connection conn = new  JdbcUtil().createConnection();
		Statement stmt = conn.createStatement();

		String sql = "select oid,ordertime,total,status,address,uid from t_order ";
		ResultSet result = stmt.executeQuery(sql);
		ArrayList<Order> orderList = new ArrayList<Order>();
		while(result.next()) {
			String oid = result.getString("oid");
			String ordertime = result.getString("ordertime");
			Double total = result.getDouble("total");
			int status = result.getInt("status");
			String address = result.getString("address");
			String uid = result.getString("uid");

			sql = "select uid,loginname from t_user WHERE uid = ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, uid);

			ResultSet userResult = pstmt.executeQuery();

			User user = new User();
			user.setUid(uid);
			while(userResult.next()) {
				String name = userResult.getString("loginname");
				user.setLoginname(name);
			}


			sql="select quantity,subtotal,bid,bname,currPrice from t_orderitem WHERE oid = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, oid);

			ResultSet orderItemResult = pstmt.executeQuery();

			List<OrderItem> orderItemList= new ArrayList<OrderItem>();

			while(orderItemResult.next()) {
				OrderItem orderItem = new OrderItem();
				int quantity = orderItemResult.getInt("quantity");

				double subtotal = orderItemResult.getDouble("subtotal");

				String bid =orderItemResult.getString("bid");

				String bname = orderItemResult.getString("bname");

				double currPrice = orderItemResult.getDouble("currPrice");

				orderItem.setQuantity(quantity);
				orderItem.setSubtotal(subtotal);

				Book book = new Book();

				book.setBid(bid);
				book.setBname(bname);
				book.setCurrPrice(currPrice);

				orderItem.setBook(book);


				orderItemList.add(orderItem);

			}

			Order order = new Order();
			order.setOid(oid);
			order.setOrdertime(ordertime);
			order.setStatus(status);
			order.setTotal(total);
			order.setAddress(address);
			order.setUser(user);
			order.setOrderItemList(orderItemList);
			orderList.add(order);
		}

		JdbcUtil.closeConnection(conn);

		return orderList;
	}


	/**
	 * 将内容写入excel
	 * @param wb
	 * @param sheet
	 * @param orderList
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	void writeExcel( HSSFWorkbook wb,HSSFSheet sheet,List<Order> orderList) throws ClassNotFoundException, SQLException {

		int sign = 2;
		for(Order order : orderList) {
			int end = sign;
			for(OrderItem orderItem : order.getOrderItemList()) {
				HSSFRow row = sheet.createRow(end);
				HSSFCell cell = row.createCell(6);
				cell.setCellValue(orderItem.getBook().getBid());
				setStyle(wb, (short)12, cell);

				cell = row.createCell(7);
				cell.setCellValue(orderItem.getBook().getBname());
				setStyle(wb, (short)12, cell);

				cell = row.createCell(8);
				cell.setCellValue(orderItem.getBook().getCurrPrice());
				setStyle(wb, (short)12, cell);

				cell = row.createCell(9);
				cell.setCellValue(orderItem.getSubtotal());
				setStyle(wb, (short)12, cell);

				cell = row.createCell(10);
				cell.setCellValue(orderItem.getQuantity());
				setStyle(wb, (short)12, cell);

				end++;
			}



			int index = 0;

			HSSFRow row = sheet.getRow(sign);

			HSSFCell cell = row.createCell(index);

			cell.setCellValue(order.getOid());
			if(sign != end -1) {
				MergedRegion(wb,sheet, sign, end-1, index, index);
			}else {
				setStyle(wb, (short)12, cell);
			}



			index++;





			row = sheet.getRow(sign);

			cell = row.createCell(index);

			cell.setCellValue(order.getUser().getLoginname());

			if(sign != end -1) {
				MergedRegion(wb,sheet, sign, end-1, index, index);
			}else {
				setStyle(wb, (short)12, cell);
			}


			index++;



			row = sheet.getRow(sign);

			cell = row.createCell(index);

			cell.setCellValue(order.getOrdertime());

			if(sign != end -1) {
				MergedRegion(wb,sheet, sign, end-1, index, index);
			}else {
				setStyle(wb, (short)12, cell);
			}


			index++;



			row = sheet.getRow(sign);

			cell = row.createCell(index);

			cell.setCellValue(order.getTotal());

			if(sign != end -1) {
				MergedRegion(wb,sheet, sign, end-1, index, index);
			}else {
				setStyle(wb, (short)12, cell);
			}


			index++;





			row = sheet.getRow(sign);

			cell = row.createCell(index);

			String status;

			switch (order.getStatus()) {
				case 1:
					status = "等待付款";
					break;

				case 2:
					status = "准备发货";
					break;

				case 3:
					status = "等待确认";
					break;

				case 4:
					status = "交易成功";
					break;

				case 5:
					status = "已取消";
					break;
				default:
					status = "";
					break;
			}

			cell.setCellValue(status);

			if(sign != end -1) {
				MergedRegion(wb,sheet, sign, end-1, index, index);
			}else {
				setStyle(wb, (short)12, cell);
			}


			index++;



			row = sheet.getRow(sign);

			cell = row.createCell(index);

			cell.setCellValue(order.getAddress());

			if(sign != end -1) {
				MergedRegion(wb,sheet, sign, end-1, index, index);
			}else {
				setStyle(wb, (short)12, cell);
			}


			index++;



			sign = end;

		}

	}







	/*
	 * 订单号，用户名，下单时间，总金额，状态，地址，商品详情（书籍id，书籍名，书籍单价格，单类总金额，购买数量）
	 */

	/**
	 * 将excel写给用户
	 * @param rep
	 * @param name
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void getExcel(HttpServletResponse rep,String name) throws SQLException, ClassNotFoundException, IOException {
		HSSFWorkbook wb = getWb(name);
		HSSFSheet sheet = getSheet(wb, name);
		initExcle(wb, sheet);
		List<Order> orderList = getOrderList();
		writeExcel(wb, sheet, orderList);
		//saveExcel(wb, filePath);
		//设置返回类型
		rep.setContentType("application/octet-stream");
		String returnName = rep.encodeURL( new String((name+".xls").getBytes(), "ISO-8859-1"));
		rep.addHeader("Content-Disposition", "attachment;filename=" + returnName);
		wb.write(rep.getOutputStream());
	}
}
