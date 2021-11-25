<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="talkProposal.review.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<c:url var="actionUrl" value="${requestScope['javax.servlet.forward.servlet_path']}">
    <c:param name="id" value="${talk.id}" />
</c:url>
<h3><c:out value="${conference.name}"/></h3>
<form method="post" action="${actionUrl}">
    <div class="row row-cols-1 row-cols-md-2 mb-3">
        <div class="col">
            <mytags:inputText name="startTime" type="time"
                              labelKey="talk.label.startTime"
                              entity="${talk}" required="true" />
            <mytags:durationDropDown name="duration"
                                     labelKey="talk.label.duration"
                                     value="${talk.duration}" required="true" />
        </div>
        <div class="col">
            <h4><fmt:message key="talkProposal.review.existingTalks"/></h4>
            <table class="table table-striped">
                <c:forEach items="${existingTalks}" var="talk">
                    <tr>
                        <td> <c:out value="${talk.name}"/> </td>
                        <td>
                            <mytl:formatDate value="${talk.startTime}" type="time"
                                             format="SHORT" locale="currentLanguage.code" />
                            -
                            <mytl:formatDate value="${talk.endTime}" type="time"
                                             format="SHORT" locale="currentLanguage.code" />

                        </td>
                        <td>
                            ${talk.duration} <fmt:message key="general.minutes"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>

    <div class="row">
        <c:forEach items="${talk.translations}" var="translation">
            <c:set var="language" value="${translation.language}" />
            <c:set var="required" value="${translation.language == defaultLanguage}" />

            <div class="col-lg-6 mb-3">
                <mytags:inputText name="name" labelKey="talk.label.name"
                                  entity="${translation}" language="${language}"
                                  required="${required}" />

                <mytags:textArea name="description" labelKey="talk.label.description"
                                 entity="${translation}" language="${language}"
                                 required="${required}" rows="10" />
            </div>
        </c:forEach>
    </div>

    <c:choose>
        <c:when test="${isNewEntity}">
            <%@ include file="/WEB-INF/jspf/form/buttonBlockCreate.jspf" %>
        </c:when>
        <c:otherwise>
            <%@ include file="/WEB-INF/jspf/form/buttonBlockEdit.jspf" %>
        </c:otherwise>
    </c:choose>

</form>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
