<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="talkProposal.listForSpeaker.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="row row-cols-1">
    <table class="table table-striped">
        <thead>
            <th scope="col">Conference</th>
            <th scope="col">Talk Name</th>
            <th scope="col">Actions</th>
        </thead>
        <tbody>
            <c:forEach items="${proposals}" var="talk">
                <tr>
                    <td> <c:out value="${talk.conference.name}"/> </td>
                    <td>
                        <c:out value="${talk.name}"/>
                        <br/>
                        (${talk.duration} <fmt:message key="general.minutes"/>)
                    </td>
                    <td>
                        <mytags:actionButton action="view-talk-proposal"
                                             entityId="${talk.id}"
                                             buttonType="primary"
                                             messageKey="button.review"
                                             icon="eye" />
                        <mytags:deleteButton action="delete-talk-proposal"
                                             entityId="${talk.id}"
                                             icon="trash" />
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>