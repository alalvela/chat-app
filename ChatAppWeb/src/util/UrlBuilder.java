package util;

public class UrlBuilder {

	public static String getUrl(String address, String appName, String classPathSegment, String method) {
		return "http://" + address + "/" + appName + "/rest/" + classPathSegment + "/" + method ;
	}
}
