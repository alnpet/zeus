<%@ page contentType="text/html; charset=utf-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="ctx" type="com.alnpet.ajax.newsletter.Context" scope="request"/>
<jsp:useBean id="payload" type="com.alnpet.ajax.newsletter.Payload" scope="request"/>
<jsp:useBean id="model" type="com.alnpet.ajax.newsletter.Model" scope="request"/>

<a:layout>
	<br>
	<b>Newsletter subscription:</b>
	<ul>
	<c:forEach var="user" items="${model.users}">
		${user.name}(${user.email}) at ${user.creationDate}<br>
	</c:forEach>
	</ul>
</a:layout>