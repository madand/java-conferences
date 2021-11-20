<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

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
    <c:if test="${isEditForm}">
        <input type="hidden" name="id"  value="${conference.id}" />
        <div class="col-4 md-3">
            <mytags:inputText name="actually_attended_count"
                              labelKey="conference.label.actuallyAttendedCount"
                              value="${conference.actuallyAttendedCount}"
                              required="${true}" />
        </div>
    </c:if>
</div>
<div class="row">
    <c:forEach items="${conference.translations}" var="trn">
        <c:set var="language" value="${trn.language}" />
        <c:set var="required" value="${trn.language == defaultLanguage}" />

        <div class="col-6 mb-3">
            <mytags:inputText name="name" labelKey="conference.label.name"
                              value="${trn.name}" language="${language}"
                              required="${required}" />

            <mytags:textArea name="description" labelKey="conference.label.description"
                             value="${trn.description}" language="${language}"
                             required="${required}" rows="10" />

            <mytags:textArea name="location" labelKey="conference.label.location"
                             value="${trn.location}" language="${language}"
                             required="${required}" rows="3" />

        </div>
    </c:forEach>
</div>
