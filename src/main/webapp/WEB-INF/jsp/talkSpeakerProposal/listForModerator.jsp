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
            <th scope="col">Speaker</th>
            <th scope="col">Created At</th>
            <th scope="col">Actions</th>
        </thead>
        <tbody>
            <c:forEach items="${talkSpeakerProposals}" var="talkProposal">
                <tr>
                    <td> <c:out value="${talkProposal.conferenceName}"/> </td>
                    <td>
                        <c:out value="${talkProposal.talkName}"/>
                    </td>
                    <td> <c:out value="${talkProposal.speakerName}"/> </td>
                    <td>
                        <mytl:formatDate value="${talkProposal.createdAt}" type="both" format="SHORT"  />
                    </td>
                    <td>
                        <mytags:deleteButton action="delete-talk-speaker-proposal"
                                             entityId="${talkProposal.id}"
                                             icon="trash" />
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
