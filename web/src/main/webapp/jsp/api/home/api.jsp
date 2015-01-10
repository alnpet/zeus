<%@ page session="false" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="w" uri="http://www.unidal.org/web/core"%>

<h3 class="text-error">API References</h3>
<table class='table table-striped table-bordered table-condensed'>
	<c:forEach var="api" items="${model.toc.apis}">
		<tr>
			<th class="text-success">API</th>
			<td colspan="4">${api.name}</td>
		</tr>
		<tr>
			<th class="text-success">Description</th>
			<td colspan="4">${api.description}</td>
		</tr>
		<tr>
			<th class="text-success">Syntax</th>
			<td colspan="4">${model.webapp}${api.syntax}</td>
		</tr>
		<c:if test="${not empty api.options}">
			<tr>
				<th class="text-success" rowspan="${w:size(api.options)+2}">Options</th>
			</tr>
			<tr>
				<th>Name</th>
				<th>Type</th>
				<th>Optional</th>
				<th>Description</th>
			</tr>
			<c:forEach var="option" items="${api.options}">
				<tr>
					<td>${option.name}</td>
					<td>${option.type}</td>
					<td>${option.optional}</td>
					<td>${option.text}</td>
				</tr>
			</c:forEach>
		</c:if>
		<tr>
			<th class="text-success" rowspan="${w:size(api.samples)*2+1}">Samples</th>
		</tr>
		<c:forEach var="sample" items="${api.samples}">
			<tr>
				<th class="text-success">Request</th>
				<td colspan="3"><pre>${sample.request}</pre></td>
			</tr>
			<tr>
				<th class="text-success">Response</th>
				<td colspan="3"><pre>${w:htmlEncode(sample.response)}</pre></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="5" style="background-color: white">&nbsp;</td>
		</tr>
	</c:forEach>
</table>
