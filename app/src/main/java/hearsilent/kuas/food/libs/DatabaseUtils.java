package hearsilent.kuas.food.libs;

import android.content.Context;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import hearsilent.kuas.food.models.ShopModel;

public class DatabaseUtils {

	private static final String DATABASE_NAME = "KUAS_FOOD.db";
	private static final String DATABASE_FOLDER = "/data/data/hearsilent.kuas.food/databases/";

	public static boolean copyDataBase(Context context) {
		byte[] buffer = new byte[1024];
		OutputStream myOutput;
		int length;
		InputStream myInput;
		try {
			myInput = context.getAssets().open(DATABASE_NAME);
			File folder = new File(DATABASE_FOLDER);
			boolean success = true;
			if (!folder.exists()) {
				success = folder.mkdir();
			}
			if (success) {
				myOutput = new FileOutputStream(DATABASE_FOLDER + DATABASE_NAME);
			} else {
				return false;
			}
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.close();
			myOutput.flush();
			myInput.close();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void addShopData(String name, String address, double lat, double lng,
	                               String region, String type, String time, String description,
	                               String image, String phone) {
		ShopModel model = new ShopModel();
		model.setName(name);
		model.setAddress(address);
		model.setLat(lat);
		model.setLng(lng);
		model.setRegion(region);
		model.setType(type);
		model.setTime(time);
		model.setDescription(description);
		model.setImage(image);
		model.setPhone(phone);
		model.save();
	}

	public static List<ShopModel> getJianGongList() {
		return DataSupport.where("region = ?", Constant.JIANGONG).find(ShopModel.class);
	}

	public static List<ShopModel> getYanChaoList() {
		return DataSupport.where("region = ?", Constant.YANCHAO).find(ShopModel.class);
	}

	public static ShopModel getShop(String name) {
		return DataSupport.where("name = ?", name).findFirst(ShopModel.class);
	}

}
