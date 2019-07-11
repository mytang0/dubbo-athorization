package cn.gov.zcy.dubbo.filter;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;

import java.util.Set;

/**
 * @author luming
 */
public class IpList {
	/**
	 * 将 IP 名单用作黑名单，默认是白名单
	 */
	private boolean usedAsBlacklist = false;

	/**
	 * IP 名单
	 */
	private Set<String> ipList = new ConcurrentHashSet<>();

	/**
	 * IP 名单，为了快速匹配储存整形值
	 */
	private Set<Integer> ipFastList = new ConcurrentHashSet<>();

	public boolean isUsedAsBlacklist() {
		return usedAsBlacklist;
	}

	public void setUsedAsBlacklist(boolean usedAsBlacklist) {
		this.usedAsBlacklist = usedAsBlacklist;
	}

	public Set<String> getIpList() {
		return ipList;
	}

	public void setIpList(Set<String> ipList) {
		this.ipList.clear();
		ipList.stream().filter(IpUtil::validIp)
				.forEach(ip -> {
					this.ipList.add(ip);
					this.ipFastList.add(IpUtil.getIntAddress(ip));
				});
	}

	public Set<Integer> getIpFastList() {
		return ipFastList;
	}
}