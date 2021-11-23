<%@ page isErrorPage="true" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

    <%@ include file="/WEB-INF/jspf/head.jspf" %>

    <body>
        <table id="main-container">

            <tr >
                <td class="content">
                    <%-- CONTENT --%>

                    <h2 class="error">
                        The following error occurred
                    </h2>

                    <h3>${pageContext.errorData.statusCode}</h3>
                    <h3>${pageContext.errorData.requestURI}</h3>
                    <h3>${fn:escapeXml(requestScope['javax.servlet.error.message'])}</h3>

                    <h3>${fn:escapeXml(pageContext.exception)}</h3>

                    <p>
                        <c:forEach var="stackTraceEl" items="${pageContext.exception.stackTrace}">
                            <c:out value="${stackTraceEl}" /><br/>
                        </c:forEach>
                    </p>

                    <%-- CONTENT --%>
			          </td>
		        </tr>
	      </table>

	      <%@ include file="/WEB-INF/jspf/footer.jspf"%>
    </body>
</html>
