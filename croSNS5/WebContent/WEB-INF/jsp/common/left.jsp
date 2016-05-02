<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div id="side">
	<div id="sns">
		<div>
			<p class="heart">♥</p>
			<p class="name">${sessionObjects.facebook.name}様</p>
			<p class="wellcom">ようこそ</p>
			<p class="heart">♥</p>
		</div>
		<div>
			<c:if test="${sessionObjects.facebook.name == null}">
				<a href="/signin.do">Facebook Login</a>
			</c:if>
			<c:if test="${sessionObjects.facebook.name!= null}">
				<a href="/facebookLogOut.do">Facebook 連動中</a>
			</c:if>
		</div>
	</div>
</div>
