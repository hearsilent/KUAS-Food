package hearsilent.kuas.food.callback;

public abstract class PermissionCallback {

	public abstract void onGranted();

	public void onDenied() {

	}

	public void onAlwaysDenied() {

	}
}
