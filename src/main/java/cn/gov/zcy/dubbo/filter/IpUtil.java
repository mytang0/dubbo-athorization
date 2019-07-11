package cn.gov.zcy.dubbo.filter;

import java.net.InetAddress;
import java.util.regex.Pattern;

/**
 * @author luming
 */
public final class IpUtil {

	private static final int INADDRSZ = 4;

	private static final String  LOCALHOST_IP = "127.0.0.1";
	private static final String  EMPTY_IP     = "0.0.0.0";

	private static final Pattern IP_PATTERN   = Pattern.compile("[0-9]{1,3}(\\.[0-9]{1,3}){3,}");

	public static boolean validIp(String ip) {
		try {
			if (ip == null || !IP_PATTERN.matcher(ip).matches()) {
				return false;
			}
			InetAddress inetAddress = InetAddress.getByName(ip);
			if (inetAddress == null || inetAddress.isLoopbackAddress()) {
				return false;
			}
			String name = inetAddress.getHostAddress();
			return (name != null && !EMPTY_IP.equals(name) && !LOCALHOST_IP.equals(name));
		} catch (Exception ex) {
			//
		}
		return false;
	}

	public static int getIntAddress(String ip) {
		try {
			if (validIp(ip)) {
				InetAddress inetAddress = InetAddress.getByName(ip);
				return inetAddress.hashCode();
			}
		} catch (Exception ex) {
			//
		}
		throw new RuntimeException(new StringBuilder(64)
				.append("[")
				.append(ip)
				.append("] is invalid ip")
				.toString());
	}

	public static String getAddress(int address) {
		byte[] addr = new byte[INADDRSZ];
		addr[0] = (byte) ((address >>> 24) & 0xFF);
		addr[1] = (byte) ((address >>> 16) & 0xFF);
		addr[2] = (byte) ((address >>> 8) & 0xFF);
		addr[3] = (byte) (address & 0xFF);
		return addr[0]+ "." + addr[1] + "." + addr[2] + "." + addr[3];
	}
}
