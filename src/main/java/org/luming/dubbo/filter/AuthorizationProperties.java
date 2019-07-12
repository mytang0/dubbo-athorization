package org.luming.dubbo.filter;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luming
 */
@ConfigurationProperties(prefix = "dubbo.provider.authorization")
public class AuthorizationProperties {
	/**
	 * 是否启用授权
	 */
	private boolean enable;

	/**
	 * 将 IP 名单用作黑名单
	 */
	private boolean usedAsBlacklist;

	/**
	 * 全局 IP 名单
	 */
	private Set<String> ipList = new ConcurrentHashSet<>();

	/**
	 * 全局 IP 名单，为了快速匹配储存整形值
	 */
	private Set<Integer> ipFastList = new ConcurrentHashSet<>();

	/**
	 * 指定接口名单
	 * <p>
	 * example:
	 * dubbo.provider.authorization.interfaceIpList[cn.gov.zcy.credit.service.evaluate.IActorConfigService].usedAsBlacklist=true
	 * dubbo.provider.authorization.interfaceIpList[cn.gov.zcy.credit.service.evaluate.IActorConfigService].ipList=127.1.1.2,127.1.1.3
	 */
	private Map<String, IpList> interfaceIpList = new ConcurrentHashMap<>();

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

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

	public Map<String, IpList> getInterfaceIpList() {
		return interfaceIpList;
	}

	public void setInterfaceIpList(Map<String, IpList> interfaceIpList) {
		this.interfaceIpList.clear();
		if (interfaceIpList != null && !interfaceIpList.isEmpty()) {
			interfaceIpList.entrySet().forEach(entry -> {
				if (entry != null && entry.getKey() != null
						&& entry.getValue() != null
						&& entry.getValue().getIpList() != null
						&& !entry.getValue().getIpList().isEmpty()) {
					this.interfaceIpList.put(entry.getKey(), entry.getValue());
				}
			});
		}
	}
}
