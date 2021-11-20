<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="conference.edit.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<form method="post" action="${requestScope['javax.servlet.forward.request_uri']}">
    <c:set var="isEditForm"  value="${true}" scope="request" />
    <%@ include file="/WEB-INF/jsp/conference/form.jsp" %>

    <div class="row">
        <div class="col-12">
            <button class="btn btn-primary" type="submit" >
                <fmt:message key="form.button.edit"/>
            </button>
        </div>
    </div>
</form>


<%@ include file="/WEB-INF/jspf/footer.jspf" %>
