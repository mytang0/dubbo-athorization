package cn.gov.zcy.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

/**
 * 权限控制Filter
 *
 * @author luming
 */
@Slf4j
@Activate(group = {Constants.PROVIDER})
public class AuthorizationFilter implements Filter {

	private static AuthorizationProperties authorizationProperties;

	public static void setAuthorizationProperties(AuthorizationProperties authorizationProperties) {
		AuthorizationFilter.authorizationProperties = authorizationProperties;
		log.info("AuthorizationFilter injection authorizationProperties ok");
	}

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		if (authorizationProperties != null && authorizationProperties.isEnable()) {
			RpcContext rpcContext = RpcContext.getContext();
			if (!hasAuthorization(invoker, invocation)) {
				throw new RpcException(
						new StringBuilder(64)
								.append("Failed to invoke service ")
								.append(invoker.getInterface().getName())
								.append(".")
								.append(invocation.getMethodName())
								.append(" because [")
								.append(rpcContext.getRemoteHostName())
								.append("] No access.")
								.toString());
			}
		}
		return invoker.invoke(invocation);
	}

	private boolean hasAuthorization(Invoker<?> invoker, Invocation invocation) {
		boolean result;
		RpcContext rpcContext = RpcContext.getContext();
		Map<String, IpList> interfaceIpList = authorizationProperties.getInterfaceIpList();

		try {
			//接口配置优先
			if (interfaceIpList != null && !interfaceIpList.isEmpty()) {
				IpList ipList = interfaceIpList.get(invocation.getMethodName());
				if (ipList != null && ipList.getIpFastList() != null && !ipList.getIpFastList().isEmpty()) {
					result = ipList.getIpFastList().contains(rpcContext.getRemoteAddress().getAddress().hashCode());

					return ipList.isUsedAsBlacklist() == !result;
				}
			}
			//检查全局配置
			Set<Integer> ipFastList = authorizationProperties.getIpFastList();
			if (ipFastList != null && !ipFastList.isEmpty()) {
				result = ipFastList.contains(rpcContext.getRemoteAddress().getAddress().hashCode());

				return authorizationProperties.isUsedAsBlacklist() == !result;
			}
		} catch (Exception ex) {
			//异常不能影响正常流程
		}
		return true;
	}
}
