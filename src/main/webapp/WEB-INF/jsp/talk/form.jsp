<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:choose>
    <c:when test="${isNewEntity}">
        <c:url var="actionUrl" value="${actionUrl}">
            <c:param name="id" value="${conference.id}" />
        </c:url>
    </c:when>
    <c:otherwise>
        <c:url var="actionUrl" value="${actionUrl}">
            <c:param name="id" value="${talk.id}" />
        </c:url>
    </c:otherwise>
</c:choose>

<form method="post" action="${actionUrl}">
    <div class="row">
        <div class="col-4 mb-3">
            <mytags:speakersDropDown name="speakerId"
                                     labelKey="talk.label.speaker"
                                     speakersList="${speakersList}"
                                     value="${talk.speaker}" required="true" />
        </div>
    </div>
    <div class="row">
        <div class="col-4 mb-3">
            <mytags:inputText name="startTime" type="time"
                              labelKey="talk.label.startTime"
                              entity="${talk}" required="true" />
        </div>
    </div>
    <div class="row">
        <div class="col-4 mb-3">
            <mytags:durationDropDown name="duration"
                                     labelKey="talk.label.duration"
                                     value="${talk.duration}" required="true" />
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
