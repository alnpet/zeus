<%@ page contentType="text/html; charset=utf-8" isELIgnored="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="w" uri="http://www.unidal.org/web/core"%>
<jsp:useBean id="ctx" type="com.alnpet.biz.payment.Context" scope="request" />
<jsp:useBean id="payload" type="com.alnpet.biz.payment.Payload" scope="request" />
<jsp:useBean id="model" type="com.alnpet.biz.payment.Model" scope="request" />

<a:layout>
	<br>

	<w:errors>
		<w:error code="order.notFound">Order(\${order}) is not found!</w:error>
		<w:error code="*">Error(\${code}) occurred.<br>
		</w:error>
	</w:errors>

	<c:set var="order" value="${model.order}" />
	<pre>
	Order Details:
	- Coupon: ${order.coupon}
	- PayPal Button Id: ${order.paypalButtonId}
	- Price: ${order.price}
	</pre>
	
	Please go ahead for payment <a
		href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=${order.paypalButtonId}">HERE</a>
</a:layout>