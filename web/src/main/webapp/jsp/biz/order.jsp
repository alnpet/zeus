<%@ page contentType="text/html; charset=utf-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld" %>
<%@ taglib prefix="w" uri="http://www.unidal.org/web/core" %>
<jsp:useBean id="ctx" type="com.alnpet.biz.order.Context" scope="request"/>
<jsp:useBean id="payload" type="com.alnpet.biz.order.Payload" scope="request"/>
<jsp:useBean id="model" type="com.alnpet.biz.order.Model" scope="request"/>

<a:layout>
   <br>
   
   <w:errors>
   	  <w:error code="email.invalid">Email(\${email}) is invalid!</w:error>
   	  <w:error code="coupon.invalid">Coupon(\${coupon}) is invalid!</w:error>
   	  <w:error code="*">Error(\${code}) occurred.<br></w:error>
   </w:errors>
   
</a:layout>