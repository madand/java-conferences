<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="talk.speaker.propose.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<c:url var="actionUrl" value="${requestScope['javax.servlet.forward.servlet_path']}">
    <c:param name="id" value="${talk.id}" />
</c:url>
<form method="post" action="${actionUrl}">
    <div class="row">
        <div class="col-4 mb-3">
            <b><fmt:message key="talk.label.name"/></b>:
            <c:out value="${talk.name}"/>
        </div>
    </div>
    <div class="row">
        <div class="col-4 mb-3">
            <mytags:speakersDropDown name="speakerId"
                                     labelKey="talk.label.speaker"
                                     speakersList="${speakersList}"
                                     value="${talk.speaker}" required="true" />
        </div>
    </div>
    <div class="row">
        <div class="col-12">
            <button class="btn btn-primary" type="submit" >
                <i class="bi-file-earmark-plus"></i>
                <fmt:message key="button.submit"/>
            </button>
        </div>
    </div>
</form>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
