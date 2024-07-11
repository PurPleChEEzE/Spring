package kr.co.green.common.interceptor;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AccessInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String referer = request.getHeader("referer");
		String requestURI = request.getRequestURI();

		String serverAddress = request.getRequestURL().toString();
		String localServerAddress = serverAddress.replace(requestURI, "");

		System.out.println("referer : " + referer);
		System.out.println("requestURI : " + requestURI);
		System.out.println("serverAddress : " + serverAddress);
		System.out.println("localServerAddress : " + localServerAddress);

		// 1. 요청받은 URL이 /free/detail.do일때
		// 2. 주소창에 직접 URL을 요청했을때 ( referer == null부분 )
		// 3. http:localhost/free/list.do가 아닌 다른곳에서 URL을 요청했을 때
//		if (requestURI.equals("/free/detail.do")
//				&& (referer == null || !referer.startsWith(localServerAddress + "/free/list.do"))) {
//
//			response.sendRedirect("/error/accessDenied?referer=" + referer);
//			return false;
//		}
//		if (requestURI.equals("/news/detail.do")
//				&& (referer == null || !referer.startsWith(localServerAddress + "/news/list.do")
//						&& !referer.startsWith(localServerAddress + "/news/editForm.do"))) {
//
//			response.sendRedirect("/error/accessDenied?referer=" + referer);
//			return false;
//		}
//
//		if (requestURI.equals("/news/enrollForm.do")
//				&& (referer == null || !referer.startsWith(localServerAddress + "/news/list.do"))) {
//
//			response.sendRedirect("/error/accessDenied?referer=" + referer);
//			return false;
//		}
//		if (requestURI.equals("/news/enroll.do")
//				&& (referer == null || !referer.startsWith(localServerAddress + "/news/enrollForm.do"))) {
//
//			response.sendRedirect("/error/accessDenied?referer=" + referer);
//			return false;
//		}
//
//		if (requestURI.equals("/news/editForm.do")
//				&& (referer == null || !referer.startsWith(localServerAddress + "/news/detail.do"))) {
//
//			response.sendRedirect("/error/accessDenied?referer=" + referer);
//			return false;
//		}
//
////		if(requestURI.equals("/news/detail.do") && 
////				(referer == null || !referer.startsWith(localServerAddress+ "/news/editForm.do"))) {
////			
////			response.sendRedirect("/error/accessDenied?referer=" + referer);
////			return false;
////		}
//		if (requestURI.equals("/news/edit.do")
//				&& (referer == null || !referer.startsWith(localServerAddress + "/news/editForm.do"))) {
//
//			response.sendRedirect("/error/accessDenied?referer=" + referer);
//			return false;
//		}
//
//		if (requestURI.equals("/news/delete.do")
//				&& (referer == null || !referer.startsWith(localServerAddress + "/news/detail.do"))) {
//
//			response.sendRedirect("/error/accessDenied?referer=" + referer);
//			return false;
//		}

		HashMap<String, String[]> requestMap = new HashMap<>();
		requestMap.put("/free/detail.do", new String[]{"/free/list.do", "/free/editForm.do", "/"});
		requestMap.put("/free/enroll.do", new String[]{"/free/enrollForm.do"});
		requestMap.put("/free/editForm.do", new String[]{"/free/detail.do"});
		requestMap.put("/free/edit.do", new String[]{"/free/editForm.do"});
		requestMap.put("/free/delete.do", new String[]{"/free/detail.do"});
		requestMap.put("/news/detail.do", new String[]{"/news/list.do", "/news/editForm.do", "/"});
		requestMap.put("/news/enroll.do", new String[]{"/news/enrollForm.do"});
		requestMap.put("/news/editForm.do", new String[]{"/news/detail.do"});
		requestMap.put("/news/edit.do", new String[]{"/news/editForm.do"});
		requestMap.put("/news/delete.do", new String[]{"/news/detail.do"});
		requestMap.put("/member/register.do", new String[]{"/member/registerForm.do"});
		boolean checkPathResult = true;
		
		for (String key : requestMap.keySet()) {
			boolean result = pathCheck(requestURI, referer, localServerAddress, key, requestMap.get(key));
			if(!result) {
				response.sendRedirect("/error/accessDenied?referer="+referer);
				checkPathResult = false;
				break;
			}
		}
		return checkPathResult;

		// return true : 컨트롤러를 호출하겠다.
		// return false : 컨트롤러를 호출하지 않겠다.
	}

	private boolean pathCheck(String requestURI, 
							String referer, 
							String localServerAddress, 
							String requestMain, 
							String[] requestSub) throws IOException {
		boolean invalidReferer = true;
		
		if(requestURI.equals(requestMain)){
			if(referer==null) {
				invalidReferer = false;
			}else {
				for(String value : requestSub) {
					if(!referer.startsWith(localServerAddress+value)) {
						invalidReferer = false;
					}else {
						invalidReferer = true;
						break;
					}
				}
			}
		}
		
		return invalidReferer;
		
//		if (requestURI.equals(requestMain)
//				&& (referer == null || !referer.startsWith(localServerAddress + requestSub))) {
//			return 1;
//		}
//		return 0;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {

	}
}
