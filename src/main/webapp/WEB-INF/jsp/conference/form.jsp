<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>
<%@ taglib uri="/WEB-INF/tld/custom.tld" prefix="mytl"%>

<c:set var="pageTitle" scope="request">
    <fmt:message key="conference.create.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<h1>${pageTitle}</h1>


<form method="post" action="${requestScope['javax.servlet.forward.request_uri']}">
    <div class="row">
        <div class="col-4 mb-3">
            <jsp:useBean id="now" class="java.util.Date" />
            <fmt:formatDate var="minDate" value="${now}" pattern="yyyy-MM-dd"/>
            <label for="event_date" class="form-label">
                <fmt:message key="conference.label.event_date"/>
            </label>
            <input name="eventDate" type="date"  id="event_date" value="${conference.eventDate}"
                   min="${minDate}" class="form-control" required />
        </div>
    </div>
    <div class="row">
        <c:forEach items="${translations}" var="trn">
            <c:set var="langCode" value="${trn.language.code}" />
            <c:set var="required" value="${trn.language == defaultLanguage}" />

            <div class="col-6 mb-3">

                <c:set var="label"><fmt:message key="conference.label.name"/></c:set>
                <c:set var="name" value="${mytl:localizedParamName('name', trn.language)}" />
                <mytags:formInputText name="${name}" label="${label}" value="${trn.name}"
                                      langCode="${langCode}" required="${required}" />

                <c:set var="label"><fmt:message key="conference.label.description"/></c:set>
                <c:set var="name" value="description[${langCode}]" />
                <mytags:formTextArea name="${name}" label="${label}" value="${trn.description}"
                                     langCode="${langCode}" required="${required}"
                                     rows="5" />

                <c:set var="label"><fmt:message key="conference.label.location"/></c:set>
                <c:set var="name" value="location[${langCode}]" />
                <mytags:formTextArea name="${name}" label="${label}" value="${trn.location}"
                                     langCode="${langCode}" required="${required}"
                                     rows="3" />

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
