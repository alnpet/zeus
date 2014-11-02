<%@ page session="false" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="w" uri="http://www.unidal.org/web/core"%>

<h3 class="text-error">Release Notes</h3>
<table class='table table-striped table-bordered table-condensed'>
	<tr class="text-success">
		<th width="3%">Versions</th>
		<th width="87%">Description</th>
		<th width="10%">Release Date</th>
	</tr>
	<c:forEach var="release" items="${model.toc.releases}">
		<tr>
			<td rowspan="2">${release.version}</td>
			<td>${release.description}</td>
			<td>${w:format(release.date, 'yyyy-MM-dd')}</td>
		</tr>
		<tr>
			<td colspan="2">
				<br>
				<table class='table table-striped table-bordered table-condensed'>
					<tr class="text-success">
						<th width="90%">Features</th>
						<th width="10%">Date</th>
					</tr>
					<c:forEach var="change" items="${release.changes}">
						<tr>
							<td>${change.text}</td>
							<td>${w:format(change.date, 'yyyy-MM-dd')}</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</c:forEach>
</table>
