<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@include file="/WEB-INF/jsp/common/header.jsp"%>
<%@include file="/WEB-INF/jsp/common/left.jsp"%>
<!-- 본문내용 작성 -->
<style type="text/css">
#feed li {
	background: #D9E5FF;
	border: solid #6799FF 1px;
	border-radius: 5px;
	margin: 0 0 10px 0;
	padding: 20px;
	overflow: hidden;
}

#feed li span {
	float: left;
	width: 846px;
}

#feed_createdTime, #feed_message {
	background: #fff;
	/* border: solid #6799FF 1px; */
	border-radius: 5px;
	margin: 0 0 10px 0;
	padding: 10px;
	overflow: hidden;
	width: 900px;
	float: left;
}

#feed_picture img {
	height: 300px;
}
#writer {
	float: left;
	overflow: hidden;
	display: block;
}
#date {
	color: #BDBDBD;
}
</style>

<div id="content">
	<ul id="feed">
		<c:forEach var="feed" items="${feed}">
			<li>
				<%-- <span id="feed_id">${feed.id}</span> --%>
				<span id="feed_createdTime"><div id="writer">作成 : ${feed.from.name}<p id="date"> : ${feed.updatedTime}</p></div></span><!-- 작성자 이름 -->
				<!-- <span id="feed_createdTime"></span> --><!-- 업데이트 날짜 -->
				<c:if test="${feed.message != null}">
					<span id="feed_message">${feed.message}</span>
				</c:if>
				<%-- <span id="feed_createdTime">${feed.name}</span> --%><!-- 게시물 이름 -->
				<%-- <span id="feed_createdTime">${feed.application.link}</span> --%>
				<%-- <span id="feed_createdTime">${feed.caption}</span> --%>
				<%-- <span id="feed_createdTime">${feed.description}</span> --%>
				<%-- <iframe width="560" height="315" src="${feed.link}" frameborder="0" allowfullscreen></iframe> --%>
				<c:if test='${feed.source != null && feed.application.name == "YouTube"}'>
				<iframe width="560" height="315" src="${feed.source}" frameborder="0" allowfullscreen></iframe>
				</c:if>
				<c:if test='${feed.source != null && feed.application.name != "YouTube"}'>
				<video width="400" controls>
				<source src="${feed.source}" type="video/mp4">
				<source src="${feed.source}" type="video/ogg">
				</video>
				</c:if>
				
				<c:if test="${feed.picture != null && feed.source == null}">
					<span id="feed_picture">
					<img src="${feed.picture}" />
					<%-- <img src="${feed.fullPicture}" /> --%>
					<%-- <img src="${feed.icon}" /> --%>
					</span>
				</c:if>
				
			</li>
		</c:forEach>
	</ul>
</div>

<!-- 본문 내용 끝 -->

<%@include file="/WEB-INF/jsp/common/footer.jsp"%>