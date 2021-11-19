<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>


<%@ page import="net.madand.conferences.web.util.URLManager" %>
<%@ page import="net.madand.conferences.entity.Conference" %>

<c:set var="pageTitle" scope="request">
    <c:out value="${conference.name}" />
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="row">
    <div class="col mb-4">
        <p class="text-muted event-date">
            <mytl:formatDate value="${conference.eventDate}" format="LONG"
                             locale="${currentLanguage.code}" />
        </p>
        <div class="text-muted event-location">
            ${mytl:linesToParagraphs(conference.location)}
        </div>
        ${mytl:linesToParagraphs(conference.description)}
    </div>
</div>

${originalURI}
<div class="row row-cols-1">
    <div class="col">
        <h2><fmt:message key="talk.list.subtitle"/></h2>
        <table class="table">
            <thead>
                <tr>
                    <th scope="col"><fmt:message key="talk.list.start"/></th>
                    <th scope="col"><fmt:message key="talk.list.name"/></th>
                    <th scope="col"><fmt:message key="talk.list.speaker"/></th>
                    <th scope="col"><fmt:message key="talk.list.description"/></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${talks}" var="talk">
                    <tr>
                        <td>
                            <mytl:formatDate value="${talk.startTime}" type="time" format="SHORT" />
                            <br/>
                            <small>
                                <nobr>(${talk.duration} <fmt:message key="general.minutes"/>)</nobr>
                            </small>
                        </td>
                        <td>
                            <nobr>
                                <c:out value="${talk.name}" />
                            </nobr>
                        </td>
                        <td>
                            <nobr>
                            <c:out value="${talk.speaker.realName}" />
                        </nobr>
                        </td>
                        <td>${mytl:linesToParagraphs(talk.description)}</td>
                    </tr>
                </c:forEach>

            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
