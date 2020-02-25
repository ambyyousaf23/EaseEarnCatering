package com.example.Manager;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.SharedClass;
import com.example.qureshi.easeearncatering.DataModels.CartDataModel;
import com.example.qureshi.easeearncatering.DataModels.Cart_DataModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

	// ===================================================== Cart Table SQLite:

	public static final String DATABASE_TABLE_CART = "cart";

	public static final String DATABASE_NAME = "cater.sqlite";
	static final int DATABASE_VERSION = 2;
	final Context context;
	SQLiteDatabase db;
	DatabaseHelper DBHelper;

	public DBManager(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}

	// ---opens the database---
	public DBManager open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	public void createDataBase() throws IOException {

		boolean mDataBaseExist = checkDataBase();

		if (mDataBaseExist) {
			// do nothing - database already exist

		} else {

			DBHelper.getReadableDatabase();

			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/** This method checks whether database is exists or not **/
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;

		try {
			String myPath = context.getDatabasePath(DATABASE_NAME).getPath()
					.toString();

			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	public long copyDataBase() throws IOException {
		String DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath()
				.toString();

		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open(DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
		return length;
	}

//	public List<Cart_DataModel> showOfflineCart(String user_id)
//	{
//		List<Cart_DataModel> data=new ArrayList<Cart_DataModel>();
//		String query="select * from cart_table where user_id = ?";
//		String[] selections={user_id};
//		Cursor cursor=db.rawQuery(query, selections);
//		if (cursor.moveToFirst()){
//			do {
//				Cart_DataModel dataModel=new Cart_DataModel();
//				dataModel.setCart_id(cursor.getString(0));
//				dataModel.setActual_price(cursor.getString(1));
//				dataModel.setDatetime(cursor.getString(2));
//				dataModel.setProduct_id(cursor.getString(3));
//				dataModel.setQuantity(cursor.getString(4));
//				dataModel.setTotal_amount(cursor.getString(5));
//				dataModel.setUser_id(cursor.getString(6));
//				dataModel.setMart_id(cursor.getString(7));
//				dataModel.setSale_price(cursor.getString(8));
//				dataModel.setTax(cursor.getString(9));
//				dataModel.setStock_id(cursor.getString(10));
//				dataModel.setTotal_stock(cursor.getString(11));
//				dataModel.setProd_name(cursor.getString(12));
//				dataModel.setDescription(cursor.getString(13));
//				data.add(dataModel);
//			}while (cursor.moveToNext());
//		}
//		return data;
//	}

	public List<CartDataModel> showCart(String user_id)
	{
		List<CartDataModel> data=new ArrayList<CartDataModel>();
		String query="select * from cart where user_id = ?";
		String[] selections={user_id};
		Cursor cursor=db.rawQuery(query, selections);
		if (cursor.moveToFirst()){
			do {
				CartDataModel dataModel=new CartDataModel();
				dataModel.setId(cursor.getString(0));
				dataModel.setMenu_id(cursor.getString(1));
				dataModel.setDish(cursor.getString(2));
				dataModel.setCater_id(cursor.getString(3));
				dataModel.setUser_id(cursor.getString(4));
				dataModel.setQuantity(cursor.getString(5));
				SharedClass.quantity_total=SharedClass.quantity_total+Integer.parseInt(dataModel.getQuantity());
				dataModel.setAmount(cursor.getString(6));
				dataModel.setTotal(cursor.getString(7));
				SharedClass.amount_total=SharedClass.amount_total+Integer.parseInt(dataModel.getTotal());
				dataModel.setDatetime(cursor.getString(8));
				dataModel.setSubmenu_id(cursor.getString(9));
				dataModel.setImage(cursor.getString(10));
				data.add(dataModel);
			}while (cursor.moveToNext());
		}
		return data;
	}

	public boolean syncCartProduct(String menu_id, String submenu_id, String dish, String cater_id, String user_id, String quantity, String amount, String total, String datetime, String image){
		String[] selections={user_id, submenu_id};
		String query="select * from cart where user_id = ? and submenu_id = ?";
		Cursor cursor=db.rawQuery(query,selections);
		if(cursor.getCount()>0){
			Toast.makeText(context, "Dish already exists", Toast.LENGTH_SHORT).show();
		}
		else {
			ContentValues contentValues = new ContentValues();
			contentValues.put("menu_id", menu_id);
			contentValues.put("submenu_id", submenu_id);
			contentValues.put("dish", dish);
			contentValues.put("cater_id", cater_id);
			contentValues.put("user_id", user_id);
			contentValues.put("quantity", quantity);
			contentValues.put("amount", amount);
			contentValues.put("total", total);
			contentValues.put("datetime", datetime);
			contentValues.put("image", image);
			long l = db.insert(DATABASE_TABLE_CART, null, contentValues);
			if (l > 0) {
				Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
			}
		}
		return true;
	}

//	public void deleteCartItem(String user_id, String prod_id, String mart_id, Intent intent){
//		long obj=db.delete("cart_table","user_id=? and product_id=? and mart_id=?",new String[] { user_id, prod_id, mart_id});
//		if (obj>0){
//			Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
//			context.startActivity(intent);
//		}
//		else {Toast.makeText(context, "Deletion Failed", Toast.LENGTH_SHORT).show();
//		}
//	}

	public void updateCartItem(String user_id, String submenu_id, String cater_id, String total_amount, String quantity, Intent intent){
		ContentValues cv = new ContentValues();
		cv.put("total", total_amount);
		cv.put("quantity", quantity);
		long obj=db.update("cart", cv, "user_id = ? and submenu_id = ? and cater_id = ?",
				new String[]{user_id, submenu_id, cater_id});
		if (obj>0){
			Toast.makeText(context, "updated successful", Toast.LENGTH_SHORT).show();
			context.startActivity(intent);
		}
		else {
			Toast.makeText(context, "Updation Failed", Toast.LENGTH_SHORT)
					.show();
		}
	}
//
//	// ================================= FAVOURITES
//
//	public List<FavouritesModel> showFavourites(String user_id, String mart_id)
//	{
//		List<FavouritesModel> data=new ArrayList<FavouritesModel>();
//		String query="select * from favourites where user_id = ? and mart_id = ?";
//		String[] selections={user_id, mart_id};
//		Cursor cursor=db.rawQuery(query, selections);
//		if (cursor.moveToFirst()){
//			do {
//				FavouritesModel dataModel=new FavouritesModel();
//				dataModel.setId(cursor.getString(0));
//				dataModel.setCategory_name(cursor.getString(1));
//				dataModel.setMart_id(cursor.getString(2));
//				dataModel.setMart_name(cursor.getString(3));
//				dataModel.setProduct_code(cursor.getString(4));
//				dataModel.setProduct_id(cursor.getString(5));
//				dataModel.setProduct_image(cursor.getString(6));
//				dataModel.setProduct_name(cursor.getString(7));
//				dataModel.setRetail_price(cursor.getString(8));
//				dataModel.setSale_price(cursor.getString(9));
//				dataModel.setStock_id(cursor.getString(10));
//				dataModel.setSub_category(cursor.getString(11));
//				dataModel.setTotal_stock(cursor.getString(12));
//				dataModel.setBarcode(cursor.getString(13));
//				dataModel.setTax(cursor.getString(14));
//				dataModel.setItem_description(cursor.getString(15));
//				data.add(dataModel);
//			}while (cursor.moveToNext());
//		}
//		return data;
//	}
//
//	public boolean insertFavourites(String category_name, String mart_id, String mart_name, String product_code, String product_id, String product_image, String product_name, String retail_price, String sale_price, String stock_id, String sub_category, String total_stock, String barcode, String tax, String item_description, String user_id){
//		String[] selections={user_id, product_id, mart_id};
//		String query="select * from "+DATABASE_TABLE_FAVOURITES+" where user_id = ? and "+KEY_FAVOURITE_PRODUCT_ID+" = ? and "+KEY_FAVOURITE_MART_ID+" = ? ";
//		Cursor cursor=db.rawQuery(query,selections);
//		if(cursor.getCount()>0){
//			Toast.makeText(context, "Product already exists for user id "+user_id+" and product id "+product_id+" and mart id "+mart_id, Toast.LENGTH_SHORT).show();
////			progress.hide();
//		}
//		else {
//			ContentValues contentValues = new ContentValues();
//			contentValues.put(KEY_FAVOURITE_CATEGORY_NAME, category_name);
//			contentValues.put(KEY_FAVOURITE_MART_ID, mart_id);
//			contentValues.put(KEY_FAVOURITE_MART_NAME, mart_name);
//			contentValues.put(KEY_FAVOURITE_PRODUCT_CODE, product_code);
//			contentValues.put(KEY_FAVOURITE_PRODUCT_ID, product_id);
//			contentValues.put(KEY_FAVOURITE_PRODUCT_IMAGE, product_image);
//			contentValues.put(KEY_FAVOURITE_PRODUCT_NAME, product_name);
//			contentValues.put(KEY_FAVOURITE_RETAIL_PRICE, retail_price);
//			contentValues.put(KEY_FAVOURITE_SALE_PRICE, sale_price);
//			contentValues.put(KEY_FAVOURITE_STOCK_ID, stock_id);
//			contentValues.put(KEY_FAVOURITE_SUB_CATEGORY, sub_category);
//			contentValues.put(KEY_FAVOURITE_TOTAL_STOCK, total_stock);
//			contentValues.put(KEY_FAVOURITE_BARCODE, barcode);
//			contentValues.put(KEY_FAVOURITE_TAX, tax);
//			contentValues.put(KEY_FAVOURITE_ITEM_DESCRIPTION, item_description);
//			contentValues.put("user_id", user_id);
//			long l = db.insert(DATABASE_TABLE_FAVOURITES, null, contentValues);
//			if (l > 0) {
//				Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
////				progress.hide();
//			}
//			else {
//				Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
////				progress.hide();
//			}
//		}
//		return true;
//	}
//
	public void deleteCartItem(String submenu_id, String cater_id, Intent intent){
		long obj=db.delete("cart","submenu_id=? and cater_id=?",new String[] { submenu_id, cater_id});
		if (obj>0){
			Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
			context.startActivity(intent);
		}
		else {Toast.makeText(context, "Deletion Failed", Toast.LENGTH_SHORT).show();
		}
	}

}