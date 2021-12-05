<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="user.changePassword.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="row">
    <div class="col-sm-4">
        <c:url var="actionUrl" value="${originalURIAndQueryString}" />
        <form method="post" action="${actionUrl}">
            <div class="row">
                <div class="col">
                    <mytags:inputText name="password" labelKey="user.label.password"
                                      type="password"
                                      value="" required="true" />
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <mytags:inputText name="passwordNew" labelKey="user.label.passwordNew"
                                      type="password"
                                      value="" required="true" />
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <mytags:inputText name="passwordNewRepeat" labelKey="user.label.passwordNewRepeat"
                                      type="password"
                                      value="" required="true" />
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <mytags:buttonSubmit labelKey="button.save" />
                </div>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
