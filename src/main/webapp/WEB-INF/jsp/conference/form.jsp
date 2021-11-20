<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>


<c:url var="actionUrl" value="${requestScope['javax.servlet.forward.servlet_path']}">
    <c:param name="id" value="${conference.id}" />
</c:url>
<form method="post" action="${actionUrl}">
    <div class="row">
        <div class="col-4 mb-3">
            <jsp:useBean id="now" class="java.util.Date" />
            <fmt:formatDate var="minDate" value="${now}" pattern="yyyy-MM-dd"/>
            <label for="event_date" class="form-label">
                <fmt:message key="conference.label.eventDate"/>
            </label>
            <input name="eventDate" type="date"  id="event_date" value="${conference.eventDate}"
                   minz="${minDate}" class="form-control" required />
        </div>

        <c:if test="${empty isNewEntity}">
            <div class="col-4 md-3">
                <mytags:inputText name="actuallyAttendedCount"
                                  labelKey="conference.label.actuallyAttendedCount"
                                  entity="${conference}"
                                  required="${true}" />
            </div>
        </c:if>

    </div>

    <div class="row">
        <c:forEach items="${conference.translations}" var="translation">
            <c:set var="language" value="${translation.language}" />
            <c:set var="required" value="${translation.language == defaultLanguage}" />

            <div class="col-6 mb-3">
                <mytags:inputText name="name" labelKey="conference.label.name"
                                  entity="${translation}" language="${language}"
                                  required="${required}" />

                <mytags:textArea name="description" labelKey="conference.label.description"
                                 entity="${translation}" language="${language}"
                                 required="${required}" rows="10" />

                <mytags:textArea name="location" labelKey="conference.label.location"
                                 entity="${translation}" language="${language}"
                                 required="${required}" rows="3" />

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
