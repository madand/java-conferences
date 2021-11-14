<%@ page isErrorPage="true" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

    <%@ include file="/WEB-INF/jspf/head.jspf" %>

    <body>

        <%@ page import="java.util.Collections" %>
        <%@ page import="java.util.Collection" %>
        <%@ page import="javax.servlet.jsp.PageContext" %>

        <%
        Collection attributenNames = Collections.list(pageContext.getAttributeNamesInScope(PageContext.APPLICATION_SCOPE));
        out.print(attributenNames);out.print("<br><br>");
        Collection attributenNames2 = Collections.list(pageContext.getAttributeNamesInScope(PageContext.SESSION_SCOPE ));
        out.print(attributenNames2);out.print("<br><br>");
        Collection attributenNames3 = Collections.list(pageContext.getAttributeNamesInScope(PageContext.REQUEST_SCOPE));
        out.print(attributenNames3);out.print("<br><br>");
        Collection attributenNames4 = Collections.list(pageContext.getAttributeNamesInScope(PageContext.PAGE_SCOPE));
        out.print(attributenNames4);out.print("<br><br>");
        %>

	<table id="main-container">

		<tr >
			<td class="content">
			<%-- CONTENT --%>

				<h2 class="error">
					The following error occurred
				</h2>

        <h3>Status code: ${pageContext.errorData.statusCode}</h3>
        <h3>Request URI: ${pageContext.errorData.requestUri}</h3>
        <h3>Error Message: ${pageContext.exception.message}</h3>

			<%-- CONTENT --%>
			</td>
		</tr>
	</table>

	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>
