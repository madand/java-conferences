<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>
    <c:set var="pageTitle" value="test" scope="request" />
    <%@ include file="/WEB-INF/jspf/head.jspf" %>

    <body>
        <%= request.getServletPath() %>
        <%= request.getQueryString() %>
    </body>
</html>
