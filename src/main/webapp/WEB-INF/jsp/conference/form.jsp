<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="conference.create.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<form method="post" action="${requestScope['javax.servlet.forward.request_uri']}">
    <div class="row">
        <div class="col-4 mb-3">
            <jsp:useBean id="now" class="java.util.Date" />
            <fmt:formatDate var="minDate" value="${now}" pattern="yyyy-MM-dd"/>
            <label for="event_date" class="form-label">
                <fmt:message key="conference.label.event_date"/>
            </label>
            <input name="eventDate" type="date"  id="event_date" value="${conference.eventDate}"
                   minz="${minDate}" class="form-control" required />
        </div>
    </div>
    <div class="row">
        <c:forEach items="${translations}" var="trn">
            <c:set var="language" value="${trn.language}" />
            <c:set var="required" value="${trn.language == defaultLanguage}" />

            <div class="col-6 mb-3">
                <mytags:inputText name="name" labelKey="conference.label.name"
                                  value="${trn.name}" language="${language}"
                                  required="${required}" />

                <mytags:textArea name="description" labelKey="conference.label.description"
                                 value="${trn.description}" language="${language}"
                                 required="${required}" rows="5" />

                <mytags:textArea name="location" labelKey="conference.label.location"
                                 value="${trn.location}" language="${language}"
                                 required="${required}" rows="3" />

            </div>
        </c:forEach>
    </div>

    <div class="row">
        <div class="col-12">
            <button class="btn btn-primary" type="submit" >
                <fmt:message key="form.button.save"/>
            </button>
        </div>
    </div>
</form>


<%@ include file="/WEB-INF/jspf/footer.jspf" %>
