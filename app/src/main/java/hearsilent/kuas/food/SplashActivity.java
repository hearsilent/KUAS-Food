package hearsilent.kuas.food;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import hearsilent.kuas.food.callback.PermissionCallback;
import hearsilent.kuas.food.libs.PermissionUtils;

public class SplashActivity extends AppCompatActivity {

	private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 200;
	private static final int PERMISSION_REQUEST_SETTINGS = 201;
	private static final int PERMISSION_REQUEST_GPS_SETTINGS = 202;

	private static final String DB_NAME = "KUAS_FOOD_DB";
	private static final String TB_NAME = "shops";
	private static final String[] FORM =
			new String[]{"name", "address", "lat", "lng", "region", "type", "time", "description",
					"image", "phone"};
	SQLiteDatabase mDB;
	Cursor mCursor;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		setUpDB();
		setUpViews();
		grantPermission();
	}

	private void setUpViews() {
		final View progressView = findViewById(R.id.view_initial_progress);
		assert progressView != null;
		progressView.post(new Runnable() {

			@Override
			public void run() {
				progressView.setVisibility(View.VISIBLE);
			}
		});
	}

	private void setUpDB() {
		mDB = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

		String createTable = "CREATE TABLE IF NOT EXISTS " + TB_NAME +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"name VARCHAR(32), " +
				"address VARCHAR(32), " +
				"lat VARCHAR(32), " +
				"lng VARCHAR(32), " +
				"region VARCHAR(32), " +
				"type VARCHAR(32), " +
				"time VARCHAR(32), " +
				"description VARCHAR(64), " +
				"image VARCHAR(64), " +
				"phone VARCHAR(16))";
		mDB.execSQL(createTable);

		mCursor = mDB.rawQuery("SELECT * FROM " + TB_NAME, null);

		if (mCursor.getCount() == 0) {
			addData("幸福咖哩", "高雄市三民區正忠路465號", "22.650773", "120.33042899999998", "建工", "午餐、晚餐", "無",
					"超級好吃的咖哩，飯也很有彈性 不會粘稠",
					"http://www.17d.com.tw/_pos/fascia/T12032310004114027/G.jpg", "");
			addData("蓮藕大王", "高雄市三民區正忠路348號", "22.6480775", "120.33029429999999", "建工", "午餐、晚餐", "無",
					"超級好喝的飲料健康，原味 讚！",
					"https://farm9.staticflickr.com/8680/15994770948_c8314fafa1_b.jpg", "");
			addData("八丁畷日式碳燒", "高雄市三民區正忠路219號", "22.6447713", "120.33014430000003", "建工", "宵夜", "無",
					"以一般外面串燒居酒屋來比較的話算便宜~東西也好吃、也有熱炒好吃又可以吃飽~100元內也可解決、有送小菜、飲料喝到飽喔~~",
					"http://iphoto.ipeen.com.tw/photo/comment/1039190/750472/cm20141023___0d12ff3faf5cc84cea990acea5c287e0609.jpg",
					"");
			addData("金飯統便當", "高雄市建工路711號", "22.647688", "120.31666799999994", "建工", "午餐、晚餐", "無",
					"全部65 學生可加飯 加的很大方",
					"http://bigegg.ezcity.com.tw/hyfiles/bigegg-photo/hotnews/gfantong/gfantong_20111122-3.jpg",
					"");
			addData("滷味鮮湯滷味", "高雄市三民區立忠路31號", "22.6530742", "120.32567549999999", "建工", "午餐、晚餐",
					"無", "便宜的滷味  不會很重鹹 老闆&老闆娘nice 很讚的喲~~", "http://i.imgur.com/Pl7AmDW.jpg", "");
			addData("韓尚宮", "高雄市三民區建工路341號", "22.6528983", "120.33093780000002", "建工", "午餐、晚餐",
					"11:00-14:00、17:00-21:30", "無",
					"http://ext.pimg.tw/millychun/1384019314-727551205.jpg", "");
			addData("大珍咖哩專賣店", "高雄市三民區昌裕街216號", "22.6522116", "120.32852060000005", "建工", "午餐、晚餐",
					"週一至週六11:00-15:00、16:30-20:00", "無",
					"http://lh3.googleusercontent.com/homeycat/R4gpC6wKyFI/AAAAAAAADRA/9j-XZzbLs2c/s512/DSCN7782.jpg?imgmax=512",
					"");
			addData("元氣早午餐", "高雄市三民區新民路86號", "22.652202", "120.32388600000002", "建工", "早午餐", "無",
					"推薦套餐5號餐，記得跟漂亮的阿姨說蛋要雙煎半熟，在配上冰奶茶，超讚!",
					"http://iphoto.ipeen.com.tw/photo/comment/2/3/9/cm20130717_534430_614122_24c48cc8ee69058dc096f8d1bae6b1f4671.jpg",
					"");
			addData("義式創意廚坊", "高雄市三民區大昌二路514號", "22.6514538", "120.33060130000001", "建工", "午餐、晚餐",
					"無",
					"這間店沒有網站，網路上是找不道的 他的東西從飯到麵一律55元 +焗烤65元 種類簇繁不及備載，大抵上可以以口味區分 白醬 茄汁 咖哩 雙味 青醬 南瓜醬 泡菜 宮保 麻辣 拉差 有義大利麵 貝殼麵 螺旋麵 通心 尖管麵 蝴蝶麵 直面 煲飯和手工麵等 還有水餃以及炒飯 丼飯",
					"http://www.17life.com/media/JL-RA-3434/634605059479987500.jpg", "");
			addData("Ruby的咖哩廚房", "高雄市三民區建工路381號", "22.65247", "120.33022200000005", "建工", "午餐、晚餐",
					"營業時間週二至週日11:00–14:00、16:00–20:00", "無",
					"https://s-media-cache-ak0.pinimg.com/736x/05/80/f9/0580f94a096e44700e7e1b51a66f18d8.jpg",
					"");
			addData("輔英蛋包飯專賣店", "高雄市三民區立志街18號", "22.6519461", "120.32695949999993", "建工", "早餐", "無",
					"各式蛋包(炒)飯 小的最便宜20元 cp值還不錯 一般是上下學時段有開", "http://i.imgur.com/L4asA8F.jpg", "");
			addData("好運來", "高雄市三民區立志街22巷1號", "22.6519699", "120.32685019999997", "建工", "午餐、晚餐", "無",
					"飯麵好吃 大概50內", "http://i.imgur.com/3stAXyJ.jpg", "");
			addData("原鄉牛肉麵", "高雄市三民區大昌二路481號", "22.6505576", "120.33124610000004", "建工", "午餐、晚餐",
					"無", "丹丹漢堡旁邊水餃一顆3塊 爽",
					"http://ext.pimg.tw/jung9572002/4af22934ab6b443dc05654dd44c14d4c.jpg", "");
			addData("阿誠碳烤", "高雄市三民區大昌二路490號", "22.6510857", "120.33093800000006", "建工", "宵夜", "無",
					"阿誠碳烤價格平實，份量卻十足，吃起來有著淡淡木炭香", "無", "");
			addData("39焗烤家族", "高雄市三民區大昌二路551號", "22.6518354", "120.32972569999993", "建工", "午餐、晚餐",
					"無", "提供義大利麵、焗烤、比薩、異國咖哩排餐、雞腿堡餐、焗烤厚片、雞塊、薯條", "無", "");
			addData("燈籠滷味", "高雄市三民區大昌二路511號", "22.651125", "120.3304253", "建工", "午餐、晚餐", "無",
					"好吃的滷味", "無", "");
			addData("赤印牛排", "高雄市三民區明誠一路443號", "22.6602182", "120.32151010000007", "建工", "午餐、晚餐",
					"無", "平價但一點都不馬虎，麵粉經過拌炒後，加入碎肉及玉米，敖煮出濃濃奶香的玉米濃湯，還有爆漿餐包及古早味紅茶，都是店內的招牌",
					"http://ext.pimg.tw/julialkpkpk/1421747284-4071200878.jpg", "");
			addData("鮮焗家", "高雄市三民區大昌一路38號", "22.6531206", "120.32766670000001", "建工", "午餐", "無",
					"焗烤", "無", "");
			addData("幽靈香腸", "高雄市三民區河北二路130號", "22.635394", "120.294318", "建工", "宵夜", "無",
					"不錯吃，營業時間非常不固定，請賭運氣在2330～0000間到三鳳宮看看", "無", "");
			addData("胖叔叔拉麵店", "高雄市三民區正忠路502號", "22.6507438", "120.33093459999998", "建工", "午餐、晚餐",
					"無", "超級好吃的拉麵店", "無", "");
			addData("正中食堂", "高雄市三民區正忠路494號", "22.6506157", "120.33084280000003", "建工", "午餐、晚餐", "無",
					"超好吃的泰式拋肉飯 吃完總是讚不絕口", "無", "");
			addData("啊懷傳家河粉麵，飯", "高雄市三民區正忠路359號", "22.648126", "120.32986400000004", "建工", "午餐、晚餐",
					"無", "新開的店 河粉好吃！", "無", "");
			addData("廣九燒臘", "高雄市大社區中山路256號", "22.7317969", "120.34898099999998", "燕巢", "午餐、晚餐",
					"08:00-14:00、16:00-20:00", "(07) 3540970－專業送高應大便當店", "無", "073540970");
			addData("禾記嫩骨飯", "高雄市大社區中山路485號", "22.7310453", "120.35454589999995", "燕巢", "午餐、晚餐",
					"無", "(07) 354 2088", "無", "073542088");
			addData("真美香台日食堂", "高雄市大社區中山路410號", "22.7312672", "120.35266219999994", "燕巢", "午餐、晚餐",
					"無", "(07) 352 3772", "無", "073523772");
			addData("八方雲集", "高雄市燕巢區中民路704號", "22.7932222", "120.35931470000003", "燕巢", "午餐、晚餐", "無",
					"(07) 353 4868", "無", "073534868");
			addData("大社第一家土魠魚羹", "高雄市大社區中山路410號", "22.7312672", "120.35266219999994", "燕巢", "午餐、晚餐",
					"無", "0970 412 513 羹飯麵偏少更新45、55，服務好", "無", "0970412513");
			addData("味廚粥品／麵食", "高雄市大社區中山路124號", "22.7319874", "120.3468183", "燕巢", "午餐、晚餐", "無",
					"(07) 352 7192", "無", "073527192");
			addData("炒翻天", "高雄市大社區中山路410號", "22.7312672", "120.35266219999994", "燕巢", "午餐、晚餐", "無",
					"(07) 353 9530 炒飯是白色的、普普", "無", "073539530");
			addData("悟饕池上飯包", "高雄市燕巢區中民路586號", "22.7932965", "120.36244439999996", "燕巢", "午餐、晚餐",
					"無", "(07) 614 2977", "無", "076142977");
		}
	}

	private void addData(String name, String address, String lat, String lng, String region,
	                     String type, String time, String description, String image, String phone) {
		ContentValues cv = new ContentValues(10);
		cv.put(FORM[0], name);
		cv.put(FORM[1], address);
		cv.put(FORM[2], lat);
		cv.put(FORM[3], lng);
		cv.put(FORM[4], region);
		cv.put(FORM[5], type);
		cv.put(FORM[6], time);
		cv.put(FORM[7], description);
		cv.put(FORM[8], image);
		cv.put(FORM[9], phone);

		mDB.insert(TB_NAME, null, cv);
	}

	private void grantPermission() {
		PermissionUtils.checkPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION,
				PERMISSION_REQUEST_ACCESS_FINE_LOCATION, new PermissionCallback() {

					@Override
					public void onGranted() {
						checkGPS();
					}
				});
	}

	private void checkGPS() {
		LocationManager status =
				(LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
		if (!(status.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
				status.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
			Toast.makeText(this, R.string.gps_not_open, Toast.LENGTH_LONG).show();
			startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
					PERMISSION_REQUEST_GPS_SETTINGS);
		} else {
			startActivity();
		}
	}

	private void startActivity() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
				finish();
			}
		}, 2500);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
	                                       @NonNull int[] grantResults) {
		if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
			PermissionUtils.onRequestPermissionsResult(permissions, grantResults, this,
					new PermissionCallback() {

						@Override
						public void onGranted() {
							checkGPS();
						}

						@Override
						public void onDenied() {
							super.onDenied();

							checkGPS();

							Toast.makeText(SplashActivity.this, R.string.permission_request_fail,
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onAlwaysDenied() {
							super.onAlwaysDenied();

							PermissionUtils
									.showRequestPermissionRationaleDialog(SplashActivity.this, null,
											PERMISSION_REQUEST_SETTINGS,
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface dialog,
												                    int which) {
													Toast.makeText(SplashActivity.this,
															R.string.permission_request_fail,
															Toast.LENGTH_SHORT).show();
													checkGPS();
												}
											});
						}
					});
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PERMISSION_REQUEST_SETTINGS) {
			if (PermissionUtils
					.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
				checkGPS();
			} else {
				Toast.makeText(this, R.string.permission_request_fail, Toast.LENGTH_SHORT).show();
				checkGPS();
			}
		} else if (requestCode == PERMISSION_REQUEST_GPS_SETTINGS) {
			startActivity();
		}
	}
}
