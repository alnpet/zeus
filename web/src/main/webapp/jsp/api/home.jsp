<%@ page contentType="text/html; charset=utf-8" isELIgnored="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="ctx" type="com.alnpet.api.home.Context" scope="request" />
<jsp:useBean id="payload" type="com.alnpet.api.home.Payload" scope="request" />
<jsp:useBean id="model" type="com.alnpet.api.home.Model" scope="request" />

<style>
.detailContent {
	margin-left: 20px;
}
</style>

<a:layout>

	<div class="row-fluid">
		<div class="span12">
			<div class="tabbable tabs-left " id="content">
				<!-- Only required for left/right tabs -->
				<ul class="nav nav-tabs well" style="text-align: right; margin-top: 30px;">
					<li id="home"><a href="?tab=home"><strong>Home</strong></a></li>
					<li id="release"><a href="?tab=release"><strong>Release Notes</strong></a></li>
					<li id="arch"><a href="?tab=arch"><strong>System Diagram</strong></a></li>
					<li id="usecase"><a href="?tab=usecase"><strong>User Scenarios</strong></a></li>
					<li id="api"><a href="?tab=api"><strong>API References</strong></a></li>
				</ul>
				<div class="tab-content">
					<br> <br>
					<c:choose>
						<c:when test="${payload.tab == 'arch'}">
							<%@ include file="home/arch.jsp"%>
						</c:when>
						<c:when test="${payload.tab == 'api'}">
							<%@ include file="home/api.jsp"%>
						</c:when>
						<c:when test="${payload.tab == 'usecase'}">
							<%@ include file="home/usecase.jsp"%>
						</c:when>
						<c:when test="${payload.tab == 'release'}">
							<%@ include file="home/release.jsp"%>
						</c:when>
						<c:otherwise>
							<br>Welcome to ALNPet Home page!
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
	<br>

	<script>
		var tab = $('#${payload.tab}');

		if (tab.size() == 0) {
			tab = $('#home');
		}

		tab.addClass('active');
	</script>

</a:layout>