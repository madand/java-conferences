<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="user.edit.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="actions">
    <mytags:actionButton action="change-password"
                         entityId="${user.id}"
                         buttonType="primary"
                         messageKey="user.changePassword.title"
                         icon="cursor-text" />
    <mytags:deleteButton action="delete-user"
                         entityId="${user.id}"
                         icon="trash" />
</div>

<form method="post" action="${requestScope['javax.servlet.forward.request_uri']}">
    <div class="row">
        <div class="col">
            <mytags:inputText name="email" labelKey="user.label.email"
                              type="email"
                              value="${bean.email}" required="true" />
        </div>
    </div>
    <div class="row">
        <div class="col">
            <mytags:inputText name="realName" labelKey="user.label.realName"
                              type="text"
                              value="${bean.realName}" required="true" />
        </div>
    </div>

    <div class="row">
        <div class="col">
            <mytags:buttonSubmit labelKey="button.save" />
        </div>
    </div>
</form>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
