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

	/**
	 * First time developer need init Database, and user copy created Database from asset by
	 * {@code copyDataBase}.
	 * <p/>
	 * Should comment out when release.
	 */
	public static void initDatabase() {
		addShopData("幸福咖哩", "高雄市三民區正忠路465號", 22.650773, 120.33042899999998, "建工", "午餐、晚餐", "無",
				"超級好吃的咖哩，飯也很有彈性 不會粘稠", "http://www.17d.com.tw/_pos/fascia/T12032310004114027/G.jpg",
				"");
		addShopData("蓮藕大王", "高雄市三民區正忠路348號", 22.6480775, 120.33029429999999, "建工", "午餐、晚餐", "無",
				"超級好喝的飲料健康，原味 讚！",
				"https://farm9.staticflickr.com/8680/15994770948_c8314fafa1_b.jpg", "");
		addShopData("八丁畷日式碳燒", "高雄市三民區正忠路219號", 22.6447713, 120.33014430000003, "建工", "宵夜", "無",
				"以一般外面串燒居酒屋來比較的話算便宜~東西也好吃、也有熱炒好吃又可以吃飽~100元內也可解決、有送小菜、飲料喝到飽喔~~",
				"http://iphoto.ipeen.com.tw/photo/comment/1039190/750472/cm20141023___0d12ff3faf5cc84cea990acea5c287e0609.jpg",
				"");
		addShopData("金飯統便當", "高雄市建工路711號", 22.647688, 120.31666799999994, "建工", "午餐、晚餐", "無",
				"全部65 學生可加飯 加的很大方",
				"http://bigegg.ezcity.com.tw/hyfiles/bigegg-photo/hotnews/gfantong/gfantong_20111122-3.jpg",
				"");
		addShopData("滷味鮮湯滷味", "高雄市三民區立忠路31號", 22.6530742, 120.32567549999999, "建工", "午餐、晚餐", "無",
				"便宜的滷味  不會很重鹹 老闆&老闆娘nice 很讚的喲~~", "http://i.imgur.com/Pl7AmDW.jpg", "");
		addShopData("韓尚宮", "高雄市三民區建工路341號", 22.6528983, 120.33093780000002, "建工", "午餐、晚餐",
				"11:00-14:00、17:00-21:30", "無",
				"http://ext.pimg.tw/millychun/1384019314-727551205.jpg", "");
		addShopData("大珍咖哩專賣店", "高雄市三民區昌裕街216號", 22.6522116, 120.32852060000005, "建工", "午餐、晚餐",
				"週一至週六11:00-15:00、16:30-20:00", "無",
				"http://lh3.googleusercontent.com/homeycat/R4gpC6wKyFI/AAAAAAAADRA/9j-XZzbLs2c/s512/DSCN7782.jpg?imgmax=512",
				"");
		addShopData("元氣早午餐", "高雄市三民區新民路86號", 22.652202, 120.32388600000002, "建工", "早午餐", "無",
				"推薦套餐5號餐，記得跟漂亮的阿姨說蛋要雙煎半熟，在配上冰奶茶，超讚!",
				"http://iphoto.ipeen.com.tw/photo/comment/2/3/9/cm20130717_534430_614122_24c48cc8ee69058dc096f8d1bae6b1f4671.jpg",
				"");
		addShopData("義式創意廚坊", "高雄市三民區大昌二路514號", 22.6514538, 120.33060130000001, "建工", "午餐、晚餐", "無",
				"這間店沒有網站，網路上是找不道的 他的東西從飯到麵一律55元 +焗烤65元 種類簇繁不及備載，大抵上可以以口味區分 白醬 茄汁 咖哩 雙味 青醬 南瓜醬 泡菜 宮保 麻辣 拉差 有義大利麵 貝殼麵 螺旋麵 通心 尖管麵 蝴蝶麵 直面 煲飯和手工麵等 還有水餃以及炒飯 丼飯",
				"http://www.17life.com/media/JL-RA-3434/634605059479987500.jpg", "");
		addShopData("Ruby的咖哩廚房", "高雄市三民區建工路381號", 22.65247, 120.33022200000005, "建工", "午餐、晚餐",
				"營業時間週二至週日11:00–14:00、16:00–20:00", "無",
				"https://s-media-cache-ak0.pinimg.com/736x/05/80/f9/0580f94a096e44700e7e1b51a66f18d8.jpg",
				"");
		addShopData("輔英蛋包飯專賣店", "高雄市三民區立志街18號", 22.6519461, 120.32695949999993, "建工", "早餐", "無",
				"各式蛋包(炒)飯 小的最便宜20元 cp值還不錯 一般是上下學時段有開", "http://i.imgur.com/L4asA8F.jpg", "");
		addShopData("好運來", "高雄市三民區立志街22巷1號", 22.6519699, 120.32685019999997, "建工", "午餐、晚餐", "無",
				"飯麵好吃 大概50內", "http://i.imgur.com/3stAXyJ.jpg", "");
		addShopData("原鄉牛肉麵", "高雄市三民區大昌二路481號", 22.6505576, 120.33124610000004, "建工", "午餐、晚餐", "無",
				"丹丹漢堡旁邊水餃一顆3塊 爽",
				"http://ext.pimg.tw/jung9572002/4af22934ab6b443dc05654dd44c14d4c.jpg", "");
		addShopData("39焗烤家族", "高雄市三民區大昌二路551號", 22.6518354, 120.32972569999993, "建工", "午餐、晚餐", "無",
				"提供義大利麵、焗烤、比薩、異國咖哩排餐、雞腿堡餐、焗烤厚片、雞塊、薯條", "http://mmweb.tw/sys/ieb/pic/m82274_2.jpg",
				"");
		addShopData("燈籠滷味", "高雄市三民區大昌二路511號", 22.651125, 120.3304253, "建工", "午餐、晚餐", "無", "好吃的滷味",
				"http://lh3.googleusercontent.com/homeycat/R9Vc0tB6HdI/AAAAAAAAEB0/vqYLZnKLhSo/s512/DSC00015.jpg?imgmax=512",
				"");
		addShopData("赤印牛排", "高雄市三民區明誠一路443號", 22.6602182, 120.32151010000007, "建工", "午餐、晚餐", "無",
				"平價但一點都不馬虎，麵粉經過拌炒後，加入碎肉及玉米，敖煮出濃濃奶香的玉米濃湯，還有爆漿餐包及古早味紅茶，都是店內的招牌",
				"http://ext.pimg.tw/julialkpkpk/1421747284-4071200878.jpg", "");
		addShopData("鮮焗家", "高雄市三民區大昌一路38號", 22.6531206, 120.32766670000001, "建工", "午餐", "無", "焗烤",
				"http://pic.pimg.tw/goshow2012/1389679245-1343507044_m.jpg", "");
		addShopData("幽靈香腸", "高雄市三民區河北二路130號", 22.635394, 120.294318, "建工", "宵夜", "無",
				"不錯吃，營業時間非常不固定，請賭運氣在2330～0000間到三鳳宮看看",
				"http://static.nownews.com/newspic/2155/i2155968.jpg", "");
		addShopData("胖叔叔拉麵店", "高雄市三民區正忠路502號", 22.6507438, 120.33093459999998, "建工", "午餐、晚餐", "無",
				"超級好吃的拉麵店", "http://pic.pimg.tw/gi23771362/1358306214-335914739_n.jpg", "");
		addShopData("正中食堂", "高雄市三民區正忠路494號", 22.6506157, 120.33084280000003, "建工", "午餐、晚餐", "無",
				"超好吃的泰式拋肉飯 吃完總是讚不絕口",
				"http://iphoto.ipeen.com.tw/photo/comment/5/8/1/cm20120715_160153_136201_81a2150260ac590cc7b43edbde9e64a7392.jpg",
				"");
		addShopData("廣九燒臘", "高雄市大社區中山路256號", 22.7317969, 120.34898099999998, "燕巢", "午餐、晚餐",
				"08:00-14:00、16:00-20:00", "(07) 3540970－專業送高應大便當店",
				"http://iphoto.ipeen.com.tw/photo/comment/6/6/4/cm20120709_103183_17332_282d30326f8c83de8e4ec90c8368db09510.jpg",
				"073540970");
		addShopData("禾記嫩骨飯", "高雄市大社區中山路485號", 22.7310453, 120.35454589999995, "燕巢", "午餐、晚餐", "無",
				"(07) 354 2088", "http://ext.pimg.tw/fdrm4583/1368853964-3998527608_l.jpg",
				"073542088");
		addShopData("真美香台日食堂", "高雄市大社區中山路410號", 22.7312672, 120.35266219999994, "燕巢", "午餐、晚餐", "無",
				"(07) 352 3772", "https://farm1.staticflickr.com/284/19548045975_7703475ed3_b.jpg",
				"073523772");
		addShopData("八方雲集", "高雄市燕巢區中民路704號", 22.7932222, 120.35931470000003, "燕巢", "午餐、晚餐", "無",
				"(07) 353 4868", "http://www.8way.asia/images/enter.jpg", "073534868");
		addShopData("大社第一家土魠魚羹", "高雄市大社區中山路410號", 22.7312672, 120.35266219999994, "燕巢", "午餐、晚餐",
				"無", "0970 412 513 羹飯麵偏少更新45、55，服務好", "http://i.imgur.com/Ihgl7Ic.jpg",
				"0970412513");
		addShopData("味廚粥品／麵食", "高雄市大社區中山路124號", 22.7319874, 120.3468183, "燕巢", "午餐、晚餐", "無",
				"(07) 352 7192",
				"http://ext.pimg.tw/trew780918/1427677359-3788168556_n.jpg?v=1427677376",
				"073527192");
		addShopData("炒翻天", "高雄市大社區中山路410號", 22.7312672, 120.35266219999994, "燕巢", "午餐、晚餐", "無",
				"(07) 353 9530 炒飯是白色的、普普",
				"http://farm3.static.flickr.com/2649/4063803564_ed700dac4c.jpg", "073539530");
		addShopData("悟饕池上飯包", "高雄市燕巢區中民路586號", 22.7932965, 120.36244439999996, "燕巢", "午餐、晚餐", "無",
				"(07) 614 2977", "http://www.wu-tau.com/themes/default/images/tip_1.jpg",
				"076142977");
	}

}
