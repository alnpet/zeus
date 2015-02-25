<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"
	trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="w" uri="http://www.unidal.org/web/core"%>
<jsp:useBean id="ctx" type="com.alnpet.biz.payment.Context"
	scope="request" />
<jsp:useBean id="payload" type="com.alnpet.biz.payment.Payload"
	scope="request" />
<jsp:useBean id="model" type="com.alnpet.biz.payment.Model"
	scope="request" />

<a:layout>
	<br>

	<w:errors>
		<w:error code="order.notFound">Order(\${order}) is not found!</w:error>
		<w:error code="*">Error(\${code}) occurred.<br>
		</w:error>
	</w:errors>

	
</a:layout>