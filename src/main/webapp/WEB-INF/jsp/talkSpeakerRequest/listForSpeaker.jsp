<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="talk.speaker.request.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="row row-cols-1">
    <table class="table table-striped">
        <thead>
            <th scope="col">Conference</th>
            <th scope="col">Talk Name</th>
            <th scope="col">Created At</th>
            <th scope="col">Actions</th>
        </thead>
        <tbody>
            <c:forEach items="${talkSpeakerRequests}" var="talkRequest">
                <tr>
                    <td> <c:out value="${talkRequest.conferenceName}"/> </td>
                    <td>
                        <c:out value="${talkRequest.talkName}"/>
                    </td>
                    <td>
                        <mytl:formatDate value="${talkRequest.createdAt}" type="both" format="MEDIUM" />
                    </td>
                    <td>
                        <mytags:deleteButton action="delete-talk-speaker-request"
                                             entityId="${talkRequest.id}"
                                             icon="trash" />
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
